package com.yupi.project;

import com.zyq.zyqapiclientsdk.ZyqApiClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(ZyqApiClientConfig.class)
@EnableDubbo
@MapperScan("com.yupi.project.mapper")
public class MyApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(MyApplication.class, args);
        Environment environment = application.getEnvironment();
        // 获取本地Ip
        String ip = InetAddress.getLocalHost().getHostAddress();
        // 获取本地端口
        String port = environment.getProperty("server.port");
        // 获取本地路径
        String property = environment.getProperty("server.servlet.context-path");
        String path = StringUtils.isBlank(property) ? "" : property;
        log.info("\n----------------------------------------------------------\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "Swagger接口文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");


    }
}