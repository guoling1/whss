package com.jkm.hsy.user.entity;

public class AppParam {
	private String serviceCode;//业务代码
	private String accessToken;//请求令牌
	private String timeStamp;//交易时间 时间格式为yyyy-MM-dd HH:mm:ss
	private String v;//版本号
	private String appType;//app类型
	private String requestData;//加密
	private String deviceid;//设备号

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String toString(){
		return "serviceCode:"+this.serviceCode+","
				+"accessToken:"+this.accessToken+","
				+"timeStamp:"+this.timeStamp+","
				+"v:"+this.v+","
				+"appType:"+this.appType+","

				+"requestData:"+this.requestData+","
				+"deviceid:"+this.deviceid;
	}

}
