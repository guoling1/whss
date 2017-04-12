package com.jkm.hss.controller.bankcard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.CreditCardListRequest;
import com.jkm.hss.helper.response.CreditCardListResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.product.entity.ChannelSupportCreditBank;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        final List<AccountBank> accountBanks = this.accountBankService.selectCreditList(accountBank.getAccountId());
        final List<ChannelSupportCreditBank> channelSupportCreditBankList =
                this.channelSupportCreditBankService.getByUpperChannel(EnumPayChannelSign.idOf(creditCardListRequest.getChannel()).getUpperChannel().getId());
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
}
