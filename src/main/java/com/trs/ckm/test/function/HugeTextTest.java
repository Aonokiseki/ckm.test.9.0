package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.SimCreateResult;
import com.trs.ckm.api.pojo.SimDeleteIndexResult;
import com.trs.ckm.api.pojo.SimDeleteModelResult;
import com.trs.ckm.api.pojo.SimRetrieveResult;
import com.trs.ckm.api.pojo.SimUpdateResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;
/*
 * 为了验证大文本发送给CKM是否会触发进程崩溃等严重错误的测试类
 * 
 * 2020-12-15:
 * 当前CKM既没有全局限制文本上传大小,也没有内部超时的机制
 * 也不准备添加, 这容易造成请求在CKM后台堆积,很久之后可能才有返回值
 * (当然, 这是建立在所有接口都不会造成进程崩溃的前提下)
 * 
 * 这样有个问题:
 * 运行此测试类,会有大批量的超时异常, passed的测试方法可能只有稀少的几个。在内部,CKM也许还在处理请求,
 * 但客户端(测试代码)已经返回了超时异常，这样连code和message都没有, 很难得知这个请求当前真正的状态。
 * 所以我们可以, 不, 就把要求定为运行完这些测试方法, CKM的进程仍然存在即可。
 * 虽然在测试代码中仍然要求code返回0会判定为failure, 不过能有code返回都已经是件很不错的事了ﾚ(ﾟ∀ﾟ;)ﾍ=3=3=3
 * 
 * 那么问题来了
 * 问: 大文本堆积, 空占资源, 导致后续有新请求发不过去了怎么办?
 * 答: 等不及的话可以杀死进程,重开
 * 问: 这样和直接触发进程崩溃有什么区别? 还不如直接让进程崩溃了……
 * 答: _(:з」∠)_  
 */
public class HugeTextTest {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private String text;
	private final static String HUGE_TEXT_PATH = "./testdata/input/specialText/novel.txt";
	private final static Logger LOGGER = LogManager.getLogger();
	
