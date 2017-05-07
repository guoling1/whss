package com.jkm.hss.controller.pushback;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.GeTuiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/5/5.
 */
@Slf4j
@Controller
@RequestMapping(value = "/geTui")
public class GeTuiController extends BaseController{

    @Autowired
    private OrderService orderService;


    @ResponseBody
    @RequestMapping(value = "/pushBack", method = RequestMethod.OPTIONS)
    public void pushBack() {

    }


    /**
     * 个推回调
     *
     * @param geTuiResponse
     */
    @ResponseBody
    @RequestMapping(value = "/pushBack", method = RequestMethod.POST)
    public JSONObject pushBack(@RequestBody GeTuiResponse geTuiResponse, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        JSONObject jsonObject = new JSONObject();
        try{
            geTuiResponse.setDescr(geTuiResponse.getDesc());
            this.orderService.save(geTuiResponse);
            jsonObject.put("code",0);
            jsonObject.put("result","ok");
            return jsonObject;
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            jsonObject.put("code",-1);
            jsonObject.put("result","ok");
            return jsonObject;
        }
    }
}
