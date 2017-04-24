package com.jkm.hsy.user.constant;

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
    public static int SHOP_STATUS_REJECT_FILL=5;//驳回充填
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

    public static String REGISTER_VERIFICATION_MESSAGE="好收银${type}验证码${code}，5分钟有效，千万不要告诉别人哦【钱包++】";
    public static int REGISTER_VERIFICATION_NOTICE_TYPE_ID=704;
    public static String SEND_PASSWORD_MESSAGE="您的好收银账号增加成功，登录账号为您当前手机号，初始密码为${password}【钱包++】";
    public static int SEND_PASSWORD_NOTICE_TYPE_ID=705;

    public static int PAGE_SIZE=10;
    public static String QR_URL="http://hsy.qianbaojiajia.com/code/scanCode";
}
