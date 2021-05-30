package com.trs.ckm.api.pojo;

public class ClusterGraphTaskResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private Result result;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public Result getResult() {
		return result;
	}
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	public static class Result{
		private String phase;
		private String method;
		private String current;
		private String total;
		@Override
		public String toString() {
			return "[phase=" + phase + ", method=" + method + ", current=" + current + ", total=" + total + "]";
		}
		public String getPhase() {
			return phase;
		}
		public void setPhase(String phase) {
			this.phase = phase;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public String getCurrent() {
			return current;
		}
		public void setCurrent(String current) {
			this.current = current;
		}
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
		}
	}
}
