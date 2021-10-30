package com.trs.dlvrs.api.pojo;

import java.util.List;

public class ImageSupportResult implements Returnable{
	
	private String code;
	private String message;
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
		return null;
	}
	@Override
	public String getDetails() {
		return result.toString();
	}
	@Override
	public String toString() {
		return "ImageSupportResult [code=" + code + ", message=" + message + ", result=" + result + "]";
	}
	
	public static class Result{
		List<String> allowed_image_format;

		@Override
		public String toString() {
			return "Result [allowed_image_format=" + allowed_image_format + "]";
		}
		
	}
}
