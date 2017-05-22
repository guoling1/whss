package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by wayne on 17/5/22.
 * 广告
 */
@Data
public class Advertisement extends BaseEntity{
    private String titleName;
    private String content;
    private Date validStartDate;
    private Date validEenDate;
    private String picUrl;
    private String jumpUrl;
    private int duration;
    private String remark;

}
