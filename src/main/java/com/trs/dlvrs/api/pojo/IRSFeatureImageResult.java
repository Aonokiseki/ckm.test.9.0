package com.trs.dlvrs.api.pojo;

public class IRSFeatureImageResult implements Returnable{
	
	private String code;
	private String message;
	private String file_name;
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
	public Result fetchResult(){
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
		return "IRSFeatureImageResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result="
				+ result + "]";
	}
	public static class Result{
		private String image_org_feature;
		private String image_str_feature;
		
		public String getImage_org_feature() {
			return image_org_feature;
		}
		public String getImage_str_feature() {
			return image_str_feature;
		}
		@Override
		public String toString() {
			return "Result [image_org_feature=" + image_org_feature + ", image_str_feature=" + image_str_feature + "]";
		}
	}
}
