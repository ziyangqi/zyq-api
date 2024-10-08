package com.yupi.zyqapigateway;
import com.zyq.zyqapiclientsdk.utils.SignUtils;
import com.zyq.zyqapicommon.model.entity.InterfaceInfo;
import com.zyq.zyqapicommon.model.entity.User;
import com.zyq.zyqapicommon.service.InnerInterfaceInfoService;
import com.zyq.zyqapicommon.service.InnerUserInterfaceInfoService;
import com.zyq.zyqapicommon.service.InnerUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 添加全局过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    // 建立访问白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");



    // 这里的注解不能用Resource 只能使用DubboReference
    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求的唯一标识:" + request.getId());
        log.info("请求路径:" +path);
        log.info("请求方法:"+method);
        log.info("请求参数:"+ request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址:" + sourceAddress);
        log.info("请求远程地址:+" +request.getRemoteAddress());
        // 2 黑白名单 todo 后面可以做成去配置文件读取的格式
         ServerHttpResponse  response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 用户鉴权
        // 从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        User invokeUser = null;

        try{
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            // 抛出异常记录日志
            log.error("getInvokeUser Error");
        }

        // 直接校验如果随机数大于1万，则抛出异常，并提示"无权限"
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }

        // todo 时间和当前时间不能超过5分钟
        // System.currentTimeMillis()返回当前时间的毫秒数，除以1000后得到当前时间的秒数。
        Long currentTime = System.currentTimeMillis() / 1000;
        // 定义一个常量FIVE_MINUTES,表示五分钟的时间间隔(乘以60,将分钟转换为秒,得到五分钟的时间间隔)。
        final Long FIVE_MINUTES = 60 * 5L;
        // 判断当前时间与传入的时间戳是否相差五分钟或以上
        // Long.parseLong(timestamp)将传入的时间戳转换成长整型
        // 然后计算当前时间与传入时间戳之间的差值(以秒为单位),如果差值大于等于五分钟,则返回true,否则返回false
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            // 如果时间戳与当前时间相差五分钟或以上，调用handleNoAuth(response)方法进行处理
            return handleNoAuth(response);
        }

        // 这里获取SecretKey
        String secretKey = invokeUser.getSecretKey();
        // todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, secretKey);

        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if ( sign==null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // chain.filter 是一个异步的操作*解决方案：利用 response 装饰者，增强原有 response 的处理能力
        // 4 请求模拟接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(path, method);
        if (interfaceInfo == null){
            return handleNoAuth(response);
        }
        // 5 请求转发调用模拟接口
        // 调用成功后输入一个响应的日志
        log.info("响应" + response.getStatusCode());
        // 调用失败返回一个失败的错误码
        return handleResponse(exchange , chain , interfaceInfo.getId() , invokeUser.getId());
    }


    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeErroe(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    /**
     * 处理相应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange,GatewayFilterChain chain,long interfaceId, long userId){
        try{
            //获得原始的相应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获得数据缓冲工厂
            DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();
            // 获取相应的状态码
            HttpStatusCode statusCode = originalResponse.getStatusCode();
            // 如果状态码为0k也就是可以正常的访问
            if (statusCode == HttpStatus.OK){
                // 创建一个装饰后的相应对象(开始增强能力)
                ServerHttpResponseDecorator decoratorResponse = new ServerHttpResponseDecorator(originalResponse){
                    // 重写writeWith方法，处理响应体的数据
                    // 这段方法就是当我们的模拟接口调用完成之后，等他返回结果
                    // 就会调用writeWith方法，我们就能根据相应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body){
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux){
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // 这里就理解为它在拼装字符串，它把缓冲区的数据提取出来，一点一点的拼接好
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                try{
                                    innerUserInterfaceInfoService.invokeCount(interfaceId,userId);
                                }
                                catch (Exception e){
                                    log.error("InvokeCount Error",e);
                                }
                                // 读取响应体的内容转化为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                // 释放内存
                                DataBufferUtils.release(dataBuffer);
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                // data
                                String data = new String(content, StandardCharsets.UTF_8);
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return dataBufferFactory.wrap(content);
                            }));
                        }else{
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200ok的请求，将装饰后的相应对象传递给下一个过滤器，并继续处理(设置response装饰过得)
                return chain.filter(exchange.mutate().response(decoratorResponse).build());
            }
            // 对于非200的请求，直接返回降级处理
            return chain.filter(exchange);
        }catch (Exception e){
            log.error("gateWay log exception.\n"+ e);
            return chain.filter(exchange);
        }
    }
}
