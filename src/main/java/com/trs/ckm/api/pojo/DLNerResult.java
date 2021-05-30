package com.trs.ckm.api.pojo;

import java.util.List;

public class DLNerResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> results;
	
	@Override
	public String toString() {
		return "DLNerResult [code=" + code + ", message=" + message + ", results=" + results + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	public String getDetails() {
		return String.format("[results=%s]", results);
	}

	public static class Result{
		private String endPos;
		private String senIdx;
		private String startPos;
		private String tag;
		private String type;
		private String word;
		@Override
		public String toString() {
			return "[endPos=" + endPos + ", senIdx=" + senIdx + ", startPos=" + startPos + ", tag=" + tag
					+ ", type=" + type + ", word=" + word + "]";
		}
		public String getEndPos() {
			return endPos;
		}
		public void setEndPos(String endPos) {
			this.endPos = endPos;
		}
		public String getSenIdx() {
			return senIdx;
		}
		public void setSenIdx(String senIdx) {
			this.senIdx = senIdx;
		}
		public String getStartPos() {
			return startPos;
		}
		public void setStartPos(String startPos) {
			this.startPos = startPos;
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
	}
}
