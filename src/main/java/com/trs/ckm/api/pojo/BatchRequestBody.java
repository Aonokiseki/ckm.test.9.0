package com.trs.ckm.api.pojo;

import java.util.List;
import java.util.Map;

public class BatchRequestBody {
	private String text;
	private List<Action> actions;
	
	public BatchRequestBody(String text, List<Action> actions) {
		this.text = text;
		this.actions = actions;
	}
	
	@Override
	public String toString() {
		return "[text=" + text + ", actions=" + actions + "]";
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	/**
	 * 批处理接口的单个行为<br>
	 * type: 调用的接口名, 参考手册<br>
	 * params: 调用接口时附加的参数, 也需要参考手册
	 */
	public static class Action{
		private String type;
		private Map<String,String> params;
		
		public Action(String type, Map<String,String> params) {
			this.type = type;
			this.params = params;
		}
		
		@Override
		public String toString() {
			return "[type=" + type + ", params=" + params + "]";
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
	}
}
