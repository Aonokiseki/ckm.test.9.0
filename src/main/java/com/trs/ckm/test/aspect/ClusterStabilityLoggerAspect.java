package com.trs.ckm.test.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/** 聚类稳定性代码的切面日志 */
@Aspect
@Component
public class ClusterStabilityLoggerAspect {
	private final static Logger LOGGER = LogManager.getLogger(LogManager.getLogger());
	
	public ClusterStabilityLoggerAspect() {}
	/**
	 * 在方法执行后拦截, 打印运行结果 
	 * @param point
	 * @param result
	 */
	@AfterReturning(value = PointCut.ALL, returning = "result")
	public void afterMethod(JoinPoint point, long result) {
		String method = point.getSignature().getName();
		Object[] args = point.getArgs();
		String logSentence = MethodAspect.buildLogSentences(method, args);
		LOGGER.info(String.format("%s=%d", logSentence, result));
	}
	
	private static class PointCut{
		private final static String ALL = "execution(* com.trs.ckm.test.cluster.ResultSet.add*(..))";
	}
}
