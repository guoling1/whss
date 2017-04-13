package com.jkm.hss.controller.website;


import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.WebsiteRequest;
import com.jkm.hss.merchant.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Controller
@RequestMapping(value = "/website")
public class WebsiteController extends BaseController {

    @Autowired
    private WebsiteService websiteService;

    /**
     * 网站
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/websiteSave", method = RequestMethod.OPTIONS)
    public void websiteSave() {

    }

    @ResponseBody
    @RequestMapping(value = "/websiteSave",method = RequestMethod.POST)
    public CommonResponse websiteSave(@RequestBody WebsiteRequest req, HttpServletRequest request, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        String ipAddress = getIpAddress(request);
        req.setIp(ipAddress);
        int response = this.websiteService.selectWebsite(req);
        if (response!=0) {
            if (response > 20) {
                return CommonResponse.simpleResponse(-1, "您已多次使用该ip提交");
            }
            this.websiteService.insertWebsite(req);
            return CommonResponse.simpleResponse(1, "提交成功");
        }
        this.websiteService.insertWebsite(req);
        return CommonResponse.simpleResponse(1, "提交成功");

    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
