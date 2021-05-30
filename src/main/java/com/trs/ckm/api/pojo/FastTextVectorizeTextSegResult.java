package com.trs.ckm.api.pojo;

import java.util.List;

public class FastTextVectorizeTextSegResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<String> result;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("result=%s", result);
	}
	public List<String> getResult(){
		return result;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", result=" + result + "]";
	}
}
