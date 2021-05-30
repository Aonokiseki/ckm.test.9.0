package com.trs.ckm.api.pojo;

import java.util.List;

public class AbsThemeResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<ThemeWord> themeWords;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<ThemeWord> getThemeWords(){
		return themeWords;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, themeWords=%s]", code, message, themeWords);
	}
	
	public String getDetails() {
		return String.format("[themeWords=%s]", themeWords);
	}
	
	public static class ThemeWord {
		private String word;
		private String rword;
		private String wordType;
		private String wordSeg;
		private String wordSen;
		private String wordLen;
		private String pfq;
		private String value;
		
		public String getWord() {return word;}
		public String getRword() {return rword;}
		public String getWordType() {return wordType;}
		public String getWordSeg() {return wordSeg;}
		public String getWordSen() {return wordSen;}
		public String getWordLen() {return wordLen;}
		public String getPfq() {return pfq;}
		public String getValue() {return value;}
		@Override
		public String toString() {
			return String.format("[word=%s, rword=%s, wordType=%s, wordSeg=%s, wordSen=%s, wordLen=%s, pfq=%s, value=%s]", 
					word, rword, wordType, wordSeg, wordSen, wordLen, pfq, value);
		}
	}

}
