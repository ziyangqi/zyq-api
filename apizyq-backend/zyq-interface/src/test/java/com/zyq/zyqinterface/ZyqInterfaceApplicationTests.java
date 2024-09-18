package com.zyq.zyqinterface;
import com.zyq.zyqapiclientsdk.client.ZyqApiClient;
import com.zyq.zyqapiclientsdk.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class ZyqInterfaceApplicationTests {

    @Resource
    private ZyqApiClient zyqApiClient;


    @Test
    void contextLoads(){
        String result = zyqApiClient.getNameByGet("zyq");
        User user = new User();
        user.setUsername("qzy");
        String userNameByPost = zyqApiClient.getUserNameByPost(user);
        System.out.println(result);
        System.out.println(userNameByPost);


    }



}
