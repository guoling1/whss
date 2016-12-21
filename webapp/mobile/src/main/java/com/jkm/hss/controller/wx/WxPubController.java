package com.jkm.hss.controller.wx;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.MerchantLoginCodeRequest;
import com.jkm.hss.helper.request.MerchantLoginRequest;
import com.jkm.hss.helper.request.OtherPayRequest;
import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.helper.request.RequestOrderRecord;
import com.jkm.hss.merchant.helper.request.TradeRequest;
import com.jkm.hss.merchant.helper.request.WithDrawRequest;
import com.jkm.hss.merchant.service.AccountInfoService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.OrderRecordService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-23 14:17
 */
@Slf4j
@Controller
@RequestMapping(value = "/wx")
public class WxPubController extends BaseController {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;




    /**
     * 登录页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toSkip", method = RequestMethod.GET)
    public String  toSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }

        Map<String,String> ret = WxPubUtil.getOpenid(code);
        CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
                ApplicationConsts.getApplicationConfig().domain());
//        String url ="";
//        if(ret!=null&&ret.size()>0) {
//            Optional<UserInfo> userInfo = userInfoService.selectByOpenId(ret.get("openid"));
//            if (userInfo.isPresent()) {//存在
//                CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
//                        ApplicationConsts.getApplicationConfig().domain());
//                url = state;
//            } else {
//                UserInfo ui = new UserInfo();
//                ui.setOpenId(ret.get("openid"));
//                ui.setStatus(0);
//                userInfoService.insertUserInfo(ui);
//                CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
//                        ApplicationConsts.getApplicationConfig().domain());
////                url="/sqb/reg";
//                url = state;
//            }
//        }
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        return "redirect:"+tempUrl;
    }

    /**
     * 火车票页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toTicketSkip", method = RequestMethod.GET)
    public String  toTicketSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        Map<String,String> ret = WxPubUtil.getOpenid(code);
        return "redirect:http://hcp.jinkaimen.com/ticket/main-menu/reserve?appid=1012&uid="+ ret.get("openid");
    }


    /**
     * 扫固定码获取openId(获取openId)
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toMerchantSkip", method = RequestMethod.GET)
    public String  toMerchantSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        Map<String,String> ret = WxPubUtil.getOpenid(code);
        model.addAttribute("openId", ret.get("openid"));
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        String redirectUrl = URLDecoder.decode(tempUrl,"UTF-8");
        String finalRedirectUrl = "http://hss.qianbaojiajia.com/code/scanCode?"+redirectUrl;
        return "redirect:"+finalRedirectUrl;
    }

    /**
     * 商户登录发送验证码
     * @param codeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCode", method = RequestMethod.POST)
    public CommonResponse getCode(@RequestBody MerchantLoginCodeRequest codeRequest) {
        final String mobile = codeRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.REGISTER_MERCHANT);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(mobile)
                    .uid("")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.REGISTER_MERCHANT)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * 发送提现验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getWithDrawCode", method = RequestMethod.POST)
    public CommonResponse getWithDrawCode(final HttpServletRequest request, final HttpServletResponse response) {
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录异常，请重新登录");
        }
        Optional<MerchantInfo>  merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录异常，请重新登录");
        }
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()), EnumVerificationCodeType.WITH_DRAW);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()))
                    .uid("")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.WITHDRAW_CODE)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * 获取商户银行卡信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "geBankInfo", method = RequestMethod.POST)
    public CommonResponse geBankInfo(final HttpServletRequest request, final HttpServletResponse response) {
        //之所以能调到此页面，说明状态已经改过，在前面已经拦截
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-1, "商户信息异常，请重新登录");
        }
        Map map = new HashMap();
        map.put("bankBin",merchantInfo.get().getBankBin());
        map.put("bankNo",merchantInfo.get().getBankNo());
        map.put("bankName",merchantInfo.get().getBankName());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", map);
    }



    /**
     * 交易记录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getOrderRecord", method = RequestMethod.POST)
    public CommonResponse getOrderRecord(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final RequestOrderRecord req) {
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
        req.setMerchantId(merchantInfo.get().getId());
        final PageModel<OrderRecord> pageModel = new PageModel<>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        List<OrderRecord> orderList =  orderRecordService.selectAllOrderRecordByPage(req);
        long count = orderRecordService.selectAllOrderRecordCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 余额明细
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getChargeRecord", method = RequestMethod.POST)
    public CommonResponse getChargeRecord(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final RequestOrderRecord req) {
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        req.setMerchantId(merchantInfo.get().getId());
        final PageModel<OrderRecord> pageModel = new PageModel<>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        List<OrderRecord> orderList =  orderRecordService.selectBalanceByPage(req);
        long count = orderRecordService.selectBalanceCount(req.getMerchantId());
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public CommonResponse login(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final MerchantLoginRequest loginRequest) {
        log.info("参数为{}",JSONObject.fromObject(loginRequest).toString());
        final String mobile = loginRequest.getMobile();
        final String verifyCode = loginRequest.getCode();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }

        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.REGISTER_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        log.info("OpenId是{}",super.getOpenId(request));

        MerchantInfo mi = new MerchantInfo();
        mi.setStatus(EnumMerchantStatus.INIT.getId());
        mi.setMobile(MerchantSupport.encryptMobile(mobile));
        mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
        if(loginRequest.getQrCode()!=null&&!"".equals(loginRequest.getQrCode())){
            log.info("扫码注册");
            mi.setCode(loginRequest.getQrCode());
            merchantInfoService.regByCode(mi);
            Optional<UserInfo> ui = userInfoService.selectByOpenId(super.getOpenId(request));
            if(ui.isPresent()){//存在
                userInfoService.uploadUserInfo(mi.getId(),MerchantSupport.encryptMobile(mobile),ui.get().getId());
            }else{//不存在，新增，添加cookie
                UserInfo uo = new UserInfo();
                uo.setOpenId(super.getOpenId(request));
                uo.setStatus(0);
                uo.setMobile(MerchantSupport.encryptMobile(mobile));
                uo.setMerchantId(mi.getId());
                userInfoService.insertUserInfo(uo);
            }
        }else{
            log.info("普通注册");
            merchantInfoService.regByWxPub(mi);
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if(userInfoOptional.isPresent()){//存在
                userInfoService.uploadUserInfo(mi.getId(),MerchantSupport.encryptMobile(mobile),userInfoOptional.get().getId());
            }else{//不存在，新增，添加cookie
                UserInfo uo = new UserInfo();
                uo.setOpenId(super.getOpenId(request));
                uo.setStatus(0);
                uo.setMobile(MerchantSupport.encryptMobile(mobile));
                uo.setMerchantId(mi.getId());
                userInfoService.insertUserInfo(uo);
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "登录成功",mi.getId());

    }

    /**
     * 收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receipt", method = RequestMethod.POST)
    public CommonResponse receipt(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final TradeRequest tradeRequest) {
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final String totalFee = tradeRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        int compareResult = (new BigDecimal(totalFee)).compareTo(new BigDecimal("5"));
        if(compareResult<0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }
        tradeRequest.setMerchantId(merchantInfo.get().getId());
        tradeRequest.setSubMerName(merchantInfo.get().getMerchantName());
        tradeRequest.setSubMerNo(merchantInfo.get().getId()+"");
        JSONObject jo = orderRecordService.PayOrder(tradeRequest);
        if(jo.getInt("code")==1){
            return CommonResponse.objectResponse(1,"收款成功",jo.getJSONObject("data"));
        }else{
            return CommonResponse.simpleResponse(-1, jo.getString("message"));
        }
    }
    /**
     * 扫固定码收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receiptByCode", method = RequestMethod.POST)
    public CommonResponse receiptByCode(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final TradeRequest tradeRequest) {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(tradeRequest.getMerchantId());
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }
        final String totalFee = tradeRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        int compareResult = (new BigDecimal(totalFee)).compareTo(new BigDecimal("5"));
        if(compareResult<0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }
        tradeRequest.setSubMerName(merchantInfo.get().getMerchantName());
        tradeRequest.setSubMerNo(merchantInfo.get().getId()+"");
        tradeRequest.setPayChannel(EnumPayChannelSign.YG_WEIXIN.getId());
        JSONObject jo = orderRecordService.PayOrder(tradeRequest);
        if(jo.getInt("code")==1){
            return CommonResponse.objectResponse(1,"收款成功",jo.getJSONObject("data"));
        }else{
            return CommonResponse.simpleResponse(-1, jo.getString("message"));
        }
    }

    /**
     * 提现
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final OtherPayRequest otherPayRequest) {
        final String verifyCode = otherPayRequest.getCode();
        if(!super.isLogin(request)){
            log.info("提现cookie找不到");
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            log.info("提现用户找不到");
            return CommonResponse.simpleResponse(-2, "登录异常，请重新登录");
        }
        Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            log.info("提现商户找不到");
            return CommonResponse.simpleResponse(-2, "登录异常，请重新登录");
        }
        if(merchantInfo.get().getStatus()!=3){
            log.info("注册信息不完善");
            return CommonResponse.simpleResponse(-2, "商户信息不完善");
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()), verifyCode, EnumVerificationCodeType.WITH_DRAW);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        JSONObject jo = orderRecordService.otherPay(merchantInfo.get());
        if(jo.getInt("code")==1){
            return CommonResponse.simpleResponse(1,"受理成功");
        }else{
            return CommonResponse.simpleResponse(-1, "提现失败");
        }
    }

    /**
     * 通过审核
     * @param request
     * @param response
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkWithdraw", method = RequestMethod.POST)
    public CommonResponse checkWithdraw(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final WithDrawRequest req) {
        JSONObject jo = orderRecordService.checkWithdraw(req);
        return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("message"));
    }

    /**
     * 提现查询
     *
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "checkOtherPay", method = RequestMethod.POST)
//    public CommonResponse checkOtherPay(final HttpServletRequest request, final HttpServletResponse response) {
//        if(!super.isLogin(request)){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
//        if(!userInfoOptional.isPresent()){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
//        if(!merchantInfo.isPresent()){
//            return CommonResponse.simpleResponse(-2, "未登录");
//        }
//        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()){
//            return CommonResponse.simpleResponse(-2, "未审核通过");
//        }
//        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(merchantInfo.get().getAccountId());
//        if(accountInfo==null){
//            return CommonResponse.simpleResponse(-1, "账户信息有误");
//        }
//        Pair<BigDecimal, BigDecimal> pair = shallProfitDetailService.withdrawParams(merchantInfo.get().getId());
//        int compareResult = accountInfo.getAvailable().compareTo(pair.getLeft());
//        if(compareResult!=1){
//            return CommonResponse.simpleResponse(-1, "提现金额至少"+pair.getLeft()+"元");
//        }
//        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "查询成功");
//    }

    /**
     * 支付回调
     * @param request
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "payResult", method = RequestMethod.POST)
    public void payResult(final HttpServletRequest request,final HttpServletResponse response) throws IOException {
        String outTradeNo = request.getParameter("outTradeNo");
        String tradeType = request.getParameter("tradeType");
        String tradeResult = request.getParameter("tradeResult");
        String returnMsg = request.getParameter("returnMsg");
        String payNum = request.getParameter("orderId");
        Map map = new HashMap();
        map.put("outTradeNo",outTradeNo);
        map.put("tradeType",tradeType);
        map.put("tradeResult",tradeResult);
        map.put("returnMsg",returnMsg);
        map.put("orderId",payNum);
        orderRecordService.payResult(map);
        response.getWriter().write("SUCCESS");
    }



}
