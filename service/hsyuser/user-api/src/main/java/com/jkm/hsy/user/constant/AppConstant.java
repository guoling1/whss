package com.jkm.hsy.user.constant;

import java.security.MessageDigest;

/**
 * Created by Allen on 2017/1/7.
 */
public class AppConstant {
    public static String SHA_KEY="excalibur";
    public static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static String FIlE_ROOT="hsy";
    public static int USER_STATUS_NORMAL=1;
    public static int USER_STATUS_NO_CHECK=2;
    public static int USER_STATUS_FORBID=99;

    public static int SHOP_STATUS_NORMAL=1;//正常
    public static int SHOP_STATUS_NO_CHECK=2;//待审核
    public static int SHOP_STATUS_REJECT=3;//申请驳回
    public static int SHOP_STATUS_REGISTERED=4;//已注册
    public static int SHOP_STATUS_FORBID=99;

    public static int CARD_STATUS_NORMAL=1;
    public static int CARD_STATUS_FORBID=99;

    public static int ROLE_STATUS_NORMAL=1;
    public static int ROLE_STATUS_FORBID=99;

    public static int ROLE_CORPORATION =1;//法人
    public static int ROLE_MANAGER=2;//店长
    public static int ROLE_EMPLOYEE=3;//店员
    public static int ROLE_FINANCE =4;//财务

    public static int ROLE_TYPE_PRIMARY =1;//主店
    public static int ROLE_TYPE_BRANCH =2;//分店

    public static String REGISTER_VERIFICATION_MESSAGE="【钱包++】${type}验证码${code}，5分钟有效，千万不要告诉别人哦";
    public static int REGISTER_VERIFICATION_NOTICE_TYPE_ID=704;
    public static String SEND_PASSWORD_MESSAGE="【钱包++】您的钱包++账号增加成功，登录账号为您当前手机号，初始密码为${password}";
    public static int SEND_PASSWORD_NOTICE_TYPE_ID=705;

    public static String SN_VOICE="SDK-SKY-010-03106";
    public static String PWD_VOICE=MD5(SN_VOICE+"b_452469-64").toUpperCase();
    public static String SMSURL_VOICE="http://sdk.entinfo.cn:8068/audio.asmx/AudioSend";
    public static String VERIFICATION_MESSAGE_VOICE="${type}验证码为6位数字，为您播放两遍，请注意聆听。您的验证码是${code}，您的验证码是${code}【钱包加加】";
    public static int VERIFICATION_NOTICE_TYPE_ID_VOICE=708;

    public static int PAGE_SIZE=10;
    public static String QR_URL="http://hsy.qianbaojiajia.com/code";

    public final static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(val));

            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
