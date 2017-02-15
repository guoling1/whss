package com.jkm.hss.controller.MyProfitController;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.helper.request.FlowDetailsSelectRequest;
import com.jkm.hss.helper.request.ProfitDetailsSelectRequest;
import com.jkm.hss.helper.response.FlowDetailsSelectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yuxiang on 2017-02-14.
 */
@RequestMapping(value = "/daili/account")
@RestController
public class AccountController extends BaseController{

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    /**
     * 獲取帳戶詳情，
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResponse withdraw(){

        final Dealer dealer = this.getDealer().get();
        final Account account = this.accountService.getById(dealer.getAccountId()).get();
        return CommonResponse.objectResponse(0, "SUCCESS", account);
    }

    /**
     * 代理商资金账户变更记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/flowDetails", method = RequestMethod.GET)
    public CommonResponse flowDetails(@RequestBody final FlowDetailsSelectRequest request){

        final Dealer dealer = this.getDealer().get();
        PageModel<AccountFlow> pageModel = this.accountFlowService.selectByParam(request.getPageNo(), request.getPageSize(),
            dealer.getAccountId(), request.getFlowSn(), request.getType(), request.getBeginDate(), request.getEndDate());

        final List<AccountFlow> records = pageModel.getRecords();

        List<FlowDetailsSelectResponse> list = Lists.transform(records, new Function<AccountFlow, FlowDetailsSelectResponse>() {
            @Override
            public FlowDetailsSelectResponse apply(AccountFlow input) {
                FlowDetailsSelectResponse response = new FlowDetailsSelectResponse();
                response.setFlowSn(input.getOrderNo());
                response.setCreateTime(DateFormatUtil.format(input.getCreateTime(),DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
                response.setBeforeAmount(input.getBeforeAmount().toString());
                response.setIncomeAmount(input.getIncomeAmount().toString());
                response.setOutAmount(input.getOutAmount().toString());
                response.setRemark("");
                return response;
            }
        });

        PageModel<FlowDetailsSelectResponse> model = new PageModel<>(pageModel.getPageNO(),pageModel.getPageSize());
        model.setCount(pageModel.getCount());
        model.setRecords(list);

        return CommonResponse.objectResponse(0, "success", model);
    }


    }
