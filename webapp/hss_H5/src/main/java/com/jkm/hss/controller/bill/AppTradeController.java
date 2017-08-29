package com.jkm.hss.controller.bill;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.BusinessOrder;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.service.BusinessOrderService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.dao.OemInfoDao;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.QueryMerchantPayOrdersResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumCheckType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/8/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "/app/trade")
public class AppTradeController extends BaseController {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private ChannelSupportCreditBankService channelSupportCreditBankService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private BusinessOrderService businessOrderService;
    @Autowired
    private OemInfoService oemInfoService;
    @Autowired
    private OemInfoDao oemInfoDao;

    /**
     * 生成订单
     *
     * @param businessOrderRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "generateOrder", method = RequestMethod.POST)
    public CommonResponse generateBusinessOrder(@RequestBody final GenerateBusinessOrderRequest businessOrderRequest) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final String amount = businessOrderRequest.getAmount();
        if (StringUtils.isBlank(amount)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }

        final long businessOrderId = this.payService.generateBusinessOrder(merchantInfo.getId(), businessOrderRequest.getAmount(), EnumAppType.HSS.getId());
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("orderId", businessOrderId)
                .addParam("amount", amount)
                .build();
    }

    /**
     * 动态码支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dcReceipt", method = RequestMethod.POST)
    public CommonResponse dynamicCodeReceipt(@RequestBody final DynamicCodePayRequest payRequest) throws UnsupportedEncodingException {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(payRequest.getPayChannel());
        //log.info("》》》》》》》》》》》》》》》" + parentChannelSign);
        if (!EnumPayChannelSign.isExistById(parentChannelSign)) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Optional<BusinessOrder> businessOrderOptional = this.businessOrderService.getById(payRequest.getOrderId());
        if (!businessOrderOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "订单不存在");
        }
        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getOrderId(),
                payRequest.getPayChannel(), EnumAppType.HSS.getId(), true);
        if (0 == resultPair.getLeft()) {
            String oemNo = "";
            if(merchantInfo.getOemId()>0){
                OemDetailResponse oemDetailResponse = oemInfoService.selectByDealerId(merchantInfo.getOemId());
                Preconditions.checkState(oemDetailResponse!=null, "O单配置有误");
                oemNo = oemDetailResponse.getOemNo();
            }
            final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(parentChannelSign);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8"))
                    .addParam("subMerName", merchantInfo.getMerchantName())
                    .addParam("oemNo", oemNo)
                    .addParam("amount", businessOrderOptional.get().getTradeAmount())
                    .addParam("payType", payChannelSign.getPaymentChannel().getId()).build();
        }
        return CommonResponse.simpleResponse(-1, "请稍后重试");
    }

    /**
     * 查询商户的支付单列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryMerchantPayOrders", method = RequestMethod.POST)
    public CommonResponse queryMerchantPayOrders(@RequestBody QueryMerchantPayOrdersRequestParam requestParam) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        requestParam.setAccountId(merchantInfo.getAccountId());
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
     * @param id
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResponse tradeDetail(@PathVariable("id") long id) throws IOException {
        final Order order = this.orderService.getById(id).get();
        final MerchantInfo merchantInfo = this.merchantInfoService.getByAccountId(order.getPayee()).get();
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("totalMoney", order.getTradeAmount().toPlainString())
                .addParam("goodsName", order.getGoodsName())
                .addParam("goodsDescribe", order.getGoodsDescribe())
                .addParam("createTime", DateFormatUtil.format(order.getCreateTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss))
                .addParam("status", EnumOrderStatus.of(order.getStatus()).getValue())
                .addParam("payType", order.getPayChannelSign() > 0 ? EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getValue() : "")
                .addParam("merchantName", merchantInfo.getMerchantName())
                .addParam("orderNo", order.getOrderNo())
                .addParam("sn", order.getSn())
                .addParam("settleStatus", EnumSettleStatus.of(order.getSettleStatus()).getValue())
                .build();
    }

    /**
     * 支付成功页面
     *
     * @param id
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/unionPaySuccess/{id}", method = RequestMethod.GET)
    public CommonResponse  unionPaySuccessPage(@PathVariable("id") long id) throws IOException {
        final Order order = this.orderService.getById(id).get();
        final AccountBank creditCard = this.accountBankService.getDefaultCreditCard(order.getPayee());
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("sn", order.getSn())
                .addParam("amount", order.getTradeAmount().toPlainString())
                .addParam("bankName", creditCard.getBankName())
                .addParam("shortNo", creditCard.getBankNo().substring(creditCard.getBankNo().length() - 4))
                .build();
    }



    /**
     * 快捷支付， 路由选择接口
     *
     * @param unionPayRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unionPayRoute", method = RequestMethod.POST)
    public CommonResponse unionPayRoute(@RequestBody UnionPayRequest unionPayRequest) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(unionPayRequest.getPayChannel());
        Preconditions.checkState(EnumPayChannelSign.isUnionPay(parentChannelSign), "渠道不是快捷");
        if (parentChannelSign == EnumPayChannelSign.JH_UNIONPAY.getId()){
            //如果是玖和快捷
            final int creditBankCount = this.accountBankService.isHasCreditBankToken(merchantInfo.getAccountId());
            /*if (creditBankCount <= 0) {
                return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                        .addParam("isFirst", 1)
                        .addParam("amount", businessOrder.getTradeAmount().toPlainString())
                        .addParam("channel", unionPayRequest.getPayChannel())
                        .addParam("orderId", businessOrder.getId())
                        .build();
            }
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("isFirst", 0)
                    .addParam("amount", businessOrder.getTradeAmount().toPlainString())
                    .addParam("channel", unionPayRequest.getPayChannel())
                    .addParam("orderId", businessOrder.getId())
                    .build();*/
        }
        final int creditBankCount = this.accountBankService.isHasCreditBank(merchantInfo.getAccountId());
        final BusinessOrder businessOrder = this.businessOrderService.getById(unionPayRequest.getOrderId()).get();
        if (creditBankCount <= 0) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("isFirst", 1)
                    .addParam("amount", businessOrder.getTradeAmount().toPlainString())
                    .addParam("channel", unionPayRequest.getPayChannel())
                    .addParam("orderId", businessOrder.getId())
                    .build();
        }
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("isFirst", 0)
                .addParam("amount", businessOrder.getTradeAmount().toPlainString())
                .addParam("channel", unionPayRequest.getPayChannel())
                .addParam("orderId", businessOrder.getId())
                .build();
    }

    /**
     * 首次, 支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "firstUnionPayPage")
    public CommonResponse firstUnionPayPage(final HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String oemNo = httpServletRequest.getParameter("oemNo");
        final String amountStr = httpServletRequest.getParameter("amount");
        final String channelStr = httpServletRequest.getParameter("channel");
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final Integer channelSign = Integer.valueOf(channelStr);
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(channelSign);
        Preconditions.checkState(EnumPayChannelSign.isUnionPay(parentChannelSign), "渠道不是快捷");
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
        final JSONObject result = new JSONObject();
        if (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType()) {
            result.put("showExpireDate", EnumBoolean.TRUE.getCode());
            result.put("showCvv", EnumBoolean.FALSE.getCode());
        } else if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
            result.put("showExpireDate", EnumBoolean.TRUE.getCode());
            result.put("showCvv", EnumBoolean.TRUE.getCode());
        } else {
            result.put("showExpireDate", EnumBoolean.FALSE.getCode());
            result.put("showCvv", EnumBoolean.FALSE.getCode());
        }
        result.put("oemNo", oemNo);
        result.put("amount", amountStr);
        result.put("merchantName", merchantInfo.getMerchantName());
        final String identity = MerchantSupport.decryptIdentity(merchantInfo.getIdentity());
        result.put("bankAccountName", merchantInfo.getName());
        result.put("idCard", identity.substring(0, 3) + "************" + identity.substring(identity.length() - 3, identity.length()));
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

    /**
     * 再次，支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "againUnionPayPage")
    public CommonResponse againUnionPayPage(final HttpServletRequest httpServletRequest) {
        final JSONObject result = new JSONObject();
        final String amountStr = httpServletRequest.getParameter("amount");
        final String channelStr = httpServletRequest.getParameter("channel");
        final  String oemNo = httpServletRequest.getParameter("oemNo");
        if(oemNo!=null&&!"".equals(oemNo)&&!"null".equals(oemNo)){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
            result.put("oemName",oemInfoOptional.get().getBrandName());
        }else{
            result.put("oemName","好收收");
        }
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final Integer channelSign = Integer.valueOf(channelStr);
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(channelSign);
        Preconditions.checkState(EnumPayChannelSign.isUnionPay(parentChannelSign), "渠道不是快捷");
        result.put("amount", amountStr);
        result.put("merchantName", merchantInfo.getMerchantName());
        final AccountBank accountBank = this.accountBankService.getDefaultCreditCard(merchantInfo.getAccountId());
        final boolean exist = this.channelSupportCreditBankService.
                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(parentChannelSign).getUpperChannel().getId(), accountBank.getBankBin());
        final String bankNo = accountBank.getBankNo();
        final String mobile = accountBank.getReserveMobile();
        if (exist) {
            result.put("status", EnumBoolean.TRUE.getCode());
        } else {
            result.put("status", EnumBoolean.FALSE.getCode());
        }
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
        if (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType()) {
            if (this.accountBankService.isHasExpiryTime(accountBank.getId())) {
                result.put("showExpireDate", EnumBoolean.FALSE.getCode());
            } else {
                result.put("showExpireDate", EnumBoolean.TRUE.getCode());
            }
            result.put("showCvv", EnumBoolean.FALSE.getCode());
        } else if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
            if (this.accountBankService.isHasExpiryTime(accountBank.getId())) {
                result.put("showExpireDate", EnumBoolean.FALSE.getCode());
            } else {
                result.put("showExpireDate", EnumBoolean.TRUE.getCode());
            }
            if (this.accountBankService.isHasCvv(accountBank.getId())) {
                result.put("showCvv", EnumBoolean.FALSE.getCode());
            } else {
                result.put("showCvv", EnumBoolean.TRUE.getCode());
            }
        } else {
            result.put("showExpireDate", EnumBoolean.FALSE.getCode());
            result.put("showCvv", EnumBoolean.FALSE.getCode());
        }
        result.put("oemNo", oemNo);
        result.put("creditCardId", accountBank.getId());
        result.put("bankName", accountBank.getBankName());
        result.put("shortNo", bankNo.substring(bankNo.length() - 4));
        result.put("mobile", mobile.substring(0, 2) + "**** ***" + mobile.substring(mobile.length() - 2));
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

    /**
     * 首次支付
     *
     * @param firstUnionPaySendMsgRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "firstUnionPay", method = RequestMethod.POST)
    public CommonResponse firstUnionPay(@RequestBody FirstUnionPaySendMsgRequest firstUnionPaySendMsgRequest) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(firstUnionPaySendMsgRequest.getChannel());
        if (!EnumPayChannelSign.isExistById(parentChannelSign)) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        if (!ValidateUtils.isMobile(firstUnionPaySendMsgRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getExpireDate())) {
            return CommonResponse.simpleResponse(-1, "有效期不能为空");
        }
       /* if(new BigDecimal(firstUnionPaySendMsgRequest.getAmount()).compareTo(new BigDecimal("10.00")) < 0){
            return CommonResponse.simpleResponse(-1, "支付金额至少10.00元");
        }*/
        if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getBankCardNo())) {
            return CommonResponse.simpleResponse(-1, "银卡卡号不能为空");
        }
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(firstUnionPaySendMsgRequest.getChannel()).get();
        if (EnumCheckType.FOUR_CHECK.getId() == basicChannel.getCheckType()) {
            firstUnionPaySendMsgRequest.setExpireDate("");
            firstUnionPaySendMsgRequest.setCvv2("");
        }
        if (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType()) {
            firstUnionPaySendMsgRequest.setCvv2("");
            if (!StringUtils.isNumeric(firstUnionPaySendMsgRequest.getExpireDate())) {
                return CommonResponse.simpleResponse(-1, "有效期不能为空");
            }
        }
        if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
            if (!StringUtils.isNumeric(firstUnionPaySendMsgRequest.getExpireDate())) {
                return CommonResponse.simpleResponse(-1, "有效期不能为空");
            }
            if (!StringUtils.isNumeric(firstUnionPaySendMsgRequest.getCvv2())) {
                return CommonResponse.simpleResponse(-1, "CVV2 不能为空");
            }
        }
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(firstUnionPaySendMsgRequest.getBankCardNo());
        if (!bankCardBinOptional.isPresent()) {
            return CommonResponse.builder4MapResult(2, "fail").addParam("errorCode", "001").build();
        }
        final BankCardBin bankCardBin = bankCardBinOptional.get();
        if (!"1".equals(bankCardBin.getCardTypeCode())) {
            return CommonResponse.builder4MapResult(2, "fail").addParam("errorCode", "002").build();
        }
        final boolean exist = this.channelSupportCreditBankService.
                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(parentChannelSign).getUpperChannel().getId(), bankCardBin.getShorthand());
        if (!bankCardBin.getShorthand().equals(firstUnionPaySendMsgRequest.getBankCode())) {
            if (!exist) {
                return CommonResponse.builder4MapResult(2, "fail").addParam("errorCode", "003").build();
            }
            firstUnionPaySendMsgRequest.setBankCode(bankCardBin.getShorthand());
        } else {
            if (!exist) {
                return CommonResponse.simpleResponse(-1, "当前信用卡不支持");
            }
        }
        final Optional<AccountBank> bankOptional = this.accountBankService.selectCreditCardByBankNo(merchantInfo.getAccountId(), firstUnionPaySendMsgRequest.getBankCardNo());
        if (bankOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "当前信用卡已经存在");
        }
        final long creditBankCardId = this.accountBankService.initCreditBankCard(merchantInfo.getAccountId(), firstUnionPaySendMsgRequest.getBankCardNo(),
                bankCardBin.getBankName(), firstUnionPaySendMsgRequest.getMobile(), bankCardBin.getShorthand(), firstUnionPaySendMsgRequest.getExpireDate(), firstUnionPaySendMsgRequest.getCvv2());
        final Pair<Integer, String> result = this.payService.firstUnionPay(firstUnionPaySendMsgRequest.getOrderId(),
                firstUnionPaySendMsgRequest.getChannel(), creditBankCardId, EnumProductType.HSS.getId());
        if (0 == result.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("orderId", result.getRight())
                    .addParam("creditCardId", creditBankCardId)
                    .build();
        }
        return CommonResponse.simpleResponse(-1, result.getRight());
    }

    /**
     * 再次支付
     *
     * @param againUnionPaySendMsgRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "againUnionPay", method = RequestMethod.POST)
    public CommonResponse againUnionPay(@RequestBody AgainUnionPaySendMsgRequest againUnionPaySendMsgRequest) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final int parentChannelSign = this.basicChannelService.selectParentChannelSign(againUnionPaySendMsgRequest.getChannel());
        if (!EnumPayChannelSign.isExistById(parentChannelSign)) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Optional<AccountBank> accountBankOptional = this.accountBankService.selectById(againUnionPaySendMsgRequest.getCreditCardId());
        if (!accountBankOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "信用卡不存在");
        }
        final boolean exist = this.channelSupportCreditBankService.
                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(parentChannelSign).getUpperChannel().getId(), accountBankOptional.get().getBankBin());
        if (!exist) {
            return CommonResponse.simpleResponse(-1, "信用卡暂不可用");
        }
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(againUnionPaySendMsgRequest.getChannel()).get();
        final boolean hasExpiryTime = this.accountBankService.isHasExpiryTime(accountBankOptional.get().getId());
        final boolean hasCvv = this.accountBankService.isHasCvv(accountBankOptional.get().getId());
        if (!StringUtils.isNumeric(againUnionPaySendMsgRequest.getExpireDate())
                && (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType() || EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType())
                && !hasExpiryTime) {
            return CommonResponse.simpleResponse(-1, "有效期不能为空");
        }
        if (!StringUtils.isNumeric(againUnionPaySendMsgRequest.getCvv2())
                && EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()
                && !hasCvv) {
            return CommonResponse.simpleResponse(-1, "CVV2 不能为空");
        }
        final Pair<Integer, String> result = this.payService.againUnionPay(againUnionPaySendMsgRequest.getOrderId(),
                againUnionPaySendMsgRequest.getChannel(), againUnionPaySendMsgRequest.getExpireDate(), againUnionPaySendMsgRequest.getCvv2(),
                againUnionPaySendMsgRequest.getCreditCardId(), EnumProductType.HSS.getId());
        if (0 == result.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("orderId", result.getRight())
                    .addParam("creditCardId", againUnionPaySendMsgRequest.getCreditCardId())
                    .build();
        }
        return CommonResponse.simpleResponse(-1, result.getRight());
    }

    /**
     * 确认支付
     *
     * @param confirmUnionPayRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "confirmUnionPay", method = RequestMethod.POST)
    public CommonResponse confirmUnionPay(@RequestBody final ConfirmUnionPayRequest confirmUnionPayRequest) {
        final Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final Optional<Order> orderOptional = this.orderService.getById(confirmUnionPayRequest.getOrderId());
        if (!orderOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "订单不存在");
        }
        if (StringUtils.isEmpty(confirmUnionPayRequest.getCode().trim())) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        final Pair<Integer, String> result = this.payService.confirmUnionPay(confirmUnionPayRequest.getOrderId(), confirmUnionPayRequest.getCode());
        if (0 == result.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, result.getRight())
                    .addParam("orderId", confirmUnionPayRequest.getOrderId())
                    .addParam("errorCode", 1)
                    .build();
        } else {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, result.getRight())
                    .addParam("orderId", confirmUnionPayRequest.getOrderId())
                    .addParam("errorCode", 2)
                    .build();
        }
    }

    /**
     * 快捷支付失败错误页面
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unionPay2Error/{orderId}")
    public CommonResponse<BaseEntity> unionPay2Error(@PathVariable final long orderId) {
        final JSONObject result = new JSONObject();
        final Order order = this.orderService.getById(orderId).get();
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.getByAccountId(order.getPayee());
        OemInfo oemInfo = oemInfoDao.selectByDealerId(merchantInfoOptional.get().getOemId());
        if(oemInfo!=null){
            result.put("oemNo", oemInfo.getOemNo());
        }else{
            result.put("oemNo", "");
        }
        result.put("errorMsg", order.getRemark());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

}
