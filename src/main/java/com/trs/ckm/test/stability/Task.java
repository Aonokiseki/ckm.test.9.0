package com.trs.ckm.test.stability;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.CkmBasicResult;
import com.trs.ckm.api.pojo.DySearchResult;
import com.trs.ckm.api.pojo.SimRetrieveResult;
import com.trs.ckm.api.pojo.SimUpdateResult;
import com.trs.ckm.test.function.Constant;
import com.trs.ckm.test.stability.Configuration.Method;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable{
	private final static String TIME_FORMATTER = "yyyy/MM/dd HH:mm:ss";
	/* 部分接口不需要用到额外的参数,只需要文本一个参数, 这样会让json解析过程中返回一个为null的parameter
	 * 在run()方法中触发空指针异常, 因此一旦检测到额外参数为null, 就引用此静态map */
	private final static HashMap<String,String> EMPTY_PARAMETER = new HashMap<String,String>(0);
	/* 一个静态常量MAP, 目的是读文件时不再追加换行符
	 * 2020/08/24: 带上换行符, 调用 /robo/title 触发服务器进程崩溃, 开发给出解决方案为服务器端过滤
	 * 以后如果解决了这个问题, 就把这个常量和静态代码段去掉即可  */
	private final static HashMap<String,String> READ_FILE_OPTIONS = new HashMap<String,String>();
	static {
		READ_FILE_OPTIONS.put("not.append.lineserparator", "true");
	}
	
	private Configuration configuration;
	private TRSCkmRequest request;
	private ResultStatistic statistic;
	private List<File> files;
	private List<Method> methods;
	
	private Task() {}
	
	public static Task build(Configuration configuration, TRSCkmRequest request, ResultStatistic statistic){
		Task task = new Task();
		task.configuration = configuration;
		task.request = request;
		task.statistic = statistic;
		task.files = configuration.getFiles();
		task.methods = configuration.getInterfaces();
		return task;
	}

	public void run() {
		int randomFileIndex = (int)(Math.random() * files.size());
		int randomFunctionIndex = (int)(Math.random() * methods.size());
		Method method = methods.get(randomFunctionIndex);
		Map<String,String> parameter = method.getParams();
		if(parameter == null) parameter = EMPTY_PARAMETER;
		CkmBasicResult result = null;
		File file = files.get(randomFileIndex);
		try {
			String text = FileOperator.read(file.getAbsolutePath(), configuration.getEncode(), READ_FILE_OPTIONS);
			result = handle(request, configuration, method.getName(), text, parameter);
			if(Constant.FAILURE.equals(result.getCode())) {
				codeEqualsZero(method, text, result, file, statistic);
				return;
			}
			if(Constant.EMPTY.equals(result.getCode())) {
				codeEqualsMinusOne(method, file, statistic);
				return;
			}
			statistic.addSuccessAndGet(method.getName());
		} catch (Throwable e) {
			catchExceptions(method, e, file, statistic);
		} finally {
			Thread.yield();
		}
	}
	/**
	 * 捕获异常时的处理策略
	 * @param method
	 * @param e
	 * @param file
	 * @param statistic
	 */
	public static void catchExceptions(Method method, Throwable e, File file, ResultStatistic statistic) {
		statistic.addFailureAndGet(method.getName());
		StringBuilder exception = new StringBuilder();
		exception.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMATTER))).append(", ")
				 .append(method.getName()).append(", ").append(file.getAbsolutePath())
				 .append(System.lineSeparator())
				 .append(Other.stackTraceToString(e)).append(System.lineSeparator());
		statistic.appendExceptionInfo(exception.toString());
	}
	/**
	 * 请求返回-1时的处理, 这意味着返回的结果为空;
	 * @param method
	 * @param file
	 * @param statistic
	 */
	public static void codeEqualsMinusOne(Method method, File file, ResultStatistic statistic) {
		statistic.addEmptyResultAndGet(method.getName());
		StringBuilder info = new StringBuilder();
		info.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMATTER)))
			.append(", ")
			.append(method.getName()).append(", ").append(file.getAbsolutePath());
		statistic.appendEmptyInfo(info.toString());
	}
	/**
	 * 请求返回0时的处理, 发生了错误但没有抛出异常, 通常的原因是模板加载错误, 或者接口名写错了
	 * @param method
	 * @param text
	 * @param result
	 * @param file
	 * @param statistic
	 */
	private static void codeEqualsZero(Method method, 
			String text, CkmBasicResult result, File file, ResultStatistic statistic) {
		/* 有一种特殊情况: 空文本, 这样也会产生0异常, 但这种异常又是在预期之内的返回值，所以就不计入错误数中 */
		/* 隐含的一个bug就是, 统计结果一定小于真正运行次数,不过数量不大, 不影响判定是否通过 */
		boolean isEmptyText = (text == null) || ("".equals(text));
		if(isEmptyText) return;
		statistic.addFailureAndGet(method.getName());
		StringBuilder error = new StringBuilder();
		error.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMATTER))).append(", ")
			 .append(method.getName()).append(", ").append(file.getAbsolutePath())
			 .append(", length=").append(file.length()).append(", ")
			 .append(result.getMessage()).append(System.lineSeparator());
		statistic.appendExceptionInfo(error.toString());
	}
	
	private static CkmBasicResult handle(TRSCkmRequest request, 
										 Configuration configuration,
										 String methodName, 
										 String text, 
										 Map<String,String> parameter) throws IOException {
		switch(methodName) {
			/*
			 * 稳定性不支持的接口: 
			 * 1.图聚类所有接口
			 * 2.模板管理的所有接口
			 * 3.批处理接口
			 * 
			 * 上述接口在调用时直接返回一个code = 0 的BasicCkmResult实例,
			 * 在run()方法中, 会被判定为结果为空, 请注意
			 */
			case "appraiseOpinionNews" : return request.appraiseOpinionNews(text, parameter.get("config"));
			case "segement" : return request.seg(text);
			case "collation" : return request.collationCommon(text, parameter.get("model"));
			case "about" : return request.about();
			case "aboutByGet" : return request.about();
			case "aboutModules" : return request.aboutModules();
			case "abs" : return request.abs(text, parameter.get("model"), 
					parameter.get("numOfAbs"), parameter.get("numOfSub"), parameter.get("percent"));
			case "dlabs" : return request.dlabs(text, parameter.get("model"));
			case "absTheme" : return request.absTheme(text, parameter.get("model"), parameter.get("num"));
			case "appraiseEmotion" : return request.appraiseEmotion(text, parameter.get("model"));
			case "absLabel" : return request.absLabel(text, parameter.get("model"), parameter.get("label"));
			case "appraiseFt" : return request.appraiseFt(text, parameter.get("model"));
			case "appraiseParser" : return request.appraiseParser(text);
			case "autocat" : return request.autocat(text, parameter.get("model"));
			case "chineseTs" : return request.chineseTs(text);
			case "collationEn" : return request.collationEn(text);
			case "coref" : return request.coref(text, parameter.get("seg"));
			case "dySearch" : return handleDySearch(request, configuration.getDys(),
					parameter.get("model"), parameter.get("options"));
			case "entityLink" : return request.entityLink(text, parameter.get("entity"));
			case "eventQk" : return request.eventQk(parameter.get("baseDate"), text);
			case "extExtract" : return request.extExtract(text, parameter.get("model"));
			case "extEntity" : return request.extEntity(text);
			case "indicators" : return request.indicators(text);
			case "rdf" : return request.extRdf(text, parameter.get("model"), parameter.get("segType"));
			case "fastTextClassifySeg" : return request.fastTextClassifySeg(
					text, parameter.get("limit"), parameter.get("model"));
			case "formatDate" : return request.formatDate(text, 
					parameter.get("year"), parameter.get("month"), parameter.get("day"));
			case "language" : return request.language(text);
			case "ner" : return request.ner(text);
			case "ner_lang" : return request.ner_lang(text, parameter.get("lang"));
			case "newword" : return request.newword(text);
			case "parserEng" : return request.parserEng(text, parameter.get("seg"));
			case "plo" : return request.plo(text, parameter.get("option"));
			case "ploSeg" : return request.ploSeg(text);
			case "posEng" : return request.posEng(text);
			case "pySearch" : return request.pySearch(parameter.get("query"), parameter.get("model"));
			case "roboTitle" : return request.roboTitle(text);
			case "rulecatSimple" : return request.rulecatSimple(text, parameter.get("model"));
			case "parserSyntax" : return request.parserSyntax(text, parameter.get("seg"));
			case "simCount" : return request.simCount(parameter.get("id"), parameter.get("model"));
			case "simRetrieve" : return request.simRetrieve(text, 
					parameter.get("model"), parameter.get("revnum"), parameter.get("threshold"));
			/* 需求是同时检查相似库的更新和检索, 对于同一条文本，更新成功是检索命中的前提，于是就把这两个行为放到一起 */
			case "sim" : return handleSim(
					request, parameter.get("model"), text, parameter.get("revnum"), parameter.get("threshold"));
			/* 文本相似度比较, 最简单的实现就是, 自己和自己比 */
			case "stsim" : return request.stsim(parameter.get("lang"), text, text);
			/* 文本比对, 自己比自己 */
			case "textCompare" : return request.textCompare(text, text);
			case "textCompareMd5Tag" : return request.textCompareMd5Tag(text);
			case "dlAllModel" : return request.dlAllModel();
			case "dlcat" : return request.dlcat(text, parameter.get("model"));
			case "dlevent" : return request.dlevent(text, parameter.get("model"));
			case "dlMlEmojiExt" : return request.dlMlEmojiExt(text);
			case "dlMlEmojiStrip" : return request.dlMlEmojiStrip(text);
			case "dlnre" : return request.dlnre(text, parameter.get("model"));
			case "dlner" : return request.dlner(text, parameter.get("model"));
			case "modelsList" : return request.modelsList(parameter.get("module"));
			case "clusterSimKeyword" : 
			default : return new CkmBasicResult() {
				public String getCode() {
					/* 填写了一个不存在的接口名, 说明配置文件出现了错误。为了提醒执行的人, 故意令其返回失败 */
					return "0";
				}
				public String getMessage() {
					return "方法未找到或不支持";
				}
				public String getDetails() {
					return null;
				}
			};
		}
	}
	/**
	 * 短语检索接口的文本为一个检索词列表, 需要特殊处理
	 * @param model
	 * @param options
	 * @return
	 * @throws IOException 
	 */
	private static DySearchResult handleDySearch(TRSCkmRequest request, 
			List<String> dys, String model, String options) throws IOException {
		int randomIndex = (int)(Math.random() * dys.size());
		String dy = dys.get(randomIndex);
		return request.dySearch(dy, model, options);
	}
	
	/**
	 * 用于验证相似库操作的方法
	 * @param request
	 * @param model
	 * @param content
	 * @param revnum
	 * @param threshold
	 * @return
	 * @throws IOException
	 */
	private static CkmBasicResult handleSim(TRSCkmRequest request, 
			String model, String content, String revnum, String threshold) throws IOException {
		/* 如果没有创建相似库, 先创建 */
		if("0".equals(request.simModel(model).getCode())) 
			request.simCreate(model);
		/* 随机生成一个id, 用于更新和删除 */
		String id = UUID.randomUUID().toString();
		/* 相似索引更新 */
		SimUpdateResult update = request.simUpdate(content, id, model);
		/* 检索 */
		SimRetrieveResult retrieve = request.simRetrieve(content, model, revnum, threshold);
		/* 将更新的索引删除, 避免插入重复文本干扰code, 扰乱返回值的判定 */
		request.simDeleteIndex(id, model);
		class SimResult implements CkmBasicResult{
			private String code;
			private String message;
			public SimResult(SimUpdateResult update, SimRetrieveResult retrieve) {
				this.code = "-1";
				this.message = update.getMessage() + ", " + retrieve.getMessage();
				/* 规定: 更新返回值不是1, 或检索返回0的, 算作整个操作失败 */
				if(!"1".equals(update.getCode()) || "0".equals(retrieve.getCode()))
					this.code = "0";
				else
					this.code = "1";
			}
			@Override
			public String getCode() {
				return code;
			}
			@Override
			public String getMessage() {
				return message;
			}
			@Override
			public String getDetails() {
				return null;
			}
		}
		return new SimResult(update, retrieve);
	}
}
