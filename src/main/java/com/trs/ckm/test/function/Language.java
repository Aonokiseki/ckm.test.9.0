package com.trs.ckm.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.LanguageResult;
import com.trs.ckm.test.aspect.AspectConfig;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Language {
	private AnnotationConfigApplicationContext context;
	private TRSCkmRequest request;
	private final static Logger LOGGER = LogManager.getLogger();
	
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
	
	@DataProvider(name = "languageDataProvider")
	public Object[][] languageDataProvider(Method method){
		if(!"language".equals(method.getName()))
			return null;
		return new Object[][] {
			/* 汉语  */
			new Object[] {1, Constant.TEST_DATA_DIR + "/language/CHINESE_SHORT.txt", "CHINESE"},
			new Object[] {2, Constant.TEST_DATA_DIR + "/language/CHINESE_LONG.txt", "CHINESE"},
			/* 英语 */
			new Object[] {3, Constant.TEST_DATA_DIR + "/language/ENGLISH_SHORT.txt", "ENGLISH"},
			new Object[] {4, Constant.TEST_DATA_DIR + "/language/ENGLISH_LONG.txt", "ENGLISH"},
			/* 丹麦语 */
			new Object[] {5, Constant.TEST_DATA_DIR + "/language/DANISH_SHORT.txt", "DANISH"},
			new Object[] {6, Constant.TEST_DATA_DIR + "/language/DANISH_LONG.txt", "DANISH"},
			/* 荷兰语 */
			new Object[] {7, Constant.TEST_DATA_DIR + "/language/DUTCH_SHORT.txt", "DUTCH"},
			new Object[] {8, Constant.TEST_DATA_DIR + "/language/DUTCH_LONG.txt", "DUTCH"},
			/* 芬兰语 */
			new Object[] {9, Constant.TEST_DATA_DIR + "/language/FINNISH_SHORT.txt", "FINNISH"},
			new Object[] {10, Constant.TEST_DATA_DIR + "/language/FINNISH_LONG.txt", "FINNISH"},
			/* 法语 */
			new Object[] {11, Constant.TEST_DATA_DIR + "/language/FRENCH_SHORT.txt", "FRENCH"},
			new Object[] {12, Constant.TEST_DATA_DIR + "/language/FRENCH_LONG.txt", "FRENCH"},
			/* 德语 */
			new Object[] {13, Constant.TEST_DATA_DIR + "/language/GERMAN_SHORT.txt", "GERMAN"},
			new Object[] {14, Constant.TEST_DATA_DIR + "/language/GERMAN_LONG.txt", "GERMAN"},
			/* 希伯来语 */
			new Object[] {15, Constant.TEST_DATA_DIR + "/language/HEBREW_SHORT.txt", "HEBREW"},
			new Object[] {16, Constant.TEST_DATA_DIR + "/language/HEBREW_LONG.txt", "HEBREW"},
			/* 意大利语 */
			new Object[] {17, Constant.TEST_DATA_DIR + "/language/ITALIAN_SHORT.txt", "ITALIAN"},
			new Object[] {18, Constant.TEST_DATA_DIR + "/language/ITALIAN_LONG.txt", "ITALIAN"},
			/* 日本 */
			new Object[] {19, Constant.TEST_DATA_DIR + "/language/JAPANESE_SHORT.txt", "JAPANESE"},
			new Object[] {20, Constant.TEST_DATA_DIR + "/language/JAPANESE_LONG.txt", "JAPANESE"},
			/* 韩语 */
			new Object[] {21, Constant.TEST_DATA_DIR + "/language/KOREAN_SHORT.txt", "KOREAN"},
			new Object[] {22, Constant.TEST_DATA_DIR + "/language/KOREAN_LONG.txt", "KOREAN"},
			/* 挪威语 */
			new Object[] {23, Constant.TEST_DATA_DIR + "/language/NORWEGIAN_SHORT.txt", "NORWEGIAN"},
			new Object[] {24, Constant.TEST_DATA_DIR + "/language/NORWEGIAN_LONG.txt", "NORWEGIAN"},
			/* 波兰语 */
			new Object[] {25, Constant.TEST_DATA_DIR + "/language/POLISH_SHORT.txt", "POLISH"},
			new Object[] {26, Constant.TEST_DATA_DIR + "/language/POLISH_LONG.txt", "POLISH"},
			/* 葡萄牙语 */
			new Object[] {27, Constant.TEST_DATA_DIR + "/language/PORTUGUESE_SHORT.txt", "PORTUGUESE"},
			new Object[] {28, Constant.TEST_DATA_DIR + "/language/PORTUGUESE_LONG.txt", "PORTUGUESE"},
			/* 俄语 */
			new Object[] {29, Constant.TEST_DATA_DIR + "/language/RUSSIAN_SHORT.txt", "RUSSIAN"},
			new Object[] {30, Constant.TEST_DATA_DIR + "/language/RUSSIAN_LONG.txt", "RUSSIAN"},
			/* 西班牙语 */
			new Object[] {31, Constant.TEST_DATA_DIR + "/language/SPANISH_SHORT.txt", "SPANISH"},
			new Object[] {32, Constant.TEST_DATA_DIR + "/language/SPANISH_LONG.txt", "SPANISH"},
			/* 瑞典语 */
			new Object[] {33, Constant.TEST_DATA_DIR + "/language/SWEDISH_SHORT.txt", "SWEDISH"},
			new Object[] {34, Constant.TEST_DATA_DIR + "/language/SWEDISH_LONG.txt", "SWEDISH"},
			/* 捷克语 */
			new Object[] {35, Constant.TEST_DATA_DIR + "/language/CZECH_SHORT.txt", "CZECH"},
			new Object[] {36, Constant.TEST_DATA_DIR + "/language/CZECH_LONG.txt", "CZECH"},
			/* 希腊语 */
			new Object[] {37, Constant.TEST_DATA_DIR + "/language/GREEK_SHORT.txt", "GREEK"},
			new Object[] {38, Constant.TEST_DATA_DIR + "/language/GREEK_LONG.txt", "GREEK"},
			/* 冰岛语 */
			new Object[] {39, Constant.TEST_DATA_DIR + "/language/ICELANDIC_SHORT.txt", "ICELANDIC"},
			new Object[] {40, Constant.TEST_DATA_DIR + "/language/ICELANDIC_LONG.txt", "ICELANDIC"},
			/* 拉脱维亚语 */
			new Object[] {41, Constant.TEST_DATA_DIR + "/language/LATVIAN_SHORT.txt", "LATVIAN"},
			new Object[] {42, Constant.TEST_DATA_DIR + "/language/LATVIAN_LONG.txt", "LATVIAN"},
			/* 立陶宛语 */
			new Object[] {43, Constant.TEST_DATA_DIR + "/language/LITHUANIAN_SHORT.txt", "LITHUANIAN"},
			new Object[] {44, Constant.TEST_DATA_DIR + "/language/LITHUANIAN_LONG.txt", "LITHUANIAN"},
			/* 罗马尼亚语 */
			new Object[] {45, Constant.TEST_DATA_DIR + "/language/ROMANIAN_SHORT.txt", "ROMANIAN"},
			new Object[] {46, Constant.TEST_DATA_DIR + "/language/ROMANIAN_LONG.txt", "ROMANIAN"},
			/* 匈牙利语 */
			new Object[] {47, Constant.TEST_DATA_DIR + "/language/HUNGARIAN_SHORT.txt", "HUNGARIAN"},
			new Object[] {48, Constant.TEST_DATA_DIR + "/language/HUNGARIAN_SHORT.txt", "HUNGARIAN"},
			/* 爱沙尼亚语 */
			new Object[] {49, Constant.TEST_DATA_DIR + "/language/ESTONIAN_SHORT.txt", "ESTONIAN"},
			new Object[] {50, Constant.TEST_DATA_DIR + "/language/ESTONIAN_LONG.txt", "ESTONIAN"},
			/* 保加利亚语 */
			new Object[] {51, Constant.TEST_DATA_DIR + "/language/BOSNIAN_SHORT.txt", "BOSNIAN"},
			new Object[] {52, Constant.TEST_DATA_DIR + "/language/BOSNIAN_LONG.txt", "BOSNIAN"},
			/* 克罗地亚语 */
			new Object[] {53, Constant.TEST_DATA_DIR + "/language/CROATIAN_SHORT.txt", "CROATIAN"},
			new Object[] {54, Constant.TEST_DATA_DIR + "/language/CROATIAN_LONG.txt", "CROATIAN"},
			/* 塞尔维亚语 */
			new Object[] {55, Constant.TEST_DATA_DIR + "/language/SERBIAN_SHORT.txt", "SERBIAN"},
			new Object[] {56, Constant.TEST_DATA_DIR + "/language/SERBIAN_LONG.txt", "SERBIAN"},
			/* 爱尔兰语 */
			new Object[] {57, Constant.TEST_DATA_DIR + "/language/IRISH_SHORT.txt", "IRISH"},
			new Object[] {58, Constant.TEST_DATA_DIR + "/language/IRISH_LONG.txt", "IRISH"},
			/* 加利西亚语 */
			new Object[] {59, Constant.TEST_DATA_DIR + "/language/GALICIAN_SHORT.txt", "GALICIAN"},
			new Object[] {60, Constant.TEST_DATA_DIR + "/language/GALICIAN_LONG.txt", "GALICIAN"},
			/* 他加路语 */
			new Object[] {61, Constant.TEST_DATA_DIR + "/language/TAGALOG_SHORT.txt", "TAGALOG"},
			new Object[] {62, Constant.TEST_DATA_DIR + "/language/TAGALOG_LONG.txt", "TAGALOG"},
			/* 土耳其语 */
			new Object[] {63, Constant.TEST_DATA_DIR + "/language/TURKISH_SHORT.txt", "TURKISH"},
			new Object[] {64, Constant.TEST_DATA_DIR + "/language/TURKISH_LONG.txt", "TURKISH"},
			/* 乌克兰语 */
			new Object[] {65, Constant.TEST_DATA_DIR + "/language/UKRAINIAN_SHORT.txt", "UKRAINIAN"},
			new Object[] {66, Constant.TEST_DATA_DIR + "/language/UKRAINIAN_LONG.txt", "UKRAINIAN"},
			/* 印地语 */
			new Object[] {67, Constant.TEST_DATA_DIR + "/language/HINDI_SHORT.txt", "HINDI"},
			new Object[] {68, Constant.TEST_DATA_DIR + "/language/HINDI_LONG.txt", "HINDI"},
			/* 马其顿语 */
			new Object[] {69, Constant.TEST_DATA_DIR + "/language/MACEDONIAN_SHORT.txt", "MACEDONIAN"},
			new Object[] {70, Constant.TEST_DATA_DIR + "/language/MACEDONIAN_LONG.txt", "MACEDONIAN"},
			/* 孟加拉语 */
			new Object[] {71, Constant.TEST_DATA_DIR + "/language/BENGALI_SHORT.txt", "BENGALI"},
			new Object[] {72, Constant.TEST_DATA_DIR + "/language/BENGALI_LONG.txt", "BENGALI"},
			/* 印度尼西亚语 */
			new Object[] {73, Constant.TEST_DATA_DIR + "/language/INDONESIAN_SHORT.txt", "INDONESIAN"},
			new Object[] {74, Constant.TEST_DATA_DIR + "/language/INDONESIAN_LONG.txt", "INDONESIAN"},
			/* 拉丁语 */
			new Object[] {75, Constant.TEST_DATA_DIR + "/language/LATIN_SHORT.txt", "LATIN"},
			new Object[] {76, Constant.TEST_DATA_DIR + "/language/LATIN_LONG.txt", "LATIN"},
			/* 马来语 */
			new Object[] {77, Constant.TEST_DATA_DIR + "/language/MALAY_SHORT.txt", "MALAY"},
			new Object[] {78, Constant.TEST_DATA_DIR + "/language/MALAY_LONG.txt", "MALAY"},
			/* 马拉雅拉姆语 */
			new Object[] {79, Constant.TEST_DATA_DIR + "/language/MALAYALAM_SHORT.txt", "MALAYALAM"},
			new Object[] {80, Constant.TEST_DATA_DIR + "/language/MALAYALAM_LONG.txt", "MALAYALAM"},
			/* 威尔士语 */
			new Object[] {81, Constant.TEST_DATA_DIR + "/language/WELSH_SHORT.txt", "WELSH"},
			new Object[] {82, Constant.TEST_DATA_DIR + "/language/WELSH_LONG.txt", "WELSH"},
			/* 尼泊尔语 */
			new Object[] {83, Constant.TEST_DATA_DIR + "/language/NEPALI_SHORT.txt", "NEPALI"},
			new Object[] {84, Constant.TEST_DATA_DIR + "/language/NEPALI_LONG.txt", "NEPALI"},
			/* 泰卢固语 */
			new Object[] {85, Constant.TEST_DATA_DIR + "/language/TELUGU_SHORT.txt", "TELUGU"},
			new Object[] {86, Constant.TEST_DATA_DIR + "/language/TELUGU_LONG.txt", "TELUGU"},
			/* 阿尔巴尼亚语 */
			new Object[] {87, Constant.TEST_DATA_DIR + "/language/ALBANIAN_SHORT.txt", "ALBANIAN"},
			new Object[] {88, Constant.TEST_DATA_DIR + "/language/ALBANIAN_LONG.txt", "ALBANIAN"},
			/* 泰米尔语 */
			new Object[] {89, Constant.TEST_DATA_DIR + "/language/TAMIL_SHORT.txt", "TAMIL"},
			new Object[] {90, Constant.TEST_DATA_DIR + "/language/TAMIL_LONG.txt", "TAMIL"},
			/* 白俄罗斯语 */
			new Object[] {91, Constant.TEST_DATA_DIR + "/language/BELARUSIAN_SHORT.txt", "BELARUSIAN"},
			new Object[] {92, Constant.TEST_DATA_DIR + "/language/BELARUSIAN_LONG.txt", "BELARUSIAN"},
			/* 爪哇语 */
			new Object[] {93, Constant.TEST_DATA_DIR + "/language/JAVANESE_SHORT.txt", "JAVANESE"},
			new Object[] {94, Constant.TEST_DATA_DIR + "/language/JAVANESE_LONG.txt", "JAVANESE"},
			/* 奥克语 */
			new Object[] {95, Constant.TEST_DATA_DIR + "/language/OCCITAN_SHORT.txt", "OCCITAN"},
			new Object[] {96, Constant.TEST_DATA_DIR + "/language/OCCITAN_LONG.txt", "OCCITAN"},
			/* 乌尔都语 */
			new Object[] {97, Constant.TEST_DATA_DIR + "/language/URDU_SHORT.txt", "URDU"},
			new Object[] {98, Constant.TEST_DATA_DIR + "/language/URDU_LONG.txt", "URDU"},
			/* BIHARA 比哈文 没有找到合适的文本  */
			/* 99 */
			/* 100 */
			/* 古吉拉特语 */
			new Object[] {101, Constant.TEST_DATA_DIR + "/language/GUJARATI_SHORT.txt", "GUJARATI"},
			new Object[] {102, Constant.TEST_DATA_DIR + "/language/GUJARATI_LONG.txt", "GUJARATI"},
			/* 泰语 */
			new Object[] {103, Constant.TEST_DATA_DIR + "/language/THAI_SHORT.txt", "THAI"},
			new Object[] {104, Constant.TEST_DATA_DIR + "/language/THAI_LONG.txt", "THAI"},
			/* 阿拉伯语 */
			new Object[] {105, Constant.TEST_DATA_DIR + "/language/ARABIC_SHORT.txt", "ARABIC"},
			new Object[] {106, Constant.TEST_DATA_DIR + "/language/ARABIC_LONG.txt", "ARABIC"},
			/* 加泰罗尼亚语 */
			new Object[] {107, Constant.TEST_DATA_DIR + "/language/CATALAN_SHORT.txt", "CATALAN"},
			new Object[] {108, Constant.TEST_DATA_DIR + "/language/CATALAN_LONG.txt", "CATALAN"},
			/* 世界语 */
			new Object[] {109, Constant.TEST_DATA_DIR + "/language/ESPERANTO_SHORT.txt", "ESPERANTO"},
			new Object[] {110, Constant.TEST_DATA_DIR + "/language/ESPERANTO_LONG.txt", "ESPERANTO"},
			/* 巴斯克语 */
			new Object[] {111, Constant.TEST_DATA_DIR + "/language/ESPERANTO_SHORT.txt", "ESPERANTO"},
			new Object[] {112, Constant.TEST_DATA_DIR + "/language/ESPERANTO_LONG.txt", "ESPERANTO"},
			/* INTERLINGUA 国际语 拉丁语国际 国际辅助语 没有找到合适的文本 */
			/* 113 */
			/* 114 */
			/* 卡纳达语 */
			new Object[] {115, Constant.TEST_DATA_DIR + "/language/ESPERANTO_SHORT.txt", "ESPERANTO"},
			new Object[] {116, Constant.TEST_DATA_DIR + "/language/ESPERANTO_LONG.txt", "ESPERANTO"},
			/* 旁遮普语 */
			new Object[] {117, Constant.TEST_DATA_DIR + "/language/PUNJABI_SHORT.txt", "PUNJABI"},
			new Object[] {118, Constant.TEST_DATA_DIR + "/language/PUNJABI_LONG.txt", "PUNJABI"},
			/* 苏格兰盖尔语 */
			new Object[] {119, Constant.TEST_DATA_DIR + "/language/SCOTS_GAELIC_SHORT.txt", "SCOTS_GAELIC"},
			new Object[] {120, Constant.TEST_DATA_DIR + "/language/SCOTS_GAELIC_LONG.txt", "SCOTS_GAELIC"},
			/* 斯瓦西里语 */
			new Object[] {121, Constant.TEST_DATA_DIR + "/language/SWAHILI_SHORT.txt", "SWAHILI"},
			new Object[] {122, Constant.TEST_DATA_DIR + "/language/SWAHILI_LONG.txt", "SWAHILI"},
			/* 斯洛文尼亚语 */
			new Object[] {123, Constant.TEST_DATA_DIR + "/language/SLOVENIAN_SHORT.txt", "SLOVENIAN"},
			new Object[] {124, Constant.TEST_DATA_DIR + "/language/SLOVENIAN_LONG.txt", "SLOVENIAN"},
			/* 马拉地语 */
			new Object[] {125, Constant.TEST_DATA_DIR + "/language/MARATHI_SHORT.txt", "MARATHI"},
			new Object[] {126, Constant.TEST_DATA_DIR + "/language/MARATHI_LONG.txt", "MARATHI"},
			/* 马耳他语 */
			new Object[] {127, Constant.TEST_DATA_DIR + "/language/MALTESE_SHORT.txt", "MALTESE"},
			new Object[] {128, Constant.TEST_DATA_DIR + "/language/MALTESE_LONG.txt", "MALTESE"},
			/* 越南语 */
			new Object[] {129, Constant.TEST_DATA_DIR + "/language/VIETNAMESE_SHORT.txt", "VIETNAMESE"},
			new Object[] {130, Constant.TEST_DATA_DIR + "/language/VIETNAMESE_LONG.txt", "VIETNAMESE"},
			/* 弗里西语 */
			new Object[] {131, Constant.TEST_DATA_DIR + "/language/VIETNAMESE_SHORT.txt", "VIETNAMESE"},
			new Object[] {132, Constant.TEST_DATA_DIR + "/language/VIETNAMESE_LONG.txt", "VIETNAMESE"},
			/* 斯洛伐克语 */
			new Object[] {133, Constant.TEST_DATA_DIR + "/language/SLOVAK_SHORT.txt", "SLOVAK"},
			new Object[] {134, Constant.TEST_DATA_DIR + "/language/SLOVAK_LONG.txt", "SLOVAK"},
			/* 繁体中文 会被识别成日文 */
			/* 2020.10.19. 这种说法也不尽然, 反复执行测试方法, 有几次是可以识别正确的
			 * 当前是所有用例的运行结果都不太稳定, PASSED的用例再执行一次有可能就FAILURE了 */
			new Object[] {135, Constant.TEST_DATA_DIR + "/language/CHINESE_T_SHORT.txt", "CHINESE"},
			new Object[] {136, Constant.TEST_DATA_DIR + "/language/CHINESE_T_LONG.txt", "CHINESE"},
			/* 法罗语 */
			new Object[] {137, Constant.TEST_DATA_DIR + "/language/FAROESE_SHORT.txt", "FAROESE"},
			new Object[] {138, Constant.TEST_DATA_DIR + "/language/FAROESE_LONG.txt", "FAROESE"},
			/* 巽他语 */
			new Object[] {139, Constant.TEST_DATA_DIR + "/language/SUNDANESE_SHORT.txt", "SUNDANESE"},
			new Object[] {140, Constant.TEST_DATA_DIR + "/language/SUNDANESE_LONG.txt", "SUNDANESE"},
			/* 乌兹别克语 */
			new Object[] {141, Constant.TEST_DATA_DIR + "/language/UZBEK_SHORT.txt", "UZBEK"},
			new Object[] {142, Constant.TEST_DATA_DIR + "/language/UZBEK_LONG.txt", "UZBEK"},
			/* 阿姆哈拉语 */
			new Object[] {143, Constant.TEST_DATA_DIR + "/language/AMHARIC_SHORT.txt", "AMHARIC"},
			new Object[] {144, Constant.TEST_DATA_DIR + "/language/AMHARIC_LONG.txt", "AMHARIC"},
			/* 阿塞拜疆语 */
			new Object[] {145, Constant.TEST_DATA_DIR + "/language/AZERBAIJANI_SHORT.txt", "AZERBAIJANI"},
			new Object[] {146, Constant.TEST_DATA_DIR + "/language/AZERBAIJANI_LONG.txt", "AZERBAIJANI"},
			/* 格鲁吉亚语 */
			new Object[] {147, Constant.TEST_DATA_DIR + "/language/GEORGIAN_SHORT.txt", "GEORGIAN"},
			new Object[] {148, Constant.TEST_DATA_DIR + "/language/GEORGIAN_LONG.txt", "GEORGIAN"},
			/* 提格利尼亚语 */
			new Object[] {149, Constant.TEST_DATA_DIR + "/language/TIGRINYA_SHORT.txt", "TIGRINYA"},
			new Object[] {150, Constant.TEST_DATA_DIR + "/language/TIGRINYA_LONG.txt", "TIGRINYA"},
			/* 波斯语 */
			new Object[] {151, Constant.TEST_DATA_DIR + "/language/PERSIAN_SHORT.txt", "PERSIAN"},
			new Object[] {152, Constant.TEST_DATA_DIR + "/language/PERSIAN_LONG.txt", "PERSIAN"},
			/* 波斯尼亚语 */
			new Object[] {153, Constant.TEST_DATA_DIR + "/language/BOSNIAN_SHORT.txt", "BOSNIAN"},
			new Object[] {154, Constant.TEST_DATA_DIR + "/language/BOSNIAN_LONG.txt", "BOSNIAN"},
			/* 增加罗语 */
			new Object[] {155, Constant.TEST_DATA_DIR + "/language/SINHALESE_SHORT.txt", "SINHALESE"},
			new Object[] {156, Constant.TEST_DATA_DIR + "/language/SINHALESE_LONG.txt", "SINHALESE"},
			/* 挪威语 -> 似乎已经有过用例了, 删除。 其余用例不准备重新调整编号了 */
			/* 157 */
			/* 158 */
			/* 葡萄牙语P PORTUGUESE_P 没有找到文本 */
			/* 159 */
			/* 160 */
			/* 葡萄牙语B PORTUGUESE_B 没有找到文本 */
			/* 161 */
			/* 162 */
			/* 科萨语 */
			new Object[] {163, Constant.TEST_DATA_DIR + "/language/XHOSA_SHORT.txt", "XHOSA"},
			new Object[] {164, Constant.TEST_DATA_DIR + "/language/XHOSA_LONG.txt", "XHOSA"},
			/* 祖鲁语 */
			new Object[] {165, Constant.TEST_DATA_DIR + "/language/ZULU_SHORT.txt", "ZULU"},
			new Object[] {166, Constant.TEST_DATA_DIR + "/language/ZULU_LONG.txt", "ZULU"},
			/* GUARANI瓜拉尼语 识别为unknown, 去掉*/
			/* 167 */
			/* 168 */
			/* 塞索托语 */
			new Object[] {169, Constant.TEST_DATA_DIR + "/language/SESOTHO_SHORT.txt", "SESOTHO"},
			new Object[] {170, Constant.TEST_DATA_DIR + "/language/SESOTHO_LONG.txt", "SESOTHO"},
			/* 土库曼语 */
			new Object[] {171, Constant.TEST_DATA_DIR + "/language/TURKMEN_SHORT.txt", "TURKMEN"},
			new Object[] {172, Constant.TEST_DATA_DIR + "/language/TURKMEN_LONG.txt", "TURKMEN"},
			/* 吉尔吉斯语 */
			new Object[] {173, Constant.TEST_DATA_DIR + "/language/KYRGYZ_SHORT.txt", "KYRGYZ"},
			new Object[] {174, Constant.TEST_DATA_DIR + "/language/KYRGYZ_LONG.txt", "KYRGYZ"},
			/* 布列塔尼语 */
			new Object[] {175, Constant.TEST_DATA_DIR + "/language/BRETON_SHORT.txt", "BRETON"},
			new Object[] {176, Constant.TEST_DATA_DIR + "/language/BRETON_LONG.txt", "BRETON"},
			/* TWI阿斯图里亚斯语, 没找到文本 */
			/* 177 */
			/* 178 */
			/* 意第绪语 */
			new Object[] {179, Constant.TEST_DATA_DIR + "/language/YIDDISH_SHORT.txt", "YIDDISH"},
			new Object[] {180, Constant.TEST_DATA_DIR + "/language/YIDDISH_LONG.txt", "YIDDISH"},
			/* SERBO_CROATIAN塞尔维亚-克罗地亚语, 未找到文本 */
			/* 181 */
			/* 182 */
			/* 索马里语 */
			new Object[] {183, Constant.TEST_DATA_DIR + "/language/SOMALI_SHORT.txt", "SOMALI"},
			new Object[] {184, Constant.TEST_DATA_DIR + "/language/SOMALI_LONG.txt", "SOMALI"},
			/* 维吾尔语 */
			new Object[] {185, Constant.TEST_DATA_DIR + "/language/UIGHUR_SHORT.txt", "UIGHUR"},
			new Object[] {186, Constant.TEST_DATA_DIR + "/language/UIGHUR_LONG.txt", "UIGHUR"},
			/* 库尔德语 */
			new Object[] {187, Constant.TEST_DATA_DIR + "/language/KURDISH_SHORT.txt", "KURDISH"},
			new Object[] {188, Constant.TEST_DATA_DIR + "/language/KURDISH_LONG.txt", "KURDISH"},
			/* 蒙古语 */
			new Object[] {189, Constant.TEST_DATA_DIR + "/language/MONGOLIAN_SHORT.txt", "MONGOLIAN"},
			new Object[] {190, Constant.TEST_DATA_DIR + "/language/MONGOLIAN_LONG.txt", "MONGOLIAN"},
			/* 亚美尼亚语 */
			new Object[] {191, Constant.TEST_DATA_DIR + "/language/ARMENIAN_SHORT.txt", "ARMENIAN"},
			new Object[] {192, Constant.TEST_DATA_DIR + "/language/ARMENIAN_LONG.txt", "ARMENIAN"},
			/* 老挝语 */
			new Object[] {193, Constant.TEST_DATA_DIR + "/language/LAOTHIAN_SHORT.txt", "LAOTHIAN"},
			new Object[] {194, Constant.TEST_DATA_DIR + "/language/LAOTHIAN_LONG.txt", "LAOTHIAN"},
			/* 阿尔巴尼亚语 */
			new Object[] {195, Constant.TEST_DATA_DIR + "/language/SINDHI_SHORT.txt", "SINDHI"},
			new Object[] {196, Constant.TEST_DATA_DIR + "/language/SINDHI_LONG.txt", "SINDHI"},
			/* RHAETO_ROMANCE 威尔士语 没有找到合适的文本 */
			/* 197 */
			/* 198 */
			/* 南非荷兰语 */
			new Object[] {199, Constant.TEST_DATA_DIR + "/language/AFRIKAANS_SHORT.txt", "AFRIKAANS"},
			new Object[] {200, Constant.TEST_DATA_DIR + "/language/AFRIKAANS_LONG.txt", "AFRIKAANS"},
			/* 卢森堡语 */
			new Object[] {201, Constant.TEST_DATA_DIR + "/language/LUXEMBOURGISH_SHORT.txt", "LUXEMBOURGISH"},
			new Object[] {202, Constant.TEST_DATA_DIR + "/language/LUXEMBOURGISH_LONG.txt", "LUXEMBOURGISH"},
			/* 缅甸语 */
			new Object[] {203, Constant.TEST_DATA_DIR + "/language/BURMESE_SHORT.txt", "BURMESE"},
			new Object[] {204, Constant.TEST_DATA_DIR + "/language/BURMESE_LONG.txt", "BURMESE"},
			/* 高棉语 */
			new Object[] {205, Constant.TEST_DATA_DIR + "/language/KHMER_SHORT.txt", "KHMER"},
			new Object[] {206, Constant.TEST_DATA_DIR + "/language/KHMER_LONG.txt", "KHMER"},
			/* 藏语 没有合适的文本 */
			/* 207 */
			/* 208 */
			/* DHIVEHI 迪维希语 */
			new Object[] {209, Constant.TEST_DATA_DIR + "/language/DHIVEHI_SHORT.txt", "DHIVEHI"},
			new Object[] {210, Constant.TEST_DATA_DIR + "/language/DHIVEHI_LONG.txt", "DHIVEHI"},
			/* 切罗基语 */
			new Object[] {211, Constant.TEST_DATA_DIR + "/language/CHEROKEE_SHORT.txt", "CHEROKEE"},
			new Object[] {212, Constant.TEST_DATA_DIR + "/language/CHEROKEE_LONG.txt", "CHEROKEE"},
			/* 叙利亚语 */
			new Object[] {213, Constant.TEST_DATA_DIR + "/language/SYRIAC_SHORT.txt", "SYRIAC"},
			new Object[] {214, Constant.TEST_DATA_DIR + "/language/SYRIAC_LONG.txt", "SYRIAC"},
			/* 林堡语 识别成了德语*/
			/* 215 */
			/* 216 */
			/* ORIYA 奥里亚语 */
			new Object[] {217, Constant.TEST_DATA_DIR + "/language/ORIYA_SHORT.txt", "ORIYA"},
			new Object[] {218, Constant.TEST_DATA_DIR + "/language/ORIYA_LONG.txt", "ORIYA"},
			/* 阿萨姆语 */
			new Object[] {219, Constant.TEST_DATA_DIR + "/language/ASSAMESE_SHORT.txt", "ASSAMESE"},
			new Object[] {220, Constant.TEST_DATA_DIR + "/language/ASSAMESE_LONG.txt", "ASSAMESE"},
			/* 科西嘉语, 识别成了Latin */
			/* 221 */
			/* 222 */
			/* INTERLINGUE国际语, 拉丁国际语, 没有找到合适的文本 */
			/* 223 */
			/* 224 */
			/* 哈萨克语 */
			new Object[] {225, Constant.TEST_DATA_DIR + "/language/KAZAKH_SHORT.txt", "KAZAKH"},
			new Object[] {226, Constant.TEST_DATA_DIR + "/language/KAZAKH_LONG.txt", "KAZAKH"},
			/* LINGALA 林加拉语 识别为unknown */
			/* 227 */
			/* 228 */
			/* MOLDAVIAV 摩尔达威亚语 没有找到合适的文本 */
			/* 229 */
			/* 230 */
			/* 普什图语 */
			new Object[] {231, Constant.TEST_DATA_DIR + "/language/PASHTO_SHORT.txt", "PASHTO"},
			new Object[] {232, Constant.TEST_DATA_DIR + "/language/PASHTO_LONG.txt", "PASHTO"},
			/* 克丘亚语 */
			new Object[] {233, Constant.TEST_DATA_DIR + "/language/QUECHUA_SHORT.txt", "QUECHUA"},
			new Object[] {234, Constant.TEST_DATA_DIR + "/language/QUECHUA_LONG.txt", "QUECHUA"},
			/* 修纳语 */
			new Object[] {235, Constant.TEST_DATA_DIR + "/language/SHONA_SHORT.txt", "SHONA"},
			new Object[] {236, Constant.TEST_DATA_DIR + "/language/SHONA_LONG.txt", "SHONA"},
			/* 塔吉克语 */
			new Object[] {237, Constant.TEST_DATA_DIR + "/language/TAJIK_SHORT.txt", "TAJIK"},
			new Object[] {238, Constant.TEST_DATA_DIR + "/language/TAJIK_LONG.txt", "TAJIK"},
			/* 鞑靼语 */
			new Object[] {239, Constant.TEST_DATA_DIR + "/language/TATAR_SHORT.txt", "TATAR"},
			new Object[] {240, Constant.TEST_DATA_DIR + "/language/TATAR_LONG.txt", "TATAR"},
			/* TONGA 汤加语 没找到合适的文本 */
			/* 241 */
			/* 242 */
			/* 约鲁巴语 */
			new Object[] {243, Constant.TEST_DATA_DIR + "/language/YORUBA_SHORT.txt", "YORUBA"},
			new Object[] {244, Constant.TEST_DATA_DIR + "/language/YORUBA_LONG.txt", "YORUBA"},
			/* CREOLES_AND_PIDGINS_ENGLISH 不纯粹的英国方言 没有找到文本 */
			/* 245 */
			/* 246 */
			/* CREOLES_AND_PIDGINS_FRENCH_BASED 不纯粹的法国方言 没有找到文本*/
			/* 247 */
			/* 248 */
			/* CREOLES_AND_PIDGINS_PORTUGUESE_BASED 不纯粹的葡萄牙方言 没有找到文本*/
			/* 249 */
			/* 250 */
			/* CREOLES_AND_PIDGINS_OTHER 克里奥尔人和皮金斯其它人 没有找到文本 */
			/* 251 */
			/* 252 */
			/* 毛利语 */
			new Object[] {253, Constant.TEST_DATA_DIR + "/language/MAORI_SHORT.txt", "MAORI"},
			new Object[] {254, Constant.TEST_DATA_DIR + "/language/MAORI_LONG.txt", "MAORI"},
			/* 沃洛夫语 */
			new Object[] {255, Constant.TEST_DATA_DIR + "/language/WOLOF_SHORT.txt", "WOLOF"},
			new Object[] {256, Constant.TEST_DATA_DIR + "/language/WOLOF_LONG.txt", "WOLOF"},
			/* ABKHAZIAN 阿布哈西亚与 没有找到文本 */
			/* 257 */
			/* 258 */
			/* AFAR 阿法尔语 没有找到文本 */
			/* 259 */
			/* 260 */
			/* 艾玛拉语 */
			new Object[] {261, Constant.TEST_DATA_DIR + "/language/AYMARA_SHORT.txt", "AYMARA"},
			new Object[] {262, Constant.TEST_DATA_DIR + "/language/AYMARA_LONG.txt", "AYMARA"},
			/* 巴什基尔语 */
			new Object[] {261, Constant.TEST_DATA_DIR + "/language/BASHKIR_SHORT.txt", "BASHKIR"},
			new Object[] {262, Constant.TEST_DATA_DIR + "/language/BASHKIR_LONG.txt", "BASHKIR"},
			/* 比斯拉马语 识别为英文 */
			/* 263 */
			/* 264 */
			/* DZONGHA 不丹语  没有找到文本*/
			/* 265 */
			/* 266 */
			/* FIJIAN 斐济语 没有找到文本 */
			/* 269 */
			/* 270 */
			/* GREENLANDIC 格陵兰语 被识别为英语 */
			/* 271 */
			/* 272 */
			/* HAUSA 豪萨语 */
			new Object[] {273, Constant.TEST_DATA_DIR + "/language/HAUSA_SHORT.txt", "HAUSA"},
			new Object[] {274, Constant.TEST_DATA_DIR + "/language/HAUSA_LONG.txt", "HAUSA"},
			/* 海地语 */
			new Object[] {275, Constant.TEST_DATA_DIR + "/language/HAITIAN_CREOLE_SHORT.txt", "HAITIAN_CREOLE"},
			new Object[] {276, Constant.TEST_DATA_DIR + "/language/HAITIAN_CREOLE_LONG.txt", "HAITIAN_CREOLE"},
			/* INUPIAK 未知语言 */
			/* 277 */
			/* 278 */
			/* INUKTITUT 因纽特语 没有找到文本 */
			/* 279 */
			/* 280 */
			/* KASHMIRI 克什米尔语 没有找到文本*/
			/* 281 */
			/* 282 */
			/* 卢旺达语 */
			new Object[] {283, Constant.TEST_DATA_DIR + "/language/KINYARWANDA_SHORT.txt", "KINYARWANDA"},
			new Object[] {284, Constant.TEST_DATA_DIR + "/language/KINYARWANDA_LONG.txt", "KINYARWANDA"},
			/* 马拉加斯语 */
			new Object[] {285, Constant.TEST_DATA_DIR + "/language/MALAGASY_SHORT.txt", "MALAGASY"},
			new Object[] {286, Constant.TEST_DATA_DIR + "/language/MALAGASY_LONG.txt", "MALAGASY"},
			/* NAURU瑙鲁语 没有找到文本 */
			/* 287 */
			/* 288 */
			/* OROMO 奥罗莫语 识别成了英语 */
			/* 289 */
			/* 290 */
			/* RUND 隆迪语 没有找到文本 */
			/* 291 */
			/* 292 */
			/* SAMOAN 萨摩亚语 识别成 unknown */
			/* 293 */
			/* 294 */
			/* SANGO桑戈语 识别成了英语 */
			/* 295 */
			/* 296 */
			/* 梵语 */
			new Object[] {297, Constant.TEST_DATA_DIR + "/language/SANSKRIT_SHORT.txt", "SANSKRIT"},
			new Object[] {298, Constant.TEST_DATA_DIR + "/language/SANSKRIT_LONG.txt", "SANSKRIT"},
			/* SISWANT 未知语言 */
			/* 299 */
			/* 300 */
			/* 聪加语 */
			new Object[] {301, Constant.TEST_DATA_DIR + "/language/TSONGA_SHORT.txt", "TSONGA"},
			new Object[] {302, Constant.TEST_DATA_DIR + "/language/TSONGA_LONG.txt", "TSONGA"},
			/* TSWANA 茨瓦纳语 没有找到文本 */
			/* 303 */
			/* 304 */
			/* VOLAPUK 沃拉普克语 没有找到文本 */
			/* 305 */
			/* 306 */
			/* ZHUANG 状语 */
			new Object[] {307, Constant.TEST_DATA_DIR + "/language/ZHUANG1.txt", "ZHUANG"},
			new Object[] {308, Constant.TEST_DATA_DIR + "/language/ZHUANG2.txt", "ZHUANG"},
			/* KHASI 卡西语 没有找到文本 */
			/* 309 */
			/* 310 */
			/* SCOTS 苏格兰语 识别为英语 */
			/* 311 */
			/* 312 */
			/* GANDA 干达语 没有找到合适的文本 */
			/* 313 */
			/* 314 */
			/* MANX 曼岛语 没有找到文本 */
			/* 315 */
			/* 316 */
			/* MONTENEGRIN 黑山语 识别成了英语*/
			/* 317 */
			/* 318 */
			/* NUM_LANGUAGES 未知语言 */
			/* 319 */
			/* 320 */
		};
	}
	
	@Test(dataProvider = "languageDataProvider")
	public void language(int caseId, String path, String language) {
		LOGGER.info("caseId="+caseId);
		try {
			String text = FileOperator.read(path).replaceAll(System.lineSeparator(), "");
			LanguageResult result = request.language(text);
			assertEquals(result.getCode(), "1");
			assertEquals(result.getMessage(), "操作成功");
			assertEquals(result.getResult(), language);
		}catch(Exception e) {
			fail(Other.stackTraceToString(e));
		}
	}

	@DataProvider(name = "languageErrDataProvider")
	public Object[][] languageErrDataProvider(Method method){
		if(!"languageErr".equals(method.getName()))
			return null;
		return new Object[][]{
			/* 空文本 */
			new Object[] {1, "", "0"},
			/* 混合文本, 这里识别为英语 */
			new Object[] {2, "隔离,人权没了; quarantine, no human rights; 不隔离, 人全没了; no quarantine, no human left", "1"},
			/* 符号文本 */
			new Object[] {3, "!@#$%^&*()", "1"},
		};
	}

	@Test(dataProvider = "languageErrDataProvider")
	public void languageErr(int caseId, String text, String expectation){
		try{
			LanguageResult language = request.language(text);
			assertEquals(language.getCode(), expectation);
		}catch(Exception e){
			fail(Other.stackTraceToString(e));
		}
	}
}
