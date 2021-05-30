package com.trs.ckm.api.pojo;

import java.util.List;

import com.trs.ckm.api.pojo.AppraiseEmotionResult.Sentence;
import com.trs.ckm.api.pojo.AutocatResult.Result;
import com.trs.ckm.api.pojo.ExtExtractResult.Slot;
import com.trs.ckm.api.pojo.PloResult.Entity;
import com.trs.ckm.api.pojo.RulecatSimpleResult.CatResult;

public class BatchResult implements CkmBasicResult{

	private String code;
	private String message;
	private List<ComplexResult> result;
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return String.format("result=%s", result);
	}
	public List<ComplexResult> getResult(){
		return result;
	}
	@Override
	public String toString() {
		return "BatchResult [code=" + code + ", message=" + message + ", result=" + result + "]";
	}

	public static class ComplexResult{
		
		private String code;
		private String message;
		/* rulecat接口, 分类结果 */
		private List<CatResult> catResults;
		/* 褒贬分析, 总分 */
		private String totalScore;
		/* 褒贬分析, 句子 */
		private List<Sentence> sentences;
		/* 摘要接口, 摘要 */
		private String abs;
		/* 摘要接口, 关键词 */
		private List<String> wordlist;
		/* plo接口, 实体清单 */
		private List<Entity> entities;
		/* autocat接口, 分类结果 */
		private List<Result> results;
		/* 信息抽取接口  */
		private List<Slot> slots;
		/* 相似索引接口, revNum */
		private String revNum;
		/* 相似索引接口, 阈值 */
		private String threshold;
		/* 相似索引接口, 相似语句 */
		private String revDoc;
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("[code=").append(code).append(", message=").append(message);
			if(catResults != null && !catResults.isEmpty())
				result.append(", catResults=").append(catResults);
			if(totalScore != null && !totalScore.isEmpty())
				result.append(", totalScore=").append(totalScore);
			if(sentences != null && !sentences.isEmpty())
				result.append(", sentences=").append(sentences);
			if(abs != null && !abs.isEmpty())
				result.append(", abs=").append(abs);
			if(wordlist != null && !wordlist.isEmpty())
				result.append(", wordlist=").append(wordlist);
			if(entities != null && !entities.isEmpty())
				result.append(", entities=").append(entities);
			if(results != null && !results.isEmpty())
				result.append(", results=").append(results);
			if(slots != null && !slots.isEmpty())
				result.append(", slots=").append(slots);
			if(revNum != null && !revNum.isEmpty())
				result.append(", revNum=").append(revNum);
			if(threshold != null && !threshold.isEmpty())
				result.append(", threshold=").append(threshold);
			if(revDoc != null && revDoc.isEmpty())
				result.append(", revDoc=").append(revDoc);
			result.append("]");
			return result.toString();
		}
		public String getCode() {
			return code;
		}
		public String getMessage() {
			return message;
		}
		public List<CatResult> getCatResults() {
			return catResults;
		}
		public String getTotalScore() {
			return totalScore;
		}
		public List<Sentence> getSentences() {
			return sentences;
		}
		public String getAbs() {
			return abs;
		}
		public List<String> getWordlist() {
			return wordlist;
		}
		public List<Entity> getEntities() {
			return entities;
		}
		public List<Result> getResults() {
			return results;
		}
		public List<Slot> getSlots() {
			return slots;
		}
		public String getRevNum() {
			return revNum;
		}
		public String getThreshold() {
			return threshold;
		}
		public String getRevDoc() {
			return revDoc;
		}
	}
}
