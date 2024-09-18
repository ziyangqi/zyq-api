package com.zyq.zyqapicommon.service;



import com.zyq.zyqapicommon.model.entity.InterfaceInfo;

/**
* @author 114514191980
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-08-29 14:53:26
*/

public interface InnerInterfaceInfoService {

    /**
     * 从数据中查询模拟接口是否存在
     */

    InterfaceInfo getInterfaceInfo(String path, String method);

}
