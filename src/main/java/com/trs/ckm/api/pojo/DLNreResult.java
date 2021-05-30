package com.trs.ckm.api.pojo;

import java.util.List;
import java.util.Map;

public class DLNreResult implements CkmBasicResult{
	
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
		return String.format("[result=%s]", result);
	}
	
	public static class Result{
		private String entity1;
		private String entity2;
		private Map<String,String> relation;
		private String sentence;
		
		@Override
		public String toString() {
			return "[entity1=" + entity1 + ", entity2=" + entity2 + ", relation=" + relation + ", sentence="
					+ sentence + "]";
		}
		public String getEntity1() {
			return entity1;
		}
		public void setEntity1(String entity1) {
			this.entity1 = entity1;
		}
		public String getEntity2() {
			return entity2;
		}
		public void setEntity2(String entity2) {
			this.entity2 = entity2;
		}
		public Map<String,String> getRelation() {
			return relation;
		}
		public String getSentence() {
			return sentence;
		}
		public void setSentence(String sentence) {
			this.sentence = sentence;
		}
	}

}
