package com.trs.ckm.api.pojo;

import java.util.List;

public class RulecatResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<CatResult> catResults;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<CatResult> getCatResults() {
		return catResults;
	}
	public String getDetails() {
		return String.format("[catResults=%s]", catResults);
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", catResults=" + catResults + "]";
	}
	public static class CatResult{
		private String cat;
		private String value;
		private List<String> rules;
		private List<String> matchedWord;
		@Override
		public String toString() {
			return "[cat=" + cat + ", value=" + value + ", rules=" + rules + ", matchedWords=" + matchedWord
					+ "]";
		}
		public String getCat() {
			return cat;
		}
		public void setCat(String cat) {
			this.cat = cat;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public List<String> getRules() {
			return rules;
		}
		public void setRules(List<String> rules) {
			this.rules = rules;
		}
		public List<String> getMatchedWords() {
			return matchedWord;
		}
		public void setMatchedWords(List<String> matchedWords) {
			this.matchedWord = matchedWords;
		}
		
	}
}
