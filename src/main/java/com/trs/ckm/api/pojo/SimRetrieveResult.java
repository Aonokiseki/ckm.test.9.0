package com.trs.ckm.api.pojo;

import java.util.List;

public class SimRetrieveResult implements CkmBasicResult{

	private String code;
	private String message;
	private String revNum;
	private String threshold;
	private List<RevDoc> revDoc;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getRevNum() {
		return revNum;
	}
	
	public String getThreshold() {
		return threshold;
	}
	
	public List<RevDoc> getRevDoc() {
		return revDoc;
	}

	public String getDetails() {
		return String.format("[revNum=%s, threshold=%s, revDoc=%s]", revNum, threshold, revDoc);
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", revNum=" + revNum + ", threshold="
				+ threshold + ", revDoc=" + revDoc + "]";
	}
	
	public static class RevDoc{
		private String id;
		private String simv;
		@Override
		public String toString() {
			return "[id=" + id + ", simv=" + simv + "]";
		}
		public String getId() {
			return id;
		}
		public String getSimv() {
			return simv;
		}
	}
}
