package com.trs.ckm.api.pojo;

import java.util.List;

public class PySearchResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<PinyinItem> pinyinItems;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<PinyinItem> getPinyinItems(){
		return pinyinItems;
	}
	
	public String getDetails() {
		return String.format("[pinyinItems=%s]", pinyinItems);
	}
	
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", pinyinItems=" + pinyinItems + "]";
	}


	public static class PinyinItem{
		private String id;
		private String frequency;
		private String py;
		@Override
		public String toString() {
			return "[id=" + id + ", frequency=" + frequency + ", py=" + py + "]";
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getFrequency() {
			return frequency;
		}
		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}
		public String getPy() {
			return py;
		}
		public void setPy(String py) {
			this.py = py;
		}
	}
}
