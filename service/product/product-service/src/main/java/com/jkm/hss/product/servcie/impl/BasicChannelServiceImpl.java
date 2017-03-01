package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.BasicChannelDao;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-24.
 */
@Slf4j
@Service
public class BasicChannelServiceImpl implements BasicChannelService {

    @Autowired
    private BasicChannelDao basicChannelDao;

    /**
     * {@inheritDoc}
     * @param basicChannel
     */
    @Override
    public void init(BasicChannel basicChannel) {
        this.basicChannelDao.init(basicChannel);
    }

    /**
     * {@inheritDoc}
     * @param payChannel
     * @return
     */
    @Override
    public Optional<BasicChannel> selectByChannelTypeSign(int payChannel) {
        return Optional.fromNullable(this.basicChannelDao.selectByChannelTypeSign(payChannel));
    }

    /**
     * {@inheritDoc}
     * @param basicChannel
     */
    @Override
    public void add(BasicChannel basicChannel) {
        this.basicChannelDao.init(basicChannel);
    }

    /**
     * {@inheritDoc}
     *
     * @param channelSource
     * @return
     */
    @Override
    public Optional<BasicChannel> selectByChannelSource(String channelSource) {
        return Optional.fromNullable(this.basicChannelDao.selectByChannelSource(channelSource));
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<BasicChannel> selectAll() {
        return this.basicChannelDao.selectAll();
    }

    /**
     * {@inheritDoc}
     * @param basicChannel
     */
    @Override
    public void update(BasicChannel basicChannel) {
        this.basicChannelDao.update(basicChannel);
    }

    @Override
    public BasicChannel selectById(long id) {
        return this.basicChannelDao.selectById(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param channelSign
     * @param type
     * @return
     */
    @Override
    public String selectCodeByChannelSign(int channelSign, EnumMerchantPayType type) {

        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(channelSign);
        switch (type){

            case MERCHANT_CODE:
                return enumPayChannelSign.getCode() + "_code";

            case MERCHANT_JSAPI:
                return enumPayChannelSign.getCode() + "_jsapi";

        }
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @param payType
     * @return
     */
    @Override
    public EnumPayChannelSign getEnumPayChannelSignByCode(final String payType) {
        if (payType.contains("_code")) {
            return EnumPayChannelSign.codeOf(payType.substring(0, payType.indexOf("_code")));
        } else if (payType.contains("_jsapi")) {
            return EnumPayChannelSign.codeOf(payType.substring(0, payType.indexOf("_jsapi")));
        }
        log.error("支付方式[{}]，异常", payType);
        return null;
    }
}
