package com.trs.ckm.api.pojo;

import java.util.List;

public class EventQuickResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Result> result;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<Result> getResult() {
		return result;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", result=" + result + "]";
	}
	public String getDetails() {
		return "[result=" + result + "]";
	}

	public static class Result{
		private String desc;
		private List<String> persons;
		private List<String> orgs;
		private String type;
		private List<Tag> tags;
		
		@Override
		public String toString() {
			return "[desc=" + desc + ", persons=" + persons + ", orgs=" + orgs + ", type=" + type + ", tags="
					+ tags + "]";
		}
		
		public String getDesc() {
			return desc;
		}
		
		public List<String> getPersons() {
			return persons;
		}
		
		public List<String> getOrgs() {
			return orgs;
		}
		
		public String getType() {
			return type;
		}
		
		public List<Tag> getTags() {
			return tags;
		}
		
		public static class Tag{
			private String tagType;
			private String word;
			
			@Override
			public String toString() {
				return "[tagType=" + tagType + ", word=" + word + "]";
			}
			
			public String getTagType() {
				return tagType;
			}
			
			public String getWord() {
				return word;
			}
		}
	}
}
