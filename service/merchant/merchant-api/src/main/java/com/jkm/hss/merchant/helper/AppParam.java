package com.jkm.hss.merchant.helper;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * app公共参数
 */
@Data
public class AppParam {
	/**
	 * 业务代码
	 */
	private String serviceCode;
	/**
	 * 请求令牌
	 */
	private String accessToken;
	/**
	 * 交易时间
	 */
	private String timeStamp;
	/**
	 * 版本号
	 */
	private String v;
	/**
	 * app类型(ios，android)
	 */
	private String appType;
	/**
	 *加密
	 */
	private String requestData;
	/**
	 * 设备号
	 */
	private String deviceId;
	/**
	 * 文件集合
	 */
	private MultipartFile[] files;

	public String toString(){
		return "serviceCode:"+this.serviceCode+","
				+"accessToken:"+this.accessToken+","
				+"timeStamp:"+this.timeStamp+","
				+"v:"+this.v+","
				+"appType:"+this.appType+","
				+"deviceId:"+this.deviceId+","
				+"requestData:"+this.requestData;

	}


}
