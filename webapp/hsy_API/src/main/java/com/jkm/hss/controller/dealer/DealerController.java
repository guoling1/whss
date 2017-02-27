package com.jkm.hss.controller.dealer;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.*;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.FirstLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.MyMerchantCount;
import com.jkm.hss.admin.helper.SecondLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerPassport;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import com.jkm.hss.dealer.enums.EnumPassportType;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.dealer.helper.requestparam.SecondLevelDealerAddRequest;
import com.jkm.hss.dealer.service.DealerPassportService;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.PastRecordService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/dealer")
public class DealerController extends BaseController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerRateService dealerRateService;

    @Autowired
    private SmsAuthService smsAuthService;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private DealerPassportService dealerPassportService;

    @Autowired
    private BankCardBinService bankCardBinService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;

    @Autowired
    private ShallProfitDetailService shallProfitDetailService;

    @Autowired
    private DealerChannelRateService dealerChannelRateService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PastRecordService pastRecordService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "/dealer/login";
    }

    /**
     * 添加二级代理页面
     *
     * @return
     */
    @RequestMapping(value = "/addSecondDealerPage", method = RequestMethod.GET)
    public String addSecondDealerPage(final Model model) {
        final Optional<Dealer> dealerOptional = super.getDealer();
        Preconditions.checkState(dealerOptional.get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以添加二级代理");
        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(dealerOptional.get().getId());
        for (DealerChannelRate channelRate : channelRates) {
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WECHAT_PUBLIC.getId()) {
                model.addAttribute("weixinSettleRate", channelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2));
                model.addAttribute("weixinMerchantSettleRate", channelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ALIPAY_PUBLIC.getId()) {
                model.addAttribute("alipaySettleRate", channelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2));
                model.addAttribute("alipayMerchantSettleRate", channelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
                model.addAttribute("quickSettleRate", channelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2));
                model.addAttribute("quickMerchantSettleRate", channelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2));
                model.addAttribute("withdrawSettleFee", channelRate.getDealerWithdrawFee());
                model.addAttribute("withdrawMerchantSettleFee", channelRate.getDealerMerchantWithdrawFee());
            }
        }
        return "/dealer/addDealer";
    }


    /**
     * 登录发送验证码
     *
     * @param codeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
    public CommonResponse sendVerifyCode(@RequestBody final DealerSendVerifyCodeRequest codeRequest) {
        final String mobile = codeRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(mobile);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "不存在的用户登录");
        }
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.LOGIN_DEALER);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(mobile)
                    .uid(dealerOptional.get().getId() + "")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.LOGIN_DEALER)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * 登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ajaxLogin", method = RequestMethod.POST)
    public CommonResponse ajaxLogin(final HttpServletResponse response, @RequestBody final DealerLoginRequest loginRequest) {
        final String mobile = loginRequest.getMobile();
        final String verifyCode = loginRequest.getCode();
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(mobile);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "不存在的用户登录");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.LOGIN_DEALER);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        final DealerPassport dealerPassport =
                this.dealerPassportService.createPassport(dealerOptional.get().getId(), EnumPassportType.MOBILE, EnumLoginStatus.LOGIN);

        CookieUtil.setSessionCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, dealerPassport.getToken(),
                ApplicationConsts.getApplicationConfig().domain(), (int)(DealerConsts.TOKEN_EXPIRE_MILLIS / 1000));
        this.dealerService.updateLoginDate(dealerOptional.get().getId());
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "登录成功").addParam("url", "/dealer/indexInfo").build();
    }

    /**
     * 登出
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public CommonResponse<BaseEntity> logout(final HttpServletResponse response){
        this.dealerPassportService.markAsLogout(getDealerId());
        CookieUtil.deleteCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }

    /**
     * 代理商首页信息展示
     *
     * @return
     */
    @RequestMapping(value = "/indexInfo")
    public String indexInfo(final Model model){
        try{
            Optional<Dealer> dealerOptional = super.getDealer();
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商信息不存在");
            //final Optional<Dealer> dealerOptional = this.dealerService.getById(4);
           final Dealer dealer = dealerOptional.get();
            final DealerIndexInfoResponse response = new DealerIndexInfoResponse();
            if (dealer.getLevel() == EnumDealerLevel.SECOND.getId()){
                //昨日分润总额
                final BigDecimal yesterdayProfitMoney = this.shallProfitDetailService.selectSecondYesterdayProfitMoneyToHss(dealer.getId(), getProfitDate(new Date()));
                //昨日店铺交易总额
                final BigDecimal yesterdayDealMoney = this.shallProfitDetailService.selectSecondYesterdayDealMoneyToHss(dealer.getId(), getProfitDate(new Date()));
                //待结算分润
                final JSONObject jsonObject = this.pastRecordService.settleMoney(dealer.getAccountId());
                final BigDecimal waitBalanceMoney = new BigDecimal(jsonObject.getString("settleMoney"));
                //已结分润
                //历史分润总额
                final BigDecimal alreadyBalanceMoney = new BigDecimal(jsonObject.getString("unSettleMoney"));
                final BigDecimal historyProfitMoney = this.shallProfitDetailService.selectSecondHistoryProfitMoneyToHss(dealer.getId());
                //查询二级代理商二维码分配情况信息
                final SecondLevelDealerCodeInfo secondLevelDealerCodeInfo = this.qrCodeService.getSecondLevelDealerCodeInfos(dealer.getId()).get();
                //我发展的店铺统计
                final MyMerchantCount myMerchantCount = this.qrCodeService.getMyMerchantCount(dealer.getId(), EnumDealerLevel.SECOND.getId()).get();
                response.setLevel(EnumDealerLevel.SECOND.getId());
                response.setYesterdayProfitMoney(yesterdayProfitMoney);
                response.setYesterdayDealMoney(yesterdayDealMoney);
                response.setHistoryProfitMoney(historyProfitMoney);
                response.setSecondLevelDealerCodeInfo(secondLevelDealerCodeInfo);
                response.setMyMerchantCount(myMerchantCount);
                response.setWaitBalanceMoney(waitBalanceMoney);
                response.setAlreadyBalanceMoney(alreadyBalanceMoney);
            }else{
                //我的分润-二级代理 昨日分润金额
                final BigDecimal secondYesterdayProfitMoney = this.shallProfitDetailService.selectFirstSecondYesterdayProfitMoneyToHss(dealer.getId(), getProfitDate(new Date()));
                //我的分润-二级代理 历史分润总额
                BigDecimal secondHistoryProfitMoney = this.shallProfitDetailService.selectFirstSecondHistoryProfitMoneyToHss(dealer.getId());
                //我的分润-直管店铺 昨日分润金额
                final BigDecimal merchantYesterdayProfitMoney = this.shallProfitDetailService.selectFirstMerchantYesterdayProfitMoneyToHss(dealer.getId(), getProfitDate(new Date()));
                //我的分润-直管店铺 历史分润总额
                final BigDecimal merchantHistoryProfitMoney = this.shallProfitDetailService.selectFirstMerchantHistoryProfitMoneyToHss(dealer.getId());
                //昨日分润总额
                final BigDecimal yesterdayProfitMoney = secondYesterdayProfitMoney.add(merchantYesterdayProfitMoney);
                //昨日店铺交易总额
                final BigDecimal yesterdayDealMoney = this.shallProfitDetailService.selectFirstYesterdayDealMoneyToHss(dealer.getId(), getProfitDate(new Date()));
                //待结算分润
                //已结分润
                // 查询一级代理商二维码分配情况信息
                final FirstLevelDealerCodeInfo firstLevelDealerCodeInfos = this.qrCodeService.getFirstLevelDealerCodeInfos(dealer.getId()).get();
                //一级代理商发展二级代理的个数
                final int mySecondDealerCount = this.dealerService.getMyDealerCount(dealer.getId());
                //一级代理商分配给二级代理商的二维码数
                final int distributeToSecondDealerCodeCount = this.qrCodeService.getDistributeToSecondDealerCodeCount(dealer.getId());
                //我发展的店铺统计
                final MyMerchantCount myMerchantCount = this.qrCodeService.getMyMerchantCount(dealer.getId(), EnumDealerLevel.FIRST.getId()).get();
                response.setLevel(EnumDealerLevel.FIRST.getId());
                response.setSecondYesterdayProfitMoney(secondYesterdayProfitMoney);
                response.setSecondHistoryProfitMoney(secondHistoryProfitMoney);
                response.setMerchantYesterdayProfitMoney(merchantYesterdayProfitMoney);
                response.setMerchantHistoryProfitMoney(merchantHistoryProfitMoney);
                response.setYesterdayProfitMoney(yesterdayProfitMoney);
                response.setYesterdayDealMoney(yesterdayDealMoney);
                response.setFirstLevelDealerCodeInfos(firstLevelDealerCodeInfos);
                response.setMySecondDealerCount(mySecondDealerCount);
                response.setDistributeToSecondDealerCodeCount(distributeToSecondDealerCodeCount);
                response.setMyMerchantCount(myMerchantCount);
            }
            response.setMobile(dealer.getPlainMobile());
           //return  CommonResponse.objectResponse(1, "success", response);
            model.addAttribute("data", response);
            if (response.getLevel() == EnumDealerLevel.FIRST.getId()){
                return "/dealer/indexFirst";
            }else{
                return "/dealer/indexSecond";
            }
        }catch (final Throwable throwable){
            log.error("获取代理商首页信息异常,异常信息:" + throwable.getMessage());
        }
          //  return CommonResponse.simpleResponse(-1, "fail");
        //TODO
        return "500";
    }
    private String getProfitDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        final String format = DateFormatUtil.format(date, DateFormatUtil.yyyy_MM_dd);
        return format;
    }

    /**
     * 分配二维码页面
     *
     * @return
     */
    @RequestMapping(value = "/toDistributeCode")
    public String toDistributeCodePage() {
        return "/dealer/issue";
    }


    /**
     * 查找一级代理未分配未激活的二维码
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "unDistributeCode")
    public String findUnDistributeCode(final Model model) {
        Preconditions.checkArgument(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理");
        final List<Pair<QRCode, QRCode>> pairs = this.dealerService.getUnDistributeCode(super.getDealerId());
        final List<UnDistributeCodeResponse> codeResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pairs)) {
            for (Pair<QRCode, QRCode> pair : pairs) {
                final UnDistributeCodeResponse unDistributeCodeResponse = new UnDistributeCodeResponse();
                unDistributeCodeResponse.setStartCode(pair.getLeft().getCode());
                unDistributeCodeResponse.setEndCode(pair.getRight().getCode());
                unDistributeCodeResponse.setCount((int) (Long.valueOf(pair.getRight().getCode()) - Long.valueOf(pair.getLeft().getCode()) + 1));
                codeResponses.add(unDistributeCodeResponse);
            }
        }
        model.addAttribute("codes", codeResponses);
        return "/dealer/unDistributeRecord";
    }


    /**
     * 查询代理商某二维码范围下的未分配的二维码个数
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "findCodeCount", method = RequestMethod.POST)
    public CommonResponse findCodeCount(@RequestBody final FindUnDistributeCodeCountRequest request) {
        Preconditions.checkArgument(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理");
        final String startCode = request.getStartCode();
        final String endCode = request.getEndCode();
        if (!StringUtils.isNumeric(startCode)) {
            return CommonResponse.simpleResponse(-1, "输入的开始二维码格式错误");
        }
        if (!StringUtils.isNumeric(endCode)) {
            return CommonResponse.simpleResponse(-1, "输入的结束二维码格式错误");
        }
        final int count = this.dealerService.getUnDistributeCodeCountByRangeCode(startCode, endCode, super.getDealerId());
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success").addParam("count", count).build();
    }


    /**
     * 添加二级代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addSecondDealer", method = RequestMethod.POST)
    public CommonResponse addSecondDealer(@RequestBody final SecondLevelDealerAddRequest secondLevelDealerAddRequest) {
        Preconditions.checkState(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以添加二级代理");
        final String mobile = secondLevelDealerAddRequest.getMobile();
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "代理手机号错误");
        }
        final String bankCard = secondLevelDealerAddRequest.getBankCard();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
        if (!bankCardBinOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "结算卡格式错误");
        }
        secondLevelDealerAddRequest.setBankName(bankCardBinOptional.get().getBankName());
        final String bankReserveMobile = secondLevelDealerAddRequest.getBankReserveMobile();
        if (!ValidateUtils.isMobile(bankReserveMobile)) {
            return CommonResponse.simpleResponse(-1, "银行预留手机号错误");
        }
        final long proxyNameCount = this.dealerService.getByProxyName(secondLevelDealerAddRequest.getName());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "代理商名字已经存在");
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(secondLevelDealerAddRequest.getMobile());
        if (dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
        }
        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(super.getDealerId());
        for (DealerChannelRate channelRate : channelRates) {
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WECHAT_PUBLIC.getId()) {

                final BigDecimal withdrawSettleFee = new BigDecimal(secondLevelDealerAddRequest.getWithdrawSettleFee());
                if (!(withdrawSettleFee.compareTo(channelRate.getDealerWithdrawFee()) > 0
                        && withdrawSettleFee.compareTo(channelRate.getDealerMerchantWithdrawFee()) < 0)) {
                    return CommonResponse.simpleResponse(-1, "提现结算价错误");
                }

                final BigDecimal weixinSettleRate = new BigDecimal(secondLevelDealerAddRequest.getWeixinSettleRate())
                        .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                if (!(weixinSettleRate.compareTo(channelRate.getDealerTradeRate()) > 0
                        && weixinSettleRate.compareTo(channelRate.getDealerMerchantPayRate()) < 0)) {
                    return CommonResponse.simpleResponse(-1, "微信结算费率错误");
                }
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ALIPAY_PUBLIC.getId()) {
                final BigDecimal alipaySettleRate = new BigDecimal(secondLevelDealerAddRequest.getAlipaySettleRate())
                        .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                if (!(alipaySettleRate.compareTo(channelRate.getDealerTradeRate()) > 0
                        && alipaySettleRate.compareTo(channelRate.getDealerMerchantPayRate()) < 0)) {
                    return CommonResponse.simpleResponse(-1, "支付宝结算费率错误");
                }
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
                final BigDecimal quickSettleRate = new BigDecimal(secondLevelDealerAddRequest.getQuickSettleRate())
                        .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                if (!(quickSettleRate.compareTo(channelRate.getDealerTradeRate()) > 0
                        && quickSettleRate.compareTo(channelRate.getDealerMerchantPayRate()) < 0)) {
                    return CommonResponse.simpleResponse(-1, "快捷支付结算费率错误");
                }
            }
        }
        final long secondDealerId = this.dealerService.createSecondDealer(secondLevelDealerAddRequest, super.getDealerId());
        final SecondLevelDealerAddResponse secondLevelDealerAddResponse = new SecondLevelDealerAddResponse();
        secondLevelDealerAddResponse.setDealerId(secondDealerId);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", secondLevelDealerAddResponse);
    }

    /**
     *查询当前登录代理商信息
     *
     * @return
     */
    @RequestMapping(value = "/get")
    public String get(final Model model) {
        //final Optional<Dealer> dealerOptional = this.dealerService.getById(5);
        final Optional<Dealer> dealerOptional = this.dealerService.getById(super.getDealerId());
        if (!dealerOptional.isPresent()) {
            return "/500";
        }
        final Dealer dealer = dealerOptional.get();
        final DealerGetResponse dealerGetResponse = new DealerGetResponse();
        dealerGetResponse.setDealerId(dealer.getId());
        dealerGetResponse.setMobile(dealer.getMobile());
        dealerGetResponse.setProxyName(dealer.getProxyName());
        dealerGetResponse.setBelongArea(dealer.getBelongArea());
        dealerGetResponse.setBankAccountName(dealer.getBankAccountName());
        dealerGetResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
        dealerGetResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(dealer.getId());
        Preconditions.checkState(!CollectionUtils.isEmpty(channelRates), "代理商费率不存在");
        for (DealerChannelRate channelRate : channelRates) {
            dealerGetResponse.setMerchantSettleRate(channelRate.getDealerMerchantWithdrawFee().toString());
            dealerGetResponse.setMerchantWithdrawSettleFee(channelRate.getDealerMerchantWithdrawFee().toString());
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WECHAT_PUBLIC.getId()) {
                dealerGetResponse.setWeixinSettleRate(channelRate.getDealerTradeRate().multiply(new BigDecimal(100)).setScale(2).toPlainString());
                dealerGetResponse.setWithdrawSettleFee(channelRate.getDealerWithdrawFee().toPlainString());
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ALIPAY_PUBLIC.getId()) {
                dealerGetResponse.setAlipaySettleRate(channelRate.getDealerTradeRate().multiply(new BigDecimal(100)).setScale(2).toPlainString());
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
                dealerGetResponse.setQuickSettleRate(channelRate.getDealerTradeRate().multiply(new BigDecimal(100)).setScale(2).toPlainString());
            }
        }
        model.addAttribute("data", dealerGetResponse);
        return "/dealer/myMsg";
    }
    /**
     *查询当前登录代理商银行卡信息
     *
     * @return
     */
    @RequestMapping(value = "/getCard")
    public String getCard(final Model model) {
        //final Optional<Dealer> dealerOptional = this.dealerService.getById(5);
        final Optional<Dealer> dealerOptional = this.dealerService.getById(super.getDealerId());
        if (!dealerOptional.isPresent()) {
            return "/500";
        }
        final Dealer dealer = dealerOptional.get();
        final DealerGetResponse dealerGetResponse = new DealerGetResponse();
        dealerGetResponse.setDealerId(dealer.getId());
        dealerGetResponse.setMobile(dealer.getMobile());
        dealerGetResponse.setProxyName(dealer.getProxyName());
        dealerGetResponse.setBelongArea(dealer.getBelongArea());
        dealerGetResponse.setBankAccountName(dealer.getBankAccountName());
        dealerGetResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
        dealerGetResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
        model.addAttribute("data", dealerGetResponse);
        return "/dealer/myCard";
    }


    /**
     * 按手机号和名称模糊匹配
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public CommonResponse find(final HttpServletRequest request) {
        Preconditions.checkState(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以分配二维码");
        final String condition = request.getParameter("condition");
        final String _condition = StringUtils.trim(condition);
        Preconditions.checkState(!Strings.isNullOrEmpty(_condition), "查询条件不能为空");
        List<Dealer> dealerList = this.dealerService.findByCondition(_condition, super.getDealerId(),
                EnumDealerLevel.SECOND.getId());
        final List<SecondLevelDealerFindResponse> responses = Lists.transform(dealerList, new Function<Dealer, SecondLevelDealerFindResponse>() {
            @Override
            public SecondLevelDealerFindResponse apply(Dealer input) {
                final SecondLevelDealerFindResponse secondLevelDealerFindResponse = new SecondLevelDealerFindResponse();
                secondLevelDealerFindResponse.setDealerId(input.getId());
                secondLevelDealerFindResponse.setMobile(input.getMobile());
                secondLevelDealerFindResponse.setName(input.getProxyName());
                return secondLevelDealerFindResponse;
            }
        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responses);
    }

    /**
     * 一级代理商分配码段
     *
     * @return
     */
    @RequestMapping(value = "/distributeQRCode", method = RequestMethod.POST)
    public String distributeQRCode(final DistributeQRCodeRequest distributeQRCodeRequest, final Model model) {
        Preconditions.checkState(getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以分配二维码");
        final int isSelf = distributeQRCodeRequest.getIsSelf();

        long dealerId = distributeQRCodeRequest.getDealerId();
        if (EnumBoolean.TRUE.getCode() == isSelf) {
            dealerId = super.getDealerId();
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        Preconditions.checkState(dealerOptional.isPresent(), "要分配二维码的代理商[{}]不存在", dealerId);
        Preconditions.checkState(EnumBoolean.TRUE.getCode() == isSelf ? true : dealerOptional.get().getFirstLevelDealerId() == super.getDealerId(),
                "二级代理商[{}]不是当前一级代理商[{}]的二级代理", dealerId, super.getDealerId());
        final List<DistributeQRCodeRecord> records = this.dealerService.distributeQRCode(super.getDealerId(),
                dealerId, distributeQRCodeRequest.getStartCode(), distributeQRCodeRequest.getEndCode());
        final DistributeQRCodeResponse distributeQRCodeResponse = new DistributeQRCodeResponse();
        distributeQRCodeResponse.setFirstLevelDealerId(super.getDealerId());
        distributeQRCodeResponse.setToDealerId(dealerId);
        distributeQRCodeResponse.setName(dealerOptional.get().getProxyName());
        distributeQRCodeResponse.setMobile(dealerOptional.get().getMobile());
        distributeQRCodeResponse.setDistributeDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
        int count = 0;
        final List<DistributeQRCodeResponse.Code> codes = new ArrayList<>();
        for (DistributeQRCodeRecord record : records) {
            count += record.getCount();
            final DistributeQRCodeResponse.Code code = distributeQRCodeResponse.new Code();
            code.setStartCode(record.getStartCode());
            code.setEndCode(record.getEndCode());
            codes.add(code);
        }
        distributeQRCodeResponse.setCount(count);
        distributeQRCodeResponse.setCodes(codes);
        model.addAttribute("data", distributeQRCodeResponse);
        return "/dealer/issueSuccess";
    }

    /**
     * 查询分配记录
     *
     * @return
     */
    @RequestMapping(value = "/distributeRecords")
    public String getDistributeQRCodeRecords(final Model model) {
        Preconditions.checkState(getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理");
        final List<DistributeQRCodeRecord> records = this.dealerService.getDistributeToSecondDealerQRCode(super.getDealerId());
        final HashSet<Long> set = new HashSet<>();
        final List<Long> dealerIds = Lists.transform(records, new Function<DistributeQRCodeRecord, Long>() {
            @Override
            public Long apply(DistributeQRCodeRecord input) {
                return input.getSecondLevelDealerId();
            }
        });
        set.addAll(dealerIds);
        List<Dealer> dealers = this.dealerService.getByIds(new ArrayList<>(set));
        final Map<Long, Dealer> dealerMap = Maps.uniqueIndex(dealers, new Function<Dealer, Long>() {
            @Override
            public Long apply(Dealer input) {
                return input.getId();
            }
        });
        final List<DistributeQRCodeSelectResponse> recordResult = Lists.transform(records, new Function<DistributeQRCodeRecord, DistributeQRCodeSelectResponse>() {
            @Override
            public DistributeQRCodeSelectResponse apply(DistributeQRCodeRecord input) {
                final DistributeQRCodeSelectResponse distributeQRCodeSelectResponse = new DistributeQRCodeSelectResponse();
                distributeQRCodeSelectResponse.setSecondLevelDealerName(dealerMap.get(input.getSecondLevelDealerId()).getProxyName());
                distributeQRCodeSelectResponse.setStartCode(input.getStartCode());
                distributeQRCodeSelectResponse.setEndCode(input.getEndCode());
                distributeQRCodeSelectResponse.setCount(input.getCount());
                distributeQRCodeSelectResponse.setDistributeDate(DateFormatUtil.format(input.getCreateTime(), DateFormatUtil.yyyy_MM_dd));
                return distributeQRCodeSelectResponse;
            }
        });
        model.addAttribute("toSecond", recordResult);
        model.addAttribute("toSelf", this.getDistributeToSelfQRCode(super.getDealerId()));
        return "/dealer/record";
    }


    /**
     * 查询分配给自己的二维码记录
     *
     * @return
     */
    private List<DistributeQRCodeSelectResponse> getDistributeToSelfQRCode(final long dealerId) {
        final List<DistributeQRCodeRecord> records = this.dealerService.getDistributeToSelfQRCode(dealerId);
        final List<DistributeQRCodeSelectResponse> recordResult = Lists.transform(records, new Function<DistributeQRCodeRecord, DistributeQRCodeSelectResponse>() {
            @Override
            public DistributeQRCodeSelectResponse apply(DistributeQRCodeRecord input) {
                final DistributeQRCodeSelectResponse distributeQRCodeSelectResponse = new DistributeQRCodeSelectResponse();
                distributeQRCodeSelectResponse.setStartCode(input.getStartCode());
                distributeQRCodeSelectResponse.setEndCode(input.getEndCode());
                distributeQRCodeSelectResponse.setCount(input.getCount());
                distributeQRCodeSelectResponse.setDistributeDate(DateFormatUtil.format(input.getCreateTime(), DateFormatUtil.yyyy_MM_dd));
                return distributeQRCodeSelectResponse;
            }
        });
        return recordResult;
    }


    /**
     * 查找我的门店
     *
     * @return
     */
    @RequestMapping(value = "/getMyMerchants")
    public String getMyMerchants(final Model model) {
        final List<MerchantInfo> merchantInfos = this.dealerService.getMyMerchants(super.getDealerId());
        final List<Long> ids = Lists.transform(merchantInfos, new Function<MerchantInfo, Long>() {
            @Override
            public Long apply(MerchantInfo input) {
                return input.getId();
            }
        });
        final List<QRCode> codes = this.qrCodeService.getByMerchantIds(ids);
        final Map<Long, QRCode> merchantCodeMap = Maps.uniqueIndex(codes, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getMerchantId();
            }
        });
        final List<MyMerchantResponse> responses = Lists.transform(merchantInfos, new Function<MerchantInfo, MyMerchantResponse>() {
            @Override
            public MyMerchantResponse apply(MerchantInfo input) {
                final MyMerchantResponse myMerchantResponse = new MyMerchantResponse();
                myMerchantResponse.setMerchantName(input.getMerchantName());
                myMerchantResponse.setCode(merchantCodeMap.get(input.getId()).getCode());
                myMerchantResponse.setRegisterDate(DateFormatUtil.format(input.getCreateTime(), DateFormatUtil.yyyy_MM_dd));
                if (EnumMerchantStatus.INIT.getId() == input.getStatus()
                        || EnumMerchantStatus.ONESTEP.getId() == input.getStatus()) {
                    myMerchantResponse.setStatus("已注册");
                } else if (EnumMerchantStatus.REVIEW.getId() == input.getStatus()) {
                    myMerchantResponse.setStatus(EnumMerchantStatus.REVIEW.getName());
                } else if (EnumMerchantStatus.PASSED.getId() == input.getStatus()) {
                    myMerchantResponse.setStatus(EnumMerchantStatus.PASSED.getName());
                } else if (EnumMerchantStatus.UNPASSED.getId() == input.getStatus()) {
                    myMerchantResponse.setStatus(EnumMerchantStatus.UNPASSED.getName());
                } else if (EnumMerchantStatus.DISABLE.getId() == input.getStatus()) {
                    myMerchantResponse.setStatus(EnumMerchantStatus.DISABLE.getName());
                }
                return myMerchantResponse;
            }
        });
        model.addAttribute("merchants", responses);
        return "/dealer/myStore";
    }

    /**
     * 查询我发展的二级代理
     *
     * @return
     */
    @RequestMapping(value = "/getMyDealers")
    public String getMyDealers(final Model model) {
        Preconditions.checkState(getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理");
        final List<Triple<Dealer, DistributeCodeCount, ActiveCodeCount>> myDealers =
                this.dealerService.getMyDealers(super.getDealerId());
        final List<MyDealerResponse> responses = Lists.transform(myDealers,
                new Function<Triple<Dealer, DistributeCodeCount, ActiveCodeCount>, MyDealerResponse>() {
            @Override
            public MyDealerResponse apply(Triple<Dealer, DistributeCodeCount, ActiveCodeCount> input) {
                final Dealer dealer = input.getLeft();
                final DistributeCodeCount distributeCodeCount = input.getMiddle();
                final ActiveCodeCount activeCodeCount = input.getRight();
                final MyDealerResponse myDealerResponse = new MyDealerResponse();
                myDealerResponse.setDealerId(dealer.getId());
                myDealerResponse.setProxyName(dealer.getProxyName());
                myDealerResponse.setDistributeCount(null == distributeCodeCount ? 0 : distributeCodeCount.getCount());
                myDealerResponse.setActiveCount(null == activeCodeCount ? 0 : activeCodeCount.getCount());
                return myDealerResponse;
            }
        });
        model.addAttribute("dealers", responses);
        return "/dealer/myDealer";
    }

    /**
     *一级代理发展的二级代理详情
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMyDealerDetail", method = RequestMethod.POST)
    public CommonResponse getMyDealerDetail(@RequestBody MyDealerDetailGetRequest myDealerDetailGetRequest) {
        Preconditions.checkState(getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理");
        final Triple<Dealer, List<DistributeQRCodeRecord>, List<DealerChannelRate>> myDealerDetail =
                this.dealerService.getMyDealerDetail(super.getDealerId(), myDealerDetailGetRequest.getDealerId());
        final MyDealerDetailResponse myDealerDetailResponse = new MyDealerDetailResponse();
        final Dealer dealer = myDealerDetail.getLeft();
        final List<DistributeQRCodeRecord> qrCodeRecords = myDealerDetail.getMiddle();
        final List<DealerChannelRate> channelRates = myDealerDetail.getRight();
        myDealerDetailResponse.setDealerId(dealer.getId());
        myDealerDetailResponse.setMobile(dealer.getMobile());
        myDealerDetailResponse.setProxyName(dealer.getProxyName());
        Preconditions.checkState(!CollectionUtils.isEmpty(channelRates), "代理商费率不存在");
        for (DealerChannelRate channelRate : channelRates) {
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WECHAT_PUBLIC.getId()) {
                myDealerDetailResponse.setWeixinSettleRate(channelRate.getDealerTradeRate().toPlainString());
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ALIPAY_PUBLIC.getId()) {
                myDealerDetailResponse.setAlipaySettleRate(channelRate.getDealerTradeRate().toPlainString());
                myDealerDetailResponse.setWithdrawSettleFee(channelRate.getDealerWithdrawFee().toPlainString());
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
                myDealerDetailResponse.setQuickSettleRate(channelRate.getDealerTradeRate().toPlainString());
            }
        }
        final List<MyDealerDetailResponse.QRCodeRecord> records = Lists.transform(qrCodeRecords, new Function<DistributeQRCodeRecord, MyDealerDetailResponse.QRCodeRecord>() {
            @Override
            public MyDealerDetailResponse.QRCodeRecord apply(DistributeQRCodeRecord input) {
                final MyDealerDetailResponse.QRCodeRecord qrCodeRecord = myDealerDetailResponse.new QRCodeRecord();
                qrCodeRecord.setCount(input.getCount());
                qrCodeRecord.setStartCode(input.getStartCode());
                qrCodeRecord.setEndCode(input.getEndCode());
                qrCodeRecord.setDistributeDate(DateFormatUtil.format(input.getCreateTime(), DateFormatUtil.yyyy_MM_dd));
                return qrCodeRecord;
            }
        });
        myDealerDetailResponse.setCodeRecords(records);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", myDealerDetailResponse);
    }

    /**
     *获取一级代理商的代理通道费率
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/channel/list", method = RequestMethod.POST)
    public CommonResponse channelList() {
        final List<Product> products = this.productService.selectAll();
        List<DealerChannelRate> list = this.dealerChannelRateService.selectByDealerIdAndProductId(this.getDealerId(), products.get(0).getId());
        return CommonResponse.objectResponse(1, "success", list);
    }
}
