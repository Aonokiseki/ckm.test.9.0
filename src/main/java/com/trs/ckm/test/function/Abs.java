package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.AbsLabelResult;
import com.trs.ckm.api.pojo.AbsResult;
import com.trs.ckm.api.pojo.AbsThemeResult;
import com.trs.ckm.api.pojo.AbsThemeResult.ThemeWord;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Abs {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger(Abs.class);
	private final static Map<String,String> READ_OPTIONS = new HashMap<String,String>();
	
	@BeforeClass
	public void beforeClass() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
		READ_OPTIONS.put("not.append.lineserparator", "true");
	}
	
	@AfterClass
	public void afterClass() {
		if(context != null)
			context.close();
	}
	
	@DataProvider(name = "absDataProvider")
	public Object[][] absDataProvider(Method method){
		if(!"abs".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 中文，主题词为1,2，为空，
			 * 摘要最少是1句话，不管你值设置是多大。 其次，如果第一句话+第二句话的字数大于了你设置的值，这样就不会出。
			 * 主题词为1，摘要长度为10，摘要长度占文章百分比为空 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/abs/case1.txt", "news-cn", "10", "1", "", 
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case1.txt", 1},
			/* 主题词为2，摘要长度为205，摘要长度占文章百分比为空 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/abs/case2.txt", "news-cn", "205", "2", "",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case2.txt", 2},
			/* 主题词为3，摘要长度为空，摘要长度占文章百分比为2 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/abs/case3.txt", "news-cn", "", "3", "2",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case3.txt", 3},
			/* 英语，主题词个数为4,6,10,摘要所占百分比为0,10,20
			 * 主题词为4，摘要长度为空，摘要长度占文章百分比为10 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/abs/case4.txt", "news-en", "", "4", "10",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case4.txt", 4},
			/* 主题词为6，同时设置摘要长度（500）和摘要长度占文章百分比（10%）
			 * 取参数中摘要字数最短的结果作为摘要结果，此处取摘要长度占文本10%的长度作为摘要结果 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/abs/case5.txt", "news-en", "500", "6", "10",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case5.txt", 6},
			/* 主题词为10，摘要长度为0，摘要长度占文章百分比为50
			 * 如果二者中任意一个不为零，为零的那个参数会取最大值，比如字数取的是全文的长度，百分比取的是100 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/abs/case6.txt", "news-en", "0", "10", "50",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case6.txt", 10},
			/* 德语，主题词个数为7,8,15,摘要所占百分比为0，10,20，摘要长度为10,50,100
			 * 主题词为7，摘要长度为0，摘要长度占文章百分比为0
			 * 同时为0的时候， news-de模板字数的默认值为300字符，百分比的默认值为50%   然后再取摘要字数短的作为摘要结果 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/abs/case7.txt", "news-de", "0", "7", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case7.txt", 7},
			/* 主题词为8，摘要长度为50，摘要长度占文章百分比为空 */
			new Object[] {8, Constant.TEST_DATA_DIR + "/abs/case8.txt", "news-de", "50", "8", "",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case8.txt", 8},
			/* 主题词为10，摘要长度为空，摘要长度占文章百分比为40 */
			new Object[] {9, Constant.TEST_DATA_DIR + "/abs/case9.txt", "news-de", "", "10", "40",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case9.txt", 10},
			/* 法语，主题词个数为11,20,50
			 * 主题词为11，同时设置摘要长度（100）和摘要长度占文章百分比（30%）
			 * 取参数中摘要字数最短的结果作为摘要结果，此处取摘要长度作为摘要结果 */
			new Object[] {10, Constant.TEST_DATA_DIR + "/abs/case10.txt", "news-fr", "100", "11", "30",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case10.txt", 11},
			/* 主题词为20，摘要长度为0，摘要长度占文章百分比为60
			 * 如果二者中任意一个不为零，为零的那个参数会取最大值，比如字数取的是全文的长度，百分比取的是100 */
			new Object[] {11, Constant.TEST_DATA_DIR + "/abs/case11.txt", "news-fr", "0", "20", "60",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case11.txt", 20},
			/* 主题词为50，摘要长度为0，摘要长度占文章百分比为0（
			 * 同时为0的时候， news-fr模板字数的默认值为300字符，百分比的默认值为50%  然后再取摘要字数短的作为摘要结果 */
			new Object[] {12, Constant.TEST_DATA_DIR + "/abs/case12.txt", "news-fr", "0", "50", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case12.txt", 50},
			/* 日语,主题词为23,74，70 主题词为23，摘要长度为300，摘要长度占文章百分比为空 */
			new Object[] {13, Constant.TEST_DATA_DIR + "/abs/case13.txt", "news-jp", "300", "23", "",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case13.txt", 23},
			/* 主题词为100，摘要长度为空，摘要长度占文章百分比为65 */
			new Object[] {14, Constant.TEST_DATA_DIR + "/abs/case14.txt", "news-jp", "", "74", "65",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case14.txt", 74},
			/* 主题词为70，同时设置摘要长度（500）和摘要长度占文章百分比（45%），取参数中摘要字数最短的结果作为摘要结果，
			 * 此处取摘要长度作为摘要结果 */
			new Object[] {15, Constant.TEST_DATA_DIR + "/abs/case15.txt", "news-jp", "500", "70", "45",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case15.txt", 70},
			/* 西班牙语，主题词为65,78,99
			 * 主题词为20，摘要长度为400，摘要长度占文章百分比为0
			 * 如果二者中任意一个不为零，为零的那个参数会取最大值，比如字数取的是全文的长度，百分比取的是100 */
			new Object[] {16, Constant.TEST_DATA_DIR + "/abs/case16.txt", "news-es", "400", "65", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case16.txt", 65},
			/* 主题词为50，摘要长度为0，摘要长度占文章百分比为0
			 * 同时为0的时候， news-es模板字数的默认值为300字符，百分比的默认值为50%   然后再取摘要字数短的作为摘要结果 */
			new Object[] {17, Constant.TEST_DATA_DIR + "/abs/case17.txt", "news-es", "0", "78", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case17.txt", 78},
			/* 主题词为99，摘要长度为800，摘要长度占文章百分比为空 */
			new Object[] {18, Constant.TEST_DATA_DIR + "/abs/case18.txt", "news-es", "800", "99", "",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case18.txt", 99},
			/* 俄语,主题词为90,33,56 主题词为90，摘要长度为空，摘要长度占文章百分比为80 */
			new Object[] {19, Constant.TEST_DATA_DIR + "/abs/case19.txt", "news-ru", "", "90", "80", 
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case19.txt", 90},
			/* 主题词为33，同时设置摘要长度（550）和摘要长度占文章百分比（50%），取参数中摘要字数最短的结果作为摘要结果，
			 * 此处取摘要长度作为摘要结果 */
			new Object[] {20, Constant.TEST_DATA_DIR + "/abs/case20.txt", "news-ru", "550", "33", "50",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case20.txt", 33},
			/* 主题词为56，摘要长度为200，摘要长度占文章百分比为0（如果二者中任意一个不为零，为零的那个参数会取最大值，
			 * 比如字数取的是全文的长度，百分比取的是100） */
			new Object[] {21, Constant.TEST_DATA_DIR + "/abs/case21.txt", "news-ru", "", "56", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case21.txt", 56},
			/* 阿语，主题词为34,9,42
			 * 主题词为34，摘要长度为0，摘要长度占文章百分比为0（同时为0的时候， 字数的默认值为300字符，百分比的默认值为50%   
			 * 然后再取摘要字数短的作为摘要结果）由于阿语的分句不确定是什么，所以无论摘要长度和摘要长度占百分比设置多少，
			 * 获取到的摘要都是相同的一段文本，开发说先这样 */
			new Object[] {22, Constant.TEST_DATA_DIR + "/abs/case22.txt", "news-ar", "0", "34", "0",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case22.txt", 34},
			/* 主题词为9，摘要长度为350，摘要长度占文章百分比为空 */
			new Object[] {23, Constant.TEST_DATA_DIR + "/abs/case23.txt", "news-ar", "350", "9", "",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case23.txt", 9},
			/* 主题词, 摘要长度为空，摘要长度占文章百分比为90 */
			new Object[] {24, Constant.TEST_DATA_DIR + "/abs/case24.txt", "news-ar", "", "42", "90",
					"1", "操作成功", Constant.EXPECTED_DATA_DIR + "/abs/case24.txt", 42}
		};
	}
	/**
	 * 测试文本摘要
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param numOfAbs 摘要长度
	 * @param numOfSub 主题词个数
	 * @param percent 摘要长度占文章长度百分比
	 * @param expectedCode 预期code
	 * @param expectedMessage 预期message
	 * @param expectationPath 预期摘要长度
	 * @param expectedKeyWordSize 预期关键词个数
	 */
	@Test(dataProvider = "absDataProvider")
	public void abs(int caseId, String filePath,String model,
			String numOfAbs, String numOfSub, String percent, String expectedCode, 
			String expectedMessage, String expectationPath, int expectedKeyWordSize) {
		LOGGER.debug("abs, caseId="+caseId);
		try {
			String text = FileOperator.read(filePath, Constant.DEFAULT_ENCODING, READ_OPTIONS);
			AbsResult abs = request.abs(text, model, numOfAbs, numOfSub, percent);
			assertEquals(abs.getCode(), expectedCode);
			assertEquals(abs.getMessage(), expectedMessage);
			String expectedAbs = FileOperator.read(expectationPath, Constant.DEFAULT_ENCODING, READ_OPTIONS);
			LOGGER.debug(String.format("expectedAbs=%s%sabs.getAbs()=%s", 
					expectedAbs, System.lineSeparator(), abs.getAbs()));
			assertEquals(abs.getAbs(), expectedAbs);
			assertEquals(abs.getWordlist().size(), expectedKeyWordSize);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "absErrDataProvider")
	public Object[][] absErrDataProvider(Method method){
		if(!"absErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 模板为空, 使用默认模板 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/absErr/case1.txt", "", "10", "1", "10", "1"},
			/* 模板不存在 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/absErr/case2.txt", "ceshi", "10", "1", "10", "0"},
			/* 中文模板, 外文文本 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/absErr/case3.txt", "news-cn", "10", "1", "10", "1"},
			/* 中文模板, 混合文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/absErr/case4.txt", "news-cn", "10", "1", "10", "1"},
			/* 主题词为负数, 默认返回所有主题词 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/absErr/case5.txt", "news-cn", "10", "-3", "10", "1"},
			/* 空文本 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/absErr/case6.txt", "news-cn", "10", "3", "10", "0"}
		};
	}
	/**
	 * 测试文本摘要-异常情况
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param numOfAbs
	 * @param numOfSub
	 * @param percent
	 * @param expectation
	 */
	@Test(dataProvider = "absErrDataProvider")
	public void absErr(int caseId, String filePath, String model, String numOfAbs, 
			String numOfSub, String percent, String expectation) {
		try {
			String text = FileOperator.read(filePath);
			AbsResult result = request.abs(text, model, numOfAbs, numOfSub, percent);
			assertEquals(result.getCode(), expectation);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "absThemeDataProvider")
	public Object[][] absThemeDataProvider(Method method){
		if(!"absTheme".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/absTheme/1.txt", "news-cn", "1", "1", "操作成功", 1},
			new Object[] {2, Constant.TEST_DATA_DIR + "/absTheme/2.txt", "news-cn", "5", "1", "操作成功", 5},
			/* 长文本 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/absTheme/3.txt", "news-cn", "10", "1", "操作成功", 10},
			/* 英文模板，长文本，短文本，中长文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/absTheme/4.txt", "news-en", "3", "1", "操作成功", 3},
			new Object[] {5, Constant.TEST_DATA_DIR + "/absTheme/5.txt", "news-en", "6", "1", "操作成功", 6},
			new Object[] {6, Constant.TEST_DATA_DIR + "/absTheme/6.txt", "news-en", "15", "1", "操作成功", 15},
			/* 德语模板, 长文本，短文本，中长文本，主题词为2,4，为空 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/absTheme/7.txt", "news-de", "2", "1", "操作成功", 2},
			new Object[] {8, Constant.TEST_DATA_DIR + "/absTheme/8.txt", "news-de", "4", "1", "操作成功", 4},
			new Object[] {9, Constant.TEST_DATA_DIR + "/absTheme/9.txt", "news-de", "", "1", "操作成功", 20},
			/* 法语模板, 长文本,短文本, 中长文本, 主题词为3,30,40 */
			new Object[] {10, Constant.TEST_DATA_DIR + "/absTheme/10.txt", "news-fr", "3", "1", "操作成功", 3},
			new Object[] {11, Constant.TEST_DATA_DIR + "/absTheme/11.txt", "news-fr", "30", "1", "操作成功", 30},
			new Object[] {12, Constant.TEST_DATA_DIR + "/absTheme/12.txt", "news-fr", "40", "1", "操作成功", 40},
			/* 日语模板, 长文本, 短文本, 中长文本 */
			new Object[] {13, Constant.TEST_DATA_DIR + "/absTheme/13.txt", "news-jp", "7", "1", "操作成功", 7},
			new Object[] {14, Constant.TEST_DATA_DIR + "/absTheme/14.txt", "news-jp", "25", "1", "操作成功", 25},
			new Object[] {15, Constant.TEST_DATA_DIR + "/absTheme/15.txt", "news-jp", "50", "1", "操作成功", 50},
			/* 西班牙语 */
			new Object[] {16, Constant.TEST_DATA_DIR + "/absTheme/16.txt", "news-es", "12", "1", "操作成功", 12},
			new Object[] {17, Constant.TEST_DATA_DIR + "/absTheme/17.txt", "news-es", "100", "1", "操作成功", 80},
			new Object[] {18, Constant.TEST_DATA_DIR + "/absTheme/18.txt", "news-es", "150", "1", "操作成功", 150},
			/* 俄语 */
			new Object[] {19, Constant.TEST_DATA_DIR + "/absTheme/19.txt", "news-ru", "22", "1", "操作成功", 22},
			new Object[] {20, Constant.TEST_DATA_DIR + "/absTheme/20.txt", "news-ru", "35", "1", "操作成功", 35},
			new Object[] {21, Constant.TEST_DATA_DIR + "/absTheme/21.txt", "news-ru", "200", "1",  "操作成功", 200},
			/* 阿拉伯语 */
			new Object[] {22, Constant.TEST_DATA_DIR + "/absTheme/22.txt", "news-ar", "8", "1", "操作成功", 8},
			new Object[] {23, Constant.TEST_DATA_DIR + "/absTheme/23.txt", "news-ar", "17", "1", "操作成功", 17},
			new Object[] {24, Constant.TEST_DATA_DIR + "/absTheme/24.txt", "news-ar", "28", "1", "操作成功", 28}
		};
	}
	/**
	 * 主题词抽取
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param num 主题词个数
	 * @param code 预期code
	 * @param message 预期message
	 * @param wordSize 预期主题词个数
	 */
	@Test(dataProvider = "absThemeDataProvider")
	public void absTheme(int caseId, String filePath, String model, 
			String num, String code, String message, int wordSize) {
		String text;
		try {
			text = FileOperator.read(filePath);
			AbsThemeResult result = request.absTheme(text, model, num);
			List<ThemeWord> themeWords = result.getThemeWords();
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			assertEquals(themeWords.size(), wordSize);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "absThemeErrDataProvider")
	public Object[][] absThemeErrDataProvider(Method method){
		if(!"absThemeErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 中文模板，文本使用阿萨姆语，有返回结果 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/absThemeErr/1.txt", "news-cn", "1", "1"},
			/* 中文模板，文本使用阿萨姆语中文混合，有返回结果 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/absThemeErr/2.txt", "news-cn", "1", "1"},
			/* 空文本*/
			new Object[] {3, Constant.TEST_DATA_DIR + "/absThemeErr/3.txt", "news-cn", "1", "0"},
			/* 模板不存在 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/absThemeErr/BEIJINGTIANANMEN.txt", "news-cn123", "1", "0"},
			/* 模板为空, 有返回结果 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/absThemeErr/BEIJINGTIANANMEN.txt", "", "1", "1"},
			/* 主题词为负数，默认显示所有主题词 */
			new Object[] {6, Constant.TEST_DATA_DIR + "/absThemeErr/BEIJINGTIANANMEN.txt", "", "1", "1"},
			/* 文本为特殊字符 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/absThemeErr/symbol.txt", "news-cn", "1", "-1"}
		};
	}
	/**
	 * 主题词抽取-异常情况
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param num
	 * @param expectation
	 */
	@Test(dataProvider = "absThemeErrDataProvider")
	public void absThemeErr(int caseId, String filePath, String model, String num, String expectation) {
		LOGGER.debug("absThemeErr, caseId="+caseId);
		AbsThemeResult result;
		try {
			String text = FileOperator.read(filePath);
			result = request.absTheme(text, model, num);
			assertEquals(result.getCode(), expectation);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "absLabelDataProvider")
	public Object[][] absLabelDataProvider(Method method){
		if(!"absLabel".equals(method.getName()))
			return null;
		return new Object[][] {
			//中长文本，标题参数为空
			new Object[] {1, Constant.TEST_DATA_DIR + "/absLabel/1.txt", "demo", "", 
					"艾力更·依明巴海;习近平;费尔南德斯;",
					"主席;特使;总统就职仪式;阿根廷首都;人文领域;卸任总统;新任总统;",
					"经济;国际;时政外交;韩国;"},
			//中长文本，标题参数不为空
			new Object[] {2, Constant.TEST_DATA_DIR + "/absLabel/2.txt", "demo", "学习",
					"艾力更·依明巴海;习近平;费尔南德斯;",
					"学习;主席;特使;总统就职仪式;阿根廷首都;人文领域;卸任总统;",
					"经济;政治;时政;法律;"},
			//短文本，标题参数为空
			new Object[] {3, Constant.TEST_DATA_DIR + "/absLabel/3.txt", "demo", "", 
					"习近平;华南;江南;",
					"洪涝;地质灾害防御;防灾救灾;暴雨;救援;成效;决策;",
					"社会;法律;时政;政法;"},
			//短文本，标题参数不为空
			new Object[] {4, Constant.TEST_DATA_DIR + "/absLabel/4.txt", "demo", "主席", 
					"习近平;华南;江南;",
					"主席;洪涝;地质灾害防御;防灾救灾;暴雨;救援;成效;",
					"社会;时政;法律;政法;"},
			//长文本，标题参数为空
			new Object[] {5, Constant.TEST_DATA_DIR + "/absLabel/5.txt", "demo", "", 
					"唐胜利;北京博爱医院;",
					"胜利;跳楼;研究中心;小姐;天涯夜总会;鲜血;社会丑恶现象;扫黄;",
					"游戏;社会;故事;文章;"},
			//长文本，标题参数不为空
			new Object[] {6, Constant.TEST_DATA_DIR + "/absLabel/6.txt", "demo", "逮捕", 
					"唐胜利;北京博爱医院;",
					"逮捕;胜利;跳楼;研究中心;小姐;天涯夜总会;鲜血;社会丑恶现象;",
					"社会;法律;游戏;故事;"},
			//超短文本，标题参数为空
			new Object[] {7, Constant.TEST_DATA_DIR + "/absLabel/7.txt", "demo", "", "胡锦涛;", "", "政治;"},
			//超短文本，标题参数不为空
			new Object[] {8, Constant.TEST_DATA_DIR + "/absLabel/8.txt", "demo", "主席", "胡锦涛;", "主席;",
					"政治;经济;时政外交;投资;"},
			//超长文本，标题参数为空
			new Object[] {9, Constant.TEST_DATA_DIR + "/absLabel/9.txt", "demo", "", 
					"习近平;中国;",
					"贫困劳动力;扶贫搬迁;住房;存量;务工;疫情;发展;贫困人口;",
					"经济;三农;投资;时政;"},
			//超长文本文本，标题参数不为空
			new Object[] {10, Constant.TEST_DATA_DIR + "/absLabel/10.txt", "demo", "会议", 
					"习近平;中国;",
					"贫困劳动力;扶贫搬迁;住房;会议;存量;务工;疫情;发展;",
					"经济;投资;时政;三农;"},
		};
	}
	/**
	 * 测试标签抽取 中文
	 * @param caseId
	 * @param filePath
	 * @param model
	 * @param title 标题
	 * @param entities 预期抽取实体
	 * @param keywords 预期返回关键词
	 * @param labels 预期抽取标签
	 */
	@Test(dataProvider = "absLabelDataProvider")
	public void absLabel(int caseId, String filePath, String model, String title, 
			String entities, String keywords, String labels) {
		LOGGER.debug("absLabel, caseId="+caseId);
		try {
			String content = FileOperator.read(filePath);
			AbsLabelResult result = request.absLabel(content, model, title);
			assertEquals(result.getEntities(), entities);
			assertEquals(result.getKeyWords(), keywords);
			assertEquals(result.getLabels(), labels);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "absLabelErrDataProvider")
	public Object[][] absLabelErrDataProvider(Method method){
		if(!"absLabelErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//不存在的模板
			new Object[] {1, "习近平主席特使、全国人大常委会副委员长艾力更·依明巴海12月10日在阿根廷首都布宜诺斯艾利斯"
					+ "出席阿新总统就职仪式，并于9日会见新任总统费尔南德斯。", "default234", "", "0"},
			//模板为空，取默认模板default
			new Object[] {2, "胡锦涛", "", "学习", "1"},
			//文本为其他语言，如韩语，有返回结果，但是不用管他的处理结果就可以
			new Object[] {3, "이 붕 은 두 나라 군대, 공안 등 부서 간 의 교류 에 지 지 를 표시 했다.그 는 이 를 믿는다 고 밝 혔 다.  이번 방 중 은 반드시 중국 과 러시아"
					+ " 양국 의 관련 부서 의 우호 협력 관계 의 진일보 한 발전 에 도움 이 될 것 이다.", "demo", "", "1"},
			//文本为混合语言，有返回结果，但是不用管他的处理结果就可以
			new Object[] {4, "이 붕 은 두 나라 군대, 공안 등 부서 간 의 교류 에 지 지 를 표시 했다.그 는 이 를 믿는다 고 밝 혔 다.  이번 방 중 은 반드시 중국 과 러시아"
					+ " 양국 의 관련 부서 의 우호 협력 관계 의 진일보 한 발전 에 도움 이 될 것 이다.李鹏对两国军队、公安等部门之间开展交往表示支持。"
					+ "他表示相信，科科申这 次访华一定有助于中俄两国有关部门友好合作关系的进一步发展。", "demo", "", "1"},
			new Object[] {5, "", "demo", "", "0"},
		};
	}
	/**
	 * 标签抽取，异常情况
	 * @param caseId
	 * @param content
	 * @param model
	 * @param title
	 * @param code
	 */
	@Test(dataProvider = "absLabelErrDataProvider")
	public void absLabelErr(int caseId, String content, String model, String title, String code) {
		LOGGER.debug("absLabelErr, caseId="+caseId);
		try {
			AbsLabelResult result = request.absLabel(content, model, title);
			assertEquals(result.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
