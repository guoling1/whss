package com.jkm.hss.controller.website;

import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CreateImageCodeUtil;
import com.jkm.hss.admin.helper.requestparam.AppBizDistrictRequest;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.merchant.entity.Join;
import com.jkm.hss.merchant.service.WebsiteService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/4/7.
 */
@Controller
@RequestMapping(value = "/join")
public class JoinController {
    @Autowired
    private AppBizDistrictService appBizDistrictService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private SmsAuthService smsAuthService;

    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 查询所有省
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/selectProvinces", method = RequestMethod.OPTIONS)
    public void selectProvinces() {

    }

    /**
     * 查询所有省
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectProvinces", method = RequestMethod.POST)
    public CommonResponse selectProvinces(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findAllProvinces();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询省对应下的市
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectCities", method = RequestMethod.OPTIONS)
    public void selectCities() {

    }

    /**
     * 查询省对应下的市
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectCities", method = RequestMethod.POST)
    public CommonResponse selectCities(@RequestBody final AppBizDistrictRequest appBizDistrictRequest,HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        if(StringUtils.isEmpty(appBizDistrictRequest.getCode())){
            return CommonResponse.simpleResponse(-1, "请选择省份");
        }
        if("110000,120000,310000,500000".contains(appBizDistrictRequest.getCode())){
            List<AppBizDistrictResponse> appBizDistrictResponseList = appBizDistrictService.findByCode(appBizDistrictRequest.getCode());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",appBizDistrictResponseList);
        }
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findCityByProvinceCode(appBizDistrictRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询所有地区
     *
     */
    @ResponseBody
    @RequestMapping(value = "/selectDistrict", method = RequestMethod.OPTIONS)
    public void selectDistrict() {

    }

    /**
     * 查询所有地区
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectDistrict", method = RequestMethod.POST)
    public CommonResponse selectDistrict(@RequestBody final AppBizDistrictRequest appBizDistrictRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findCityByProvinceCode(appBizDistrictRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 保存
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.OPTIONS)
    public void save() {

    }

    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResponse save(@RequestBody HttpServletResponse httpServletResponse,Join join){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        String userName = join.getUserName();
        String mobile = join.getMobile();
        String companyName = join.getCompanyName();
        String provinceCode = join.getProvinceCode();
        String provinceName = join.getProvinceName();
        String cityCode = join.getCityCode();
        String cityName = join.getCityName();
        String countyCode = join.getCountyCode();
        String countyName = join.getCountyName();
        String type = join.getType();
        String mobileNo = websiteService.selectMobile(mobile);
        if (mobile.equals(mobileNo)){
            return CommonResponse.simpleResponse(-1,"改手机号已注册！");
        }

        this.websiteService.saveInfo(userName,mobile,companyName,provinceCode,provinceName,cityCode,cityName,countyCode,countyName,type);
        return CommonResponse.simpleResponse(1,"提交成功！");
    }

    /**
     * 获取手机验证码
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/phoneNo", method = RequestMethod.OPTIONS)
    public void phoneNo() {

    }

    /**
     * 获取手机验证码
     *
     */
    @ResponseBody
    @RequestMapping(value = "/phoneNo",method = RequestMethod.POST)
    public CommonResponse phoneNo(@RequestBody Join join, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        if (("").equals(join.getMobile())){
            return CommonResponse.simpleResponse(-1,"手机号不能为空！");
        }
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(join.getMobile(), EnumVerificationCodeType.OFFICIAL_WEBSITE);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(join.getMobile())
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.OFFICIAL_WEBSITE)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }



    /**
     * 校证码
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createYZM", method = RequestMethod.OPTIONS)
    public void createYZM() {

    }

    /**
     * 生成验证码图片
     *
     * @param response
     * @param session
     */
    @RequestMapping(value = "/createYZM",method = RequestMethod.POST)
    public void createYZM(@RequestBody HttpServletResponse response, HttpSession session,HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        OutputStream out = null;
        try {
            // 设置响应类型
            response.setContentType("image/jpg");
            // 获取创建验证码工具类实例
            CreateImageCodeUtil yzm = CreateImageCodeUtil.getInstance();
            // 获取生成的验证码字符串
            String code = yzm.getCreateYZMCode();
            // 将验证码存放在session
            session.setAttribute("code", code);
            // 获取验证码图片
            BufferedImage img = yzm.getCreateYZMImg(code);
            out = response.getOutputStream();
            // 通过ImageIO写出图片
            ImageIO.write(img, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
