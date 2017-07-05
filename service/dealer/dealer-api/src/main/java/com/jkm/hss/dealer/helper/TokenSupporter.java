package com.jkm.hss.dealer.helper;

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
     * @param dealerId
     * @return
     */
    public static String generateToken(final long dealerId) {
        return AESUtil.encrypt(String.valueOf(dealerId) + "&" + System.nanoTime(), DealerConsts.getDealerConfig().tbDealerPassportEncryptKey());
    }

    /**
     * 获取解密uid
     *
     * @param token
     * @return
     */
    public static long decryptDealerId(final String token) {
        final String decryptStr = AESUtil.decrypt(token, DealerConsts.getDealerConfig().tbDealerPassportEncryptKey());
        return NumberUtils.toLong(decryptStr.substring(0, decryptStr.indexOf("&")));
    }
}
