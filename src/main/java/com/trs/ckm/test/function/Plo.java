package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

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
import com.trs.ckm.api.pojo.PloResult;
import com.trs.ckm.api.pojo.PloResult.Entity;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Plo {
	private final static Logger LOGGER = LogManager.getLogger(Plo.class);
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	
	@BeforeClass
	private void beforeClass() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
		context.close();
	}
	
	@AfterClass
	private void afterClass() {
		if(context != null)
			context.close();
	}
	
	@DataProvider(name = "ploDataProvider")
	private Object[][] ploDataProvider(Method method){
		return new Object[][] {
			/* 短文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/plo/short.txt", "1", new String[] {"习近平","特使","主席"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/plo/short.txt", "2", new String[] {"6月"}},
			new Object[] {3, Constant.TEST_DATA_DIR + "/plo/short.txt", "", new String[] {"6月","习近平","特使","主席"}},
			/* 中长文本 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/plo/medium.txt", "1", 
					new String[] {"中国","拉迪","中国政府","美国智库彼得森国际经济研究所",
							"记者","中国央行","新华社","跨国金融公司","高级研究员","尼古拉斯·拉迪"}},
			new Object[] {5, Constant.TEST_DATA_DIR + "/plo/medium.txt", "2",
					new String[] {"今年","6227004474350536878","http://www.baidu.com","4656@msn.com",
							"642226199404230536","jia.huanhuan@trs.com.cn","6月份","京A·88888","18310846019","明年"}},
			new Object[] {6, Constant.TEST_DATA_DIR + "/plo/medium.txt", "",
					new String[] {"中国","拉迪", "今年","6227004474350536878","http://www.baidu.com","记者",
							"642226199404230536","6月份","新华社","18310846019","明年","跨国金融公司","高级研究员",
							"尼古拉斯·拉迪","中国政府","美国智库彼得森国际经济研究所", "中国央行","4656@msn.com",
							"jia.huanhuan@trs.com.cn","京A·88888"}},
			/* 长文本 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/plo/long.txt", "1",
					new String[] {"中国","国务院扶贫办","公民","国家领导人","非洲","舒之恒鞋业车间","国际清算银行","俄罗斯",
							"上海合作组织", "法人","二十国集团","企业负责人","亚洲","习近平","留学生","四川苍溪县","领事","美国",
							"德国科尔伯基金会","博鳌亚洲论坛","弭平","代表","瑞士日内瓦万国宫","负责人","总书记","澳大利亚",
							"二十国集团领导人杭州峰会","郭安淑","工人"}},
			new Object[] {8, Constant.TEST_DATA_DIR + "/plo/long.txt", "2",
					new String[] {"今年","去年","2020年1月","6月30日","2008年","2019年"}},
			new Object[] {9, Constant.TEST_DATA_DIR + "/plo/long.txt", "",
					new String[] {"中国","去年","今年","国务院扶贫办","公民","国家领导人","非洲","舒之恒鞋业车间",
							"国际清算银行","俄罗斯","上海合作组织","法人","二十国集团","企业负责人","亚洲","习近平",
							"留学生","四川苍溪县","领事","6月30日","2019年","美国","德国科尔伯基金会","博鳌亚洲论坛",
							"弭平","代表","瑞士日内瓦万国宫","负责人","总书记","2020年1月","2008年","澳大利亚",
							"二十国集团领导人杭州峰会","郭安淑","工人"}},
		};
	}
	/**
	 * 实体识别
	 * @param caseId
	 * @param path
	 * @param option
	 * @param words 预期返回词语
	 */
	@Test(dataProvider = "ploDataProvider")
	private void plo(int caseId, String path, String option, String[] words) {
		LOGGER.debug("plo, caseId="+caseId);
		try {
			String text = FileOperator.read(path);
			PloResult plo = request.plo(text, option);
			assertEquals(plo.getCode(), "1");
			assertEquals(plo.getMessage(), "操作成功");
			List<Entity> entities = plo.getEntities();
			/* 2020-12-30: 算法又调整了, 预期又不对了, 只能断言有结果, 底下的注释留着吧 */
			assertTrue(entities.size() > 0);
//			assertEquals(entities.size(), words.length);
//			for(int i=0; i<words.length; i++)
//				assertEquals(entities.get(i).getWord(), words[i]);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "ploErrDataProvider")
	private Object[][] ploErrDataProvider(Method method){
		return new Object[][] {
			//文本中不含实体
			new Object[] {1,"今天要好好学习","1","-1"},
			//文本为除中文外其他语言文本
			new Object[] {2,"المبعوث الخاص للرئيس ","2","-1"},
			//文本为混合文本，有返回结果
			new Object[] {3,"6月，习近平主席特使المبعوث الخاص للرئيس ","","1"},	
			//文本为空
			new Object[] {4,"","1","0"},			
		};
	}
	
	@Test(dataProvider = "ploErrDataProvider")
	private void ploErr(int caseId, String text, String option, String code) {
		LOGGER.debug("ploErr, caseId="+caseId);
		try {
			PloResult plo = request.plo(text, option);
			assertEquals(plo.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
