package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.DySearchResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.Other;

public class DySearch {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	
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
	
	@DataProvider(name = "dySearchDataProvider")
	public Object[][] dySearchDataProvider(Method method){
		if(!"dySearch".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, "计算机", "demo", "0",
					"1", "操作成功", 
					new String[] {"计算机", "半导体设备市场", "科技", "针尖大小", "计算机主机", 
							      "计算机整机", "计算机死机", "计算机人机", "计算机单机", "计算机网", 
							      "计算机业", "计算机系", "计算机维", "计算机图", "计算机所", "计算机室"}},
			new Object[] {2, "计算机", "demo", "1",
					"1", "操作成功", 
					new String[] {"半导体设备市场", "科技", "针尖大小"}},
			new Object[] {3, "计算机", "demo", "2",
					"1", "操作成功", 
					new String[] {"计算机","计算机主机","计算机整机","计算机死机","计算机人机",
							      "计算机单机","计算机网","计算机业","计算机系","计算机维",
							      "计算机图","计算机所","计算机室","计算机配","计算机界"}},
			new Object[] {4,"测试","demo","",
					"1","操作成功",
					new String[] {"测试","电文","北京","记者","测试中",
							      "测试值","测试仪","测试线","测试头","测试题",
							      "测试台","测试室","测试人","测试区","测试类",
							      "测试卡"}},
			new Object[] {5,"测试","demo","1",
					"1","操作成功",
					new String[] {"电文","北京","记者"}},
			new Object[] {6,"测试","demo","2",
					"1","操作成功",
					new String[] {"测试","测试中","测试值","测试仪","测试线",
							      "测试头","测试题","测试台","测试室","测试人",
							      "测试区","测试类","测试卡","测试卷","测试盒","小测试"}},
			//demo_mul  多语言模型：支持中文、英文、德文、法文、日文、西班牙文、俄文、阿文
			//中文
			new Object[] {7,"学习","demo_mul","0",
					"1","操作成功",
					new String[] {"学习","学习过程","学生学习","自学","学习方法",
							      "读书","学员学习","学习者","学习潜力","学习游泳",
							      "学习园地","学习驾照","老人学习","平台学习","廉政学习","鲁能学习"}},
			new Object[] {8,"学习","demo_mul","1",
					"1","操作成功",
					new String[] {"学习过程","学生学习","自学","学习方法","读书"}},
			new Object[] {9,"学习","demo_mul","2",
					"1","操作成功",
					new String[] {"学习","学生学习","学员学习","学习者","学习潜力",
							      "学习游泳","学习园地","学习驾照","老人学习","平台学习",
							      "廉政学习","鲁能学习"}},
			//英文
			new Object[] {10,"test","demo_mul","0",
					"1","操作成功",
					new String[] {"test","crucial","string","interview","system","Status"}},
			new Object[] {11,"test","demo_mul","1",
					"1","操作成功",
					new String[] {"crucial","string","interview","system","Status"}},
			new Object[] {12,"test","demo_mul","2",
					"1","操作成功",
					new String[] {"test"}},
			//德语
			new Object[] {13,"Peking","demo_mul","0",
					"1","操作成功",new String[] {"Pete King","Perry King"}},
			new Object[] {14,"Peking","demo_mul","2",
					"1","操作成功",new String[] {"Pete King","Perry King"}},
			//法语
			new Object[] {15,"Emmanuel de Meester","demo_mul","0",
					"1","操作成功",
					new String[] {"Emmanuel de Meester","Emmanuel de Margerie","Emmanuel de Liechtenstein"}},
			new Object[] {16,"Emmanuel de Meester","demo_mul","2",
					"1","操作成功",
					new String[] {"Emmanuel de Meester","Emmanuel de Margerie","Emmanuel de Liechtenstein"}},
	        //日语
			new Object[] {17,"技術","demo_mul","0",
					"1","操作成功",
					new String[] {"技術","安全技術","頸域","項目","鐁頭","民間交流","信息技術"}},
			new Object[] {18,"技術","demo_mul","2",
					"1","操作成功",
					new String[] {"技術","信息技術","安全技術"}},
		    //俄语
			new Object[] {19,"компьютер","demo_mul","0",
					"1","操作成功",
					new String[] {"Компьютер","Биокомпьютер","ДНК компьютер","Нанокомпьютер","Миникомпьютер", 
								  "Велокомпьютер","Компьютерра"}},
			new Object[] {20,"компьютер","demo_mul","2",
					"1","操作成功",
					new String[] {"Компьютер","Биокомпьютер","ДНК компьютер","Нанокомпьютер","Миникомпьютер", 
							      "Велокомпьютер","Компьютерра"}},
			//西班牙语
			new Object[] {21,"Computadora","demo_mul","0",
					"1","操作成功",
					new String[] {"Placa computadora"}},
			new Object[] {22,"Computadora","demo_mul","2",
					"1","操作成功",
					new String[] {"Placa computadora"}},
			//阿拉伯语
			new Object[] {23,"Computadora","demo_mul","0",
					"1","操作成功",
					new String[] {"Placa computadora"}},
			new Object[] {24,"Computadora","demo_mul","2",
					"1","操作成功",
					new String[] {"Placa computadora"}},
		};
	}
	
	@Test(dataProvider = "dySearchDataProvider")
	public void dySearch(int caseId, String query, String model, String option, 
			String code, String message, String[] expectation) {
		try {
			DySearchResult result = request.dySearch(query, model, option);
			assertEquals(result.getCode(), code);
			assertEquals(result.getMessage(), message);
			List<String> dyResults = result.getDyResult();
			Collections.sort(dyResults);
			Arrays.sort(expectation);
			for(int i=0,size= dyResults.size(); i<size; i++)
				assertEquals(dyResults.get(i), expectation[i]);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "dySearchErrDataProvider")
	public Object[][] dySearchErrDataProvider(Method method){
		if(!"dySearchErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//demo-中文模型,option为3
			new Object[] {1,"计算机","demo","3","0"},
			//demo-中文模型,检索串为阿拉伯文
			new Object[] {2,"حاسب ","demo","0","-1"},
			//demo_mul，检索串为韩语
			new Object[] {3,"컴퓨터","demo","0","-1"},	
			//demo_mul，检索串为德语，相关检索
			new Object[] {4,"morgen","demo","1","-1"},	
			//demo_mul，检索串为法语，相关检索
			new Object[] {5,"Demain.","demo","1","-1"},			
			//demo_mul，检索串为日语，相关检索
			new Object[] {6,"勉強します","demo","1","-1"},
			//demo_mul，检索串为西班牙语，相关检索
			new Object[] {7,"Computadora","demo","1","-1"},
			//demo_mul，检索串为俄语，相关检索
			new Object[] {8,"компьютер ","demo","1","-1"},
			//demo_mul，检索串为阿语，相关检索
			new Object[] {9," حاسب ","demo","1","-1"},
			//模板为不存在模板
			new Object[] {10,"计算机","demo1","1","0"},		
			//模板为空，取默认模板demo
			new Object[] {11,"计算机","","1","1"},	
			//检索串为空
			new Object[] {12,"","demo","1","0"},
		};
	}
	
	@Test(dataProvider = "dySearchErrDataProvider")
	public void dySearchErr(int caseId, String query, String model, String option, String expectation) {
		try {
			assertEquals(request.dySearch(query, model, option).getCode(), expectation);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
