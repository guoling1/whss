package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/1/9.
 */
public enum VerificationCodeType {
    REGISTER(1,"注册"),
    ALT_PASSWORD(2,"修改密码");
    public int verificationCodeKey;
    public String verificationCodeValue;

    VerificationCodeType(int verificationCodeKey, String verificationCodeValue) {
        this.verificationCodeKey = verificationCodeKey;
        this.verificationCodeValue = verificationCodeValue;
    }

    public static boolean contains(Integer type){
        for(VerificationCodeType verificationCodeType : VerificationCodeType.values()){
            if(verificationCodeType.verificationCodeKey==type){
                return true;
            }
        }
        return false;
    }

    public static String getValue(Integer type){
        for(VerificationCodeType verificationCodeType : VerificationCodeType.values()){
            if(verificationCodeType.verificationCodeKey==type){
                return verificationCodeType.verificationCodeValue;
            }
        }
        return "";
    }
}
