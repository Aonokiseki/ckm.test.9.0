package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.ParserSyntaxResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ParserSyntax {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private SmartChineseAnalyzer analyzer;
	
	@BeforeClass
	private void before() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
		analyzer = new SmartChineseAnalyzer();
	}
	
	@AfterClass
	private void after() {
		if(context != null)
			context.close();
	}
	
	@DataProvider(name = "parserSyntaxDataProvider")
	private Object[][] parserSyntaxDataProvider(Method method){
		if(!"parserSyntax".equals(method.getName()))
			return null;
		return new Object[][] {
			/* demo文本 */
			new Object[] {1, 1, Constant.TEST_DATA_DIR + "/parserSyntax/demo.txt", "操作成功", 139},
			/* 长文本 */
			/* 2020-11-27: 此文本不分词直接提交给CKM必定触发进程崩溃, 提交禅道*/
			/* 2020-11-30: 开发组回复: 崩溃的原因是输入文本过长
			 * 1. 句法接口的正常用法是以句为单位输入，整篇调用效果不好没有实用意义而且可能会崩溃
			 * 2. 此文章，分句以后分别调用，均能正常返回结果 ->  在 parserSyntaxWithSplitText() 验证
			 * 3. 将修改接口，至多取输入的前128字，以保证程序稳定性 
			 * 以上解释, 说明即使传入这篇文本, 也不会造成进程崩溃, 也一定有结果, 但结果好不好, 甚至对不对都不强求了
			 * 针对这篇特殊的文本, 额外准备一个测试方法(并将这篇测试文本分句), 专门检查分句之后有没有结果*/
			new Object[] {2, 1, Constant.TEST_DATA_DIR + "/parserSyntax/long.txt", "操作成功", 153},
			/* 短文本 */
			new Object[] {3, 1, Constant.TEST_DATA_DIR + "/parserSyntax/short.txt", "操作成功", 1},
			/* 连续文本, 强制让seg=0 */
			new Object[] {4, 0, Constant.TEST_DATA_DIR + "/parserSyntax/demo.txt", "操作成功", 1},
			/* 空文本 */
			new Object[] {5, 1, Constant.TEST_DATA_DIR + "/parserSyntax/empty.txt", "输入参数为空！", 0},
		};
	}
	/**
	 * 中文句法分析
	 * @param caseId
	 * @param seg
	 * @param filePath
	 * @param message
	 * @param keyWordsSize 关键词列表预期大小
	 */
	@Test(dataProvider="parserSyntaxDataProvider")
	private void parserSyntax(int caseId, int seg, String filePath, 
			String message, int keyWordsSize) {
		try {
			String text = FileOperator.read(filePath);
			ParserSyntaxResult syntax = request.parserSyntax(text, String.valueOf(seg));
			assertEquals(syntax.getMessage(), message);
			/* 强制seg=0的情况, 意思是对连续文本不做分词直接返回, 属于用法错误, 不检查关键词数量 */
			if(seg == 0 || !"操作成功".equals(message))
				return;
			/* 2020-12-31: 算法可能有调整, 不做细致检查 */
			assertTrue(syntax.getKeywords().size() > 0);
//			assertEquals(syntax.getKeywords().size(), keyWordsSize);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	/**
	 * 长文本, 先分句之后再传入CKM做句法分析。对应上方用例2
	 */
	@Test
	private void parserSyntaxWithSplitText() {
		try {
			String[] lines = FileOperator.read(Constant.TEST_DATA_DIR + "/parserSyntax/afterSplit.txt")
					.split(System.lineSeparator());
			ParserSyntaxResult syntax;
			for(int i=0; i<lines.length; i++) {
				syntax = request.parserSyntax(lines[i], "1");
				assertEquals(syntax.getMessage(), "操作成功");
				assertTrue(syntax.getKeywords().size() > 0);
			}
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	
	@DataProvider(name = "parserSyntaxWithPreTokenDataProvider")
	private Object[][] parserSyntaxWithPreTokenDataProvider(Method method) {
		if(!"parserSyntaxWithPreToken".equals(method.getName()))
			return null;
		return new Object[][] {
			/* demo 文本 */
			new Object[] {1, 0, Constant.TEST_DATA_DIR + "/parserSyntax/demo.txt", "操作成功"},
			/* 长文本 */
			new Object[] {2, 0, Constant.TEST_DATA_DIR + "/parserSyntax/long.txt", "操作成功"},
			/* 短文本 */
			new Object[] {3, 0, Constant.TEST_DATA_DIR + "/parserSyntax/short.txt", "操作成功"},
			/* 分隔后的文本, 强制让seg=1 */
			new Object[] {4, 1, Constant.TEST_DATA_DIR + "/parserSyntax/demo.txt", "操作成功"}
		};
	}
	/**
	 * 带本地预分词的/parser/syntax接口测试<br>
	 * 会先调用lucene-smartChineseAnalyzer将连续文本分词后再发送给CKM<br>
	 * 这样可以避免准备测试数据时手工分词<br>
	 * 同时也无需在预期中给定关键词数量,只要框架的分词和请求返回的关键词一致即可
	 * @param caseId
	 * @param seg
	 * @param filePath
	 * @param message
	 */
	@Test(dataProvider="parserSyntaxWithPreTokenDataProvider")
	private void parserSyntaxWithPreToken(int caseId, int seg, String filePath, String message) {
		try {
			String text = FileOperator.read(filePath);
			/* 本地分词 */
			List<String> tokens = tokens(text);
//			int expectedKeyWordsSize = tokens.size();
			StringBuilder textAfterToken = new StringBuilder();
			for(int i=0, size=tokens.size(); i<size; i++)
				textAfterToken.append(tokens.get(i)).append(" ");
			text = textAfterToken.toString();
			/* 提交分词后的文本 */
			ParserSyntaxResult syntax = request.parserSyntax(text, String.valueOf(seg));
			assertEquals(syntax.getMessage(), message);
			/* 对于强制让seg=1,给已经分过词的文本再分词, 属于用法不当, 不要求两次分词的数量一致 */
			if(seg == 1 || !"操作成功".equals(message))
				return;
			/* 2020-11-30: 对于已经手工分词的文本而言, CKM只读取前64个 */
			/* 2020-12-30: 又改了, 实际使用发现有长句子, 现在是512字, 或256词
			 * seg=1的时候，是先按512截断，再分词，再走句法
			 * seg=0的时候，是按256个词截断，再走句法
			 * 
			 * 变更检查策略，长度大于0即可
			 */
//			assertEquals(syntax.getKeywords().size(), Math.min(expectedKeyWordsSize, 64));
			assertTrue(syntax.getKeywords().size() > 0);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	/**
	 * 将文本分词
	 * @param text
	 * @return
	 * @throws IOException
	 */
	private List<String> tokens(String text) throws IOException{
		List<String> tokens = new LinkedList<String>();
		TokenStream tokenStream = analyzer.tokenStream("keywords", text);
		tokenStream.reset();
		CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
		while(tokenStream.incrementToken())
			tokens.add(attribute.toString());
		tokenStream.end();
		tokenStream.close();
		return tokens;
	}
}
