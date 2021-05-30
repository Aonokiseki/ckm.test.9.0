package com.trs.ckm.api.pojo;

import java.util.List;

public class AbsResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private String abs;
	private List<String> wordlist;
	
	public AbsResult(String code, String message, String abs, List<String> wordlist) {
		this.code = code;
		this.message = message;
		this.abs = abs;
		this.wordlist = wordlist;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getAbs() {
		return abs;
	}
	
	public List<String> getWordlist(){
		return wordlist;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, abs=%s, wordlist=%s]", code, message, abs, wordlist);
	}

	public String getDetails() {
		return String.format("[abs=%s, wordlist=%s]", abs, wordlist);
	}

}
