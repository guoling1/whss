package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/10.
 */
@Repository
public interface ChannelSupportCreditBankDao {

    /**
     * 查询当前通道支持的银行
     *
     * @param channelSign
     * @return
     */
    List<ChannelSupportCreditBank> selectByChannelSign(@Param("channelSign") int channelSign);

    /**
     * 查询当前通道，是否支持当前银行
     *
     * @param channel
     * @param bankName
     * @return
     */
    int selectByChannelSignAndBankName(@Param("channel") int channel, @Param("bankName") String bankName);
}
