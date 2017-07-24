package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.ClientSocketUtil;
import com.jkm.hsy.user.dao.ShopSocketDao;
import com.jkm.hsy.user.entity.ShopSocket;
import com.jkm.hsy.user.service.ShopSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yulong.zhang on 2017/7/24.
 */
@Slf4j
@Service
public class ShopSocketServiceImpl implements ShopSocketService {

    @Autowired
    private ShopSocketDao shopSocketDao;

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    private final ConcurrentMap<Long, Socket> shopSocketConcurrentMap = new ConcurrentHashMap<>();


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (isNotInit()) {
            synchronized (ShopSocketServiceImpl.class) {
                if (isNotInit()) {
                    final List<ShopSocket> shopSockets = this.shopSocketDao.selectLimit1000();
                    if (!CollectionUtils.isEmpty(shopSockets)) {
                        for (ShopSocket shopSocket : shopSockets) {
                            final Socket socket = ClientSocketUtil.getSocket(shopSocket.getIp(), shopSocket.getPort());
                            shopSocketConcurrentMap.put(shopSocket.getShopId(), socket);
                        }
                    }
                    isInit.set(true);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param ip
     * @param port
     */
    @Override
    public Socket putSocket(long shopId, final String ip, final int port) {
        this.assertInit();
        final Socket socket = ClientSocketUtil.getSocket(ip, port);
        shopSocketConcurrentMap.put(shopId, socket);
        return socket;
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @return
     */
    @Override
    public void removeSocket(final long shopId) {
        shopSocketConcurrentMap.remove(shopId);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @return
     */
    @Override
    public Socket getSocket(long shopId) {
        this.assertInit();
        final Socket socket = shopSocketConcurrentMap.get(shopId);
        if (null != socket) {
            return socket;
        }
        final Optional<ShopSocket> shopSocketOptional = this.getByShopId(shopId);
        if (shopSocketOptional.isPresent()) {
            final ShopSocket shopSocket = shopSocketOptional.get();
            final Socket socket1 = this.putSocket(shopId, shopSocket.getIp(), shopSocket.getPort());
            return socket1;
        }
        return null;
    }


    /**
     * 初始化
     */
    private void assertInit() {
        if (isNotInit()) {
            this.init();
        }
    }
    /**
     * 未初始化
     *
     * @return
     */
    private boolean isNotInit() {
        return !isInit.get();
    }

    /**
     * {@inheritDoc}
     *
     * @param shopSocket
     */
    @Override
    @Transactional
    public void add(final ShopSocket shopSocket) {
        this.shopSocketDao.insert(shopSocket);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopSocket
     * @return
     */
    @Override
    @Transactional
    public int update(final ShopSocket shopSocket) {
        return this.shopSocketDao.update(shopSocket);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopSocket
     * @return
     */
    @Override
    @Transactional
    public int updateByShopId(final ShopSocket shopSocket) {
        return this.updateByShopId(shopSocket);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ShopSocket> getById(final long id) {
        return Optional.fromNullable(this.shopSocketDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @return
     */
    @Override
    public Optional<ShopSocket> getByShopId(final long shopId) {
        return Optional.fromNullable(this.shopSocketDao.selectByShopId(shopId));
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param ip
     * @param port
     */
    @Override
    public void saveSocketIp(final long shopId, final String ip, final int port) {
        final Optional<ShopSocket> socketOptional = this.getByShopId(shopId);
        if (socketOptional.isPresent()) {
            final ShopSocket shopSocket = socketOptional.get();
            if (shopSocket.isSame(ip, port)) {
                return;
            }
            final ShopSocket updateShopSocket = new ShopSocket();
            updateShopSocket.setIp(ip);
            updateShopSocket.setPc("");
            updateShopSocket.setPort(port);
            updateShopSocket.setId(shopSocket.getId());
            this.update(shopSocket);
            //TODO更新缓存

            return;
        }
        final ShopSocket shopSocket = new ShopSocket();
        shopSocket.setIp(ip);
        shopSocket.setPc("");
        shopSocket.setPort(port);
        shopSocket.setShopId(shopId);
        this.add(shopSocket);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param msg
     */
    @Override
    public void sendSocketMsg(final long shopId, final String msg) {
        this.sendMsg(shopId, msg);
    }

    /**
     * 尝试2次
     *
     * @param shopId
     * @param msg
     */
    private void sendMsg(final long shopId, final String msg) {
        for (int i = 2; i > 0; i--) {
            log.info("店铺[{}], 第一次[{}], 推送消息", shopId, i);
            final Socket socket = this.getSocket(shopId);
            try {
                ClientSocketUtil.sendMsg(socket, msg);
                return;
            } catch (final IOException e) {
                this.removeSocket(shopId);
                log.error("店铺[" + shopId + "]-推送socket消息[ " + msg + " ]异常", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     */
    @Override
    public String readSocketMsg(final long shopId) {
        final Socket socket = this.getSocket(shopId);
        try {
            return ClientSocketUtil.readMsg(socket);
        } catch (final IOException e) {
            log.error("读取socket信息异常", e);
        }
        return null;
    }
}
