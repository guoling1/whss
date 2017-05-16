package com.jkm.hss.controller.channel;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by yuxiang on 2017-05-09.
 */
@Slf4j
@Controller
@RequestMapping(value = "/xmms/callback")
public class MsCallbackController{


    /**
     * 支付异步通知
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void handlePayCallbackMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final HashMap<String, String> map = new HashMap<>();
        map.put("rt1_bizType",request.getParameter("rt1_bizType"));
        map.put("rt2_retCode",request.getParameter("rt2_retCode"));
        map.put("rt3_retMsg",request.getParameter("rt3_retMsg"));
        map.put("rt4_customerNumber",request.getParameter("rt4_customerNumber"));
        map.put("rt5_orderId",request.getParameter("rt5_orderId"));
        map.put("rt6_serialNumber",request.getParameter("rt6_serialNumber"));
        map.put("rt7_completeDate",request.getParameter("rt7_completeDate"));
        map.put("rt8_orderAmount",request.getParameter("rt8_orderAmount"));
        map.put("rt9_orderStatus",request.getParameter("rt9_orderStatus"));
        map.put("rt10_bindId",request.getParameter("rt10_bindId"));
        map.put("rt11_bankId",request.getParameter("rt11_bankId"));
        map.put("rt12_onlineCardType",request.getParameter("rt12_onlineCardType"));
        map.put("rt13_cardAfterFour",request.getParameter("rt13_cardAfterFour"));
        map.put("rt14_userId",request.getParameter("rt14_userId"));
        final JSONObject jsonObject = (JSONObject) JSONObject.toJSON(map);
        log.info("收到合利宝支付回调通知，参数[{}]", jsonObject.toJSONString());
        log.info(request.getParameter("encryptData")+"............");
        log.info(request.getParameter("encryptKey")+"............");

        ResponseWriter.writeTxtResponse(response, "000000");

        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @RequestMapping(value = "merchant", method = RequestMethod.POST)
    public void handleMerchantPayCallbackMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        log.info(request.getParameter("encryptData")+"............");
        log.info(request.getParameter("encryptKey")+"............");
        ResponseWriter.writeTxtResponse(response, "000000");
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public void handleWithdrawPayCallbackMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        log.info(request.getParameter("encryptData")+"............");
        log.info(request.getParameter("encryptKey")+"............");
        ResponseWriter.writeTxtResponse(response, "000000");
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

















    /**
     * 获取参数
     * @return
     * @throws IOException
     */
    protected JSONObject getRequestJsonParams(HttpServletRequest request){
        try{
            if (request == null) {
                return null;
            }
            String line = "";
            StringBuilder body = new StringBuilder();
            int counter = 0;
            InputStream stream;
            stream = request.getInputStream();
            //读取POST提交的数据内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"utf-8"));
            while ((line = reader.readLine()) != null) {
                if(counter > 0){
                    body.append("\r\n");
                }
                body.append(line);
                counter++;
            }
            if(!"".equals(body)){
                JSONObject jo = JSONObject.parseObject(body.toString());
                return  jo;
            }else{
                return null;
            }
        }catch(Exception e){
            log.info("获取参数异常",e);
        }
        return null;
    }
}
