package com.trs.dlvrs.api.pojo;

public class TerroristImageResult implements Returnable{

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
		private String terr_type;
		private Double confidence;
		
		public String getTerr_type() {
			return terr_type;
		}
		public Double getConfidence() {
			return confidence;
		}
		@Override
		public String toString() {
			return "Result [terr_type=" + terr_type + ", confidence=" + confidence + "]";
		}
	}
}
