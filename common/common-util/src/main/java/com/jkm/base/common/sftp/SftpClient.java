package com.jkm.base.common.sftp;

import java.io.Closeable;
import java.util.List;

/**
 * Created by hutao on 15/12/3.
 * 下午1:46
 */
public interface SftpClient extends Closeable {
    /**
     * 连接
     */
    void connect(final String ip, final int port);

    /**
     * 上传文件
     *
     * @param localFilePath
     * @param remoteFilePath
     */
    void uploadFile(final String localFilePath, final String remoteFilePath);

    /**
     * 下载文件
     *
     * @param remoteFilePath
     * @param localFilePath
     */
    void downloadFile(final String remoteFilePath, final String localFilePath);

    /**
     * 文件是否存在
     *
     * @param remoteFilePath
     * @return
     */
    boolean isFileExist(final String remoteFilePath);

    /**
     * 获取目录下的所有文件名列表
     *
     * @param remoteFilePath
     * @return
     */
    List<String> ls(final String remoteFilePath);
}
