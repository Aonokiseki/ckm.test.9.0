package com.trs.ckm.test.stability;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResultStatistic {
	/* 统计各个接口执行成功的次数 */
	private ConcurrentHashMap<String,Long> success;
	/* 统计各个接口抛出异常的次数 */
	private ConcurrentHashMap<String,Long> failure;
	/* 统计各个接口返回结果为空的次数 */
	private ConcurrentHashMap<String,Long> empty;
	/* 对于返回结果为空的情况, 记录调用的接口名和测试文件的绝对路径 */
	private ConcurrentLinkedQueue<String> emptyInfo;
	/* 对于调用接口抛出异常的情况, 记录调用的接口名和测试文件的绝对路径 */
	private ConcurrentLinkedQueue<String> exceptionInfo;
	
	public ResultStatistic() {
		success = new ConcurrentHashMap<String,Long>();
		failure = new ConcurrentHashMap<String,Long>();
		empty = new ConcurrentHashMap<String,Long>();
		emptyInfo = new ConcurrentLinkedQueue<String>();
		exceptionInfo = new ConcurrentLinkedQueue<String>();
	}
	
	public long addEmptyResultAndGet(String key) {
		if(key == null || "".equals(key))
			return -1;
		if(empty.isEmpty() || !empty.containsKey(key))
			empty.put(key, 1L);
		else
			empty.put(key, empty.get(key) + 1);
		return empty.get(key);
	}
	
	public ConcurrentHashMap<String,Long> getEmptyResult(){
		return empty;
	}
	
	public long addSuccessAndGet(String key) {
		if(key == null || "".equals(key))
			return -1;
		if(success.isEmpty() || !success.containsKey(key))
			success.put(key, 1L);
		else
			success.put(key, success.get(key) + 1);
		return success.get(key);
	}
	
	public ConcurrentHashMap<String,Long> getSuccess() {
		return success;
	}
	
	public long addFailureAndGet(String key) {
		if(key == null || "".equals(key))
			return -1;
		if(failure.isEmpty() || !failure.containsKey(key))
			failure.put(key, 1L);
		else
			failure.put(key, failure.get(key) + 1);
		return failure.get(key);
	}
	
	public ConcurrentHashMap<String,Long> getFailure(){
		return failure;
	}
	
	public ConcurrentLinkedQueue<String> getEmptyInfo(){
		return this.emptyInfo;
	}
	public void appendEmptyInfo(String info) {
		this.emptyInfo.add(info);
	}
	public ConcurrentLinkedQueue<String> getExceptionInfo(){
		return this.exceptionInfo;
	}
	public void appendExceptionInfo(String exceptionInfo) {
		this.exceptionInfo.add(exceptionInfo);
	}
}
