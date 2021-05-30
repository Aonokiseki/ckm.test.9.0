package com.trs.ckm.api.pojo;

import java.util.List;

public class CorefResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<List<Coref>> corefChainList;
	private List<List<String>> sentenceList;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	public List<List<Coref>> getCorefChainList() {
		return corefChainList;
	}
	
	public List<List<String>> getSentenceList(){
		return sentenceList;
	}
	public String getDetails() {
		return String.format("[corefChainList=%s, sentenceList=%s]", corefChainList, sentenceList);
	}
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, corefChainList=%s, sentenceList]", 
				code, message, corefChainList, sentenceList);
	}
	
	public static class Coref{
		private String text;
		private String sentIndex;
		private String startIndex;
		private String endIndex;
		
		public String getText() {return text;}
		public String getSentIndex() {return sentIndex;}
		public String getStartIndex() {return startIndex;}
		public String getEndIndex() {return endIndex;}
		
		@Override
		public String toString() {
			return String.format("[text=%s, sentIndex=%s, startIndex=%s, endIndex=%s]", 
					text, sentIndex, startIndex, endIndex);
		}
	}
}
