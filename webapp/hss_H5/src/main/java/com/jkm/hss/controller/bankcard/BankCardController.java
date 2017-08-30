package com.jkm.hss.controller.bankcard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.OpenCardRequest;
import com.jkm.api.helper.responseparam.OpenCardResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.OpenCardService;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.helper.request.CreditCardListRequest;
import com.jkm.hss.helper.response.CreditCardListResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/14.
 */
@Slf4j
@Controller
@RequestMapping(value = "bankcard")
public class BankCardController extends BaseController {

    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private ChannelSupportCreditBankService channelSupportCreditBankService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private OpenCardService openCardService;
    /**
     * 信用卡列表
     *
     * @param creditCardListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse listCreditCard(@RequestBody CreditCardListRequest creditCardListRequest) {
        final AccountBank accountBank = this.accountBankService.selectById(creditCardListRequest.getCreditCardId()).get();
        List<AccountBank> accountBanks;
        if (creditCardListRequest.getChannel() == EnumPayChannelSign.JH_UNIONPAY.getId()){
            accountBanks = this.accountBankService.selectCreditList2Token(accountBank.getAccountId());
        }else{
            accountBanks = this.accountBankService.selectCreditList(accountBank.getAccountId());
        }

        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(creditCardListRequest.getChannel());
        final List<ChannelSupportCreditBank> channelSupportCreditBankList =
                this.channelSupportCreditBankService.getByUpperChannel(EnumPayChannelSign.idOf(parentChannelSign).getUpperChannel().getId());
        final ImmutableMap<String, ChannelSupportCreditBank> creditBankImmutableMap = Maps.uniqueIndex(channelSupportCreditBankList,
                new Function<ChannelSupportCreditBank, String>() {
            @Override
            public String apply(ChannelSupportCreditBank channelSupportCreditBank) {
                return channelSupportCreditBank.getBankCode();
            }
        });
        final List<CreditCardListResponse> responses = Lists.transform(accountBanks, new Function<AccountBank, CreditCardListResponse>() {
            @Override
            public CreditCardListResponse apply(AccountBank accountBank) {
                final CreditCardListResponse creditCardListResponse = new CreditCardListResponse();
                final ChannelSupportCreditBank channelSupportCreditBank = creditBankImmutableMap.get(accountBank.getBankBin());
                creditCardListResponse.setCreditCardId(accountBank.getId());
                creditCardListResponse.setBankName(accountBank.getBankName());
                creditCardListResponse.setBankCode(accountBank.getBankBin());
                if (null != channelSupportCreditBank) {
                    creditCardListResponse.setStatus(channelSupportCreditBank.getStatus());
                } else {
                    creditCardListResponse.setStatus(EnumBoolean.FALSE.getCode());
                }
                creditCardListResponse.setShortNo(accountBank.getBankNo().substring(accountBank.getBankNo().length() - 4));
                final String mobile = accountBank.getReserveMobile();
                creditCardListResponse.setMobile(mobile.substring(0, 2) + "**** ***" + mobile.substring(mobile.length() - 2));
                return creditCardListResponse;
            }
        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responses);
    }

    /**
     * H5快捷支付绑卡
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "openCard", method = RequestMethod.POST)
    public CommonResponse openCard(final HttpServletRequest httpServletRequest) {
        final JSONObject openCardResponse = new JSONObject();
        String readParam;
        OpenCardRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, OpenCardRequest.class);
        } catch (final IOException e) {
            log.error("快捷绑卡读取数据流异常", e);
            return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "开卡失败");
        }
        log.info("快捷绑卡入网参数[{}]", JSON.toJSON(request).toString());
        try {
            request.setBindCardReqNo(SnGenerator.generate());
            String html = openCardService.kuaiPayOpenCard(request);
            openCardResponse.put("html",html);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", openCardResponse);
        } catch (final Throwable e) {
            log.error("快捷绑卡异常", e);
        }
        return CommonResponse.simpleResponse(CommonResponse.FAIL_CODE, "开卡失败");
    }
}
