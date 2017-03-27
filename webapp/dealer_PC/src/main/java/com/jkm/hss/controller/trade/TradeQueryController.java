package com.jkm.hss.controller.trade;

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

import java.util.List;

/**
 * Created by zhangbin on 2017/3/27.
 */
@Controller
@RequestMapping(value = "/tradeQuery")
public class TradeQueryController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/tradeList", method = RequestMethod.POST)
    public CommonResponse tradeList(@RequestBody OrderTradeRequest req) {
        final PageModel<MerchantTradeResponse> pageModel = new PageModel<MerchantTradeResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        req.setDealerId(dealerId);
        if (level==2){
            List<MerchantTradeResponse> list = orderService.getTrade(req);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", list);
        }
        if (level==1){
            List<MerchantTradeResponse> list = orderService.getTradeFirst(req);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", list);
        }

        return CommonResponse.simpleResponse(-1, "查询异常");
    }
}
