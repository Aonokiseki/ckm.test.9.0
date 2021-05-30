package com.trs.ckm.api.pojo;

public class EntityLinkResult implements CkmBasicResult{
	
	private String code;
	private String message;
	private Result result;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public Result getResult() {
		return result;
	}

	public String getDetails() {
		return String.format("[result=%s]", result);
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, result=%s]", code, message, result);
	}
	
	public static class Result{
		private String kbId;
		private String name;
		private String type;
		private String desc;
		private String offset;
		private String mention;
		
		public String getKbId() {
			return kbId;
		}
		public String getName() {
			return name;
		}
		public String getType() {
			return type;
		}
		public String getDesc() {
			return desc;
		}
		public String getOffset() {
			return offset;
		}
		public String getMention() {
			return mention;
		}
		@Override
		public String toString() {
			return String.format("[kbId=%s, name=%s, type=%s, desc=%s, offset=%s, mention=%s]", 
					kbId, name, type, desc, offset, mention);
		}
	}
	
}
