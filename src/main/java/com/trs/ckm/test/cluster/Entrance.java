package com.trs.ckm.test.cluster;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.test.function.Constant;

public class Entrance {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
		TRSCkmRequest request = (TRSCkmRequest) context.getBean(com.trs.ckm.api.master.TRSCkmRequest.class);
		com.trs.ckm.test.cluster.Configuration configuration = 
				(Configuration) context.getBean(com.trs.ckm.test.cluster.Configuration.class);
		ResultSet resultSet = (ResultSet)context.getBean(com.trs.ckm.test.cluster.ResultSet.class);
		context.close();
		Constant.reconfigureLog4j2("./config/log4j2_cluster.xml");
		Thread timer = new Timer(configuration, resultSet);
		timer.start();
		int coreSize = configuration.getThreadNumber();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize, 
				0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(coreSize));
		while(!timer.isInterrupted()) {
			if(threadPoolExecutor.getActiveCount() >= coreSize || threadPoolExecutor.getQueue().size() > 0)
				continue;
			threadPoolExecutor.submit(new ClusterTask(request, configuration, resultSet));
		}
		if(!threadPoolExecutor.isShutdown())
			threadPoolExecutor.shutdown();
	}
}
