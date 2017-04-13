package com.jkm.hss.controller.bill;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.base.common.util.*;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.QueryMerchantPayOrdersResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumCheck;
import com.jkm.hss.merchant.enums.EnumCleanType;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumCheckType;
import com.jkm.hss.product.entity.UpgradePayRecord;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.enums.EnumUpgrade;
import com.jkm.hss.product.enums.EnumUpgradePayResult;
import com.jkm.hss.product.servcie.ChannelSupportCreditBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Slf4j
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
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private ChannelSupportCreditBankService channelSupportCreditBankService;
    @Autowired
    private BasicChannelService basicChannelService;


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
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final String totalFee = payRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        if (!EnumPayChannelSign.isExistById(payRequest.getPayChannel())) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), merchantInfo.getId(), EnumAppType.HSS.getId(), true);
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8"))
                    .addParam("subMerName", merchantInfo.getMerchantName())
                    .addParam("amount", totalFee).build();
        }
        return CommonResponse.simpleResponse(-1, "请稍后重试");
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

    /**
     * 支付成功页面
     *
     * @param model
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/unionPaySuccess/{id}", method = RequestMethod.GET)
    public String  unionPaySuccessPage(final Model model, @PathVariable("id") long id) throws IOException {
        final Optional<Order> orderOptional = this.orderService.getById(id);
        if(!orderOptional.isPresent()){
            return "/500.jsp";
        }else{
            final Order order = orderOptional.get();
            final AccountBank creditCard = this.accountBankService.getDefaultCreditCard(order.getPayee());
            model.addAttribute("sn", order.getSn());
            model.addAttribute("amount", order.getTradeAmount().toPlainString());
            model.addAttribute("bankName", creditCard.getBankName());
            model.addAttribute("shortNo", creditCard.getBankNo().substring(creditCard.getBankNo().length() - 4));
            return "/unionPaySuccess";
        }
    }



    /**
     * 快捷支付， 路由选择接口
     *
     * @param unionPayRequest
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unionPayRoute", method = RequestMethod.POST)
    public CommonResponse unionPayRoute(@RequestBody UnionPayRequest unionPayRequest,
                           final HttpServletRequest httpServletRequest) {
        if(!super.isLogin(httpServletRequest)){
            return CommonResponse.simpleResponse(-2, " 未登录");
        }
        Optional<UserInfo> userInfoOptional = this.userInfoService.selectByOpenId(super.getOpenId(httpServletRequest));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, " 未登录");
        }
        Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, " 未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId() && merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, " 未登录");
        }
        Preconditions.checkState(EnumPayChannelSign.isUnionPay(unionPayRequest.getPayChannel()), "渠道不是快捷");
        final int creditBankCount = this.accountBankService.isHasCreditBank(merchantInfo.getAccountId());
        if (creditBankCount <= 0) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("url", "/trade/firstUnionPayPage?amount=" + unionPayRequest.getTotalFee() + "&channel=" + unionPayRequest.getPayChannel())
                    .build();
        }
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("url", "/trade/againUnionPayPage?amount=" + unionPayRequest.getTotalFee() + "&channel=" + unionPayRequest.getPayChannel())
                .build();
    }

    /**
     * 首次, 支付页面
     *
     * @return
     */
    @RequestMapping(value = "firstUnionPayPage")
    public String firstUnionPayPage(final HttpServletRequest httpServletRequest,final HttpServletResponse httpServletResponse,
                                    final Model model) {
        boolean isRedirect = false;
        if(!super.isLogin(httpServletRequest)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+httpServletRequest.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else{
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(httpServletRequest));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        final String amountStr = httpServletRequest.getParameter("amount");
                        final String channelStr = httpServletRequest.getParameter("channel");
                        final UserInfo userInfo = this.userInfoService.selectByOpenId(super.getOpenId(httpServletRequest)).get();
                        final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();
                        final Integer channelSign = Integer.valueOf(channelStr);
                        Preconditions.checkState(EnumPayChannelSign.isUnionPay(channelSign), "渠道不是快捷");
                        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
                        if (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType()) {
                            model.addAttribute("showExpireDate", EnumBoolean.TRUE.getCode());
                            model.addAttribute("showCvv", EnumBoolean.FALSE.getCode());
                        } else if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
                            model.addAttribute("showExpireDate", EnumBoolean.TRUE.getCode());
                            model.addAttribute("showCvv", EnumBoolean.TRUE.getCode());
                        } else {
                            model.addAttribute("showExpireDate", EnumBoolean.FALSE.getCode());
                            model.addAttribute("showCvv", EnumBoolean.FALSE.getCode());
                        }
                        model.addAttribute("amount", amountStr);
                        model.addAttribute("merchantName", merchantInfo.getMerchantName());
                        final String identity = MerchantSupport.decryptIdentity(merchantInfo.getIdentity());
                        model.addAttribute("bankAccountName", merchantInfo.getName());
                        model.addAttribute("idCard", identity.substring(0, 3) + "************" + identity.substring(identity.length() - 3, identity.length()));
                        url = "/firstUnionPay";
                    }
                }else{
                    CookieUtil.deleteCookie(httpServletResponse,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(httpServletResponse,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }

    }

    /**
     * 再次，支付页面
     *
     * @return
     */
    @RequestMapping(value = "againUnionPayPage")
    public String againUnionPayPage(final HttpServletRequest httpServletRequest,final HttpServletResponse httpServletResponse,
                                final Model model) {
        boolean isRedirect = false;
        if(!super.isLogin(httpServletRequest)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+httpServletRequest.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else{
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(httpServletRequest));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        final String amountStr = httpServletRequest.getParameter("amount");
                        final String channelStr = httpServletRequest.getParameter("channel");
                        final UserInfo userInfo = this.userInfoService.selectByOpenId(super.getOpenId(httpServletRequest)).get();
                        final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();
                        final Integer channelSign = Integer.valueOf(channelStr);
                        Preconditions.checkState(EnumPayChannelSign.isUnionPay(channelSign), "渠道不是快捷");
                        model.addAttribute("amount", amountStr);
                        model.addAttribute("merchantName", merchantInfo.getMerchantName());
                        final AccountBank accountBank = this.accountBankService.getDefaultCreditCard(merchantInfo.getAccountId());
                        final boolean exist = this.channelSupportCreditBankService.
                                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(channelSign).getUpperChannel().getId(), accountBank.getBankBin());
                        final String bankNo = accountBank.getBankNo();
                        final String mobile = accountBank.getReserveMobile();
                        if (exist) {
                            model.addAttribute("status", EnumBoolean.TRUE.getCode());
                        } else {
                            model.addAttribute("status", EnumBoolean.FALSE.getCode());
                        }
                        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
                        if (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType()) {
                            if (this.accountBankService.isHasExpiryTime(accountBank.getId())) {
                                model.addAttribute("showExpireDate", EnumBoolean.FALSE.getCode());
                            } else {
                                model.addAttribute("showExpireDate", EnumBoolean.TRUE.getCode());
                            }
                            model.addAttribute("showCvv", EnumBoolean.FALSE.getCode());
                        } else if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
                            if (this.accountBankService.isHasExpiryTime(accountBank.getId())) {
                                model.addAttribute("showExpireDate", EnumBoolean.FALSE.getCode());
                            } else {
                                model.addAttribute("showExpireDate", EnumBoolean.TRUE.getCode());
                            }
                            if (this.accountBankService.isHasCvv(accountBank.getId())) {
                                model.addAttribute("showCvv", EnumBoolean.FALSE.getCode());
                            } else {
                                model.addAttribute("showCvv", EnumBoolean.TRUE.getCode());
                            }
                        } else {
                            model.addAttribute("showExpireDate", EnumBoolean.FALSE.getCode());
                            model.addAttribute("showCvv", EnumBoolean.FALSE.getCode());
                        }
                        model.addAttribute("creditCardId", accountBank.getId());
                        model.addAttribute("bankName", accountBank.getBankName());
                        model.addAttribute("shortNo", bankNo.substring(bankNo.length() - 4));
                        model.addAttribute("mobile", mobile.substring(0, 2) + "**** ***" + mobile.substring(mobile.length() - 2));
                        url = "/againUnionPay";
                    }
                }else{
                    CookieUtil.deleteCookie(httpServletResponse,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(httpServletResponse,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 首次支付
     *
     * @param firstUnionPaySendMsgRequest
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "firstUnionPay", method = RequestMethod.POST)
    public CommonResponse firstUnionPay(@RequestBody FirstUnionPaySendMsgRequest firstUnionPaySendMsgRequest,
                                        final HttpServletRequest httpServletRequest) {
        if(!super.isLogin(httpServletRequest)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final Optional<UserInfo> userInfoOptional = this.userInfoService.selectByOpenId(super.getOpenId(httpServletRequest));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId() && merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        if (!EnumPayChannelSign.isUnionPay(firstUnionPaySendMsgRequest.getChannel())) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        if (!ValidateUtils.isMobile(firstUnionPaySendMsgRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getExpireDate())) {
            return CommonResponse.simpleResponse(-1, "有效期不能为空");
        }
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
            if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getExpireDate())) {
                return CommonResponse.simpleResponse(-1, "有效期不能为空");
            }
        }
        if (EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()) {
            if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getExpireDate())) {
                return CommonResponse.simpleResponse(-1, "有效期不能为空");
            }
            if (StringUtils.isEmpty(firstUnionPaySendMsgRequest.getCvv2())) {
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
                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(firstUnionPaySendMsgRequest.getChannel()).getUpperChannel().getId(), bankCardBin.getShorthand());
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
        final Pair<Integer, String> result = this.payService.firstUnionPay(merchantInfo.getId(), firstUnionPaySendMsgRequest.getAmount(),
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
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "againUnionPay", method = RequestMethod.POST)
    public CommonResponse againUnionPay(@RequestBody AgainUnionPaySendMsgRequest againUnionPaySendMsgRequest,
                                        final HttpServletRequest httpServletRequest) {
        if(!super.isLogin(httpServletRequest)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final Optional<UserInfo> userInfoOptional = this.userInfoService.selectByOpenId(super.getOpenId(httpServletRequest));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        if (!EnumPayChannelSign.isUnionPay(againUnionPaySendMsgRequest.getChannel())) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Optional<AccountBank> accountBankOptional = this.accountBankService.selectById(againUnionPaySendMsgRequest.getCreditCardId());
        if (!accountBankOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "信用卡不存在");
        }
        final boolean exist = this.channelSupportCreditBankService.
                isExistByUpperChannelAndBankCode(EnumPayChannelSign.idOf(againUnionPaySendMsgRequest.getChannel()).getUpperChannel().getId(), accountBankOptional.get().getBankBin());
        if (!exist) {
            return CommonResponse.simpleResponse(-1, "信用卡暂不可用");
        }
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(againUnionPaySendMsgRequest.getChannel()).get();
        final boolean hasExpiryTime = this.accountBankService.isHasExpiryTime(accountBankOptional.get().getId());
        final boolean hasCvv = this.accountBankService.isHasCvv(accountBankOptional.get().getId());
        if (StringUtils.isEmpty(againUnionPaySendMsgRequest.getExpireDate())
                && (EnumCheckType.FIVE_CHECK.getId() == basicChannel.getCheckType() || EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType())
                && !hasExpiryTime) {
            return CommonResponse.simpleResponse(-1, "有效期不能为空");
        }
        if (StringUtils.isEmpty(againUnionPaySendMsgRequest.getCvv2())
                && EnumCheckType.SIX_CHECK.getId() == basicChannel.getCheckType()
                && !hasCvv) {
            return CommonResponse.simpleResponse(-1, "CVV2 不能为空");
        }
        final Pair<Integer, String> result = this.payService.againUnionPay(merchantInfo.getId(), againUnionPaySendMsgRequest.getAmount(),
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
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "confirmUnionPay", method = RequestMethod.POST)
    public CommonResponse confirmUnionPay(@RequestBody final ConfirmUnionPayRequest confirmUnionPayRequest,
                                          final HttpServletRequest httpServletRequest) {
        if(!super.isLogin(httpServletRequest)){
            return CommonResponse.simpleResponse(-2, "未登录");
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
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("orderId", confirmUnionPayRequest.getOrderId())
                    .build();
        }
        return CommonResponse.simpleResponse(-1, result.getRight());
    }
}
