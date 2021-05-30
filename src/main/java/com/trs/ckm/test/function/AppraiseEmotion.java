package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.AppraiseEmotionResult;
import com.trs.ckm.api.pojo.AppraiseEmotionResult.Sentence;
import com.trs.ckm.api.pojo.AppraiseFTResult;
import com.trs.ckm.api.pojo.AppraiseParserResult;
import com.trs.ckm.api.pojo.AppraiseParserResult.AppraiseRes;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class AppraiseEmotion {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger();
	
	@BeforeClass
	public void beforeClass() {
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		Constant.reconfigureLog4j2();
		request = context.getBean(TRSCkmRequest.class);
	}
	
	@AfterClass
	public void afterClass() {
		if(context != null)
			context.close();
	}
	
	@DataProvider(name = "AppraiseEmotionDataProvider")
	public Object[][] appraiseEmotionDataProvider(Method method){
		if(!"appraiseEmotion".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 中文模板 , 短文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseEmotion/1.txt", "appraise_general",
					"1", "操作成功", "-10.0", new String[] {"-2.0", "-2.0", "-2.0"},
					new String[] {"地震", "杀人", "谋杀"}},
			/* 示例 文本 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseEmotion/2.txt", "appraise_general",
					"1", "操作成功", "10.0", 
					new String[] {"-1.0","-1.0","-1.0","-1.0","-1.0","-1.0",
							"-1.0","-1.0","-1.0","-1.0", "-1.0", "-1.0"},
					new String[] {"热烈","良好","发展","加强","全面","发展",
							"亲切","亲切","重视","充满","信心","提升"}},
			/* 示例文本中添加部分干扰词 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseEmotion/3.txt", "appraise_general",
					"1", "操作成功", "7.16", 
					new String[] {"-2.0","-2.0","-1.0","-1.0","-1.0","-1.0","-1.0",
							"-1.0","-1.0","-1.0","-1.0","-1.0","-1.0","-1.0"},
                    new String[] {"坏人","坏蛋","热烈","良好","发展","加强","全面",
                    		"发展","亲切","亲切","重视","充满","信心","提升"}},
			/* 长文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseEmotion/4.txt", "appraise_general", 
					"1", "操作成功", "-5.71", 
					new String[] {"-1.0","-2.0","-2.0","-1.0","-1.0","-1.0","-2.0","-2.0",
							"-1.0","-1.0","-1.0","-2.0","-2.0","-1.0","-1.0","-1.0","-2.0","-2.0","-1.0","-1.0",
							"-1.0","-2.0","-2.0","-1.0","-1.0","-1.0","-2.0","-2.0","-1.0","-1.0","-1.0","-2.0",
							"-2.0","-1.0","-1.0","-1.0","-2.0","-2.0","-1.0","-1.0","-1.0","-2.0","-2.0","-1.0",
							"-1.0","-1.0","-2.0","-2.0","-1.0","-1.0"},
					new String[] {"买","混乱","缺乏","认可","大","买","混乱","缺乏","认可","大","买","混乱","缺乏",
							"认可","大","买","混乱","缺乏","认可","大","买","混乱","缺乏","认可","大","买","混乱","缺乏",
							"认可","大","买","混乱","缺乏","认可","大","买","混乱","缺乏","认可","大","买","混乱","缺乏",
							"认可","大","买","混乱","缺乏","认可","大"}
					},
			/* 英文短文本 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseEmotion/5.txt", "appraise_general_en",
					"1", "操作成功", "-10.0", new String[] {"-2.0"}, new String[] {"murderer"}},
			/* 英文中文本 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseEmotion/6.txt", "appraise_general_en",
					"1", "操作成功", "10.0", 
					new String[] {"-1.0","-1.0","-1.0","-1.0","-1.0","-1.0","-1.0","-1.0",
							"-1.0","-1.0","-1.0","-1.0","-1.0"},
					new String[] {"warm","congratulations","good","wishes","strengthen","promote",
							"promote","comprehensive","greetings","happy","great","importance","confidence"}},
			/* 英文长文本 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/appraiseEmotion/7.txt", "appraise_general_en",
					"1", "操作成功", "8.55", 
					new String[] {"-1.0","-1.0","-1.0","-2.0","-1.0","-1.0","-1.0","-1.0",
							"-1.0","-2.0","-1.0","-2.0","-1.0","-1.0","-1.0","-1.0"},
					new String[] {"boost","help","ease","pressure","increase","boost","super",
							"help","ease","pressure","increase","problems","flagship","solid","share","excited"}},
			new Object[] {8, Constant.TEST_DATA_DIR + "/appraiseEmotion/8.txt", "appraise_general_en",
					"1", "操作成功", "-6.92", new String[] {"-2.0","-2.0","-2.0","-1.0","-1.0"},
					new String[] {"death","controversy","lack","strong","interest"}}
		};
	}
	/**
	 * 测试情感分析-褒贬分析
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param code 预期code
	 * @param message 预期message
	 * @param totalScore 预期情感总分
	 * @param scores 预期情感词序列
	 * @param words 预期情感词分序列
	 */
	@Test(dataProvider = "AppraiseEmotionDataProvider")
	public void appraiseEmotion(int caseId, String filePath, String model, 
			String code, String message, String totalScore, String[] scores, String[] words) {
		LOGGER.info("appraiseEmotion, caseId="+caseId);
		try {
			String text = FileOperator.read(filePath);
			AppraiseEmotionResult appraiseEmotion = request.appraiseEmotion(text, model);
			assertEquals(appraiseEmotion.getCode(), code);
			assertEquals(appraiseEmotion.getMessage(), message);
			assertEquals(appraiseEmotion.getTotalScore(), totalScore);
			List<Sentence> sentences = appraiseEmotion.getSentences();
			assertEquals(sentences.size(), scores.length);
			Sentence sentence;
			for(int i=0, size=sentences.size(); i<size; i++) {
				sentence = sentences.get(i);
				assertEquals(sentence.getScore(), scores[i]);
				assertEquals(sentence.getWord(), words[i]);
			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "appraiseEmotionErrDataProvider")
	public Object[][] appraiseEmotionErrDataProvider(Method method){
		if(!"appraiseEmotionErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 韩文, 中文模板无结果 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseEmotionErr/1.txt", "appraise_general", "-1"},
			/* 中韩混合, 中文模板有结果 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseEmotionErr/2.txt", "appraise_general", "1"},
			/* 韩文, 英文模板 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseEmotionErr/3.txt", "appraise_general_en", "-1"},
			/* 英韩混合, 英文模板有结果 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseEmotionErr/4.txt", "appraise_general_en", "1"},
			/* 模板不存在 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseEmotion/1.txt", "ceshi", "0"},
			/* 模板为空 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseEmotion/1.txt", "", "0"},
			/* 文本为空 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/appraiseEmotionErr/empty.txt", "appraise_general_en", "0"},
		};
	}
	/**
	 * 异常情况
	 * 测试情感分析 - 褒贬分析
	 * @param caseId
	 * @param path
	 * @param model
	 * @param code 预期code
	 */
	@Test(dataProvider = "appraiseEmotionErrDataProvider")
	public void appraiseEmotionErr(int caseId, String path, String model, String code) {
		LOGGER.debug("appraiseEmotionErr, caseId="+caseId);
		try {
			assertEquals(request.appraiseEmotion(FileOperator.read(path), model).getCode(), code);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "appraiseFtDataProvider")
	public Object[][] appraiseFtDataProvider(Method method){
		if(!"appraiseFt".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 英文中长文本, neg 负面 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseFt/1.txt", "ft_appraise_en", "1", "操作成功", "neg"},
			/* 长文本, neu 中性文本 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseFt/2.txt", "ft_appraise_en", "1", "操作成功", "neu"},
			/* 短文本, pos正面文本 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseFt/3.txt", "ft_appraise_en", "1", "操作成功", "pos"},
			/* 阿拉伯文 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseFt/4.txt", "ft_appraise_ar", "1", "操作成功", "neg"},
			/* 阿拉伯文长文本 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseFt/5.txt", "ft_appraise_ar", "1", "操作成功", "neu"},
			/* 阿拉伯文短文本 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseFt/6.txt", "ft_appraise_ar", "1", "操作成功", "pos"},
		};
	}
	/**
	 * 测试情感分析- 基于FastText的篇章级情感分类
	 * @param caseId
	 * @param path
	 * @param model
	 * @param code 预期code
	 * @param message 预期message
	 * @param result 预期类别
	 */
	/* 2021-02-02: 原先的测试数据没有中文模板, 只给了英文和阿拉伯文模板, 现在这两个模板都去掉了, 只有一个中文模板
	 * 在信创测试中, 先去掉这个测试方法, 改为手工检查, 待下次标准介质发布时再恢复 */
	@Test(dataProvider = "appraiseFtDataProvider")
	public void appraiseFt(int caseId, String path, String model, String code, String message, String result) {
		LOGGER.debug("appraiseFt, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			AppraiseFTResult appraiseFt = request.appraiseFt(text, model);
			assertEquals(appraiseFt.getCode(), code);
			assertEquals(appraiseFt.getMessage(), message);
			assertEquals(appraiseFt.getResult(), result);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "appraiseFtErrDataProvider")
	public Object[][] appraiseFtErrDataProvider(Method method){
		if(!"appraiseFtErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 英文模板, 中文文本, 正常返回 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseFtErr/1.txt", "ft_appraise_en", "1"},
			/* 英文模板, 中英文混合文本, 正常返回 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseFtErr/2.txt", "ft_appraise_en", "1"},
			/* 阿拉伯文模板, 中文文本 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseFtErr/3.txt", "ft_appraise_ar", "1"},
			/* 阿拉伯文模板, 中阿文本混合 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseFtErr/4.txt", "ft_appraise_ar", "1"},
			/* 模板 不存在 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseFtErr/5.txt", "ceshi", "0"},
			/* 模板为空 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseFtErr/6.txt", "", "0"},
		};
	}
	
	/**
	 * 异常情况
	 * 测试情感分析-基于FastText的篇章级情感分类
	 * @param caseId
	 * @param path
	 * @param model
	 * @param code
	 */
	@Test(dataProvider = "appraiseFtErrDataProvider")
	public void appraiseFtErr(int caseId, String path, String model, String code) {
		LOGGER.debug("appraiseFtErr, caseId="+caseId);
		try {
			AppraiseFTResult result = request.appraiseFt(FileOperator.read(path), model);
			assertEquals(result.getCode(), code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@DataProvider(name = "appraiseParserDataProvider")
	public Object[][] appraiseParserDataProvider(Method method){
		if(!"appraiseParser".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 英文 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/appraiseParser/1.txt", "1", "操作成功", "0.03181980852514251",
					new String[] {"endless","cold","forget","full","white","suitable","love","talk"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/appraiseParser/2.txt", "1", "操作成功", "-0.6717514421272202",
					new String[] {"share","prodigal"}},
			new Object[] {3, Constant.TEST_DATA_DIR + "/appraiseParser/3.txt", "1", "操作成功", "0.4300000071525574",
					new String[] {"bloom"}},
			/* 中文 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/appraiseParser/4.txt", "1", "操作成功", "0.47404641762739724",
					new String[] {"热烈", "良好", "感谢", "亲切", "重视"}},
			new Object[] {5, Constant.TEST_DATA_DIR + "/appraiseParser/5.txt", "1", "操作成功", "-0.5692209596981078",
					new String[] {"丑恶", "控诉", "不当", "损伤", "成功", "可耻", "优秀", "丑恶"}},
			new Object[] {6, Constant.TEST_DATA_DIR + "/appraiseParser/6.txt", "1", "操作成功", "0.498194971431905",
					new String[] {"受损","上涨","上涨","上涨","上涨","上涨","上涨"}}
		};
	}
	
	/**
	 * 测试情感分析, 基于句法的情感分析(中文, 英文)
	 * @param caseId 
	 * @param path 测试文件路径
	 * @param code 预期code
	 * @param message 预期message
	 * @param dValue 预期总情感分
	 * @param words 预期情感词序列
	 */
	@Test(dataProvider = "appraiseParserDataProvider")
	public void appraiseParser(int caseId, String path, String code, String message, String dValue, String[] words) {
		LOGGER.debug("appraiseParser, caseId="+caseId);
		String text;
		try {
			text = FileOperator.read(path);
			AppraiseParserResult result = request.appraiseParser(text);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
//			assertEquals(result.getDValue(), dValue);
			double dvalue = Double.valueOf(result.getDValue());
			assertTrue(dvalue >= -1.0 && dvalue <= 1.0);
			List<AppraiseRes> appraiseRes = result.getAppraiseRes();
			AppraiseRes appraiseResInstance;
			for(int i=0, size=appraiseRes.size(); i<size; i++) {
				appraiseResInstance = appraiseRes.get(i);
				assertEquals(appraiseResInstance.getWord(), words[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
