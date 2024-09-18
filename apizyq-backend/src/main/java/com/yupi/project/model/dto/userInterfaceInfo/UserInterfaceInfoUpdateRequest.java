package com.yupi.project.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {


    private Long id;
    private Integer totalNum;
    private Integer leftNum;
    private Integer status;

    private static final long serialVersionUID = 1L;
}