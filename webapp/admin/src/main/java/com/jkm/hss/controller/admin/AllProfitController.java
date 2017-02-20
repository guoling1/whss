package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import com.jkm.hss.merchant.service.AllProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by yuxiang on 2016-12-08.
 */
@RequestMapping(value = "/admin/allProfit")
@Controller
@Slf4j
public class AllProfitController extends BaseController {

    @Autowired
    private AllProfitService allProfitService;
    /**
     * 公司分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit", method = RequestMethod.POST)
    public CommonResponse getCompanyProfit(@RequestBody final CompanyPrifitRequest req){
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<CompanyProfitResponse> list = allProfitService.selectCompanyProfit(req);
        if (list==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = allProfitService.selectCompanyProfitCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);

    }

    /**
     * 公司分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/companyProfit/detail", method = RequestMethod.POST)
    public CommonResponse getCompanyProfitDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectCompanyProfitDetails(req.getAccId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
            return CommonResponse.simpleResponse(-1, "查询公司分润明细异常");
        }
    /**
     * 一级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstProfit", method = RequestMethod.POST)
    public CommonResponse getFirstProfit(@RequestBody final CompanyPrifitRequest req){
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<CompanyProfitResponse> list = allProfitService.selectOneProfit(req);
        if (list==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = allProfitService.selectOneProfitCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.simpleResponse(-1, "查询一级代理商分润异常");

    }

    /**
     * 一级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstDealer/detail", method = RequestMethod.POST)
    public CommonResponse getFirstDealerDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectOneProfitDetails(req.getReceiptMoneyAccountId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        return CommonResponse.simpleResponse(-1, "查询一代分润明细异常");
    }

    /**
     * 二级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondProfit", method = RequestMethod.POST)
    public CommonResponse getSecondProfit(@RequestBody final CompanyPrifitRequest req){
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<CompanyProfitResponse> list = allProfitService.selectTwoProfit(req);
        if (list==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = allProfitService.selectTwoProfitCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.simpleResponse(-1, "查询二级代理商分润异常");
    }

    /**
     * 二级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondDealer/detail", method = RequestMethod.POST)
    public CommonResponse getSecondDealerDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectTwoProfitDetails(req.getReceiptMoneyAccountId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        return CommonResponse.simpleResponse(-1, "查询二代分润明细异常");
    }
}
