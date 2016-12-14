package com.jkm.base.common.sftp.session.helper;

import com.google.common.base.Strings;
import lombok.*;

/**
 * Created by hutao on 15/12/3.
 * 下午2:03
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateSessionParams {
    private String username;
    private String password;
    private String privateKey;
    private String passphrase;
    private SessionConfig sessionConfig;

    /**
     * 用户名和密码构造
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static CreateSessionParams buildByPassword(final String username,
                                                      final String password) {
        return CreateSessionParams.builder()
                .username(username)
                .password(password)
                .sessionConfig(SessionConfig.createDefault())
                .build();
    }

    /**
     * 用户名和私钥构造
     *
     * @param username   用户名
     * @param privateKey 私钥
     * @param passphrase 私钥密码
     * @return
     */
    public static CreateSessionParams buildByPrivateKey(final String username,
                                                        final String privateKey,
                                                        final String passphrase) {
        return CreateSessionParams.builder()
                .username(username)
                .privateKey(privateKey)
                .passphrase(passphrase)
                .sessionConfig(SessionConfig.createDefault())
                .build();
    }

    /**
     * 是否有私钥
     *
     * @return
     */
    public boolean hasPrivateKey() {
        return !Strings.isNullOrEmpty(privateKey);
    }

    /**
     * 是否有密码
     *
     * @return
     */
    public boolean hasPassword() {
        return !Strings.isNullOrEmpty(password);
    }

    /**
     * 是否有私钥密码
     *
     * @return
     */
    public boolean hasPrivateKeyPassphrase() {
        return !Strings.isNullOrEmpty(passphrase);
    }
}
