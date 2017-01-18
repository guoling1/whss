package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 扫码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    public String scanCode(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "openId", required = false) String openId) {
        boolean isRedirect = true;
        final String code = request.getParameter("code");
        final String sign = request.getParameter("sign");
        if ((Long.valueOf(code) >= Long.valueOf("100010063208")) && (Long.valueOf(code) <= Long.valueOf("100010068207"))) {
            return "redirect:http://hsy.qianbaojiajia.com/code/scanCode?" + "code" + "=" + code + "&" + "sign" + "=" + sign;
        }
        log.info("scan code[{}], sign is [{}]", code, sign);
        final Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(code);
        Preconditions.checkState(qrCodeOptional.isPresent(), "二维码不存在");
        Preconditions.checkState((qrCodeOptional.get().getSysType()).equals(EnumQRCodeSysType.HSY.getId()), "二维码不属于该系统");
        final QRCode qrCode = qrCodeOptional.get();
        Preconditions.checkState(qrCode.isCorrectSign(sign), "sign is not correct");
        final long merchantId = qrCode.getMerchantId();
        final String agent = request.getHeader("User-Agent");
        String url = "";
        if (qrCode.isActivate()) {//已激活
            log.info("code[{}] is activate", code);
            List<AppBizShop> appBizShops = hsyShopDao.findAppBizShopByID(merchantId);
            Preconditions.checkState(appBizShops!=null&&appBizShops.size()>0, "商户不存在");
            String merchantName = hsyShopDao.findShopNameByID(merchantId);
            model.addAttribute("merchantId", merchantId);
            model.addAttribute("name", merchantName);
            log.info("设备标示{}",agent.indexOf("MicroMessenger"));
            if (agent.indexOf("MicroMessenger") > -1) {
                url = "/sqb/paymentWx";
            }
            if (agent.indexOf("AliApp") > -1) {
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


//    /**
//     * 扫码
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
//    public String scanCode(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "openId", required = false) String openId) {
//        //如何没有openId跳授权页面
//        if(openId==null||"".equals(openId)){
//            String requestUrl = "";
//            if(request.getQueryString() == null){
//                requestUrl = "";
//            }else{
//                requestUrl = request.getQueryString();
//            }
//            try {
//                String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
//                return "redirect:"+ WxConstants.WEIXIN_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        }
//        final String code = request.getParameter("code");
//        final String sign = request.getParameter("sign");
//        if ((Long.valueOf(code) >= Long.valueOf("100010063208")) && (Long.valueOf(code) <= Long.valueOf("100010068207"))) {
//            return "redirect:http://"+ ApplicationConsts.getApplicationConfig().domain()+"/code/scanCode?" + "code" + "=" + code + "&" + "sign" + "=" + sign;
//        }
//        log.info("scan code[{}], sign is [{}]", code, sign);
//        final Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(code);
//        Preconditions.checkState(qrCodeOptional.isPresent(), "QRCode not exist");
//        final QRCode qrCode = qrCodeOptional.get();
//        //未分配，也可以扫码
////        Preconditions.checkState(qrCode.isDistribute(), "QRCode not distribute");
//        Preconditions.checkState(qrCode.isCorrectSign(sign), "sign is not correct");
//        final long merchantId = qrCode.getMerchantId();
//        final String agent = request.getHeader("User-Agent");
//        String url = "";
//        if (qrCode.isActivate()) {//已激活
//            Optional<UserInfo> userInfoOptional = userInfoService.selectByMerchantId(merchantId);
//            Preconditions.checkState(userInfoOptional.isPresent(), "userInfo is not exist");
//            String openIdTemp = userInfoOptional.get().getOpenId();
//            if(openId.equals(openIdTemp)){
//                log.info("code[{}] is activate", code);
//                final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(merchantId);
//                Preconditions.checkState(merchantInfoOptional.isPresent(), "merchant is not exist");
//                final MerchantInfo merchantInfo = merchantInfoOptional.get();
//                model.addAttribute("merchantId", merchantId);
//                if (EnumMerchantStatus.INIT.getId() == merchantInfo.getStatus()) { // 初始化-->填资料
//                    log.info("code[{}], merchant is in init; go to fill profile", code);
//                    url = "/sqb/addInfo";
//                } else if (EnumMerchantStatus.ONESTEP.getId() == merchantInfo.getStatus()) { // 初始化-->图片
//                    log.info("code[{}], merchant is in one step; go to fill pic", code);
//                    url = "/sqb/addNext";
//                } else if (EnumMerchantStatus.REVIEW.getId() == merchantInfo.getStatus()) {//待审核
//                    log.info("code[{}], merchant is in due audit", code);
//                    url = "/sqb/prompt";
//                } else if (EnumMerchantStatus.UNPASSED.getId() == merchantInfo.getStatus()) {//审核未通过
//                    log.info("code[{}], merchant is not pass", code);
//                    url = "/sqb/prompt";
//                } else if (EnumMerchantStatus.PASSED.getId() == merchantInfo.getStatus()) {//审核通过
//                    model.addAttribute("name", merchantInfo.getMerchantName());
//                    if (agent.indexOf("MicroMessenger") > -1) {//weixin
//                        url = "/sqb/paymentWx";
//                    }
//                    if (agent.indexOf("AliApp") > -1) {// AliApp
//                        url = "/sqb/paymentZfb";
//                    }
//                }else if (EnumMerchantStatus.LOGIN.getId() == merchantInfo.getStatus()) {//注册
//                    model.addAttribute("code", code);
//                    url =  "/sqb/reg";
//                } else {
//                    log.error("code[{}] is activate, but merchant[{}] is disabled", code, merchantId);
//                }
//            }else{
//                log.info("code[{}] is activate", code);
//                final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(merchantId);
//                Preconditions.checkState(merchantInfoOptional.isPresent(), "merchant is not exist");
//                final MerchantInfo merchantInfo = merchantInfoOptional.get();
//                model.addAttribute("merchantId", merchantId);
//                if (EnumMerchantStatus.PASSED.getId() == merchantInfo.getStatus()) {//审核通过
//                    model.addAttribute("name", merchantInfo.getMerchantName());
//                    if (agent.indexOf("MicroMessenger") > -1) {//weixin
//                        url = "/sqb/paymentWx";
//                    }
//                    if (agent.indexOf("AliApp") > -1) {// AliApp
//                        url = "/sqb/paymentZfb";
//                    }
//                }else{
//                    log.info("code[{}], merchant is not pass", code);
//                    url = "/sqb/unFinishedPrompt";
//                }
//            }
//        } else {//注册
//            Preconditions.checkState(agent.indexOf("MicroMessenger") > -1, "please register in weixin");
//            model.addAttribute("code", code);
//            url =  "/sqb/reg";
//        }
//        return "redirect:"+url;
//    }
}
