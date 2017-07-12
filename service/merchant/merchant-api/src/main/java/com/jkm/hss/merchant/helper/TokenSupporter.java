package com.jkm.hss.merchant.helper;

import com.jkm.base.common.util.AESUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by huangwei on 3/5/16.
 */
@UtilityClass
public class TokenSupporter {

    /**
     * 生成token
     *
     * @param merchantId
     * @return
     */
    public static String generateToken(final long merchantId) {
        return AESUtil.encrypt(String.valueOf(merchantId) + "&" + System.nanoTime(), MerchantConsts.getMerchantConfig().tbMerchantPassportEncryptKey());
    }

    /**
     * 获取解密uid
     *
     * @param token
     * @return
     */
    public static long decryptMerchantId(final String token) {
        final String decryptStr = AESUtil.decrypt(token, MerchantConsts.getMerchantConfig().tbMerchantPassportEncryptKey());
        return NumberUtils.toLong(decryptStr.substring(0, decryptStr.indexOf("&")));
    }
}
