package com.trs.dlvrs.api.pojo;

import java.util.List;

public class ModulesResult implements Returnable{
	
	private String code;
	private String message;
	private List<String> result;

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public String getResult() {
		return result.toString();
	}
	public List<String> getResults(){
		return result;
	}
	@Override
	public String getFileName() {
		return null;
	}
	@Override
	public String getDetails() {
		return result.toString();
	}
	@Override
	public String toString() {
		return "ModulesResult [code=" + code + ", message=" + message + ", result=" + result + "]";
	}
}
