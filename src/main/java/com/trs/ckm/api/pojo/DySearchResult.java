package com.trs.ckm.api.pojo;

import java.util.List;

public class DySearchResult implements CkmBasicResult{

	private String code;
	private String message;
	private List<String> dyResult;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getDyResult() {
		return dyResult;
	}

	public String getDetails() {
		return String.format("[dyResult=%s]", dyResult);
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, dyResult=%s]", code, message, dyResult);
	}
}
