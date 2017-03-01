package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumMerchantPayType;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-23.
 */
public interface BasicChannelService {

    /**
     * 初始化
     * @param basicChannel
     */
    void init(BasicChannel basicChannel);

    /**
     * 查询通道
     * @param payChannel
     * @return
     */
    Optional<BasicChannel> selectByChannelTypeSign(int payChannel);

    /**
     * 添加
     * @param basicChannel
     */
    void add(BasicChannel basicChannel);

    /**
     * 根据通道来源查询
     * @param channelSource
     * @return
     */
    Optional<BasicChannel> selectByChannelSource(String channelSource);

    /**
     * 查询
     * @return
     */
    List<BasicChannel> selectAll();

    /**
     *
     * @param basicChannel
     */
    void update(BasicChannel basicChannel);

    BasicChannel selectById(long id);

    /**
     *  根据通道，扫码方式选择 传给支付中心的 code
     *
     * @param channelSign
     * @param type
     * @return
     */
    String selectCodeByChannelSign(int channelSign, EnumMerchantPayType type);

    /**
     * 查询产品中添加通道列表
     * @return
     */
//    List<BasicChannel> selectListChannel();

    /**
     * 查询通道
     * @param channelTypeSign
     * @return
     */
//    BasicChannel selectChannel(int channelTypeSign);
}
