package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.ShopSocket;

import java.net.Socket;

/**
 * Created by yulong.zhang on 2017/7/24.
 */
public interface ShopSocketService {

    /**
     * 初始化socketMap
     */
    void init();

    /**
     * 创建socket
     *
     * @param shopId
     * @param ip
     * @param port
     */
    Socket putSocket(long shopId, String ip, int port);

    /**
     * 删除socket
     *
     * @param shopId
     */
    void removeSocket(long shopId);

    /**
     * 获得socket
     *
     * @param shopId
     * @return
     */
    Socket getSocket(long shopId);

    /**
     * 插入
     *
     * @param shopSocket
     */
    void add(ShopSocket shopSocket);

    /**
     * 更新
     *
     * @param shopSocket
     * @return
     */
    int update(ShopSocket shopSocket);

    /**
     * 更新
     *
     * @param shopSocket
     * @return
     */
    int updateByShopId(ShopSocket shopSocket);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<ShopSocket> getById(long id);

    /**
     * an店铺id查询
     *
     * @param shopId
     * @return
     */
    Optional<ShopSocket> getByShopId(long shopId);

    /**
     * 保存ip
     *
     * @param shopId
     * @param ip
     * @param port
     */
    void saveSocketIp(long shopId, String ip, int port);

    /**
     * 发送消息
     *
     * @param shopId
     * @param msg
     */
    void sendSocketMsg(long shopId, String msg);

    /**
     * 读取socket消息
     *
     * @param shopId
     */
    String readSocketMsg(long shopId);
}
