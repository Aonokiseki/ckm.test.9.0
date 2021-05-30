package com.trs.ckm.api.pojo;

import java.util.List;

public class PloResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Entity> entities;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<Entity> getEntities(){
		return entities;
	}

	public String getDetails() {
		return String.format("[entities=%s]", entities);
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", entities=" + entities + "]";
	}
	
	public static class Entity{
		private String type;
		private String word;
		private String frequency;
		@Override
		public String toString() {
			return "[type=" + type + ", word=" + word + ", frequency=" + frequency + "]";
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getFrequency() {
			return frequency;
		}
		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}
	}
	
}
