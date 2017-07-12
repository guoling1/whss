package com.jkm.base.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/15.
 *
 * 数据库对象的基类
 */
@Data
public class BaseEntity {

    /**
     * 主键id
     */
    protected long id;
    /**
     * 状态 0审核通过 1 审核失败或驳回重填
     * tinyint
     */
    protected int status;
    /**
     * 创建时间
     * datetime
     */
    protected Date createTime;
    /**
     * 修改时间
     * timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    protected Date updateTime;
}
