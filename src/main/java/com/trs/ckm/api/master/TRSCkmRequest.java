package com.trs.ckm.api.master;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.trs.ckm.api.pojo.AboutModulesResult;
import com.trs.ckm.api.pojo.AboutResult;
import com.trs.ckm.api.pojo.AbsLabelResult;
import com.trs.ckm.api.pojo.AbsResult;
import com.trs.ckm.api.pojo.AbsSegResult;
import com.trs.ckm.api.pojo.AbsThemeResult;
import com.trs.ckm.api.pojo.AppraiseCommentResult;
import com.trs.ckm.api.pojo.AppraiseEmotionResult;
import com.trs.ckm.api.pojo.AppraiseFTResult;
import com.trs.ckm.api.pojo.AppraiseOpinionResult;
import com.trs.ckm.api.pojo.AppraiseParserResult;
import com.trs.ckm.api.pojo.AutocatResult;
import com.trs.ckm.api.pojo.BatchRequestBody;
import com.trs.ckm.api.pojo.BatchRequestBody.Action;
import com.trs.ckm.api.pojo.BatchResult;
import com.trs.ckm.api.pojo.ChineseS2tResult;
import com.trs.ckm.api.pojo.ClusterGraphResult;
import com.trs.ckm.api.pojo.ClusterGraphTaskResult;
import com.trs.ckm.api.pojo.ClusterSimKeywordResult;
import com.trs.ckm.api.pojo.ClusterSimResult;
import com.trs.ckm.api.pojo.CollationCommonResult;
import com.trs.ckm.api.pojo.CollationEnResult;
import com.trs.ckm.api.pojo.CorefResult;
import com.trs.ckm.api.pojo.DLAbsResult;
import com.trs.ckm.api.pojo.DLAllModelResult;
import com.trs.ckm.api.pojo.DLCatResult;
import com.trs.ckm.api.pojo.DLCollateResult;
import com.trs.ckm.api.pojo.DLEventResult;
import com.trs.ckm.api.pojo.DLMLEmojiExtResult;
import com.trs.ckm.api.pojo.DLMLEmojiStripResult;
import com.trs.ckm.api.pojo.DLNerResult;
import com.trs.ckm.api.pojo.DLNreResult;
import com.trs.ckm.api.pojo.DySearchResult;
import com.trs.ckm.api.pojo.EntityLinkResult;
import com.trs.ckm.api.pojo.EventQuickResult;
import com.trs.ckm.api.pojo.ExtEntityResult;
import com.trs.ckm.api.pojo.ExtExtractResult;
import com.trs.ckm.api.pojo.FastTextClassifySegResult;
import com.trs.ckm.api.pojo.FastTextVectorizeTextSegResult;
import com.trs.ckm.api.pojo.FormatDateResult;
import com.trs.ckm.api.pojo.IndicatorsResult;
import com.trs.ckm.api.pojo.LanguageResult;
import com.trs.ckm.api.pojo.LexParseResult;
import com.trs.ckm.api.pojo.ModelDeleteResult;
import com.trs.ckm.api.pojo.ModelImportResult;
import com.trs.ckm.api.pojo.ModelListResult;
import com.trs.ckm.api.pojo.NerResult;
import com.trs.ckm.api.pojo.NewwordResult;
import com.trs.ckm.api.pojo.OpinionDebugResult;
import com.trs.ckm.api.pojo.OpinionResult;
import com.trs.ckm.api.pojo.ParserEngResult;
import com.trs.ckm.api.pojo.ParserPcfgResult;
import com.trs.ckm.api.pojo.ParserSyntaxResult;
import com.trs.ckm.api.pojo.PloResult;
import com.trs.ckm.api.pojo.PloSegResult;
import com.trs.ckm.api.pojo.PosEngResult;
import com.trs.ckm.api.pojo.PySearchResult;
import com.trs.ckm.api.pojo.PyZhuyinResult;
import com.trs.ckm.api.pojo.RdfResult;
import com.trs.ckm.api.pojo.RoboTitleResult;
import com.trs.ckm.api.pojo.RulecatFreeResult;
import com.trs.ckm.api.pojo.RulecatResult;
import com.trs.ckm.api.pojo.RulecatSimpleResult;
import com.trs.ckm.api.pojo.SegmentResult;
import com.trs.ckm.api.pojo.SimCountResult;
import com.trs.ckm.api.pojo.SimCreateResult;
import com.trs.ckm.api.pojo.SimDeleteIndexResult;
import com.trs.ckm.api.pojo.SimDeleteModelResult;
import com.trs.ckm.api.pojo.SimFlushResult;
import com.trs.ckm.api.pojo.SimFreeResult;
import com.trs.ckm.api.pojo.SimMd5Result;
import com.trs.ckm.api.pojo.SimModelAllResult;
import com.trs.ckm.api.pojo.SimModelResult;
import com.trs.ckm.api.pojo.SimRetrieveResult;
import com.trs.ckm.api.pojo.SimTruncateResult;
import com.trs.ckm.api.pojo.SimUpdateResult;
import com.trs.ckm.api.pojo.StsimResult;
import com.trs.ckm.api.pojo.TextCompareMd5TagResult;
import com.trs.ckm.api.pojo.TextCompareResult;
import com.trs.ckm.util.HttpOperator;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TRSCkmRequest {
	
	private String host;
	private final static Gson GSON = new Gson();
	private HttpOperator httpOperator;
	
	public TRSCkmRequest() {
		httpOperator = HttpOperator.get();
	}
	
	@Autowired
	public TRSCkmRequest(
			@Value("${host}") String host,
			@Value("${connect.timeout}") Integer connectTimeout,
			@Value("${read.timeout}") Integer readTimeout) {
		this();
		this.httpOperator = HttpOperator.get(connectTimeout, readTimeout);
		this.host = host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHost() {
		return this.host;
	}
	/**
	 * ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public SegmentResult seg(String text)throws IOException{
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_SEG, parameter);
		return GSON.fromJson(json, SegmentResult.class);
	}
	/**
	 * ??????????????????
	 * @param text	???????????????
	 * @param model ??????
	 * @return CollationResult?????????, ???????????????????????????
	 * @throws IOException
	 */
	public CollationCommonResult collationCommon(String text, String model) throws IOException {
		if(model == null || "".equals(model)) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_COLLATION_COMMON , parameter);
		return GSON.fromJson(json, CollationCommonResult.class);
	}
	/**
	 * ??????CKM??????
	 */
	public AboutResult about() throws IOException {
		String json = httpOperator.get(host + ControllerPath.RS_ABOUT);
		return GSON.fromJson(json, AboutResult.class); 
	}
	/**
	 * ????????????????????????
	 * @return AboutModulesResult?????????
	 * @throws IOException
	 */
	public AboutModulesResult aboutModules()throws IOException {
		String json = httpOperator.get(host + ControllerPath.RS_ABOUT_MODULES);
		return GSON.fromJson(json, AboutModulesResult.class);
	}
	/**
	 * ????????????
	 * @param text ???????????????
	 * @param model ??????
	 * @param numOfAbs ????????????(???????????????)
	 * @param numOfSub ???????????????
	 * @param percent ????????????????????????????????????
	 * @return AbsResult ?????????
	 * @throws IOException
	 */
	public AbsResult abs(String text, String model, String numOfAbs, String numOfSub, String percent) throws IOException {
		if(model == null) model = "";  
		if(numOfAbs == null) numOfAbs = "";
		if(numOfSub == null) numOfSub = "";  
		if(percent == null) percent = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("numOfAbs", numOfAbs);
		parameter.add("numOfSub", numOfSub);
		parameter.add("percent", percent);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_ABS, parameter);
		return GSON.fromJson(json, AbsResult.class);
	}
	/**
	 * ????????????????????????
	 * @param text ???????????????
	 * @param model ????????????
	 * @return
	 * @throws IOException
	 */
	public DLAbsResult dlabs(String text, String model) throws IOException {
		if(text == null) text = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + "/rs/dl/abs/" + model, parameter);
		return GSON.fromJson(json, DLAbsResult.class);
	}
	/**
	 * ???????????????
	 * @param text ???????????????
	 * @param model ??????
	 * @param num ???????????????
	 * @return AbsThemeResult ????????????
	 * @throws IOException
	 */
	public AbsThemeResult absTheme(String text, String model, String num) throws IOException {
		if(model == null) model = "";
		if(text == null) text = "";
		if(num == null) num = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("num", num);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_ABS_THEME, parameter);
		return GSON.fromJson(json, AbsThemeResult.class);
	}
	/**
	 * ????????????-????????????
	 * @param text ???????????????
	 * @param model ??????
	 * @return AppraiseCommentResult ????????????
	 * @throws IOException
	 * @deprecated
	 */
	public AppraiseCommentResult appraiseComment(String text, String model) throws IOException {
		if(model == null || "".equals(model)) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_COMMENT, parameter);
		return GSON.fromJson(json, AppraiseCommentResult.class);
	}
	/**
	 * ????????????-????????????
	 * @param text ???????????????
	 * @param model ??????
	 * @return AppraiseEmotionResult ????????????
	 * @throws IOException
	 */
	public AppraiseEmotionResult appraiseEmotion(String text, String model) throws IOException{
		if(text == null) text = "";
		if(model == null || "".equals(model)) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_EMOTION, parameter);
		return GSON.fromJson(json, AppraiseEmotionResult.class);
	}
	/**
	 * ????????????
	 * @param content ???????????????
	 * @param model ??????
	 * @param title ??????
	 * @return AbsLabelResult ????????????
	 * @throws IOException
	 */
	public AbsLabelResult absLabel(String content, String model, String title) throws IOException {
		if(model == null || "".equals(model)) model = "";
		if(title == null || "".equals(title)) title = "";
		if(content == null || "".equals(content)) content = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		parameter.add("model", model);
		parameter.add("title", title);
		String json = httpOperator.post(host + ControllerPath.RS_ABS_LABEL, parameter);
		return GSON.fromJson(json, AbsLabelResult.class);	
	}
	/**
	 * ??????
	 * @param text ???????????????
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public AbsSegResult absSeg(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_ABS_SEG, parameter);
		return GSON.fromJson(json, AbsSegResult.class);
	}
	/**
	 * ???????????? - ??????FastText????????????????????????
	 * @param text ???????????????
	 * @param model FastText??????
	 * @return AppraiseFTResult ????????????
	 * @throws IOException
	 */
	public AppraiseFTResult appraiseFt(String text, String model) throws IOException {
		if(model == null || "".equals(model)) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_FT, parameter);
		return GSON.fromJson(json, AppraiseFTResult.class);
	}
	/**
	 * ????????????????????? - ????????????????????????????????????????????????????????????
	 * @param text ???????????????
	 * @param config ????????????
	 * @return AppraiseOpinionCommentResult????????????
	 * @throws IOException
	 * @deprecated
	 */
	public AppraiseOpinionResult appraiseOpinionComment(String text, String config) throws IOException {
		if(text == null) text = "";
		if(config == null || "".equals(config)) config = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("config", config);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_OPINION_COMMENT, parameter);
		return GSON.fromJson(json, AppraiseOpinionResult.class);
	}
	/**
	 * ????????????????????? - ??????????????????????????????????????????????????????????????????
	 * @param text ???????????????
	 * @param config ????????????
	 * @return AppraiseOpinionCommentResult????????????
	 * @throws IOException
	 */
	public AppraiseOpinionResult appraiseOpinionNews(String text, String config) throws IOException {
		if(text == null) text = "";
		if(config == null || "".equals(config)) config = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("config", config);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_OPINION_NEWS, parameter);
		return GSON.fromJson(json, AppraiseOpinionResult.class);
	}
	/**
	 * ???????????? - ???????????????????????????
	 * @param text ???????????????
	 * @return AppraiseParserResult ????????????
	 * @throws IOException
	 */
	public AppraiseParserResult appraiseParser(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_APPRAISE_PARSER, parameter);
		return GSON.fromJson(json, AppraiseParserResult.class);
	}
	/**
	 * ????????????
	 * @param text ???????????????
	 * @param model ??????
	 * @return AutocatResult 
	 * @throws IOException
	 */
	public AutocatResult autocat(String text, String model) throws IOException {
		if(model == null || "".equals(model)) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_AUTOCAT, parameter);
		return GSON.fromJson(json, AutocatResult.class);
	}
	/**
	 * ??????????????????
	 * @param text ???????????????
	 * @return
	 * @throws IOException
	 */
	public ChineseS2tResult chineseTs(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_CHINESE_TS, parameter);
		return GSON.fromJson(json, ChineseS2tResult.class);
	}
	/**
	 * ?????????
	 * @param encoding ????????????
	 * @param filePath ????????????????????????zip???????????????????????????????????????????????????.txt????????????????????????
	 * @param maxClusterNum ??????????????????
	 * @param method ????????????????????? person-????????????|org-??????????????????|theme-???????????????
	 * @param minClusterNum ??????????????????
	 * @param minNumOfClu ????????????????????????????????????
	 * @return ClusterGraphResult ??????, ?????????????????????id
	 * @throws IOException<br><br>
	 * 
	 * ????????????<br>
	 * <code>
	 *  //???????????????, ?????????????????????<br>
	 *  ClusterGraphResult graphResult = request.clusterGraph(null, "./testdata/input/clu/5k.zip", null, null, null, null);<br>
	 *  //??????taskid ?????????????????????<br>
	 *  ClusterGraphTaskResult taskResult = request.clusterGraphTask(graphResult.getTaskId());<br>
	 *  //???????????????????????????????????????, ??????????????????, ????????????????????????<br>
	 *  while(taskResult.getPhase().equals("Finished")){<br>
	 *  	try{<br>
	 *  		Thread.sleep(1000);<br>
	 *  	}catch(InterruptedException e){<br>
	 *  		e.printStackTrace();<br>
	 *  	}<br>
	 *  	taskResult = request.clusterGraphTask(graphResult.getTaskId());<br>
	 *  }<br>
	 *  // ???????????????????????????????????????????????????????????????<br>
	 *  byte[] bytes = request.clusterGraphDownload(graphResult.getTaskId());<br>
	 *  // ?????????????????????????????????, ??? ./testdata/clu/ ?????????????????????<br>
	 *  File file = FileOperator.writeBytesToFile(byte, "clusterGraph_", ".xml", new File("./testdata/clu/"));<br>
	 * </code>
	 */
	public ClusterGraphResult clusterGraph(String encoding, String filePath, String maxClusterNum, String method, String minClusterNum, String minNumOfClu) throws IOException {
		if(encoding == null) encoding = "";  
		if(maxClusterNum == null) maxClusterNum = "";
		if(method == null) method = "";  
		if(minClusterNum == null) minClusterNum = "";
		if(minNumOfClu == null) minNumOfClu = ""; 
		if(filePath == null) filePath = "";
		MultiValueMap<String,Object> parameter = new LinkedMultiValueMap<String,Object>();
		parameter.add("encoding", encoding);
		parameter.add("maxClusterNum", maxClusterNum);
		parameter.add("method", method);
		parameter.add("minClusterNum", minClusterNum);
		parameter.add("minNumOfClu", minNumOfClu);
		parameter.add("file", new FileSystemResource(filePath));
		String json = httpOperator.specialPost(host + ControllerPath.RS_CLUSTER_GRAPH.toString(), parameter);
		return GSON.fromJson(json, ClusterGraphResult.class);
	}
	
	/**
	 * ?????? taskId ?????????????????????
	 * @param taskId ???????????? taskId
	 * @return
	 * @throws IOException
	 */
	public ClusterGraphTaskResult clusterGraphTask(String taskId) throws IOException {
		if(taskId == null) taskId = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("taskId", taskId);
		String json = httpOperator.post(host + ControllerPath.RS_CLUSTER_GRPAH_TASK.toString(), parameter);
		return GSON.fromJson(json, ClusterGraphTaskResult.class);
	}
	/**
	 * ?????? taskId ?????????????????????(??????, ????????????????????????)
	 * @param taskId ???????????? taskId
	 * @return
	 * @throws IOException
	 */
	public byte[] clusterGraphDownload(String taskId) throws IOException {
		if(taskId == null) taskId = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("taskId", taskId);
		byte[] bytes = httpOperator.postForBinary(host + ControllerPath.RS_CLUSTER_GRAPH_DOWNLOAD, parameter);
		return bytes;
	}
	/**
	 * ????????????
	 * @param clusterSize
	 * @param colContent
	 * @param colId
	 * @param colTitle
	 * @param dataList
	 * @param minMemberSize
	 * @param threshold
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public ClusterSimResult clusterSim(String clusterSize, String colContent, String colId, String colTitle, String dataList, String minMemberSize, String threshold) throws IOException {
		if(clusterSize == null) clusterSize = "";
		if(colContent == null) colContent = "";
		if(colId == null) colId = "";
		if(colTitle == null) colTitle = "";
		if(dataList == null) dataList = "";
		if(minMemberSize == null) minMemberSize = "";
		if(threshold == null) threshold = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("clusterSize", clusterSize);
		parameter.add("colContent", colContent);
		parameter.add("colId", colId);
		parameter.add("colTitle", colTitle);
		parameter.add("dataList", dataList);
		parameter.add("minMemberSize", minMemberSize);
		parameter.add("threshold", threshold);
		String json = httpOperator.post(host + ControllerPath.RS_CLUSTER_SIM, parameter);
		return GSON.fromJson(json, ClusterSimResult.class);
	}
	/**
	 * ????????????
	 * @param clusterSize
	 * @param colContent
	 * @param colId
	 * @param colTitle
	 * @param dataList
	 * @param minMemberSize
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public ClusterSimResult clusterSim(String clusterSize, String colContent, String colId, String colTitle, String dataList, String minMemberSize) throws IOException {
		if(clusterSize == null) clusterSize = "";
		if(colContent == null) colContent = "";
		if(colId == null) colId = "";
		if(colTitle == null) colTitle = "";
		if(dataList == null) dataList = "";
		if(minMemberSize == null) minMemberSize = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("clusterSize", clusterSize);
		parameter.add("colContent", colContent);
		parameter.add("colId", colId);
		parameter.add("colTitle", colTitle);
		parameter.add("dataList", dataList);
		parameter.add("minMemberSize", minMemberSize);
		String json = httpOperator.post(host + ControllerPath.RS_CLUSTER_SIM, parameter);
		return GSON.fromJson(json, ClusterSimResult.class);
	}
	/**
	 * ????????????????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public CollationEnResult collationEn(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_COLLATION_EN, parameter);
		return GSON.fromJson(json, CollationEnResult.class);
	}
	/**
	 * ????????????
	 * @param text ???????????????
	 * @param seg ??????????????????????????? 0-??????????????????,??????????????????????????????????????????; 1-??????????????????????????????????????????????????????1
	 * @return
	 * @throws IOException
	 */
	public CorefResult coref(String text, String seg) throws IOException {
		if(text == null) text = "";
		if(seg == null || "".equals(seg)) seg = "1";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("seg", seg);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_COREF, parameter);
		return GSON.fromJson(json, CorefResult.class);
	}
	/**
	 * ????????????
	 * @param query
	 * @param model
	 * @param option
	 * @return
	 * @throws IOException
	 */
	public DySearchResult dySearch(String query, String model, String option) throws IOException {
		if(query == null) query = "";
		if(model == null) model = "";
		if(option == null) option = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("query", query);
		parameter.add("option", option);
		String json = httpOperator.post(host + ControllerPath.RS_DY_SEARCH, parameter);
		return GSON.fromJson(json, DySearchResult.class);
	}
	/**
	 * ????????????
	 * @param desc ??????????????????
	 * @param entity ????????????
	 * @return
	 * @throws IOException 
	 */
	public EntityLinkResult entityLink(String desc, String entity) throws IOException {
		if(desc == null) desc ="";
		if(entity == null) entity = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("desc", desc);
		parameter.add("entity", entity);
		String json = httpOperator.post(host + ControllerPath.RS_ENTITY_LINK, parameter);
		return GSON.fromJson(json, EntityLinkResult.class);
	}
	/**
	 * ???????????????, ??????
	 * @param baseDate
	 * @param sentences
	 * @return
	 * @throws IOException
	 */
	public EventQuickResult eventQk(String baseDate, String sentences) throws IOException {
		if(baseDate == null) baseDate = "";
		if(sentences == null) sentences = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("baseDate", baseDate);
		parameter.add("sentences", sentences);
		String json = httpOperator.post(host + ControllerPath.RS_EVENT_QK, parameter);
		return GSON.fromJson(json, EventQuickResult.class);
	}
	/**
	 * ????????????
	 * @param text
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public ExtExtractResult extExtract(String text, String model) throws IOException {
		if(model == null) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_EXT_EXTRACT, parameter);
		return GSON.fromJson(json, ExtExtractResult.class);
	}
	/**
	 * ?????????????????? ?????????????????????????????????????????????
	 * @param content ??????
	 * @return
	 * @throws IOException
	 */
	public ExtEntityResult extEntity(String content) throws IOException {
		if(content == null) content = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		String json = httpOperator.post(host + ControllerPath.RS_EXT_ENTITY, parameter);
		return GSON.fromJson(json, ExtEntityResult.class);
	}
	/**
	 * ??????????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public IndicatorsResult indicators(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_EXT_INDICATORS, parameter);
		return GSON.fromJson(json, IndicatorsResult.class);
	}
	/**
	 * ???????????????????????? ???????????????????????? 
	 * @param content ???????????????
	 * @param model ????????????
	 * @param segType ????????????, 1-????????????|2-????????????|3-????????????
	 * @return
	 * @throws IOException 
	 */
	public RdfResult extRdf(String content, String model, String segType) throws IOException {
		if(content == null) content = "";
		if(model == null) model = "";
		if(segType == null) segType = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		parameter.add("model", model);
		parameter.add("segType", segType);
		String json = httpOperator.post(host + ControllerPath.RS_EXT_RDF, parameter);
		return GSON.fromJson(json, RdfResult.class);
	}
	/**
	 * fastTextClassifySeg
	 * @param text
	 * @param limit
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public FastTextClassifySegResult fastTextClassifySeg(String text, String limit, String model) throws IOException {
		if(text == null) text = "";
		if(limit == null) limit = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("limit", limit);
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_FASTTEXT_CLASSIFY_SEG, parameter);
		return GSON.fromJson(json,FastTextClassifySegResult.class);
	}
	/**
	 * fastTextVecTextSeg
	 * @param text
	 * @param model
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public FastTextVectorizeTextSegResult fastTextVectorizeTextSeg(String text, String model) throws IOException {
		if(model == null) model = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_FASTTEXT_VECTORYIZE_TEXT_SEG, parameter);
		return GSON.fromJson(json, FastTextVectorizeTextSegResult.class);
	}
	/**
	 * ????????????
	 * @param text ???????????????
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @throws IOException
	 */
	public FormatDateResult formatDate(String text, String year, String month, String day) throws IOException {
		if(year == null) year = ""; if(month == null) month = "";
		if(day == null) day = ""; if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("year", year);
		parameter.add("month", month);
		parameter.add("day", day);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_FORMAT_DATE, parameter);
		return GSON.fromJson(json, FormatDateResult.class);
	}
	/**
	 * ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public LanguageResult language(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_LANGUAGE, parameter);
		return GSON.fromJson(json, LanguageResult.class);
	}
	/**
	 * ??????????????????
	 * @param text ???????????????
	 * @param seg ?????????????????? 0-??????????????????, ??????????????????????????????????????????; 1-???????????????, ????????????????????????????????? ??????1
	 * @param timeout ????????????????????????
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public LexParseResult lexParse(String text, String seg, String timeout) throws IOException {
		if(text == null) text = "";
		if(seg == null) seg = "";
		if(timeout == null) timeout = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("seg", seg);
		parameter.add("timeout", timeout);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_LEX_PARSE, parameter);
		return GSON.fromJson(json, LexParseResult.class);
	}
	
	/**
	 * ????????????????????????
	 * @deprecated
	 */
	public void lm() {}
	/**
	 * ????????????????????? ??????????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public NerResult ner(String text) throws IOException{
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_NER, parameter);
		return GSON.fromJson(json, NerResult.class);
	}
	/**
	 * ????????????????????? ????????????
	 * @param text ??????
	 * @param lang ??????
	 * @return
	 * @throws IOException
	 */
	public NerResult ner_lang (String text, String lang) throws IOException {
		if(lang == null) lang = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_NER + "/"  + lang, parameter);
		return GSON.fromJson(json, NerResult.class);
	}
	
	/**
	 * ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public NewwordResult newword(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_NEWWORD, parameter);
		return GSON.fromJson(json, NewwordResult.class);
	}
	/**
	 * ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public OpinionResult opinion(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_OPINION, parameter);
		return GSON.fromJson(json, OpinionResult.class);
	}
	/**
	 * ????????????-debug
	 * @param text
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public OpinionDebugResult opinionDebug(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_OPINION_DEBUG, parameter);
		return GSON.fromJson(json, OpinionDebugResult.class);
	}
	/**
	 * ??????????????????
	 * @param text
	 * @param seg ??????????????????????????? 0-?????????????????????1/??????1 ???2/??????2???   1-?????????????????????
	 * @return
	 * @throws IOException
	 */
	public ParserEngResult parserEng(String text, String seg) throws IOException {
		if(seg == null) seg = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("seg", seg);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_PARSER_ENG, parameter);
		return GSON.fromJson(json, ParserEngResult.class);
	}
	/**
	 * ??????????????????
	 * @param text ???????????????
	 * @param seg ???????????????????????????0-???????????????????????????????????????????????????????????????; 1-????????????????????????????????????????????????????????????1???
	 * @param timeout ????????????, ?????????
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public ParserPcfgResult parserPcfg(String text, String seg, String timeout) throws IOException {
		if(seg == null) seg = "";
		if(timeout == null) timeout = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("seg", seg);
		parameter.add("timeout", timeout);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_PARSER_PCFG, parameter);
		return GSON.fromJson(json, ParserPcfgResult.class);
	}
	/**
	 * ??????????????????
	 * @param text
	 * @param seg
	 * @return
	 * @throws IOException
	 */
	public ParserSyntaxResult parserSyntax(String text, String seg) throws IOException {
		if(seg == null) seg = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("seg", seg);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_PARSER_SYNTAX, parameter);
		return GSON.fromJson(json, ParserSyntaxResult.class);
	}
	/**
	 * ??????????????????????????????
	 * @param text
	 * @param option ??????????????????, 1-??????/??????/????????? 2-?????? ??????????????????
	 * @return
	 * @throws IOException 
	 */
	public PloResult plo(String text, String option) throws IOException {
		if(option == null) option = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("option", option);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_PLO, parameter);
		return GSON.fromJson(json, PloResult.class);
	}
	/**
	 * PLO??????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public PloSegResult ploSeg(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_PLO_SEG, parameter);
		return GSON.fromJson(json, PloSegResult.class);
	}
	/**
	 * ??????????????????????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public PosEngResult posEng(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_POS_ENG, parameter);
		return GSON.fromJson(json, PosEngResult.class);
	}
	/**
	 * ????????????
	 * @param pinyin
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public PySearchResult pySearch(String pinyin, String model) throws IOException {
		if(pinyin == null) pinyin = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("pinyin", pinyin);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_PY_SEARCH, parameter);
		return GSON.fromJson(json, PySearchResult.class);
	}
	/**
	 * ????????????
	 * @param query ?????????
	 * @param model ????????????
	 * @return
	 * @throws IOException 
	 * @deprecated
	 */
	public PyZhuyinResult pyZhuyin(String query, String model) throws IOException {
		if(query == null) query = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("query", query);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_PY_ZHUYIN, parameter);
		return GSON.fromJson(json, PyZhuyinResult.class);
	}
	/**
	 * ???????????????
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public RoboTitleResult roboTitle(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_ROBO_TITLE, parameter);
		return GSON.fromJson(json, RoboTitleResult.class);
	}
	/**
	 * ???????????? ?????? 
	 * @param records ??????????????????,json??????
	 * @param model ??????
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public RulecatResult rulecat(String records, String model) throws IOException {
		if(model == null) model = "";
		if(records == null) records = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("records", records);
		String json = httpOperator.post(host + ControllerPath.RS_RULECAT, parameter);
		return GSON.fromJson(json, RulecatResult.class);
	}
	/**
	 * ????????????????????????????????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public RulecatFreeResult rulecatFree(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_RULECAT_FREE, parameter);
		return GSON.fromJson(json, RulecatFreeResult.class);
	}
	/**
	 * ????????????
	 * @param content
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public RulecatSimpleResult rulecatSimple(String content, String model) throws IOException {
		if(content == null) content = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_RULECAT_SIMPLE, parameter);
		return GSON.fromJson(json, RulecatSimpleResult.class);
	}
	/**
	 * ????????????, ??????????????????id??????????????????
	 * @param id
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimCountResult simCount(String id, String model) throws IOException {
		if(id == null) id = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("id", id);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_COUNT, parameter);
		return GSON.fromJson(json, SimCountResult.class);
	}
	/**
	 * ????????????-?????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimCreateResult simCreate(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_CREATE, parameter);
		return GSON.fromJson(json, SimCreateResult.class);
	}
	/**
	 * ????????????-????????????
	 * @param id
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimDeleteIndexResult simDeleteIndex(String id, String model) throws IOException{
		if(id == null) id = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("id", id);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_DELETE_INDEX, parameter);
		return GSON.fromJson(json, SimDeleteIndexResult.class);
	}
	/**
	 * ???????????? ?????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimDeleteModelResult simDeleteModel(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_DELETE_MODEL, parameter);
		return GSON.fromJson(json, SimDeleteModelResult.class);
	}
	/**
	 * ???????????? ?????????????????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimFlushResult simFlush(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_FLUSH, parameter);
		return GSON.fromJson(json, SimFlushResult.class);
	}
	/**
	 * ???????????? ?????????????????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimFreeResult simFree(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_FREE, parameter);
		return GSON.fromJson(json, SimFreeResult.class);
	}
	/**
	 * ????????????-??????MD5
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public SimMd5Result simMd5(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_MD5, parameter);
		return GSON.fromJson(json, SimMd5Result.class);
	}
	/**
	 * ????????????-?????????????????????????????????
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public SimModelResult simModel(String model) throws IOException {
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_MODEL, parameter);
		return GSON.fromJson(json, SimModelResult.class);
	}
	/**
	 * ????????????-????????????????????????????????????
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
	public SimModelAllResult simModelAll() throws IOException {
		String json = httpOperator.post(host + ControllerPath.RS_SIM_MODEL_ALL, null);
		return GSON.fromJson(json, SimModelAllResult.class);
	}
	/**
	 * ????????????-????????????
	 * @param content ????????????
	 * @param model ???????????????
	 * @param revnum ???????????????
	 * @param threshold ???????????????
	 * @return
	 * @throws IOException
	 */
	public SimRetrieveResult simRetrieve(String content, String model, String revnum, String threshold) throws IOException {
		if(content == null) content = "";
		if(model == null) model = "";
		if(revnum == null) revnum = "";
		if(threshold == null) threshold = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		parameter.add("model", model);
		parameter.add("revnum", revnum);
		parameter.add("threshold", threshold);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_RETRIEVE, parameter);
		return GSON.fromJson(json, SimRetrieveResult.class);
	}
	/**
	 * ???????????? ??????????????????????????? (????????????)
	 * @param model
	 * @param timeid
	 * @return
	 * @throws IOException 
	 */
	public SimTruncateResult simTruncate(String model, String timeid) throws IOException {
		if(model == null) model = "";
		if(timeid == null) timeid = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("model", model);
		parameter.add("timeid", timeid);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_TRUNCATE, parameter);
		return GSON.fromJson(json, SimTruncateResult.class);
	}
	/**
	 * ??????????????????????????
	 * @param content ????????????
	 * @param id ????????????
	 * @param model ??????????????????
	 * @return
	 * @throws IOException
	 */
	public SimUpdateResult simUpdate(String content, String id, String model) throws IOException {
		if(content == null) content = "";
		if(id == null) id = "";
		if(model == null) model = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("content", content);
		parameter.add("id", id);
		parameter.add("model", model);
		String json = httpOperator.post(host + ControllerPath.RS_SIM_UPDATE, parameter);
		return GSON.fromJson(json, SimUpdateResult.class);
	}
	/**
	 * ????????????????????????
	 * @param lang ??????(CN,EN,JP,KR,AR,RU,ES,FR,DE)
	 * @param text1 ???????????????1
	 * @param text2 ???????????????2
	 * @return
	 * @throws IOException
	 */
	public StsimResult stsim(String lang, String text1, String text2) throws IOException {
		if(lang == null) lang = "";
		if(text1 == null) text1 = "";
		if(text2 == null) text2 = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("lang", lang);
		parameter.add("text1", text1);
		parameter.add("text2", text2);
		String json = httpOperator.post(host + ControllerPath.RS_STSIM, parameter);
		return GSON.fromJson(json, StsimResult.class);
	}
	/**
	 * ????????????
	 * @param compare ???????????????
	 * @param text ???????????????
	 * @return
	 * @throws IOException
	 */
	public TextCompareResult textCompare(String compare, String text) throws IOException {
		if(compare == null) compare = "";
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("compare", compare);
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_TEXT_COMPARE, parameter);
		return GSON.fromJson(json, TextCompareResult.class);
	}
	/**
	 * ??????MD5 Tag
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public TextCompareMd5TagResult textCompareMd5Tag(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + ControllerPath.RS_TEXT_COMPARE_MD5TAG, parameter);
		return GSON.fromJson(json, TextCompareMd5TagResult.class);
	}
	/**
	 * ????????????????????????. ???????????????????????????, ??????????????????????????????????????????
	 */
	public DLAllModelResult dlAllModel() throws IOException {
		String json = httpOperator.get(host + "/rs/dl/all_model");
		return GSON.fromJson(json, DLAllModelResult.class);
	}
	/**
	 * ???????????? ????????????
	 * @param text ??????????????? 
	 * @param model ????????????
	 * @return
	 * @throws IOException
	 */
	public DLCatResult dlcat(String text, String model) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host +  "/rs/dl/cat/" + model, parameter);
		return GSON.fromJson(json, DLCatResult.class);
	}
	/**
	 * ???????????? ????????????
	 * @param sentences ???????????????
	 * @param model ????????????
	 * @return
	 * @throws IOException 
	 */
	public DLEventResult dlevent(String sentences, String model) throws IOException {
		if(sentences == null) sentences = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("sentences", sentences);
		String json = httpOperator.post(host +  "/rs/dl/event/" + model, parameter);
		return GSON.fromJson(json, DLEventResult.class);
	}
	/**
	 * ?????? emoji ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public DLMLEmojiExtResult dlMlEmojiExt(String text) throws IOException{
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host +  "/rs/dl/ml/emoji_ext", parameter);
		return GSON.fromJson(json, DLMLEmojiExtResult.class);
	}
	/**
	 * ?????? emoji ????????????
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public DLMLEmojiStripResult dlMlEmojiStrip(String text) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + "/rs/dl/ml/emoji_strip", parameter);
		return GSON.fromJson(json, DLMLEmojiStripResult.class);
	}
	/**
	 * ????????????-??????
	 * @param text 
	 * @param model ??????
	 * @return
	 * @throws IOException
	 */
	public DLCollateResult dlcollate(String text, String model) throws IOException{
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + "/rs/dl/collate/" + model, parameter);
		return GSON.fromJson(json, DLCollateResult.class);
	}
	
	/**
	 * ????????????
	 * @param text
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public DLNreResult dlnre(String text, String model) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + "/rs/dl/nre/" + model, parameter);
		return GSON.fromJson(json, DLNreResult.class);
	}
	/**
	 * ????????????
	 * @param text ???????????????
	 * @param model ????????????
	 * @return
	 * @throws IOException
	 */
	public DLNerResult dlner(String text, String model) throws IOException {
		if(text == null) text = "";
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("text", text);
		String json = httpOperator.post(host + "/rs/dl/ner/" + model, parameter);
		return GSON.fromJson(json, DLNerResult.class);
	}
	
	/**
	 * ????????????????????????????????????
	 * @param module ?????????
	 * @return
	 * @throws IOException
	 */
	public ModelListResult modelsList(String module) throws IOException {
		if(module == null) module = "";
		String controller = "/rs/models/list/" + module;
		String json = httpOperator.post(host + controller, null);
		return GSON.fromJson(json, ModelListResult.class);
	}
	/**
	 * ????????????
	 * @param module
	 * @param modelFilePath
	 * @return
	 */
	public ModelImportResult modelsImport(String module, String modelFilePath) {
		if(module == null) module = "";
		String controller = "/rs/models/import/" + module;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String,Object>();
		if(modelFilePath != null && !modelFilePath.isEmpty())
			parameter.add("model", new FileSystemResource(modelFilePath));
		String json = httpOperator.specialPost(host + controller, parameter);
		return GSON.fromJson(json, ModelImportResult.class);
	}
	/**
	 * ????????????
	 * @param module ?????????
	 * @param model ?????????
	 * @return
	 * @throws IOException
	 */
	public ModelDeleteResult modelsDelete(String module, String model) throws IOException {
		if(module == null) module = "";
		String controller = "/rs/models/delete/" + module;
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		if(model != null && !"".equals(model))
			parameter.add("model", model);
		String json = httpOperator.post(host + controller, parameter);
		return GSON.fromJson(json, ModelDeleteResult.class);
	}
	
	/**
	 * ????????????
	 * @param module
	 * @param model
	 * @return
	 */
	public byte[] modelsExport(String module, String model) {
		if(module == null) module = "";
		String controller = "/rs/models/export/" + module;
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		/* 2020.9.2, ??????????????????????????????, ???????????????null, ??????????????????. 
		 * ???????????????????????????, ??????????????? ?????????, ?????????????????????????????????, ??????????????????????????????;
		 * ??????????????????????????????, ?????????????????????????????????.
		 * ???????????????????????????, ???????????????????????????????????????, HTTP????????????????????????????????????, ??????????????????????????? 
		 * ????????????, ??????model?????????????????????????????????????????? */
		if(model != null && !model.isEmpty())
			parameter.add("model", model);
		return httpOperator.postForBinary(host + controller, parameter);
	}
	/**
	 * ???????????????
	 * @param text ??????
	 * @param actions ????????????, ?????? {@link com.trs.ckm.api.pojo.BatchRequestBody.Action}
	 * @return BatchResult ???????????????
	 * @throws IOException<br><br>
	 * <b>????????????</b><br>
	 * 
	 * <code>
	 *  // ?????? abs ???????????????<br>
	 * 	Map&ltString,String&gt absParams = new HashMap&ltString,String&gt();<br>
	 *  absParams.put("numOfSub", "3");<br>
	 *  // ?????? rulecat ???????????????<br>
	 *  Map&ltString,String&gt catParams = new HashMap&ltString,String&gt();<br>
	 *  catParams.put("model", "demo");<br>
	 *  // ????????????<br>
	 * 	String text = "";<br>
	 *  // ???????????????, Action?????????????????????, ??????????????????2???; ?????? action ???????????????<br>
	 * 	BatchResult result = request.batch(text, new Action("abs", absParams), new Action("rulecat", catParams) ...... );<br>
	 *  // ???????????????????????????<br>
	 *  List&ltComplexResult&gt complexResults = result.getResults();<br>
	 *  // ????????????????????? rulecat ???????????????, ????????????batch??????????????? action ?????????, ???????????????????????????1<br>
	 *  List&ltCatResult&gt catResults = complexResults.get(1).getCatResults();
	 * </code>
	 * 
	 * @deprecated 2020/8/24 ???????????????????????????????????????????????????????????????????????????????????????
	 */
	public BatchResult batch(String text, com.trs.ckm.api.pojo.BatchRequestBody.Action...actions) throws IOException {
		if(actions == null || actions.length == 0) actions = new Action[0];
		List<Action> actionList = Arrays.asList(actions);
		return batch(text, actionList);
	}
	/**
	 * ???????????????
	 * @param text
	 * @param actions
	 * @return
	 * @deprecated
	 */
	public BatchResult batch(String text, List<Action> actions) {
		if(text == null) text = "";
		if(actions == null) actions = new ArrayList<Action>(0);
		BatchRequestBody body = new BatchRequestBody(text, actions);
		String jsonRequestBody = GSON.toJson(body);
		return batch(jsonRequestBody);
	}
	/**
	 * ???????????????
	 * @param text
	 * @param jsonRequestBody
	 * @return
	 */
	private BatchResult batch(String jsonRequestBody) {
		if(jsonRequestBody == null) jsonRequestBody = "";
		String json = httpOperator.postJson(host + ControllerPath.RS_BATCH, jsonRequestBody);
		return GSON.fromJson(json, BatchResult.class);
	}
	 /**
	  * ?????????-???????????????????????????
	  * @param clusterSize ???????????????????????????????????????
	  * @param colContent ??????????????????
	  * @param colId id????????????
	  * @param colTitle ??????????????????
	  * @param dataList ???????????? ??????: List<Map<String, Object>>
	  * @param keywordNum ?????????????????????????????????????????????
	  * @param minMemberSize ???????????????????????????????????????
	  * @param threshold ?????????????????????1-0???????????????0.6 
	  * @return
	 * @throws IOException 
	  */
	 public ClusterSimKeywordResult debug_clusterSimKeyword(
			 String clusterSize,String colContent,String colId,String colTitle,List<Map<String, String>> dataList,
			 String keywordNum, String minMemberSize, String threshold) throws IOException {
		 if(clusterSize == null) clusterSize = "";
		 if(colContent == null) colContent = "";
		 if(colId == null) colId = "";
		 if(colTitle == null) colTitle = "";
		 if(keywordNum == null) keywordNum = "";
		 if(minMemberSize == null) minMemberSize = "";
		 if(threshold == null) threshold = "";
		 if(dataList == null) 
			 dataList = new ArrayList<Map<String,String>>();
		 String controller = "/rs/cluster/sim/keyword";
		 MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String,String>();
		 parameter.add("clusterSize", clusterSize);
		 parameter.add("colContent", colContent);
		 parameter.add("colId", colId);
		 parameter.add("colTitle", colTitle);
		 /* ?????????????????????????????????,?????????dataList,dataList????????????json?????????, ???????????? */
		 parameter.add("dataList", GSON.toJson(dataList));
		 parameter.add("keywordNum", keywordNum);
		 parameter.add("minMemberSize", minMemberSize);
		 parameter.add("threshold", threshold);	 
		 String json = httpOperator.post(host + controller, parameter);
		 return GSON.fromJson(json, ClusterSimKeywordResult.class);
	 }
}
