package com.jkm.hss.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.helper.responseparam.PcStatisticsOrder;
import com.jkm.hss.bill.helper.util.VerifyAuthCodeUtil;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.PcStatisticsOrderResponse;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulong.zhang on 2017/7/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/pc/trade")
public class PcTradeController extends BaseController {

    @Autowired
    private HSYTransactionService  hsyTransactionService;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private HSYTradeService hsyTradeService;

    @RequestMapping(value = "/generateOrder", method = RequestMethod.OPTIONS)
    public void generateOrder() {

    }

    /**
     * 下单
     *
     * @param generateOrderRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "generateOrder", method = RequestMethod.POST)
    public CommonResponse generateOrder(@RequestBody PcGenerateOrderRequest generateOrderRequest, final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        final long orderId = this.hsyTransactionService.createOrder2(generateOrderRequest.getShopId(),
                getPcUserPassport().getUid(), new BigDecimal(generateOrderRequest.getAmount()));
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("orderId", orderId)
                .addParam("amount", generateOrderRequest.getAmount())
                .build();
    }

    @RequestMapping(value = "/pay", method = RequestMethod.OPTIONS)
    public void pay() {

    }

    /**
     * 支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public CommonResponse pay (@RequestBody PcPayRequest pcPayRequest, final HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        if (pcPayRequest.getOrderId() <= 0) {
            return CommonResponse.simpleResponse(-1, "订单id错误");
        }
        if (StringUtils.isEmpty(pcPayRequest.getAuthCode())) {
            return CommonResponse.simpleResponse(-1, "授权码不能为空");
        }
        if (null == VerifyAuthCodeUtil.verify(pcPayRequest.getAuthCode())) {
            return CommonResponse.simpleResponse(-1, "授权码错误");
        }
        final Pair<Integer, String> result = this.hsyTransactionService.authCodePay(pcPayRequest.getOrderId(), pcPayRequest.getAuthCode());
        if (0 !=result.getLeft()) {
            return CommonResponse.simpleResponse(-1, result.getRight());
        }
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "")
                .addParam("orderId", pcPayRequest.getOrderId())
                .build();
    }

    @RequestMapping(value = "queryOrder/{orderId}", method = RequestMethod.OPTIONS)
    public void queryOrder() {

    }

    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryOrder/{orderId}")
    public CommonResponse queryOrder(@PathVariable long orderId, final HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        final HsyOrder hsyOrder = this.hsyOrderService.getById(orderId).get();
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("orderNo", hsyOrder.getOrdernumber())
                .addParam("tradeOrderNo", hsyOrder.getOrderno())
                .addParam("status", hsyOrder.getOrderstatus())
                .addParam("paySuccessTime", hsyOrder.getPaysuccesstime())
                .addParam("shopName", hsyOrder.getShopname())
                .addParam("tradeAmount", hsyOrder.getAmount())
                .addParam("discountAmount", "0.00")
                .addParam("totalAmount", hsyOrder.getRealAmount())
                .addParam("payChannel", hsyOrder.getPaymentChannel())
                .addParam("errorMsg", hsyOrder.getRemark())
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.OPTIONS)
    public void list() {

    }

    /**
     * 订单列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody PcOrderListRequest pcOrderListRequest, final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(pcOrderListRequest.getStartTime())) {
            startTime = DateFormatUtil.parse(pcOrderListRequest.getStartTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(pcOrderListRequest.getEndTime())) {
            endTime = DateFormatUtil.parse(pcOrderListRequest.getEndTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final PageModel<JSONObject> pageModel = new PageModel<>(pcOrderListRequest.getPageNo(), pcOrderListRequest.getPageSize());
        final long count = this.hsyOrderService.getOrderCountByParam(pcOrderListRequest.getShopId(), "", pcOrderListRequest.getTradeOrderNo(),
                pcOrderListRequest.getAll(), pcOrderListRequest.getPaymentChannels(), startTime, endTime);
        final List<HsyOrder> hsyOrders = this.hsyOrderService.getOrdersByParam(pcOrderListRequest.getShopId(), "", pcOrderListRequest.getTradeOrderNo(),
                pcOrderListRequest.getAll(), pcOrderListRequest.getPaymentChannels(),
                startTime, endTime, pageModel.getFirstIndex(), pageModel.getPageSize());
        if (CollectionUtils.isEmpty(hsyOrders)) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<JSONObject>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", pageModel);
        }
        final List<JSONObject> records = new ArrayList<>(pageModel.getRecords().size());
        for(HsyOrder hsyOrder : hsyOrders){
            final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
            final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
            final JSONObject jo = new JSONObject();
            records.add(jo);
            if(hsyOrder.isRefund() && refundDate.compareTo(payDate) == 0){
                jo.put("canRefund", 1);
            } else {
                jo.put("canRefund", 0);
            }
            jo.put("tradeAmount", hsyOrder.getAmount());
            jo.put("totalAmount", hsyOrder.getRealAmount());
            jo.put("validationCode", hsyOrder.getValidationcode());
            jo.put("orderStatus", hsyOrder.getOrderstatus());
            jo.put("orderStatusValue", EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
            jo.put("refundAmount", hsyOrder.getRefundamount());
            jo.put("payChannel", hsyOrder.getPaymentChannel());
            jo.put("payChannelValue", EnumPaymentChannel.of(hsyOrder.getPaymentChannel()));
            jo.put("createTime", hsyOrder.getCreateTime());
            jo.put("paySuccessTime", hsyOrder.getPaysuccesstime());
            jo.put("orderNo", hsyOrder.getOrdernumber());
            jo.put("tradeOrderNo", hsyOrder.getOrderno());
            jo.put("orderId", hsyOrder.getId());
            jo.put("shopName", hsyOrder.getShopname());
            jo.put("cashierName", hsyOrder.getCashiername());
            jo.put("discountAmount", "0.00");
        }
        pageModel.setCount(count);
        pageModel.setRecords(records);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", pageModel);
    }


    @RequestMapping(value = "statisticsOrder", method = RequestMethod.OPTIONS)
    public void statisticsOrder() {

    }
    /**
     * 订单汇总
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "statisticsOrder", method = RequestMethod.POST)
    public CommonResponse statisticsOrder(@RequestBody PcStatisticsOrderRequest statisticsOrderRequest, final HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        final Date startTime = DateFormatUtil.parse(statisticsOrderRequest.getStartDate() + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date endTime = DateFormatUtil.parse(statisticsOrderRequest.getEndDate() + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final List<PcStatisticsOrder> statisticsOrderList = this.hsyOrderService.pcStatisticsOrder(statisticsOrderRequest.getShopId(), startTime, endTime);
        final PcStatisticsOrderResponse statisticsOrderResponse = new PcStatisticsOrderResponse();
        if (!CollectionUtils.isEmpty(statisticsOrderList)) {
            final HashMap<Integer, PcStatisticsOrderResponse.Detail> detailMap = new HashMap<>();
            for (PcStatisticsOrder statisticsOrder : statisticsOrderList) {
                statisticsOrderResponse.addDetails(detailMap, statisticsOrder);
            }
            statisticsOrderResponse.getDetails().addAll(detailMap.values());
        }
        log.info("[{}]", statisticsOrderResponse);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", statisticsOrderResponse);
    }

    @RequestMapping(value = "refund", method = RequestMethod.OPTIONS)
    public void refund() {

    }

    /**
     * 退款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refund", method = RequestMethod.POST)
    public CommonResponse pcRefund(@RequestBody PcRefundRequest refundRequest, final HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        final JSONObject result = this.hsyTradeService.pcAppRefund(refundRequest.getOrderId(), getPcUserPassport().getUid(), refundRequest.getPassword());
        final int code = result.getIntValue("code");
        if (0 == code) {
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }
        return CommonResponse.simpleResponse(-1, result.getString("msg"));
    }

}
