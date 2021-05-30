package com.trs.ckm.api.pojo;

import java.util.List;

public class AppraiseCommentResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Comment> comments;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Comment> getComments(){
		return comments;
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, comments=%s]", code, message, comments);
	}
	
	public String getDetails() {
		return String.format("[comments=%s]", comments);
	}
	
	public static class Comment{
		private String confidence;
		private String sentimentalScore;
		private String entity;
		private String comment;
		private String pattern;
		
		public String getConfidence() {return confidence;}
		public String getSentimentalScore() {return sentimentalScore;}
		public String getEntity() {return entity;}
		public String getComment() {return comment;}
		public String getPattern() {return pattern;}
		@Override
		public String toString() {
			return String.format("[confidence=%s, sentimentalScore=%s, entity=%s, comment=%s, pattern=%s]", 
					confidence, sentimentalScore, entity, comment, pattern);
		}
	}

}
