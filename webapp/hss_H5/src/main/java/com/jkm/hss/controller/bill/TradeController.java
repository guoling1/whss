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
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DynamicCodePayRequest;
import com.jkm.hss.helper.request.StaticCodePayRequest;
import com.jkm.hss.helper.response.QueryMerchantPayOrdersResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
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
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountBankService accountBankService;

    /**
     * 动态码支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dcReceipt", method = RequestMethod.POST)
    public CommonResponse dynamicCodeReceipt(@RequestBody final DynamicCodePayRequest payRequest,
                                             final HttpServletRequest request,
                                             final Model model) throws UnsupportedEncodingException {
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
        final String totalFee = payRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(new BigDecimal(totalFee).compareTo(new BigDecimal("5.00")) < 0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }
        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        if (!EnumPayChannelSign.isExistById(payRequest.getPayChannel())) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        if (EnumPayChannelSign.isUnionPay(payRequest.getPayChannel())) {
            //快捷支付

            if (true) {

                model.addAttribute("bankAccountName", merchantInfo.get().getName());
                model.addAttribute("idCard", "");



            } else {

            }
        }

        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), merchantInfo.get().getId(), EnumAppType.HSS.getId(), true);
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8"))
                    .addParam("subMerName", merchantInfo.get().getMerchantName())
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
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
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
        final List<Integer> payTypeList = requestParam.getPayType();
        final ArrayList<Integer> payChannelIds = new ArrayList<>();
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
            payChannelIds.addAll(EnumPayChannelSign.getIdListByPaymentChannel(payTypeList.get(i)));
        }
        //重写值
        requestParam.setPayType(payChannelIds);
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
                queryMerchantPayOrdersResponse.setPayType(input.getPayChannelSign() > 0 ? EnumPayChannelSign.idOf(input.getPayChannelSign()).getPaymentChannel().getValue() : "");
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
     * 交易单详情
     *
     * @param model
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String tradeDetail(final Model model, @PathVariable("id") long id) throws IOException {
        final Optional<Order> orderOptional = this.orderService.getById(id);

        if(!orderOptional.isPresent()){
            return "/500.jsp";
        }else{
            final Order order = orderOptional.get();
            model.addAttribute("totalMoney", order.getTradeAmount().toPlainString());
            model.addAttribute("goodsName", order.getGoodsName());
            model.addAttribute("goodsDescribe", order.getGoodsDescribe());
            model.addAttribute("createTime", DateFormatUtil.format(order.getCreateTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            model.addAttribute("status", EnumOrderStatus.of(order.getStatus()).getValue());
            model.addAttribute("payType", order.getPayChannelSign() > 0 ? EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getValue() : "");
            final MerchantInfo merchantInfo = this.merchantInfoService.getByAccountId(order.getPayee()).get();
            model.addAttribute("merchantName", merchantInfo.getMerchantName());
            model.addAttribute("orderNo", order.getOrderNo());
            model.addAttribute("sn", order.getSn());
            model.addAttribute("settleStatus", EnumSettleStatus.of(order.getSettleStatus()).getValue());
            return "/tradeRecordDetail";
        }
    }



    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String unionPay(final HttpServletRequest httpServletRequest) {




        return "";
    }


}
