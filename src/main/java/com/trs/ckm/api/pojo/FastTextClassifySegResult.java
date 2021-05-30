package com.trs.ckm.api.pojo;

import java.util.List;

public class FastTextClassifySegResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> results;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Result> getResults(){
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
		private String classname;
		private String weight;
		@Override
		public String toString() {
			return "[classname=" + classname + ", weight=" + weight + "]";
		}
		public String getClassname() {
			return classname;
		}
		public void setClassname(String classname) {
			this.classname = classname;
		}
		public String getWeight() {
			return weight;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
	}

}
