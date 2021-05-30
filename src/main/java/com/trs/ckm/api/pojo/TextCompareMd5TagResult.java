package com.trs.ckm.api.pojo;

public class TextCompareMd5TagResult implements CkmBasicResult{
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
		return String.format("[result=%s]", result);
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", result=" + result + "]";
	}
}
