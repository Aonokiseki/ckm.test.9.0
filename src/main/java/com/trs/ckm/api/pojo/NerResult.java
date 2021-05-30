package com.trs.ckm.api.pojo;

import java.util.Map;

public class NerResult implements CkmBasicResult{
	private String code;
	private String message;
	private Map<String,String> map;
	private String language;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[map=%s, language=%s]", map, language);
	}
	public Map<String,String> getMap() {
		return map;
	}
	public String getLanguage() {
		return language;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", map=" + map + ", language=" + language + "]";
	}
}
