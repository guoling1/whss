package com.jkm.hss.push.producer;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.jkm.hss.push.config.PushConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息生产
 */
@Slf4j
public class PushProducer {


    //发送消息需要配置的参数
    private static String appId = PushConfig.APPID;
    private static String appKey = PushConfig.APPKEY;
    private static String masterSecret = PushConfig.MASTERSECRET;
    private static String url = PushConfig.URL;




//    private static String appId = "IglbCXAlFFAbv0RJK64619";
//    private static String appKey = "iqviYHBtLL8aEVvdB7QVn1";
//    private static String masterSecret = "F7gCfOPvPu6HSY81gb8hR9";
//    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";


//    private static String appId = "E8K5ZHZmLO6RzIZVFbij9";
//    private static String appKey = "dyeYs6dAdi8MnXMT9Px9M1";
//    private static String masterSecret = "AYUqM4vBjH7OtawmyENyl";
//    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";



    public static void pushMessage(){

    }


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
    public static String pushNotificationMsg(String title, String text,String logoUrl, String pushType, String clientId, List<String> targets) {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle(title);
        template.setText(text);
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);

         return pushTemplate(push,template,pushType,clientId, targets);
    }

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
    public static String pushLinkMsg(String title, String text,String linkUrl, String pushType, String clientId, List<String> targets){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle(title);
        template.setText(text);
        template.setUrl(linkUrl);

        return  pushTemplate(push,template,pushType,clientId,targets);
    }


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
    public static String pushNotyPopLoadMsg(String notyTitle, String notyText,String popTitle, String popContent,String loadUrl, String pushType, String clientId, List<String> targets){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        NotyPopLoadTemplate template = new NotyPopLoadTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setNotyTitle(notyTitle);
        template.setNotyContent(notyText);
        // 配置通知栏图标
        template.setNotyIcon("icon.png");
        // 设置通知是否响铃，震动，或者可清除
        template.setBelled(true);
        template.setVibrationed(true);
        template.setCleared(true);
        // 设置弹框标题与内容
        template.setPopTitle(popTitle);
        template.setPopContent(popContent);
        // 设置弹框显示的图片
        template.setPopImage("");
        template.setPopButton1("下载");
        template.setPopButton2("取消");

        //设置下载地址
        template.setLoadUrl(loadUrl);


        return pushTemplate(push,template,pushType,clientId,targets);
    }

    /**
     * 该方法发送透传类消息
     * @param type  透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
     * @param content  透传消息内容
     * @param pushType 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId clientId
     * @param targets clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */
    public static String pushTransmissionMsg(Integer  type, String content, String pushType, String clientId, List<String> targets){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(type);
        template.setTransmissionContent(content);

        JSONObject jsonObject = JSONObject.parseObject(content);
        String resultMessage = jsonObject.getString("resultMessage");

        //ios透传需要设置的内容
        APNPayload payload = new APNPayload();
        payload.setContentAvailable(0);
        payload.setSound("suc1.wav");
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(resultMessage));
//        payload.setCategory(content);
        payload.addCustomMsg("date",content);
//        payload.addCustomMsg("code",100);
        template.setAPNInfo(payload);
        return pushTemplate(push,template,pushType,clientId,targets);
    }
    public static Map pushTransmissionMsgTask(Integer  type, String content, String pushType, String clientId, List<String> targets){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(type);
        template.setTransmissionContent(content);

        JSONObject jsonObject = JSONObject.parseObject(content);
        String resultMessage = jsonObject.getString("resultMessage");

        //ios透传需要设置的内容
        APNPayload payload = new APNPayload();
        payload.setContentAvailable(0);
        payload.setSound("suc1.wav");
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(resultMessage));
//        payload.setCategory(content);
        payload.addCustomMsg("date",content);
