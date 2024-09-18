package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.UserInterfaceInfoMapper;
import com.yupi.project.model.vo.UserUseInterfaceVO;
import com.yupi.project.service.InterfaceInfoService;
import com.yupi.project.service.AnalysisInterfaceService;
import com.zyq.zyqapicommon.model.entity.InterfaceInfo;
import com.zyq.zyqapicommon.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class analysisInterfaceServiceImpl implements AnalysisInterfaceService {


    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 分析接口的方法然后根据接口的Id拿到接口的完整信息。
     */
    @Override
    public BaseResponse<List<UserUseInterfaceVO>> analysisInterface() {
        List<UserInterfaceInfo> userList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        // 创建一个map根据id进行分组
        Map<Long,List<UserInterfaceInfo>> interfaceInfoIdObjMap = userList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        //
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id" ,interfaceInfoIdObjMap.keySet());
        // 根据queryWrapper去进行查询
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);

        // 请求的接口信息不存在
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"请求的数据不存在");
        }
        // 构建接口的信息库也就是重新封装的信息

        // 然后重新封装一些返回UserUseInterfaceVo的格式。
        List<UserUseInterfaceVO> interfaceVOList = list.stream().map(interfaceInfo ->{
            // 1 建立一个新的接口的信息
            UserUseInterfaceVO userUseInterfaceVO  = new UserUseInterfaceVO();
            // 2 将接口复制到接口的VO的对象中
            BeanUtils.copyProperties(interfaceInfo,userUseInterfaceVO);
            // 3 从接口信息的id中获取调用的次数
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            userUseInterfaceVO.setTotalNum(totalNum);
            return userUseInterfaceVO;
        }).collect(Collectors.toList());
        //找到数据后重新封装一下
        return ResultUtils.success(interfaceVOList);
    }
}
