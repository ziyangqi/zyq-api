package com.yupi.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据 id 发起的请求
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