	@BeforeClass
	private void before() {
		Constant.reconfigureLog4j2();
		context = new AnnotationConfigApplicationContext(AspectConfig.class);
		request = context.getBean(TRSCkmRequest.class);
		try {
			LOGGER.info(String.format("FileOperator.read(%s)", HUGE_TEXT_PATH));
			text = FileOperator.read(HUGE_TEXT_PATH);
			LOGGER.info(String.format("Loading file succeed, file.length=%d", text.length()));
		}catch(IOException e) {
			LOGGER.error(Other.stackTraceToString(e));
			text = "";
		}
	}
	@AfterClass
	private void after() {
		if(context != null)
			context.close();
	}
	@Test
	public void abs() {
		try {
			assertNotEquals(request.abs(text, "news-cn", "", "12", "20").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void absTheme() {
		try {
			assertNotEquals(request.absTheme(text, "news-cn", "6").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void absLabel() {
		try {
			assertNotEquals(request.absLabel(text, "demo", "").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void autocat() {
		try {
			assertNotEquals(request.autocat(text, "demo").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void appraiseEmotion() {
		try {
			assertNotEquals(request.appraiseEmotion(text, "appraise_general").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void appraiseOpinionNews() {
		try {
			assertNotEquals(request.appraiseOpinionNews(text, "config.ini").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void appraiseFt() {
		try {
			assertNotEquals(request.appraiseFt(text, "ft_appraise_en").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	
	@Test
	public void ChineseTs() {
		try {
			assertNotEquals(request.chineseTs(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void collationCommon() {
		try {
			assertNotEquals(request.dlcollate(text, "demo").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void collationEn() {
		try {
			assertNotEquals(request.collationEn(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void coref() {
		try {
			assertNotEquals(request.coref(text, "1").getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dySearch() {
		try {
			assertNotEquals(request.dySearch(text, "demo", "0"), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void entityLink() {
		try {
			assertNotEquals(request.entityLink(text, "唐三").getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void eventQk() {
		try {
			assertNotEquals(request.eventQk("", text).getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void extEntity() {
		try {
			assertNotEquals(request.extEntity(text).getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void extExtract() {
		try {
			assertNotEquals(request.extExtract(text, "demo").getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void fastTextClassifySeg() {
		try {
			assertNotEquals(request.fastTextClassifySeg(text, "10", "demo"), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void formatDate() {
		try {
			assertNotEquals(request.formatDate(text, "2020", "12", "3"), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void indicators() {
		try {
			assertNotEquals(request.indicators(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void language() {
		try{
			assertNotEquals(request.language(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void ner() {
		try {
			assertNotEquals(request.ner(text).getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void newword() {
		try {
			assertNotEquals(request.newword(text).getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void parserEng() {
		try {
			assertNotEquals(request.parserEng(text, "1").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void parserSyntax() {
		try {
			assertNotEquals(request.parserSyntax(text, "1"), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void plo() {
		try {
			assertNotEquals(request.plo(text, "1").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void ploseg() {
		try {
			assertNotEquals(request.ploSeg(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void posEng() {
		try {
			assertNotEquals(request.posEng(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void pySearch() {
		try {
			assertNotEquals(request.pySearch(text, "demo").getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void rdf() {
		try {
			assertNotEquals(request.extRdf(text, "rdf_person", "3").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void robotitle() {
		try {
			assertNotEquals(request.roboTitle(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void rulecat() {
		try {
			assertNotEquals(request.rulecatSimple(text, "地区分类").getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void seg() {
		try {
			assertNotEquals(request.seg(text).getCode(), Constant.FAILURE);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	/*
	 * sim接口比较麻烦，建库,入库，查询, 删除索引, 删库……
	 */
	@Test
	public void sim() {
		try {
			String model = "novel";
			String id = UUID.randomUUID().toString();
			SimCreateResult create = request.simCreate(model);
			assertEquals(create.getCode(), Constant.SUCCESS);
			SimUpdateResult update = request.simUpdate(text, id, model);
			assertEquals(update.getCode(), Constant.SUCCESS);
			SimRetrieveResult retrieve = request.simRetrieve(text, model, "3", "0.5");
			assertEquals(retrieve.getCode(), Constant.SUCCESS);
			SimDeleteIndexResult deleteIndex = request.simDeleteIndex(id, model);
			assertEquals(deleteIndex.getCode(), Constant.SUCCESS);
			SimDeleteModelResult delete = request.simDeleteModel(model);
			assertEquals(delete.getCode(), Constant.SUCCESS);
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void stsim() {
		try {
			assertNotEquals(request.stsim("CN", text, text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void textCompare() {
		try {
			assertNotEquals(request.textCompare(text, text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void textCompareMd5Tag() {
		try {
			assertNotEquals(request.textCompareMd5Tag(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlabs() {
		try {
			assertEquals(request.dlabs(text, "bert_abs"), Constant.SUCCESS);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlcat() {
		try {
			assertEquals(request.dlcat(text, "cat_demo").getCode(), Constant.SUCCESS);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlevent() {
		try {
			assertNotEquals(request.dlevent(text, "bert_event").getCode(),Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlMlEmojiExt() {
		try {
			assertNotEquals(request.dlMlEmojiExt(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlMlEmojiStrip() {
		try {
			assertNotEquals(request.dlMlEmojiStrip(text).getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlner() {
		try {
			assertNotEquals(request.dlner(text, "ner_demo").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlnre() {
		try {
			assertNotEquals(request.dlnre(text, "bert_nre_en2").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void dlcollate() {
		try {
			assertNotEquals(request.dlcollate(text, "bert_collate").getCode(), Constant.FAILURE);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
	@Test
	public void md5() {
		try {
			assertEquals(request.simMd5(text).getCode(), Constant.SUCCESS);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}
}
