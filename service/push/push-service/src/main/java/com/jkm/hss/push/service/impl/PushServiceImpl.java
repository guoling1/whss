package com.jkm.hss.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.jkm.base.common.util.VelocityStringTemplate;
import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.push.dao.PushDao;
import com.jkm.hss.push.entity.AppResult;
import com.jkm.hss.push.entity.Push;
import com.jkm.hss.push.producer.PushProducer;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by longwen.jiang on 2017/01/10
 */
@Slf4j
@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private PushDao pushDao;


    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override

    /**
     * 该方法发送通知类消息，点击通知打开弹框
     * @param notyTitle 通知标题
     * @param notyText 通知内容
     * @param popTitle 弹框标题
     * @param popContent 弹框内容
     * @param loadUrl 下载地址
     * @param pushType 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId clientId
     * @param targets clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */
    public String pushNotyPopLoadMsg(String notyTitle, String notyText, String popTitle, String popContent, String loadUrl, String pushType, String clientId, List<String> targets) {

        String ret= PushProducer.pushNotyPopLoadMsg(notyTitle,notyText,  popTitle,  popContent,  loadUrl,  pushType,  clientId,  targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(notyTitle);
        push.setContent(notyText);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("3");
        push.setTargets(targets!=null?targets.toString():null);

        if(ret.contains("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }
        pushDao.insert(push);
        return ret;
    }

    @Override

    /**
     * 该方法发送通知类消息，点击通知打开链接
     * @param title 通知标题
     * @param text 通知内容
     * @param linkUrl  linkUrl
     * @param pushType 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId clientId
     * @param targets clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */
    public String pushLinkMsg(String title, String text, String linkUrl, String pushType, String clientId, List<String> targets) {
        String ret=  PushProducer.pushLinkMsg( title, text, linkUrl, pushType, clientId, targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(title);
        push.setContent(text);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("2");
        push.setTargets(targets!=null?targets.toString():null);

        if(ret.contains("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }
        pushDao.insert(push);
        return ret;
    }

    @Override

    /**
     *  该方法发送通知类消息，点击通知打开应用
     * @param title:通知标题
     * @param text： 通知内容
     * @param logoUrl：logeUrl
     * @param pushType: 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId： clientId
     * @param targets：clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */
    public String pushNotificationMsg(String title, String text, String logoUrl, String pushType, String clientId, List<String> targets) {

        String ret= PushProducer.pushNotificationMsg( title, text,  logoUrl,  pushType, clientId,  targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(title);
        push.setContent(text);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("1");
        push.setTargets(targets!=null?targets.toString():null);

        if(ret.contains("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }
        pushDao.insert(push);
        return ret;
    }

    @Override
    public List<Map> selectUserAppBySid(String sid) {
        return pushDao.selectUserAppBySid(sid);
    }

    @Override
    public String selectUserAppBySidPushMsg(String sid, String setType ,String content) {


       List<Map>  list=pushDao.selectUserAppBySid(sid);

        List<String>  clients= new ArrayList<>();
        for(Map map: list){
            String clientid=map.get("CLIENTID").toString();
            clients.add(clientid);
        }

        String newContent = null;
        try {
            newContent = new String(content.getBytes("iso8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String ret = this.pushTransmissionMsg(Integer.parseInt(setType), newContent, "2", null, clients);
        return ret;
    }

    @Override
    public String pushAuditMsg(Long uid, Boolean isSucc) {

        List<Map>  list=pushDao.selectUserAppByUid(uid.toString());
        List<String>  clients= new ArrayList<>();
        for(Map map: list){
            if(map.get("CLIENTID")!=null){
                String clientid=map.get("CLIENTID").toString();
                clients.add(clientid);
            }
        }

        final SmsTemplate messageTemplate;
        AppResult appResult = new AppResult();
        if(isSucc){
            messageTemplate = messageTemplateDao.getTemplateByType(EnumNoticeType.AUDIT_PASS.getId());
            appResult.setResultCode(100);
        }else{
            messageTemplate = messageTemplateDao.getTemplateByType(EnumNoticeType.AUDIT_NOPASS.getId());
            appResult.setResultCode(101);
        }
        String newContent =messageTemplate.getMessageTemplate();
        appResult.setResultMessage(newContent);
        String ret = this.pushTransmissionMsg1(2, JSON.toJSONString(appResult), "2", null, clients);
        return ret;



    }

    /**
     * 正式
     * @param sid ：店铺ＩＤ
     *        payChannel: 支付渠道， 直接传汉字
     *            amount:  金额
     * @param payChannel
     * @param amount
     * @param code
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map pushCashMsg(Long sid, String payChannel, Double amount, String code, String transactionNumber) {
        final int count = this.pushDao.getTransactionNumber(transactionNumber);
        if (count > 0){
            Map map = new HashMap();
            map.put("result","已经推送过，不可重复推送");
            return map;
        }
        List<Map>  list=pushDao.selectUserAppBySid(sid.toString());
        List<String>  clients= new ArrayList<>();
        for(Map map: list){
            String clientid=map.get("CLIENTID").toString();
            System.out.print("------------------------------------");
            System.out.print(clientid);
            clients.add(clientid);
        }
         SmsTemplate  messageTemplate = messageTemplateDao.getTemplateByType(EnumNoticeType.CASH.getId());

        Map  data= new HashMap();
        data.put("code", code);
        data.put("payChannel",payChannel );
        data.put("amount", amount);

         String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), data);
        AppResult   appResult=new AppResult() ;
        appResult.setResultCode(200);
        appResult.setResultMessage(content);


//        Map ret = this.pushTransmissionMsg(2, JSON.toJSONString(appResult), "2", null, clients);
        Map ret = this.pushTransmissionMsgTask(2, JSON.toJSONString(appResult), "2", null, clients,transactionNumber);
        return ret;
    }

    @Override
    public String pushCashMsg1(Long sid, String payChannel, Double amount, String code) {
        List<Map>  list=pushDao.selectUserAppBySid(sid.toString());
        List<String>  clients= new ArrayList<>();
        for(Map map: list){
            String clientid=map.get("CLIENTID").toString();
            clients.add(clientid);
        }
        SmsTemplate  messageTemplate = messageTemplateDao.getTemplateByType(EnumNoticeType.CASH.getId());

        Map  data= new HashMap();
        data.put("code", code);
        data.put("payChannel",payChannel );
        data.put("amount", amount);

        String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), data);
        AppResult   appResult=new AppResult() ;
        appResult.setResultCode(200);
        appResult.setResultMessage(content);


//        Map ret = this.pushTransmissionMsg(2, JSON.toJSONString(appResult), "2", null, clients);
        String  ret = this.pushTransmissionMsg(2, JSON.toJSONString(appResult), "2", null, clients);
        return ret;
    }

    @Override
    public String pushCashOutMsg(Long uid, String payBank, Double amount, String cardNo) {
        List<Map>  list=pushDao.selectUserAppByUid(uid.toString());
        List<String>  clients= new ArrayList<>();
        for(Map map: list){
            String clientid=map.get("CLIENTID").toString();
            clients.add(clientid);
        }
         SmsTemplate  messageTemplate = messageTemplateDao.getTemplateByType(EnumNoticeType.CASH_OUT.getId());
        Map  data= new HashMap();
        data.put("bank", payBank);
        data.put("cardNo",cardNo );
        data.put("amount", amount);

        String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), data);

        AppResult   appResult=new AppResult() ;
        appResult.setResultCode(200);
        appResult.setResultMessage(content);


        String ret = this.pushTransmissionMsg(2, content, "2", null, clients);
        return ret;
    }


    @Override
    /**
     * 该方法发送透传类消息
     * @param type  透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
     * @param content  透传消息内容
     * @param pushType 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId clientId
     * @param targets clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */
    public String pushTransmissionMsg(Integer type, String content, String pushType, String clientId, List<String> targets) {


        String target="";
        if(targets!=null){
             target= targets.toString();
        }

        String ret= PushProducer.pushTransmissionMsg(type,content,pushType,clientId,targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle("");
        push.setContent(content);
        push.setClientId(clientId);
//        push.setClientId("3c3002bf2b52d12798a5d29673d91437");
        push.setPushType(pushType);
        push.setTempType("4");
        push.setTargets(target);

        if(ret.contains("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }


        pushDao.insert(push);
        return ret;
    }


    public Map pushTransmissionMsgTask(Integer type, String content, String pushType, String clientId, List<String> targets,String transactionNumber) {


        String target="";
        if(targets!=null){
            target= targets.toString();
        }

        Map ret= PushProducer.pushTransmissionMsgTask(type,content,pushType,clientId,targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle("");
        push.setContent(content);

//        push.setClientId("3c3002bf2b52d12798a5d29673d91437");
        push.setPushType(pushType);
        push.setTempType("4");
        System.out.print("++++++++++++++++");
        System.out.print(ret.get("response"));
        System.out.print(ret);
        System.out.print(ret.get("clientId"));
        if(ret.containsValue("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }
        push.setTaskId((String) ret.get("taskId"));
        push.setClientId((String) ret.get("clientId"));
        push.setTargets(target);
        push.setTransactionNumber(transactionNumber);
        pushDao.insert(push);
        return ret;
    }

    /**
     * 审核
     * @param type
     * @param content
     * @param pushType
     * @param clientId
     * @param targets
     * @return
     */
    public String pushTransmissionMsg1(Integer type, String content, String pushType, String clientId, List<String> targets) {


        String target="";
        if(targets!=null){
            target= targets.toString();
        }

        String ret= PushProducer.pushTransmissionMsg1(type,content,pushType,clientId,targets);

        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle("");
        push.setContent(content);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("4");
        push.setTargets(target);

        if(ret.contains("result=ok")){
            push.setStatus(1);
        }else{
            push.setStatus(0);
        }

        pushDao.insert(push);
        return ret;
    }

    public static void main(String[] args){
        // PushProducer  push = new PushProducer();


        Map ret= PushProducer.pushTransmissionMsgTask(1,"ios测试123456","1","86a8bca1f74ab42d9a7d119943bcdc1b",null);


        // String ret= PushProducer.pushNotificationMsg("title","ios测试","","3",null,null);
//
//        PushServiceImpl impl=new PushServiceImpl();
//
//        impl.pushTransmissionMsgTask(1,"测试","2","86a8bca1f74ab42d9a7d119943bcdc1b",null);
    }
}


