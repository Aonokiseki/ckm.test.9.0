package com.trs.ckm.api.pojo;

public class SimModelResult implements CkmBasicResult{
	private String code;
	private String message;
	private String name;
	private String creationDate;
	private String recCount;
	private String type;
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", name=" + name + ", createionDate=" + creationDate
				+ ", recCount=" + recCount + ", type=" + type + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getRecCount() {
		return recCount;
	}
	public void setRecCount(String recCount) {
		this.recCount = recCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDetails() {
		return String.format("[name=%s, creationDate=%s, recCount=%s, type=%s]", name, creationDate, recCount, type);
	}
}
