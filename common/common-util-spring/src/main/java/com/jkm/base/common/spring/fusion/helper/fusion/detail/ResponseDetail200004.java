package com.jkm.base.common.spring.fusion.helper.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL", propOrder={"QUERY_SN","QUERY_REMARK","BUSINESS_CODE","MERCHANT_ID","SEND_TIME","SEND_DT",
		"ID_TYPE","ID","TEL","CARD_NO","ACCOUNT_NAME","RET_COD","ERR_MSG"})
public class ResponseDetail200004 {
	@XmlElement(name="QUERY_SN")
	private String QUERY_SN;
	@XmlElement(name="QUERY_REMARK")
	private String QUERY_REMARK;
	@XmlElement(name="BUSINESS_CODE")
	private String BUSINESS_CODE;
	@XmlElement(name="MERCHANT_ID")
	private String MERCHANT_ID;
	@XmlElement(name="SEND_TIME")
	private String SEND_TIME;
	@XmlElement(name="SEND_DT")
	private String SEND_DT;
	@XmlElement(name="ID_TYPE")
	private String ID_TYPE;
	@XmlElement(name="ID")
	private String ID;
	@XmlElement(name="TEL")
	private String TEL;
	@XmlElement(name="CARD_NO")
	private String CARD_NO;
	@XmlElement(name="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	@XmlElement(name="RET_COD")
	private String RET_COD;
	@XmlElement(name="ERR_MSG")
	private String ERR_MSG;

	public String getQUERY_SN() {
		return QUERY_SN;
	}

	public void setQUERY_SN(String QUERY_SN) {
		this.QUERY_SN = QUERY_SN;
	}

	public String getQUERY_REMARK() {
		return QUERY_REMARK;
	}

	public void setQUERY_REMARK(String QUERY_REMARK) {
		this.QUERY_REMARK = QUERY_REMARK;
	}

	public String getBUSINESS_CODE() {
		return BUSINESS_CODE;
	}

	public void setBUSINESS_CODE(String BUSINESS_CODE) {
		this.BUSINESS_CODE = BUSINESS_CODE;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String MERCHANT_ID) {
		this.MERCHANT_ID = MERCHANT_ID;
	}

	public String getSEND_TIME() {
		return SEND_TIME;
	}

	public void setSEND_TIME(String SEND_TIME) {
		this.SEND_TIME = SEND_TIME;
	}

	public String getSEND_DT() {
		return SEND_DT;
	}

	public void setSEND_DT(String SEND_DT) {
		this.SEND_DT = SEND_DT;
	}

	public String getID_TYPE() {
		return ID_TYPE;
	}

	public void setID_TYPE(String ID_TYPE) {
		this.ID_TYPE = ID_TYPE;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getTEL() {
		return TEL;
	}

	public void setTEL(String TEL) {
		this.TEL = TEL;
	}

	public String getCARD_NO() {
		return CARD_NO;
	}

	public void setCARD_NO(String CARD_NO) {
		this.CARD_NO = CARD_NO;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String ACCOUNT_NAME) {
		this.ACCOUNT_NAME = ACCOUNT_NAME;
	}

	public String getRET_COD() {
		return RET_COD;
	}

	public void setRET_COD(String RET_COD) {
		this.RET_COD = RET_COD;
	}

	public String getERR_MSG() {
		return ERR_MSG;
	}

	public void setERR_MSG(String ERR_MSG) {
		this.ERR_MSG = ERR_MSG;
	}
}