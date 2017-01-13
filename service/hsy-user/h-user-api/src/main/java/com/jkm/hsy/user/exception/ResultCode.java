package com.jkm.hsy.user.exception;

public enum ResultCode {
	SUCCESS(1000,"操作成功"),
	PARAM_EXCEPTION(2000,"请求参数异常"),
	VERSION_NOT_EXIST(2001,"版本不存在"),
	CLASS_NOT_EXIST(2002,"业务代码不存在"),
	FUNCTION_REMOTE_CALL_FAILED(2003,"方法调用失败"),
	ECRYPT_FAIL(2004,"加密失败"),
	DECRYPT_FAIL(2005,"解密失败"),
	PARAM_TRANS_FAIL(2006,"参数转换失败"),
	PARAM_LACK(2007,"参数缺失"),
	CELLPHONE_HAS_BEEN_REGISTERED(2008,"手机号已被注册"),
	INSERT_ERROR(2009,"保存失败"),
	UPDATE_ERROR(2010,"更新失败"),
	CEELLPHONE_HAS_NOT_BEEN_REGISTERED(2011,"该手机号没有注册"),
	PASSWORD_NOT_CORRECT(2012,"密码错误"),
	USER_NO_CEHCK(2013,"用户未通过审核"),
	CELLPHONE_NOT_CORRECT_FORMAT(2014,"手机号必须为11位并符合手机号规则"),
	VERIFICATIONCODE_NO_EFFICACY(2015,"验证码未发送或已失效"),
	VERIFICATIONCODE_ERROR(2015,"验证码错误"),
	VERIFICATIONCODE_SEND_FAIL(2016,"验证码发送失败"),
	VERIFICATIONCODE_TYPE_NULL(2017,"验证码类型为空"),
	VERIFICATIONCODE_TYPE_NOT_EXSIT(2018,"验证码类型不存在"),
	VERIFICATIONCODE_FREQUENT(2019,"验证码发送太频繁"),
	CARDNO_HAS_EXSITED(2020,"银行卡号已存在");

	public int resultCode;
	public String resultMessage;
	private ResultCode(int resultCode, String resultMessage) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
}
