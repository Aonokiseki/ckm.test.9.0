package com.trs.ckm.api.pojo;

public class ClusterGraphResult implements CkmBasicResult{
	private String code;
	private String message;
	private String taskId;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[taskId=%s]", taskId);
	}
	public String getTaskId() {
		return taskId;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", taskId=" + taskId + "]";
	}
}
