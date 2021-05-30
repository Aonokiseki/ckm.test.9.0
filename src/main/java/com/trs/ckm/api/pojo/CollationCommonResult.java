package com.trs.ckm.api.pojo;

import java.util.List;
/**
 * 常识校对的结果
 */
public class CollationCommonResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Error> errs;
		
	public CollationCommonResult(String code, String message, List<Error> errs) {
		this.code = code;
		this.message = message;
		this.errs = errs;
	}
	
	public CollationCommonResult setCode(String code) {
		this.code = code;
		return this;
	}
	public CollationCommonResult setMessage(String message) {
		this.message = message;
		return this;
	}
	public CollationCommonResult setErrors(List<Error> errs) {
		this.errs = errs;
		return this;
	}
	public String getCode() {
		return this.code;
	}
	public String getMessage() {
		return this.message;
	}
	public List<Error> getErrs(){
		return this.errs;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, errs=%s]", code, message, errs);
	}
	
	public String getDetails() {
		return String.format("[errs=%s]", errs);
	}
	
	public static class Error{
		private String type;
		private List<Word> wordList;
		private String wordCount;
		private String info;
				
		public Error(String type, List<Word> wordList, String wordCount, String info) {
			this.type = type;
			this.wordList = wordList;
			this.wordCount = wordCount;
			this.info = info;
		}
		
		public String getType() {
			return this.type;
		}
		public List<Word> wordList(){
			return this.wordList;
		}
		public String getWordCount(){
			return this.wordCount;
		}
		public String getInfo(){
			return this.info;
		}
		
		@Override
		public String toString() {
			return String.format("[type=%s, wordList=%s, wordCount=%s, info=%s]", type, wordList, wordCount, info);
		}
		
		public static class Word{
			private String word;
			private String position;
			
			public Word(String word, String position) {
				this.word = word;
				this.position = position;
			}
			
			public String getWord() {
				return this.word;
			}
			public String getPosition() {
				return this.position;
			}
			
			@Override
			public String toString() {
				return String.format("[word=%s, position=%s]", word, position);
			}
		}
	}
}
