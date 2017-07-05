package com.jkm.base.common.sftp.session;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jkm.base.common.sftp.session.helper.CreateSessionParams;
import com.jkm.base.common.sftp.session.helper.SessionConfig;

import java.util.Map;

/**
 * Created by hutao on 15/12/3.
 * 下午1:52
 */
public class SftpSessionFactoryImpl implements SftpSessionFactory {
    private final CreateSessionParams params;

    /**
     * @param params
     */
    public SftpSessionFactoryImpl(final CreateSessionParams params) {
        this.params = params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession(final String ip, final int port) {
        try {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(ip) && port > 0);
            final JSch jsch = new JSch();

            if (params.hasPrivateKey()) {
                if (params.hasPrivateKeyPassphrase()) {
                    jsch.addIdentity(params.getPrivateKey(), params.getPassphrase());
                } else {
                    jsch.addIdentity(params.getPrivateKey());
                }
            }

            final Session session = jsch.getSession(params.getUsername(), ip, port);
            final SessionConfig sessionConfig = params.getSessionConfig();
            if (params.hasPassword()) {
                session.setPassword(params.getPassword());
            }
            final Map<String, String> configs = sessionConfig.getConfigs();
            if (!configs.isEmpty()) {
                for (Map.Entry<String, String> entry : configs.entrySet()) {
                    session.setConfig(entry.getKey(), entry.getValue());
                }
            }
            session.connect(sessionConfig.getConnectTime());
            return session;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
