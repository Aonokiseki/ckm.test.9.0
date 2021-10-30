package com.trs.dlvrs.test.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.trs.ckm.util.Other;
import com.trs.dlvrs.api.pojo.Returnable;

@Aspect
@Component
public class FakeShellAspect {
	
	private final static Logger LOGGER = LogManager.getLogger(LogManager.getLogger());
	/** 当参数长度超过此值时, 裁减掉后边的文字 */
	private final static int PARAMETER_CUT_SIZE = Integer.MAX_VALUE; 
	
	public FakeShellAspect() {}
	/**
	 * 将切面抓到方法名, 参数列表和结果拼装成字符串, 方便写日志
	 * @param method
	 * @param args
	 * @param code
	 * @param message
	 * @return
	 */
	static String buildLogSentences(String method, Object[] args) {
		StringBuilder logSentence = new StringBuilder();
		logSentence.append(method).append("(");
		String subString = null;
		for(int i=0; i<args.length; i++) {
			if(args[i] == null) {
				logSentence.append("null");
				if(i < args.length - 1)
					logSentence.append(", ");
				continue;
			}
			if(args[i].toString().length() > PARAMETER_CUT_SIZE) {
				subString = args[i].toString().substring(0, PARAMETER_CUT_SIZE);
				logSentence.append(subString).append("..., ");
				continue;
			}
			logSentence.append(args[i]);
			if(i < args.length - 1)
				logSentence.append(", ");
		}
		logSentence.append(")");
		return logSentence.toString();
	}
	/**
	 * 拦截方法, 并在执行前打印方法名和参数
	 * @param point
	 */
	@Before(value = PointCut.ALL)
	public void beforeMethod(JoinPoint point) {
		String method = point.getSignature().getName();
		Object[] args = point.getArgs();
		String logSentence = buildLogSentences(method, args);
		LOGGER.info(logSentence);
	}
	/**
	 * 在方法执行后拦截, 打印运行结果 
	 * @param point
	 * @param result
	 */
	@AfterReturning(value = PointCut.ALL, returning = "result")
	public void afterMethod(JoinPoint point, Returnable result) {
		LOGGER.info(String.format("[code=%s, message=%s]", result.getCode(), result.getMessage()));
		LOGGER.debug(result.getDetails());
	}
	/**
	 * 抛出异常时拦截, 打印
	 * @param point
	 * @param e
	 */
	@AfterThrowing(value = PointCut.ALL, throwing = "e")
	public void afterThrowing(JoinPoint point, Throwable e) {
		LOGGER.error(Other.stackTraceToString(e));
	}
	/**
	 * 记录 PointCut
	 */
	static class PointCut{
		final static String ALL = "execution(* com.trs.ckm.util.FakeShell.*(..))";
	}
}
