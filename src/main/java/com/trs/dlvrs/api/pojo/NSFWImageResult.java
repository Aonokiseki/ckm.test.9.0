package com.trs.dlvrs.api.pojo;

public class NSFWImageResult implements Returnable{
	
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
	public Result fetchResult() {
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
		private String nsfw_type;
		private Double nsfw_score;
		
		public String getNsfw_type() {
			return nsfw_type;
		}
		public Double getNsfw_score() {
			return nsfw_score;
		}
		@Override
		public String toString() {
			return "Result [nsfw_type=" + nsfw_type + ", nsfw_score=" + nsfw_score + "]";
		}
	}
}
