package com.trs.ckm.api.pojo;

public class DLAbsResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private Result results;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public Result getResults() {
		return results;
	}

	public String getDetails() {
		return String.format("[results=%s]", results);
	}
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", results=" + results + "]";
	}


	public static class Result{
		private String score;
		private String text;
		@Override
		public String toString() {
			return "[score=" + score + ", text=" + text + "]";
		}
		public String getScore() {
			return score;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}
	
}
