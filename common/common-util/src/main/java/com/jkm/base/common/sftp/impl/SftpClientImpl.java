package com.jkm.base.common.sftp.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jkm.base.common.sftp.SftpClient;
import com.jkm.base.common.sftp.helper.SftpConfig;
import com.jkm.base.common.sftp.session.SftpSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.annotation.NotThreadSafe;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by hutao on 15/12/3.
 * 下午3:20
 */
@Slf4j
@NotThreadSafe
public class SftpClientImpl implements SftpClient {
    private final SftpSessionFactory sftpSessionFactory;
    private final SftpConfig sftpConfig;
    private Optional<ChannelSftp> channelSftp;

    /**
     * constructor
     *
     * @param sftpSessionFactory
     * @param sftpConfig
     */
    public SftpClientImpl(final SftpSessionFactory sftpSessionFactory,
                          final SftpConfig sftpConfig) {
        this.sftpSessionFactory = sftpSessionFactory;
        this.sftpConfig = sftpConfig;
        this.channelSftp = Optional.absent();
    }

    /**
     * constructor
     *
     * @param sftpSessionFactory
     */
    public SftpClientImpl(final SftpSessionFactory sftpSessionFactory) {
        this(sftpSessionFactory, SftpConfig.createDefault());
    }

    private ChannelSftp createChannelSftp(final String ip, final int port) throws Exception {
        final Session session = sftpSessionFactory.createSession(ip, port);
        final Channel channel = session.openChannel("sftp");
        channel.connect(sftpConfig.getConnectTimeout());
        return (ChannelSftp) channel;
    }

    private ChannelSftp getChannelSftp() {
        Preconditions.checkState(channelSftp.isPresent(), "没有建立连接");
        return channelSftp.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final String ip, final int port) {
        try {
            channelSftp = Optional.of(createChannelSftp(ip, port));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadFile(final String localFilePath,
                           final String remoteFilePath) {
        try {
            getChannelSftp().put(localFilePath, remoteFilePath);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void downloadFile(final String remoteFilePath,
                             final String localFilePath) {
        try {
            getChannelSftp().get(remoteFilePath, localFilePath);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileExist(final String remoteFilePath) {
        try {
            final ChannelSftp channelSftp = getChannelSftp();
            final SftpATTRS stat = channelSftp.stat(remoteFilePath);
            return stat.isReg();
        } catch (Exception e) {
            if (e.getMessage().contains("No such file")) {
                return false;
            }
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> ls(final String remoteFilePath) {
        try {
            final ChannelSftp channelSftp = getChannelSftp();
            final Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(remoteFilePath);
            return Lists.transform(vector, new Function<ChannelSftp.LsEntry, String>() {
                @Override
                public String apply(final ChannelSftp.LsEntry lsEntry) {
                    return lsEntry.getFilename();
                }
            });
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (channelSftp.isPresent()) {
            try {
                if (channelSftp.get().isConnected()) {
                    channelSftp.get().disconnect();
                    channelSftp.get().exit();
                }
            } catch (Exception ignore) {
                log.warn("closr sftp client error:{}", ignore.getMessage(), ignore);
            }
        }
    }
}
