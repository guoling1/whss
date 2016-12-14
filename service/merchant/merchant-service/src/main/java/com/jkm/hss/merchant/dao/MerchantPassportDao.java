package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantPassport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Repository
public interface MerchantPassportDao {

    /**
     * 插入
     *
     * @param merchantPassport
     */
    void insert(MerchantPassport merchantPassport);

    /**
     * 更新
     *
     * @param merchantPassport
     * @return
     */
    int updateByMerchantId(MerchantPassport merchantPassport);

    /**
     * 更新登录状态
     *
     * @param id
     * @param loginStatus
     * @return
     */
    int updateLoginStatusById(@Param("id") long id, @Param("loginStatus") int loginStatus);

    /**
     * 更新过期时间
     *
     * @param id
     * @param expireTime
     * @return
     */
    int updateExpireTimeById(@Param("id") long id, @Param("expireTime") long expireTime);

    /**
     * 更新登录状态
     *
     * @param merchantId
     * @param loginStatus
     * @return
     */
    int updateLoginStatusByMerchantId(@Param("merchantId") long merchantId, @Param("loginStatus") int loginStatus);

    /**
     * 按merchantId查询
     *
     * @param merchantId
     * @return
     */
    MerchantPassport selectByMerchantId(@Param("merchantId") long merchantId);

    /**
     * 按token查询
     *
     * @param token
     * @param type
     * @return
     */
    MerchantPassport selectByToken(@Param("token") String token, @Param("type") int type);
}
