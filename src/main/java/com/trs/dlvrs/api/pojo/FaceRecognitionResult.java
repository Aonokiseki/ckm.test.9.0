package com.trs.dlvrs.api.pojo;

import java.util.List;

public class FaceRecognitionResult implements Returnable{
	
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
	public List<Result> getResultList() {
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
	
	public static class Result{
		
		private Location location;
		private String gender;
		private String age;
		private String type;
		private String ori_feature;
		private String str_feature;
		private String have_recognition;
		
		@Override
		public String toString() {
			return "Result [location=" + location + ", gender=" + gender + ", age=" + age + ", type=" + type
					+ ", ori_feature=" + ori_feature + ", str_feature=" + str_feature + ", have_recognition="
					+ have_recognition + "]";
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
