package com.yupi.project.model.dto.userInterfaceInfo;

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
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;
    private Long userId;
    private Long interfaceInfoId;
    private Integer totalNum;
    private Integer leftNum;
    private Integer status;



    private static final long serialVersionUID = 1L;
}