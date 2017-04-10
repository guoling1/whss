package com.jkm.hss.controller.channel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.QueryChannelSupportBankRequest;
import com.jkm.hss.helper.response.QueryChannelSupportBankResponse;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;


/**
 * Created by yulong.zhang on 2017/3/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "channel")
public class ChannelController extends BaseController {

    @Autowired
    private ChannelSupportCreditBankService channelSupportCreditBankService;

    /**
     * 查询当前快捷通道支持的信用卡银行列表
     *
     * @param queryChannelSupportBankRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryChannelSupportBank", method = RequestMethod.POST)
    public CommonResponse queryChannelSupportBank(@RequestBody final QueryChannelSupportBankRequest queryChannelSupportBankRequest) {
        final List<ChannelSupportCreditBank> channelSupportCreditBankList =
                this.channelSupportCreditBankService.getByUpperChannel(EnumPayChannelSign.idOf(queryChannelSupportBankRequest.getChannelSign()).getUpperChannel().getId());
        if (CollectionUtils.isEmpty(channelSupportCreditBankList)) {
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", Collections.emptyList());
        }
        final List<QueryChannelSupportBankResponse> responseList = Lists.transform(channelSupportCreditBankList, new Function<ChannelSupportCreditBank, QueryChannelSupportBankResponse>() {
            @Override
            public QueryChannelSupportBankResponse apply(ChannelSupportCreditBank channelSupportCreditBank) {
                final QueryChannelSupportBankResponse supportBankResponse = new QueryChannelSupportBankResponse();
                supportBankResponse.setId(channelSupportCreditBank.getId());
                supportBankResponse.setBankName(channelSupportCreditBank.getBankName());
                supportBankResponse.setBankCode(channelSupportCreditBank.getBankCode());
                supportBankResponse.setSingleLimitAmount(channelSupportCreditBank.getSingleLimitAmount().toPlainString());
                supportBankResponse.setDayLimitAmount(channelSupportCreditBank.getDayLimitAmount().toPlainString());
                supportBankResponse.setStatus(channelSupportCreditBank.getStatus());
                return supportBankResponse;
            }
        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responseList);
    }
}
