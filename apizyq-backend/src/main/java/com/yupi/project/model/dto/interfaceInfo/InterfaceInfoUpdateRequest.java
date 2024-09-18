package com.yupi.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private Integer  status;
    /**
     * 请求参数
     */
    private String requestParams;

    private String method;

    private static final long serialVersionUID = 1L;
}