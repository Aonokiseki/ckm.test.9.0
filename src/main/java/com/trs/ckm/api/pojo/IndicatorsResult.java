package com.trs.ckm.api.pojo;

import java.util.List;

public class IndicatorsResult implements CkmBasicResult {

	private String code;
	private String message;
	private List<IndicatorsInfo> indicatorsInfoList;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public List<IndicatorsInfo> getIndicatorsInfoList(){
		return indicatorsInfoList;
	}
	
	public String getDetails() {
		return String.format("[indicatorsInfoList=%s]", indicatorsInfoList);
	}

	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, indicatorsInfoList=%s]", code, message, indicatorsInfoList);
	}

	public static class IndicatorsInfo {
		private String numMin;
		private String numMax;
		private String unitMin;
		private String unitMax;
		private String des;
		private String entity;
		private String property;
		private String indicator;
		private String formatTag;
		private String numMinFormat;
		private String numMaxFormat;
		private String unitFormat;
		private String pos;
		private String len;

		public String getNumMin() {
			return numMin;
		}

		public String getNumMax() {
			return numMax;
		}

		public void setNumMax(String numMax) {
			this.numMax = numMax;
		}

		public String getUnitMin() {
			return unitMin;
		}

		public void setUnitMin(String unitMin) {
			this.unitMin = unitMin;
		}

		public String getUnitMax() {
			return unitMax;
		}

		public void setUnitMax(String unitMax) {
			this.unitMax = unitMax;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public String getEntity() {
			return entity;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getIndicator() {
			return indicator;
		}

		public void setIndicator(String indicator) {
			this.indicator = indicator;
		}

		public String getFormatTag() {
			return formatTag;
		}

		public void setFormatTag(String formatTag) {
			this.formatTag = formatTag;
		}

		public String getNumMinFormat() {
			return numMinFormat;
		}

		public void setNumMinFormat(String numMinFormat) {
			this.numMinFormat = numMinFormat;
		}

		public String getNumMaxFormat() {
			return numMaxFormat;
		}

		public void setNumMaxFormat(String numMaxFormat) {
			this.numMaxFormat = numMaxFormat;
		}

		public String getUnitFormat() {
			return unitFormat;
		}

		public void setUnitFormat(String unitFormat) {
			this.unitFormat = unitFormat;
		}

		public String getPos() {
			return pos;
		}

		public void setPos(String pos) {
			this.pos = pos;
		}

		public String getLen() {
			return len;
		}

		public void setLen(String len) {
			this.len = len;
		}

		public void setNumMin(String numMin) {
			this.numMin = numMin;
		}

		@Override
		public String toString() {
			return "[numMin=" + numMin + ", numMax=" + numMax + ", unitMin=" + unitMin + ", unitMax="
					+ unitMax + ", des=" + des + ", entity=" + entity + ", property=" + property + ", indicator="
					+ indicator + ", formatTag=" + formatTag + ", numMinFormat=" + numMinFormat + ", numMaxFormat="
					+ numMaxFormat + ", unitFormat=" + unitFormat + ", pos=" + pos + ", len=" + len + "]";
		}

	}

}
