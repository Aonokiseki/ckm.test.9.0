package com.trs.ckm.api.pojo;

public class SimMd5Result implements CkmBasicResult{
	
	private String code;
	private String message;
	private String md5;
	private String theme;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return String.format("[md5=%s, theme=%s]", md5, theme);
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", md5=" + md5 + ", theme=" + theme + "]";
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	
}
