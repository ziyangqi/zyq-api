package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.UserInterfaceInfoMapper;
import com.yupi.project.service.UserInterfaceInfoService;
import com.zyq.zyqapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author 114514191980
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-09-05 10:15:32
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    // 验证是否是管理员
    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {

        if (userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        if (add){
            if (userInterfaceInfo.getInterfaceInfoId() <0 || userInterfaceInfo.getUserId() <0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或者用户的id不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() <=0 ){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"接口所剩余执行次数为0");
        }

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId < 0 || userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户id或者接口id小于0");
        }
        // 使用updateWrapper构建对象
        UpdateWrapper<UserInterfaceInfo>  updateWrapper = new UpdateWrapper<>();
        // 在updateWrapper 中设置了两个条件，interfaceId等于给定的interdaceid
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum = leftNum -1 , totalNum = totalNum+1");
        return this.update(updateWrapper);


    }
}




