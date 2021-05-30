package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.CollationCommonResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class CollationCommon {
    private AnnotationConfigApplicationContext context;
    private TRSCkmRequest request;

    @BeforeClass
    private void beforeClass(){
        Constant.reconfigureLog4j2();
        context = new AnnotationConfigApplicationContext(AspectConfig.class);
        request = context.getBean(TRSCkmRequest.class);
    }
    @AfterClass
    private void afterClass(){
        if(context != null)
            context.close();
    }
    @DataProvider(name = "collationCommonDataProvider")
    private Object[][] collationCommonDataProvider(Method method){
    	if(!"collationCommon".equals(method.getName()))
    		return null;
    	return new Object[][] {
    		/* 人物职称错误 */
    		new Object[] {1, Constant.TEST_DATA_DIR + "/collationCommon/1.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/1.txt"},
    		new Object[] {2, Constant.TEST_DATA_DIR + "/collationCommon/2.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/2.txt"},
    		/* 政治术语错误, 敏感词 */
    		new Object[] {3, Constant.TEST_DATA_DIR + "/collationCommon/3.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/3.txt"},
    		new Object[] {4, Constant.TEST_DATA_DIR + "/collationCommon/4.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/4.txt"},
    		new Object[] {5, Constant.TEST_DATA_DIR + "/collationCommon/5.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/5.txt"},
    		new Object[] {6, Constant.TEST_DATA_DIR + "/collationCommon/6.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/6.txt"},
    		new Object[] {7, Constant.TEST_DATA_DIR + "/collationCommon/7.txt", "demo", "1", "操作成功",
    				Constant.EXPECTED_DATA_DIR + "/collationCommon/7.txt"},
    	};
    }
    /**
     * 常识校对中文
     * @param caseId
     * @param text
     * @param model
     * @param code
     * @param message
     * @param infos
     */
    @Test(dataProvider = "collationCommonDataProvider")
    private void collationCommon(int caseId, String path, String model, 
    		String code, String message, String expectedInfoPath) {
    	try {
    		String text = FileOperator.read(path);
    		CollationCommonResult result = request.collationCommon(text, model);
    		assertEquals(result.getCode(), code);
    		assertEquals(result.getMessage(), message);
    		List<com.trs.ckm.api.pojo.CollationCommonResult.Error> errors = result.getErrs();
    		String[] infos = FileOperator.read(expectedInfoPath).split(System.lineSeparator());
    		for(int i=0,size=errors.size(); i<size; i++)
    			assertEquals(errors.get(i).getInfo(), infos[i]);
    	}catch(Exception e) {
    		fail(Other.stackTraceToString(e));
    	}
    }
    @DataProvider(name = "collationCommonErrDataProvider")
    private Object[][] collationCommonErrDataProvider(Method method) {
    	if(!"collationCommonErr".equalsIgnoreCase(method.getName()))
    		return null;
    	return new Object[][] {
    		/* 模板为空, 取默认模板demo */
    		new Object[] {1, "习近平总经理，胡锦涛总统", "", "1"},
    		/* 模板不存在 */
    		new Object[] {2, "习近平总经理，胡锦涛总统", "demo1", "0"},
    		/* 空文本 */
    		new Object[] {3, "", "demo", "0"},
    		/* 没有常识错误的文本, 返回空结果 */
    		new Object[] {4, "习近平", "demo", "-1"},
    		/* 外文文本 */
    		new Object[] {5, "Beijing, August 24 (reporter Zhang Huizhong) - Premier Li Keqiang attended "
    				+ "the third Lancang Mekong Cooperation leaders' video conference in the Great Hall of the people "
    				+ "on the morning of 24. Li Keqiang and Prime Minister Tong Lun of Laos jointly presided "
    				+ "over the meeting. Cambodian Prime Minister Hun Sen, Burmese president Wen Min, "
    				+ "Thai Prime Minister bayou and Vietnamese Prime Minister Nguyen Chun Fuk attended.",
    				"demo", "-1"},
    		/* 文本内容为混合文本, 会正常抽取中文文本 */
    		new Object[] {6, "Beijing, 习近平总经理August 24 (reporter Zhang Huizhong) - "
    				+ "Premier Li Keqiang attended the third Lancang Mekong Cooperation leaders' "
    				+ "video conference in the Great Hall of the people on the morning of 24. "
    				+ "Li Keqiang and Prime Minister Tong Lun of Laos jointly presided over the meeting. "
    				+ "Cambodian Prime Minister Hun Sen, Burmese president Wen Min, "
    				+ "Thai Prime Minister bayou and Vietnamese Prime Minister Nguyen Chun Fuk attended.",
    				"demo", "1"}
    	};
    }
    @Test(dataProvider = "collationCommonErrDataProvider")
    private void collationCommonErr(int caseId, String text, String model, String code) {
    	try {
    		CollationCommonResult result = request.collationCommon(text, model);
    		assertEquals(result.getCode(), code);
    	}catch(Exception e) {
    		fail(Other.stackTraceToString(e));
    	}
    }
}
