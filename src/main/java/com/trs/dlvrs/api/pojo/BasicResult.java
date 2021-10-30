package com.trs.dlvrs.api.pojo;

public class BasicResult implements Returnable{
	
	private String code;
	private String message;
	private String file_name;
	private String result;

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public String getFileName() {
		return file_name;
	}
	@Override
	public String getResult() {
		return result;
	}
	@Override
	public String getDetails() {
		return String.format("file_name=%s, result=%s", file_name, result);
	}
	@Override
	public String toString() {
		return "BasicResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result=" + result
				+ "]";
	}
}
