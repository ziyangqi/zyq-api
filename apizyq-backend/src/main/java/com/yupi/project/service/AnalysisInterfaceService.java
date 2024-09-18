package com.yupi.project.service;

import com.yupi.project.common.BaseResponse;
import com.yupi.project.model.vo.UserUseInterfaceVO;

import java.util.List;

public interface AnalysisInterfaceService {

    /**
     * 分析调用的接口
     */
    public BaseResponse<List<UserUseInterfaceVO>> analysisInterface();
}
