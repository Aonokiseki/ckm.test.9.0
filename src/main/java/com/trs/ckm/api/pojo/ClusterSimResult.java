package com.trs.ckm.api.pojo;

import java.util.List;
import java.util.Map;

public class ClusterSimResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> result;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	public List<Result> getResult(){
		return result;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", result=" + result + "]";
	}


	public static class Result{
		private String shortTitle;
		private List<Member> memberList;
		private String memberSize;	
		@Override
		public String toString() {
			return "[shortTitle=" + shortTitle + ", memberList=" + memberList + ", memberSize=" + memberSize
					+ "]";
		}
		public String getShortTitle() {
			return shortTitle;
		}
		public void setShortTitle(String shortTitle) {
			this.shortTitle = shortTitle;
		}
		public List<Member> getMemberList() {
			return memberList;
		}
		public void setMemberList(List<Member> memberList) {
			this.memberList = memberList;
		}
		public String getMemberSize() {
			return memberSize;
		}
		public void setMemberSize(String memberSize) {
			this.memberSize = memberSize;
		}

		public static class Member{
			private String id;
			private String title;
			private Map<String,String> colMap;
			private String sortWight;
			@Override
			public String toString() {
				return "[id=" + id + ", title=" + title + ", colMap=" + colMap + ", sortWight=" + sortWight
						+ "]";
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public Map<String, String> getColMap() {
				return colMap;
			}
			public void setColMap(Map<String, String> colMap) {
				this.colMap = colMap;
			}
			public String getSortWight() {
				return sortWight;
			}
			public void setSortWight(String sortWight) {
				this.sortWight = sortWight;
			}
		}
	}
}
