package com.trs.dlvrs.api.pojo;

import java.util.List;

public class OCRImageResult implements Returnable{
	
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
	public List<Result> fetchResult(){
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
	
	public static class Result {
		private Location location;
		private String text;
		private String confidence;
		@Override
		public String toString() {
			return "Result [location=" + location + ", text=" + text + ", confidence=" + confidence + "]";
		}
		public static class Location{
			private String x;
			private String y;
			private String w;
			private String h;
			@Override
			public String toString() {
				return "Location [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
			}
		}
	}
}
