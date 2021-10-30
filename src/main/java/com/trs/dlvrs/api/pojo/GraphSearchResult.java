package com.trs.dlvrs.api.pojo;

import java.util.List;

public class GraphSearchResult implements Returnable{
	
	private String code;
	private String message;
	private String file_name;
	private List<Result> result;
	
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public String getResult() {
		return result.toString();
	}
	public List<Result> fetchResult() {
		return result;
	}
	@Override
	public String getFileName() {
		return file_name;
	}
	@Override
	public String getDetails() {
		return String.format("file_name=%s, result=%s", file_name, result);
	}
	@Override
	public String toString() {
		return "GraphSearchResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result="
				+ result + "]";
	}
	public static class Result{
		private String id;
		private Double confidence;
		
		public String getId() {
			return id;
		}
		public Double getConfidence() {
			return confidence;
		}
		@Override
		public String toString() {
			return "Result [id=" + id + ", confidence=" + confidence + "]";
		}
	}
}
