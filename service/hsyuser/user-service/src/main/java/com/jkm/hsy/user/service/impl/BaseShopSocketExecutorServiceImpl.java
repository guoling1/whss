package com.jkm.hsy.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hsy.user.service.ShopSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yulong.zhang on 2017/7/24.
 *
 * socket线程池实现
 */
@Slf4j
@Service
public class BaseShopSocketExecutorServiceImpl implements BaseShopSocketExecutorService {

    @Autowired
    private ShopSocketService shopSocketService;
    /**
     * 线程池，固定大小20
     */
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(20);


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
                    shopSocketService.sendSocketMsg(shopId, msg);
                    final String resultMsg = shopSocketService.readSocketMsg(shopId);
                    if (!StringUtils.isEmpty(resultMsg)) {
                        final JSONObject jsonObject = JSONObject.parseObject(resultMsg);
                        final int code = jsonObject.getIntValue("code");
                        final String tradeOrderNo = jsonObject.getString("tradeOrderNo");
                        if (1 == code) {
                            log.info("店铺[{}]--订单--交易[{}]，推送成功", shopId, tradeOrderNo);
                            return;
                        }
                        log.error("店铺[{}]--订单--交易[{}]，推送失败-[{}]", shopId, tradeOrderNo, msg);
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
}
