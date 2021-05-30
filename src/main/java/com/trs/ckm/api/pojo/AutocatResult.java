package com.trs.ckm.api.pojo;

import java.util.List;

public class AutocatResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> results;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Result> getResults(){
		return results;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, results=%s]", 
				code, message, results);
	}
	
	public String getDetails() {
		return String.format("[results=%s]", results);
	}
	
	public static class Result{
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		@Override
		public String toString() {
			return String.format("[name=%s, value=%s]", 
					name, value);
		}
	}
}
