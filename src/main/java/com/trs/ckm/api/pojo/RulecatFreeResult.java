package com.trs.ckm.api.pojo;

public class RulecatFreeResult implements CkmBasicResult{
	private String code;
	private String message;
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return null;
	}
}
