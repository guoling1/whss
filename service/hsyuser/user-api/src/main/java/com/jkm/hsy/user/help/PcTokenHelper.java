package com.jkm.hsy.user.help;

import com.jkm.base.common.util.AESUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.UUID;

/**
 * Created by huangwei on 3/5/16.
 */
@UtilityClass
@Slf4j
public class PcTokenHelper {

    /**
     * 生成token
     *
     * @param uid
     * @return
     */
    public static String generateToken(final long uid) {
        return AESUtil.encrypt(String.valueOf(uid) + "&" + UUID.randomUUID().toString(), PcUserConsts.getConfig().tbAppPcUserTokenEncryptKey());
    }

    /**
     * 获取解密auid
     *
     * @param token
     * @return
     */
    public static long decryptUid(final String token) {
        try {
            final String decryptStr = AESUtil.decrypt(token, PcUserConsts.getConfig().tbAppPcUserTokenEncryptKey());
            return NumberUtils.toLong(decryptStr.substring(0, decryptStr.indexOf("&")));
        } catch (Exception e) {
            log.warn("decrypt uid token error", e);
            return 0;
        }
    }
}
