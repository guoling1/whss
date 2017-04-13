package com.jkm.hss.merchant.entity;

import lombok.Data;


@Data
public class LogResponse {

    /**
     * 审核人
     */
    private String name;

    /**
     * 批复信息
     */
    private String descr;

    /**
     * 审核时间
     */
    private String createTime;

    /**
     * 审核状态
     */
    private Integer status;


}
