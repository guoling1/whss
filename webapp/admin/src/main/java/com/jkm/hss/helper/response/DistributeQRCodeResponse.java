package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * admin 给一级代理商分配二维码
 *
 */
@Data
public class DistributeQRCodeResponse {

    /**
     * 一级代理商id
     */
    private long dealerId;

    /**
     * 代理商名称
     */
    private String name;

    /**
     * 代理商手机号
     */
    private String mobile;

    /**
     * 分配时间
     */
    private Date distributeDate;

    /**
     * 分配个数
     */
    private int count;

    /**
     * 开始码段
     */
    private String startCode;

    /**
     * 结束码段
     */
    private String endCode;
}
