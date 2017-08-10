package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/19.
 */
@Data
public class MerchantMemberResponse {

    private Long id;

    private String globalID;//商户编号

    private String realname;//商户名称

    private int cardCount;//会员卡数

    private String total;//储值额
}
