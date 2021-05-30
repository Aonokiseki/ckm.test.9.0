package com.trs.ckm.api.pojo;

import java.util.List;

public class CollationEnResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> result;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	public List<Result> getResult() {
		return result;
	}
	public String getDetails() {
		return String.format("[result==%s]", result);
	}
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, result=%s]", 
				code, message, result);
	}
	
	public static class Result{
		private String word;
		private String lemma;
		private List<Correction> correction;
		
		public String getWord() {
			return word;
		}
		public String getLemma() {
			return lemma;
		}
		public List<Correction> getCorrection(){
			return correction;
		}
		@Override
		public String toString() {
			return String.format("[word=%s, lemma=%s, correction=%s]", word, lemma, correction);
		}
		
		public static class Correction{
			private String word;
			private String type;
			private String keyId;
			
			public String getWord() {
				return word;
			}
			public String getType() {
				return type;
			}
			public String getKeyId() {
				return keyId;
			}
			
			@Override
			public String toString() {
				return String.format("[word=%s, type=%s, keyId=%s]", 
						word, type, keyId);
			}
		}
	}
	
	
}
