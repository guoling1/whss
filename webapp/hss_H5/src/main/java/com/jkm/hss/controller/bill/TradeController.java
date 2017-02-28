package com.jkm.hss.controller.bill;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DynamicCodePayRequest;
import com.jkm.hss.helper.request.StaticCodePayRequest;
import com.jkm.hss.helper.request.WithdrawRequest;
import com.jkm.hss.helper.response.QueryMerchantPayOrdersResponse;
import com.jkm.hss.helper.response.QueryOrderByIdResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Controller
@RequestMapping(value = "/trade")
public class TradeController extends BaseController {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    @RequestMapping(value = "test")
    public void test() {
        this.withdrawService.merchantWithdrawBySettlementRecord(79, 1, "1120170228152908929305627" , 101);
    }


    /**
     * 动态码支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dcReceipt", method = RequestMethod.POST)
    public CommonResponse dynamicCodeReceipt(@RequestBody final DynamicCodePayRequest payRequest,
                                             final HttpServletRequest request) throws UnsupportedEncodingException {
//        if(!super.isLogin(request)){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
//        if(!userInfoOptional.isPresent()){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
//        if(!merchantInfo.isPresent()){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
//            return CommonResponse.simpleResponse(-2, "未审核通过");
//        }
        final String totalFee = payRequest.getTotalFee();
//        if (StringUtils.isBlank(totalFee)) {
//            return CommonResponse.simpleResponse(-1, "请输入收款金额");
//        }
//        if(new BigDecimal(totalFee).compareTo(new BigDecimal("5.00")) < 0){
//            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
//        }
//        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
//            return CommonResponse.simpleResponse(-1, "缺失商户名称");
//        }
//        if (!EnumPayChannelSign.isExistById(payRequest.getPayChannel())) {
//            return CommonResponse.simpleResponse(-1, "支付方式错误");
//        }
        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), 79, EnumAppType.HSS.getId(), true);
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8"))
                    .addParam("subMerName", "")
                    .addParam("amount", totalFee).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }

    /**
     * 静态码支付
     *
     * @param payRequest
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scReceipt", method = RequestMethod.POST)
    public CommonResponse staticCodeReceipt(@RequestBody final StaticCodePayRequest payRequest,
                                             final HttpServletRequest request) throws UnsupportedEncodingException {
        final Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(payRequest.getMerchantId());
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final String totalAmount = payRequest.getTotalFee();
        if (StringUtils.isBlank(totalAmount)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }

        if(new BigDecimal(totalAmount).compareTo(new BigDecimal("5.00")) < 0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }

        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }

        if (!EnumPayChannelSign.isExistById(payRequest.getPayChannel())) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), merchantInfo.get().getId(), EnumAppType.HSS.getId(), false);
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8"))
                    .addParam("subMerName", merchantInfo.get().getMerchantName())
                    .addParam("amount", totalAmount).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }

    /**
     * 查询商户的支付单列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryMerchantPayOrders", method = RequestMethod.POST)
    public CommonResponse queryMerchantPayOrders(@RequestBody QueryMerchantPayOrdersRequestParam requestParam, final HttpServletRequest request) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        requestParam.setAccountId(merchantInfo.get().getAccountId());
        final PageModel<QueryMerchantPayOrdersResponse> result = new PageModel<>(requestParam.getPageNo(), requestParam.getPageSize());
        final List<Integer> payStatusList = requestParam.getPayStatus();
        final List<String> payTypeList = requestParam.getPayType();
        if (CollectionUtils.isEmpty(payStatusList)) {
            return CommonResponse.simpleResponse(-1, "支付状态不可以为空");
        }
        if (CollectionUtils.isEmpty(payTypeList)) {
            return CommonResponse.simpleResponse(-1, "支付方式不可以为空");
        }

        for (int i = 0; i < payStatusList.size(); i++) {
            final Integer payStatus = payStatusList.get(i);
            if (EnumOrderStatus.DUE_PAY.getId() != payStatus
                    && EnumOrderStatus.PAY_FAIL.getId() != payStatus
                    && EnumOrderStatus.PAY_SUCCESS.getId() != payStatus) {
                return CommonResponse.simpleResponse(-1, "不存在的支付状态");
            }
        }
        for (int i = 0; i < payTypeList.size(); i++) {
            final String payType = payTypeList.get(i);
            if (!EnumPayChannelSign.isExistByCode(payType)) {
                return CommonResponse.simpleResponse(-1, "不存在的支付方式");
            }
        }
        if (StringUtils.isEmpty(requestParam.getOrderNo())) {
            requestParam.setOrderNo(null);
        }
        if ("".equals(requestParam.getStartDate()) || null == requestParam.getStartDate()) {
            //TODO
            requestParam.setStartDate("2016-01-01 00:00:01");
//            requestParam.setStartDate(null);
        } else {
            requestParam.setStartDate(requestParam.getStartDate() + " 00:00:01");
        }
        if ("".equals(requestParam.getEndDate()) || null == requestParam.getEndDate()) {
            //TODO
            requestParam.setEndDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) + " 23:59:59");
//            requestParam.setEndDate(null);
        } else {
            requestParam.setEndDate(requestParam.getEndDate() + " 23:59:59");
        }
        final PageModel<Order> pageModel = this.orderService.queryMerchantPayOrders(requestParam);
        final List<Order> records = pageModel.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            result.setCount(0);
            result.setRecords(Collections.<QueryMerchantPayOrdersResponse>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }
        final List<QueryMerchantPayOrdersResponse> ordersResponses = Lists.transform(records, new Function<Order, QueryMerchantPayOrdersResponse>() {
            @Override
            public QueryMerchantPayOrdersResponse apply(Order input) {
                final QueryMerchantPayOrdersResponse queryMerchantPayOrdersResponse = new QueryMerchantPayOrdersResponse();
                queryMerchantPayOrdersResponse.setOrderId(input.getId());
                queryMerchantPayOrdersResponse.setAmount(input.getTradeAmount().toPlainString());
                queryMerchantPayOrdersResponse.setPayType(input.getPayType());
                queryMerchantPayOrdersResponse.setPayStatus(input.getStatus());
                queryMerchantPayOrdersResponse.setPayStatusValue(EnumOrderStatus.of(input.getStatus()).getValue());
                queryMerchantPayOrdersResponse.setDatetime(input.getCreateTime());
                return queryMerchantPayOrdersResponse;
            }
        });
        result.setCount(pageModel.getCount());
        result.setRecords(ordersResponses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

    /**
     * 查询商户的支付单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "{orderId}")
    public CommonResponse queryById(@PathVariable long orderId) {
        final Optional<Order> orderOptional = this.orderService.getById(orderId);
        if (!orderOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "交易订单不存在");
        }
        final Order order = orderOptional.get();
        final MerchantInfo merchantInfo = this.merchantInfoService.getByAccountId(order.getPayee()).get();
        final QueryOrderByIdResponse response = new QueryOrderByIdResponse();
        response.setOrderId(order.getId());
        response.setAmount(order.getTradeAmount().toPlainString());
        response.setOrderNo(order.getOrderNo());
        response.setGoodsName(order.getGoodsName());
        response.setGoodsDescribe(order.getGoodsDescribe());
        response.setDateTime(order.getCreateTime());
        response.setPayStatus(order.getStatus());
        response.setPayStatusValue(EnumOrderStatus.of(order.getStatus()).getValue());
        response.setPayType(order.getPayType());
        response.setMerchantName(merchantInfo.getMerchantName());
        response.setSettleStatus(order.getSettleStatus());
        response.setSettleStatusValue(EnumSettleStatus.of(order.getSettleStatus()).getValue());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", response);
    }
}
