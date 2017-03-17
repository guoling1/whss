package com.jkm.hss.product.servcie;

import com.jkm.hss.product.entity.ChannelSupportCreditBank;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/10.
 */
public interface ChannelSupportCreditBankService {

    /**
     * 按通道标志查询
     *
     * @param channelSign
     * @return
     */
    List<ChannelSupportCreditBank> getByUpperChannel(int channelSign);

    /**
     * 当前渠道是否支持，当前银行
     *
     * @param channel
     * @param bankName
     */
    boolean isExistByUpperChannelAndBankName(int channel, String bankName);
}
