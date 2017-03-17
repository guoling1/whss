package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.product.dao.ChannelSupportCreditBankDao;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.helper.requestparam.QuerySupportBankParams;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status) {
        return this.channelSupportCreditBankDao.updateStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ChannelSupportCreditBank> getById(final long id) {
        return Optional.fromNullable(this.channelSupportCreditBankDao.selectById(id));
    }

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
        querySupportBankParams.setOffset(result.getFirstIndex());
        querySupportBankParams.setCount(result.getPageSize());
        final int count = this.channelSupportCreditBankDao.selectCountByParam(querySupportBankParams);
        final List<ChannelSupportCreditBank> channelSupportCreditBanks = this.channelSupportCreditBankDao.selectByParam(querySupportBankParams);
        if (CollectionUtils.isEmpty(channelSupportCreditBanks)) {
            result.setCount(0);
            result.setRecords(Collections.<ChannelSupportCreditBank>emptyList());
        }
        result.setCount(count);
        result.setRecords(channelSupportCreditBanks);
        return result;
    }
}
