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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public CommonResponse getCompanyProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
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
    @RequestMapping(value = "/companyProfitDetail", method = RequestMethod.POST)
    public CommonResponse getCompanyProfitDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectCompanyProfitDetails(req.getAccId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", res);
        }
    /**
     * 一级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstProfit", method = RequestMethod.POST)
    public CommonResponse getFirstProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CompanyProfitResponse> list = allProfitService.selectOneProfit(req);
        if (list==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = allProfitService.selectOneProfitCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);

    }

    /**
     * 一级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/firstDealerDetail", method = RequestMethod.POST)
    public CommonResponse getFirstDealerDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectOneProfitDetails(req.getReceiptMoneyAccountId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", res);
    }

    /**
     * 二级代理商分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondProfit", method = RequestMethod.POST)
    public CommonResponse getSecondProfit(@RequestBody final CompanyPrifitRequest req) throws ParseException {
        final PageModel<CompanyProfitResponse> pageModel = new PageModel<CompanyProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CompanyProfitResponse> list = allProfitService.selectTwoProfit(req);
        if (list==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        int count = allProfitService.selectTwoProfitCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 二级代理分润详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/secondDealerDetail", method = RequestMethod.POST)
    public CommonResponse getSecondDealerDeatail(@RequestBody final CompanyPrifitRequest req){

        CompanyProfitResponse res = allProfitService.selectTwoProfitDetails(req.getReceiptMoneyAccountId());
        if (res==null){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", res);
    }
}
