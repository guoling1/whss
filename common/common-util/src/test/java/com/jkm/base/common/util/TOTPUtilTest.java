package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/1/22.
 * 下午3:39
 */
public class TOTPUtilTest {
    @Test
    public void testGenerate160BitSharedSecret() throws Exception {
        final String secret = TOTPUtil.generate160BitSharedSecret();
        assertThat(secret.length(), is(32));
        assertThat(TOTPUtil.prettifySecret(secret).length(), is(39));
        final String qrUri = TOTPUtil.makeTimeBasedQrUri("login", "hutao@example.com", secret);
        assertThat(qrUri, is("otpauth://totp/login%3Ahutao%40example.com?secret=" + secret + "&issuer=login"));
        assertThat(TOTPUtil.calculateTimeBasedCode(secret).length(), is(6));
    }
}