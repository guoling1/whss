package com.jkm.hss.merchant.exception;

public enum ResultCode {

	SUCCESS(1000,"操作成功"),
	CUSTOM_EXCEPTION(1001,"自定义异常"),
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
	PASSWORD_NOT_CORRECT(2011,"密码错误"),
	USER_NO_CEHCK(2012,"用户未通过审核"),
	CELLPHONE_NOT_CORRECT_FORMAT(2013,"请输入正确的手机号"),
	VERIFICATIONCODE_ERROR(2014,"验证码错误"),
	ACCESSTOKEN_NOT_FOUND(2015,"找不到该令牌"),
	OEM_NOT_EXSIT(2016,"分公司不存在"),
	INVITECODE_NOT_EXSIT(2017,"邀请码不存在"),
	FRIEND_NOT_OPEN_SHARE(2018,"您的朋友没有开通分享功能"),
	CANT_NOT_SHARE_TO_SELF(2019,"不能邀请自己"),
	PRODUCT_NOT_EXIST(2020,"产品不存在"),
	DEALER_RATE_ERROR(2021,"代理商产品费率配置有误"),
	PRODUCT_RATE_ERROR(2022,"产品费率配置有误"),
	USER_NOT_EXIST(2023,"用户不存在"),
	TOKEN_CAN_NOT_BE_NULL(2024,"token为空"),
	USER_NOT_LOGIN(2025,"用户未登录"),
	USER_LOGIN_OUTTIME(2026,"用户登录超时请重新登录"),
	CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO(2027,"页数必须大于零"),
	;

	public int resultCode;
	public String resultMessage;
	private ResultCode(int resultCode, String resultMessage) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
}
