package com.trs.ckm.api.pojo;

import java.util.List;

public class PloSegResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<SegResult> segResult;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<SegResult> getSegResult(){
		return segResult;
	}
	
	public String getDetails() {
		return String.format("[segResult=%s]", segResult);
	}
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", segResult=" + segResult + "]";
	}

	public static class SegResult{
		private String word;
		private String cate;
		private String posNum;
		private String senNum;
		@Override
		public String toString() {
			return "[word=" + word + ", cate=" + cate + ", posNum=" + posNum + ", senNum=" + senNum + "]";
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getCate() {
			return cate;
		}
		public void setCate(String cate) {
			this.cate = cate;
		}
		public String getPosNum() {
			return posNum;
		}
		public void setPosNum(String posNum) {
			this.posNum = posNum;
		}
		public String getSenNum() {
			return senNum;
		}
		public void setSenNum(String senNum) {
			this.senNum = senNum;
		}
	}

}
