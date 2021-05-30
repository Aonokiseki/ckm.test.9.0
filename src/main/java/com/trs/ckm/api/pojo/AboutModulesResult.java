package com.trs.ckm.api.pojo;

import java.util.List;

public class AboutModulesResult implements CkmBasicResult{

	private final String code;
	private final String message;
	private final List<String> result;

	public AboutModulesResult(final String code, final String message, final List<String> result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<String> getResult(){
		return this.result;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, result=%s]", code, message, result);
	}

	public String getDetails() {
		return String.format("[result=%s]", result);
	}
}
