package com.jkm.hss.merchant.helper;

import com.jkm.base.common.util.AESUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@UtilityClass
public class MerchantSupport {
    /**
     * 加密手机号
     *
     * @param mobile
     * @return
     */
    public static String encryptMobile(final String mobile) {
        return AESUtil.encrypt(mobile, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
    }

    /**
     * 解密手机号
     *
     * @param encryptMobile
     * @return
     */
    public static String decryptMobile(final String encryptMobile) {
        try {
            return AESUtil.decrypt(encryptMobile, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密[{}]异常", encryptMobile);
            throw e;
        }
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
            return AESUtil.decrypt(encryptMobile, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
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
        return AESUtil.encrypt(identity, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
    }
    /**
     * 解密身份证
     *
     * @param encryptIdentity
     * @return
     */
    public static String decryptIdentity(final String encryptIdentity) {
        try {
            return AESUtil.decrypt(encryptIdentity, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密[{}]异常",  encryptIdentity);
            throw e;
        }
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
            return AESUtil.decrypt(encryptIdentity, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经销商[{}]手机号[{}]异常", dealerId, encryptIdentity);
            throw e;
        }
    }

    /**
     * 加密银行卡
     *
     * @param bankCard
     * @return
     */
    public static String encryptBankCard(final String bankCard) {
        return AESUtil.encrypt(bankCard, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
    }

    /**
     * 加密银行卡
     *
     * @param encryptBankCard
     * @return
     */
    public static String decryptBankCard(final String encryptBankCard) {
        try {
            return AESUtil.decrypt(encryptBankCard, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经[{}]异常",encryptBankCard);
            throw e;
        }
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
            return AESUtil.decrypt(encryptBankCard, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密经销商[{}]手机号[{}]异常", dealerId, encryptBankCard);
            throw e;
        }
    }


    /**
     * 加密cvv
     *
     * @param cvv
     * @return
     */
    public static String encryptCvv(final String cvv) {
        return AESUtil.encrypt(cvv, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
    }

    /**
     * 解密cvv
     *
     * @param cvv
     * @return
     */
    public static String decryptCvv(final String cvv) {
        try {
            return AESUtil.decrypt(cvv, MerchantConsts.getMerchantConfig().tbMerchantEncryptKey());
        } catch (final Throwable e) {
            log.error("解密[{}]异常", cvv);
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
    public static void main(String[] args ){
        System.out.println(decryptMobile("B3sItZYv_xR2FGOBZqzPpg"));
//        System.out.println(decryptBankCard("6212260200059789657"));
 //       System.out.println(decryptIdentity(""));
//        System.out.println("=============================================");
//        String mobile = encryptMobile("13597634925");
//        String bankCard = encryptBankCard("6222620130005705891");
      String Identity = encryptIdenrity("140932198808296252");
//        System.out.println(bankCard);
//        System.out.println(bankCard);
        System.out.println(Identity);
//        String sql = "update tb_merchant_info set reserve_mobile='"+mobile+"',identity='"+Identity+"',bank_no='"+bankCard+"',bank_no_short='?',bank_bin='ICBC',bank_name='工商银行' where id=?";
//        System.out.println("sql="+sql);

    }
}
