package com.jkm.hss.controller.code;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.hss.account.entity.MemberAccount;
import com.jkm.hss.account.sevice.MemberAccountService;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.JKMTradeServiceException;
import com.jkm.hss.helper.JkmApiErrorCode;
import com.jkm.hss.helper.request.CreateApiOrderRequest;
import com.jkm.hss.helper.response.CreateApiOrderResponse;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hsy.user.constant.RechargeValidType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppPolicyMember;
import com.jkm.hsy.user.entity.AppPolicyMembershipCardShop;
import com.jkm.hsy.user.service.HsyMembershipService;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.immutables.value.internal.$processor$.meta.$TreesMirrors;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2017/1/18.
 */
@Slf4j
@Controller
@RequestMapping(value = "/sqb")
public class WebSkipController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AlipayOauthService alipayOauthService;
    @Autowired
    private UserChannelPolicyService userChannelPolicyService;
    @Autowired
    private HsyMembershipService hsyMembershipService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HSYTransactionService hsyTransactionService;
    @Autowired
    private HSYOrderService hsyOrderService;
    /**
     * 支付升级成功页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/success/{amount}/{orderId}", method = RequestMethod.GET)
    public String buySuccess(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") long orderId) throws IOException {
        model.addAttribute("money", amount);
        final Order order = this.orderService.getById(orderId).get();
        model.addAttribute("firstSn", order.getOrderNo().substring(0, order.getOrderNo().length() - 6));
        model.addAttribute("secondSn", order.getOrderNo().substring(order.getOrderNo().length() - 6, order.getOrderNo().length()));
        return "/success";
    }

    /**
     * 扫固定码微信支付页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentWx", method = RequestMethod.GET)
    public String paymentWx(final HttpServletRequest request, final HttpServletResponse response, final Model model, @RequestParam(value = "hsyOrderId", required = true) long hsyOrderId ,@RequestParam(value = "openId") String openId,@RequestParam(value = "userId") Long userId,@RequestParam(value = "merchantId") Long merchantId) throws IOException {
        model.addAttribute("hsyOrderId", hsyOrderId);
        String merchantName = hsyShopDao.findShopNameByID(merchantId);
        model.addAttribute("merchantName", merchantName);
        AppPolicyMember appPolicyMember = hsyMembershipService.findAppPolicyMember(openId,null,userId);
        if(appPolicyMember!=null){
            Optional<MemberAccount> account=memberAccountService.getById(appPolicyMember.getAccountID());
            appPolicyMember.setRemainingSum(account.get().getAvailable());
            List<AppPolicyMembershipCardShop> cardShopList=hsyMembershipService.getMembershipCardShop(appPolicyMember.getMcid());
            boolean viewFlag=false;
            for(AppPolicyMembershipCardShop appPolicyMembershipCardShop:cardShopList){
                if(appPolicyMembershipCardShop.getSid().equals(merchantId))
                {
                    viewFlag=true;
                    break;
                }
            }
            if(viewFlag)
                model.addAttribute("appPolicyMember",appPolicyMember);
        }
        return "/payment-wx";
    }

    /**
     * 扫固定码支付宝支付页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentZfb", method = RequestMethod.GET)
    public String paymentZfb(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "hsyOrderId", required = true) long hsyOrderId,@RequestParam(value = "openId") String openId,@RequestParam(value = "userId") Long userId,@RequestParam(value = "merchantId") Long merchantId) throws IOException {
        model.addAttribute("hsyOrderId", hsyOrderId);
        String merchantName = hsyShopDao.findShopNameByID(merchantId);
        model.addAttribute("merchantName", merchantName);
        AppPolicyMember appPolicyMember = hsyMembershipService.findAppPolicyMember(null,openId,userId);
        if(appPolicyMember!=null){
            Optional<MemberAccount> account=memberAccountService.getById(appPolicyMember.getAccountID());
            appPolicyMember.setRemainingSum(account.get().getAvailable());
            List<AppPolicyMembershipCardShop> cardShopList=hsyMembershipService.getMembershipCardShop(appPolicyMember.getMcid());
            boolean viewFlag=false;
            for(AppPolicyMembershipCardShop appPolicyMembershipCardShop:cardShopList){
                if(appPolicyMembershipCardShop.getSid().equals(merchantId))
                {
                    viewFlag=true;
                    break;
                }
            }
            if(viewFlag)
                model.addAttribute("appPolicyMember",appPolicyMember);
        }
        return "/payment-zfb";
    }

    /**
     * 扫固定微信跳转页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toSkip", method = RequestMethod.GET)
    public String  toSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        Preconditions.checkState(request.getQueryString() != null, "微信授权失败");
        String getQueryString = request.getQueryString();
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        String appId = "";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        String redirectUrl = URLDecoder.decode(tempUrl,"UTF-8");
        String[] arr1 = redirectUrl.split("&");
        for(int j =0;j<arr1.length;j++){
            if("appId".equals(arr1[j].split("=")[0])){
                appId = arr1[j].split("=")[1];
            }
        }
        Preconditions.checkState(appId!=null&&!"".equals(appId), "微信授权失败");
        String appSecret =WxConstants.APP_HSY_SECRET;
        if(!(WxConstants.APP_HSY_ID).equals(appId)){
            appSecret = userChannelPolicyService.selectAppSecretByAppId(appId);
        }
        Preconditions.checkState(appSecret!=null&&!"".equals(appSecret), "微信授权失败");
        Map<String,String> ret = WxPubUtil.getOpenid(code, appId,appSecret);
        Preconditions.checkState(ret.get("openid")!=null&&!"".equals(ret.get("openid")), "微信授权失败");
        model.addAttribute("openId", ret.get("openid"));
        String finalRedirectUrl = "http://"+ ApplicationConsts.getApplicationConfig().domain()+"/code/scanCode?"+redirectUrl;
        return "redirect:"+finalRedirectUrl;
    }

    /**
     * 扫固定微信跳转页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toAlipaySkip", method = RequestMethod.GET)
    public String  toAlipaySkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        log.info("请求地址是：{}",request.getRequestURL());
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        log.info("请求参数是:{}",getQueryString);
        String[] arr = getQueryString.split("&");
        String appId="";
        String authcode="";
        String state = "";
        String code = "";
        String sign = "";
        for(int i =0;i<arr.length;i++){
            if("app_id".equals(arr[i].split("=")[0])){
                appId = arr[i].split("=")[1];
                log.info("appId是:{}",appId);
            }
            if("auth_code".equals(arr[i].split("=")[0])){
                authcode = arr[i].split("=")[1];
                log.info("authcode是:{}",authcode);
            }
            if("state".equals(arr[i].split("=")[0])){
                    code = arr[i].split("=")[2];
                    log.info("code参数是:{}",code);
            }

            if("sign".equals(arr[i].split("=")[0])){
                sign = arr[i].split("=")[1];
                log.info("sign参数是:{}",sign);
            }
        }
        String userId = alipayOauthService.getUserId(authcode);
        if(userId==null||"".equals(userId)){
            model.addAttribute("message", "支付宝授权失败");
            return "/message";
        }
        model.addAttribute("openId", userId);
        model.addAttribute("code", code);
        model.addAttribute("sign", sign);
        String finalRedirectUrl = "http://"+ ApplicationConsts.getApplicationConfig().domain()+"/code/scanCode";
        log.info("跳转地址是：{}",finalRedirectUrl);
        return "redirect:"+finalRedirectUrl;
    }

    @RequestMapping("needRecharge")
    public String needRecharge(HttpServletRequest request, HttpServletResponse response,Model model,Long mid,String cellphone,String source){
        model.addAttribute("mid",mid);
        model.addAttribute("cellphone",cellphone);
        model.addAttribute("source",source);
        return "/needRecharge";
    }

    @RequestMapping("toRecharge")
    public String toRecharge(HttpServletRequest request, HttpServletResponse response,Model model,Long mid,String source){
        AppPolicyMember appPolicyMember=hsyMembershipService.findMemberInfoByID(mid);
        Optional<MemberAccount> account=memberAccountService.getById(appPolicyMember.getAccountID());
        appPolicyMember.setRemainingSum(account.get().getAvailable());
        appPolicyMember.setRechargeTotalAmount(account.get().getRechargeTotalAmount());
        appPolicyMember.setConsumeTotalAmount(account.get().getConsumeTotalAmount());
        DecimalFormat a=new DecimalFormat("0.0");
        String discountStr=a.format(appPolicyMember.getDiscount());
        String discountInt=discountStr.split("\\.")[0];
        String discountFloat=discountStr.split("\\.")[1];
        model.addAttribute("appPolicyMember",appPolicyMember);
        model.addAttribute("type", RechargeValidType.RECHARGE.key);
        model.addAttribute("discountInt",discountInt);
        model.addAttribute("discountFloat",discountFloat);
        model.addAttribute("source", source);
        return "/toRecharge";
    }

    /**
     * 扫固定码微信支付页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/codeapi", method = RequestMethod.GET)
    public String wxapi(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "payInfo", required = true) long payInfo, @RequestParam(value = "hsyOrderId", required = true) long hsyOrderId ) throws IOException {
        final HsyOrder hsyOrder = this.hsyOrderService.getById(hsyOrderId).get();
        final int paymentChannel = hsyOrder.getPaymentChannel();
        //获取openID
        if (paymentChannel == EnumPaymentChannel.WECHAT_PAY.getId()){
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ WxConstants.APP_HSY_ID+"&redirect_uri=http%3a%2f%2fhsy.qianbaojiajia.com%2f/sqb%2fopenIdBack&response_type=code&scope=snsapi_base&state="+hsyOrderId+"#wechat_redirect";
        }else if (paymentChannel == EnumPaymentChannel.ALIPAY.getId()){
            return "redirect:https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+ AlipayServiceConstants.APP_ID+"&state="+hsyOrderId+"&scope=auth_base&redirect_uri=http%3a%2f%2fhsy.qianbaojiajia.com%2f/sqb%2fuserIdBack";
        }
        model.addAttribute("hsyOrderId", hsyOrderId);
        model.addAttribute("payInfo", payInfo);
        return "/payment-wx-api";
    }

    /**
     * openid
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/openIdBack", method = RequestMethod.GET)
    public String payOpenIdOrder(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws UnsupportedEncodingException {
        Long startTime = System.currentTimeMillis();
        log.info("微信回调获取OPENID");
        try{

            Preconditions.checkState(request.getQueryString() != null, "微信授权失败");
            String getQueryString = request.getQueryString();
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
            Map<String,String> ret = WxPubUtil.getOpenid(code, WxConstants.APP_HSY_ID,WxConstants.APP_HSY_SECRET);
            Preconditions.checkState(ret.get("openid")!=null&&!"".equals(ret.get("openid")), "微信授权失败");
            final HsyOrder hsyOrder = this.hsyOrderService.getById(Integer.valueOf(state)).get();
            hsyOrder.setMemberId(ret.get("openid"));
            this.hsyOrderService.update(hsyOrder);
            final Triple<Integer, String, String> resultPair = this.hsyTransactionService.placeOrder(hsyOrder.getAmount().toString(), hsyOrder.getId(),hsyOrder.getAmount(),null,null,null,null);
            if (0 == resultPair.getLeft()) {
                //("payUrl", URLDecoder.decode(resultPair.getMiddle(), "UTF-8"))
                //下单成功
                final Order order = this.orderService.getByBusinessOrderNo(hsyOrder.getOrdernumber()).get();
                final String payInfo = order.getPayInfo();
                final String[] split = payInfo.split("|");
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("appId", split[0]);
                jsonObject.put("timeStamp", split[1]);
                jsonObject.put("nonceStr", split[2]);
                jsonObject.put("package", split[3]);
                jsonObject.put("signType", split[4]);
                jsonObject.put("paySign", split[5]);
                model.addAttribute("payUrl",jsonObject.toJSONString());
                return "/api-wx";
            }else{
                //下单失败
            }

        } catch (JKMTradeServiceException e) {
            log.error("#【微信回调获取OPENID并下单】controller.payOpenIdOrder.JKMTradeServiceException", e);

        } catch (Exception e) {
            log.error("#【微信回调获取OPENID并下单】controller.payOpenIdOrder.Exception", e);

        }
        //结果返回
        //createApiOrderResponse = afterComplete();
        Long endTime = System.currentTimeMillis();
        log.info("#【微信回调获取OPENID并下单】merchantOrderNo:"  + ",endTime:" + endTime + ",totalTime:" + (endTime - startTime) + "ms");
        return "";
    }

}
