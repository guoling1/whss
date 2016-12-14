package com.jkm.base.common.spring.util;

import com.jkm.base.common.spring.config.ConfigurationUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by huangwei on 7/24/15.
 */
@Slf4j
public final class PBEEncryptor {

    private static final Map<KeyType, PooledPBEStringEncryptor> ENCRYPTOR_MAP = new EnumMap<>(KeyType.class);

    static {
        for (final KeyType type : KeyType.values()) {
            final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            encryptor.setPassword(type.getPwd());
            encryptor.setPoolSize(Runtime.getRuntime().availableProcessors());
            ENCRYPTOR_MAP.put(type, encryptor);
        }
    }

    private PBEEncryptor() {
    }

    /**
     * 加密
     *
     * @param plaintext
     * @param keyType
     * @return
     */
    public static String encrypt(final String plaintext, final KeyType keyType) {
        if (StringUtils.isBlank(plaintext)) {
            return plaintext;
        }
        if (keyType == null) {
            throw new IllegalArgumentException("keyType can't be null");
        }
        return ENCRYPTOR_MAP.get(keyType).encrypt(plaintext);
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @param keyType
     * @return
     */
    public static String decrypt(final String ciphertext, final KeyType keyType) {
        if (StringUtils.isBlank(ciphertext)) {
            return ciphertext;
        }
        if (keyType == null) {
            throw new IllegalArgumentException("keyType can't be null");
        }

        try {
            return ENCRYPTOR_MAP.get(keyType).decrypt(ciphertext);
        } catch (Exception e) {
            log.warn("passWord error!", e);
            return null;
        }
    }

    /**
     * 加密key类型
     */
    public enum KeyType {
        WX_TOKEN_PASSWORD("wx_token_password"),
        ADMIN_TOKEN_PASSWORD("admin_token_password");

        @Getter
        private String pwd;

        private KeyType(final String key) {
            this.pwd = ConfigurationUtils.getString(key);
        }

    }

}
