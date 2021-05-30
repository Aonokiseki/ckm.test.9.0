package com.trs.ckm.api.pojo;

public class RoboTitleResult implements CkmBasicResult{
	private String code;
	private String message;
	private String result;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getResult() {
		return result;
	}
	public String getDetails() {
		return result;
	}
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, result=%s]",code, message, result);
	}
}
