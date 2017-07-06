package com.jkm.base.common.util;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hutao on 15/12/1.
 * 下午7:12
 * <p/>
 * 负责证书相关操作
 * 目前仅支持证书签名与验签
 */
@Slf4j
public final class CerUtil {
    private static final Map<String, KeyStore> keyMap = new HashMap<>();
    private static final Map<String, Certificate> certMap = new HashMap<>();
    /**
     * 公钥证书格式
     */
    private static final String TYPE_X509 = "X.509";

    private static KeyStore getKeyStore(final String keyStorePath,
                                        final String password) {
        if (!keyMap.containsKey(keyStorePath)) {
            synchronized (keyMap) {
                if (!keyMap.containsKey(keyStorePath)) {
                    final KeyStore keyStore = generateKeyStoreFromFile(keyStorePath, password);
                    keyMap.put(keyStorePath, keyStore);
                }
            }
        }
        return keyMap.get(keyStorePath);
    }

    /**
     * 取得私钥证书
     *
     * @param keyStorePath 证书文件全路径名，支持pfx和jks两种格式
     * @param password     密码
     * @return 私钥证书
     */
    public static KeyStore generateKeyStoreFromFile(final String keyStorePath,
                                                    final String password) {
        try (final FileInputStream in = new FileInputStream(keyStorePath)) {
            final KeyStore keyStore = createKeyStore(keyStorePath);
            keyStore.load(in, password.toCharArray());
            return keyStore;
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static KeyStore generateKeyStoreFromInputStream(final InputStream inputStream,
                                                           final EnumCerKeyType keyType,
                                                           final String password) {
        try {
            final KeyStore keyStore = KeyStore.getInstance(keyType.type);
            keyStore.load(inputStream, password.toCharArray());
            return keyStore;
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 取得私钥证书
     *
     * @param keyStoreFile 证书文件全路径名，支持pfx和jks两种格式
     * @param password     密码
     * @return 私钥证书
     */
    public static KeyStore generateKeyStoreFromFile(final File keyStoreFile,
                                                    final String password) {
        try (final FileInputStream in = new FileInputStream(keyStoreFile)) {
            final KeyStore keyStore = createKeyStore(keyStoreFile.getAbsolutePath());
            keyStore.load(in, password.toCharArray());
            return keyStore;
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static KeyStore createKeyStore(final String keyStorePath)
            throws KeyStoreException, CertificateException {
        if (keyStorePath.endsWith(".jks")) {
            return KeyStore.getInstance(EnumCerKeyType.JKS.type);
        } else if (keyStorePath.endsWith(".pfx") || keyStorePath.endsWith(".p12")) {
            return KeyStore.getInstance(EnumCerKeyType.PFX.type);
        } else {
            throw new CertificateException("unknow file type.");
        }
    }

    private static Certificate getCertificate(final String certificatePath) {
        if (!certMap.containsKey(certificatePath)) {
            synchronized (certMap) {
                if (!certMap.containsKey(certificatePath)) {
                    return getCertificateImpl(certificatePath);
                }
            }
        }
        return certMap.get(certificatePath);
    }

    private static Certificate getCertificateImpl(final String certificatePath) {
        try {
            final Certificate certificate = generateCertificateFromFile(certificatePath);
            certMap.put(certificatePath, certificate);
            return certificate;
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 取得公钥证书
     *
     * @param certificatePath 证书文件全路径，仅支持cer格式
     * @return 证书
     */
    public static Certificate generateCertificateFromFile(final String certificatePath) {
        try (FileInputStream in = new FileInputStream(certificatePath)) {
            final CertificateFactory certificateFactory = CertificateFactory.getInstance(TYPE_X509);
            return certificateFactory.generateCertificate(in);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 从私钥库中取得证书
     *
     * @param keyStorePath 证书文件全路径名，支持pfx和jks两种格式
     * @param alias        证书别名
     * @param password     密码
     * @return 证书信息
     */
    public static Certificate getCertificateFromKeyStoreFile(final String keyStorePath,
                                                             final String alias,
                                                             final String password) {
        final KeyStore keyStore = generateKeyStoreFromFile(keyStorePath, password);
        try {
            return keyStore.getCertificate(alias);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static Certificate getCertificateByKeyStore(final String keyStorePath,
                                                        final String alias,
                                                        final String password) {
        final String certificatePath = keyStorePath + ":" + alias;
        if (!certMap.containsKey(certificatePath)) {
            synchronized (certMap) {
                if (!certMap.containsKey(certificatePath)) {
                    final Certificate certificate = getCertificateFromKeyStoreFile(keyStorePath, alias, password);
                    certMap.put(certificatePath, certificate);
                }
            }
        }
        return certMap.get(certificatePath);
    }

    /**
     * 取得证书私钥
     *
     * @param keyStorePath 证书文件全路径名，支持pfx和jks两种格式
     * @param alias        私钥别名
     * @param password     证书密码
     * @return 私钥
     */
    private static PrivateKey getPrivateKey(final String keyStorePath,
                                            final String alias,
                                            final String password) throws Exception {
        final KeyStore keyStore = getKeyStore(keyStorePath, password);
        return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    }

    /**
     * 取得证书公钥
     *
     * @param certificatePath 证书文件全路径，仅支持cer格式
     * @return 公钥
     */
    private static PublicKey getPublicKey(final String certificatePath) {
        final Certificate certificate = getCertificate(certificatePath);
        return certificate.getPublicKey();
    }

    /**
     * 将原数据使用证书私钥签名，签名结果转换为base64格式
     *
     * @param signStr      待签名数据
     * @param keyStorePath 证书文件全路径名，支持pfx和jks两种格式
     * @param alias        私钥别名
     * @param password     证书密码
     * @return 签名结果
     */
    public static byte[] sign(final byte[] signStr,
                              final String keyStorePath,
                              final String alias,
                              final String password) {
        try {
            final PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
            final X509Certificate cer = (X509Certificate) getCertificateByKeyStore(keyStorePath, alias, password);
            final Signature signature = Signature.getInstance(cer.getSigAlgName());
            signature.initSign(privateKey);
            signature.update(signStr);
            return signature.sign();
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 将原数据使用证书私钥签名，签名结果转换为base64格式
     *
     * @param signStr  待签名数据
     * @param keyStore 证书，支持pfx和jks两种格式
     * @param alias    私钥别名
     * @param password 证书密码
     * @return 签名结果
     */
    public static byte[] sign(final byte[] signStr,
                              final KeyStore keyStore,
                              final String alias,
                              final String password) {
        try {
            final PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            final X509Certificate cer = (X509Certificate) keyStore.getCertificate(alias);
            final Signature signature = Signature.getInstance(cer.getSigAlgName());
            signature.initSign(privateKey);
            signature.update(signStr);
            return signature.sign();
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 对原数据与base64格式的比对签名串进行校验
     *
     * @param signStr         原数据
     * @param sign            比对签名串
     * @param certificatePath 证书文件全路径，仅支持cer格式
     * @return 比对结果，如果一致则返回<code>true</code>，不一致或比对过程中出现异常均返回<code>false</code>
     */
    public static boolean verify(final byte[] signStr,
                                 final byte[] sign,
                                 final String certificatePath) {
        try {
            final X509Certificate cer = (X509Certificate) getCertificate(certificatePath);
            final PublicKey publicKey = getPublicKey(certificatePath);
            final Signature signature = Signature.getInstance(cer.getSigAlgName());
            signature.initVerify(publicKey);
            signature.update(signStr);
            return signature.verify(sign);
        } catch (final Exception e) {
            log.warn("verify sign error", e);
            return false;
        }
    }

    /**
     * 对原数据与base64格式的比对签名串进行校验
     *
     * @param signStr      原数据
     * @param sign         比对签名串
     * @param keyStorePath 证书文件全路径名，支持pfx和jks两种格式
     * @param alias        私钥别名
     * @param password     证书密码
     * @return 比对结果，如果一致则返回<code>true</code>，不一致或比对过程中出现异常均返回<code>false</code>
     */
    public static boolean verify(final byte[] signStr,
                                 final byte[] sign,
                                 final String keyStorePath,
                                 final String alias,
                                 final String password) {
        try {
            final X509Certificate cer = (X509Certificate) getCertificateByKeyStore(keyStorePath, alias, password);
            final PublicKey publicKey = cer.getPublicKey();
            final Signature signature = Signature.getInstance(cer.getSigAlgName());
            signature.initVerify(publicKey);
            signature.update(signStr);
            return signature.verify(sign);
        } catch (final Exception e) {
            log.warn("verify sign error", e);
            return false;
        }
    }

    public enum EnumCerKeyType {
        /**
         * jks密钥库
         */
        JKS("JKS"),
        /**
         * pfx密钥库
         */
        PFX("PKCS12"),;

        private final String type;

        EnumCerKeyType(final String type) {
            this.type = type;
        }
    }
}
