package com.jkm.hss.helper;


import java.io.Serializable;


/**
 * @company jkm
 * @Title JKMTradeServiceException
 * @version 1.0
 * @date 2016-03-29
 * @author xiezhenghua
 * 
 * @description 行业应用系统异常定义
 */
public class JKMTradeServiceException extends RuntimeException{

	protected JKMTradeServiceException(String defineCode) {
		super(defineCode);
	}


	private static final long serialVersionUID = 618428211364837565L;
	
	private JkmApiErrorCode jkmTradeErrorCode;
	
	/*-------------------------------------------
	 * 构造指定错误码的异常
	 * 好处：错误码统一维护
	 */
	public JKMTradeServiceException(JkmApiErrorCode errorCode){
		super(errorCode.getErrorCode());
		this.jkmTradeErrorCode = errorCode;
	}
	
	public JKMTradeServiceException(JkmApiErrorCode jkmTradeErrorCode, String defineMsg){
		super(jkmTradeErrorCode.getErrorCode());
		this.jkmTradeErrorCode = jkmTradeErrorCode;
		this.jkmTradeErrorCode.setErrorMessage(defineMsg);//自定义错误信息
	}

	public JkmApiErrorCode getJKMTradeErrorCode() {
		return jkmTradeErrorCode;
	}

	public void setJKMTradeErrorCode(JkmApiErrorCode jkmTradeErrorCode) {
		this.jkmTradeErrorCode = jkmTradeErrorCode;
	}

	
	//--------------------------------------------
	
	

}
