package com.trs.ckm.api.pojo;

import java.util.List;

public class DLCollateResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> results;

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public String getDetails() {
		return "[results="+ results +"]";
	}
	@Override
	public String toString() {
		return "DLCollateResult [code=" + code + ", message=" + message + ", results=" + results + "]";
	}
	public List<Result> getResults(){
		return results;
	}

	public static class Result{
		private String collateWord;
		private Integer endPos;
		private Integer errorType;
		private String errorWord;
		private Integer startPos;
		private String weight;
		@Override
		public String toString() {
			return "[collateWord=" + collateWord + ", endPos=" + endPos + ", errorType=" + errorType
					+ ", errorWord=" + errorWord + ", startPos=" + startPos + ", weight=" + weight + "]";
		}
		public String getCollateWord() {
			return collateWord;
		}
		public void setCollateWord(String collateWord) {
			this.collateWord = collateWord;
		}
		public Integer getEndPos() {
			return endPos;
		}
		public void setEndPos(Integer endPos) {
			this.endPos = endPos;
		}
		public Integer getErrorType() {
			return errorType;
		}
		public void setErrorType(Integer errorType) {
			this.errorType = errorType;
		}
		public String getErrorWord() {
			return errorWord;
		}
		public void setErrorWord(String errorWord) {
			this.errorWord = errorWord;
		}
		public Integer getStartPos() {
			return startPos;
		}
		public void setStartPos(Integer startPos) {
			this.startPos = startPos;
		}
		public String getWeight() {
			return weight;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
	}
	
}
