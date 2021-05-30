package com.trs.ckm.test.cluster;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
	private String input;
	private String output;
	private Long totalTime;
	private String failureOutput;
	private Long block;
	private String encoding;
	private String maxClusterNum;
	private String method;
	private String minClusterNum;
	private String minNumOfClu;
	private Integer threadNumber;
	private Long stopErrorNum;
	private List<File> zips;
	
	public Configuration() {}
	
	public Configuration(
			@Value("${input}") String input, 
			@Value("${output}") String output, 
			@Value("${totalTime}") Long totalTime,
			@Value("${failureOutput}") String failureOutput,
			@Value("${block}") Long block,
			@Value("${encoding}") String encoding, 
			@Value("${maxClusterNum}") String maxClusterNum,
			@Value("${method}") String method,
			@Value("${minClusterNum}") String minClusterNum,
			@Value("${minNumOfClu}") String minNumOfClu,
			@Value("${threadNumber}") Integer threadNumber,
			@Value("${stopErrorNum}") Long stopErrorNum
			) {
		this.input = input;
		this.output = output;
		this.totalTime = totalTime;
		this.failureOutput = failureOutput;
		this.block = block;
		this.encoding = encoding;
		this.maxClusterNum = maxClusterNum;
		this.method = method;
		this.minClusterNum = minClusterNum;
		this.minNumOfClu = minNumOfClu;
		this.threadNumber = threadNumber;
		this.stopErrorNum = stopErrorNum;
	}
	
	@Override
	public String toString() {
		return "Configuration [input=" + input + ", output=" + output + ", totalTime=" + totalTime + ", failureOutput="
				+ failureOutput + ", block=" + block + ", encoding=" + encoding + ", maxClusterNum=" + maxClusterNum
				+ ", method=" + method + ", minClusterNum=" + minClusterNum + ", minNumOfClu=" + minNumOfClu + ", zips="
				+ zips + ", threadNumber=" + threadNumber + ", stopErrorNum=" + stopErrorNum + "]";
	}
	
	public Long getStopErrorNum() {
		return stopErrorNum;
	}
	public void setStopErrorNum(Long stopErrorNum) {
		this.stopErrorNum = stopErrorNum;
	}
	public Integer getThreadNumber() {
		return threadNumber;
	}
	public void setThreadNumber(Integer threadNumber) {
		this.threadNumber = threadNumber;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getMaxClusterNum() {
		return maxClusterNum;
	}
	public void setMaxClusterNum(String maxClusterNum) {
		this.maxClusterNum = maxClusterNum;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMinClusterNum() {
		return minClusterNum;
	}
	public void setMinClusterNum(String minClusterNum) {
		this.minClusterNum = minClusterNum;
	}
	public String getMinNumOfClu() {
		return minNumOfClu;
	}
	public void setMinNumOfClu(String minNumOfClu) {
		this.minNumOfClu = minNumOfClu;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public List<File> getZips() {
		return zips;
	}
	public void setZips(List<File> zips) {
		this.zips = zips;
	}
	public String getFailureOutput() {
		return failureOutput;
	}
	public void setFailureOutput(String failureOutput) {
		this.failureOutput = failureOutput;
	}
	public Long getBlock() {
		return block;
	}
	public void setBlock(Long block) {
		this.block = block;
	}
}
