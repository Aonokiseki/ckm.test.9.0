package com.trs.dlvrs.api.pojo;

import java.util.List;

public class CompareFaceImageResult implements Returnable{
	
	private String code;
	private String message;
	private List<String> file_name;
	private Result result;
	
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
	public Result fetchResult() {
		return result;
	}
	@Override
	public String getFileName() {
		return file_name.toString();
	}
	@Override
	public String getDetails() {
		return String.format("file_name=%s, result=%s", file_name, result);
	}
	@Override
	public String toString() {
		return "CompareFaceImageResult [code=" + code + ", message=" + message + ", file_name=" + file_name
				+ ", result=" + result + "]";
	}
	public static class Result{
		private Face first_face;
		private Face second_face;
		private Double similarity;
		public Double similarity() {
			return similarity;
		}
		@Override
		public String toString() {
			return "Result [first_face=" + first_face + ", second_face=" + second_face + ", similarity=" + similarity
					+ "]";
		}
		public static class Face{
			private String name;
			private Location location;
			@Override
			public String toString() {
				return "Face [name=" + name + ", location=" + location + "]";
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

}
