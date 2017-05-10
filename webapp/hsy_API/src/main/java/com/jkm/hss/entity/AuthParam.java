package com.jkm.hss.entity;

/**
 * Created by Allen on 2017/5/10.
 */
public class AuthParam {
    private String code;// _微信

    private String state; // _微信&支付宝

    private String app_id;// _支付宝
    private String auth_code;// _支付宝
    private String sign;// _支付宝

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String toString() {
        return "code:"+code+",state:"+state+",app_id:"+app_id+",auth_code:"+auth_code+",sign:"+sign;
    }
}
