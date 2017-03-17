package com.jkm.hss.product.servcie.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.product.dao.ChannelSupportCreditBankDao;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.helper.requestparam.QuerySupportBankParams;
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
     * @param upperChannel
     * @return
     */
    @Override
    public List<ChannelSupportCreditBank> getByUpperChannel(final int upperChannel) {
        return this.channelSupportCreditBankDao.selectByUpperChannel(upperChannel);
    }

    /**
     * {@inheritDoc}
     *
     * @param upperChannel
     * @param bankName
     * @return
     */
    @Override
    public boolean isExistByUpperChannelAndBankName(final int upperChannel, final String bankName) {
        return this.channelSupportCreditBankDao.selectByUpperChannelAndBankName(upperChannel, bankName) > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param querySupportBankParams
     * @return
     */
    @Override
    public PageModel<ChannelSupportCreditBank> querySupportBank(final QuerySupportBankParams querySupportBankParams) {
        final PageModel<ChannelSupportCreditBank> result = new PageModel<>(querySupportBankParams.getPageNo(), querySupportBankParams.getPageSize());
        final int count = this.channelSupportCreditBankDao.selectCountByParam(querySupportBankParams);
        return result;
    }
}
