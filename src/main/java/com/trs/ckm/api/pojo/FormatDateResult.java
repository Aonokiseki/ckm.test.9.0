package com.trs.ckm.api.pojo;

public class FormatDateResult implements CkmBasicResult{

	private String code;
	private String message;
	private String year;
	private String month;
	private String day;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDay() {
		return day;
	}

	public String getDetails() {
		return String.format("[year=%s, month=%s, day=%s]", year,month,day);
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", year=" + year + ", month=" + month
				+ ", day=" + day + "]";
	}
}
