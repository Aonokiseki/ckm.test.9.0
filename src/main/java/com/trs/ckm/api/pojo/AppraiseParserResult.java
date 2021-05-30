package com.trs.ckm.api.pojo;

import java.util.List;

public class AppraiseParserResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private String dValue;
	private List<AppraiseRes> appraiseRes;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getDValue() {
		return dValue;
	}
	
	public List<AppraiseRes> getAppraiseRes(){
		return appraiseRes;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, dValue=%s, appraiseRes=%s]", 
				code, message, dValue, appraiseRes);
	}
	
	public String getDetails() {
		return String.format("[dValue=%s, appraise=%s]", dValue, appraiseRes);
	}
	
	
	public static class AppraiseRes{
		private String dValue;
		private String theme;
		private String type;
		private String word;
		
		public String getDValue() {return dValue;}
		public String getTheme() {return theme;}
		public String getType() {return type;}
		public String getWord() {return word;}
		
		@Override
		public String toString() {
			return String.format("[dValue=%s, theme=%s, type=%s, word=%s]", 
					dValue, theme, type, word);
		}
	}
}
