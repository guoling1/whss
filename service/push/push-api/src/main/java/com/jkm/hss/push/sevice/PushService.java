package com.jkm.hss.push.sevice;

import java.util.List;
import java.util.Map;

/**
 * Created by longwen.jiang
 */
public interface PushService {


    /**
     * 该方法发送透传类消息
     * @param type  透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
     * @param content  透传消息内容
     * @param pushType 1:单发， 需要传递clientID参数，2：多发需要传递targets 为clientID字符串集合, 3: 群发
     * @param clientId clientId
     * @param targets clientID字符串集合
     * @return  json串形如{result=ok, taskId=OSS-0109_8a01b93476a0ec948435a3f9357f02b2, status=successed_online}，  result=ok 成功
     */

    public  String pushTransmissionMsg(Integer  type, String content, String pushType, String clientId, List<String> targets);



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
    public  String pushNotyPopLoadMsg(String notyTitle, String notyText,String popTitle, String popContent,String loadUrl, String pushType, String clientId, List<String> targets);


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
    public  String pushLinkMsg(String title, String text,String linkUrl, String pushType, String clientId, List<String> targets);
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
    public  String pushNotificationMsg(String title, String text,String logoUrl, String pushType, String clientId, List<String> targets);



    /**
     * 根据店铺ID查询改店铺下所有注册过的APP
     *
     * @param sid
     */
    public List<Map> selectUserAppBySid(String sid);


    /**
     * 根据店铺ID查询改店铺下所有注册过的APP并推送消息
     *
     * @param sid
     */
    public String selectUserAppBySidPushMsg(String sid, String setType,  String content);

    /**
     * 审核资料消息推送
     *
     * @param uid : 用户ID
     *         isSucc: 审核是否成功
     */
    public String pushAuditMsg(Long uid,  Boolean isSucc);

    /**
     * 收款消息推送
     *
     * @param sid ：店铺ＩＤ
     *        payChannel: 支付渠道， 直接传汉字
     *            amount:  金额
     *            code: 收款码
     *
     */
    public String pushCashMsg(Long sid,  String payChannel,Double amount, String code );

    /**
     * 提现消息推送
     *
     * @param uid ：用户ＩＤ
     *        payBank: 提现银行
     *            amount:  金额
     *            cardNo: 卡尾号
     *
     */
    public String pushCashOutMsg(Long uid,  String payBank,Double amount, String cardNo );

}
