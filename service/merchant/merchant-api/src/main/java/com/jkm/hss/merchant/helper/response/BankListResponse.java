package com.jkm.hss.merchant.helper.response;

import com.jkm.hss.merchant.enums.EnumAccountBank;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/24.
 */
@Data
public class BankListResponse {
    /**
     * 卡号编码
     */
    private long bankId;
    /**
     * 银行卡号
     */
    private String bankNo;
    /**
     * 银行卡名称
     */
    private String bankName;
    /**
     * 预留手机号
     */
    private String reserveMobile;
    /**
     * 卡宾
     */
    private String bankBin;
    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 银行卡类型（0借记卡 1借贷卡）
     * {@link EnumAccountBank}
     */
    private int cardType;

    /**
     * 是否填写支行信息
     * 1有支行信息 0没有支行信息
     */
    private int hasBranch;


}
