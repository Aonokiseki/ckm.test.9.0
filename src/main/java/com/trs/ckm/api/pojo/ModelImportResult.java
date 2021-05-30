package com.trs.ckm.api.pojo;

public class ModelImportResult implements CkmBasicResult{
	
	private String code;
	private String message;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return null;
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + "]";
	}
}
