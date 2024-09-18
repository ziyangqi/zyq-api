package com.yupi.project.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyq.zyqapicommon.model.entity.InterfaceInfo;
import com.zyq.zyqapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 114514191980
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-09-05 10:15:32
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    public boolean invokeCount(long interfaceInfoId , long userId);
}
