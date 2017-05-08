package com.jkm.hss.controller.pushback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.GeTuiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 */
@Slf4j
@Controller
@RequestMapping(value = "/geTui")
public class GeTuiController extends BaseController{

    @Autowired
    private OrderService orderService;


//    /**
//     * 个推回调
//     *
//     * @param geTuiResponse
//     */
//    @ResponseBody
//    @RequestMapping(value = "/pushBack", method = RequestMethod.POST)
//    public JSONObject pushBack(@RequestBody GeTuiResponse geTuiResponse, HttpServletResponse httpServletResponse) {
//        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//        JSONObject jsonObject = new JSONObject();
//        try{
//            geTuiResponse.setDescr(geTuiResponse.getDesc());
//            this.orderService.save(geTuiResponse);
//            jsonObject.put("code",0);
//            jsonObject.put("result","ok");
//            return jsonObject;
//        }catch (Exception e){
//            log.error("错误信息时",e.getStackTrace());
//            jsonObject.put("code",-1);
//            jsonObject.put("result","ok");
//            return jsonObject;
//        }
//    }

    @ResponseBody
    @RequestMapping(value = "/pushBack", method = RequestMethod.POST)
    public Map<String,Object> pushBack(HttpServletRequest request) throws IOException {
        byte[] bytes = getRequestInputStream(request);
        String jsonstring = new String(bytes);
        GeTuiResponse u2 = JSON.parseObject(jsonstring,GeTuiResponse.class);
        GeTuiResponse geTuiResponse = new GeTuiResponse();
        geTuiResponse.setAppid(u2.getAppid());
        geTuiResponse.setCid(u2.getCid());
        geTuiResponse.setTaskid(u2.getTaskid());
        geTuiResponse.setMsgid(u2.getMsgid());
        geTuiResponse.setCode(u2.getCode());
        geTuiResponse.setDescr(u2.getDesc());
        geTuiResponse.setSign(u2.getSign());
        geTuiResponse.setActionId(u2.getActionId());
        JSONObject jsonObject = new JSONObject();
        try{
            this.orderService.save(geTuiResponse);
            jsonObject.put("code",0);
            jsonObject.put("result","ok");
            return jsonObject;
        }catch (Exception e) {
            log.error("错误信息时",e.getStackTrace());
            jsonObject.put("code",-1);
            jsonObject.put("result","ok");
            return null;
        }
    }

    public static byte[] getRequestInputStream(HttpServletRequest request) throws IOException {
        ServletInputStream input = request.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int position = 0;

        while (true) {
            position = input.read(buffer);
            if (position == -1) {
                break;
            }
            output.write(buffer, 0, position);
        }

        return output.toByteArray();
    }

}
