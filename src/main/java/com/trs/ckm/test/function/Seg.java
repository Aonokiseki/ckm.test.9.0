package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.SegmentResult;
import com.trs.ckm.api.pojo.SegmentResult.Result;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Seg {
	private final static Logger LOGGER = LogManager.getLogger(Seg.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request ;
	
	@BeforeClass
	private void beforeClass() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
	}
	@AfterClass
	private void afterClass() {
		if(context != null)
			context.close();
	}
	@DataProvider(name = "segDataProvider")
	private Object[][] segDataProvider(Method method){
		if(!"seg".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, Constant.TEST_DATA_DIR + "/seg/1.txt", Constant.EXPECTED_DATA_DIR + "/seg/1.txt"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/seg/2.txt", Constant.EXPECTED_DATA_DIR + "/seg/2.txt"},
			new Object[] {3, Constant.TEST_DATA_DIR + "/seg/3.txt", Constant.EXPECTED_DATA_DIR + "/seg/3.txt"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/seg/4.txt", Constant.EXPECTED_DATA_DIR + "/seg/4.txt"},
		};
	}
	
	/**
	 * 基础分词, 相较于plo/seg, 速度更快, 代价是效果不如plo/seg
	 * @param caseId
	 * @param path
	 * @param words
	 */
	@Test(dataProvider = "segDataProvider")
	private void seg(int caseId, String path, String wordsPath) {
		LOGGER.debug("seg, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			SegmentResult segmentResult = request.seg(text);
			assertEquals(segmentResult.getCode(), "1");
			List<com.trs.ckm.api.pojo.SegmentResult.Result> segments = segmentResult.getSegResult();	
			String[] words = FileOperator.read(wordsPath).split(System.lineSeparator());
			assertEquals(segments.size(), words.length);
			Arrays.sort(words);
			Collections.sort(segments, new Comparator<Result>() {
				@Override
				public int compare(Result first, Result second) {
					return first.getWord().compareTo(second.getWord());
				}
			});
			for(int i=0,size=segments.size(); i<size; i++)
				assertEquals(segments.get(i).getWord(), words[i]);
		} catch (IOException e) {
			Assert.fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "segErrDataProvider")
	private Object[][] segErrDataProvider(Method method){
		if(!"segErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 外文文本 */
			new Object[] {1, "Alecken Eminbahe, the special envoy of President Xi Jinping and vice chairman of the "
					+ "Standing Committee of the National People's Congress, attended the inauguration ceremony of "
					+ "the new president in Buenos Aires, Argentina, on December 10th, and met with the new "
					+ "president Fernandes on 9.", "1"},
			/* 中韩文混合 */
			new Object[] {2, "好好学习，天天向上열심히 공부 해서 매일 향상 하 다.", "1"},
			/* 空文本 */
			new Object[] {3, "", "0"},
			/* 符号字符 */
			new Object[] {4, "#@%￥#%￥#%", "1"}
		};
	}
	
	@Test(dataProvider = "segErrDataProvider")
	private void segErr(int caseId, String text, String code) {
		LOGGER.debug("segErr, caseId="+caseId);
		try {
			SegmentResult segErr = request.seg(text);
			assertEquals(segErr.getCode(), code);
		}catch(Exception e) {
			Assert.fail(Other.stackTraceToString(e));
		}
	}
}
