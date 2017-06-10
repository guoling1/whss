package com.jkm.hss.controller.trade;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
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
 * Created by zhangbin on 2017/3/27.
 */
@Controller
@RequestMapping(value = "/daili/tradeQuery")
public class TradeQueryController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/tradeList", method = RequestMethod.POST)
    public CommonResponse tradeList(@RequestBody OrderTradeRequest req) throws ParseException {
        final PageModel<MerchantTradeResponse> pageModel = new PageModel<MerchantTradeResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        req.setDealerId(dealerId);
        if (level==2){
            List<MerchantTradeResponse> list = orderService.getTrade(req);
            int count = orderService.listCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }
        if (level==1){
            List<MerchantTradeResponse> list = orderService.getTradeFirst(req);
            int count = orderService.listFirstCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }

        return CommonResponse.simpleResponse(-1, "查询异常");
    }

    /**
     * 统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/amountCount",method = RequestMethod.POST)
    public CommonResponse amountCount(@RequestBody OrderTradeRequest req) throws ParseException {
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        req.setDealerId(dealerId);
        if (level==2){
            String totalPayment = this.orderService.getAmountCount(req);
            String totalPoundage = this.orderService.getAmountCount1(req);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalPayment",totalPayment);
            jsonObject.put("totalPoundage",totalPoundage);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        if (level==1){
            String totalPayment = this.orderService.getAmountCount(req);
            String totalPoundage = this.orderService.getAmountCount1(req);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalPayment",totalPayment);
            jsonObject.put("totalPoundage",totalPoundage);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        return CommonResponse.simpleResponse(-1, "查询异常");
    }
}
