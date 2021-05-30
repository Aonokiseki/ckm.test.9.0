package com.trs.ckm.api.pojo;

import java.util.List;

public class ExtExtractResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private List<Slot> slots;
	private String trsString;
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return String.format("[slots=%s, trsString=%s]", slots, trsString);
	}
	
	public List<Slot> getSlots(){
		return slots;
	}
	
	public String getTrsString() {
		return trsString;
	}
	
	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + ", slots=" + slots + ", trsString=" + trsString + "]";
	}

	public static class Slot{
		private String lex;
		private String typeName;
		private String slotName;
		private String typeId;
		private String relationId;
		private String wrapperTag;
		private String rowPos;
		private String colPos;
		private String pos;
		private String param;
		@Override
		public String toString() {
			return "[lex=" + lex + ", typeName=" + typeName + ", slotName=" + slotName + ", typeId=" + typeId
					+ ", relationId=" + relationId + ", wrapperTag=" + wrapperTag + ", rowPos=" + rowPos + ", colPos="
					+ colPos + ", pos=" + pos + ", param=" + param + "]";
		}
		public String getLex() {
			return lex;
		}
		public void setLex(String lex) {
			this.lex = lex;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public String getSlotName() {
			return slotName;
		}
		public void setSlotName(String slotName) {
			this.slotName = slotName;
		}
		public String getTypeId() {
			return typeId;
		}
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}
		public String getRelationId() {
			return relationId;
		}
		public void setRelationId(String relationId) {
			this.relationId = relationId;
		}
		public String getWrapperTag() {
			return wrapperTag;
		}
		public void setWrapperTag(String wrapperTag) {
			this.wrapperTag = wrapperTag;
		}
		public String getRowPos() {
			return rowPos;
		}
		public void setRowPos(String rowPos) {
			this.rowPos = rowPos;
		}
		public String getColPos() {
			return colPos;
		}
		public void setColPos(String colPos) {
			this.colPos = colPos;
		}
		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}
		public String getParam() {
			return param;
		}
		public void setParam(String param) {
			this.param = param;
		}
	}

}
