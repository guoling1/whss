package com.jkm.hsy.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/7/10.
 */
@Data
public class MemberResponse {

    private String memberCardNO;//会员卡号

    private String consumerCellphone;//消费者手机号

    private Date createTime;

    private String createTimes;//绑定时间

    private Date createTime1;

    private String createTimes1;//手机绑定时间（）

    private String userID;//支付宝ID

    private String openID;//微信ID

}
