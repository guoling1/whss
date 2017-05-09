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

        final JSONObject requestJsonParams = this.getRequestJsonParams(request);
        log.info(requestJsonParams.toString());
        ResponseWriter.writeTxtResponse(response, "000000");
    }

    @RequestMapping(value = "merchant", method = RequestMethod.POST)
    public void handleMerchantPayCallbackMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final JSONObject requestJsonParams = this.getRequestJsonParams(request);
        log.info(requestJsonParams.toString());
        ResponseWriter.writeTxtResponse(response, "000000");
    }

    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public void handleWithdrawPayCallbackMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final JSONObject requestJsonParams = this.getRequestJsonParams(request);
        log.info(requestJsonParams.toString());
        ResponseWriter.writeTxtResponse(response, "000000");
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
