package com.trs.dlvrs.test.function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonSyntaxException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.trs.ckm.test.function.Constant;
import com.trs.ckm.util.FakeShell;
import com.trs.ckm.util.Other;
import com.trs.dlvrs.api.master.TRSVRSRequest;
import com.trs.dlvrs.api.pojo.ObjectDetectionResult;
import com.trs.dlvrs.api.pojo.ObjectDetectionResult.Result;
import com.trs.dlvrs.test.aspect.DLVRSAspectConfig;

public class ObjectDetectionTest {
	private final static Logger LOGGER = LogManager.getLogger(ObjectDetectionTest.class);
	private final static String TEST_DATA_DIRECTORY = "./testdata/input/dlvrs/ObjectDetection";
	
	private AnnotationConfigApplicationContext context;
	private TRSVRSRequest request;
	private FakeShell fakeShell;
	private String remoteDirectory;
	private String className = this.getClass().getName();
	
	@BeforeClass
	public void before() {
		context = new AnnotationConfigApplicationContext(DLVRSAspectConfig.class);
		request = context.getBean(TRSVRSRequest.class);
		fakeShell = context.getBean(FakeShell.class);
		remoteDirectory = (String) context.getBean("remoteDirectory");
		Constant.reconfigureLog4j2("./config/log4j2_dlvrs.xml");
	}
	
	@AfterClass
	public void after() {
		if(context != null)
			context.close();
	}
	
