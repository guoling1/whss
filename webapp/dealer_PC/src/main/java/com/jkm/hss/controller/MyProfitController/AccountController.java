package com.jkm.hss.controller.MyProfitController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.helper.request.ProfitDetailsSelectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yuxiang on 2017-02-14.
 */
@RequestMapping(value = "/daili/account")
@RestController
public class AccountController extends BaseController{

    @Autowired
    private AccountService accountService;
    /**
     * 獲取帳戶詳情，
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public CommonResponse withdraw(){

        final Dealer dealer = this.getDealer().get();
        final Account account = this.accountService.getById(dealer.getAccountId()).get();
        return CommonResponse.objectResponse(0, "SUCCESS", account);
    }
}
