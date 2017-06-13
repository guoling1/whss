package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.Enum.EnumHxbsOpenProductStatus;
import com.jkm.hsy.user.Enum.EnumHxbsStatus;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import com.jkm.hsy.user.service.UserCurrentChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 微信扫码入口
 */
@Slf4j
@Controller
@RequestMapping(value = "/code")
public class CodeController extends BaseController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private HsyShopDao hsyShopDao;

    @Autowired
    private HSYTransactionService hsyTransactionService;

    @Autowired
    private UserCurrentChannelPolicyService userCurrentChannelPolicyService;


    /**
     * 扫码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    public String scanCode(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "openId", required = false) String openId) throws UnsupportedEncodingException {
        boolean isRedirect = true;
        final String code = request.getParameter("code");
        final String sign = request.getParameter("sign");
        if ((Long.valueOf(code) >= Long.valueOf("100010063208")) && (Long.valueOf(code) <= Long.valueOf("100010068207"))) {
            return "redirect:http://hss.qianbaojiajia.com/code/scanCode?" + "code" + "=" + code + "&" + "sign" + "=" + sign;
        }
        log.info("scan code[{}], sign is [{}]", code, sign);
        final Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(code);
        Preconditions.checkState(qrCodeOptional.isPresent(), "二维码不存在");
        Preconditions.checkState((qrCodeOptional.get().getSysType()).equals(EnumQRCodeSysType.HSY.getId()), "二维码不属于该系统");
        final QRCode qrCode = qrCodeOptional.get();
        Preconditions.checkState(qrCode.isCorrectSign(sign), "sign is not correct");
        final long merchantId = qrCode.getMerchantId();
        final String agent = request.getHeader("User-Agent").toLowerCase();
        log.info("User-Agent is [{}]",agent);
        String url = "";
        if (qrCode.isActivate()) {//已激活
            log.info("code[{}] is activate", code);
            List<AppBizShop> appBizShops = hsyShopDao.findAppBizShopByID(merchantId);
            Preconditions.checkState(appBizShops!=null&&appBizShops.size()>0, "商户不存在");
            Preconditions.checkState(appBizShops.get(0).getStatus()!=null, "商户未通过审核");
            Preconditions.checkState(appBizShops.get(0).getStatus()==AppConstant.SHOP_STATUS_NORMAL, "商户未通过审核");
            List<AppAuUser> appAuUsers = hsyShopDao.findCorporateUserByShopID(merchantId);
            Preconditions.checkState(appAuUsers!=null&&appAuUsers.size()>0, "商户不存在");
            Preconditions.checkState(appAuUsers.get(0).getHxbStatus()!=null, "商户未通过审核");
            Preconditions.checkState(appAuUsers.get(0).getHxbOpenProduct()!=null, "商户未通过审核");
            Preconditions.checkState(appAuUsers.get(0).getHxbStatus()== EnumHxbsStatus.PASS.getId(), "该商户收款功能暂未开通，请使用其他方式向商户付款");
            Preconditions.checkState(appAuUsers.get(0).getHxbOpenProduct()== EnumHxbsOpenProductStatus.PASS.getId(), "该商户收款功能暂未开通，请使用其他方式向商户付款");
            String merchantName = hsyShopDao.findShopNameByID(merchantId);
            model.addAttribute("name", merchantName);
            log.info("设备标示{}",agent.indexOf("micromessenger"));
            Optional<UserCurrentChannelPolicy> userCurrentChannelPolicyOptional = userCurrentChannelPolicyService.selectByUserId(appAuUsers.get(0).getId());
            Preconditions.checkState(userCurrentChannelPolicyOptional.isPresent(), "商户使用中通道未设置");
            if (agent.indexOf("micromessenger") > -1) {
                if(openId==null||"".equals(openId)){
                    String requestUrl = "";
                    if(request.getQueryString() == null){
                        requestUrl = "";
                    }else{
                        requestUrl = request.getQueryString();
                    }
                    String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                    return "redirect:"+ WxConstants.WEIXIN_HSY_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                }

                long orderId = hsyTransactionService.createOrder(userCurrentChannelPolicyOptional.get().getWechatChannelTypeSign(),merchantId,openId,code);
                model.addAttribute("orderId",orderId);
                url = "/sqb/paymentWx";
            }
            if (agent.indexOf("aliapp") > -1) {
                log.info("进入支付宝");
                log.info("alipay的openId={}",openId);
                if(openId==null||"".equals(openId)){
                    String requestUrl = "";
                    if(request.getQueryString() == null){
                        requestUrl = "";
                    }else{
                        requestUrl = request.getQueryString();
                    }
                    log.info("请求地址是:{}",requestUrl);
                    String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                    log.info("加密之后的地址是:{}",encoderUrl);
                    log.info("加密之后的请求地址是:{}",AlipayServiceConstants.OAUTH_URL+encoderUrl);
                    return "redirect:"+ AlipayServiceConstants.OAUTH_URL+encoderUrl+AlipayServiceConstants.OAUTH_URL_AFTER;
                }
                long orderId = hsyTransactionService.createOrder(userCurrentChannelPolicyOptional.get().getWechatChannelTypeSign(),merchantId,openId,code);
                model.addAttribute("orderId",orderId);
                url = "/sqb/paymentZfb";
            }
        } else {
            log.info("code[{}] is not activate", code);
            isRedirect = false;
            model.addAttribute("message", "该二维码未激活");
            url = "/message";
        }
        if(isRedirect){
            return "redirect:"+url;
        }else{
            return url;
        }
    }
}
