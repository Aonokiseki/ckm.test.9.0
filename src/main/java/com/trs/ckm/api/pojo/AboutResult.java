package com.trs.ckm.api.pojo;

public class AboutResult implements CkmBasicResult{

	private String code;
	private String message;
	
	public AboutResult(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s]", code, message);
	}

	public String getDetails() {
		return null;
	}
}
