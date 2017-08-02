package com.jkm.socket.service;


import java.net.Socket;

/**
 * Created by yulong.zhang on 2017/7/24.
 */
public interface SocketExecutorService {

    /**
     * 运行任务
     *
     * @param shopId
     * @param msg
     */
    void runTask(long shopId, String msg);

    /**
     * 首次连接，获取店铺和socket的关系
     *
     * @param socket
     */
    void getSocket(Socket socket);
}
