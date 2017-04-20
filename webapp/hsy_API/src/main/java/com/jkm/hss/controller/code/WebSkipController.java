package com.jkm.hss.controller.code;

import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import lombok.extern.slf4j.Slf4j;
import org.immutables.value.internal.$processor$.meta.$TreesMirrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
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
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentWx", method = RequestMethod.GET)
    public String paymentWx(final HttpServletRequest request, final HttpServletResponse response, final Model model, @RequestParam(value = "merchantId", required = true) long merchantId, @RequestParam(value = "name") String name, @RequestParam(value = "openId") String openId) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        model.addAttribute("openId", openId);
        return "/payment-wx";
    }

    /**
     * 扫固定码支付宝支付页面
     * @param request
     * @param response
     * @param model
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentZfb", method = RequestMethod.GET)
    public String paymentZfb(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "merchantId", required = true) long merchantId,@RequestParam(value = "name") String name,@RequestParam(value = "openId") String openId) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        model.addAttribute("openId", openId);
        log.info("openId={}",openId);
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
        Map<String,String> ret = WxPubUtil.getOpenid(code, WxConstants.APP_HSY_ID,WxConstants.APP_HSY_SECRET);
        model.addAttribute("openId", ret.get("openid"));
        log.info("openid是：{}",ret.get("openid"));
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        String redirectUrl = URLDecoder.decode(tempUrl,"UTF-8");
        String finalRedirectUrl = "http://"+ ApplicationConsts.getApplicationConfig().domain()+"/code/scanCode?"+redirectUrl;
        log.info("跳转地址是：{}",finalRedirectUrl);
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
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
                log.info("code参数是:{}",code);
            }
            if("sign".equals(arr[i].split("=")[0])){
                sign = state.split("=")[1];
                log.info("sign参数是:{}",code);
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

}
