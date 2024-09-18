package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.project.annotation.AuthCheck;
import com.yupi.project.common.*;
import com.yupi.project.constant.CommonConstant;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.yupi.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.yupi.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.yupi.project.service.UserInterfaceInfoService;
import com.yupi.project.service.UserService;
import com.zyq.zyqapiclientsdk.client.ZyqApiClient;
import com.zyq.zyqapicommon.model.entity.User;
import com.zyq.zyqapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/userUserInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    // 引入接口的实例
    @Resource
    private ZyqApiClient zyqApiClient;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceInfoService.validInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        Long id = userInterfaceInfoQuery.getId();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        // 根据Id精确查询
        queryWrapper.eq(id != null && id > 0,"id",id);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }




//    // endregion
//    @PostMapping("/online")
//    @AuthCheck(mustRole = "admin")
//    public BaseResponse<Boolean>onlineUserInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
//        // 如果id为空或者id < 0
//        if (idRequest == null || idRequest.getId() < 10) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 校验接口是否存在
//        long id = idRequest.getId();
//        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
//        if (oldUserInterfaceInfo == null){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//        // 判断该接口是否可以调用
//        com.zyq.zyqapiclientsdk.model.User user= new com.zyq.zyqapiclientsdk.model.User();
//        user.setUsername("test");
//        String userName = zyqApiClient.getUserNameByPost(user);
//        if (StringUtils.isBlank(userName)){
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
//        }
//        // 创建一个实体对象
//        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
//        userInterfaceInfo.setId(id);
//        // 修改属性
//        userInterfaceInfo.setStatus(UserInterfaceInfoStatusEnum.ONLINE.getValue());
//        // 调用结果
//        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
//        return ResultUtils.success(result);
//    }

//    // endregion
//    @PostMapping("/offline")
//    @AuthCheck(mustRole = "admin")
//    public BaseResponse<Boolean>offlineUserInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
//        // 如果id为空或者id < 0
//        if (idRequest == null || idRequest.getId() < 10) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 校验接口是否存在
//        long id = idRequest.getId();
//        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
//        if (oldUserInterfaceInfo == null){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//        // 创建一个实体对象
//        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
//        userInterfaceInfo.setId(id);
//        // 修改属性
//        userInterfaceInfo.setStatus(UserInterfaceInfoStatusEnum.OFFLINE.getValue());
//        // 调用结果
//        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
//        return ResultUtils.success(result);
//    }


//    /**
//     * 测试调用
//     * @param userInterfaceInfoInvokeRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/invoke")
//    @AuthCheck(mustRole = "admin")
//    public BaseResponse<Object>invokeUserInterfaceInfo(@RequestBody UserInterfaceInfoInvokeRequest userInterfaceInfoInvokeRequest, HttpServletRequest request) {
//        // 如果id为空或者id < 0
//        if (userInterfaceInfoInvokeRequest == null || userInterfaceInfoInvokeRequest.getId() < 10) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 校验接口是否存在
//        long id = userInterfaceInfoInvokeRequest.getId();
//        String userRequestParams = userInterfaceInfoInvokeRequest.getUserRequestParams();
//        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
//        if (oldUserInterfaceInfo == null){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//        //检查接口是否是下线的状态
//        if (oldUserInterfaceInfo.getStatus() == UserInterfaceInfoStatusEnum.OFFLINE.getValue()){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"接口已经下线了");
//        }
//
//        // 获取当前用户的信息 获取secretKey和accessKey
//        User loginUser = userService.getLoginUser(request);
//        String secretKey = loginUser.getSecretKey();
//        String accessKey = loginUser.getAccessKey();
//        ZyqApiClient zyqApiClient1 = new ZyqApiClient(accessKey,secretKey);
//        // 解析传递过来的参数
//        Gson gson = new Gson();
//        // 将用户请求的转化为User对象
//        com.zyq.zyqapiclientsdk.model.User user = gson.fromJson(userRequestParams,com.zyq.zyqapiclientsdk.model.User.class);
//        String userNameByPost = zyqApiClient.getUserNameByPost(user);
//        // 返回调用结果
//        return ResultUtils.success(userNameByPost);
//
//    }
}
