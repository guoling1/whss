package com.jkm.base.common.sftp.session;

import com.jcraft.jsch.Session;

/**
 * Created by hutao on 15/12/3.
 * 下午1:47
 */
public interface SftpSessionFactory {
    /**
     * 创建session
     *
     * @param ip   ip
     * @param port 端口
     * @return
     */
    Session createSession(final String ip,
                          final int port);
}
