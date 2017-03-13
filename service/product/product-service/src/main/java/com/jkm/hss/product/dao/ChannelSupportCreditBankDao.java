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

    List<ChannelSupportCreditBank> selectByChannelSign(@Param("channelSign") int channelSign);
}
