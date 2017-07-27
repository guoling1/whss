package com.jkm.socket.service.impl;

import com.jkm.socket.service.SocketExecutorService;
import com.jkm.socket.service.ServerSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yulong.zhang on 2017/7/26.
 */
@Slf4j
@Service
public class ServerSocketServiceImpl implements ServerSocketService {

    @Autowired
    private SocketExecutorService socketExecutorService;
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.acceptServerSocket();
    }

    /**
     * 创建serverSocket，接受socket请求
     */
    private void acceptServerSocket() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(11111);
            serverSocket.setSoTimeout(60000);
            log.info("serverSocket--start");
        } catch (final IOException e) {
            log.error("创建serverSocket，异常！", e);
        }
        if (null != serverSocket) {
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    log.info("接受socket请求-[{}]", socket.getInetAddress().getHostAddress());
                    socket.setSoTimeout(10000);
                    socketExecutorService.getSocket(socket);
                } catch (final IOException e) {
                    log.error("serverSocket，接受socket请求, 60s未收到请求，超时！");
                }
            }
        }
    }


}