	/*---------- 旗帜检测 ----------*/
	@DataProvider(name = "detectionFlagDataProvider")
	public Object[][] detectionFlagDataProvider(Method method){
		String directory = TEST_DATA_DIRECTORY + "/flag";
		return new Object[][] {
			/* 亚洲 */
			new Object[] {1, directory + "/阿富汗.jpg", "0.4", "1", new String[] {"阿富汗"}},
			new Object[] {2, directory + "/朝鲜.jpg", "0.6", "1", new String[] {"朝鲜"}},
			new Object[] {3, directory + "/俄罗斯.jpg", "0.6", "1", new String[] {"俄罗斯"}},
			new Object[] {4, directory + "/哈萨克斯坦.jpg", "0.6", "1", new String[] {"哈萨克斯坦"}},
			new Object[] {5, directory + "/中国.png", "0.6", "1", new String[] {"中国"}},
			new Object[] {6, directory + "/沙特阿拉伯.jpg", "0.6", "1", new String[] {"沙特阿拉伯"}},
			new Object[] {7, directory + "/伊朗.jpg", "0.6", "1", new String[] {"伊朗"}},
			new Object[] {8, directory + "/柬埔寨.jpg", "0.6", "1", new String[] {"柬埔寨"}},
			new Object[] {9, directory + "/塔吉克斯坦.jpg", "0.6", "1", new String[] {"塔吉克斯坦"}},
			new Object[] {10, directory + "/吉尔吉斯斯坦.jpg", "0.6", "1", new String[] {"吉尔吉斯斯坦"}},
			new Object[] {11, directory + "/老挝.jpg", "0.6", "1", new String[] {"老挝"}},
			new Object[] {12, directory + "/东帝汶.jpg", "0.6", "1", new String[] {"东帝汶"}},
			new Object[] {13, directory + "/亚美尼亚.jpg", "0.6", "1", new String[] {"亚美尼亚"}},
			new Object[] {14, directory + "/土耳其.jpg", "0.6", "1", new String[] {"土耳其"}},
			new Object[] {15, directory + "/尼泊尔.jpg", "0.6", "1", new String[] {"尼泊尔"}},
			new Object[] {16, directory + "/塞浦路斯.jpg", "0.6", "1", new String[] {"塞浦路斯"}},
			new Object[] {17, directory + "/叙利亚.jpg", "0.6", "1", new String[] {"叙利亚"}},
			new Object[] {18, directory + "/马来西亚.jpg", "0.6", "1", new String[] {"马来西亚"}},
			new Object[] {19, directory + "/阿曼.jpg", "0.6", "1", new String[] {"阿曼"}},
			new Object[] {20, directory + "/阿联酋.jpg", "0.6", "1", new String[] {"阿拉伯联合酋长国"}},
			new Object[] {21, directory + "/也门.jpg", "0.6", "1", new String[] {"也门"}},
			new Object[] {22, directory + "/斯里兰卡.jpg", "0.6", "1", new String[] {"斯里兰卡"}},
			new Object[] {23, directory + "/韩国.jpg", "0.6", "1", new String[] {"韩国"}},
			new Object[] {24, directory + "/巴基斯坦.jpg", "0.3", "1", new String[] {"巴基斯坦"}},
			new Object[] {25, directory + "/阿塞拜疆.png", "0.6", "1", new String[] {"阿塞拜疆"}},
			new Object[] {26, directory + "/卡塔尔.jpg", "0.6", "1", new String[] {"卡塔尔"}},
			new Object[] {27, directory + "/不丹.jpg", "0.6", "1", new String[] {"不丹"}},
			new Object[] {28, directory + "/马尔代夫.jpg", "0.6", "1", new String[] {"马尔代夫"}},
			new Object[] {29, directory + "/以色列.jpg", "0.6", "1", new String[] {"以色列"}},
			new Object[] {30, directory + "/土库曼斯坦.jpg", "0.6", "1", new String[] {"土库曼斯坦"}},
			new Object[] {31, directory + "/缅甸.jpg", "0.6", "1", new String[] {"缅甸"}},
			new Object[] {32, directory + "/蒙古.jpg", "0.6", "1", new String[] {"蒙古"}},
			new Object[] {33, directory + "/巴林.jpg", "0.6", "1", new String[] {"巴林"}},
			new Object[] {34, directory + "/孟加拉国.jpg", "0.6", "1", new String[] {"孟加拉国"}},
			new Object[] {35, directory + "/科威特.jpg", "0.6", "1", new String[] {"科威特"}},
			new Object[] {36, directory + "/泰国.jpg", "0.6", "1", new String[] {"泰国"}},
			new Object[] {37, directory + "/文莱.jpg", "0.6", "1", new String[] {"文莱"}},
			new Object[] {38, directory + "/菲律宾.jpg", "0.6", "1", new String[] {"菲律宾"}},
			new Object[] {39, directory + "/印度.jpg", "0.6", "1", new String[] {"印度"}},
			new Object[] {40, directory + "/黎巴嫩.jpg", "0.6", "1", new String[] {"黎巴嫩"}},
			new Object[] {41, directory + "/新加坡.jpg", "0.6", "1", new String[] {"新加坡"}},
			new Object[] {42, directory + "/巴勒斯坦.jpg", "0.6", "1", new String[] {"巴勒斯坦"}},
			new Object[] {43, directory + "/伊拉克.jpg", "0.6", "1", new String[] {"伊拉克"}},
			new Object[] {44, directory + "/约旦.jpg", "0.6", "1", new String[] {"约旦"}},
			new Object[] {45, directory + "/越南.jpg", "0.6", "1", new String[] {"越南"}},
			new Object[] {46, directory + "/日本.jpg", "0.6", "1", new String[] {"日本"}},
			new Object[] {47, directory + "/乌兹别克斯坦.jpg", "0.6", "1", new String[] {"乌兹别克斯坦"}},
			new Object[] {48, directory + "/印度尼西亚.jpg", "0.6", "1", new String[] {"印度尼西亚"}},
			/* 欧洲 */
			new Object[] {49, directory + "/波兰.jpg", "0.6", "1", new String[] {"波兰"}},
			new Object[] {50, directory + "/冰岛.jpg", "0.6", "1", new String[] {"冰岛"}},
			new Object[] {51, directory + "/匈牙利.jpg", "0.6", "1", new String[] {"匈牙利"}},
			new Object[] {52, directory + "/北爱尔兰.jpg", "0.6", "1", new String[] {"北爱尔兰"}},
			new Object[] {53, directory + "/苏格兰.jpg", "0.6", "1", new String[] {"苏格兰"}},
			new Object[] {54, directory + "/乌克兰.png", "0.6", "1", new String[] {"乌克兰"}},
			new Object[] {55, directory + "/摩尔多瓦共和国.jpg", "0.6", "1", new String[] {"摩尔多瓦共和国"}},
			new Object[] {56, directory + "/爱沙尼亚.jpg", "0.6", "1", new String[] {"爱沙尼亚"}},
			new Object[] {57, directory + "/摩纳哥.jpg", "0.6", "1", new String[] {"摩纳哥"}},
			new Object[] {58, directory + "/英格兰.jpg", "0.6", "1", new String[] {"英格兰"}},
			new Object[] {59, directory + "/瑞典.jpg", "0.6", "1", new String[] {"瑞典"}},
			new Object[] {60, directory + "/比利时.jpg", "0.6", "1", new String[] {"比利时"}},
			new Object[] {61, directory + "/希腊.jpg", "0.6", "1", new String[] {"希腊"}},
			new Object[] {62, directory + "/安道尔.jpg", "0.6", "1", new String[] {"安道尔"}},
			new Object[] {63, directory + "/马其顿.jpg", "0.6", "1", new String[] {"马其顿"}},
			new Object[] {64, directory + "/瑞士.jpg", "0.6", "1", new String[] {"瑞士"}},
			new Object[] {65, directory + "/保加利亚.jpg", "0.6", "1", new String[] {"保加利亚"}},
			new Object[] {66, directory + "/德国.jpg", "0.6", "1", new String[] {"德国"}},
			new Object[] {67, directory + "/英国.jpg", "0.6", "1", new String[] {"英国"}},
			new Object[] {68, directory + "/拉脱维亚.jpg", "0.6", "1", new String[] {"拉脱维亚"}},
			new Object[] {69, directory + "/奥地利.jpg", "0.6", "1", new String[] {"奥地利"}},
			new Object[] {70, directory + "/圣马力诺.jpg", "0.6", "1", new String[] {"圣马力诺"}},
			new Object[] {71, directory + "/西班牙.jpg", "0.6", "1", new String[] {"西班牙"}},
			new Object[] {72, directory + "/葡萄牙.jpg", "0.6", "1", new String[] {"葡萄牙"}},
			new Object[] {73, directory + "/卢森堡.jpg", "0.6", "1", new String[] {"卢森堡"}},
			new Object[] {74, directory + "/塞尔维亚.jpg", "0.6", "1", new String[] {"塞尔维亚"}},
			new Object[] {75, directory + "/法国.jpg", "0.6", "1", new String[] {"法国"}},
			new Object[] {76, directory + "/意大利.jpg", "0.6", "1", new String[] {"意大利"}},
			new Object[] {77, directory + "/列支敦士登.jpg", "0.3", "1", new String[] {"列支敦士登"}},
			new Object[] {78, directory + "/白俄罗斯.jpg", "0.6", "1", new String[] {"白俄罗斯"}},
			new Object[] {79, directory + "/捷克共和国.jpg", "0.6", "1", new String[] {"捷克共和国"}},
			new Object[] {80, directory + "/丹麦.jpg", "0.6", "1", new String[] {"丹麦"}},
			new Object[] {81, directory + "/阿尔巴尼亚.jpg", "0.6", "1", new String[] {"阿尔巴尼亚"}},
			new Object[] {82, directory + "/荷兰.jpg", "0.6", "1", new String[] {"荷兰"}},
			new Object[] {83, directory + "/梵提冈.jpg", "0.6", "1", new String[] {"梵蒂冈"}},
			new Object[] {84, directory + "/克罗地亚.jpg", "0.6", "1", new String[] {"克罗地亚"}},
			new Object[] {85, directory + "/威尔士.jpg", "0.6", "1", new String[] {"威尔士"}},
			new Object[] {86, directory + "/波斯尼亚和黑塞哥维那.jpg", "0.6", "1", new String[] {"波斯尼亚和黑塞哥维那"}},
			new Object[] {87, directory + "/马耳他.png", "0.3", "1", new String[] {"马耳他"}},
			new Object[] {88, directory + "/芬兰.jpg", "0.6", "1", new String[] {"芬兰"}},
			new Object[] {89, directory + "/爱尔兰.jpg", "0.6", "1", new String[] {"爱尔兰"}},
			new Object[] {90, directory + "/立陶宛.jpg", "0.6", "1", new String[] {"立陶宛"}},
			new Object[] {91, directory + "/斯洛文尼亚.jpg", "0.6", "1", new String[] {"斯洛文尼亚"}},
			new Object[] {92, directory + "/斯洛伐克.jpg", "0.6", "1", new String[] {"斯洛伐克"}},
			new Object[] {93, directory + "/罗马尼亚.jpg", "0.6", "1", new String[] {"罗马尼亚"}},
			new Object[] {94, directory + "/挪威.jpg", "0.6", "1", new String[] {"挪威"}},
			/* 非洲 */
			new Object[] {95, directory + "/肯尼亚.jpg", "0.6", "1", new String[] {"肯尼亚"}},
			new Object[] {96, directory + "/加纳.jpg", "0.6", "1", new String[] {"加纳"}},
			new Object[] {97, directory + "/利比里亚.jpg", "0.6", "1", new String[] {"利比里亚"}},
			new Object[] {98, directory + "/圣多美和普林西比.jpg", "0.6", "1", new String[] {"圣多美和普林西比"}},
			new Object[] {99, directory + "/纳米比亚.jpg", "0.6", "1", new String[] {"纳米比亚"}},
			new Object[] {100, directory + "/坦桑尼亚.jpg", "0.6", "1", new String[] {"坦桑尼亚"}},
			new Object[] {101, directory + "/赞比亚.jpg", "0.6", "1", new String[] {"赞比亚"}},
			new Object[] {102, directory + "/莱索托.jpg", "0.6", "1", new String[] {"莱索托"}},
			new Object[] {103, directory + "/吉布提.jpg", "0.6", "1", new String[] {"吉布提"}},
			new Object[] {104, directory + "/安哥拉.jpg", "0.6", "1", new String[] {"安哥拉"}},
			new Object[] {105, directory + "/突尼斯.jpg", "0.6", "1", new String[] {"突尼斯"}},
			new Object[] {106, directory + "/几内亚.jpg", "0.6", "1", new String[] {"几内亚"}},
			new Object[] {107, directory + "/塞拉利昂.jpg", "0.6", "1", new String[] {"塞拉利昂"}},
			new Object[] {108, directory + "/阿尔及利亚.jpg", "0.6", "1", new String[] {"阿尔及利亚", "阿尔及利亚"}},
			new Object[] {109, directory + "/卢旺达.jpg", "0.6", "1", new String[] {"卢旺达"}},
			new Object[] {110, directory + "/喀麦隆.png", "0.6", "1", new String[] {"喀麦隆"}},
			new Object[] {111, directory + "/中非共和国.jpg", "0.6", "1", new String[] {"中非共和国"}},
			new Object[] {112, directory + "/塞舌尔群岛.jpg", "0.6", "1", new String[] {"塞舌尔群岛"}},
			new Object[] {113, directory + "/布隆迪.jpg", "0.6", "1", new String[] {"布隆迪"}},
			new Object[] {114, directory + "/刚果民主共和国.jpg", "0.6", "1", new String[] {"刚果民主共和国"}},
			new Object[] {115, directory + "/莫桑比克.jpg", "0.6", "1", new String[] {"莫桑比克"}},
			new Object[] {116, directory + "/贝宁.jpg", "0.6", "1", new String[] {"贝宁"}},
			new Object[] {117, directory + "/加蓬.png", "0.6", "1", new String[] {"加蓬"}},
			new Object[] {118, directory + "/利比亚.jpg", "0.6", "1", new String[] {"利比亚"}},
			new Object[] {119, directory + "/赤道几内亚.jpg", "0.6", "1", new String[] {"赤道几内亚"}},
			new Object[] {120, directory + "/马达加斯加.jpg", "0.6", "1", new String[] {"马达加斯加", "马达加斯加"}},
			new Object[] {121, directory + "/科摩罗.jpg", "0.6", "1", new String[] {"科摩罗"}},
			/* 塞内加尔和喀麦隆……晕 */
			new Object[] {122, directory + "/塞内加尔.png", "0.6", "1", new String[] {"塞内加尔"}},
			new Object[] {123, directory + "/尼日利亚.jpg", "0.6", "1", new String[] {"尼日利亚"}},
			new Object[] {124, directory + "/佛得角.jpg", "0.6", "1", new String[] {"佛得角"}},
			new Object[] {125, directory + "/南苏丹.jpg", "0.6", "1", new String[] {"南苏丹"}},
			new Object[] {126, directory + "/多哥.jpg", "0.6", "1", new String[] {"多哥"}},
			new Object[] {127, directory + "/冈比亚.jpg", "0.6", "1", new String[] {"冈比亚"}},
			new Object[] {128, directory + "/毛里塔尼亚.jpg", "0.6", "1", new String[] {"毛里塔尼亚"}},
			new Object[] {129, directory + "/索马里.jpg", "0.6", "1", new String[] {"索马里"}},
			/* 130 马约特岛, 没有找到 */
			new Object[] {131, directory + "/苏丹.jpg", "0.6", "1", new String[] {"苏丹"}},
			new Object[] {132, directory + "/几内亚比绍.jpg", "0.6", "1", new String[] {"几内亚比绍"}},
			new Object[] {133, directory + "/乌干达.jpg", "0.6", "1", new String[] {"乌干达"}},
			new Object[] {134, directory + "/毛里求斯.jpg", "0.6", "1", new String[] {"毛里求斯"}},
			/* 和几内亚的国旗十分相似, 都是三色旗, 颜色选用的都一样, 就是顺序不一样, 注意区分…算了, 晕就晕吧 */
			new Object[] {135, directory + "/马里.jpg", "0.6", "1", new String[] {"马里"}},
			new Object[] {136, directory + "/布基纳法索.jpg", "0.3", "1", new String[] {"布基纳法索"}},
			new Object[] {137, directory + "/南非.jpg", "0.6", "1", new String[] {"南非"}},
			new Object[] {138, directory + "/津巴布韦.jpg", "0.6", "1", new String[] {"津巴布韦"}},
			new Object[] {139, directory + "/厄立特里亚.jpg", "0.6", "1", new String[] {"厄立特里亚"}},
			new Object[] {140, directory + "/乍得.jpg", "0.6", "1", new String[] {"乍得"}},
			new Object[] {141, directory + "/埃塞俄比亚.jpg", "0.6", "1", new String[] {"埃塞俄比亚"}},
			new Object[] {142, directory + "/刚果共和国.jpg", "0.6", "1", new String[] {"刚果共和国"}},
			new Object[] {143, directory + "/西撒哈拉.jpg", "0.6", "1", new String[] {"西撒哈拉"}},
			new Object[] {144, directory + "/斯威士兰.png", "0.6", "1", new String[] {"斯威士兰"}},
			new Object[] {145, directory + "/科特迪瓦.jpg", "0.6", "1", new String[] {"科特迪瓦"}},
			new Object[] {146, directory + "/马拉维.jpg", "0.6", "1", new String[] {"马拉维"}},
			new Object[] {147, directory + "/尼日尔.jpg", "0.6", "1", new String[] {"尼日尔"}},
			new Object[] {148, directory + "/埃及.jpg", "0.6", "1", new String[] {"埃及"}},
			new Object[] {149, directory + "/博茨瓦纳.jpg", "0.6", "1", new String[] {"博茨瓦纳"}},
			new Object[] {150, directory + "/摩洛哥.jpg", "0.6", "1", new String[] {"摩洛哥"}},
			/* 北美洲 */
			new Object[] {151, directory + "/危地马拉.jpg", "0.6", "1", new String[] {"危地马拉"}},
			new Object[] {152, directory + "/牙买加.jpg", "0.6", "1", new String[] {"牙买加"}},
			new Object[] {153, directory + "/圣文森特和格林纳丁斯.jpg", "0.6", "1", new String[] {"圣文森特和格林纳丁斯"}},
			new Object[] {154, directory + "/古巴.jpg", "0.6", "1", new String[] {"古巴"}},
			new Object[] {155, directory + "/特立尼达和多巴哥.jpg", "0.6", "1", new String[] {"特立尼达和多巴哥"}},
			new Object[] {156, directory + "/格陵兰.jpg", "0.6", "1", new String[] {"格陵兰"}},
			new Object[] {157, directory + "/阿鲁巴.jpg", "0.6", "1", new String[] {"阿鲁巴岛"}},
			new Object[] {158, directory + "/洪都拉斯.jpg", "0.6", "1", new String[] {"洪都拉斯"}},
			new Object[] {159, directory + "/蒙特塞拉特.jpg", "0.6", "1", new String[] {"蒙特塞拉特"}},
			new Object[] {160, directory + "/格林纳达.jpg", "0.6", "1", new String[] {"格林纳达"}},
			new Object[] {161, directory + "/百慕大.png", "0.6", "1", new String[] {"百慕大"}},
			new Object[] {162, directory + "/圣基茨和尼维斯.jpg", "0.6", "1", new String[] {"圣基茨和尼维斯"}},
			new Object[] {163, directory + "/巴拿马.jpg", "0.6", "1", new String[] {"巴拿马"}},
			new Object[] {164, directory + "/萨尔瓦多.jpg", "0.3", "1", new String[] {"萨尔瓦多"}},
			new Object[] {165, directory + "/加拿大.jpg", "0.6", "1", new String[] {"加拿大"}},
			new Object[] {166, directory + "/波多黎各.png", "0.6", "1", new String[] {"波多黎各"}},
			new Object[] {167, directory + "/哥斯达黎加.jpg", "0.6", "1", new String[] {"哥斯达黎加"}},
			new Object[] {168, directory + "/尼加拉瓜.jpg", "0.6", "1", new String[] {"尼加拉瓜"}},
			new Object[] {169, directory + "/海地.jpg", "0.6", "1", new String[] {"海地"}},
			new Object[] {170, directory + "/多米尼加.jpg", "0.6", "1", new String[] {"多明尼加共和国"}},
			new Object[] {171, directory + "/圣卢西亚.jpg", "0.6", "1", new String[] {"圣卢西亚"}},
			new Object[] {172, directory + "/巴巴多斯.jpg", "0.6", "1", new String[] {"巴巴多斯"}},
			new Object[] {173, directory + "/安提瓜和巴布达.jpg", "0.6", "1", new String[] {"安提瓜和巴布达"}},
			new Object[] {174, directory + "/巴哈马.jpg", "0.6", "1", new String[] {"巴哈马"}},
			/* 175 多……多明尼加共和国??? 和170有啥区别??? */
			/* 170 识别成了多明尼加共和国了 */
			new Object[] {176, directory + "/伯利兹城.jpg", "0.6", "1", new String[] {"伯利兹城"}},
			new Object[] {177, directory + "/美国.jpg", "0.6", "1", new String[] {"美国"}},
			new Object[] {178, directory + "/墨西哥.jpg", "0.6", "1", new String[] {"墨西哥"}},
			
			/* 2021.10.14 时间关系, 后边的国家或组织不能一一尝试了, 这里只挑选几个
			 * 编号和手册中一一对应, 可能会出现用例编号跳跃的情况 */
			new Object[] {180, directory + "/阿根廷.jpg", "0.4", "1", new String[] {"阿根廷"}},
			new Object[] {181, directory + "/哥伦比亚.jpg", "0.6", "1", new String[] {"哥伦比亚"}},
			new Object[] {183, directory + "/巴西.jpg", "0.6", "1", new String[] {"巴西"}},
			new Object[] {188, directory + "/秘鲁.jpg", "0.6", "1", new String[] {"秘鲁"}},
			new Object[] {190, directory + "/智利.jpg", "0.6", "1", new String[] {"智利"}},
			
			new Object[] {192, directory + "/巴布亚新几内亚.jpg", "0.6", "1", new String[] {"巴布亚新几内亚"}},
			new Object[] {198, directory + "/澳大利亚.jpg", "0.6", "1", new String[] {"澳大利亚"}},
			new Object[] {203, directory + "/新西兰.jpg", "0.6", "1", new String[] {"新西兰"}},
			
			new Object[] {208, directory + "/伊斯兰国.jpg", "0.6", "1", new String[] {"伊斯兰国"}},
			new Object[] {209, directory + "/中华民国.jpg", "0.6", "1", new String[] {"中华民国"}},
			
			new Object[] {212, directory + "/东盟.jpg", "0.6", "1", new String[] {"东盟"}},
			new Object[] {213, directory + "/欧盟.jpg", "0.6", "1", new String[] {"欧盟"}},
		};
	}
	/**
	 * 旗帜检测检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code 预期结果码
	 * @param labels 预期标签
	 */
//	@Test(dataProvider = "detectionFlagDataProvider")
	public void detectionFlagFile(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		ObjectDetectionResult result = request.detectionFlagFile(filePath, thresh);
		assertEquals(result.getCode(), code);
		List<ObjectDetectionResult.Result> subResult = result.fetchResult();
		assertEquals(subResult.size(), labels.length);
		for(int i=0; i<labels.length; i++)
			assertEquals(subResult.get(i).getLabel(), labels[i]);
	}
	/**
	 * 旗帜检测检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
//	@Test(dataProvider = "detectionFlagDataProvider")
	public void detectionFlagPath(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			ObjectDetectionResult result = request.detectionFlagPath(remoteFilePath, thresh);
			assertEquals(result.getCode(), code);
			List<ObjectDetectionResult.Result> subResult = result.fetchResult();
			assertEquals(subResult.size(), labels.length);
			for(int i=0; i<labels.length; i++)
				assertEquals(subResult.get(i).getLabel(), labels[i]);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f "+remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	
	/*---------- 商品logo检测 ----------*/
	
