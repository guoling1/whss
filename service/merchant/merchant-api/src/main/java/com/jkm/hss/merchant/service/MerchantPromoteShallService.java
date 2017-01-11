package com.jkm.hss.merchant.service;

import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by yuxiang on 2017-01-09.
 *
 * 商户升级分润
 */
public interface MerchantPromoteShallService {


    /**
     *  商户升级分润接口
     * @param merchantId
     * @return
     */
    Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShall(long merchantId, String orderNo, BigDecimal tradeAmount);
}
