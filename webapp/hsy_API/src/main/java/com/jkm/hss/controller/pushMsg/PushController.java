package com.jkm.hss.controller.pushMsg;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.PushRequest;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by huangwei on 5/11/16.
 */
@Slf4j
@Controller
@RequestMapping("/pushMsg")
public class PushController extends BaseController {

    @Autowired
    private PushService pushService;




    /**
     * 推送收银消息--公共接口
     *
     *
     * setType  :透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动。
     * pushType:  1: 单发， 2：多发， 3： 群发   。
     * content: 发送消息的内容。
     * clientId: 客户端ID

     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushCommon")
    public CommonResponse pushCommon(final HttpServletResponse response, HttpServletRequest request,@RequestBody final PushRequest pushRequest) {
        String setType = pushRequest.getSetType();
        String pushType = pushRequest.getPushType();
        String content = pushRequest.getContent();
        String sid= pushRequest.getSid();
        String clientId= pushRequest.getClientId();


        try{
            Preconditions.checkState(!Strings.isNullOrEmpty(setType), "setType不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(pushType), "pushType不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(content), "content不能为空");
        }catch (Exception e){
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }

        String newContent = null;
        try {
            newContent = new String(content.getBytes("iso8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String ret = pushService.pushTransmissionMsg(Integer.parseInt(setType), newContent, pushType, clientId, null);

        System.out.print(ret);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, ret);
    }




    /**
     * 推送收银消息--根据店铺ID发送消息
     *
     *
     * setType  :透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动。
     * content: 发送消息的内容。
     * sid: 店铺ID

     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushSY")
    public CommonResponse pushSY(final HttpServletResponse response, HttpServletRequest request,@RequestBody final PushRequest pushRequest) {
        String setType = pushRequest.getSetType();
        String content = pushRequest.getContent();
        String sid= pushRequest.getSid();
        try{
            Preconditions.checkState(!Strings.isNullOrEmpty(setType), "setType不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(content), "content不能为空");
            Preconditions.checkState(!Strings.isNullOrEmpty(sid), "sid不能为空");
        }catch (Exception e){
             return CommonResponse.simpleResponse(-1, e.getMessage());
        }

        //String ret = pushService.selectUserAppBySidPushMsg(sid, setType, content);


        //String ret =pushService.pushAuditMsg(123456L,true);

        // String ret = pushService.pushCashMsg(123456L,"微信", 100D,"1234");

          String ret =  pushService.pushCashOutMsg(123456L,"招商银行",100D,"123456");

        System.out.print(ret);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, ret);
    }

}
