package com.trs.ckm.api.pojo;

import java.util.List;

public class DLAllModelResult implements CkmBasicResult{

	private String code;
	private String message;
	private List<String> results;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[results=%s]", results);
	}
	public List<String> getResults(){
		return results;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", results=" + results + "]";
	}
}
