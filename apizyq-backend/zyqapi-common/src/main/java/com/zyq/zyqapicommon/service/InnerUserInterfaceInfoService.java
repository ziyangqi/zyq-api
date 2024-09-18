package com.zyq.zyqapicommon.service;




/**
* @author 114514191980
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-09-05 10:15:32
*/

public interface InnerUserInterfaceInfoService {

    public boolean invokeCount(long interfaceInfoId , long userId);

}
