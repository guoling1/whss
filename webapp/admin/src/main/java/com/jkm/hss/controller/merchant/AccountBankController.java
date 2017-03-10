package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.requestparam.ChangeBankCardRequest;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.service.AccountBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xingliujie on 2017/3/7.
 */
@Controller
@RequestMapping(value = "/admin/accountBank")
public class AccountBankController extends BaseController {
    @Autowired
    private AccountBankService accountBankService;

    /**
     * 更改银行卡信息
     * @param changeBankCardRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeBankCard", method = RequestMethod.POST)
    public CommonResponse changeBankCard (@RequestBody ChangeBankCardRequest changeBankCardRequest) {
        accountBankService.changeBankCard(changeBankCardRequest.getAccountId(),changeBankCardRequest.getBankNo(),changeBankCardRequest.getReserveMobile());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "更改成功");
    }
}
