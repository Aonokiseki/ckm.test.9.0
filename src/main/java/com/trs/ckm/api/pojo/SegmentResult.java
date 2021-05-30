package com.trs.ckm.api.pojo;

import java.util.List;

public class SegmentResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> segResult;
	
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
		return String.format("[segResult=%s]", segResult);
	}
	public List<Result> getSegResult(){
		return segResult;
	}
	@Override
	public String toString() {
		return "SegResult [code=" + code + ", message=" + message + ", segResult=" + segResult + "]";
	}
	public static class Result{
		private String word;
		private String cate;
		private String posNum;
		private String senNum;
		public String getWord() {
			return word;
		}
		public String getCate() {
			return cate;
		}
		public String getPosNum() {
			return posNum;
		}
		public String getSenNum() {
			return senNum;
		}
		@Override
		public String toString() {
			return "[word=" + word + ", cate=" + cate + ", posNum=" + posNum + ", senNum=" + senNum + "]";
		}
	}
}