//        payload.addCustomMsg("code",100);
        template.setAPNInfo(payload);
        return pushTemplate1(push,template,pushType,clientId,targets);
    }


    /**
     * 审核透传
     * @param type
     * @param content
     * @param pushType
     * @param clientId
     * @param targets
     * @return
     */
    public static String pushTransmissionMsg1(Integer  type, String content, String pushType, String clientId, List<String> targets){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(type);
        template.setTransmissionContent(content);

        JSONObject jsonObject = JSONObject.parseObject(content);
        String resultMessage = jsonObject.getString("resultMessage");

        //ios透传需要设置的内容
        APNPayload payload = new APNPayload();
        payload.setContentAvailable(0);
//        payload.setSound("default");
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(resultMessage));
//        payload.setCategory(content);
        payload.addCustomMsg("date",content);
        template.setAPNInfo(payload);
        return pushTemplate(push,template,pushType,clientId,targets);
    }


    public static String pushTemplate(IGtPush push , ITemplate template , String pushType, String clientId, List<String> targets){


        IPushResult ret=null;
        if(pushType.equals("1")){
            SingleMessage messageS = new SingleMessage();
            messageS.setData(template);
            messageS.setOffline(true);
            // 离线有效时间，单位为毫秒，可选
            messageS.setOfflineExpireTime(24 * 3600 * 1000);
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(clientId);
            //target.setAlias(Alias);
            try {
                ret = push.pushMessageToSingle(messageS, target);
            } catch (RequestException e) {
                e.printStackTrace();
                ret = push.pushMessageToSingle(messageS, target, e.getRequestId());
            }
        }else if(pushType.equals("2")){
            ListMessage messageL = new ListMessage();
            messageL.setData(template);
            // 设置消息离线，并设置离线时间
            messageL.setOffline(true);
            // 离线有效时间，单位为毫秒，可选
            messageL.setOfflineExpireTime(24 * 1000 * 3600);
            // 配置推送目标
            // taskId用于在推送时去查找对应的message
            String taskId = push.getContentId(messageL);
            List targetss = new ArrayList();

            if(targets!=null) {
                for (int i = 0; i < targets.size(); i++) {
                    Target target1 = new Target();
                    target1.setAppId(appId);
                    target1.setClientId(targets.get(i));
                    targetss.add(target1);
                }
            }
             ret = push.pushMessageToList(taskId, targetss);
        }else{
            AppMessage messageA = new AppMessage();
            messageA.setData(template);
            messageA.setOffline(true);

            //离线有效时间，单位为毫秒，可选
            messageA.setOfflineExpireTime(24 * 1000 * 3600);
            //推送给App的目标用户需要满足的条件
            List<String> appIdList = new ArrayList<String>();
            appIdList.add(appId);
            messageA.setAppIdList(appIdList);
            ret = push.pushMessageToApp(messageA);
        }

        if (ret != null) {
            return ret.getResponse().toString();
        } else {
            return "服务器响应异常";
        }
    }

    public static Map pushTemplate1(IGtPush push , ITemplate template , String pushType, String clientId, List<String> targets){
        Map map = new HashMap();
        String taskId = null;
        IPushResult ret=null;
        if(pushType.equals("2")){
            ListMessage messageL = new ListMessage();
            messageL.setData(template);
            // 设置消息离线，并设置离线时间
            messageL.setOffline(true);
            // 离线有效时间，单位为毫秒，可选
            messageL.setOfflineExpireTime(24 * 1000 * 3600);
            // 配置推送目标
            // taskId用于在推送时去查找对应的message
            taskId = push.getContentId(messageL);
            List targetss = new ArrayList();

            if(targets!=null) {
                for (int i = 0; i < targets.size(); i++) {
                    Target target1 = new Target();
                    target1.setAppId(appId);
                    target1.setClientId(targets.get(i));
                    targetss.add(target1);
                }
            }
            ret = push.pushMessageToList(taskId, targetss);
        }else{
            AppMessage messageA = new AppMessage();
            messageA.setData(template);
            messageA.setOffline(true);

            //离线有效时间，单位为毫秒，可选
            messageA.setOfflineExpireTime(24 * 1000 * 3600);
            //推送给App的目标用户需要满足的条件
            List<String> appIdList = new ArrayList<String>();
            appIdList.add(appId);
            messageA.setAppIdList(appIdList);
            ret = push.pushMessageToApp(messageA);
        }

        if (ret != null) {
            map.put("response",ret.getResponse().toString());
            map.put("taskId",taskId);
        } else {
            map.put("response","服务器响应异常");
            map.put("taskId",taskId);
        }
        return map;
    }









    /**
     * 调用方法
     */
    public void test() {
        final JSONObject jsonObject = new JSONObject();



    }


    public static void main(String[] args){
      //  PushProducer  push = new PushProducer();

        String ret= PushProducer.pushTransmissionMsg(1,"ios测试","2","7c26b6edb57421af16d0dafba23ea1eb",null);


       // String ret= PushProducer.pushNotificationMsg("title","ios测试","","3",null,null);

        System.out.print(ret);
    }



}
