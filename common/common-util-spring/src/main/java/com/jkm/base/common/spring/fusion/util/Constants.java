package com.jkm.base.common.spring.fusion.util;

/**
 * 系统使用常量
 * @author wujh
 *
 */
public interface Constants {
	
	// 通讯报文字符集
	public static final String transfer_charset = "GBK";
	
	// 加密相关的字符集
	public static final String encrypt_charset = "UTF-8";
	
	/** 数据格式 */
	public static String DATA_TYPE_XML = "0";
	
	/** 处理级别 */
	public static String LEVEL_0 = "0";
	
	/**
	 * 访问成功编码
	 */
	public static final String  RESPONSE_CODE_SUCCESS = "PWM00000";
	
	/**
	 * 访问失败编码
	 */
	public static final String  RESPONSE_CODE_FAIL = "F00001";

	/**
	 * 商户号
	 */
	public static final String MERC_ID = "800010000050050";

	/**
	 * 银行卡鉴权（100004）
	 */
	public static final String  CARD_AUTH = "https://www.ulpay.com/gateway/auth/cardAuth";
	/**
	 * 银行卡鉴权查询（200004）
	 */
	public static final String  QUERY_CARD_AUTH = "https://www.ulpay.com/gateway/query/queryCardAuth";

}
