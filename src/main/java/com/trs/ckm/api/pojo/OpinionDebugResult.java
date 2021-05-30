package com.trs.ckm.api.pojo;

public class OpinionDebugResult implements CkmBasicResult{
	private String code;
	private String message;
	private Result result;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	public Result getResult() {
		return result;
	}
	
	public static class Result{
		private String result;
		private String debugString;
		@Override
		public String toString() {
			return "[result=" + result + ", debugString=" + debugString + "]";
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getDebugString() {
			return debugString;
		}
		public void setDebugString(String debugString) {
			this.debugString = debugString;
		}
	}
}
