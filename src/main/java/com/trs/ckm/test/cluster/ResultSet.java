package com.trs.ckm.test.cluster;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResultSet {
	private AtomicLong success;
	private AtomicLong failure;
	private ConcurrentLinkedQueue<String> failureInfoList;
	public ResultSet() {
		success = new AtomicLong(0);
		failure = new AtomicLong(0);
		failureInfoList = new ConcurrentLinkedQueue<String>();
	}
	@Override
	public String toString() {
		return "[success=" + success + ", failure=" + failure + "]";
	}
	public AtomicLong getSuccess() {
		return success;
	}
	public AtomicLong getFailure() {
		return failure;
	}
	public long addSuccessAndGet() {
		return success.addAndGet(1);
	}
	public long addFailureAndGet() {
		return failure.addAndGet(1);
	}
	public ConcurrentLinkedQueue<String> getFailureInfoList() {
		return failureInfoList;
	}
	public void appendFailureInfoList(String message) {
		this.failureInfoList.add(message);
	}
}
