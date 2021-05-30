package com.trs.ckm.api.pojo;

import java.util.List;

public class RdfResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<RdfResultInfo> rdfResultInfo;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return String.format("[rdfResultInfo=%s]", rdfResultInfo);
	}
	
	public List<RdfResultInfo> getRdfResultInfo(){
		return rdfResultInfo;
	}
	
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", rdfResultInfo=" + rdfResultInfo + "]";
	}


	public static class RdfResultInfo{
		private String entity;
		private String feature;
		private String relationship;
		private String sen;
		private String idSen;
		private String entityType;
		private String featureType;
		private String dValue;
		@Override
		public String toString() {
			return "RdfResultInfo [entity=" + entity + ", feature=" + feature + ", relationship=" + relationship
					+ ", sen=" + sen + ", idSen=" + idSen + ", entityType=" + entityType + ", featureType="
					+ featureType + ", dValue=" + dValue + "]";
		}
		public String getEntity() {
			return entity;
		}
		public void setEntity(String entity) {
			this.entity = entity;
		}
		public String getFeature() {
			return feature;
		}
		public void setFeature(String feature) {
			this.feature = feature;
		}
		public String getRelationship() {
			return relationship;
		}
		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}
		public String getSen() {
			return sen;
		}
		public void setSen(String sen) {
			this.sen = sen;
		}
		public String getIdSen() {
			return idSen;
		}
		public void setIdSen(String idSen) {
			this.idSen = idSen;
		}
		public String getEntityType() {
			return entityType;
		}
		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}
		public String getFeatureType() {
			return featureType;
		}
		public void setFeatureType(String featureType) {
			this.featureType = featureType;
		}
		public String getdValue() {
			return dValue;
		}
		public void setdValue(String dValue) {
			this.dValue = dValue;
		}
	}
	
}
