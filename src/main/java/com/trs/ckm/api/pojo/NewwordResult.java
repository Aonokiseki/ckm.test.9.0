package com.trs.ckm.api.pojo;

import java.util.List;

public class NewwordResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> result;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<Result> getResult(){
		return result;
	}
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	
	@Override
	public String toString() {
		return  "[code=" + code + ", message=" + message + ", result=" + result + "]";
	}

	public static class Result{
		private String word;
		private String frequency;
		private String inList;
		
		@Override
		public String toString() {
			return "[word=" + word + ", frequency=" + frequency + ", inList=" + inList + "]";
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getFrequency() {
			return frequency;
		}
		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}
		public String getInList() {
			return inList;
		}
		public void setInList(String inList) {
			this.inList = inList;
		}
	}
}
