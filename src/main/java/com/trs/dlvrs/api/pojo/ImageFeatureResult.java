package com.trs.dlvrs.api.pojo;

public class ImageFeatureResult implements Returnable{
	
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
		return "AHashImageResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result="
				+ result + "]";
	}

	public static class Result{
		private String aHash_feature;
		private String pHash_feature;
		private String dHash_feature;
		private String md5;
		
		@Override
		public String toString() {
			return "Result [aHash_feature=" + aHash_feature + ", pHash_feature=" + pHash_feature + ", dHash_feature="
					+ dHash_feature + ", md5=" + md5 + "]";
		}
	}

}
