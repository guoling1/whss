package com.jkm.hss.controller.merchant;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.service.DealerWithdrawService;
import com.jkm.hss.bill.service.MerchantWithdrawService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.MerchantWithdrawRequest;
import com.jkm.hss.helper.response.AccountInfoResponse;
import com.jkm.hss.helper.response.FlowDetailsSelectResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantFlowRequest;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-02-14.
 */
@Slf4j
@RequestMapping(value = "/account")
@Controller
public class AccountController extends BaseController{

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private DealerWithdrawService dealerWithdrawService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantWithdrawService merchantWithdrawService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private OemInfoService oemInfoService;

    /**
     * 跳到提现页面
     * @return
     */
    @RequestMapping(value = "/toWithdraw", method = RequestMethod.GET)
    public String toWithdrawJsp(final HttpServletRequest request, final HttpServletResponse response,Model model) throws UnsupportedEncodingException {
        String oemNo = request.getParameter("oemNo");
        if(oemNo!=null&&!"".equals(oemNo)){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
            model.addAttribute("oemName",oemInfoOptional.get().getBrandName());
        }else{
            model.addAttribute("oemName","好收收");
        }
        model.addAttribute("oemNo",oemNo);
        return "/withdraw";
    }

    /**
     * 跳到余额页面
     * @return
     */
    @RequestMapping(value = "/toHssAccount", method = RequestMethod.GET)
    public String toHssAccount(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws UnsupportedEncodingException {
        String oemNo = request.getParameter("oemNo");
        if(oemNo!=null&&!"".equals(oemNo)){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
            model.addAttribute("oemName",oemInfoOptional.get().getBrandName());
        }else{
            model.addAttribute("oemName","好收收");
        }
        model.addAttribute("oemNo",oemNo);
        return "/hssAccount";
    }

    /**
     * 跳到余额流水详情
     * @return
     */
    @RequestMapping(value = "/toHssAccountFlow", method = RequestMethod.GET)
    public String toHssAccountFlow(HttpServletRequest request){

        return "/hssAccountFlow";
    }

    /**
     * 跳到提现页面
     * @return
     */
    @RequestMapping(value = "/toHssWithdrawSuccess", method = RequestMethod.GET)
    public String toHssWithdrawSuccess(HttpServletRequest request){

        return "/hssWithdrawSuccess";
    }

    /**
     * 獲取帳戶詳情，
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public CommonResponse withdraw(HttpServletRequest request){

        try{
            UserInfo userInfo = userInfoService.selectByOpenId(super.getOpenId(request)).get();
            //final UserInfo userInfo = userInfoService.selectByOpenId("ou2YpwfghecQNYYUpIQ-kbqGY7Hc").get();
            final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();
            log.info( "<<<" + merchantInfo.getId() +">>>商户提现页面");
            final MerchantChannelRateRequest channelRateRequest = new MerchantChannelRateRequest();
            channelRateRequest.setChannelTypeSign(EnumPayChannelSign.YG_WECHAT.getId());
            channelRateRequest.setProductId(merchantInfo.getProductId());
            channelRateRequest.setMerchantId(merchantInfo.getId());
            final MerchantChannelRate merchantChannelRate =
                    this.merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(channelRateRequest).get();

            final Account account = this.accountService.getById(merchantInfo.getAccountId()).get();
            final AccountBank accountBank = this.accountBankService.getDefault(merchantInfo.getAccountId());
            AccountInfoResponse response = new AccountInfoResponse();
            response.setTotalAmount(account.getTotalAmount());
            response.setAvailable(account.getAvailable());
            response.setBankName(accountBank.getBankName());
            response.setWithdrawFee(merchantChannelRate.getMerchantWithdrawFee());
            response.setSettleAmount(account.getDueSettleAmount());

            final String bankNo = accountBank.getBankNo();
            response.setBankNo("尾号" + bankNo.substring(bankNo.length() - 4 , bankNo.length()));
            response.setMobile(accountBank.getReserveMobile());

            return CommonResponse.objectResponse(1, "SUCCESS", response);

        }catch (final Throwable throwable){
            log.error( "<<<" + super.getOpenId(request) +">>>商户提现异常，异常：" + throwable.getMessage());
            return CommonResponse.simpleResponse(-1, "fail");
        }

    }

    /**
     * 商户资金账户变更记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/flowDetails", method = RequestMethod.POST)
    public CommonResponse flowDetails(@RequestBody MerchantFlowRequest flowRequest, HttpServletRequest request){

        try{
            UserInfo userInfo = userInfoService.selectByOpenId(super.getOpenId(request)).get();
            //final UserInfo userInfo = userInfoService.selectByOpenId("ou2YpwcIteXav-vgB9l6p3d0B5VA").get();
            final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();

            PageModel<AccountFlow> pageModel = this.accountFlowService.selectByParamToMerchantFlow(flowRequest.getPageNo(), flowRequest.getPageSize(),merchantInfo.getAccountId());

            final List<AccountFlow> records = pageModel.getRecords();

            List<FlowDetailsSelectResponse> list = Lists.transform(records, new Function<AccountFlow, FlowDetailsSelectResponse>() {
                @Override
                public FlowDetailsSelectResponse apply(AccountFlow input) {
                    FlowDetailsSelectResponse response = new FlowDetailsSelectResponse();
                    response.setFlowSn(input.getOrderNo());
                    response.setCreateTime(DateFormatUtil.format(input.getCreateTime(),DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
                    response.setIncomeAmount(input.getIncomeAmount().toString());
                    response.setOutAmount(input.getOutAmount().toString());
                    response.setRemark(input.getRemark());
                    return response;
                }
            });

            PageModel<FlowDetailsSelectResponse> model = new PageModel<>(pageModel.getPageNO(),pageModel.getPageSize());
            model.setCount(pageModel.getCount());
            model.setRecords(list);

            return CommonResponse.objectResponse(1, "success", model);
        }catch (final Throwable throwable){
            log.error("获取账户流水异常：" + throwable.getMessage());
        }

        return CommonResponse.simpleResponse(-1 , "fail");
    }

    /**
     * 提现
     *
     * @param withdrawRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(HttpServletRequest request, @RequestBody final MerchantWithdrawRequest withdrawRequest) {

        log.info(super.getDealerId() + ">>>>>申请提现， 提现金额：" + withdrawRequest.getAmount());
        try{
            UserInfo userInfo = userInfoService.selectByOpenId(super.getOpenId(request)).get();
            //final UserInfo userInfo = userInfoService.selectByOpenId("ou2YpwfghecQNYYUpIQ-kbqGY7Hc").get();
            final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();

            final String mobile = MerchantSupport.decryptMobile(merchantInfo.getReserveMobile());

           final Pair<Integer, String> pair = smsAuthService.checkVerifyCode(mobile, withdrawRequest.getCode(), EnumVerificationCodeType.WITH_DRAW);

            if (1 != pair.getLeft()) {
                return CommonResponse.simpleResponse(-1, pair.getRight());
            }
            final MerchantChannelRateRequest channelRateRequest = new MerchantChannelRateRequest();
            channelRateRequest.setChannelTypeSign(EnumPayChannelSign.YG_WECHAT.getId());
            channelRateRequest.setProductId(merchantInfo.getProductId());
            channelRateRequest.setMerchantId(merchantInfo.getId());
            final MerchantChannelRate merchantChannelRate =
                    this.merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(channelRateRequest).get();
            final BigDecimal merchantWithdrawFee = merchantChannelRate.getMerchantWithdrawFee();

            if ( (new BigDecimal(withdrawRequest.getAmount()).compareTo(merchantWithdrawFee) == -1)){
                return CommonResponse.simpleResponse(-1, "提现金额不得小于手续费");
            }

            final Optional<Account> accountOptional = this.accountService.getById(merchantInfo.getAccountId());
            if (!accountOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "账户不存在");
            }
            final Account account = accountOptional.get();
            if (account.getAvailable().compareTo(new BigDecimal(withdrawRequest.getAmount())) < 0) {
                return CommonResponse.simpleResponse(-1, "可用余额不足");
            }
            if (EnumPayChannelSign.YG_WECHAT.getId() != withdrawRequest.getChannel()
                    && EnumPayChannelSign.YG_ALIPAY.getId() != withdrawRequest.getChannel()
                    && EnumPayChannelSign.YG_UNIONPAY.getId() != withdrawRequest.getChannel()) {
                return CommonResponse.simpleResponse(-1, "提现方式错误");
            }
            final Pair<Integer, String> withdraw = this.merchantWithdrawService.withdraw(account.getId(), withdrawRequest.getAmount(),
                    withdrawRequest.getChannel(), "dealerWithdraw", merchantWithdrawFee);
            if (0 == withdraw.getLeft()) {
                return CommonResponse.simpleResponse(1, "提现受理成功");
            }

            return CommonResponse.simpleResponse(-1, "提现失败");
        }catch (final Throwable throwable){

            log.error("提现异常：" + throwable.getMessage());
        }

        return  CommonResponse.simpleResponse(-1, "提现失败");
    }

    /**
     * 发送提现验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
    public CommonResponse sendVerifyCode(HttpServletRequest request) {

        UserInfo userInfo = userInfoService.selectByOpenId(super.getOpenId(request)).get();
        //final UserInfo userInfo = userInfoService.selectByOpenId("ou2YpwfghecQNYYUpIQ-kbqGY7Hc").get();
        final MerchantInfo merchantInfo = this.merchantInfoService.selectById(userInfo.getMerchantId()).get();
        log.info( "<<<" + merchantInfo.getId() +">>>商户提现发送验证码");
        final String mobile = MerchantSupport.decryptMobile(merchantInfo.getReserveMobile());

        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }

        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.WITH_DRAW);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(mobile)
                    .uid(merchantInfo.getId() + "")
                    .data(params)
                    .userType(EnumUserType.FOREGROUND_USER)
                    .noticeType(EnumNoticeType.WITHDRAW_CODE)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

}
