package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.bill.entity.AccountDetailsResponse;
import com.jkm.hss.bill.service.ShareProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lt on 2016/12/7.
 */
@Slf4j

@Controller
@RequestMapping(value = "/admin/queryJkmProfit")
public class JkmProfitController extends BaseController{

    @Autowired
    private ShareProfitService shareProfitService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 金开门账户
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/accountList",method = RequestMethod.POST)
    public CommonResponse accountList(@RequestBody JkmProfitRequest req) throws ParseException {
        final PageModel<Account> pageModel = new PageModel<Account>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());

        List<Account> orderList =  shareProfitService.selectAccountList(req);
        int count = shareProfitService.selectAccountListCount(req);
        if (orderList.size()==0){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 金开门账户明细
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/accountDetails",method = RequestMethod.POST)
    public CommonResponse accountDetails(@RequestBody ProfitDetailsRequest req) throws ParseException {
        final PageModel<AccountDetailsResponse> pageModel = new PageModel<AccountDetailsResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<AccountDetailsResponse> orderList =  shareProfitService.selectAccountDetails(req);
        if (orderList==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = shareProfitService.selectAccountDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }


//    /**
//     * 分润明细
//     * @param req
//     * @return
//     * @throws ParseException
//     */
//    @ResponseBody
//    @RequestMapping(value = "/profitDetails",method = RequestMethod.POST)
//    public CommonResponse profitDetails(ProfitDetailsRequest req) throws ParseException {
//        final PageModel<JkmProfitDetailsResponse> pageModel = new PageModel<JkmProfitDetailsResponse>(req.getPageNo(), req.getPageSize());
//        req.setOffset(pageModel.getFirstIndex());
//        List<JkmProfitDetailsResponse> orderList =  shareProfitService.selectProfitDetails(req);
//        int count = shareProfitService.selectProfitDetailsCount(req);
//        pageModel.setCount(count);
//        pageModel.setRecords(orderList);
//
//        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
//    }



}

