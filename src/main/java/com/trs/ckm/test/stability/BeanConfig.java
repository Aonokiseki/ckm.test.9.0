package com.trs.ckm.test.stability;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.trs.ckm.test.function.Constant;
import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.MapOperator;

@Configuration
@ComponentScan(basePackageClasses= {
		com.trs.ckm.test.aspect.AspectConfig.class,
		com.trs.ckm.test.aspect.StabilityLoggerAspect.class,
		com.trs.ckm.test.stability.Configuration.class, 
		com.trs.ckm.test.stability.ResultStatistic.class})
public class BeanConfig {
	/**
	 * 由Spring生成一个com.trs.ckm.test.stability.Configuration的bean<br>
	 * 完成由json文件到内存实体之间的转换<br>
	 * 如果转换失败或遇到异常, 由Spring框架负责抛出/处理异常;另外, 遍历文件,提供默认值也在这里完成<br>
	 * 好处是无需在测试代码的主逻辑里考虑配置异常的情况<br>
	 * @return
	 * @throws IOException
	 */
	@Bean
	public com.trs.ckm.test.stability.Configuration configuration() throws IOException {
		Map<String,String> ini = 
				FileOperator.readConfiguration(Constant.STABILITY_TEST_CONFIG_INI, Constant.DEFAULT_ENCODING);
		String module = MapOperator.safetyGet(ini, "module", "standard.json");
		String configurationDescription = 
				FileOperator.read(Constant.STABILITY_TEST_MOUDLE_DIRECTORY + "/" + module);
		Gson gson = new Gson();
		com.trs.ckm.test.stability.Configuration configuration = 
				(com.trs.ckm.test.stability.Configuration) 
				gson.fromJson(configurationDescription, com.trs.ckm.test.stability.Configuration.class);
		if(isEmpty(configuration.getEncode())) 
			configuration.setEncode(Constant.DEFAULT_ENCODING);
		if(isEmpty(configuration.getTotalTime()))
			configuration.setTotalTime("600");
		if(isEmpty(configuration.getBlock()))
			configuration.setBlock("30");
		if(isEmpty(configuration.getOutput()))
			configuration.setOutput("./logs/statistic.txt");
		if(isEmpty(configuration.getEmpty()))
			configuration.setEmpty("./logs/emptyResultInfo.txt");
		if(isEmpty(configuration.getException()))
			configuration.setException("./logs/exceptionInfo.txt");
		if(isEmpty(configuration.getStopErrorNum()))
			configuration.setStopErrorNum(String.valueOf(Long.MAX_VALUE));
		List<File> files = FileOperator.traversal(configuration.getInput(), ".txt", false);
		configuration.setFiles(files);
		List<String> dys = new ArrayList<String>();
		if(isEmpty(configuration.getDysPath()) || !new File(configuration.getDysPath()).exists()) {
			configuration.setDys(dys);
		}else {
			String[] words = FileOperator.read(configuration.getDysPath()).split(System.lineSeparator());
			for(int i=0;i<words.length;i++) 
				dys.add(words[i]);
			configuration.setDys(dys);
		}
		return configuration;
	}
	
	private static boolean isEmpty(String parameter) {
		if(parameter == null || parameter.trim().isEmpty())
			return true;
		return false;
	}
}
