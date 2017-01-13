package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/1/7.
 */
public class AppConstant {
    public static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static String FIlE_ROOT="hsy_app";
    public static int USER_STATUS_NORMAL=1;
    public static int USER_STATUS_NO_CHECK=2;
    public static int USER_STATUS_FORBID=99;

    public static int SHOP_STATUS_NORMAL=1;
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
}
