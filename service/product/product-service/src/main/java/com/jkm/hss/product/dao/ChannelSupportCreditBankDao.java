package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.helper.requestparam.QuerySupportBankParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/10.
 */
@Repository
public interface ChannelSupportCreditBankDao {

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    ChannelSupportCreditBank selectById(@Param("id") long id);

    /**
     * 查询当前通道支持的银行
     *
     * @param upperChannel
     * @return
     */
    List<ChannelSupportCreditBank> selectByUpperChannel(@Param("upperChannel") int upperChannel);

    /**
     * 查询当前通道，是否支持当前银行
     *
     * @param upperChannel
     * @param bankCode
     * @return
     */
    int selectByUpperChannelAndBankCode(@Param("upperChannel") int upperChannel, @Param("bankCode") String bankCode);

    /**
     * 列表-个数
     *
     * @param querySupportBankParams
     * @return
     */
    int selectCountByParam(QuerySupportBankParams querySupportBankParams);

    /**
     * 列表-集合
     *
     * @param querySupportBankParams
     * @return
     */
    List<ChannelSupportCreditBank> selectByParam(QuerySupportBankParams querySupportBankParams);
}
