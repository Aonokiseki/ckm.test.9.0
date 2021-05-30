package com.trs.ckm.api.pojo;

import java.util.List;

public class DLCatResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<List<String>> results;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<List<String>> getResults(){
		return results;
	}
	public String getDetails() {
		return String.format("[results=%s]", results);
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", results=" + results + "]";
	}
}
