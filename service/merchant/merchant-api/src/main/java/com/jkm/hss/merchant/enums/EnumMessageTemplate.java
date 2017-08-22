package com.jkm.hss.merchant.enums;

/**
 * Created by Allen on 2017/8/15.
 */
public enum EnumMessageTemplate {
    DIRECT_MERCHAN_BENEFIT_TEMPLATE(1201,"直接商户收款分润{amount}元"),
    INDIRECT_MERCHAN_BENEFIT_TEMPLATE(1202,"间接商户收款分润{amount}元"),
    DEALER_DIRECT_MERCHAN_BENEFIT_TEMPLATE(1203,"合伙人直接收款分润{amount}元"),
    DEALER_INDIRECT_MERCHAN_BENEFIT_TEMPLATE(1204,"合伙人间接收款分润{amount}元"),
    SUPER_DEALER_DIRECT_MERCHAN_BENEFIT_TEMPLATE(1205,"超级合伙人直接收款分润{amount}元"),
    SUPER_DEALER_INDIRECT_MERCHAN_BENEFIT_TEMPLATE(1206,"超级合伙人间接收款分润{amount}元"),
    VERIFY_SUCCESS_TEMPLATE(1207,"审核通过"),
    VERIFY_FAILURE_TEMPLATE(1208,"审核不通过，{reason}"),
    PAY_TEMPLATE(1209,"收款成功{amount}元"),
    WITHDRAW_TEMPLATE(1210,"提现成功，手续费{fee}元，到账{amount}元（{bank}，尾号{lastnumber}）"),
    ;
    public Integer key;
    public String value;

    EnumMessageTemplate(Integer key,String value) {
        this.key=key;
        this.value = value;
    }
}
