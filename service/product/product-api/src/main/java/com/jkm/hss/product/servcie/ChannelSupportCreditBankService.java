package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.helper.requestparam.QuerySupportBankParams;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/10.
 */
public interface ChannelSupportCreditBankService {


    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(long id, int status);

    /**
     * 按ID 查询
     *
     * @param id
     * @return
     */
    Optional<ChannelSupportCreditBank> getById(long id);

    /**
     * 按通道标志查询
     *
     * @param channelSign
     * @return
     */
    List<ChannelSupportCreditBank> getByUpperChannel(int channelSign);

    /**
     * 当前渠道是否支持，当前银行
     *
     * @param channel
     * @param bankName
     */
    boolean isExistByUpperChannelAndBankName(int channel, String bankName);

    /**
     * 行用卡-支持银行列表
     *
     * @param querySupportBankParams
     * @return
     */
    PageModel<ChannelSupportCreditBank> querySupportBank(QuerySupportBankParams querySupportBankParams);
}
