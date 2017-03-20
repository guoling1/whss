package com.jkm.hss.controller.bankcard;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.response.CreditCardListResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.service.AccountBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 信用卡列表
     *
     * @param creditCardId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list/{creditCardId}")
    public CommonResponse listCreditCard(@PathVariable("creditCardId") long creditCardId) {
        final AccountBank accountBank = this.accountBankService.selectById(creditCardId).get();
        final List<AccountBank> accountBanks = this.accountBankService.selectCreditList(accountBank.getAccountId());
        final List<CreditCardListResponse> responses = Lists.transform(accountBanks, new Function<AccountBank, CreditCardListResponse>() {
            @Override
            public CreditCardListResponse apply(AccountBank accountBank) {
                final CreditCardListResponse creditCardListResponse = new CreditCardListResponse();
                creditCardListResponse.setCreditCardId(accountBank.getId());
                creditCardListResponse.setBankName(accountBank.getBankName());
                creditCardListResponse.setBankCode(accountBank.getBankBin());
                creditCardListResponse.setShortNo(accountBank.getBankNo().substring(accountBank.getBankNo().length() - 4));
                final String mobile = accountBank.getReserveMobile();
                creditCardListResponse.setMobile(mobile.substring(0, 2) + "**** ***" + mobile.substring(mobile.length() - 2));
                return creditCardListResponse;
            }
        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responses);
    }
}
