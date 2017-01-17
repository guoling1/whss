package com.jkm.hsy.user.entity;

public class AppResult {
	private int resultCode;
	private String resultMessage;
	private String encryptDataResult;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getEncryptDataResult() {
		return encryptDataResult;
	}

	public void setEncryptDataResult(String encryptDataResult) {
		this.encryptDataResult = encryptDataResult;
	}
}
