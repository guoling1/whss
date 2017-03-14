package com.jkm.hss.product.servcie.impl;

import com.jkm.hss.product.dao.ChannelSupportCreditBankDao;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/10.
 */
@Service
public class ChannelSupportCreditBankServiceImpl implements ChannelSupportCreditBankService {

    @Autowired
    private ChannelSupportCreditBankDao channelSupportCreditBankDao;

    /**
     * {@inheritDoc}
     *
     * @param channelSign
     * @return
     */
    @Override
    public List<ChannelSupportCreditBank> getByChannelSign(final int channelSign) {
        return this.channelSupportCreditBankDao.selectByChannelSign(channelSign);
    }

    /**
     * {@inheritDoc}
     *
     * @param channel
     * @param bankName
     * @return
     */
    @Override
    public boolean isExistByChannelSignAndBankName(final int channel, final String bankName) {
        return this.channelSupportCreditBankDao.selectByChannelSignAndBankName(channel, bankName) > 0;
    }
}
