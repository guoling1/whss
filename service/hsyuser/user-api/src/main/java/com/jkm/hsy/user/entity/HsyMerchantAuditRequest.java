package com.jkm.hsy.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/19.
 */
@Data
public class HsyMerchantAuditRequest {

    private String changePhone;//更改后的手机号
    private String mobile;//报单员手机号

    /**
     * 状态
     */
    private int stat;

    private String username;//报单员

    private String realname;//姓名
    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 审核时间
     */
    private String auditTime1;

    private String commitTime;//提交开始时间

    private String commitTime1;//提交结束时间

    private Long id;
    private String globalID;//对外ID
    private String name;//店铺名称
    private String shortName;//店铺简称
    private String industryCode;//行业代码
    private String districtCode;//行政区代码
    private String address;//地址
    private String licenceID;//营业执照id
    private String storefrontID;//店面照片id
    private String counterID;//收银台ID
    private String indoorID;//室内ID
    private Long parentID;//父ID
    private String contactName;//联系人姓名
    private String contactCellphone;//联系人手机号
    private Integer status;//状态：1 正常 99禁用
    private Integer isPublic;//是否对公1是 2否'
    private String checkErrorInfo;//审核错误原因
    private Long accountID;//账户ID
    private Date createTime;
    private Date updateTime;

    private String cellphone;//注册手机号

    /**
     * 所属1代理商名称
     */
    private String proxyName;

    /**
     * 所属2代理商名称
     */
    private String proxyName1;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    private Long uid;//用户ID
    private Long sid;
    private Integer role;//角色 1法人 2店长 3店员  4财务
    private Integer type;//类型1主店 2分店

    private String licenceURL;//营业执照URL
    private String storefrontURL;//店面照片URL
    private String counterURL;//收银台URL
    private String indoorURL;//室内URL

    private String fileA;
    private String fileB;
    private String fileC;

    private Integer countEmployee;//员工个数
    private Integer countQR;//二维码个数
    /**
     * 条数
     */
    private Integer offset;
    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;
}
