package com.trs.ckm.api.pojo;

import java.util.List;

public class SimModelAllResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Info> info;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public List<Info> getInfo() {
		return info;
	}

	public String getDetails() {
		return String.format("[info=%s]", info);
	}
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", info=" + info + "]";
	}

	public static class Info{
		private String name;
		private String creationDate;
		private String recCount;
		private String type;
		@Override
		public String toString() {
			return "[name=" + name + ", creationDate=" + creationDate + ", recCount=" + recCount + ", type=" + type
					+ "]";
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
	}
}
