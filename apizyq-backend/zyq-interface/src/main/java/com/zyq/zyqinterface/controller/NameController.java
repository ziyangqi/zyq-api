package com.zyq.zyqinterface.controller;

import com.zyq.zyqapiclientsdk.model.User;
import com.zyq.zyqapiclientsdk.utils.SignUtils;
import com.zyq.zyqapicommon.service.InnerUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * Name controller 表
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name){
       return "Get 姓名是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "Post 姓名是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){
//        // 从请求头中获取accessKey和SecretKey
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        String timestamp = request.getHeader("timestamp");
//
//        com.zyq.zyqapicommon.model.entity.User invokeUser = innerUserService.getInvokeUser(accessKey);
//        String secretKey = invokeUser.getSecretKey();
//        String accessKeyInvoker = invokeUser.getAccessKey();
//        // 从数据库中查询accessKey
//        // 检查accessKey和SecretKey是否匹配
//        if (!Objects.equals(accessKeyInvoker, accessKey)) {
//            throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce) > 10000){
//            throw new RuntimeException("无权限");
//        }
//        // 校验时间戳和当前的时间的差距 if (timestamp);
//        String serverSign = SignUtils.genSign(body,secretKey);
//        if  (!sign.equals(serverSign)){
//            throw new RuntimeException("无权限");
//        }
        return "Post 姓名是" + user.getUsername();
    }
}
