package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Allen on 2017/2/15.
 */
@Data
public class AppVersion {
    private String uuid;
    private Integer versionCode;//版本号
    private String versionName;//版本名称
    private String packageName;//包名
    private String appType;//app类型
    private String versionUrl;//版本下载地址
    private Long size;//版本大小 kb
    private Integer isUpgrade;//0不需要 1需要升级 2强制升级
    private String description;//版本信息描述
    private String isReview;//ios审核0 不审核 1审核
    private Date createTime;
    private Date updateTime;
}
