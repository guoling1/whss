package com.jkm.hss.push.service.impl;

import com.jkm.hss.push.dao.PushDao;
import com.jkm.hss.push.entity.Push;
import com.jkm.hss.push.producer.PushProducer;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by longwen.jiang on 2017/01/10
 */
@Slf4j
@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private PushDao pushDao;

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
        String target= targets.toString();
        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(notyTitle);
        push.setContent(notyText);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("3");
        push.setTargets(target);

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
        String target= targets.toString();
        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(title);
        push.setContent(text);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("2");
        push.setTargets(target);

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
        String target= targets.toString();
        Push push= new Push();
        push.setPid(UUID.randomUUID().toString());
        push.setTitle(title);
        push.setContent(text);
        push.setClientId(clientId);
        push.setPushType(pushType);
        push.setTempType("1");
        push.setTargets(target);

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

        String target= targets.toString();
        String ret= PushProducer.pushTransmissionMsg(type,content,pushType,clientId,targets);

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


        //String ret= PushProducer.pushTransmissionMsg(1,"ios测试123456","1","7c26b6edb57421af16d0dafba23ea1eb",null);


        // String ret= PushProducer.pushNotificationMsg("title","ios测试","","3",null,null);

        PushServiceImpl   impl=new PushServiceImpl();

        impl.pushTransmissionMsg(1,"ios测试123456","1","7c26b6edb57421af16d0dafba23ea1eb",null);
    }
}


