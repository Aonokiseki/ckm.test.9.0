package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.FormatDateResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.Other;

public class FormDate {
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
	
	@DataProvider(name = "formatDateDataProvider")
	private Object[][] formatDateDataProvider(Method method){
		if(!"formatDate".equals(method.getName()))
			return null;
		return new Object[][] {
			//文本中只有月份，只能根据基准日期推算到年月，日子推算不出来
			new Object[] {1,
					"习近平指出，6月以来，我国江南、华南、西南暴雨明显增多，多地发生洪涝地质灾害，"
					+ "各地区各有关部门坚决贯彻党中央决策部署，全力做好洪涝地质灾害防御和应急抢险救援等工作，"
					+ "防灾救灾取得积极成效。",
					"2020","8","21","1","操作成功","2020","6","0"},
							
			//文本中只有具体日期，可以根据基准日期推算到年月日
			new Object[] {2,
					"狠抓名牌产品质量，不断提高名牌产品的科技含量，是扬名牌产品优势的基础 。"
					+ "前年初，安徽省从全行业7550多种产品中，精心筛选出叉车、液压机、"
					+ "大中 型中高档客车、客车底盘、汽车仪表、柴油机、分马力电机、"
					+ "农用车等40种在国 内外市场上占有率较高、有一定竞争能力又有发展前途的名牌产品，"
					+ "进行重点培育 和支持。通过两年的努力，使大部分产品提高了一个技术档次，"
					+ "增强了在国内外市 场的竞争能力。如全椒柴油机总厂的小型单缸柴油机，"
					+ "不仅独占国内市场鳌头，还 畅销到世界13个国家，前年出口创汇达1160多万美元，"
					+ "跃居全国同行业第一 ，该厂出口创汇又比上年同期增长36%。合肥电机厂的大型轴流潜水电泵，"
					+ "12日在伊朗排灌机械国际招标中，以其质优款新先后战胜了四国竞争强手而中标，"
					+ "当场签订了1200多万美元的供货合同。",
					"2020","8","21","1","操作成功","2020","8","12"},
					
			//文本中含月和日，可以根据基准日期推算到年月日
			new Object[] {3,
					"新华社北京6月28日电 中共中央总书记、国家主席、中央军委主席习近平对防汛救灾工作作出重要指示。",
					"2020","8","21","1","操作成功","2020","6","28"},
					
			//文本中含年月日，基准日期年份大于文本内的年份，以文本内的年份为准，可以根据基准日期推算到年月日
			new Object[] {4,"2019年6月8日", "2020","8","21","1","操作成功","2019","6","8"},
					
			//文本中含年月日，基准日期年份小于文本内的年份，以基准日期年份为准，可以根据基准日期推算到年月日
			new Object[] {5,"2019年6月8日", "2018","8","21","1","操作成功","2018","6","8"},
					
			//基准日期为当前时间，文本内输入昨天
			new Object[] {6,
					"昨天，据了解，道熙科技为网络游戏研发企业，主要产品为网页游戏和移动游戏等，"
					+ "目前，道熙科技已推出《城防三国》《战争霸业》《天下霸域》《真正男子汉》《名将与征服》"
					+ "等多款游戏，前两者是其主要盈利产品。",
					"2020","8","21","1","操作成功","2020","8","20"},
					
			//基准日期为当前时间，文本内输入今天
			new Object[] {7,"今天", "2020","8","21","1","操作成功","2020","8","21"},
					
			//基准日期为当前时间，文本输入多个日期,文本只能输入一个日期
			//new Object[] {7,"今天", "2020","8","21","1","操作成功","2020","8","21"},	
			
			
		};
	}
	
	@Test(dataProvider = "formatDateDataProvider")
	private void formatDate(int caseId, String text, String year, String month, String day,
			String code, String message, String expectedYear, String expectedMonth, String expectedDay) {
		try {
			FormatDateResult formatDate = request.formatDate(text, year, month, day);
			assertEquals(formatDate.getCode(), code);
			assertEquals(formatDate.getMessage(), message);
			assertEquals(formatDate.getYear(), expectedYear);
			assertEquals(formatDate.getMonth(), expectedMonth);
			assertEquals(formatDate.getDay(), expectedDay);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "formatDateErrDataProvider")
	private Object[][] formatDateErrDataProvider(Method method) {
		if(!"formatDateErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//输入的文本不为日期
			new Object[] {1,
					"前几年，按照上级统一规划，村里进行了大规模风貌改造，兴建了通组公路。"
					+ "当时的村支部书记梁和仕忙前忙后快两年，村子大变样，可是从开工到完工，群众“告状”就没断过。  　　"
					+ "“钱根本不经村干部手，村民却总是怀疑我在里面赚了不知道多少钱。修路占了一点菜园，挨着了一点宅基地，"
					+ "补偿肯定是东家多一点，西家少一点。老百姓为了这个，闹了很多矛盾。”"
					+ "当过村支书的梁和仕、梁毓掌也接过话茬：“有时候村里做点工程建设，有村民就以各种理由去阻止施工。”",
					"2020","8","21","1","0"},
			//文本输入的日期为其他语言
			new Object[] {2,"June 8, 2019", "2020","8","21","1","0"},		
			//待处理文本为空
			new Object[] {3,"", "2020","8","21","0","0"},
			//基准日期的日为空
			new Object[] {4,"2019年6月8日", "2020","8","","0","0"},
			//基准日期的月份为空
			new Object[] {5,"2019年6月8日", "2020","","25","0","0"},
			//基准日期的年份为空
			new Object[] {6,"2019年6月8日", "","8","25","0","0"},
			//文本输入多个日期
			new Object[] {7,"2019年6月8日,2020年4月23日", "2020","8","21","1","2020"},
		};
	}
	
	@Test(dataProvider = "formatDateErrDataProvider")
	private void formatDateErr(int caseId, String text, String year, String month, String day, 
			String code,String expectedYear) {
		try {
			FormatDateResult formatDate = request.formatDate(text, year, month, day);
			assertEquals(formatDate.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
