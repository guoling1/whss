package com.jkm.hsy.user.exception;

public class ApiHandleException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResultCode resultCode;
	private String msg;
	
	public ApiHandleException() {
		super();
	}
	
	public ApiHandleException(ResultCode resultCode) {
		super();
		this.resultCode = resultCode;
	}

	public ApiHandleException( ResultCode resultCode, String msg) {
		super();
		this.resultCode = resultCode;
		this.msg = msg;
	}

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
