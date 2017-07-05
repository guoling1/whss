package com.jkm.base.common.util;

import org.junit.Test;

import java.io.InputStream;
import java.security.KeyStore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/12/6.
 * 下午1:54
 */
public class CerUtilTest {
    @Test
    public void testSignByPath() throws Exception {
        final String keystore = this.getClass().getResource("/").getPath() + "jinkaimen.p12";
        final String certFile = this.getClass().getResource("/").getPath() + "test.crt";
        final String alias = "jinkaimen";
        final String pwd = "jkm1202";
        final String str = "测试数据测试数据测试数据测试数据测试数据测试数据测试数据";
        final byte[] signStr = Md5Util.md5Digest(str.getBytes("utf8"));
        final byte[] sign = CerUtil.sign(signStr, keystore, alias, pwd);
        final String base64Sign = Base64Util.encode(sign);
        assertThat(base64Sign, is("N7a4S/zG3t1Xe0rF8aBd55+EHUSRjPqc3xtte4hxFBYSmhue0sGOc0inF0dPwUbWzrMlc9gkw4flFSXdmw384pb8teUv1Z0mh+jM+fl+ESS1ogqTDNjyModrsMPB8Ao+MNW75sqMJEieBHeXVXGSEIHweN/ww83IZgKWPh3ArVM="));
        assertThat(CerUtil.verify(signStr, sign, keystore, alias, pwd), is(true));
        CerUtil.verify(signStr, sign, certFile);
    }

    @Test
    public void testSignByKeyStore() throws Exception {
        final String alias = "jinkaimen";
        final String pwd = "jkm1202";
        try (final InputStream inputStream = this.getClass().getResourceAsStream("/jinkaimen.p12")) {
            final KeyStore keyStore = CerUtil.generateKeyStoreFromInputStream(
                    inputStream, CerUtil.EnumCerKeyType.PFX, pwd);
            final String str = "测试数据测试数据测试数据测试数据测试数据测试数据测试数据";
            final byte[] signStr = Md5Util.md5Digest(str.getBytes("utf8"));
            final byte[] sign = CerUtil.sign(signStr, keyStore, alias, pwd);
            final String base64Sign = Base64Util.encode(sign);
            assertThat(base64Sign, is("N7a4S/zG3t1Xe0rF8aBd55+EHUSRjPqc3xtte4hxFBYSmhue0sGOc0inF0dPwUbWzrMlc9gkw4flFSXdmw384pb8teUv1Z0mh+jM+fl+ESS1ogqTDNjyModrsMPB8Ao+MNW75sqMJEieBHeXVXGSEIHweN/ww83IZgKWPh3ArVM="));
        }
    }
}