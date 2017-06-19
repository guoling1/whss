package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.UserTradeRate;
import com.jkm.hsy.user.help.requestparam.UserTradeRateListResponse;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

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
     * 更新
     * @param userTradeRate
     */
    void update(UserTradeRate userTradeRate);
    /**
     * 根据法人编码和政策类型查询
     * @return
     */
    Optional<UserTradeRate> selectByUserIdAndPolicyType(long userId, String policyType);

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
    void saveUserRate(long userId,long dealerId,BigDecimal wxRate,BigDecimal zfbRate,int isOpenWx,int isOpenZfb);

    /**
     * 商户费率信息
     * @return
     */
    List<UserTradeRateListResponse> getUserRate(long userId);

    /**
     * 商户费率
     * @return
     */
    List<UserTradeRate> selectAllByUserId(long userId);

}
