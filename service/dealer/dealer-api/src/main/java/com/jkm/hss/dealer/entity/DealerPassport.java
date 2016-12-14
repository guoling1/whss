package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import com.jkm.hss.dealer.enums.EnumPassportStatus;
import com.jkm.hss.dealer.helper.TokenSupporter;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/23.
 *
 * 经销商登录令牌
 *
 * tb_dealer_passport
 */
@Data
public class DealerPassport extends BaseEntity {

    /**
     * 经销商id
     */
    private long dealerId;

    /**
     * 加密token
     */
    private String token;

    /**
     * token类型
     *
     * {@link com.jkm.hss.dealer.enums.EnumPassportType}
     */
    private int type;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 登录状态
     *
     * {@link com.jkm.hss.dealer.enums.EnumLoginStatus}
     */
    private int loginStatus;


    public void setDealerId(final long dealerId) {
        this.dealerId = dealerId;
        this.token = TokenSupporter.generateToken(dealerId);
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
        return TokenSupporter.decryptDealerId(getToken());
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
