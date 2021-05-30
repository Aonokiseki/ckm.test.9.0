package com.trs.ckm.test.function;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.DLAbsResult;
import com.trs.ckm.api.pojo.DLAllModelResult;
import com.trs.ckm.api.pojo.DLCatResult;
import com.trs.ckm.api.pojo.DLCollateResult;
import com.trs.ckm.api.pojo.DLEventResult;
import com.trs.ckm.api.pojo.DLMLEmojiExtResult;
import com.trs.ckm.api.pojo.DLMLEmojiExtResult.Result;
import com.trs.ckm.api.pojo.DLMLEmojiStripErrResult;
import com.trs.ckm.api.pojo.DLMLEmojiStripResult;
import com.trs.ckm.api.pojo.DLNerResult;
import com.trs.ckm.api.pojo.DLNreResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.HttpOperator;
import com.trs.ckm.util.Other;

/* 2020-12-01: 深度学习接口返回的code和message要和普通接口统一啦
 * 情况1: code=1 message=操作成功
 * 情况2: code=0 message=[各种各样出错原因]
 * 情况3: code=-1 message=结果为空！ 
 * 除此之外再无其他 
 * */
@Component
public class DeepLearningTest {
	private final static Logger LOGGER = LogManager.getLogger();
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	/**
	 * 从上下文中获取 <code>com.trs.ckm.api.TRSCkmRequest</code> 的bean,<br>
	 * 定位 log4j2.xml 的路径并重新加载配置
	 */
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
	
