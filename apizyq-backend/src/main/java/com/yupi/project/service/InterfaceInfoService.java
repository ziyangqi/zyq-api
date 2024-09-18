package com.yupi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zyq.zyqapicommon.model.entity.InterfaceInfo;

/**
* @author 114514191980
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-08-29 14:53:26
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);


}
