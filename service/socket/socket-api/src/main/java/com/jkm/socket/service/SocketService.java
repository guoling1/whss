package com.jkm.socket.service;

import java.net.Socket;

/**
 * Created by yulong.zhang on 2017/7/26.
 */
public interface SocketService {


    /**
     * socketMap大小
     *
     * @return
     */
    int getSocketMapSize();

    /**
     * 缓存socket
     *
     * @param shopId
     * @param socket
     */
    void putSocket(long shopId, Socket socket);

    /**
     * 删除缓存socket
     *
     * @param shopId
     */
    void removeSocket(long shopId);

    /**
     * 发送消息
     *
     * @param shopId
     * @param msg
     */
    void sendMsg(long shopId, String msg);

    /**
     * 读取消息
     *
     * @param shopId
     */
    String readMsg(long shopId);
}
