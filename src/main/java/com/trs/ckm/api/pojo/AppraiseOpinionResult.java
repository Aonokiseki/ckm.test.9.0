package com.trs.ckm.api.pojo;

import java.util.List;

public class AppraiseOpinionResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> result;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Result> getResult(){
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, result=%s]", 
				code, message, result);
	}
	
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	
	public static class Result{
		private String sub;
		private String des;
		private String obj;
		private String sen;
		private String polarity;
		
		public String getSub() {return sub;}
		public String getDes() {return des;}
		public String getObj() {return obj;}
		public String getSen() {return sen;}
		public String getPolarity() {return polarity;}
		
		@Override
		public String toString() {
			return String.format("[sub=%s, des=%s, obj=%s, sen=%s, polarity=%s]", 
					sub, des, obj, sen, polarity);
		}
	}
}
