package com.trs.ckm.api.pojo;


public class DLMLEmojiStripResult implements CkmBasicResult{

	private String code;
	private String message;
	private String results;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return String.format("results=%s", results);
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", results=" + results + "]";
	}
	
	public String getResults() {
		return results;
	}

}
