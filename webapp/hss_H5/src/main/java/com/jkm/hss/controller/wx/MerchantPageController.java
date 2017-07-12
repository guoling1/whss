package com.jkm.hss.controller.wx;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.service.RequestUrlParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by xingliujie on 2017/6/21.
 */
@Slf4j
@Controller
@RequestMapping(value = "/page")
public class MerchantPageController extends BaseController {
    @Autowired
    private RequestUrlParamService requestUrlParamService;
    @Autowired
    private OemInfoService oemInfoService;
    /**
     * 分公司注册微信跳转
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toSkip", method = RequestMethod.GET)
    public String  toSkip(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String[] arr = request.getQueryString().split("&");
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
        String url = requestUrlParamService.getRequestUrlById(Long.parseLong(state));
        Preconditions.checkState(url!=null&&!"".equals(url), "微信授权失败");
        String[] temArr = url.split("[?]");
        String oemNo = "";
        if(temArr.length>1){
            String[] temArr1 = temArr[1].split("&");
            for(int i =0;i<temArr1.length;i++){
                if("oemNo".equals(temArr1[i].split("=")[0])){
                    String[] temArr2 = temArr1[i].split("=");
                    if(temArr2.length==2){
                        oemNo = temArr1[i].split("=")[1];
                    }
                }
            }
        }
        Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
        Map<String,String> ret = null;
        if(oemInfoOptional.isPresent()){
            ret = WxPubUtil.getOpenid(code,oemInfoOptional.get().getAppId(),oemInfoOptional.get().getAppSecret());
        }else{
            ret = WxPubUtil.getOpenid(code, WxConstants.APP_ID,WxConstants.APP_KEY);
        }
        Preconditions.checkState(ret!=null, "微信授权失败");
        CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
                ApplicationConsts.getApplicationConfig().domain());
        System.out.println("cookie="+CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY));
        return "redirect:"+url;
    }
}
