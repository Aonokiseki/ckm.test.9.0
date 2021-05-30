package com.trs.ckm.test.cluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.trs.ckm.util.FileOperator;

public class Timer extends Thread{
	private Configuration configuration;
	private ResultSet resultSet;
	private long totalTime;
	private long surplus;
	private long block;
	
	
	public Timer(Configuration configuration, ResultSet resultSet) {
		super();
		this.configuration = configuration;
		this.resultSet = resultSet;
		this.totalTime = configuration.getTotalTime();
		this.surplus = totalTime;
		this.block = configuration.getBlock();
	}
	
	@Override
	public void run() {
		StringBuilder info = new StringBuilder();
		ConcurrentLinkedQueue<String> exceptions;
		Map<String,String> writedFileOptions = new HashMap<String,String>();
		writedFileOptions.put("is.append.contents", "true");
		while(true) {
			if(totalTime > 0 && surplus <= 0)
				break;
			boolean tooManyErrors = 
					(resultSet.getFailure().longValue() > configuration.getStopErrorNum().longValue());
			if(tooManyErrors)
				break;
			try {
				Thread.sleep(block * 1000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			exceptions = resultSet.getFailureInfoList();
			if(exceptions.size() > 0) {
				for(int i=0, size=exceptions.size(); i<size; i++)
				info.append(exceptions.poll()).append(System.lineSeparator());
				try {
					FileOperator.write(configuration.getFailureOutput(), info.toString(), "UTF-8",  writedFileOptions);
				} catch (IOException e) {
					e.printStackTrace();
				}
				info.delete(0, info.length());
			}
			if(surplus > Long.MIN_VALUE + block)
				surplus -= block;
		}
		super.interrupt();
	}
}
