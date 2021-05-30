package com.trs.ckm.api.pojo;

import java.util.List;

public class ExtEntityResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<EntityItem> entityItemList;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("[entityItemList=%s]", entityItemList);
	}	
	public List<EntityItem> getEntityItemList(){
		return entityItemList;
	}
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", entityItemList=" + entityItemList + "]";
	}
	public static class EntityItem{
		private String person;
		private String country;
		private String org;
		private String career;
		private String pos;
		private List<String> entityPropertyList;
		private String tag;
		@Override
		public String toString() {
			return "[person=" + person + ", country=" + country + ", org=" + org + ", career=" + career
					+ ", pos=" + pos + ", entityPropertyList=" + entityPropertyList + ", tag=" + tag + "]";
		}
		public String getPerson() {
			return person;
		}
		public void setPerson(String person) {
			this.person = person;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getOrg() {
			return org;
		}
		public void setOrg(String org) {
			this.org = org;
		}
		public String getCareer() {
			return career;
		}
		public void setCareer(String career) {
			this.career = career;
		}
		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}
		public List<String> getEntityPropertyList() {
			return entityPropertyList;
		}
		public void setEntityPropertyList(List<String> entityPropertyList) {
			this.entityPropertyList = entityPropertyList;
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
	}

}
