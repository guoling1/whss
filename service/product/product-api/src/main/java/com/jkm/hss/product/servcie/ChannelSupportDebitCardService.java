package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.ChannelSupportDebitCard;

/**
 * Created by yuxiang on 2017-05-16.
 */
public interface ChannelSupportDebitCardService {

    void add(ChannelSupportDebitCardService channelSupportDebitCard);

    Optional<ChannelSupportDebitCard> selectByBankCode(String bankCode);
}
