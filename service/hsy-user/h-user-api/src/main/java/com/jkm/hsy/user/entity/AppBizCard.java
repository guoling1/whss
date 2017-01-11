package com.jkm.hsy.user.entity;

import java.util.Date;

/**app_biz_card*/
public class AppBizCard {
    private Long id;
    private Long sid;//店铺ID
    private String cardNO;//卡号
    private String cardAccountName;//开户名
    private String cardBank;//开户行
    private String bankAddress;//开户行支行
    private String cardfID;//银行卡正面照
    private String cardCellphone;//银行预留手机号
    private String idcardNO;//身份证号
    private Integer status;//状态：1正常 2 已删除
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getCardAccountName() {
        return cardAccountName;
    }

    public void setCardAccountName(String cardAccountName) {
        this.cardAccountName = cardAccountName;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getCardfID() {
        return cardfID;
    }

    public void setCardfID(String cardfID) {
        this.cardfID = cardfID;
    }

    public String getCardCellphone() {
        return cardCellphone;
    }

    public void setCardCellphone(String cardCellphone) {
        this.cardCellphone = cardCellphone;
    }

    public String getIdcardNO() {
        return idcardNO;
    }

    public void setIdcardNO(String idcardNO) {
        this.idcardNO = idcardNO;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
