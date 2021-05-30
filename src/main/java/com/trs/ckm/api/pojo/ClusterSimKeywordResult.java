package com.trs.ckm.api.pojo;

import java.util.List;
import java.util.Map;

public class ClusterSimKeywordResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<Result> result;
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	public List<Result> getResult() {
		return result;
	}
	@Override
	public String getDetails() {
		return result == null ? "[]" : result.toString();
	}
	@Override
	public String toString() {
		return "ClusterSimKeywordResult [code=" + code + ", message=" + message + ", result=" + result + "]";
	}
	public static class Result{
		private String entities;
		private String keywords;
		private List<Member> memberList;
		private String memberSize;
		private String shortTitle;
		
		public String getEntities() {
			return entities;
		}
		public String getKeywords() {
			return keywords;
		}
		public List<Member> getMemberList() {
			return memberList;
		}
		public String getMemberSize() {
			return memberSize;
		}
		public String getShortTitle() {
			return shortTitle;
		}
		@Override
		public String toString() {
			return "Result [entities=" + entities + ", keywords=" + keywords + ", memberList=" + memberList
					+ ", memberSize=" + memberSize + ", shortTitle=" + shortTitle + "]";
		}
		public static class Member{
			private Map<String,String> colMap;
			private String id;
			private String sortWeight;
			private String title;
			public Map<String, String> getColMap() {
				return colMap;
			}
			public String getId() {
				return id;
			}
			public String getSortWeight() {
				return sortWeight;
			}
			public String getTitle() {
				return title;
			}
			@Override
			public String toString() {
				return "Member [colMap=" + colMap + ", id=" + id + ", sortWeight=" + sortWeight + ", title=" + title
						+ "]";
			}
		}
	}
}
