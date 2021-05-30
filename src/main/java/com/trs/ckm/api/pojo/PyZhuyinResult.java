package com.trs.ckm.api.pojo;

import java.util.List;

public class PyZhuyinResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Zhuyin> zhuyinList;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<Zhuyin> getZhuyinList(){
		return zhuyinList;
	}
	public String getDetails() {
		return String.format("[zhuyinList=%s]", zhuyinList);
	}
	
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", zhuyinList=" + zhuyinList + "]";
	}


	public static class Zhuyin{
		private String smpy;
		private String ympy;
		private String allpy;
		private String ftpy;
		private List<Seg> segList;
		
		@Override
		public String toString() {
			return "[smpy=" + smpy + ", ympy=" + ympy + ", allpy=" + allpy + ", ftpy=" + ftpy + ", segList="
					+ segList + "]";
		}

		public String getSmpy() {
			return smpy;
		}
		public void setSmpy(String smpy) {
			this.smpy = smpy;
		}
		public String getYmpy() {
			return ympy;
		}
		public void setYmpy(String ympy) {
			this.ympy = ympy;
		}
		public String getAllpy() {
			return allpy;
		}
		public void setAllpy(String allpy) {
			this.allpy = allpy;
		}
		public String getFtpy() {
			return ftpy;
		}
		public void setFtpy(String ftpy) {
			this.ftpy = ftpy;
		}
		public List<Seg> getSegList() {
			return segList;
		}
		public void setSegList(List<Seg> segList) {
			this.segList = segList;
		}

		public static class Seg{
			private String shengmu;
			private String yunmu;
			@Override
			public String toString() {
				return "[shengmu=" + shengmu + ", yunmu=" + yunmu + "]";
			}
			public String getShengmu() {
				return shengmu;
			}
			public void setShengmu(String shengmu) {
				this.shengmu = shengmu;
			}
			public String getYunmu() {
				return yunmu;
			}
			public void setYunmu(String yunmu) {
				this.yunmu = yunmu;
			}
		}
	}
}
