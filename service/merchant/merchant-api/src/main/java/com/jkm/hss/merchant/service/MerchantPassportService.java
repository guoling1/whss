package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;

import com.jkm.hss.merchant.entity.MerchantPassport;
import com.jkm.hss.merchant.enums.EnumLoginStatus;
import com.jkm.hss.merchant.enums.EnumPassportType;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public interface MerchantPassportService {


    /**
     * 新建passport
     *
     * @param merchantId
     * @param passportType
     * @param loginStatus
     * @return
     */
    MerchantPassport createPassport(long merchantId, EnumPassportType passportType, EnumLoginStatus loginStatus);

    /**
     * 根据token获取passport
     *
     * @param token
     * @param passportType
     * @return
     */
    Optional<MerchantPassport> getByToken(String token, EnumPassportType passportType);

    /**
     * 标记为登录
     *
     * @param passportId
     * @return
     */
    int markAsLogin(long passportId);

    /**
     * 刷新passport过期时间
     *
     * @param passportId
     * @return
     */
    int refreshToken(long passportId);

    /**
     * 标记为退出
     *
     * @param merchantId
     * @return
     */
    int markAsLogout(long merchantId);

    /**
     * merchantId
     *
     * @param merchantId
     * @return
     */
    Optional<MerchantPassport> selectByMerchantId(long merchantId);
}
