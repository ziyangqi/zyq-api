package com.zyq.zyqinterface;

import com.zyq.zyqapiclientsdk.ZyqApiClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(ZyqApiClientConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.zyq.zyqapicommon", "com.zyq.zyqinterface"})
public class ZyqInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZyqInterfaceApplication.class, args);
    }

}
