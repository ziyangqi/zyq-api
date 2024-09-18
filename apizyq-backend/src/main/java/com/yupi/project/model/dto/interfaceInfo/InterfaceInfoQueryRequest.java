package com.yupi.project.model.dto.interfaceInfo;

import com.yupi.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {


    private Long id;

    private String name;

    private String description;

    private String url;


    private String requestHeader;

    private String responseHeader;


    private Integer  status;


    private String method;

    private Long userId;


    private static final long serialVersionUID = 1L;
}