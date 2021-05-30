package com.trs.ckm.api.pojo;

import java.util.List;

public class DLEventResult implements CkmBasicResult{
	private String code;
	private String message;
	private List<AtomEvent> atomeventRes;
	private String totalnum;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public List<AtomEvent> getAtomeventRes() {
		return atomeventRes;
	}

	public String getTotalnum() {
		return totalnum;
	}

	public String getDetails() {
		return String.format("[atomEventRes=%s, totalnum=%s]", atomeventRes, totalnum);
	}
	
	@Override
	public String toString() {
		return String.format("[code=%s, message=%s, atomEventRes=%s, totalnum=%s]", 
				code, message, atomeventRes, totalnum);
	}
	
	public static class AtomEvent{
		private String senTenceid;
		private List<String> docids;
		private String eventId;
		private String eventDescription;
		private String themeEventId;
		private String eventHotNum;
		private List<String> eventRelaPersons;
		private List<String> eventRelaOrgs;
		private String eventType;
		private List<EventTag> eventTags;
		private String eventSentence;
		private String eventTitle;
		private List<String> eventFields;
		private List<String> eventKeywords;
		private String eventFatherId;
		private String fatherRela;
		private String verbPos;
		private String subPos;
		private String objPos;
		private String fatherPos;
		
		public String getSenTenceid() {return senTenceid;}
		public List<String> getDocids() {return docids;}
		public String getEventId() {return eventId;}
		public String getEventDescription() {return eventDescription;}
		public String getEhemeEventId() {return themeEventId;}
		public String getEventHotNum() {return eventHotNum;}
		public List<String> getEventRelaPersons() {return eventRelaPersons;}
		public List<String> getEventRelaOrgs(){return eventRelaOrgs;}
		public String getEventType() {return eventType;}
		public List<EventTag> getEventTags(){return eventTags;}
		public String getEventSentence() {return eventSentence;}
		public String getEventTitle() {return eventTitle;}
		public List<String> getEventFields() {return eventFields;}
		public List<String> getEventKeywords() {return eventKeywords;}
		public String getEventFatherId() {return eventFatherId;}
		public String getFatherRela() {return fatherRela;}
		public String getVerboPos() {return verbPos;}
		public String getSubPos() {return subPos;}
		public String getObjPos() {return objPos;}
		public String getFatherPos() {return fatherPos;}
		
		@Override
		public String toString() {
			return String.format("[senTenceid=%s, docids=%s, eventId=%s, eventDescription=%s, themeEventId=%s, eventHotNum=%s, eventRelaPersons=%s"
					+ "eventRelaOrgs=%s, eventType=%s, eventTags=%s, eventSentence=%s, eventTitle=%s eventFields=%s, eventKeywords=%s, eventFatherId=%s"
					+ "fatherRela=%s, verbPos=%s, subPos=%s, objPos=%s, fatherPos=%s]", 
					senTenceid, docids, eventId, eventDescription, themeEventId, eventHotNum, eventRelaPersons, eventRelaOrgs, eventType, 
					eventTags, eventSentence, eventTitle, eventFields, eventKeywords, eventFatherId, fatherRela, verbPos, subPos, objPos, fatherPos);
		}
		
		public static class EventTag{
			private String oriWord;
			private String rewriteWord;
			private String desWord;
			private String wordType;
			private String tag;
			private String startPos;
			private String endPos;
			
			public String getOriWord() {return oriWord;}
			public String getRewriteWord() {return rewriteWord;}
			public String getDesWord() {return desWord;}
			public String getWordType() {return wordType;}
			public String getTag() {return tag;}
			public String getStartPos() {return startPos;}
			public String getEndPos() {return endPos;}
			
			@Override
			public String toString() {
				return String.format("[oriWord=%s, rewriteWord=%s, desWord=%s, wordType=%s, tag=%s, startPos=%s, endPos=%s]", 
						oriWord, rewriteWord, desWord, wordType, tag, startPos, endPos);
			}
		}
	}
}
