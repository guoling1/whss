package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.ChannelSupportDebitCardDao;
import com.jkm.hss.product.entity.ChannelSupportDebitCard;
import com.jkm.hss.product.servcie.ChannelSupportDebitCardService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2017-05-16.
 */
@Service
@Slf4j
public class ChannelSupportDebitCardServiceImpl implements ChannelSupportDebitCardService{

    @Autowired
    private ChannelSupportDebitCardDao channelSupportDebitCardDao;

    @Override
    public void add(ChannelSupportDebitCardService channelSupportDebitCard) {

    }

    @Override
    public Optional<ChannelSupportDebitCard> selectByBankCode(String bankCode) {
        return Optional.fromNullable(this.channelSupportDebitCardDao.selectByBankCode(bankCode));
    }
}
