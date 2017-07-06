package com.jkm.base.common.util;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by hutao on 15/12/1.
 * 下午7:12
 */
@Slf4j
public final class RsaUtil {
    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private RsaUtil() {
    }

    /**
     * 获取签名
     *
     * @param privateKeyValue 私钥
     * @param signStr         签名字符串
     * @return 签名
     */
    public static String sign(final byte[] privateKeyValue, final byte[] signStr) {
        try {
            final PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKeyValue);
            final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            final PrivateKey privateKey = keyFactory.generatePrivate(encodedKeySpec);
            final Signature signet = Signature
                    .getInstance(SIGNATURE_ALGORITHM);
            signet.initSign(privateKey);
            signet.update(signStr);
            final byte[] signed = signet.sign(); // 对信息的数字签名
            return Base64Util.encode(signed);
        } catch (Exception e) {
            throw new RuntimeException("rsa sign error:" + e.getMessage(), e);
        }
    }

    /**
     * 签名验证
     *
     * @param pubKeyValue  公钥
     * @param signStrBytes 被签名字符串
     * @param sign         签名
     * @return 签名是否正确
     */
    public static boolean checkSign(final byte[] pubKeyValue, final byte[] signStrBytes,
                                    final String sign) {
        try {
            final X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                    pubKeyValue);
            final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            final PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            final byte[] signed = Base64Util.decode(sign);
            final Signature signature = Signature
                    .getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(signStrBytes);
            return signature.verify(signed);
        } catch (Exception e) {
            log.warn("check sign exception", e);
            return false;
        }
    }

    /**
     * 生成一对公私钥
     *
     * @return
     */
    public static Pair<String, String> generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGen =
                    KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            return Pair.of(BytesHexConverterUtil.bytesToHexStr(publicKey.getEncoded()),
                    BytesHexConverterUtil.bytesToHexStr(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, byte[] privateKey) {
        try {
            final Cipher cipher = getDecryptCipherByPrivateKey(privateKey);
            return decrypt(encryptedData, cipher);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static Cipher getDecryptCipherByPrivateKey(final byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        final Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        final Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        return cipher;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, byte[] publicKey) {
        try {
            final Cipher cipher = getDecryptCipherByPublicKey(publicKey);
            return decrypt(encryptedData, cipher);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static Cipher getDecryptCipherByPublicKey(final byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        final Key publicK = keyFactory.generatePublic(x509KeySpec);
        final Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return cipher;
    }

    private static byte[] decrypt(final byte[] encryptedData,
                                  final Cipher cipher) throws Exception {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            return out.toByteArray();
        }
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) {
        try {
            final Cipher cipher = getEncryptCipherByPublicKey(publicKey);
            return encrypt(data, cipher);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static byte[] encrypt(final byte[] data,
                                  final Cipher cipher) throws Exception {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            return out.toByteArray();
        }
    }

    private static Cipher getEncryptCipherByPublicKey(final byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        final Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        final Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return cipher;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) {
        try {
            final Cipher cipher = getEncryptCipherByPrivateKey(privateKey);
            return encrypt(data, cipher);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static Cipher getEncryptCipherByPrivateKey(final byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        final Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        final Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return cipher;
    }
}
