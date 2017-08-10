package com.jkm.socket.controller;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.socket.service.SocketExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yulong.zhang on 2017/7/26.
 */
@Slf4j
@Controller
@RequestMapping("/socket")
public class SocketController extends BaseController {

    @Autowired
    private SocketExecutorService socketExecutorService;

    /**
     * socket发送消息
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("sendPrintMsg")
    public Object sendPrintMsg(final HttpServletRequest httpServletRequest) {
        final String msg = super.read(httpServletRequest);
        final long shopId = JSONObject.parseObject(msg).getLongValue("shopId");
        log.info("店铺-[{}],发送打印小票信息-[{}]", shopId, msg);
        this.socketExecutorService.runTask(shopId, msg);
        return "success";
    }

}
