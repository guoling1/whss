package com.jkm.hss.controller.website;

import com.aliyun.oss.OSSClient;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CreateImageCodeUtil;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictListResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.helper.request.DistrictRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

/**
 * Created by zhangbin on 2017/4/7.
 */
@Controller
@RequestMapping(value = "/createImageCode")
public class CreateImageCodeController {
    @Autowired
    private AppBizDistrictService appBizDistrictService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 查询所有省
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectProvinces", method = RequestMethod.POST)
    public CommonResponse selectProvinces(){
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findAllProvinces();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询省对应下的市
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectCities", method = RequestMethod.POST)
    public CommonResponse selectCities(@RequestBody final DistrictRequest districtRequest){
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findCityByProvinceCode(districtRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询所有地区
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectDistrict", method = RequestMethod.POST)
    public CommonResponse selectDistrict(){
        List<AppBizDistrictListResponse> list =  this.appBizDistrictService.findAllDistrict();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResponse save(@RequestBody String userName, String checkCode, HttpSession session, String companyName){
        String code=(String) session.getAttribute("code");
        if(code!=null && !code.equals(checkCode)){
            return CommonResponse.simpleResponse(-1,"验证码不正确，请重新输入！");
        }
        return null;
    }

    /**
     * 生成验证码图片
     *
     * @param response
     * @param session
     */
    @RequestMapping(value = "/createYZM",method = RequestMethod.GET)
    public void createYZM(HttpServletResponse response, HttpSession session) {
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
