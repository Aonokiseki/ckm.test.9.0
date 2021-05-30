package com.trs.ckm.api.pojo;

import java.util.List;

public class DLMLEmojiExtResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> results;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[results=%s]", results);
	}
	public List<Result> getResults(){
		return results;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", results=" + results + "]";
	}

	public static class Result{
		private String endIndex;
		private String startIndex;
		private String type;
		private String typeName;
		private String word;
		@Override
		public String toString() {
			return "Result [endIndex=" + endIndex + ", startIndex=" + startIndex + ", type=" + type + ", typeName="
					+ typeName + ", word=" + word + "]";
		}
		public String getEndIndex() {
			return endIndex;
		}
		public void setEndIndex(String endIndex) {
			this.endIndex = endIndex;
		}
		public String getStartIndex() {
			return startIndex;
		}
		public void setStartIndex(String startIndex) {
			this.startIndex = startIndex;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
	}
}