	@DataProvider(name="dlabsDataProvider")
	private Object[][] dlabsDataProvider(Method method){
		Object[][] result = null;
		if("dlAbs".equals(method.getName())) {
			result = new Object[][] {
				/* 示例文本 */
				new Object[] {1, "bert_abs", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/demo.txt"},
				/* 短文本 */
				new Object[] {2, "bert_abs", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/shortContent.txt"},
				/* 纯符号 */
				new Object[] {3, "bert_abs", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/symbol.txt"},
				/* 长文本 */
				new Object[] {4, "bert_abs", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/long.txt"},
				/* 英文 */
				new Object[] {5, "sum_en", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/english.txt"},
				/* 英文模板 + 中文文本, 是有内容的, 这就可以了. 因为不要求内容正确 */
				new Object[] {6, "sum_en", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlabs/demo.txt"}
			};
		}
		return result;
	}
	/**
	 * 深度学习摘要抽取测试
	 * @param caseId
	 * @param model
	 * @param filePath
	 */
	@Test(dataProvider="dlabsDataProvider")
	public void dlAbs(int caseId, String model, String code, String message, String filePath) {
		LOGGER.info("dlAbs, case="+caseId);
		try {
			String text = FileOperator.read(filePath);
			DLAbsResult result = request.dlabs(text, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			assertNotNull(result.getResults().getText());
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	
	@DataProvider(name="dlAbsErrDataProvider")
	private Object[][] dlAbsErrDataProvider(Method method){
		Object[][] result = null;
		if("dlAbsErr".equals(method.getName())) {
			result = new Object[][] {
				/* 空文本 */
				new Object[] {1, "bert_abs", Constant.TEST_DATA_DIR + "/dlabs/empty.txt", 
						Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
				/* 没有模板 */
				new Object[] {2, "", Constant.FAILURE, Constant.TEST_DATA_DIR + "/dlabs/demo.txt", 
						"0 (系统找不到指定的文件。)"},
			};
		}
		return result;
	}
	/**
	 * 深度学习摘要抽取测试-预期抛出异常
	 * @param caseId
	 * @param model
	 * @param filePath
	 */
	@Test(dataProvider="dlAbsErrDataProvider")
	public void dlAbsErr(int caseId, String model, String filePath, String code, String expectation) {
		LOGGER.info("dlAbsErr, case="+caseId);
		try {
			String text = FileOperator.read(filePath);
			DLAbsResult result = request.dlabs(text, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), expectation);
		}catch(Exception e){
			assertEquals(e.getLocalizedMessage(), expectation);
		}
	}
	/**
	 * 验证获取已加载的模型
	 */
	@Test
	public void dlAllModel() {
		try {
			DLAllModelResult result = request.dlAllModel();
			assertEquals(result.getCode(), Constant.SUCCESS);
			assertEquals(result.getMessage(), Constant.SUCCESS_MSG);
		} catch (IOException e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name="dlcatDataProvider")
	private Object[][] dlcatProvider(Method method){
		Object[][] result = null;
		if("dlcat".equals(method.getName())) {
			result = new Object[][] {
				/* 中文文本 */
				new Object[] {1, "cat_demo", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlcat/demo.txt"},
				new Object[] {2, "cat_demo", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlcat/long.txt"},
				new Object[] {3, "cat_demo", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlcat/short.txt"},
				/* 英文文本 */
				new Object[] {4, "cat_demo", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlcat/english.txt"},
				/* 纯符号 */
				new Object[] {5, "cat_demo", Constant.SUCCESS, Constant.SUCCESS_MSG, 
						Constant.TEST_DATA_DIR + "/dlcat/symbol.txt"},
			};
		}
		return result;
	}
	/**
	 * 深度学习接口 自动分类
	 * @param caseId
	 * @param model
	 * @param filePath
	 * @param expectation
	 */
	@Test(dataProvider = "dlcatDataProvider")
	public void dlcat(int caseId, String model, String code, String message, String filePath) {
		LOGGER.info("dlcat, case="+caseId);
		try {
			String text = FileOperator.read(filePath);
			DLCatResult result = request.dlcat(text, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			List<List<String>> results = result.getResults();
			assertTrue(results.size() > 0);
		} catch (IOException e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dlcatErrDataProvider")
	private Object[][] dlcatErrDataProvider(Method method){
		Object[][] result = null;
		if("dlcatErr".equals(method.getName())) {
			result = new Object[][] {
				/* 不存在的模板 */
				new Object[] {1, "nullModel", Constant.TEST_DATA_DIR + "/dlcat/demo.txt", Constant.FAILURE, 
						"not find model"},
				/* 空模板 */
				new Object[] {2, "", Constant.TEST_DATA_DIR + "/dlcat/demo.txt", Constant.FAILURE, "404 Not Found"},
			    /* 空文本 */
				new Object[] {3, "cat_demo", Constant.TEST_DATA_DIR + "/dlcat/empty.txt", 
						Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
			};
		}
		return result;
	}
	/**
	 * 深度学习自动分类，预期异常的验证
	 * @param caseId
	 * @param model
	 * @param filePath
	 * @param expectation
	 */
	@Test(dataProvider = "dlcatErrDataProvider")
	public void dlcatErr(int caseId, String model, String filePath, String code, String expectation) {
		LOGGER.info("dlcatErr, case="+caseId);
		try {
			String text = FileOperator.read(filePath);
			DLCatResult result = request.dlcat(text, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), expectation);
		} catch (Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name="dleventDataProvider")
	private Object[][] dleventDataProvider(Method method){
		Object[][] result = null;
		if("dlevent".equals(method.getName())) {
			result = new Object[][] {
				/* 示例文本 */
				new Object[] {1, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/demo.txt", 
						Constant.SUCCESS, Constant.SUCCESS_MSG},
				/* 长文本 */
				new Object[] {2, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/long.txt",
						Constant.SUCCESS, Constant.SUCCESS_MSG},
				/* 短文本 */
				new Object[] {3, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/short.txt",
						Constant.SUCCESS, Constant.SUCCESS_MSG},
				/* 英文 */
				new Object[] {4, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/english.txt",
						Constant.SUCCESS, Constant.SUCCESS_MSG},
			};
			
		}
		return result;
	}
	/**
	 * 深度学习 事件抽取
	 * @param caseId
	 * @param model
	 * @param filePath
	 */
	@Test(dataProvider="dleventDataProvider")
	public void dlevent(int caseId, String model, String filePath, String code, String message) {
		LOGGER.info("dlevent, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLEventResult result = request.dlevent(content, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			assertTrue(result.getAtomeventRes().get(0).getEventTags().size() > 0);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}	
	}
	
	@DataProvider(name="dleventErrDataProvider")
	private Object[][] dleventErrDataProvider(Method method){
		Object[][] result = null;
		if("dleventErr".equals(method.getName())) {
			result = new Object[][] {
				/* 全符号文本, 这个应该返回code=-1, message=结果为空！  */
				new Object[] {1, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/symbol.txt", 
						Constant.EMPTY, Constant.RESULT_IS_EMPTY},
				/* 空文本 */
				new Object[] {2, "bert_event", Constant.TEST_DATA_DIR + "/dlevent/empty.txt", 
						Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
				/* 空模板 */
				new Object[] {3, "", Constant.TEST_DATA_DIR + "/dlevent/demo.txt", Constant.FAILURE, 
						"404 Not Found"},
				/* 模板/模型不存在 */
				new Object[] {4, "xxx", Constant.TEST_DATA_DIR + "/dlevent/demo.txt", Constant.FAILURE, 
						"not find model"},
			};
		}
		return result;
	}
	/**
	 * 深度学习事件抽取异常情况
	 * @param caseId
	 * @param model
	 * @param filePath
	 * @param expectation
	 */
	@Test(dataProvider="dleventErrDataProvider")
	public void dleventErr(int caseId, String model, String filePath, String code, String expectation) {
		LOGGER.info("dleventErr, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLEventResult result = request.dlevent(content, model);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), expectation);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "dlMlEmojiExtDataProvider")
	private Object[][] dlMlEmojiExtDataProvider(Method method){
		if(!"dlMlEmojiExt".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 正常结果 */
			new Object[] {1, "出事了😂手机忘带555", Constant.SUCCESS, Constant.SUCCESS_MSG, 
					new String[] {"😂"}},
			new Object[] {2, "干得漂亮👍👍👍", Constant.SUCCESS, Constant.SUCCESS_MSG, 
					new String[] {"👍", "👍", "👍"}},
			new Object[] {3, "这张好看❤❤❤我已经动心了😍", Constant.SUCCESS, Constant.SUCCESS_MSG, 
					new String[] {"❤","❤","❤","😍"}},
			new Object[] {4, "😂😘😍😊😁", Constant.SUCCESS, Constant.SUCCESS_MSG, 
					new String[] {"😂", "😘", "😍", "😊", "😁"}},
			/* 不含Emoji的文本, 正常执行, 就是没有结果 */
			new Object[] {5, "中华人民共和国", Constant.EMPTY, Constant.RESULT_IS_EMPTY, new String[] {}}
		};
	}
	/**
	 * 深度学习 绘文字 提取
	 * @param caseId
	 * @param text
	 * @param expectation
	 */
	@Test(dataProvider = "dlMlEmojiExtDataProvider")
	public void dlMlEmojiExt(int caseId, String text, String code, String message, String[] expectation) {
		LOGGER.info("dlMlEmojiExt, case="+caseId);
		try {
			DLMLEmojiExtResult emojiExt = request.dlMlEmojiExt(text);
			assertEquals(emojiExt.getCode(), code);
			assertEquals(emojiExt.getMessage(), message);
			if(!Constant.SUCCESS.equals(emojiExt.getCode()))
				return;
			List<Result> results = emojiExt.getResults();
			assertEquals(results.size(), expectation.length);
			for(int i=0, size=results.size(); i<size; i++)
				assertEquals(results.get(i).getWord(), expectation[i]);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dlMlEmojiExtErrDataProvider")
	private Object[][] dlMlEmojiExtErrDataProvider(Method method){
		if(!"dlMlEmojiExtErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 空文本 */
			new Object[] {1, "", Constant.FAILURE, "text is null"},
		};
	}
	/**
	 * 深度学习 绘文字 提取 异常情况
	 * @param caseId
	 * @param text
	 * @param expectation
	 */
	@Test(dataProvider = "dlMlEmojiExtErrDataProvider")
	public void dlMlEmojiExtErr(int caseId, String text, String code, String expectation) {
		LOGGER.info("dlMlEmojiExtErr, case="+caseId);
		try {
			DLMLEmojiExtResult emojiExt = request.dlMlEmojiExt(text);
			assertEquals(emojiExt.getCode(), code);
			assertEquals(emojiExt.getMessage(), expectation);
		} catch (Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "dlMlEmojiStripDataProvider")
	private Object[][] dlMlEmojiStripDataProvider(Method method){
		if(!"dlMlEmojiStrip".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 正常情况 */
			new Object[] {1, "出事了😂手机忘带555", 
					Constant.SUCCESS, Constant.SUCCESS_MSG, "出事了手机忘带555"},
			new Object[] {2, "干得漂亮👍👍👍", Constant.SUCCESS, Constant.SUCCESS_MSG, "干得漂亮"},
			new Object[] {3, "这张好看❤❤❤我已经动心了😍", 
					Constant.SUCCESS, Constant.SUCCESS_MSG, "这张好看我已经动心了"},
			new Object[] {4, "😂😘😍😊😁", Constant.SUCCESS, Constant.SUCCESS_MSG, ""},
			/* 不含emoji表情 */
			new Object[] {5, "中华人民共和国", Constant.SUCCESS, Constant.SUCCESS_MSG, "中华人民共和国"}
		};
	}
	/**
	 * 深度学习 绘文字 删除
	 * @param caseId
	 * @param text
	 * @param expectation
	 */
	@Test(dataProvider = "dlMlEmojiStripDataProvider")
	public void dlMlEmojiStrip(int caseId, String text, String code, String message, String expectation) {
		LOGGER.info("dlMlEmojiStrip, case="+caseId);
		try {
			DLMLEmojiStripResult emojiStrip = request.dlMlEmojiStrip(text);
			assertEquals(emojiStrip.getCode(), code);
			assertEquals(emojiStrip.getMessage(), message);
			assertEquals(emojiStrip.getResults(), expectation);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dlMlEmojiStripErrDataProvider")
	private Object[][] dlMlEmojiStripErrDataProvider(Method method){
		if(!"dlMlEmojiStripErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 空文本 */
			new Object[] {1, "", Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY}
		};
	}
	/**
	 * 深度学习 绘文字 删除  异常情况
	 * @param caseId
	 * @param text
	 * @param expectation
	 */
	@Test(dataProvider = "dlMlEmojiStripErrDataProvider")
	public void dlMlEmojiStripErr(int caseId, String text, String code, String expectation) {
		LOGGER.info("dlMlEmojiStripErr, case="+caseId);
		try {
			/* 2020.8.31: 成功的时候系统返回一个字符串, 失败的时候返回"[]", 
			 * 会被Gson框架认为这是个数组导致和模板类型不匹配而抛出解析失败异常
			 * 为此方法临时创造一个模板再用Gson框架解析 */
			MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
			parameter.add("text", text);
			String json = HttpOperator.get().post(request.getHost() + "/rs/dl/ml/emoji_strip", parameter);
			Gson gson = new Gson();
			DLMLEmojiStripErrResult err = gson.fromJson(json, DLMLEmojiStripErrResult.class);
			assertEquals(err.getCode(), code);
			assertEquals(err.getMessage(), expectation);
		} catch (Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "dlnerDataProvider")
	private Object[][] dlnerDataProvider(Method method){
		if(!"dlner".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlner/demo.txt", "ner_demo", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlner/long.txt", "ner_demo", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			new Object[] {3, Constant.TEST_DATA_DIR + "/dlner/shortContent.txt", "ner_demo", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
		};
	}
	/**
	 * 深度学习 实体识别
	 * @param caseId
	 * @param filePath
	 * @param model
	 */
	@Test(dataProvider = "dlnerDataProvider")
	public void dlner(int caseId, String filePath, String model, String code, String message) {
		LOGGER.info("dlner, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLNerResult dlner = request.dlner(content, model);
			assertEquals(dlner.getCode(), code);
			assertEquals(dlner.getMessage(), message);
			assertFalse(dlner.getResults().isEmpty());
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dlnerErrDataProvider")
	private Object[][] dlnerErrDataProvider(Method method){
		if(!"dlnerErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 中文模型提取英文实体, 结果为空 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlner/english.txt", "ner_demo", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},
			/* 纯符号文本, 结果为空 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlner/symbol.txt", "ner_demo", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},
			/* 空文本, 抛出异常*/
			new Object[] {3, Constant.TEST_DATA_DIR + "/dlner/empty.txt", "ner_demo", 
					Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
			/* 空模板, 抛出异常 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/dlner/demo.txt", "", 
					Constant.FAILURE, "404 Not Found"}
		};
	}
	/**
	 * 深度学习 实体识别 异常情况
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param expectation
	 */
	@Test(dataProvider = "dlnerErrDataProvider")
	public void dlnerErr(int caseId, String filePath, String model, String code, String expectation) {
		LOGGER.info("dlnerErr, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLNerResult dlner = request.dlner(content, model);
			assertEquals(dlner.getCode(), code);
			assertEquals(dlner.getMessage(), expectation);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "dlnreDataProvider")
	private Object[][] dlnreDataProvider(Method method){
		if(!"dlnre".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 短文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlnre/1.txt", "bert_nre_en2", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			/* 长文本 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlnre/english.txt", "bert_nre_en2", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
		};
	}
	/**
	 * 深度学习 关系抽取
	 * @param caseId
	 * @param filePath
	 * @param model
	 */
	@Test(dataProvider = "dlnreDataProvider")
	public void dlnre(int caseId, String filePath, String model, String code, String message) {
		LOGGER.info("dlnre, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLNreResult dlnre = request.dlnre(content, model);
			assertEquals(dlnre.getCode(), code);
			assertEquals(dlnre.getMessage(), message);
			assertNotNull(dlnre.getResult());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name="dlnreErrDataProvider")
	private Object[][] dlnreErrDataProvider(Method method){
		if(!"dlnreErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 空文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlnre/empty.txt", "bert_nre_en2", 
					Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
			/* 中文文本, 英文模型 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlnre/demo.txt", "bert_nre_en2", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},
			/* 模型为空 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/dlnre/english.txt", "", 
					Constant.FAILURE, "404 Not Found"},
			/* 纯符号文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/dlnre/symbol.txt", "bert_nre_en2", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY}
		};
	}
	/**
	 * 深度学习 关系抽取 异常情况
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param expectation
	 */
	@Test(dataProvider="dlnreErrDataProvider")
	public void dlnreErr(int caseId, String filePath, String model, String code, String expectation) {
		LOGGER.info("dlnreErr, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLNreResult dlnre = request.dlnre(content, model);
			assertEquals(dlnre.getCode(), code);
			assertEquals(dlnre.getMessage(), expectation);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "dlcollateDataProvider")
	private Object[][] dlcollateDataProvider(Method method){
		if(!"dlcollate".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 较长文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlcollate/1.txt", "bert_collate", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			/* 超短文本 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlcollate/2.txt", "bert_collate", 
					Constant.SUCCESS, Constant.SUCCESS_MSG},
			/* 政治性错误文本; */
			/* 2020-11-24: 此接口不识别此类错误,只识别单字错误(别字), 此接口允许返回空 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/dlcollate/3.txt", "bert_collate", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},
			/* 接上, 如果允许结果空, 纯符号文本也不会抛出异常, 就返回空结果 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/dlcollate/symbol.txt", "bert_collate", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},
			/* 英文同理, hello world, 故意写错其中一个字母, 也识别不出来的 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/dlcollate/english.txt", "bert_collate", 
					Constant.EMPTY, Constant.RESULT_IS_EMPTY},	
		};
	}
	
	/**
	 * 深度学习-文本校对
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param exception
	 */
	@Test(dataProvider="dlcollateDataProvider")
	public void dlcollate(int caseId, String filePath, String model, String code, String message) {
		LOGGER.info("dlcollate, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLCollateResult dlCollate = request.dlcollate(content, model);
			assertEquals(dlCollate.getCode(), code);
			assertEquals(dlCollate.getMessage(), message);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dlcollateErrDataProvider")
	private Object[][] dlcollateErrDataProvider(Method method){
		if(!"dlcollateErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 空文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/dlcollateErr/empty.txt", "bert_collate", 
					Constant.FAILURE, Constant.INPUT_PARAMETER_IS_EMPTY},
			/* 模板不存在 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/dlcollateErr/standard.txt", "unknown", 
					Constant.FAILURE, "not find model"},
			/* 空模板, 和其它接口又有点儿小差别, 其它接口返回 404 Not Found, 无伤大雅,算了  */
			new Object[] {3, Constant.TEST_DATA_DIR + "/dlcollateErr/standard.txt", "unknown", 
					Constant.FAILURE, "not find model"},
		};
	}
	/**
	 * 深度学习校对-异常情况
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param exception
	 */
	@Test(dataProvider = "dlcollateErrDataProvider")
	public void dlcollateErr(int caseId, String filePath, String model, String code, String exception) {
		LOGGER.info("dlcollateErr, case="+caseId);
		try {
			String content = FileOperator.read(filePath);
			DLCollateResult dlcollate = request.dlcollate(content, model);
			assertEquals(dlcollate.getCode(), code);
			assertEquals(dlcollate.getMessage(), exception);
		}catch(Exception e) {
			assertEquals(e.getMessage(), exception);
		}
	}
}
