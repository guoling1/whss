package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/10.
 */
@Data
public class MemberRequest {

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

    private String memberCardNO;//会员卡号

    private String consumerCellphone;//消费者手机号
}
