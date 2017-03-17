package com.jkm.hss.controller.channel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.product.helper.requestparam.QuerySupportBankParams;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.ChannelAddRequest;
import com.jkm.hss.helper.response.QuerySupportBankResponse;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Controller
@RequestMapping(value = "/admin/channel")
@Slf4j
public class ChannelController extends BaseController {

    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ChannelSupportCreditBankService channelSupportCreditBankService;
    /**
     * 添加通道
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody final ChannelAddRequest request) {
        return null;
    }

    /**
     * 获取通道列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list() {
        try{
            final List<BasicChannel> list = this.basicChannelService.selectAll();
            if (list.size()>0){
                for (int i=0;i<list.size();i++){
                    BigDecimal basicTradeRate = list.get(i).getBasicTradeRate();
                    BigDecimal res = new BigDecimal(100);
                    list.get(i).setBasicTradeRate(basicTradeRate.multiply(res));
                }
            }
            return  CommonResponse.objectResponse(1, "success", list);
        }catch (final Throwable throwable){
            log.error("获取通道列表异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 修改通道
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody final BasicChannel request) {
        try{
            final BasicChannel basicChannel = this.basicChannelService.selectById(request.getId());
            request.setBasicTradeRate(request.getBasicTradeRate().divide(new BigDecimal(100)));
            request.setChannelTypeSign(basicChannel.getChannelTypeSign());
            request.setChannelCompany(EnumPayChannelSign.of(request.getChannelName()).getUpperChannel().getValue());
            this.basicChannelService.update(request);
            return  CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("修改通道,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }


    /**
     * 快捷信用卡-支持银行列表
     *
     * @param querySupportBankParams
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "querySupportBank", method = RequestMethod.POST)
    public CommonResponse querySupportBank(@RequestBody QuerySupportBankParams querySupportBankParams) {
        final PageModel<QuerySupportBankResponse> result = new PageModel<>(querySupportBankParams.getPageNo(), querySupportBankParams.getPageSize());
        if (StringUtils.isEmpty(querySupportBankParams.getChannelName())) {
            querySupportBankParams.setChannelName(null);
        }
        if (StringUtils.isEmpty(querySupportBankParams.getChannelCode())) {
            querySupportBankParams.setChannelCode(null);
        }
        if (StringUtils.isEmpty(querySupportBankParams.getBankCode())) {
            querySupportBankParams.setBankCode(null);
        }
        final PageModel<ChannelSupportCreditBank> page = this.channelSupportCreditBankService.querySupportBank(querySupportBankParams);
        final List<ChannelSupportCreditBank> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            result.setCount(0);
            result.setRecords(Collections.<QuerySupportBankResponse>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }
        final List<QuerySupportBankResponse> responses = Lists.transform(records, new Function<ChannelSupportCreditBank, QuerySupportBankResponse>() {
            @Override
            public QuerySupportBankResponse apply(ChannelSupportCreditBank channelSupportCreditBank) {
                final QuerySupportBankResponse querySupportBankResponse = new QuerySupportBankResponse();
                querySupportBankResponse.setChannelName(channelSupportCreditBank.getUpperChannelName());
                querySupportBankResponse.setChannelCode(channelSupportCreditBank.getUpperChannelCode());
                querySupportBankResponse.setBankCode(channelSupportCreditBank.getBankCode());
                querySupportBankResponse.setBankName(channelSupportCreditBank.getBankName());
                querySupportBankResponse.setCardType(channelSupportCreditBank.getBankCardType());
                querySupportBankResponse.setSingleLimitAmount(channelSupportCreditBank.getSingleLimitAmount().toPlainString());
                querySupportBankResponse.setDayLimitAmount(channelSupportCreditBank.getDayLimitAmount().toPlainString());
                querySupportBankResponse.setStatus(channelSupportCreditBank.getStatus());
                return querySupportBankResponse;
            }
        });
        result.setCount(page.getCount());
        result.setRecords(responses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

}
