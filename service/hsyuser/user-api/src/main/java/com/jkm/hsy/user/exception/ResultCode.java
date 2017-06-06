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
	CELLPHONE_NOT_CORRECT_FORMAT(2014,"请输入正确的手机号"),
	VERIFICATIONCODE_NO_EFFICACY(2015,"验证码未发送或已失效"),
	VERIFICATIONCODE_ERROR(2015,"验证码错误"),
	VERIFICATIONCODE_SEND_FAIL(2016,"验证码发送失败"),
	VERIFICATIONCODE_TYPE_NULL(2017,"验证码类型为空"),
	VERIFICATIONCODE_TYPE_NOT_EXSIT(2018,"验证码类型不存在"),
	VERIFICATIONCODE_FREQUENT(2019,"验证码发送太频繁"),
	CARDNO_HAS_EXSITED(2020,"该商铺银行卡号已存在"),
	FILE_UPLOAD_FAIL(2021,"上传失败"),
	UPLOADFILE_NOT_EXSITS(2022,"存在未上传文件"),
	UPLOADFILE_COUNT_NOT_RIGHT(2023,"文件个数不符合"),
	FILE_TYPE_NOT_EXSIT(2024,"上传类型不存在"),
	PRIMARY_SHOP_NOT_EXSIT(2025,"主店类型不存在"),
	ACCESSTOKEN_NOT_FOUND(2026,"找不到该令牌"),
	ROLE_TYPE_NOT_EXSIT(2027,"角色类型不存在"),
	ROLE_STATUS_NOT_EXSIT(2028,"角色状态不存在"),
	USER_FORBID(2029,"您已被禁止登陆"),
	PASSWORD_SEND_FAIL(2030,"密码发送失败"),
	USER_CAN_NOT_BE_FOUND(2031,"找不到该用户"),
	SHOP_STATUS_NOT_EXSIT(2032,"店铺状态不存在"),
	SORT_TYPE_NOT_EXSIT(2033,"排序类型不存在"),
	CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO(2034,"当前页数必须大于零"),
	SORT_COLUMN_NOT_EXSIT(2035,"排序列不存在"),
	DISCOUNT_NOT_RIGHT(2036,"请填写正确的折扣"),
	MEMBERSHIP_CARD_ABOVE_LIMIT(2037,"会员开卡超过上线"),
	DEPOSIT_AMOUNT_MUST_BE_ABOVE_ZERO(2038,"储值金额必须大于零"),
	TOKEN_CAN_NOT_BE_NULL(2039,"token为空"),
	USER_NOT_LOGIN(2040,"用户未登录"),
	USER_LOGIN_OUTTIME(2041,"用户登录超时请重新登录"),
	RESULT_FAILE(2100,"返回结果错误"),
	;

	public int resultCode;
	public String resultMessage;
	private ResultCode(int resultCode, String resultMessage) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
}
