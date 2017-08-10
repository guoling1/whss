package com.jkm.socket.service.impl;

import com.jkm.base.common.util.ClientSocketUtil;
import com.jkm.socket.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yulong.zhang on 2017/7/26.
 */
@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    /**
     * 店铺id--socket
     */
    public static final ConcurrentMap<Long, Socket> shopSocketConcurrentMap = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int getSocketMapSize() {
        return shopSocketConcurrentMap.size();
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param socket
     */
    @Override
    public void putSocket(final long shopId, final Socket socket) {
        shopSocketConcurrentMap.put(shopId, socket);
        log.info("此时socket的数目-[{}]", shopSocketConcurrentMap.size());
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     */
    @Override
    public void removeSocket(final long shopId) {
        shopSocketConcurrentMap.remove(shopId);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param msg
     */
    @Override
    public void sendMsg(final long shopId, final String msg) {
        try {
            final Socket socket = shopSocketConcurrentMap.get(shopId);
            if (null != socket) {
                ClientSocketUtil.sendMsg(socket, msg);
                log.info("发送成功");
                return;
            }
            log.error("店铺-[{}]发送socket消息, socket不存在", shopId, msg);
        } catch (final Throwable e) {
            log.error("发送socket消息异常-[" + msg + "]", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     */
    @Override
    public String readMsg(final long shopId) {
        try {
            final Socket socket = shopSocketConcurrentMap.get(shopId);
            if (null != socket) {
                final String msg = ClientSocketUtil.readMsg(socket);
                return msg;
            }
            log.error("店铺-[{}]读取socket消息, socket不存在", shopId);
        } catch (final Throwable e) {
            log.error("读取socket消息异常", e);
        }
        return "";
    }
}
