package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/21.
 */
@Data
public class CardNoResponse {
    private String consumerCellphone;//消费者手机号
    private String shortName;//店铺简称
    private String membershipName;//会员卡名称
    private String tradeAmount;//储值额
    private String amount;//已消费
}
