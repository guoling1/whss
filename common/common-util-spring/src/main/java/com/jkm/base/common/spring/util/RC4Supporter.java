package com.jkm.base.common.spring.util;

import com.jkm.base.common.spring.config.ConfigurationUtils;
import com.jkm.base.common.util.RC4;
import lombok.Getter;

/**
 * Created by huangwei on 7/27/15.
 */
public final class RC4Supporter {
    private RC4Supporter() {

    }

    /**
     * 加密
     *
     * @param key
     * @param str
     * @return
     */
    public static String encrypt(final KeyType key, final String str) {
        return RC4.encrypt(str, key.getPwd());
    }

    /**
     * 解密
     *
     * @param key
     * @param str
     * @return
     */
    public static String decrypt(final KeyType key, final String str) {
        return RC4.decrypt(str, key.getPwd());
    }

    /**
     * 一个数据库表对应一个key
     */
    public enum KeyType {
        TB_ACCOUNT("account.tb_accounts.pwd"),
        TB_USER_BANK_CARD("account.tb_user_bank_cards.pwd"),
        TB_USER_VERIFIED("user.tb_user_verified.pwd"),
        TB_USER("user.tb_user.pwd");

        @Getter
        private String pwd;

        private KeyType(final String key) {
            this.pwd = ConfigurationUtils.getString(key);
        }

    }
}
