package com.jkm.hss.controller.orderRecord;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.QueryHsyOrderRequest;
import com.jkm.hss.bill.entity.QueryHsyOrderResponse;
import com.jkm.hss.bill.entity.QueryOrderRequest;
import com.jkm.hss.bill.entity.QueryOrderResponse;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
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
 * Created by zhangbin on 2017/5/18.
 */
@Controller
@RequestMapping(value = "/admin/queryOrder")
public class QueryOrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HSYOrderService hsyOrderService;

    @ResponseBody
    @RequestMapping(value = "/queryOrderList",method = RequestMethod.POST)
    public CommonResponse queryOrderList(@RequestBody QueryOrderRequest req) throws ParseException {

        final PageModel<QueryOrderResponse> pageModel = new PageModel<QueryOrderResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(req.getPaySuccessTime()!=null&&!"".equals(req.getPaySuccessTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getPaySuccessTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setPaySuccessTime(sdf.format(rightNow.getTime()));
        }
        List<QueryOrderResponse> orderList = this.orderService.queryOrderList(req);
        int count = this.orderService.queryOrderListCount(req);
        pageModel.setRecords(orderList);
        pageModel.setCount(count);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }


    /**
     * 统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderCount ",method = RequestMethod.POST)
    public CommonResponse getOrderCount(@RequestBody QueryOrderRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(req.getPaySuccessTime()!=null&&!"".equals(req.getPaySuccessTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getPaySuccessTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setPaySuccessTime(sdf.format(rightNow.getTime()));
        }
        String totalPayment = this.orderService.getOrderCount(req);
        String totalPoundage = this.orderService.getOrderCount1(req);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalPayment",totalPayment);
        jsonObject.put("totalPoundage",totalPoundage);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
    }

    /**
     * hsy订单
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/queryHsyOrderList",method = RequestMethod.POST)
    public CommonResponse queryHsyOrderList(@RequestBody QueryHsyOrderRequest req) throws ParseException {
        final PageModel<QueryHsyOrderResponse> pageModel = new PageModel<QueryHsyOrderResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        List<QueryHsyOrderResponse> orderList = this.hsyOrderService.queryHsyOrderList(req);
        int count = this.hsyOrderService.queryHsyOrderListCount(req);
        pageModel.setRecords(orderList);
        pageModel.setCount(count);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getHsyOrderCount ",method = RequestMethod.POST)
    public CommonResponse getHsyOrderCount(@RequestBody QueryHsyOrderRequest req){
        String totalPayment = this.hsyOrderService.getHsyOrderCounts(req);
        String totalPoundage = this.hsyOrderService.getHsyOrderCounts1(req);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalPayment",totalPayment);
        jsonObject.put("totalPoundage",totalPoundage);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
    }
}
