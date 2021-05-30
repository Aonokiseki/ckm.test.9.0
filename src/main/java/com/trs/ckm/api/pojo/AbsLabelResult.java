package com.trs.ckm.api.pojo;

public class AbsLabelResult implements CkmBasicResult{
	private String code;
	private String message;
	private String entities;
	private String keywords;
	private String labels;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getEntities() {
		return entities;
	}
	
	public String getKeyWords() {
		return keywords;
	}
	
	public String getLabels() {
		return labels;
	}
	
	@Override
	public String toString(){
		return String.format("[code=%s, message=%s, entities=%s, keywords=%s, labels=%s]", 
				code, message, entities, keywords, labels);
	}

	public String getDetails() {
		return String.format("[entities=%s, keywords=%s, labels=%s]",
				entities, keywords, labels);
	}
}

