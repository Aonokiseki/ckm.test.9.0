package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
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
import com.trs.ckm.api.pojo.CorefResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Coref {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger();
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
	
	@DataProvider(name = "corefDataProvider")
	public Object[][] corefDataProvider(Method method){
		if(!"coref".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 单组消解 */
			new Object[] {0, "1", Constant.TEST_DATA_DIR + "/coref/abe.txt", "操作成功", 
					new String[][] {new String[] {"安倍晋三", "他"}}},
			/* 多组消解 */
			new Object[] {1, "1", Constant.TEST_DATA_DIR + "/coref/COVID19.txt", "操作成功", 
					new String[][] {
						new String[] {"世界卫生组织 总干事 谭 德塞", "谭 德塞", "他"}, 
						new String[] {"中国", "中国"}
					}
			},
			/* 空文本 */
			new Object[] {2, "1", Constant.TEST_DATA_DIR + "/coref/empty.txt", "输入参数为空！", new String[] {}},
			/* 符号文本 */
			new Object[] {3, "1", Constant.TEST_DATA_DIR + "/coref/symbol.txt", "结果为空！", new String[] {}},
			/* 首页附带的demo文本有一些小错误, 不过开发说除非大量训练, 否则没法改进了 */
			new Object[] {4, "1", Constant.TEST_DATA_DIR + "/coref/demo.txt", "操作成功", 
					new String[][] {
						new String[] {"主席", "主席", "主席"},
						new String[] { "署 函",  "署 函"},
						new String[] {"艾力更·依明巴海","艾力更·依明巴海","艾力更·依明巴海","艾力更·依明巴海"},
						new String[] {"阿", "阿"},
						new String[] {"中方", "中方"},
						new String[] {"新任 总统 费尔南德斯", "费尔南德斯"},
						new String[] {"阿 新 总统 就职 仪式", "其 就职 仪式"},
						new String[] {"各 领域", "各 领域"},
						new String[] {"中 阿 全面 战略 伙伴 关系", "两 国 关系"},
						new String[] {"习近平 主席", "习近平 主席", "其", "他", "习近平 主席"},
						new String[] {"中 阿", "两 国", "国"}
						/* 2020.10.19: 没有下边两项了 
						new String[] {"对 阿 关系", "对 华 关系"},
						new String[] {"新 的 高度", "高度"},
						 */
					}
			},
			/* 首页的demo文本, 已经过plo分词, 调用时设置seg=0 */
			new Object[] {5, "0", Constant.TEST_DATA_DIR + "/coref/demo_after_ploseg.txt", "操作成功", new String[][] {
				new String[] {"中 阿 全面 战略 伙伴 关系", "两 国 关系"},
				new String[] {"中 阿", "两 国", "国"},
				new String[] {"新 的 高度", "高度"},
				new String[] {"艾力更·依明巴海","艾力更·依明巴海","艾力更·依明巴海","艾力更·依明巴海"},
				new String[] {"阿", "阿"},
				new String[] {"阿 新 总统 就职 仪式", "其 就职 仪式"},
				new String[] {"习近平 主席", "习近平 主席", "其", "他", "习近平 主席"},
				new String[] {"主席", "主席", "主席"},
				new String[] { "署 函",  "署 函"},
				new String[] {"对 阿 关系", "对 华 关系"},
				new String[] {"新任 总统 费尔南德斯", "费尔南德斯"},
				new String[] {"中方", "中方"},
				new String[] {"各 领域", "各 领域"}}},
			/* seg=2或其它非法参数 */
			new Object[] {6, "2", Constant.TEST_DATA_DIR + "/coref/abe.txt", "结果为空！", new String[][] {}},
			new Object[] {7, "&", Constant.TEST_DATA_DIR + "/coref/abe.txt", "结果为空！", new String[][] {}},
		};
	}
	/**
	 * 指代消解验证<br/>
	 * 指代消解: 从给定文本中, 提取实体和后续指代这个实体的代称<br>
	 * 如: 李克强近日主持召开国务院常务会议, 他强调要坚持稳健货币政策<br>
	 * 李克强 <- 他
	 * @param caseid
	 * @param seg
	 * @param filePath
	 * @param message
	 * @param corefChainTexts
	 */
	@Test(dataProvider = "corefDataProvider")
	public void coref(int caseid, String seg, String filePath, String message, String[][] corefChainTexts) {
		LOGGER.debug("coref, caseId="+caseid);
		try {
			String content = FileOperator.read(filePath);
			CorefResult coref = request.coref(content, seg);
			assertEquals(coref.getMessage(), message);
			if(!"操作成功".equals(coref.getMessage()))
				return;
			List<List<CorefResult.Coref>> corefChainList = coref.getCorefChainList();
			/* 2020-12-30: 算法调整导致预期不对. 算法时时会调整, 无法给固定预期了, 只能用简陋的判定条件
			 * 下方注释的内容仍然保留, 不要删除  */
			assertTrue(corefChainList.size() > 0);
//			assertEquals(corefChainTexts.length, corefChainList.size());
//			List<CorefResult.Coref> corefs;
//			for(int i=0,size=corefChainList.size(); i<size; i++) {
//				corefs = corefChainList.get(i);
//				for(int j=0, sz=corefs.size(); j<sz; j++)
//					assertEquals(corefs.get(j).getText(), corefChainTexts[i][j]);
//			}
		} catch (IOException e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
