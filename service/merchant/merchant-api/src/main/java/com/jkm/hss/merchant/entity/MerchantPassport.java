package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;

import com.jkm.hss.merchant.enums.EnumLoginStatus;
import com.jkm.hss.merchant.enums.EnumPassportStatus;
import com.jkm.hss.merchant.helper.TokenSupporter;
import lombok.Data;

/**
 *
 *
 * 经销商登录令牌
 *
 * tb_dealer_passport
 */
@Data
public class MerchantPassport extends BaseEntity {

    /**
     * 经销商id
     */
    private long merchantId;

    /**
     * 加密token
     */
    private String token;

    /**
     * token类型
     *
     * {@link com.jkm.hss.merchant.enums.EnumPassportType}
     */
    private int type;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 登录状态
     *
     * {@link com.jkm.hss.merchant.enums.EnumLoginStatus}
     */
    private int loginStatus;


    public void setMerchantId(final long merchantId) {
        this.merchantId = merchantId;
        this.token = TokenSupporter.generateToken(merchantId);
    }

    /**
     * 判断token是否已过期
     *
     * @return
     */
    public boolean isExpire() {
        return getExpireTime() < System.currentTimeMillis();
    }

    /**
     * token状态是否正常
     *
     * @return
     */
    public boolean isValid() {
        return getStatus() == EnumPassportStatus.VALID.getId();
    }

    /**
     * 获取解密后的uid
     *
     * @return
     */
    public long getDecryptDealerId() {
        return TokenSupporter.decryptMerchantId(getToken());
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return getLoginStatus() == EnumLoginStatus.LOGIN.getId();
    }


}
