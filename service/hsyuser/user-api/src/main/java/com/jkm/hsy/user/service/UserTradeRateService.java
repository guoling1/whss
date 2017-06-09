package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.UserTradeRate;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/9.
 */
public interface UserTradeRateService {
    /**
     * 新增
     * @param userTradeRate
     */
    void insert(UserTradeRate userTradeRate);

    /**
     * 查询T1结算费率
     * @param dealerId
     * @return
     */
    UserTradeRateResponse getRateRang(long dealerId);

    /**
     * 获取当前商户T1费率
     * @param userId
     * @return
     */
    Pair<BigDecimal,BigDecimal> getCurrentUserRate(long userId);

    /**
     * 保存商户费率
     * @param userId
     * @param wxRate
     * @param zfbRate
     * @param isOpenWx
     * @param isOpenZfb
     */
    void saveUserRate(long userId,BigDecimal wxRate,BigDecimal zfbRate,int isOpenWx,int isOpenZfb);

}
