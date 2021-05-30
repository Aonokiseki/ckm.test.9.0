package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
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
import com.trs.ckm.api.pojo.ModelDeleteResult;
import com.trs.ckm.api.pojo.ModelImportResult;
import com.trs.ckm.api.pojo.ModelListResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class ModelsManagement {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger();
	
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
	
	@DataProvider(name = "modelOperationDataProvider")
	public Object[][] modelOperationDataProvider(Method method){
		if(!"modelOperation".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, "abs", "news-kr"},
			new Object[] {2, "appraise", "bb_pingjia"},
			new Object[] {3, "autocat", "mod_en5"},
			new Object[] {4, "collation", "demo"},
			new Object[] {5, "extextract", "demo"},
			new Object[] {6, "rulecat", "qa_demo"},
			new Object[] {7, "label", "demo"},
			new Object[] {8, "dy", "sysnymos"},
			new Object[] {9, "py", "demo"},
			new Object[] {10, "sim", "demo"},
			new Object[] {11, "ft", "edu_mod2"},
		};
	}
	/**
	 * 测试模板的基本操作<br>
	 * 注: 执行前要求linux系统的用户是 TRSCKM_LINUX64 以及其所有子目录/文件的拥有者 
	 * @param caseId
	 * @param module
	 * @param model
	 */
	@Test(dataProvider = "modelOperationDataProvider")
	public void modelOperation(int caseId, String module, String model) {
		LOGGER.debug(String.format("modelOperation, caseId==%d", caseId));
		File file = null;
		try {
			/* 先检查这个模板是否存在 */
			ModelListResult modelsList = request.modelsList(module);
			List<String> models = modelsList.getResult();
			assertTrue(models.contains(model));
			/* 下载这个模板, 有一点要注意：
			 * 1. 下载的文件本质上是个zip文件, 但我写文件的时候没有处理, 因此它显示的样子怪怪的, 不过没有关系, 并不影响执行 */
			byte[] bytes = request.modelsExport(module, model);
			/* 接口名长度为2的几个接口, 如py, dy, 在写文件时会抛出前缀长度不足的异常, 所以要追加长度*/
			String prefix = module;
			if(module.length() < 3)
				prefix = module + module;
			file = FileOperator.writeBytesToFile(bytes, prefix, model, new File(Constant.OUTPUT_DIR));
			assertTrue(file.exists());
			/* 删除这个模板 */
			ModelDeleteResult modelDelete = request.modelsDelete(module, model);
			assertEquals(modelDelete.getMessage(), "操作成功");
			/* 重新检查模板是否存在, 现在应该是不存在 */
			modelsList = request.modelsList(module);
			models = modelsList.getResult();
			assertFalse(models.contains(model));
			/* 导入模板 */
			ModelImportResult modelImport = request.modelsImport(module, file.getAbsolutePath());
			assertEquals(modelImport.getMessage(), "操作成功");
			/* 再检查模板是否存在, 现在应该是存在 */
			modelsList = request.modelsList(module);
			models = modelsList.getResult();
			assertTrue(models.contains(model));
		} catch (Exception e) {
			fail(Other.stackTraceToString(e));
		} finally {
			/* 最后把导出的文件删掉 */
			if(file != null && file.exists())
				file.delete();
		}
	}
	
	@DataProvider(name = "modelsExportErrDataProvider")
	public Object[][] modelsExportErrDataProvider(Method method){
		if(!"modelsExportErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 未指定接口, 抛出异常 */
			new Object[] {1, "", "absdict_cn", "404 Not Found"},
			/* 未指定模板, 不会抛出异常, 导不出模板文件, 也不需要写预期 */
			new Object[] {2, "abs", "", null},
			/* 指定一个不存在的模板, 不会抛出异常, 导不出模板文件, 也不需要写预期 */
			new Object[] {3, "abs", "qwertyuiop", null},
			/* 不存在的接口 */
			new Object[] {4, "qwertyuiop", "absdict_cn", "输入参数不合法！"},
		};
	}
	
	/**
	 * 模板导出异常情况验证
	 * @param caseId
	 * @param module
	 * @param model
	 * @param expectation
	 */
	@Test(dataProvider = "modelsExportErrDataProvider")
	public void modelsExportErr(int caseId, String module, String model, String expectation){
		try {
			byte[] bytes = request.modelsExport(module, model);
			assertNull(bytes);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "modelsDeleteErrDataProvider")
	public Object[][] modelsDeleteErrDataProvider(Method method){
		if(!"modelsDeleteErr".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, "", "absdict_cn", "404 Not Found"},
			new Object[] {2, "abs", "", "400 Bad Request"},
			/* 2020.9.2 删除一个不存在的模板, 没有抛出异常, 显示操作成功。
			 * 暂时不打算深究是否应该警告模板不存在了 */
			new Object[] {3, "abs", "qwertyuiop", "操作成功"},
			/* 不存在的接口 */
			new Object[] {4, "qwertyuiop", "absdict_cn", "输入参数不合法！"}
		};
	}
	/**
	 * 模板删除异常情况
	 * @param caseId
	 * @param module
	 * @param model
	 * @param expectation
	 */
	@Test(dataProvider = "modelsDeleteErrDataProvider")
	public void modelsDeleteErr(int caseId, String module, String model, String expectation) {
		try {
			ModelDeleteResult delete = request.modelsDelete(module, model);
			assertEquals(delete.getMessage(), expectation);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "modelsImportErrDataProvider")
	public Object[][] modelsImportErrDataProvider(Method method){
		if(!"modelsImportErr".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 未指定接口 */
			new Object[] {1, "", Constant.TEST_DATA_DIR + "/modelImport/news-cn.zip", "404 Not Found"},
			/* 未指定模板路径 */
			new Object[] {2, "abs", "", "400 Bad Request"},
			/* 指定一个错误的路径, 返回的message是null */
			new Object[] {3, "abs", "/abcdefg", null},
			/* 指定了一个不存在的接口 */
			new Object[] {4, "qwertyuiop", Constant.TEST_DATA_DIR + "/modelImport/news-cn.zip", "不支持的模板类型"}
		};
	}
	/**
	 * 模板导入异常情况
	 * @param caseId
	 * @param module
	 * @param modelPath
	 * @param expectation
	 */
	@Test(dataProvider = "modelsImportErrDataProvider")
	public void modelsImportErr(int caseId, String module, String modelPath, String expectation) {
		try {
			ModelImportResult modelImport = request.modelsImport(module, modelPath);
			assertEquals(modelImport.getMessage(), expectation);
		}catch(Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
	
	@DataProvider(name = "modelsListErrDataProvider")
	public Object[][] modelsListErrDataProvider(Method method){
		if(!"modelsListErr".equals(method.getName()))
			return null;
		return new Object[][] {
			new Object[] {1, "", "404 Not Found"},
			new Object[] {2, "qazwsx", "输入参数不合法！"}
		};
	}
	/**
	 * 模板展示异常情况
	 * @param caseId
	 * @param module
	 * @param expectation
	 */
	@Test(dataProvider = "modelsListErrDataProvider")
	public void modelsListErr(int caseId, String module, String expectation) {
		try {
			ModelListResult list = request.modelsList(module);
			assertEquals(list.getMessage(), expectation);
			assertNull(list.getResult());
		} catch (Exception e) {
			assertEquals(e.getMessage(), expectation);
		}
	}
}
