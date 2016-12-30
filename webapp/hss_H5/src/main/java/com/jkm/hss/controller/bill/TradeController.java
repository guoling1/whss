package com.jkm.hss.controller.bill;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DynamicCodePayRequest;
import com.jkm.hss.helper.request.StaticCodePayRequest;
import com.jkm.hss.helper.request.WithdrawRequest;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Controller
@RequestMapping(value = "/trade")
public class TradeController extends BaseController {

    @Autowired
    private PayService payService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 动态码支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dcReceipt", method = RequestMethod.POST)
    public CommonResponse dynamicCodeReceipt(@RequestBody final DynamicCodePayRequest payRequest,
                                             final HttpServletRequest request) {
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
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()){
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
        if (EnumPayChannelSign.YG_WEIXIN.getId() != payRequest.getPayChannel()
                && EnumPayChannelSign.YG_ZHIFUBAO.getId() != payRequest.getPayChannel()
                && EnumPayChannelSign.YG_YINLIAN.getId() != payRequest.getPayChannel()) {
            return CommonResponse.simpleResponse(-1, "支付方式错误");
        }
        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), 2);
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", resultPair.getRight()).addParam("subMerName", merchantInfo.get().getMerchantName())
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
                                             final HttpServletRequest request) {
        final Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(payRequest.getMerchantId());
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()){
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

        final Pair<Integer, String> resultPair = this.payService.codeReceipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), merchantInfo.get().getId());
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", resultPair.getRight()).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }


    /**
     * 商户提现
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(@RequestBody final WithdrawRequest withdrawRequest, final HttpServletRequest request) {
//        final String verifiedCode = withdrawRequest.getCode();
//        if (!super.isLogin(request)) {
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
//        if (!userInfoOptional.isPresent()) {
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
//        if (!merchantInfo.isPresent()) {
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        if (merchantInfo.get().getStatus() != EnumMerchantStatus.PASSED.getId()) {
//            return CommonResponse.simpleResponse(-2, "未审核通过");
//        }
//        final Pair<Integer, String> checkResult =
//                this.smsAuthService.checkVerifyCode(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()), verifiedCode, EnumVerificationCodeType.WITH_DRAW);
//        if (1 != checkResult.getLeft()) {
//            return CommonResponse.simpleResponse(-1, checkResult.getRight());
//        }
        final Pair<Integer, String> resultPair = this.withdrawService.merchantWithdrawByOrder(2, 106, "D0");
        if (0 == resultPair.getLeft()) {
            return CommonResponse.simpleResponse(1, "受理成功");
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }

}
