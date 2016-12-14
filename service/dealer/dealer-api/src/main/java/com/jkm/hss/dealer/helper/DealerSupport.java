package com.jkm.hss.dealer.helper;

import com.jkm.base.common.util.AESUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@UtilityClass
public class DealerSupport {
    /**
     * 加密手机号
     *
     * @param mobile
     * @return
     */
    public static String encryptMobile(final String mobile) {
        return AESUtil.encrypt(mobile, DealerConsts.getDealerConfig().tbDealerEncryptKey());
    }

    /**
     * 解密手机号
     *
     * @param encryptMobile
     * @return
     */
    public static String decryptMobile(final long dealerId,
                                       final String encryptMobile) {
        try {
            return AESUtil.decrypt(encryptMobile, DealerConsts.getDealerConfig().tbDealerEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经销商[{}]手机号[{}]异常", dealerId, encryptMobile);
            throw e;
        }
    }

    /**
     * 加密身份证
     *
     * @param identity
     * @return
     */
    public static String encryptIdenrity(final String identity) {
        return AESUtil.encrypt(identity, DealerConsts.getDealerConfig().tbDealerEncryptKey());
    }

    /**
     * 解密身份证
     *
     * @param encryptIdentity
     * @return
     */
    public static String decryptIdentity(final long dealerId,
                                       final String encryptIdentity) {
        try {
            return AESUtil.decrypt(encryptIdentity, DealerConsts.getDealerConfig().tbDealerEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经销商[{}]手机号[{}]异常", dealerId, encryptIdentity);
            throw e;
        }
    }

    /**
     * 加密身份证
     *
     * @param bankCard
     * @return
     */
    public static String encryptBankCard(final String bankCard) {
        return AESUtil.encrypt(bankCard, DealerConsts.getDealerConfig().tbDealerEncryptKey());
    }

    /**
     * 解密身份证
     *
     * @param encryptBankCard
     * @return
     */
    public static String decryptBankCard(final long dealerId,
                                         final String encryptBankCard) {
        try {
            return AESUtil.decrypt(encryptBankCard, DealerConsts.getDealerConfig().tbDealerEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经销商[{}]手机号[{}]异常", dealerId, encryptBankCard);
            throw e;
        }
    }

    /**
     * 密码hash
     *
     * @param password
     * @param salt
     * @return
     */
    public static String passwordDigest(final String password, final String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}
