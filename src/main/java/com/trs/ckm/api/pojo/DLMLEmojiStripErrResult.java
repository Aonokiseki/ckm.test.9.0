package com.trs.ckm.api.pojo;

import java.util.List;

public class DLMLEmojiStripErrResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<String> results;
	public String getCode() { return code; }
	public String getMessage() { return message; }
	public String getDetails() { return null; }
	public List<String> getResults() { return results; }
	@Override
	public String toString() {
		return "DLMLEmojiStripErrResult [code=" + code + ", message=" + message + ", results=" + results + "]";
	}
}
