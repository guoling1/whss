package com.jkm.hss.controller.MyProfitController;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.ProfitCountRequest;
import com.jkm.hss.account.entity.ProfitCountRespons;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public CommonResponse getCount(@RequestBody ProfitCountRequest request){
        final PageModel<ProfitCountRespons> pageModel = new PageModel<ProfitCountRespons>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        final Dealer dealer = super.getDealer().get();
        final long accountId = dealer.getAccountId();
        int level = dealer.getLevel();
        int oemType = dealer.getOemType();
        long firstLevelDealerId = dealer.getFirstLevelDealerId();
        if (level==2) {
            request.setAccountId(accountId);
            request.setFirstLevelDealerId(firstLevelDealerId);
            List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
            int count = this.splitAccountRecordService.getProfitCount(request);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(1, "success", pageModel);
        }
        if (level==1){
            request.setAccountId(accountId);
            List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
            int count = this.splitAccountRecordService.getProfitCount(request);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(1, "success", pageModel);
        }
        if (oemType==1){
            request.setAccountId(accountId);
            List<ProfitCountRespons> list = this.splitAccountRecordService.getProfit(request);
            int count = this.splitAccountRecordService.getProfitCount(request);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(1, "success", pageModel);
        }

        return null;
    }

    /**
     * 分润详情
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCountDetails", method = RequestMethod.POST)
    public CommonResponse getCountDetails(@RequestBody ProfitCountRequest request){
        final PageModel<ProfitCountRespons> pageModel = new PageModel<ProfitCountRespons>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());

        return CommonResponse.objectResponse(1, "success", pageModel);
    }


}
