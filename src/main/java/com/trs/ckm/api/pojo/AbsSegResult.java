package com.trs.ckm.api.pojo;

import java.util.List;

public class AbsSegResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Term> terms;
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	public List<Term> getTerms(){
		return terms;
	}
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, terms=%s]", 
				code, message, terms);
	}
	
	public String getDetails() {
		return String.format("[terms=%s]", terms);
	}
	
	public static class Term{
		private String cate;
		private String word;
		
		public String getCate() {return cate;}
		public String getWord() {return word;}
		
		@Override
		public String toString() {return String.format("[cate=%s, word=%s]", cate, word);}
	}
}
