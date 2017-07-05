package com.jkm.hss.controller.MyProfitController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.ProfitCountRequest;
import com.jkm.hss.account.entity.ProfitCountRespons;
import com.jkm.hss.account.entity.ProfitDetailCountRespons;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
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
 * Created by zhangbin on 2017/5/22.
 * 分润统计
 */
@Controller
@RequestMapping(value = "/daili/profitCount")
public class ProfitCountController extends BaseController {

    @Autowired
    private SplitAccountRecordService splitAccountRecordService;

    /**
     * 分润统计
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCount", method = RequestMethod.POST)
    public CommonResponse getCount(@RequestBody ProfitCountRequest request) throws ParseException {
        final PageModel<ProfitCountRespons> pageModel = new PageModel<ProfitCountRespons>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        final Dealer dealer = super.getDealer().get();
        final long accountId = dealer.getAccountId();
        int level = dealer.getLevel();
        int oemType = dealer.getOemType();
//        Long oemId = dealer.getOemId();

        if (level==2) {
            request.setAccountId(accountId);
            int count = this.splitAccountRecordService.getProfitCount(request);
            if (count>0) {
                List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
                pageModel.setCount(count);
                pageModel.setRecords(list);
                return CommonResponse.objectResponse(1, "success", pageModel);
            }

        }
        if (level==1){
            if (oemType==0) {
                request.setAccountId(accountId);
                int count = this.splitAccountRecordService.getProfitCount(request);
                if (count>0) {
                    List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
                    pageModel.setCount(count);
                    pageModel.setRecords(list);
                    return CommonResponse.objectResponse(1, "success", pageModel);
                }

            }
        }
        if (oemType==1){
            request.setAccountId(accountId);
            int count = this.splitAccountRecordService.getProfitCount(request);
            if (count>0) {
                List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
                pageModel.setCount(count);
                pageModel.setRecords(list);
                return CommonResponse.objectResponse(1, "success", pageModel);
            }

        }

        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     * 分润详情
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCountDetails", method = RequestMethod.POST)
    public CommonResponse getCountDetails(@RequestBody ProfitCountRequest request) throws ParseException {
        final PageModel<ProfitDetailCountRespons> pageModel = new PageModel<ProfitDetailCountRespons>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        ProfitCountRequest req = getTime(request);
        final Dealer dealer = super.getDealer().get();
        final long accountId = dealer.getAccountId();
        req.setAccountId(accountId);
        List<ProfitDetailCountRespons> list = this.splitAccountRecordService.getCountDetails(req);
        int count = this.splitAccountRecordService.getCountDetailsNo(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


    public ProfitCountRequest getTime(ProfitCountRequest request) throws ParseException {
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        return request;
    }
}
