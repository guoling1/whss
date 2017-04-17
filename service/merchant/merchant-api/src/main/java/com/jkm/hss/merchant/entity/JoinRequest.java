package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/4/11.
 */
@Data
public class JoinRequest {

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;

    /**
     * 条数
     */
    private Integer offset;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市区代码
     */
    private String cityCode;

    /**
     * 市区名称
     */
    private String cityName;

    /**
     * 所在县代码
     */
    private String countyCode;

    /**
     * 所在县名称
     */
    private String countyName;

    /**
     * 类型（代理商还是商户）
     */
    private String type;

    /**
     * 验证码
     */
    private String code;
}
