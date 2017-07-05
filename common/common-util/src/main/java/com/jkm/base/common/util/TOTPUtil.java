package com.jkm.base.common.util;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by hutao on 16/1/22.
 * 下午3:32
 */
@UtilityClass
public class TOTPUtil {
    private final static SecureRandom random = new SecureRandom();

    /**
     * Generates an 80-bit shared secret and returns a string that can be used as input when setting up a new account
     * in the Google Authenticator app. In general {@link #generate160BitSharedSecret} should be used instead of this.
     *
     * @see #generate160BitSharedSecret
     */
    public static String generate80BitSharedSecret() {
        final byte[] secret = new byte[10];
        random.nextBytes(secret);
        return BaseEncoding.base32().encode(secret);
    }

    /**
     * Generates an 160-bit shared secret and returns a string that can be used as input when setting up a new account
     * in the Google Authenticator app.
     */
    public static String generate160BitSharedSecret() {
        final byte[] secret = new byte[20];
        random.nextBytes(secret);
        return BaseEncoding.base32().encode(secret);
    }

    /**
     * Makes the shared secret looks more pretty to humans and thus easier to type. Google Authenticator is compatible with
     * both the pretty and non-pretty format and so is this class.
     */
    public static String prettifySecret(final @NonNull String secret) {
        final int length = secret.length();
        if (length % 4 != 0) {
            throw new IllegalArgumentException("secret should be divisible by 4");
        }

        final StringBuilder buf = new StringBuilder((5 * (length / 4)) - 1);
        int index = 0;
        while (index < length) {
            if (index != 0) {
                buf.append(' ');
            }
            buf.append(secret.substring(index, index + 4).toLowerCase());
            index += 4;
        }
        return buf.toString();
    }

    /**
     * Matches a counter based code
     *
     * @param secret  the shared secret
     * @param code    code to match
     * @param counter current counter
     */
    public static boolean matchCounterBasedCode(final @NonNull String secret,
                                                final @NonNull String code,
                                                final long counter) {
        final String expectedCode = calculateCounterBasedCode(secret, counter);
        return expectedCode.equals(code);
    }

    /**
     * Matches a time based code, not allowing for drift.
     *
     * @param secret the shared secret
     * @param code   code to match
     */
    public static boolean matchTimeBasedCode(final @NonNull String secret,
                                             final @NonNull String code) {
        return matchTimeBasedCode(secret, code, 0);
    }

    /**
     * Matches a time based code, allowing for a few seconds of drift. It is recommended to only allow a few seconds drift.
     *
     * @param secret       the shared secret
     * @param code         code to match
     * @param secondsDrift how many seconds of drift is allowed, must be between 0 and 29 (inclusive).
     */
    public static boolean matchTimeBasedCode(final @NonNull String secret,
                                             final @NonNull String code,
                                             final int secondsDrift) {
        if (secondsDrift < 0 || secondsDrift > 29) {
            throw new IllegalArgumentException("secondsDrift must be between 0 and 29");
        }

        final long currentTimeMillis = System.currentTimeMillis();
        long counter = currentTimeMillis / 30000;

        if (matchCounterBasedCode(secret, code, counter)) {
            return true;
        }
        if (secondsDrift == 0) {
            return false;
        }

        long millisDrift = secondsDrift * 1000;

        // try to match against a clock that's ahead of this system's clock
        counter = (currentTimeMillis + millisDrift) / 30000;
        if (matchCounterBasedCode(secret, code, counter)) {
            return true;
        }

        // try to match against a clock that's behind this system's clock
        counter = (currentTimeMillis - millisDrift) / 30000;
        return matchCounterBasedCode(secret, code, counter);
    }

    /**
     * Calculates a time based code, based on the current system time.
     *
     * @param secret the shared secret
     */
    public static String calculateTimeBasedCode(final @NonNull String secret) {
        final long counter = System.currentTimeMillis() / 30000;
        return calculateCounterBasedCode(secret, counter);
    }

    /**
     * Calculate a counter based code.
     *
     * @param secret  the shared secret
     * @param counter current counter
     */
    public static String calculateCounterBasedCode(final @NonNull String secret,
                                                   final long counter) {
        final byte[] key = BaseEncoding.base32().decode(secret.replace(" ", "").toUpperCase());
        final byte[] hash = hmacSha1(key, counter);
        final int offset = hash[hash.length - 1] & 0x0f;
        final byte[] truncatedHash = Arrays.copyOfRange(hash, offset, offset + 4);
        truncatedHash[0] = (byte) (truncatedHash[0] & 0x7f);
        final int code = ByteBuffer.allocate(4).put(truncatedHash).getInt(0) % 1000000;
        return new DecimalFormat("000000").format(code);
    }

    /**
     * Generates an URI that can be used with your favorite QR encoding software to generate
     * a QR code that the Google Authenticator app will accept. The format is documented
     * <a href="https://code.google.com/p/google-authenticator/wiki/KeyUriFormat">here</a>.
     * Note that Google Authenticator will overwrite secrets with the same issuerName and acccountName
     * so choose something relatively unique.
     *
     * @param issuerName  name of issuer (eg. company or product name)
     * @param accountName name of the account, usually the login or email
     */
    public static String makeTimeBasedQrUri(final @NonNull String issuerName,
                                            final @NonNull String accountName,
                                            final @NonNull String secret) {
        return makeQrUri("totp", issuerName, accountName, secret).toString();
    }

    /**
     * Generates an URI that can be used with your favorite QR encoding software to generate
     * a QR code that the Google Authenticator app will accept. The format is documented
     * <a href="https://code.google.com/p/google-authenticator/wiki/KeyUriFormat">here</a>.
     * Note that Google Authenticator will overwrite secrets with the same issuerName and acccountName
     * so choose something relatively unique.
     *
     * @param issuerName   name of issuer (eg. company or product name)
     * @param accountName  name of the account, usually the login or email
     * @param counterValue value of the counter
     */
    public static String makeCounterBasedQrUri(final @NonNull String issuerName,
                                               final @NonNull String accountName,
                                               final @NonNull String secret,
                                               final long counterValue) {
        return makeQrUri("hotp", issuerName, accountName, secret).append("&counter=").append(counterValue).toString();
    }

    private static StringBuilder makeQrUri(final String type,
                                           final String issuerName,
                                           final String accountName,
                                           final String secret) {
        if (issuerName.contains(":")) {
            throw new IllegalArgumentException("issuerName may not contain colon");
        }
        if (accountName.contains(":")) {
            throw new IllegalArgumentException("accountName may not contain colon");
        }
        try {
            return new StringBuilder("otpauth://").append(type).append('/')
                    .append(encode(issuerName)).append(encode(":"))
                    .append(encode(accountName)).append("?secret=").append(encode(secret.replace(" ", "").toUpperCase()))
                    .append("&issuer=").append(encode(issuerName));

        } catch (UnsupportedEncodingException e) {
            throw Throwables.propagate(e);
        }
    }

    private static String encode(final String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, Charsets.UTF_8.name()).replace("+", "%20");
    }

    private static byte[] hmacSha1(final byte[] key, final long counter) {
        try {
            final Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            mac.update(ByteBuffer.allocate(8).putLong(counter).array());
            return mac.doFinal();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw Throwables.propagate(e);
        }
    }
}
