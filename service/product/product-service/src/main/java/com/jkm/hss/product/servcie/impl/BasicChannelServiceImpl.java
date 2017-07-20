package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.jkm.hss.product.dao.BasicChannelDao;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
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
    //@Cacheable(cacheName="channelCache")
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
    //@TriggersRemove(cacheName="channelCache",  removeAll=true)
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
        if (enumPayChannelSign.getPaymentChannel().getId() == EnumPaymentChannel.QQPAY.getId()){
            return enumPayChannelSign.getCode() + "_code";
        }
        switch (type){
            case MERCHANT_CODE:
                return enumPayChannelSign.getCode() + "_code";
            case MERCHANT_JSAPI:
                return enumPayChannelSign.getCode() + "_jsapi";
            case MERCHANT_BAR:
                return enumPayChannelSign.getCode() + "_" + type.getId();

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
        } else if (payType.contains("_bar")) {
            return EnumPayChannelSign.codeOf(payType.substring(0, payType.indexOf("_bar")));
        } else {
            return EnumPayChannelSign.codeOf(payType);
        }
    }

    /**
     * 查询好收银通道
     *
     * @return
     */
    @Override
    public List<BasicChannel> selectHsyAll() {
        return this.basicChannelDao.selectHsyAll();
    }

    @Override
    public int selectParentChannelSign(int channelSign) {
        final BasicChannel basicChannel = this.basicChannelDao.selectParentChannelSign(channelSign);
        final int parentChannelSign = basicChannel.getChannelTypeSign();
        if (parentChannelSign == 0){
            return channelSign;
        }
        return parentChannelSign;
    }

    @Override
    public BasicChannel selectParentChannel(int channelSign) {
        return this.basicChannelDao.selectParentChannelSign(channelSign);
    }
//    @Override
//    public List<BasicChannel> selectListChannel() {
//        return this.basicChannelDao.selectListChannel();
//    }

//    @Override
//    public BasicChannel selectChannel(int channelTypeSign) {
//        return this.basicChannelDao.selectChannel(channelTypeSign);
//    }


}
