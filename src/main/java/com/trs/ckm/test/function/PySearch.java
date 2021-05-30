package com.trs.ckm.test.function;

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
import com.trs.ckm.api.pojo.PySearchResult;
import com.trs.ckm.api.pojo.PySearchResult.PinyinItem;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.Other;

public class PySearch {
	private final static Logger LOGGER = LogManager.getLogger(PySearch.class);
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
	
	@DataProvider(name = "pySearchDataProvider")
	public Object[][] pySearchDataProvider(Method method){
		if(!"pySearch".equals(method.getName()))
			return null;
		return new Object[][] {
			 //demo-中文模型
			new Object[] {1,"ceshi",
					"demo","1","操作成功",new String[] {"测试","侧室"}},
			new Object[] {2,"jisuanji",
					"demo","1","操作成功",new String[] {"计算机"}},
			new Object[] {3,"hehe",
					"demo","1","操作成功",new String[] {"和合","和和","喝喝","嗬嗬"}},
			new Object[] {4,"qingse",
					"demo","1","操作成功",new String[] {"情色","青涩","青色"}},
			new Object[] {5,"zhidao",
					"demo","1","操作成功",new String[] {"知道","指导","之道","直到","直捣","直道","执导","至道","只道","制导"}},
			new Object[] {6,"ziji",
					"demo","1","操作成功",new String[] {"自己","字迹","自给","仔鸡"}},
			new Object[] {7,"xiaoxixi",
					"demo","1","操作成功",new String[] {"笑嘻嘻"}},
			new Object[] {8,"yeyijiri",
					"demo","1","操作成功",new String[] {"夜以继日"}},
			new Object[] {9,"xuexi",
					"demo","1","操作成功",new String[] {"学习","学系","血洗","学戏","学熙"}},
			new Object[] {10,"qianlvjiqiong",
					"demo","1","操作成功",new String[] {"黔驴技穷"}},
			new Object[] {11,"shangshangxiaxia",
					"demo","1","操作成功",new String[] {"上上下下"}},
			/* 有bug，输入xixi，会把蜥蜴和栖息都检索到*/
			/* 2020.10.19腊和栖有读音xi, 不算问题, 蜥蜴被检索出这个现象提交到禅道*/
			new Object[] {12,"xixi",
					"demo","1","操作成功",
					new String[] {"希腊", "嬉戏", "栖息", "西溪", "西西", "细细", "洗洗", "兮兮", "嘻嘻", "习习",
							"熙熙", "息息", "曦曦"}},
		};
	}
	
	@Test(dataProvider = "pySearchDataProvider")
	public void pySearch(int caseId, String pinyin, String model, 
			String code, String message, String[] pinyins) {
		LOGGER.debug("pySearch, caseId="+caseId);
		try {
			PySearchResult pySearch = request.pySearch(pinyin, model);
			assertEquals(pySearch.getCode(), code);
			assertEquals(pySearch.getMessage(), message);
			List<PinyinItem> pinyinItems = pySearch.getPinyinItems();
			Comparator<PinyinItem> pinyinComparator = new Comparator<PinyinItem>() {
				@Override
				public int compare(PinyinItem o1, PinyinItem o2) {
					return o1.getPy().compareTo(o2.getPy());
				}
			};
			Collections.sort(pinyinItems, pinyinComparator);
			Arrays.sort(pinyins);
			for(int i=0,size=pinyinItems.size(); i<size; i++)
				assertEquals(pinyinItems.get(i).getPy(), pinyins[i]);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "pySearchErrDataProvider")
	public Object[][] pySearchErrDataProvider(Method method){
		if(!"pySearchErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//模板为不存在模板
			new Object[] {1,"ceshi","demo1","0"},
			//模板为空，取默认模板demo
			new Object[] {2,"ceshi","","1"},
			//检索串为英文
			new Object[] {3,"test","demo","-1"},
			//检索串为一段话
			new Object[] {4,"woxiangkandianying","demo","-1"},
			//检索串为空
			new Object[] {5,"","demo","0"},
		};
	}
	
	@Test(dataProvider = "pySearchErrDataProvider")
	public void pySearchErr(int caseId, String pinyin, String model, String code) {
		LOGGER.debug("pySearchErr, caseId="+caseId);
		try {
			assertEquals(request.pySearch(pinyin, model).getCode(), code);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
