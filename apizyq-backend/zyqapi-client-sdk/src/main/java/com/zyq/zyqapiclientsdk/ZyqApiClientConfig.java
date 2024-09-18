package com.zyq.zyqapiclientsdk;


import com.zyq.zyqapiclientsdk.client.ZyqApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("zyqapi.client")
@ComponentScan
public class ZyqApiClientConfig {

    private String accessKey;

    private String secretKey;
    @Bean
    public ZyqApiClient zyqApiClient(){
        return new ZyqApiClient(accessKey,secretKey);
    }
}
