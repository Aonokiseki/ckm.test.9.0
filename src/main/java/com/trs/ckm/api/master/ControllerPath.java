package com.trs.ckm.api.master;
/**
 * Controller(接口控制器)的名称,用于映射接口路径<br>
 */
public class ControllerPath {
	/** CKM版本 */
	public final static String RS_ABOUT = "/rs/about";
	/** 获取已启用的功能 */
	public final static String RS_ABOUT_MODULES = "/rs/about/modules";
	/** 文本摘要 */
	public final static String RS_ABS = "/rs/abs";
	/** 基础分词 */
	public final static String RS_SEG = "/rs/seg";
	/** 标签抽取 */
	public final static String RS_ABS_LABEL = "/rs/abs/label";
	/** 分词 */
	public final static String RS_ABS_SEG = "/rs/abs/seg";
	/** 主题词提取 */ 
	public final static String RS_ABS_THEME = "/rs/abs/theme";
	/** 情感分析-评论抽取 */
	public final static String RS_APPRAISE_COMMENT = "/rs/appraise/comment";
	/** 情感分析-褒贬分析 */
	public final static String RS_APPRAISE_EMOTION = "/rs/appraise/emotion";
	/** 情感分析-基于FastText的篇章级情感分类 */
	public final static String RS_APPRAISE_FT = "/rs/appraise/ft";
	/** 观点及褒贬分析-从评论数据中抽取客体,观点以及观点情感 */
	public final static String RS_APPRAISE_OPINION_COMMENT = "/rs/appraise/opinion/comment";
	/** 观点及褒贬判断 - 对新闻数据，提取主体、客体、观点以及观点情感 */
	public final static String RS_APPRAISE_OPINION_NEWS = "/rs/appraise/opinion/news";
	/** 情感分析-基于句法的情感分析 */
	public final static String RS_APPRAISE_PARSER = "/rs/appraise/parser";
	/** 自动分类 */
	public final static String RS_AUTOCAT = "/rs/autocat";
	/** 中文简繁切换 */
	public final static String RS_CHINESE_TS = "/rs/chinese/ts";
	/** 图聚类 */
	public final static String RS_CLUSTER_GRAPH = "/rs/cluster/graph";
	/** 获取图聚类任务状态 */
	public final static String RS_CLUSTER_GRPAH_TASK = "/rs/cluster/graph/task";
	/** 获取图聚类任务结果 */
	public final static String RS_CLUSTER_GRAPH_DOWNLOAD = "/rs/cluster/graph/download";
	/** 热点聚类 */
	public final static String RS_CLUSTER_SIM = "/rs/cluster/sim";
	/** 常识校对 */
	public final static String RS_COLLATION_COMMON = "/rs/collation/common";
	/** 英文句子单词纠错 */
	public final static String RS_COLLATION_EN = "/rs/collation/en";
	/** 指代消解 */
	public final static String RS_COREF = "/rs/coref";
	/** 短语检索 */
	public final static String RS_DY_SEARCH = "/rs/dy/search";
	/** 实体链接 */
	public final static String RS_ENTITY_LINK = "/rs/entity-link";
	/** 元事件抽取-快速 */
	public final static String RS_EVENT_QK = "/rs/event/qk";
	/** 信息抽取 */
	public final static String RS_EXT_EXTRACT = "/rs/ext-extract";
	/** 人物关系识别(人物, 机构, 职务关系, 国别信息) */
	public final static String RS_EXT_ENTITY = "/rs/ext/entity";
	/** 数值指标抽取 */
	public final static String RS_EXT_INDICATORS = "/rs/ext/indicators";
	/** 人物属性关系识别 人物知识图谱抽取 */
	public final static String RS_EXT_RDF = "/rs/ext/rdf";
	/** fastTextClassifySeg */
	public final static String RS_FASTTEXT_CLASSIFY_SEG = "/rs/fasttext/classify-seg";
	/** fastTextVecTextSeg */
	public final static String RS_FASTTEXT_VECTORYIZE_TEXT_SEG = "/rs/fasttext/vectorize-text-seg";
	/** 日期推理 */
	public final static String RS_FORMAT_DATE = "/rs/format-date";
	/** 语种检测 */
	public final static String RS_LANGUAGE = "/rs/language";
	/** 中文句法分析 */
	public final static String RS_LEX_PARSE = "/rs/lex-parse";
	/** 获取语言概率模型 */
	public final static String RS_LM = "/rs/lm";
	/** 多语种实体识别-自动检测语种 */
	public final static String RS_NER = "/rs/ner";
	/** 新词发现 */
	public final static String RS_NEWWORD = "/rs/newword";
	/** 观点分析 */
	public final static String RS_OPINION = "/rs/opinion";
	/** 观点分析 debug */
	public final static String RS_OPINION_DEBUG = "/rs/opinion/debug";
	/** 英文句法分析*/
	public final static String RS_PARSER_ENG = "/rs/parser/eng";
	/** 中文句法分析 */
	public final static String RS_PARSER_PCFG = "/rs/parser/pcfg";
	/** 新句法分析 */
	public final static String RS_PARSER_SYNTAX = "/rs/parser/syntax";
	/** 实体识别 */
	public final static String RS_PLO = "/rs/plo";
	/** PLO分词 */
	public final static String RS_PLO_SEG = "/rs/plo/seg";
	/** 带分句的英文词性标注 */
	public final static String RS_POS_ENG = "/rs/pos/eng";
	/** 拼音检索 */
	public final static String RS_PY_SEARCH = "/rs/py/search";
	/** 拼音注音 */
	public final static String RS_PY_ZHUYIN = "/rs/py/zhuyin";
	/** 短标题生成 */
	public final static String RS_ROBO_TITLE = "/rs/robo/title";
	/** 规则分类(字段) */
	public final static String RS_RULECAT = "/rs/rulecat";
	/** 释放已加载至内存中的规则分类模板 */
	public final static String RS_RULECAT_FREE = "/rs/rulecat/free";
	/** 规则分类 */
	public final static String RS_RULECAT_SIMPLE = "/rs/rulecat/simple";
	/** 相似检索-查询库中指定id的记录的个数 */
	public final static String RS_SIM_COUNT = "/rs/sim/count";
	/** 相似检索-新建相似检索库 */
	public final static String RS_SIM_CREATE = "/rs/sim/create";
	/** 相似检索-删除索引 */
	public final static String RS_SIM_DELETE_INDEX = "/rs/sim/delete/index";
	/** 相似检索-删除相似检索库 */
	public final static String RS_SIM_DELETE_MODEL = "/rs/sim/delete/model";
	/** 相似检索-将内存中的库写入到磁盘中 */
	public final static String RS_SIM_FLUSH = "/rs/sim/flush";
	/** 相似检索-释放内存中的相似检索库 */
	public final static String RS_SIM_FREE = "/rs/sim/free";
	/** 相似检索-生成MD5 */
	public final static String RS_SIM_MD5 = "/rs/sim/md5";
	/** 相似检索 获取单个相似检索库的信息  */
	public final static String RS_SIM_MODEL = "/rs/sim/model";
	/** 相似检索-获取所有相似检索库的信息 */
	public final static String RS_SIM_MODEL_ALL = "/rs/sim/model/all";
	/** 相似检索-相似索引 */
	public final static String RS_SIM_RETRIEVE = "/rs/sim/retrieve";
	/** 相似检索-删除指定日期的索引(模板压缩) */
	public final static String RS_SIM_TRUNCATE = "/rs/sim/truncate";
	/** 相似检索-添加索引 */
	public final static String RS_SIM_UPDATE = "/rs/sim/update";
	/** 短文本相似度计算 */
	public final static String RS_STSIM = "/rs/stsim";
	/** 文本对比 */
	public final static String RS_TEXT_COMPARE = "/rs/text-compare";
	/** 生成MD5 Tag */
	public final static String RS_TEXT_COMPARE_MD5TAG = "/rs/text-compare/md5tag";
	/** 批量接口 */
	public final static String RS_BATCH = "/rs/batch";
}
