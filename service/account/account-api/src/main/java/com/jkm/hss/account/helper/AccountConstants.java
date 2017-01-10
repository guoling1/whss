package com.jkm.hss.account.helper;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
@UtilityClass
public final class AccountConstants {

    /**
     * 手续费账户默认 1
     */
    public static final long POUNDAGE_ACCOUNT_ID = 1;


    /**
     * 金开门 收钱默认账户
     */
    public static final long JKM_ACCOUNT_ID = 2;

    /**
     * 商户自动升级最低金额
     */
    public static final BigDecimal MERCHANT_UPGRADE_MIN_AMOUNT = new BigDecimal("2000");
}
