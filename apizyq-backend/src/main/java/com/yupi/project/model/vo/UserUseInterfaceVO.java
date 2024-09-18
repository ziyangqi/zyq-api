package com.yupi.project.model.vo;

import com.zyq.zyqapicommon.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户使用情况接口
 */
@Data
public class UserUseInterfaceVO extends InterfaceInfo {


    /**
     * 使用含量
     */
    private long totalNum;

}
