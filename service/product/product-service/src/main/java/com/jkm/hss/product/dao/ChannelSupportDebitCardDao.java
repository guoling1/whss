package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ChannelSupportDebitCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yuxiang on 2017-05-16.
 */
@Repository
public interface ChannelSupportDebitCardDao {


    ChannelSupportDebitCard selectByBankCode(@Param("bankCode") String bankCode);
}
