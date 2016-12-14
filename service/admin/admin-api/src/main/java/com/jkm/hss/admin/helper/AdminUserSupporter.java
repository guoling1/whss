package com.jkm.hss.admin.helper;

import com.google.common.base.Strings;
import com.jkm.base.common.util.AESUtil;
import com.jkm.base.common.util.TOTPUtil;
import com.jkm.hss.admin.entity.AdminUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by yulong.zhang on 2016/11/24.
 */
@Slf4j
public class AdminUserSupporter {

    /**
     * 加密手机号
     *
     * @param mobile
     * @return
     */
    public static String encryptMobile(final String mobile) {
        return AESUtil.encrypt(mobile, AdminConsts.getAdminConfig().tbAdminEncryptKey());
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
            return AESUtil.decrypt(encryptMobile, AdminConsts.getAdminConfig().tbAdminEncryptKey());
        } catch (final Throwable e) {
            log.error("解密后台用户[{}]手机号[{}]异常", dealerId, encryptMobile);
            throw e;
        }
    }

    /**
     * 密码是否正确
     *
     * @param adminUser
     * @param password
     * @return
     */
    public static boolean isPasswordCorrect(final AdminUser adminUser, final String password) {
        final String digest = DigestUtils.sha256Hex(password + adminUser.getSalt());
        return digest.equals(adminUser.getPassword());
    }

    /**
     * 生成加密后的密码
     *
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(final String salt, final String password) {
        return DigestUtils.sha256Hex(password + salt);
    }

    /**
     * 生成加密后的二次验证恢复码
     *
     * @param salt
     * @param recoverCode
     * @return
     */
    public static String encryptTwoFactorReconverCode(final String salt, final String recoverCode) {
        return DigestUtils.sha256Hex(recoverCode + salt);
    }

    /**
     * 获取生成二维码的url
     *
     * @param twoFactorSecret
     * @param userName
     * @return
     */
    public static String generateTwoFactorAuthorizeQrUrl(@NonNull final String twoFactorSecret,
                                                         @NonNull final String userName) {
        return TOTPUtil.makeTimeBasedQrUri("finance", userName, twoFactorSecret);
    }

    /**
     * 获取生成二次验证的恢复码
     *
     * @return
     */
    public static String generateTwoFactorRecoverCode() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * 生成随机8位密码
     *
     * @return
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(5) + RandomStringUtils.randomNumeric(3);
    }

    /**
     * 生成盐
     *
     * @return
     */
    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    /**
     * 检查密码格式是否正确
     *
     * @return
     */
    public static boolean checkPasswordFormatCorrect(final String password) {
        if (Strings.isNullOrEmpty(password)) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasAlphanumeric = false;
        for (final Byte b : password.getBytes()) {
            if (Character.isDigit(b)) {
                hasDigit = true;
            } else if (Character.isLetter(b)) {
                hasAlphanumeric = true;
            }
        }
        return hasDigit && hasAlphanumeric;
    }
}