	/* 注意, 测试用例仅挑选了一部分图片作为测试数据, 并没有完全覆盖附录中的所有商标 */
	@DataProvider(name = "detectionLogoDataProvider")
	public Object[][] detectionLogoDataProvider(Method method){
		String directory = TEST_DATA_DIRECTORY + "/logo";
		return new Object[][] {
			new Object[] {1, directory + "/ABUS.jpg", "0.6", "1", new String[] {"ABUS"}},
			new Object[] {2, directory + "/audi.jpg", "0.6", "1", new String[] {"奥迪", "奥迪"}},
			new Object[] {3, directory + "/Gucci.jpg", "0.6", "1", new String[] {"古驰"}},
			new Object[] {4, directory + "/HM.jpg", "0.6", "1", new String[] {"H&M"}},
			new Object[] {5, directory + "/IBM.jpg", "0.6", "1", new String[] {"IBM"}},
			new Object[] {6, directory + "/JackInTheBox.jpg", "0.6", "1", new String[] {"Jack in the Box"}},
			new Object[] {7, directory + "/nivea.jpg", "0.6", "1", new String[] {"妮维雅"}},
			new Object[] {8, directory + "/QQ.jpg", "0.6", "1", new String[] {"腾讯QQ"}},
			new Object[] {9, directory + "/walmart.jpg", "0.6", "1", new String[] {"沃尔玛"}},
			new Object[] {10, directory + "/阿迪达斯.jpg", "0.6", "1", new String[] {"阿迪达斯"}},
			new Object[] {11, directory + "/爱普生.jpg", "0.6", "1", new String[] {"爱普生"}},
			new Object[] {12, directory + "/安踏.jpg", "0.6", "1", new String[] {"安踏"}},
			new Object[] {13, directory + "/宝马.jpg", "0.6", "1", new String[] {"宝马"}},
			new Object[] {14, directory + "/丰田.jpg", "0.6", "1", new String[] {"丰田"}},
			new Object[] {15, directory + "/高露洁.jpg", "0.6", "1", new String[] {"高露洁", "高露洁"}},
			new Object[] {16, directory + "/海尔.jpg", "0.6", "1", new String[] {"海尔集团"}},
			new Object[] {17, directory + "/海信集团.jpg", "0.6", "1", new String[] {"海信集团"}},
			new Object[] {18, directory + "/鸿星尔克.jpg", "0.6", "1", new String[] {"鸿星尔克"}},
			new Object[] {19, directory + "/汇源.jpg", "0.6", "1", new String[] {"汇源"}},
			new Object[] {20, directory + "/佳能.jpg", "0.6", "1", new String[] {"佳能"}},
			new Object[] {21, directory + "/奸如磐石硕.jpg", "0.6", "1", new String[] {"华硕"}},
			new Object[] {22, directory + "/卡帕.jpg", "0.6", "1", new String[] {"卡帕"}},
			new Object[] {23, directory + "/可口可乐.jpg", "0.6", "1", new String[] {"可口可乐"}},
			new Object[] {24, directory + "/兰博基尼.jpg", "0.6", "1", new String[] {"兰博基尼"}},
			new Object[] {25, directory + "/乐高.jpg", "0.3", "1", new String[] {"乐高"}},
			new Object[] {26, directory + "/雷克萨斯.jpg", "0.6", "1", new String[] {"雷克萨斯", "雷克萨斯"}},
			new Object[] {27, directory + "/泸州老窖.jpg", "0.6", "1", new String[] {"泸州老窖", "泸州老窖"}},
			new Object[] {28, directory + "/麦当劳.jpg", "0.6", "1", new String[] {"麦当劳"}},
			new Object[] {29, directory + "/米其林.jpg", "0.6", "1", new String[] {"米其林"}},
			new Object[] {30, directory + "/飒拉.jpg", "0.6", "1", new String[] {"飒拉"}},
			new Object[] {31, directory + "/台独嘉.jpg", "0.6", "1", new String[] {"技嘉科技"}},
			new Object[] {32, directory + "/铁板熊掌普.jpg", "0.6", "1", new String[] {"惠普"}},
			new Object[] {33, directory + "/五芳斋.jpg", "0.6", "1", new String[] {"五芳斋"}},
			new Object[] {34, directory + "/西凤酒.jpg", "0.6", "1", new String[] {"西凤酒","西凤酒"}},
			new Object[] {35, directory + "/亚马逊.jpg", "0.6", "1", new String[] {"亚马逊"}},
			new Object[] {36, directory + "/伊利.jpg", "0.6", "1", new String[] {"伊利"}},
			new Object[] {37, directory + "/英菲尼迪.jpg", "0.6", "1", new String[] {"英菲尼迪"}},
			new Object[] {38, directory + "/英特尔.jpg", "0.6", "1", new String[] {"英特尔"}},
			new Object[] {39, directory + "/英伟达.jpg", "0.6", "1", new String[] {"英伟达"}},
			new Object[] {40, directory + "/优衣库.jpg", "0.6", "1", new String[] {"优衣库"}},
			new Object[] {41, directory + "/尤尼克斯.jpg", "0.6", "1", new String[] {"尤尼克斯"}},
			new Object[] {42, directory + "/有友.jpg", "0.6", "1", new String[] {"有友","有友"}},
			new Object[] {43, directory + "/郁美净.jpg", "0.6", "1", new String[] {"郁美净"}},
			new Object[] {44, directory + "/中国工商银行.jpg", "0.6", "1", new String[] {"中国工商银行","中国工商银行"}},
			new Object[] {45, directory + "/宗教信仰果.jpg", "0.6", "1", new String[] {"苹果"}},
		};
	}
	/**
	 * 商品LOGO检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
	@Test(dataProvider = "detectionLogoDataProvider")
	public void detectionLogoFile(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		ObjectDetectionResult result = request.detectionLogoFile(filePath, thresh);
		assertEquals(result.getCode(), code);
		List<Result> subResult = result.fetchResult();
		assertEquals(subResult.size(), labels.length);
		for(int i=0; i<labels.length; i++)
			assertEquals(subResult.get(i).getLabel(), labels[i]);
	}
	/**
	 * 商品LOGO检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
	@Test(dataProvider = "detectionLogoDataProvider")
	public void detectionLogoPath(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			ObjectDetectionResult result = request.detectionLogoPath(remoteFilePath, thresh);
			assertEquals(result.getCode(), code);
			List<Result> subResult = result.fetchResult();
			assertEquals(subResult.size(), labels.length);
			for(int i=0; i<labels.length; i++)
				assertEquals(subResult.get(i).getLabel(), labels[i]);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	
	/*---------- 电视台标检测 ----------*/
	@DataProvider(name = "detectionTvDataProvider")
	public Object[][] detectionTvDataProvider(Method method){
		String directory = TEST_DATA_DIRECTORY + "/tv";
		return new Object[][] {
			new Object[] {1, directory + "/安徽卫视.jpg", "0.6", "1", new String[] {"安徽卫视"}},
			new Object[] {2, directory + "/澳门卫视.jpg", "0.6", "1", new String[] {"澳门卫视"}},
			new Object[] {3, directory + "/西藏卫视.jpg", "0.6", "1", new String[] {"西藏卫视"}},
			new Object[] {4, directory + "/北京卫视.jpg", "0.6", "1", new String[] {"北京卫视"}},
			new Object[] {5, directory + "/央视.jpg", "0.6", "1", new String[] {"央视"}},
			new Object[] {6, directory + "/重庆卫视.jpg", "0.6", "1", new String[] {"重庆卫视"}},
			new Object[] {7, directory + "/云南卫视.jpg", "0.6", "1", new String[] {"云南卫视"}},
			new Object[] {8, directory + "/东方卫视.jpg", "0.6", "1", new String[] {"东方卫视"}},
			new Object[] {9, directory + "/东南卫视.jpg", "0.6", "1", new String[] {"东南卫视"}},
			new Object[] {10, directory + "/凤凰卫视.jpg", "0.6", "1", new String[] {"凤凰卫视"}},
			new Object[] {11, directory + "/浙江卫视.jpg", "0.6", "1", new String[] {"浙江卫视"}},
			new Object[] {12, directory + "/甘肃卫视.jpg", "0.6", "1", new String[] {"甘肃卫视"}},
			new Object[] {13, directory + "/广东卫视.jpg", "0.6", "1", new String[] {"广东卫视"}},
			new Object[] {14, directory + "/广西卫视.jpg", "0.6", "1", new String[] {"广西卫视"}},
			new Object[] {15, directory + "/贵州卫视.jpg", "0.6", "1", new String[] {"贵州卫视"}},
			new Object[] {16, directory + "/湖南卫视.jpg", "0.6", "1", new String[] {"湖南卫视"}},
			new Object[] {17, directory + "/河北卫视.jpg", "0.6", "1", new String[] {"河北卫视"}},
			new Object[] {18, directory + "/黑龙江卫视.jpg", "0.6", "1", new String[] {"黑龙江卫视"}},
			new Object[] {19, directory + "/河南卫视.jpg", "0.6", "1", new String[] {"河南卫视"}},
			new Object[] {20, directory + "/香港卫视.jpg", "0.6", "1", new String[] {"香港卫视"}},
			new Object[] {21, directory + "/湖北卫视.jpg", "0.6", "1", new String[] {"湖北卫视"}},
			new Object[] {22, directory + "/湖南卫视.jpg", "0.6", "1", new String[] {"湖南卫视"}},
			new Object[] {23, directory + "/江苏卫视.jpg", "0.6", "1", new String[] {"江苏卫视"}},
			new Object[] {24, directory + "/江西卫视.jpg", "0.6", "1", new String[] {"江西卫视"}},
			new Object[] {25, directory + "/吉林卫视.jpg", "0.6", "1", new String[] {"吉林卫视"}},
			new Object[] {26, directory + "/辽宁卫视.jpg", "0.6", "1", new String[] {"辽宁卫视"}},
			new Object[] {27, directory + "/内蒙古卫视.jpg", "0.6", "1", new String[] {"内蒙古卫视"}},
			new Object[] {28, directory + "/宁夏卫视.jpg", "0.6", "1", new String[] {"宁夏卫视"}},
			new Object[] {29, directory + "/青海卫视.jpg", "0.6", "1", new String[] {"青海卫视"}},
			new Object[] {30, directory + "/山东卫视.jpg", "0.6", "1", new String[] {"山东卫视"}},
			new Object[] {31, directory + "/陕西卫视.jpg", "0.6", "1", new String[] {"陕西卫视"}},
			new Object[] {32, directory + "/山西卫视.jpg", "0.6", "1", new String[] {"山西卫视"}},
			new Object[] {33, directory + "/四川卫视.jpg", "0.6", "1", new String[] {"四川卫视"}},
			new Object[] {34, directory + "/天津卫视.jpg", "0.6", "1", new String[] {"天津卫视"}},
			new Object[] {35, directory + "/八一电视.jpg", "0.6", "1", new String[] {"八一电视"}},
			new Object[] {36, directory + "/新疆卫视.jpg", "0.6", "1", new String[] {"新疆卫视"}},
			new Object[] {37, directory + "/BBC.jpg", "0.6", "1", new String[] {"BBC"}},
			new Object[] {38, directory + "/CGTN.jpg", "0.6", "1", new String[] {"CGTN"}},
			new Object[] {39, directory + "/CNN.jpg", "0.6", "1", new String[] {"CNN"}},
			new Object[] {40, directory + "/FOX_NEWS.jpg", "0.6", "1", new String[] {"FOX NEWS"}},
			new Object[] {41, directory + "/TVBS.jpg", "0.6", "1", new String[] {"TVBS"}},
		};
	}
	/**
	 * 电视台标检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
	@Test(dataProvider = "detectionTvDataProvider")
	public void detectionTvFile(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		ObjectDetectionResult result = request.detectionTvFile(filePath, thresh);
		assertEquals(result.getCode(), code);
		List<Result> subResult = result.fetchResult();
		assertEquals(subResult.size(), labels.length);
		for(int i=0; i<labels.length; i++)
			assertEquals(subResult.get(i).getLabel(), labels[i]);
	}
	/**
	 * 电视台标检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
	@Test(dataProvider = "detectionTvDataProvider")
	public void detectionTvPath(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s.%s, caseId=%d", className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			ObjectDetectionResult result = request.detectionTvPath(remoteFilePath, thresh);
			assertEquals(result.getCode(), code);
			List<Result> subResult = result.fetchResult();
			assertEquals(subResult.size(), labels.length);
			for(int i=0; i<labels.length; i++)
				assertEquals(subResult.get(i).getLabel(), labels[i]);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
	
	/*---------- coco目标检测 ----------*/
	@DataProvider(name = "detectionCocoDataProvider")
	public Object[][] detectionCocoDataProvider(Method method){
		String directory = TEST_DATA_DIRECTORY + "/coco";
		return new Object[][] {
			new Object[] {1, directory + "/asuna.jpg", "0.6", "1", new String[] {"人"}},
			/* 都给识别成摩托了 */
			new Object[] {2, directory + "/自行车和人.jpg", "0.25", "1", new String[] {"人", "自行车"}},
			/* 识别成红绿灯了 */
			new Object[] {3, directory + "/汽车.jpg", "0.3", "1", new String[] {"汽车"}},
			new Object[] {4, directory + "/摩托车.jpg", "0.3", "1", new String[] {"摩托车"}},
			new Object[] {5, directory + "/飞机.jpg", "0.6", "1", new String[] {"飞机"}},
			new Object[] {6, directory + "/公交车.jpg", "0.6", "1", new String[] {"公交车"}},
			new Object[] {7, directory + "/火车.jpg", "0.6", "1", new String[] {"火车"}},
			new Object[] {8, directory + "/卡车.jpg", "0.6", "1", new String[] {"卡车"}},
			new Object[] {9, directory + "/船.jpg", "0.6", "1", new String[] {"船"}},
			new Object[] {10, directory + "/红绿灯.jpg", "0.6", "1", new String[] {"红绿灯"}},
			new Object[] {11, directory + "/消防栓.jpg", "0.6", "1", new String[] {"消防栓"}},
			new Object[] {12, directory + "/停止标志.jpg", "0.6", "1", new String[] {"停止标志"}},
			new Object[] {13, directory + "/停车收费表.jpg", "0.6", "1", new String[] {"停车收费表"}},
			new Object[] {14, directory + "/长凳.jpg", "0.6", "1", new String[] {"长凳"}},
			new Object[] {15, directory + "/鸟.jpg", "0.6", "1", new String[] {"鸟", "鸟"}},
			new Object[] {16, directory + "/猫.jpg", "0.6", "1", new String[] {"猫"}},
			/* 躺在床上的狗, 只识别出来了床 */
			new Object[] {17, directory + "/狗.jpg", "0.6", "1", new String[] {"狗"}},
			new Object[] {18, directory + "/骑士和马.jpg", "0.6", "1", new String[] {"马"}},
			new Object[] {19, directory + "/山羊.jpg", "0.6", "1", new String[] {"羊", "羊"}},
			new Object[] {20, directory + "/牛.jpg", "0.6", "1", new String[] {"牛"}},
			new Object[] {21, directory + "/象.jpg", "0.6", "1", new String[] {"象"}},
			new Object[] {22, directory + "/北极熊.jpg", "0.6", "1", new String[] {"熊"}},
			new Object[] {23, directory + "/斑马.jpg", "0.7", "1", new String[] {"斑马"}},
			new Object[] {24, directory + "/长颈鹿.jpg", "0.7", "1", new String[] {"长颈鹿"}},
			/* 25 背包, 没找到 */
			new Object[] {26, directory + "/雨伞.jpg", "0.7", "1", new String[] {"雨伞","雨伞","雨伞","雨伞","雨伞","雨伞"}},
			/* BUG?: 这究竟是手提包还是手提箱? */
			new Object[] {27, directory + "/手提包.jpg", "0.3", "1", new String[] {"手提包"}},
			new Object[] {28, directory + "/领带.jpg", "0.7", "1", new String[] {"领带"}},
			new Object[] {29, directory + "/手提箱.jpg", "0.7", "1", new String[] {"手提箱"}},
			/* 30 飞盘, 没找到 */
			new Object[] {31, directory + "/滑雪板和人.jpg", "0.3", "1", new String[] {"滑雪板"}},
			new Object[] {32, directory + "/单板滑雪.jpg", "0.7", "1", new String[] {"单板滑雪"}},
			/* 33 运动球, 没找到 */
			new Object[] {34, directory + "/放风筝.jpg", "0.25", "1", new String[] {
					"风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝",
					"风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝","风筝"}},
			/* BUG?: 没有识别出棒球棒 */
			new Object[] {35, directory + "/棒球.jpg", "0.3", "1", new String[] {"棒球棒", "棒球手套"}},
			new Object[] {36, directory + "/棒球.jpg", "0.3", "1", new String[] {"棒球棒", "棒球手套"}},
			new Object[] {37, directory + "/滑板.jpg", "0.7", "1", new String[] {"滑板"}},
			new Object[] {38, directory + "/冲浪板.jpg", "0.7", "1", new String[] {"冲浪板"}},
			new Object[] {39, directory + "/人和网球和拍子.jpg", "0.7", "1", new String[] {"网球拍"}},
			/* 40 瓶子, 没找到 */
			new Object[] {41, directory + "/红酒杯.jpg", "0.7", "1", new String[] {"红酒杯","红酒杯","红酒杯"}},
			/* BUG: 杯子和刀都没识别出来, 橘色的盘子被当成了橙子 */
			new Object[] {42, directory + "/杯子和刀.jpg", "0.25", "1", new String[] {"杯子", "刀"}},
			/* BUG?: 我要叉子, 蛋糕只是背景啊 */
			new Object[] {43, directory + "/叉子和蛋糕.jpg", "0.25", "1", new String[] {"叉子"}},
			new Object[] {44, directory + "/杯子和刀.jpg", "0.25", "1", new String[] {"杯子", "刀"}},
			/* BUG?: 别光识别出碗来啊, 勺子才是要紧的 */
			new Object[] {45, directory + "/勺和盘子和咖喱.jpg", "0.25", "1", new String[] {"勺"}},
			/* BUG?: 只识别出来餐桌 */
			new Object[] {46, directory + "/一碗牛肉.jpg", "0.25", "1", new String[] {"碗"}},
			new Object[] {47, directory + "/香蕉.jpg", "0.5", "1", new String[] {"香蕉"}},
			/* BUG?: 识别出来是橙子 */
			new Object[] {48, directory + "/橘子和苹果.jpg", "0.25", "1", new String[] {"苹果"}},
			new Object[] {49, directory + "/三明治.jpg", "0.5", "1", new String[] {"三明治"}},
			new Object[] {50, directory + "/橙子.jpg", "0.5", "1", new String[] {"橙子","橙子","橙子","橙子","橙子"}},
			/* BUG?: 西兰花没识别出来 */
			new Object[] {51, directory + "/西兰花.jpg", "0.25", "1", new String[] {"西兰花"}},
			new Object[] {52, directory + "/带着胡萝卜的盒饭.jpg", "0.5", "1", new String[] {"胡萝卜"}},
			/* BUG?: 有趣, 但凡食物放到餐盘, 餐盘又放到餐桌上的, 统统识别为餐桌 */
			new Object[] {53, directory + "/热狗.jpg", "0.25", "1", new String[] {"热狗"}},
			new Object[] {54, directory + "/比萨.jpg", "0.5", "1", new String[] {"比萨"}},
			new Object[] {55, directory + "/甜甜圈.jpg", "0.5", "1", 
					new String[] {"甜甜圈", "甜甜圈", "甜甜圈", "甜甜圈", "甜甜圈", "甜甜圈"}},
			new Object[] {56, directory + "/蛋糕.jpg", "0.5", "1", new String[] {"蛋糕","蛋糕","蛋糕"}},
			/* 57 椅子, 没找到 */
			/* 58 长椅, 没找到 */
			/* BUG? 只看到了花瓶, 没看到盆栽 */
			new Object[] {59, directory + "/花瓶和盆栽.jpg", "0.25", "1", new String[] {"花瓶", "盆栽"}},
			/* BUG? 时钟这种不显眼的玩意儿识别倒是清楚, 这么大一张床就给忽略了 */
			new Object[] {60, directory + "/床.jpg", "0.25", "1", new String[] {"床"}},
			/* BUG? 餐桌呢? 烤箱呢? 都识别不出来了? */
			new Object[] {61, directory + "/餐桌和香蕉.jpg", "0.25", "1", new String[] {"餐桌", "香蕉"}},
			new Object[] {62, directory + "/马桶.jpg", "0.5", "1", new String[] {"马桶"}},
			/* 63 电视, 没找到 */
			/* BUG?: 我要笔记本电脑, 不仅仅是鼠标, 而且这么怪异的鼠标都识别出来了, case65为啥又识别不出了??? */
			new Object[] {64, directory + "/笔记本电脑.jpg", "0.25", "1", new String[] {"笔记本电脑"}},
			/* BUG?: 键盘是找到了, 鼠标呢 */
			new Object[] {65, directory + "/键盘和鼠标.jpg", "0.25", "1", new String[] {"键盘", "鼠标"}},
			/* BUG?: 我真没注意到原来后边还有一排书…… */
			new Object[] {66, directory + "/猫和遥控器.jpg", "0.25", "1", new String[] {"遥控器"}},
			new Object[] {67, directory + "/键盘和鼠标.jpg", "0.5", "1", new String[] {"键盘", "鼠标"}},
			new Object[] {68, directory + "/手机.jpg", "0.5", "1", new String[] {"手机","手机","手机"}},
			new Object[] {69, directory + "/微波炉.jpg", "0.5", "1", new String[] {"微波炉"}},
			new Object[] {70, directory + "/烤箱.jpg", "0.5", "1", new String[] {"烤箱"}},
			/* 71 烤面包机, 没找到 */
			new Object[] {72, directory + "/洗碗槽.jpg", "0.5", "1", new String[] {"洗碗槽"}},
			new Object[] {73, directory + "/冰箱.jpg", "0.5", "1", new String[] {"冰箱"}},
			new Object[] {74, directory + "/书.jpg", "0.5", "1", 
					new String[] {"书","书","书","书","书","书","书","书","书","书","书"}},
			new Object[] {75, directory + "/时钟.jpg", "0.5", "1", new String[] {"时钟"}},
			new Object[] {76, directory + "/花瓶和盆栽.jpg", "0.5", "1", new String[] {"花瓶", "盆栽"}},
			new Object[] {77, directory + "/剪刀.jpg", "0.5", "1", new String[] {"剪刀"}},
			new Object[] {78, directory + "/泰迪.jpg", "0.5", "1", new String[] {"泰迪熊","泰迪熊","泰迪熊"}},
			/* 79 吹风机, 没找到 */
			new Object[] {80, directory + "/牙刷.jpg", "0.5", "1", new String[] {"牙刷","牙刷"}},
		};
	}
	/**
	 * COCO目标检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
//	@Test(dataProvider = "detectionCocoDataProvider")
	public void detectionCocoFile(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		ObjectDetectionResult result = request.detectionCocoFile(filePath, thresh);
		assertEquals(result.getCode(), code);
		List<Result> subResult = result.fetchResult();
		assertEquals(subResult.size(), labels.length);
		for(int i=0; i<labels.length; i++)
			assertEquals(subResult.get(i).getLabel(), labels[i]);
	}
	/**
	 * COCO目标检查
	 * @param caseId
	 * @param filePath
	 * @param thresh
	 * @param code
	 * @param labels
	 */
//	@Test(dataProvider = "detectionCocoDataProvider")
	public void detectionCocoPath(int caseId, String filePath, String thresh, String code, String[] labels) {
		LOGGER.info(String.format("%s%s%s.%s, caseId=%d", 
				"================", System.lineSeparator(), className, Other.getMethodName(), caseId));
		File file = new File(filePath);
		String remoteFilePath = remoteDirectory + "/" + file.getName();
		try {
			fakeShell.connect();
			fakeShell.upload(remoteDirectory, filePath);
			ObjectDetectionResult result = request.detectionCocoPath(remoteFilePath, thresh);
			assertEquals(result.getCode(), code);
			List<Result> subResult = result.fetchResult();
			assertEquals(subResult.size(), labels.length);
			for(int i=0; i<labels.length; i++)
				assertEquals(subResult.get(i).getLabel(), labels[i]);
		} catch (JSchException | SftpException | JsonSyntaxException | IOException e) {
			String failureLog = Other.stackTraceToString(e);
			LOGGER.error(failureLog);
			fail(failureLog);
		} finally {
			try {
				fakeShell.executeCommand("rm -f " + remoteFilePath, "utf-8");
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fakeShell.disconnect();
		}
	}
}
