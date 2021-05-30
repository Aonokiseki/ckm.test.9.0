package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.AbsResult;
import com.trs.ckm.api.pojo.SimCountResult;
import com.trs.ckm.api.pojo.SimCreateResult;
import com.trs.ckm.api.pojo.SimDeleteIndexResult;
import com.trs.ckm.api.pojo.SimDeleteModelResult;
import com.trs.ckm.api.pojo.SimRetrieveResult;
import com.trs.ckm.api.pojo.SimRetrieveResult.RevDoc;
import com.trs.ckm.api.pojo.SimTruncateResult;
import com.trs.ckm.api.pojo.SimUpdateResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Sim {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger(LogManager.getLogger());
	private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
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
	
	@DataProvider(name = "retrieveDataProvider")
	public Object[][] retrieveDataProvider(Method method){
		if(!"retrieve".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 单个结果 */
			new Object[] {1, "custom", "中国共产党在国共内战中击败中华民国国军, 1949年10月1日在北京成立中华人民共和国", 
					"2", "0.4", "操作成功", new String[] {"1"} },
			/* 多个结果 */
			new Object[] {2, "custom", 
					"中国共产党, 率领中国人民解放军击败中华民国国军, 于1949年10月1日在北京成立中华人民共和国, 即中国. "
					+ "中国人民解放军根据中央军委作出的规定, 团以上的部队均冠以\"中国人民解放军\"并沿用至今",
					"3", "0.3", "操作成功", new String[] {"2", "1"}},
			/* 顾名思义, 篇章级相似检索, 查询串过短, 结果为空是正常的 */
			new Object[] {3, "custom", "中国", "10", "0.1", "结果为空！", new String[] {}},
			/* revNum给0, 意义为不限制数量, 此时人工限制阈值30%, 应该返回2个*/
			new Object[] {4, "custom", 
					"中国共产党, 率领中国人民解放军击败中华民国国军, 于1949年10月1日在北京成立中华人民共和国, 即中国. "
					+ "中国人民解放军根据中央军委作出的规定, 团以上的部队均冠以\"中国人民解放军\"并沿用至今", 
					"0", "0.3", "操作成功", new String[] {"2", "1"}},
			/* 令revNum的值小于实际返回的数量, 优先返回相似度更高的; 注意和上一个case的区别, 这里同时限制了revNum 和 threshold */
			new Object[] {5, "custom", 
					"中国共产党, 率领中国人民解放军击败中华民国国军, 于1949年10月1日在北京成立中华人民共和国, 即中国."
					+ "中国人民解放军根据中央军委作出的规定, 团以上的部队均冠以\"中国人民解放军\"并沿用至今",
					"1", "0.2", "操作成功", new String[] {"2"}},
			/* 令阈值为0.0, 意味着不限制阈值, 手工限制返回数量为1, 应该只返回相关度最高的那1个 */
			new Object[] {6, "custom",
					"中国共产党, 率领中国人民解放军击败中华民国国军, 于1949年10月1日在北京成立中华人民共和国, 即中国. "
					+ "中国人民解放军根据中央军委作出的规定, 团以上的部队均冠以\"中国人民解放军\"并沿用至今",
					"1", "0.0", "操作成功", new String[] {"2"}},
			/* 同时令revNum 和 threshold 为0, 按照开发手册的设定, 判定为非法, 应当返回“输入参数不合法！” */
			/* 2020.8.26: 返回了所有的结果, 和手册冲突 */
			/* 2020.10.15: 开发团队新的规定, 同时为0, 返回相似度最高的前3个 */
			new Object[] {7, "custom", 
					"中国共产党, 率领中国人民解放军击败中华民国国军, 于1949年10月1日在北京成立中华人民共和国, 即中国. "
					+ "中国人民解放军根据中央军委作出的规定, 团以上的部队均冠以\"中国人民解放军\"并沿用至今",
					"0", "0.0", "操作成功", new String[] {"2", "1", "0"}},
			/* revNum设置为符号, 内部自动视为0, 也就是不限制数量 */
			new Object[] {8, "custom", "中国共产党在国共内战中击败中华民国国军, 1949年10月1日在北京成立中华人民共和国", 
					"!@#$%^&*()", "0.4", "操作成功", new String[] {"1"} },
			/* 输入索引库为空 */
			new Object[] {9, "", "中国共产党在国共内战中击败中华民国国军, 1949年10月1日在北京成立中华人民共和国", 
					"1", "0.4", "该模板不存在！", new String[] {}},
			/* 检索串为空 */
			new Object[] {10, "custom", "", "1", "0.4", Constant.INPUT_PARAMETER_IS_EMPTY, new String[] {}},
			/* 其它参数为空串, 内部自动改为0.0f */
			new Object[] {11, "custom", "中国共产党在国共内战中击败中华民国国军, 1949年10月1日在北京成立中华人民共和国", 
					"1", "", "操作成功", new String[] {"1"}}
		};
	}
	/**
	 * 篇章级文本相似度查询
	 * @param caseId
	 * @param model
	 * @param content
	 * @param revnum
	 * @param threshold
	 * @param msg
	 * @param expected
	 */
	@Test(dataProvider = "retrieveDataProvider")
	public void  retrieve(
			int caseId, 
			String model, 
			String content, 
			String revnum, 
			String threshold,
			String msg,
			String[] expected
			) {
		LOGGER.info("retrieve, caseId="+caseId);
		String dbName =  "custom";
		String[] texts = new String[] {
				/* 1 */
				"中华人民共和国, 简称\"中国\", 成立于1949年10月1日, 位于亚洲东部,太平洋西岸, 是工人阶级领导的, "
				+ "以工农联盟为基础的人民民主专政的社会主义国家",
				/* 2 */
				"中国共产党, 简称中共, 创建于1921年7月23日, 历经多年国共内战, "
				+ "中国共产党在中国大陆和绝大多数沿海岛屿取得全面胜利, 率领中国人民解放军打败中华民国国军,"
				+ "迫使中华民国政府退守台澎金马,并在1949年于北京建立中华人民共和国",
				/* 3 */
				"中国人民解放军, 是中华人民共和国最主要的武装力量, 第一次使用此称谓出现在1946年。"
				+ "1948年11月1日中央军委作出<<关于全军组织及部队番号的规定>>, 团以上各部队均冠以\"中国人民解放军\"字样。"
				+ "从此,\"中国人民解放军\"的名称一直沿用至今",
				/* 4 */
				"新型冠状病毒肺炎(Corona Virus Disease 2019, COVID-19), 简称\"新冠肺炎\", "
				+ "世界卫生组织命名为\"2019冠状病毒病\", 是指2019新型冠状病毒感染导致的肺炎。2019年12月以来, "
				+ "湖北省武汉市部分医院陆续发现了多例有华南海鲜市场暴露史的不明原因肺炎病例, "
				+ "现已证实为2019新型冠状病毒感染引起的机型呼吸道传染病"
		};
		try {
			assertEquals(request.simCreate(dbName).getCode(), "1");
			for(int i=0; i<texts.length; i++) 
				assertEquals(request.simUpdate(texts[i], String.valueOf(i), dbName).getCode(), "1");
			assertEquals(request.simFlush(dbName).getCode(), "1");
			SimRetrieveResult retrieve = request.simRetrieve(content, model, revnum, threshold);
			assertEquals(retrieve.getMessage(), msg);
			if(!"操作成功".equals(retrieve.getMessage()))
				return;
			List<RevDoc> revdocs = retrieve.getRevDoc();
			assertEquals(Integer.valueOf(retrieve.getRevNum()).intValue(), revdocs.size());
			assertEquals(revdocs.size(), expected.length);
			for(int i=0, size= revdocs.size(); i<size; i++) {
				assertEquals(revdocs.get(i).getId(), expected[i]);
			}
				
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		} finally {
			try {
				request.simDeleteModel(dbName);
			}catch(IOException e) {}
		}
	}
	/**
	 * 相似检索, 通过批量文本检查
	 */
	@Test
	public void batchRetrieve() {
		String simDatabase = "custom";
		SimUpdateResult updateResult;
		String content;
		AbsResult absResult;
		SimCountResult countResult;
		double rate;
		SimRetrieveResult retrieveResult;
		try {
			LOGGER.debug("batchRetrieve()");
			/* 建立相似库 */
			SimCreateResult createResult = request.simCreate(simDatabase);
			assertEquals(createResult.getCode(), "1");
			/* 遍历目录 */
			List<File> files = FileOperator.traversal(Constant.TEST_DATA_DIR + "/sim", "txt", false);
			for(int i=0, size=files.size(); i<size; i++) {
				/* 获取文件内容 */
				content = FileOperator.read(files.get(i).getAbsolutePath());
				/* 更新到索引库 */
				updateResult = request.simUpdate(content, String.valueOf(i), simDatabase);
				assertEquals(updateResult.getCode(), "1");
				request.simFlush(simDatabase);
				/* 检索, 文章入库检查 */
				countResult = request.simCount(String.valueOf(i), simDatabase);
				assertEquals("1", countResult.getResult());
				/* 先用给定文章调用摘要抽取接口, 获得文章摘要, 然后将摘要作为相似检索的待检文本调用相似检索接口
				 * 摘要抽取提供的参数percent, 同时作为相似检索的阈值, 保证只命中1条记录, 方便检查
				 * percent, 为 20% ~ 80% 中的一个随机值 */
				rate = Math.random() * 0.6 + 0.2;
				absResult = request.abs(content, "", "", "", String.valueOf((int)(rate * 100)));
				retrieveResult = request.simRetrieve(absResult.getAbs(), simDatabase, "10", String.valueOf(rate));
				assertEquals("1", retrieveResult.getRevNum());
				assertEquals(retrieveResult.getRevDoc().get(0).getId(), String.valueOf(i));
			}
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		} finally {
			try {
				request.simDeleteModel("custom");
			}catch(Exception e) {
				fail(Other.stackTraceToString(e));
			}
		}
	}
	
	@DataProvider(name = "createDataProvider")
	public Object[][] createDataProvider(Method method){
		Object[][] result = null;
		if("create".equals(method.getName())) {
			result = new Object[][] {
				/* 一般情况 */
				new Object[] {1, "custom", "操作成功"},
				/* 支持下划线 */
				new Object[] {2, "_", "操作成功"},
				/* 特殊字符 */
				new Object[] {3, "!", "输入参数不合法！"},
				new Object[] {4, "@", "输入参数不合法！"},
				new Object[] {5, "%", "输入参数不合法！"},
				new Object[] {6, "*", "输入参数不合法！"},
				/* 重复建表 */
				new Object[] {7, "demo", "该记录已经存在！"},
				/* 中文表 */
				new Object[] {8, "相似新闻", "操作成功"},
				/* 表名为空 */
				new Object[] {9, "", "输入参数为空！"},
				/* 超长串, linux限制为255 */
				new Object[] {10, new RandomStringGenerator.Builder().withinRange(new char[] {'a', 'z'}).build().generate(255), "操作成功"}
			};
		}
		return result;
	}
	
	/**
	 * 相似库创建检查
	 * @param caseId
	 * @param simDatabaseName
	 * @param expectation
	 */
	@Test(dataProvider = "createDataProvider")
	public void create(int caseId, String simDatabaseName, String expectation) {
		LOGGER.debug("create, caseId="+caseId);
		try {
			/* 2020.08.25: 多库是测试用的，目前存在漏检问题，更新后会删除创建多库格式的相似库功能。
			 * 所以 type 直接给定0; 更新后注意同步修改simCreate()方法 */
			SimCreateResult create = request.simCreate(simDatabaseName);
			assertEquals(create.getMessage(), expectation);
			if("success".equals(create.getMessage())) {
				SimUpdateResult update = request.simUpdate("中华人民共和国", "1", simDatabaseName);
				assertEquals(update.getMessage(), "success");
			}
		} catch (Exception e) {
			assertEquals(e.getMessage(), expectation);
		} finally {
			if("demo".equals(simDatabaseName))
				return;
			try {
				request.simDeleteModel(simDatabaseName);
			}catch(IOException e) {}
		}
	}
	
	@DataProvider(name = "updateDataProvider")
	public Object[][] updateDataProvider(Method method){
		if(!"update".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 一般情况不验证了, 只验证异常情况 */
			/* 情况1: 模板名为空 */
			new Object[] {1, "create", "insertion records", "1", "", "0"},
		};
	}
	
	@Test(dataProvider = "updateDataProvider")
	public void update(int caseId, String createModel, String content, String id, String updateModel, String code) {
		try {
			SimCreateResult create = request.simCreate(createModel);
			assertEquals(create.getCode(), Constant.SUCCESS);
			SimUpdateResult update = request.simUpdate(content, id, updateModel);
			assertEquals(update.getCode(), code);
		} catch(Exception e) {
		} finally {
			try {
				request.simDeleteModel(createModel);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@DataProvider(name = "deleteDataProvider")
	public Object[][] deleteDataProvider(Method method){
		Object[][] result = null;
		if(!"delete".equals(method.getName()))
			return null;
		result = new Object[][] {
			/* 正常情况 */
			new Object[] {1, "custom", "custom", "操作成功"},
			/* 异常情况, 删除不存在的表 */
			new Object[] {2, "custom", "custom1", "该模板不存在！"},
			/* 异常情况, 删除参数为空 */
			new Object[] {3, "custom", "", "输入参数为空！"}
		};
		return result;
	}
	
	@Test(dataProvider = "deleteDataProvider")
	public void delete(int caseId, String simDatabaseName, String whichYouWannaToDelete, String expected) {
		LOGGER.debug("delete, caseId="+caseId);
		try {
			assertEquals(request.simCreate(simDatabaseName).getMessage(), "操作成功");
			assertEquals(request.simUpdate("中华人民共和国", "1", simDatabaseName).getMessage(), "操作成功");
			try {
				SimDeleteModelResult delete = request.simDeleteModel(whichYouWannaToDelete);
				assertEquals(delete.getMessage(), expected);
			}catch(Exception e) {
				assertEquals(e.getMessage(), expected);
			}
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		} finally {
			try {
				request.simDeleteModel(simDatabaseName);
			} catch (IOException e) {}
		}
	}
	
	@DataProvider(name = "countDataProvider")
	public Object[][] countDataProvider(Method method){
		Object[][] result = null;
		if(!"count".equals(method.getName()))
			return result;
		result = new Object[][] {
			/* 正常情况 */
			new Object[] {1, 3, "0", "custom", "操作成功", "5"},
			/* 正常情况: 每个文件一个id */
			new Object[] {2, 999, "1", "custom", "操作成功", "1"},
			/* 输入为空 */
			new Object[] {3, 3, "0", "", "该模板不存在！", null},
			/* 从一个不存在的库中获取 */
			new Object[] {4, 3, "0", "custom1", "该模板不存在！", null}
		};
		return result;
	}
	
	@Test(dataProvider = "countDataProvider")
	public void count(int caseId, int mod, String id, String model, String message, String expected) {
		LOGGER.debug("count, caseId="+caseId);
		String dbName = "custom";
		try {
			assertEquals(request.simCreate(dbName).getCode(), "1");
			List<File> files = FileOperator.traversal(Constant.TEST_DATA_DIR + "/sim", ".txt", false);
			String content = null;
			for(int i=0,size=files.size(); i<size; i++) {
				content = FileOperator.read(files.get(i).getAbsolutePath());
				assertEquals(request.simUpdate(content, String.valueOf((i % mod)), dbName).getCode(), "1");
			}
			assertEquals(request.simFlush(dbName).getCode(), "1");
			SimCountResult result = request.simCount(id, model);
			assertEquals(result.getMessage(), message);
			if(!"操作成功".equals(result.getMessage()))
				return;
			assertEquals(result.getResult(), expected);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expected);
		}finally {
			try {
				request.simDeleteModel(dbName);
			}catch(IOException e) {}
		}
	}
	
	@DataProvider(name = "deleteIndexDataProvider")
	public Object[][] deleteIndexDataProvider(Method method){
		Object[][] result = null;
		if(!"deleteIndex".equals(method.getName())) 
			return result;
		result = new Object[][] {
			/* 每篇文章独自一个id, 按id删除就只能删除1篇 */
			new Object[] {1, 999, "0", "custom", "操作成功", "1"},
			/* 所有文本共用同一个id,按id删除会删除所有文本 */
			new Object[] {2, 1, "0", "custom", "操作成功", "1"},
			/* 拆分为3组 */
			new Object[] {3, 3, "0", "custom", "操作成功", "1"},
			/* 输入为空 */
			new Object[] {4, 3, "0", "", "该模板不存在！", null},
			/* 从一个不存在的库中获取 */
			new Object[] {5, 3, "0", "custom1", "该模板不存在！", null}
		};
		return result;
	}
	
	@Test(dataProvider = "deleteIndexDataProvider")
	public void deleteIndex(int caseId, int mod, String deleteId, String model, String message, String expected) {
		LOGGER.debug("deleteIndex, caseId="+caseId);
		String dbName = "custom";
		try {
			/* 建表 */
			assertEquals(request.simCreate("custom").getCode(), "1");
			/* 遍历文件 */
			List<File> files = FileOperator.traversal(Constant.TEST_DATA_DIR + "/sim", ".txt", false);
			String content = null;
			/* 更新索引库 */
			for(int i=0,size=files.size(); i<size; i++) {
				content = FileOperator.read(files.get(i).getAbsolutePath());
				assertEquals(request.simUpdate(content, String.valueOf((i % mod)), dbName).getCode(), "1");
			}
			assertEquals(request.simFlush(dbName).getCode(), "1");
			/* 按id删除索引 */
			SimDeleteIndexResult deleteIndex = request.simDeleteIndex(deleteId, model);
			assertEquals(deleteIndex.getMessage(), message);
			if(!"操作成功".equals(deleteIndex.getMessage())) 
				return;
			/* 删除索引后再次检索, 命中结果为0 */
			assertEquals(deleteIndex.getCode(), expected);
			SimCountResult count = request.simCount(deleteId, model);
			assertEquals(count.getResult(), "0");
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}finally {
			try {
				request.simDeleteModel(dbName);
			}catch(IOException e) {}
		}
	}
	
	@DataProvider(name = "truncateDataProvider")
	public Object[][] truncateDataProvider(Method method){
		if(!"truncate".equals(method.getName()))
			return null;
		return new Object[][] {
			/* =今天 */
			new Object[] {1, "custom", LocalDate.now(), "=", "操作成功", "0"},
			/* +今天 */
			new Object[] {2, "custom", LocalDate.now(), "+", "操作成功", "1"},
			/* -今天 */
			new Object[] {3, "custom", LocalDate.now(), "-", "操作成功", "1"},
			/* =昨天 */
			new Object[] {4, "custom", LocalDate.now().minusDays(1), "=", "操作成功", "1"},
			/* -昨天 */
			new Object[] {5, "custom", LocalDate.now().minusDays(1), "-", "操作成功", "1"},
			/* +昨天 */
			new Object[] {6, "custom", LocalDate.now().minusDays(1), "+", "操作成功", "0"},
			/* =明天 */
			new Object[] {7, "custom", LocalDate.now().plusDays(1), "=", "操作成功", "1"},
			/* +明天 */
			new Object[] {8, "custom", LocalDate.now().plusDays(1), "+", "操作成功", "1"},
			/* -明天 */
			new Object[] {9, "custom", LocalDate.now().plusDays(1), "-", "操作成功", "0"},
			/* 不添加符号, 效果等同于添加减号 */
			new Object[] {10, "custom", LocalDate.now(), "", "操作成功", "1"},
		};
	}
	/**
	 * 删除指定日期的索引
	 * @param caseId
	 * @param model
	 * @param localDate 指定日期
	 * @param prefix  填写 +, -, = 三种符号
	 * @param expected 预期删除操作的结果
	 * @param expectedHit 删除后再次检索的预期命中数
	 */
	@Test(dataProvider = "truncateDataProvider")
	public void truncate(int caseId, String model, LocalDate localDate, String prefix, String expected, String expectedHit) {
		LOGGER.debug("truncate, caseId="+caseId);
		String dbName = "custom";
		String id = "1";
		try {
			/*
			 * 逻辑大体如下:
			 * 1. 新建相似库
			 * 2. 插入一条文本, 毫无疑问这条文本一定是在当前日期插入的
			 * 3. 指定日期删除, 自然也就只能在昨天、今天、明天以及“+” “-” “=” 相互组合做文章, 这样共计9种情况, 检查哪种情况能删掉上面插入的文本
			 * 4. 因此expectedHit就是在问经过删除后, 再去检索插入的文本, 问能命中多少条记录, 不是0就是1
			 */
			assertEquals(request.simCreate("custom").getCode(), "1");
			assertEquals(request.simUpdate("中华人民共和国", id, dbName).getCode(), "1");
			assertEquals(request.simCount(id, dbName).getCode(), "1");
			String timeid = localDate.format(FORMATTER);
			SimTruncateResult truncate = request.simTruncate(model, prefix + timeid);
			assertEquals(truncate.getMessage(), expected);
			assertEquals(request.simCount(id, dbName).getResult(), expectedHit);
		}catch(Exception e) {
			fail(e.getMessage());
		}finally {
			try {
				request.simDeleteModel(dbName);
			}catch(Exception e) {}
		}
	}
	/**
	 * 删除指定标题的索引 异常情况验证: 日期格式不合法
	 */
	@Test
	public void truncateErr() {
		String dbName = "custom";
		String id = "1";
		try {
			assertEquals(request.simCreate(dbName).getCode(), "1");
			assertEquals(request.simUpdate("中华人民共和国", id, dbName).getCode(), "1");
			assertEquals(request.simCount(id, dbName).getCode(), "1");
			/* 指定日期时, 格式为一串符号 */
			request.simTruncate(dbName, "!@#$%^&*()");
			/* 预期删除无效 */
			assertEquals(request.simCount(id, dbName).getResult(), "1");
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}finally {
			try {
				request.simDeleteModel(dbName);
			}catch(IOException e) {}
		}
	}
	/**
	 * 释放内存中的相似库, 接口只能验证返回值, 其它必须手工检查
	 */
	@Test
	public void free() {
		String dbName = "custom";
		try {
			assertEquals(request.simCreate(dbName).getMessage(), "操作成功");
			assertEquals(request.simUpdate("中华人民共和国", "1", dbName).getMessage(), "操作成功");
			assertEquals(request.simFree(dbName).getMessage(), "操作成功");
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}finally {
			try {
				request.simDeleteModel(dbName);
			}catch(IOException e) {}
		}
	}
}
