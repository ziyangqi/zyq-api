package com.zyq.zyqapicommon.service;


import com.zyq.zyqapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author yupi
 */

public interface InnerUserService {

    // 写两个方法
    // 1. 数据库中查询是否已经分配给用户密匙(accessKey)
    // 2. 从数据中查询模拟的接口是否存在(请求路径，请求方法，请求参数)

    /**
     * 数据库中查询密匙是否分配给用户(accessKey)
     * @param accessKey 密匙
     * @return
     */
    User getInvokeUser(String accessKey);

}
