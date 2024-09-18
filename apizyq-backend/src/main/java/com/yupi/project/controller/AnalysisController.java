package com.yupi.project.controller;


import com.yupi.project.common.BaseResponse;
import com.yupi.project.model.vo.UserUseInterfaceVO;
import com.yupi.project.service.AnalysisInterfaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {


    @Resource
    private AnalysisInterfaceService analysisInterfaceService;

    /**
     * 用户使用接口
     * @return
     */
    @GetMapping("userUserTop")
    public BaseResponse <List<UserUseInterfaceVO>>analysisUserInterface (){
        return  analysisInterfaceService.analysisInterface();
    }
}
