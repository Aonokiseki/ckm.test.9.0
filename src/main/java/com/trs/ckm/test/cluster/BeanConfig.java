package com.trs.ckm.test.cluster;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.MapOperator;

@Configuration
@ComponentScan(basePackageClasses= {
	com.trs.ckm.test.aspect.AspectConfig.class,
	com.trs.ckm.test.aspect.ClusterStabilityLoggerAspect.class,
	com.trs.ckm.test.cluster.Configuration.class,
	com.trs.ckm.test.cluster.ResultSet.class
})
// 直接读配置文件./config/clusterTest.ini 意味着遍历文件要放到主方法里完成, 不好
// @PropertySource("file:./config/clusterTest.ini")
public class BeanConfig {
	@Bean
	public com.trs.ckm.test.cluster.Configuration configuration() throws IOException{
		Map<String,String> map = FileOperator.readConfiguration("./config/clusterTest.ini", "UTF-8");
		String input = MapOperator.safetyGet(map, "input", "./testdata/input/clu");
		String output = MapOperator.safetyGet(map, "output", "./testdata/output/clu");
		Long totalTime = Long.valueOf(MapOperator.safetyGet(map, "totalTime", "600"));
		Long block = Long.valueOf(MapOperator.safetyGet(map, "block", "30"));
		String encoding = MapOperator.safetyGet(map, "encoding", "UTF-8");
		String maxClusterNum = MapOperator.safetyGet(map, "maxClusterNum", "200");
		String method = MapOperator.safetyGet(map, "method", "person");
		String minClusterNum = MapOperator.safetyGet(map, "minClusterNum", "100");
		String minNumOfClu = MapOperator.safetyGet(map, "minNumOfClu", "2");
		Integer threadNumber = Integer.valueOf(MapOperator.safetyGet(map, "threadNumber", "1"));
		Long stopErrorNum = Long.valueOf(MapOperator.safetyGet(map, "stopErrorNum", String.valueOf(Long.MAX_VALUE)));
		
		com.trs.ckm.test.cluster.Configuration configuration = new com.trs.ckm.test.cluster.Configuration();
		configuration.setInput(input);
		configuration.setOutput(output);
		configuration.setTotalTime(totalTime);
		configuration.setBlock(block);
		configuration.setEncoding(encoding);
		configuration.setMaxClusterNum(maxClusterNum);
		configuration.setMethod(method);
		configuration.setMinClusterNum(minClusterNum);
		configuration.setMinNumOfClu(minNumOfClu);
		configuration.setThreadNumber(threadNumber);
		configuration.setStopErrorNum(stopErrorNum);
		configuration.setZips(FileOperator.traversal(input, ".zip", false));
		return configuration;
	}
}
