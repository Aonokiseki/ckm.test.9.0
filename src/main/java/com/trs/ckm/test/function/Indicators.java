package com.trs.ckm.test.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.IndicatorsResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Indicators {
    private AnnotationConfigApplicationContext context;
    private TRSCkmRequest request;
    private final static Map<String,String> READ_OPTIONS = new HashMap<String,String>();
    static {
    	READ_OPTIONS.put("not.append.lineseparator", "true");
    }

    @BeforeClass
    private void beforeClass(){
    	Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = (TRSCkmRequest)context.getBean(TRSCkmRequest.class);
    }
    @AfterClass
    private void afterClass(){
        if(context != null)
            context.close();
    }

    @DataProvider(name = "indicatorsDataProvider")
    private Object[][] indicatorsDataProvider(Method method){
        return new Object[][]{
            
            new Object[] {1, Constant.TEST_DATA_DIR + "/indicators/1.txt", 
                new String[] {"100"},
                Constant.EXPECTED_DATA_DIR + "/indicators/1.txt"},
            
            new Object[] {2, Constant.TEST_DATA_DIR + "/indicators/2.txt",
                new String[] {"1.7","1.4","9","77"},
                Constant.EXPECTED_DATA_DIR + "/indicators/2.txt"},

            new Object[] {3, Constant.TEST_DATA_DIR + "/indicators/3.txt",
                new String[] {"2","3000","1.7","97.8","8700"},
                Constant.EXPECTED_DATA_DIR + "/indicators/3.txt"},

            new Object[] {4, Constant.TEST_DATA_DIR + "/indicators/4.txt",
                new String[] {"150","4","27","25","17","180","112","1.3",
                              "30","6","170","4","150","40","80"},
                Constant.EXPECTED_DATA_DIR + "/indicators/4.txt"},
        };
    }

    @Test(dataProvider = "indicatorsDataProvider")
    private void indicators(int caseId, String path, String[] numMin, String expectedIndicatorsPath){
        try{
            String text = FileOperator.read(path);
            IndicatorsResult indicators = request.indicators(text);
            assertEquals(indicators.getCode(), "1");
            assertEquals(indicators.getMessage(), "操作成功");
            List<com.trs.ckm.api.pojo.IndicatorsResult.IndicatorsInfo> indicatorsInfo = 
            		indicators.getIndicatorsInfoList();
            assertEquals(indicatorsInfo.size(), numMin.length);
            String[] expectedIndicators = FileOperator.read(expectedIndicatorsPath, "UTF-8", READ_OPTIONS)
            		.split(System.lineSeparator());
            for(int i=0, size=indicatorsInfo.size(); i<size; i++){
                assertEquals(indicatorsInfo.get(i).getNumMin(), numMin[i]);
                assertEquals(indicatorsInfo.get(i).getIndicator().trim(), expectedIndicators[i].trim());
            }
        }catch(Exception e){
            fail(Other.stackTraceToString(e));
        }
    }

    @DataProvider(name = "indicatorsErrDataProvider")
    private Object[][] indicatorsErrDataProvider(Method method){
        return new Object[][]{
            //文本为中文以为其他语言文本
					new Object[] {1,"Li Na scored 100 points in the exam", "-1"},
            //文本中不包含数值指标
            new Object[] {2,"李娜同学", "-1"},		
            //文本为混合文本，可以正常抽取
            new Object[] {3,"李娜同学考了100分。Li Na scored 100 points in the exam", "1"},
            //文本为空
            new Object[] {4,"", "0"},
        };
    }

    @Test(dataProvider = "indicatorsErrDataProvider")
    private void indicatorsErr(int caseId, String text, String code){
        try{
            assertEquals(request.indicators(text).getCode(), code);
        }catch(Exception e){
            fail(Other.stackTraceToString(e));
        }
    }
}
