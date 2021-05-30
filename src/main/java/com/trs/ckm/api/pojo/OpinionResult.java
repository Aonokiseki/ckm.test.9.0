package com.trs.ckm.api.pojo;

public class OpinionResult implements CkmBasicResult{
	private String code;
	private String message;
	private String result;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	public String getResult() {
		return result;
	}
}
