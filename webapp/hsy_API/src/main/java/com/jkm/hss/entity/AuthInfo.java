package com.jkm.hss.entity;

/**
 * Created by Allen on 2017/5/10.
 */
public class AuthInfo {
    private boolean successFlag;
    private String infoDetail;
    private String uidEncode;
    private String source;
    private String userID;
    private String openID;
    private String operate;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public String getUidEncode() {
        return uidEncode;
    }

    public void setUidEncode(String uidEncode) {
        this.uidEncode = uidEncode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getInfoDetail() {
        return infoDetail;
    }

    public void setInfoDetail(String infoDetail) {
        this.infoDetail = infoDetail;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String toString() {
        return "successFlag:"+successFlag+",uidEncode:"+uidEncode+",userID:"+userID+",openID:"+openID+",infoDetail:"+infoDetail+",operate:"+operate;
    }

}
