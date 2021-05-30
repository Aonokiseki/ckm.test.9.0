package com.trs.ckm.api.pojo;

import java.util.List;

public class ParserEngResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<KeyWord> keywords;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<KeyWord> getKeywords(){
		return keywords;
	}
	public String getDetails() {
		return String.format("[keywords=%s]", keywords);
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, keywords=%s]", code, message, keywords);
	}
	
	public static class KeyWord{
		private String dep_rel;
		private String father_id;
		private String id;
		private String postag;
		private String word;
		@Override
		public String toString() {
			return "[dep_rel=" + dep_rel + ", father_id=" + father_id + ", id=" + id + ", postag=" + postag
					+ ", word=" + word + "]";
		}
		public String getDep_rel() {
			return dep_rel;
		}
		public void setDep_rel(String dep_rel) {
			this.dep_rel = dep_rel;
		}
		public String getFather_id() {
			return father_id;
		}
		public void setFather_id(String father_id) {
			this.father_id = father_id;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPostag() {
			return postag;
		}
		public void setPostag(String postag) {
			this.postag = postag;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
	}
}
