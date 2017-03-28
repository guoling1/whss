package com.jkm.hss.controller.QueryMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.QueryMerchantRequest;
import com.jkm.hss.dealer.entity.QueryMerchantResponse;
import com.jkm.hss.dealer.service.DealerService;
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
 * Created by zhangbin on 2017/3/24.
 */
@Controller
@RequestMapping(value = "/daili/queryMerchant")
public class QueryMerchantController extends BaseController {

    @Autowired
    private DealerService dealerService;

    @ResponseBody
    @RequestMapping(value = "/dealerMerchantList", method = RequestMethod.POST)
    public CommonResponse dealerMerchantList(@RequestBody QueryMerchantRequest req) throws ParseException {
        final PageModel<QueryMerchantResponse> pageModel = new PageModel<QueryMerchantResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(req.getEndTime1()!=null&&!"".equals(req.getEndTime1())){
            Date dt = sdf.parse(req.getEndTime1());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime1(sdf.format(rightNow.getTime()));
        }
        req.setDealerId(dealerId);
        if(level==1){
            List<QueryMerchantResponse> list = dealerService.dealerMerchantList(req);
            int count = dealerService.dealerMerchantCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
        }
        if(level==2){
            List<QueryMerchantResponse> list = dealerService.dealerMerchantSecondList(req);
            int count = dealerService.dealerMerchantSecondCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
        }


        return CommonResponse.simpleResponse(-1, "查询异常");
    }

}
