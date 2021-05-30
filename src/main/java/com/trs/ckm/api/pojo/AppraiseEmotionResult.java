package com.trs.ckm.api.pojo;

import java.util.List;

public class AppraiseEmotionResult implements CkmBasicResult{

	private String code;
	private String message;
	private List<Sentence> sentences;
	private String totalScore;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Sentence> getSentences(){
		return sentences;
	}
	public String getTotalScore() {
		return totalScore;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, sentences=%s, totalScore=%s]", 
				code, message, sentences, totalScore);
	}
	
	public String getDetails() {
		return String.format("[sentences=%s, totalScore=%s]", sentences, totalScore);
	}
	
	public static class Sentence{
		private String score;
		private String sentence;
		private String word;
		
		public String getScore() {
			return score;
		}
		public String getSentence() {
			return sentence;
		}
		public String getWord() {
			return word;
		}
		
		@Override
		public String toString() {
			return String.format("[score=%s, sentence=%s, word=%s]", 
					score, sentence, word);
		}
	}
}
