package com.jkm.base.common.util;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/12/1.
 * 下午7:16
 */
public class RsaUtilTest {
    private static final byte[] publicKey;
    private static final byte[] privateKey;
    private static final String signStr = SnGenerator.generate();

    static {
        final Pair<String, String> keyPair = RsaUtil.generateKeyPair();
        publicKey = BytesHexConverterUtil.hexStrToBytes(keyPair.getLeft());
        privateKey = BytesHexConverterUtil.hexStrToBytes(keyPair.getRight());
    }

    @Test
    public void testSign() throws Exception {
        final byte[] signStrBytes = signStr.getBytes();
        final String sign = RsaUtil.sign(privateKey, signStrBytes);
        assertThat(RsaUtil.checkSign(publicKey, signStrBytes, sign), is(true));
    }

    @Test
    public void testEncryptByPrivateKey() throws Exception {
        final byte[] str = SnGenerator.generate().getBytes();
        final byte[] encryptStr = RsaUtil.encryptByPrivateKey(str, privateKey);
        assertThat(RsaUtil.decryptByPublicKey(encryptStr, publicKey), is(str));
    }

    @Test
    public void testEncryptByPublicKey() throws Exception {
        final byte[] str = SnGenerator.generate().getBytes();
        final byte[] encryptStr = RsaUtil.encryptByPublicKey(str, publicKey);
        assertThat(RsaUtil.decryptByPrivateKey(encryptStr, privateKey), is(str));
    }
}