package com.jkm.socket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.util.ClientSocketUtil;
import com.jkm.socket.service.SocketExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yulong.zhang on 2017/7/24.
 *
 * socket线程池实现
 */
@Slf4j
@Service
public class SocketExecutorServiceImpl implements SocketExecutorService {

    @Autowired
    private SocketServiceImpl socketService;

    /**
     * 线程池，固定大小10
     */
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);


    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param msg
     */
    @Override
    public void runTask(final long shopId, final String msg) {
        try {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    socketService.sendMsg(shopId, msg);
                    final String resultMsg = socketService.readMsg(shopId);
                    if (!StringUtils.isEmpty(resultMsg)) {
                        final JSONObject jsonObject = JSONObject.parseObject(resultMsg);
                        final int code = jsonObject.getIntValue("code");
                        final long shopId1 = jsonObject.getLongValue("shopId");
                        final String tradeOrderNo = jsonObject.getString("tradeOrderNo");
                        if (1 == code) {
                            log.info("店铺[{}]-回参[{}]--订单--交易[{}]，推送成功", shopId, shopId1, tradeOrderNo);
                            return;
                        }
                        log.error("店铺[{}]-回参[{}]--订单--交易[{}]，推送失败-[{}]", shopId, shopId1, tradeOrderNo, msg);
                        return;
                    }
                    log.error("店铺[{}]--订单，推送失败-[{}]", shopId, msg);
                    return;
                }
            });
        } catch (final Throwable e) {
            log.error("店铺[" + shopId + "]--订单，推送失败-[" + msg + "]", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param socket
     */
    @Override
    public void getSocket(final Socket socket) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String resultMsg = ClientSocketUtil.readMsg(socket);
                    log.info("socket读取数据-[{}]", resultMsg);
                    if (!StringUtils.isEmpty(resultMsg)) {
                        final JSONObject jsonObject = JSONObject.parseObject(resultMsg);
                        final int code = jsonObject.getIntValue("code");
                        final long shopId = jsonObject.getLongValue("shopId");
                        if (1 == code) {
                            log.info("首次连接，读取socket消息，获取店铺和socket的关系,shopId-[{}]", shopId);
                            socketService.putSocket(shopId, socket);
                            return;
                        }
                    }
                } catch (final Throwable e) {
                    try {
                        socket.close();
                    } catch (final IOException e1) {
                        log.error("首次连接，读取socket消息，获取店铺和socket的关系，关闭socket异常", e1);
                    }
                    log.error("首次连接，读取socket消息，获取店铺和socket的关系，异常", e);
                }
            }
        });
    }
}
