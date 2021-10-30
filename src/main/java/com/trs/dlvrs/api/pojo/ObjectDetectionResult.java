package com.trs.dlvrs.api.pojo;

import java.util.List;

public class ObjectDetectionResult implements Returnable{
	
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
	@Override
	public String toString() {
		return "ObjectDetectionResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result="
				+ result + "]";
	}

	public static class Result{
		private Location location;
		private String label;
		private Double confidence;
		
		public Location getLocation() {
			return location;
		}
		public String getLabel() {
			return label;
		}
		public Double getConfidence() {
			return confidence;
		}
		@Override
		public String toString() {
			return "Result [location=" + location + ", label=" + label + ", confidence=" + confidence + "]";
		}

		public static class Location{
			private Integer x;
			private Integer y;
			private Integer w;
			private Integer h;
			@Override
			public String toString() {
				return "Location [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
			}
		}
	}
}
