

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package com.jkm.base.common.spring.alipay.constant;


/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝生活号详情页面获取*/
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgYpQ0hJUWSYUHUoWtP4tRFD/8Lc2vSPobIQH1lkZnUGrjqBW29atE09/M5kRN8hT5GrkQm3sFEU1n8+LQpidvhyWFT0vaqKfNgcANAaabM8YZHNWP6SyVw8MVSW09OM9xmEunrpZVboJF4wl6AgBN2MVrMxxpc7GLkSUa2RsIWthxQTOFJ5h8GH7Vs2cJMBngqlxnLrG2+576m+H4IQfRg/QYB0/r+SrgQj7gLAnZSO04RU0Ajq0D5PESg/FdV+e2962yy/ELIfKVLD7mqg+k/T0DhpCwNBWq36mbW2QMeEnfuk1coF5LsFOg4+c1oaf6HuWrAZnDVATS7dNbIIm0QIDAQAB";

    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET      = "GBK";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET           = "GBK";

    /**签名类型-视支付宝服务窗要求*/
    public static final String SIGN_TYPE         = "RSA2";

    /**开发者账号PID*/
    public static final String PARTNER           = "2088421947044115";

    /** 服务窗appId  */
    //TODO !!!! 注：该appId必须设为开发者自己的生活号id
    public static final String APP_ID            = "2016092801990045";

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患
    public static final String PRIVATE_KEY       = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDOIkhuRDez2nlwSBfoE5jybsL2rlJZ4j3Zk/u6ZeM5Ib4ydhviPaK49+ywFIew3S6VBVaoWpqabQoBL6NTY/Jfwz9q2pLKoPYh69QUCf2z0NLsDDeWW3s6xvmVjxeTGQk38pyky8z8FnB6HPxw3zm2zKnqWTPCH0qrdH2JBEpT/Ib3DTFZW3gHQ2jTBRnCnmWcdy5PkkryT+YDe41mxPwmVtBzj0SOZbmqO9bluumcEfHP2E+jchz+c7oZKiPxKyz2Zf/fhpkEWevduRSyAfuDjjWYb8jr5T+G462vS37e971OxgIQ7fmbW+gFIqQqLmNQxq9YYzOa6hgMA+pcTii3AgMBAAECggEBAJVVNM/dT9VV8yAqulcAy47mOwvmofZH9rLHiFgzM/fRwC5ibjTqnhYOVkQBu4Of2P1QjsUMTnCNie/uCQ6Cm7ZQhoLssedl0KomZM/XpRo9sHAdMbAVuiKMv4df0J5aRtlnQ6AHnQsLXpS4pxFGRlNt1bLWyK5baQbpL4CxwP8x535Pq63AcJqOVdflLp37tmsgwGez/nWVNy5OlV+lujWppCGZzLk3qIt75pT0HASIsUpfFfSji8CKowH4dP1kpyNunVLhpASHixRLb9zOxq69xBE4VsbjuZvs4wCZM0yPWmN48FamI0qBF6kGKmchovjRg+dbOMJ9ql7TGhVgyiECgYEA8U2cWCEGSPN7Z+nLsnrcYFHFkp/0m73KUu3Orp/s5MmN3AQ5Wf2Lkx7Rl3VkvAZ2WxKtMisx//oVlzc0BQ4np1BGsireLvhtzqdlLoixOzrWrbLyFYR5ini/eUGsF+MR81u+/YXRQIbezp6nES6wzwgHDu0zc4tUrn1032Pi32kCgYEA2rBQj7sIkqacQD5u9CfK4gYUfRbpxmdZJsBzeBG4zAo2kF0vxY+GDwTGbbgJC8t7AvB0bvGjHl1VSPqjUP1h5nd45zqGRzlsEVRFnsq8oClKQ3pRaUH8qYz3/FJ3tQdV+tmdAHgkA2OC5myLUmEsEBeXu6H2f2bjXWPTKP2D4x8CgYAVZU9OlScMguJXVWilW7V8qON+28YBpTNyZPmljDKiGqkEcC9xpkSg9+OXrWYQMfZ+nspSn3fgfZk6fj+Tjyy+7iRGmfx3Z7m80CTtmls0qEPuxOYoOEXanFhF9SfAOncV//WQXzQtp4fMBE9F+8DbqpeNoESmox0QF7msMRwluQKBgBRLfI56KweBEssbgCyEbN2g9tHNCJDzEmD4Rz8DVgUJhZrMKxzIfwZxyQBxrdU8YwQY0JVjM2IkA0Askm93tFgxCjVbRw7hE1hgW72qzuqGKnAsr9V23D3X6UOvW5CfR6lp/JoIjD8MQx+jPyK8BdzQc7JkC34zTDJkizoonM69AoGAOn2K7erduEvv7gvb6VKVwgckKUtAP+oQJ9prGDOy7OShR3SwE0RWrC7SzX0hOjrIReVQ9sml7mT4F5S0/1u4eeq+Qp2ONYX0ljV8zb7wVHSJ8Vvh3adlqVYMsQROVjsiTAxGyxMM++Dhn1T8g4q1QEERInLxhJNH/s2YCT2Ylm8=";

    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
    public static final String PUBLIC_KEY        = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAziJIbkQ3s9p5cEgX6BOY8m7C9q5SWeI92ZP7umXjOSG+MnYb4j2iuPfssBSHsN0ulQVWqFqamm0KAS+jU2PyX8M/atqSyqD2IevUFAn9s9DS7Aw3llt7Osb5lY8XkxkJN/KcpMvM/BZwehz8cN85tsyp6lkzwh9Kq3R9iQRKU/yG9w0xWVt4B0No0wUZwp5lnHcuT5JK8k/mA3uNZsT8JlbQc49EjmW5qjvW5brpnBHxz9hPo3Ic/nO6GSoj8Sss9mX/34aZBFnr3bkUsgH7g441mG/I6+U/huOtr0t+3ve9TsYCEO35m1voBSKkKi5jUMavWGMzmuoYDAPqXE4otwIDAQAB";
    /**支付宝网关*/
    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

    /**授权访问令牌的授权类型*/
    public static final String GRANT_TYPE        = "authorization_code";
}