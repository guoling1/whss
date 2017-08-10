package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Objects;

/**
 * Created by yulong.zhang on 2017/7/24.
 *
 * 店铺 socket 推送
 *
 * tb_shop_socket
 */
@Data
public class ShopSocket extends BaseEntity {

    /**
     * 店铺id
     */
    private long shopId;
    /**
     * 公网ip
     */
    private String ip;
    /**
     * socket端口
     */
    private int port;
    /**
     * pc标识
     */
    private String pc;

    /**
     * 是否是同一个公网ip，端口
     *
     * @param ip
     * @param port
     * @return
     */
    public boolean isSame(final String ip, final int port) {
        return Objects.equals(this.ip, ip) && Objects.equals(this.port, port);
    }
}
