package com.yupi.project.service.impl;

import com.yupi.project.service.UserInterfaceInfoService;
import com.zyq.zyqapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class innerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {


    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
         // 调用以前写的Services来进行调用
         return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
