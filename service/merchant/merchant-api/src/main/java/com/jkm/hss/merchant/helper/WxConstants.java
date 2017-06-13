package com.jkm.hss.merchant.helper;

public class WxConstants {
    public final static String DOMAIN = "hss.qianbaojiajia.com";
    public final static String DOMAIN1 = "hsy.qianbaojiajia.com";
    /**
     * 微信公众账号
     */
    public final static String MCH_ID = "1254500201";//商户号
    public static final String APP_ID = "wx77635f5214432d7e";//服务号的应用号
    public final static String APP_KEY = "8f77d0e84c850bb92f5136beb586e5d8";//API密钥
    public final static String API_KEY_CFT = "abcdefghijABCDEFGHIJ123456789jkm";//API密钥(财付通)
    public final static String SIGN_TYPE = "MD5";//签名加密方式
    /**
     * 好收银
     */
    public static final String APP_HSY_ID = "wx2c95c8925f90f9ee";//服务号的应用号
    public final static String APP_HSY_SECRET = "b0ef5fe632a2602f95f8dad15c5e2b84";//密钥
    /**
     * 微信基础接口地址
     */
    public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access
    public static final String GET_CALLBACK_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip";// 获取access
    public static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info"; //获取用户基本信息
    public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";//获取ticket
    public static final String GET_LOGIN_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";// 获取access
    public static final String GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
    public final static String WEIXIN_USERINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN+"%2fwx%2ftoSkip&response_type=code&scope=snsapi_base&state=";
    public final static String WEIXIN_USERINFO_REDIRECT="#wechat_redirect";
    public final static String WEIXIN_TICKET_USERINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN+"%2fwx%2ftoTicketSkip&response_type=code&scope=snsapi_base&state=1012#wechat_redirect";
    public final static String WEIXIN_MERCHANT_USERINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN+"%2fwx%2ftoMerchantSkip&response_type=code&scope=snsapi_base&state=";
    public final static String WEIXIN_DEALER_USERINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN+"%2fwx%2ftoDealerSkip&response_type=code&scope=snsapi_base&state=";
    /**
     * 好收银
     */
    public final static String WEIXIN_HSY_MERCHANT_USERINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_HSY_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN1+"%2fsqb%2ftoSkip&response_type=code&scope=snsapi_base&state=";
    public final static String WEIXIN_HSY_MEMBERSHIP_AUTHINFO="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_HSY_ID+"&redirect_uri=http%3a%2f%2f"+DOMAIN1+"%2fmembership%2fgetAuthInfo&response_type=code&scope=snsapi_base&state=";

}
