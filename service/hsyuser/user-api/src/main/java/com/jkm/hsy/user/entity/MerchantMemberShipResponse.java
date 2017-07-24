package com.jkm.hsy.user.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangbin on 2017/7/20.
 */
@Data
public class MerchantMemberShipResponse {

    private Long uid;//主店法人ID
    private String membershipName;//会员卡名称
    private String  discount;//会员卡折扣(折)
    private String depositAmount;//开卡储值金额
    private Integer isPresentedViaActivate;//是否开卡赠送0否 1是
    private String rechargePresentAmount;//单笔充值赠送金额
    private List validShop;//有效店铺
    private int cardCount;//办理数量
    private String shortName;//商户简称
    private Long mcid;
    private Long sid;
}
