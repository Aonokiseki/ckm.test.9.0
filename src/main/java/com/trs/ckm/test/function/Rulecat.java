package com.trs.ckm.test.function;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.RulecatSimpleResult;
import com.trs.ckm.api.pojo.RulecatSimpleResult.CatResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Rulecat {
	private final static Logger LOGGER = LogManager.getLogger(Rulecat.class);
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
	
	@DataProvider(name = "rulecatSimpleDataProvider")
	private Object[][] rulecatSimpleDataProvider(Method method){
		return new Object[][] {
			/* 地区分类 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/rulecatSimple/1.txt", "地区分类", 
					new String[] {"中国"}},
			new Object[] {2, Constant.TEST_DATA_DIR + "/rulecatSimple/2.txt", "地区分类",
					new String[] {"南美\\阿根廷"}},
			new Object[] {3, Constant.TEST_DATA_DIR + "/rulecatSimple/3.txt", "地区分类",
					new String[] {"中国","北美\\美国","西欧\\英国"}},
			/* QA负面过滤 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/rulecatSimple/4.txt", "QA负面过滤",
					new String[] {"负面","有害\\其他"}},
			new Object[] {5, Constant.TEST_DATA_DIR + "/rulecatSimple/5.txt", "QA负面过滤",
					new String[] {"负面"}},
			new Object[] {6, Constant.TEST_DATA_DIR + "/rulecatSimple/6.txt", "QA负面过滤",
					new String[] {"负面","有害\\社会民生\\从业人员","有害\\突发\\群体性事件"}},
			/* 涉华标签CN */
			new Object[] {7, Constant.TEST_DATA_DIR + "/rulecatSimple/7.txt", "涉华标签CN",
					new String[] {"中央领导\\李克强"}},
			new Object[] {8, Constant.TEST_DATA_DIR + "/rulecatSimple/8.txt", "涉华标签CN",
					new String[] {"地区\\安徽","地区\\北京", "主题\\重大主题\\一带一路\\五通\\设施联通","地区\\台湾",
							"主题\\重大主题\\一带一路","地区\\广东\\地区划分\\深圳市","主题\\重大主题\\一带一路\\原则\\共商",
							"主题\\重大主题\\一带一路\\原则\\共建","主题\\重大主题\\一带一路\\原则\\共享",
							"主题\\一般议题\\经济\\走出去\\高铁","主题\\一般议题\\国际关系\\东盟",
							"主题\\一般议题\\国际关系\\大国关系","主题\\热点议题\\中美贸易战",
							"媒体\\北京日报","媒体\\新华社","中央政府\\外交部"}},
			new Object[] {9, Constant.TEST_DATA_DIR + "/rulecatSimple/9.txt", "涉华标签CN",
					new String[] {"主题\\重大主题\\全球治理","主题\\一般议题\\经济\\行业经济\\农业",
							"主题\\一般议题\\国际关系\\大国关系","主题\\一般议题\\生态环保\\气候变化",
							"主题\\热点议题\\美国大选","主题\\热点议题\\中美贸易战","中央政府\\国防部"}},
			/* 涉华标签EN */
			new Object[] {10, Constant.TEST_DATA_DIR + "/rulecatSimple/10.txt", "涉华标签EN",
					new String[] {"中国"}},
			new Object[] {11, Constant.TEST_DATA_DIR + "/rulecatSimple/11.txt", "涉华标签EN",
					new String[] {"地区\\安徽","地区\\安徽\\行政区划\\合肥市","中国","中央领导\\习近平"}},
			new Object[] {12, Constant.TEST_DATA_DIR + "/rulecatSimple/12.txt", "涉华标签EN",
					new String[] {"地区\\湖南\\行政区划\\益阳市","地区\\江西\\行政区划\\南昌市","地区\\湖北", "地区\\湖南",
							"地区\\安徽", "地区\\江西", "中国", "主题\\一般议题\\经济\\行业经济\\农业","地区\\北京"}},
			//涉华标签RU		
			new Object[] {13, Constant.TEST_DATA_DIR + "/rulecatSimple/13.txt","涉华标签RU",new String[] {"地区\\北京"}},	
			new Object[] {14, Constant.TEST_DATA_DIR + "/rulecatSimple/14.txt", "涉华标签RU",
					new String[] {"主题\\重大主题\\一带一路\\五通\\贸易畅通","主题\\重大主题\\一带一路\\五通\\设施联通",
							"地区\\北京","中央政府\\外交部","地区\\海南","中国"}},	
			new Object[] {15, Constant.TEST_DATA_DIR + "/rulecatSimple/15.txt", "涉华标签RU",
					new String[] {"主题\\重大主题\\一带一路\\五通\\贸易畅通","主题\\重大主题\\一带一路\\五通\\设施联通",
							"中央领导\\习近平","主题\\一般议题\\社会民生\\自然灾害\\地震","中国"}},	
			//涉华标签FR	
			new Object[] {16, Constant.TEST_DATA_DIR + "/rulecatSimple/16.txt", "涉华标签FR",new String[] {"中国"}},	
			new Object[] {17, Constant.TEST_DATA_DIR + "/rulecatSimple/17.txt", "涉华标签FR",
					new String[] {"地区\\湖北\\地区划分\\武汉市","中国"}},	
			new Object[] {18, Constant.TEST_DATA_DIR + "/rulecatSimple/18.txt", "涉华标签FR",
					new String[] {"地区\\湖北","地区\\上海","地区\\广东\\地区划分\\深圳市","地区\\安徽\\地区划分\\宿州市",
							"地区\\湖北\\地区划分\\武汉市","地区\\江苏\\地区划分\\苏州市","地区\\澳门",
							"地区\\四川\\地区划分\\成都市","地区\\香港","媒体\\中国日报","中央政府\\外交部",
							"中央政府\\中国人民银行","主题\\一般议题\\经济\\行业经济\\互联网",
							"主题\\一般议题\\经济\\行业经济\\农业","主题\\一般议题\\科技\\5G","中国"}},	
			//涉华标签JP	
			new Object[] {19, Constant.TEST_DATA_DIR + "/rulecatSimple/19.txt", "涉华标签JP", new String[] {"中国"}},	
			new Object[] {20, Constant.TEST_DATA_DIR + "/rulecatSimple/20.txt", "涉华标签JP",
					new String[] {"地区\\台湾","中国"}},	
			new Object[] {21, Constant.TEST_DATA_DIR + "/rulecatSimple/21.txt", "涉华标签JP",
					new String[] {"地区\\湖北","地区\\湖北\\地区划分\\恩施州","地区\\山东","地区\\上海","地区\\天津",
							"地区\\广东\\地区划分\\深圳市","地区\\河北\\地区划分\\石家庄市","地区\\澳门","地区\\北京",
							"地区\\湖北\\地区划分\\武汉市","地区\\江苏\\地区划分\\南京市","地区\\江苏\\地区划分\\无锡市",
							"地区\\广东","地区\\山东\\地区划分\\潍坊市","地区\\广东\\地区划分\\广州市","地区\\四川\\地区划分\\成都市",
							"地区\\广东\\地区划分\\惠州市","地区\\香港","地区\\浙江\\地区划分\\杭州市","地区\\重庆","媒体\\光明日报",
							"媒体\\科技日报","媒体\\人民日报","媒体\\新华社","企业\\华为技术有限公司","中央政府\\外交部",
							"主题\\一般议题\\经济\\行业经济\\互联网","主题\\一般议题\\科技\\5G","主题\\一般议题\\科技\\大数据",
							"主题\\一般议题\\科技\\人工智能","主题\\一般议题\\文化\\电影","中国"}},	
						
			//涉华标签ES		
			new Object[] {22, Constant.TEST_DATA_DIR + "/rulecatSimple/22.txt", "涉华标签ES",
					new String[] {"中国"}},	
			new Object[] {23, Constant.TEST_DATA_DIR + "/rulecatSimple/23.txt", "涉华标签ES",
					new String[] {"地区\\湖北\\地区划分\\武汉市","中国"}},	
			new Object[] {24, Constant.TEST_DATA_DIR + "/rulecatSimple/24.txt", "涉华标签ES",
					new String[] {"地区\\河北","地区\\广东\\地区划分\\深圳市","地区\\安徽\\地区划分\\宿州市",
							"地区\\河北\\地区划分\\廊坊市","地区\\湖北\\地区划分\\武汉市","地区\\江苏\\地区划分\\苏州市",
							"地区\\上海","地区\\四川\\地区划分\\成都市","地区\\香港","企业\\华为技术有限公司",
							"中央领导\\习近平","中央政府\\中国人民银行","主题\\一般议题\\经济\\行业经济\\互联网",
							"主题\\一般议题\\经济\\行业经济\\农业","主题\\一般议题\\科技\\5G","中国"}},
		};
	}
	/**
	 *   规则分类检查
	 * @param caseId
	 * @param path
	 * @param model
	 * @param cats 预期的分类结果
	 */
	@Test(dataProvider = "rulecatSimpleDataProvider")
	private void rulecatSimple(int caseId, String path, String model, String[] cats) {
		LOGGER.debug("rulecatSimple, caseId="+caseId);
		try {
			String content = FileOperator.read(path);
			RulecatSimpleResult rulecat = request.rulecatSimple(content, model);
			assertEquals(rulecat.getCode(), "1");
			/* 2020-12-31: 有一条case触发了警告: 
			 * 无法获取部分结果的命中规则及匹配词，请增大/config/application.yml中trs.ckm.rulecat.max-rule-length及
			 * 规则分类模板configrule.txt中的keep_rules_len 
			 * 
			 * 但是两个值都扩大为原来的10倍, 仍然有警告, 也就是message的信息不是“操作成功”
			 * 对分类结果没影响，只是匹配词和命中规则可能不完整
			 * 如果是底层的C代码变动导致的, 不好调整
			 * 遇到failure的情况是一定的 */
			assertEquals(rulecat.getMessage(), "操作成功");
			List<CatResult> results = rulecat.getCatResults();
			/* 2020-12-30: 算法调整, 对 result 预期可能又不对了, 注释的内容不要物理删除, 就这么放着 */
			/* 2020-1-14: 开发组改了, 可以深入验证了 */
			assertTrue(results.size() > 0);
			assertEquals(results.size(), cats.length);
			Collections.sort(results, new Comparator<CatResult>(){
				@Override
				public int compare(CatResult o1, CatResult o2) {
					return o1.getCat().compareTo(o2.getCat());
				}
			});
			Arrays.sort(cats);
			for(int i=0; i<cats.length; i++)
				assertEquals(results.get(i).getCat(), cats[i]);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "rulecatSimpleErrDataProvider")
	private Object[][] rulecatSimpleErrDataProvider(Method method){
		if(!"rulecatSimpleErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//模板为不存在模板
			new Object[] {1,"中国", "嘿哈","0"},	
			//模板为空
			new Object[] {2,"中国", "","0"},
			//文本为泰语
			new Object[] {3,"ภาษาจีน", "地区分类","-1"},
			//文本为混合文本，可以正常返回
			new Object[] {4,"ภาษาจีน中国", "地区分类","1"},
			//文本中不含涉华标签
			new Object[] {5,"将水资源合作推向新高度。中方愿在力所能及的范围内，为各国更好利用水资源提供更多帮助。"
					+ "中方将从今年开始与湄公河国家分享澜沧江全年水文信息，共建澜湄水资源合作信息共享平台，更好应对洪旱灾害。"
					+ "定期举办水资源合作部长级会议和水资源合作论坛，实施好大坝安全、洪水预警等合作项目，"
					+ "提升流域综合治理和水资源管理能力。", "涉华标签CN","-1"},
			//文本为空
			new Object[] {6,"", "涉华标签CN","0"},
		};
	}
	
	@Test(dataProvider = "rulecatSimpleErrDataProvider")
	private void rulecatSimpleErr(int caseId, String content, String model, String code) {
		LOGGER.debug("rulecatSimpleErr, caseId="+caseId);
		try {
			assertEquals(request.rulecatSimple(content, model).getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
