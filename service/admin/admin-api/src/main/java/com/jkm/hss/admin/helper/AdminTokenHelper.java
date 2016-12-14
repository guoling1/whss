package com.jkm.hss.admin.helper;

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
public class AdminTokenHelper {

    /**
     * 生成token
     *
     * @param uid
     * @return
     */
    public static String generateToken(final long uid) {
        return AESUtil.encrypt(String.valueOf(uid) + "&" + UUID.randomUUID().toString(), AdminConsts.getAdminConfig().tbAdminUserTokenEncryptKey());
    }

    /**
     * 获取解密auid
     *
     * @param token
     * @return
     */
    public static long decryptAdminUserId(final String token) {
        try {
            final String decryptStr = AESUtil.decrypt(token, AdminConsts.getAdminConfig().tbAdminUserTokenEncryptKey());
            return NumberUtils.toLong(decryptStr.substring(0, decryptStr.indexOf("&")));
        } catch (Exception e) {
            log.warn("decrypt admin token error", e);
            return 0;
        }
    }
}
