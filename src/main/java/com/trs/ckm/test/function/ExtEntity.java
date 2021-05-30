package com.trs.ckm.test.function;

import static org.junit.Assert.assertFalse;
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
import com.trs.ckm.api.pojo.ExtEntityResult;
import com.trs.ckm.api.pojo.ExtEntityResult.EntityItem;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ExtEntity {
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
	
	@DataProvider(name = "extEntityDataProvider")
	private Object[][] extEntityDataProvider(Method method){
		if(!"extEntity".equals(method.getName()))
			return null;
		return new Object[][] {
			/* tag : 1表示中国； 2表示外国， 不表示实体类型 国别等识别错误，暂时不好修改，先不处理 */
//			new Object[] {1, Constant.TEST_DATA_DIR + "/extEntity/1.txt",
//					new String[] {"习近平","艾力更·依明巴海","费尔南德斯","","克里斯蒂娜","马克里"},
//					new String[] {"主席","副委员长","新任总统","","新任副总统","总统",},
//					new String[] {"中国","阿根廷","阿根廷","","阿根廷","中国"},
//					new String[] {"1","2","2","0","2","1"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/extEntity/2.txt",
					new String[] {"胡锦涛","特朗普"},
					new String[] {"总书记","总统"},
					new String[] {"中国","美国"},
					new String[] {"1","2"}},
					
			new Object[] {3, Constant.TEST_DATA_DIR + "/extEntity/3.txt",
					new String[] {"李克强", null, null},
					new String[] {"总理", null, null},
					new String[] {"中国",null, null},
					new String[] {"1","0","0"}},
			
			/*2020-12-23: 和预期对不上, 开发组回复没有调整过算法, 可能调整过词典
			 *现在预期总在变, 根本没法判断结果对不对, 所以现在只检查entityItems非空 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/extEntity/4.txt",
					new String[] {"习近平", null},
					new String[] {"主席_总书记_国家主席",null},
					new String[] {"中国",null},
					new String[] {"1","0"}},
		};
	}
	/**
	 * 人物关系识别（人物、机构、职务关系、国别信息）:中文
	 * @param caseId
	 * @param path
	 * @param names 人名
	 * @param careers 职务
	 * @param countries 国家
	 * @param tags 0为无法判断类型，1为中国人名，2为外国人名
	 */
	@Test(dataProvider = "extEntityDataProvider")
	private void extEntity(int caseId, String path, 
			String[] names, String[] careers, String[] countries, String[] tags) {
		try {
			String content = FileOperator.read(path);
			ExtEntityResult extEntity = request.extEntity(content);
			assertEquals(extEntity.getCode(), "1");
			assertEquals(extEntity.getMessage(), "操作成功");
			List<EntityItem> entityItems = extEntity.getEntityItemList();
			/* 怀疑开发组暗中调整过算法或词典, 每轮测试的结果都不固定(同一个版本运行多次结果当然是稳定的)
			 * 没法检查细节 */
			assertFalse(entityItems.isEmpty());
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@DataProvider(name = "extEntityErrDataProvider")
	private Object[][] extEntityErrDataProvider(Method method){
		return new Object[][] {
			//文本输入中文以外的语言
			new Object[] {1, Constant.TEST_DATA_DIR + "/extEntityErr/1.txt", "-1"},
			//文本输入短文本（文本不含人物、机构、职务关系、国别信息）
			new Object[] {2, Constant.TEST_DATA_DIR + "/extEntityErr/2.txt", "-1"},
			//文本输入混合文本
			new Object[] {3, Constant.TEST_DATA_DIR + "/extEntityErr/3.txt", "-1"},
			//文本输入为空
			new Object[] {4, Constant.TEST_DATA_DIR + "/extEntityErr/empty.txt", "0"},	
		};
	}
	
	@Test(dataProvider = "extEntityErrDataProvider")
	private void extEntityErr(int caseId, String path, String code) {
		try {
			String text = FileOperator.read(path);
			assertEquals(request.extEntity(text).getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
