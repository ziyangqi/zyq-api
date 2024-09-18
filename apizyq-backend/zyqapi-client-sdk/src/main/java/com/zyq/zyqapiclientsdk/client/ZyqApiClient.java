package com.zyq.zyqapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zyq.zyqapiclientsdk.model.User;
import java.util.HashMap;
import java.util.Map;
import static com.zyq.zyqapiclientsdk.utils.SignUtils.genSign;

/**
 * Description:调用第三方接口
 */
public class ZyqApiClient {


    private static final  String GATEWAY_HOST = "http://localhost:8090";
    private String accessKey ;

    private String secretKey ;

    public ZyqApiClient(String accessKey, String secretKey){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }



    // 新建一个发送的时候戴上请求头
        private Map<String, String> getHeaderMap(String body){
        Map<String,String>  hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
        // 一定不要直接发送
//        hashMap.put("secretKey",secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body",body);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign",genSign(body,secretKey));
        return hashMap;
    }


    // 使用Get方法从服务器获取名称信息
    public String getNameByGet(String name){
        HashMap<String,Object> paramMap =  new HashMap<>();
        paramMap.put("name",name);
        // 使用HttpClient发送请求
        String result = HttpUtil.get(GATEWAY_HOST+"/api/name/",paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost( String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        // 使用HttpClient发送请求
        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/",paramMap);
        System.out.println(result);
        return result;

    }

    // 使用Post方法向服务器发送User对象，并获取服务器返回结果
    public String getUserNameByPost( User user){
        // 把User转化为stringJson
        String json = JSONUtil.toJsonStr(user);
        // 使用HttpClient发送请求
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json) // 将JSON字符串设置为请求体
                .execute(); // 执行请求
        // 获取状态码
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        return result;
    }
}
