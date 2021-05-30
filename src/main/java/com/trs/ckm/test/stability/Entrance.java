package com.trs.ckm.test.stability;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.test.function.Constant;

class Entrance {
	
	public static void main(String[] args) {
		/* 初始化Spring上下文 */
    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
    	TRSCkmRequest request = (TRSCkmRequest)context.getBean(com.trs.ckm.api.master.TRSCkmRequest.class);
    	Configuration configuration = (Configuration) context.getBean(com.trs.ckm.test.stability.Configuration.class);
    	ResultStatistic statistic = (ResultStatistic) context.getBean(com.trs.ckm.test.stability.ResultStatistic.class);
    	context.close();
    	/* 重新指定日志的配置 */
    	Constant.reconfigureLog4j2(Constant.STABILITY_LOG4J2_XML_PATH);
		ThreadPoolExecutor threadPoolExecutor = null;
		int coreSize = Integer.valueOf(configuration.getThreadNumber());
		/* 开启计时器 */
		Thread timer = new Timer(statistic, configuration);
		timer.start();
		/* 开启线程池, 提交任务 */
		threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(coreSize));
		while(!timer.isInterrupted()) {
			if(threadPoolExecutor.getActiveCount() >= coreSize || threadPoolExecutor.getQueue().size() > 0)
				continue;
			threadPoolExecutor.submit(Task.build(configuration, request, statistic));
		}
		if(!threadPoolExecutor.isShutdown())
			threadPoolExecutor.shutdown();
	}
}
