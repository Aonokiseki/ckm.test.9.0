package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.NewwordResult;
import com.trs.ckm.api.pojo.NewwordResult.Result;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class NewWord {
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
	@DataProvider(name = "newWordDataProvider")
	private Object[][] newWordDataProvider(Method method){
		if(!"newWord".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 短文本 */
			new Object[] {1, Constant.TEST_DATA_DIR + "/newWord/1.txt", new String[] {"湿地"}},
			/* 国际新闻 */
			new Object[] {2, Constant.TEST_DATA_DIR + "/newWord/2.txt", 
					new String[] {"发展中国家","合作","病例","抗疫合作","疫情","抗疫","命运共同体","病毒",
							"印度","巴西","病房","形势","物资","防控","防疫","人类","国界","理念","在帮","危机"}},
			/* 财经新闻 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/newWord/3.txt",
					new String[] {"汇算","纳税人","税务部门","税务","税收治理","自然人","税收","个人所得税","自然人纳税人",
							"办税体验","汇算工作","信息","创新","记者","改革","工作","汇算渠道便捷","财经","邮寄","设计","税制",
							"制度","收入", "实践"}},
			/* 军事新闻 */
			new Object[] {4, Constant.TEST_DATA_DIR + "/newWord/4.txt",
					new String[] {"无人机", "MQ-9", "导弹", "型号"}},
			/* 教育新闻 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/newWord/5.txt",
					new String[] {"万元", "上传", "专业", "个人", "中文", "中文课程", "人员", "人生", "价值观", "作文", 
							"侨胞", "侨胞子女", "侨胞家长", "信息", "公开高考满分作文", "内心", "华文", "华文教育", 
							"华文教育基金会", "华文教育基金会中文学校", "发音", "培训", "培训机构", "培训班", "复读", 
							"大学", "媒体", "学习", "学生", "家长", "工作", "巨奖", "平台", "微信","心态", "怎么办", "意义", 
							"成绩", "招生", "招生广告", "教学", "教育", "教育考试院", "教育资源", "教育部", "新闻", 
							"标价", "毕业", "浙江", "海外", "海外侨胞", "清华北大", "湛江", "满分作文", "潜能", "班级", 
							"理由", "生活", "疫情", "百万", "眼识", "短信", "社会监督", "神童", "竞争", "网络", "老师", 
							"考上", "考上清华北大", "考生", "行业", "行为", "规律", "记者", "评卷", "语文评卷组", "诱惑", 
							"课程", "采访", "金钱", "阅卷", "青春", "非洲华文教育基金会", "音频", "骗子", "骗局", "高校", 
							"高考", "高考作文", "高考公平", "高考成绩", "高考满分作文", "高考评卷", "高考阅卷"}},
		};
	}
	
	@Test(dataProvider = "newWordDataProvider")
	private void newWord(int caseId, String path, String newwords[]) {
		try {
			String text = FileOperator.read(path);
			NewwordResult newWord = request.newword(text);
			assertEquals(newWord.getCode(), "1");
			assertEquals(newWord.getMessage(), "操作成功");
			List<Result> words = newWord.getResult();
			assertEquals(words.size(), newwords.length);
			Collections.sort(words, new Comparator<Result>() {
				@Override
				public int compare(Result r1, Result r2) {
					return r1.getWord().compareTo(r2.getWord());
				}
			});
			Arrays.parallelSort(newwords);
			for(int i=0; i<newwords.length; i++)
				assertEquals(words.get(i).getWord(), newwords[i]);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@DataProvider(name = "newWordErrDataProvider")
	private Object[][] newWordErrDataProvider(Method method){
		if(!"newWordErr".equals(method.getName()))
			return null;
		return new Object[][] {
			//文本为除中文以为其他语言，可以正常返回
			new Object[] {1,"المبعوث الخاص للرئيس ","1"},
			//文本为混合文本，可以正常返回
			new Object[] {2,"ا潘塔纳尔湿地是世界上最大的湿地المبعوث الخاص للرئيس  ","1"},
			//文本为空
			new Object[] {3,"","0"},
		};
	}
	
	@Test(dataProvider = "newWordErrDataProvider")
	private void newWordErr(int caseId, String text, String code) {
		try {
			NewwordResult newWord = request.newword(text);
			assertEquals(newWord.getCode(), code);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
