package com.trs.ckm.test.stability;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.trs.ckm.util.FileOperator;
import com.trs.ckm.util.Other;

public class Timer extends Thread {
	private final static Logger LOGGER = LogManager.getLogger(LogManager.getLogger());
	private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	private long block;
	private long total;
	private long surplus;
	private long stopErrorNum;
	private ResultStatistic statistic;
	private LocalDateTime start;
	private Configuration configuration;
	
	public Timer(ResultStatistic statistic, Configuration configuration) {
		super();
		this.statistic = statistic;
		this.configuration = configuration;
		this.block = Long.valueOf(configuration.getBlock());
		this.total = Long.valueOf(configuration.getTotalTime());
		this.stopErrorNum = Long.valueOf(configuration.getStopErrorNum());
		this.surplus = total;
	}
	
	@Override
	public void run() {
		start = LocalDateTime.now();
		while(true) {
			/* 仅当给出了限定时间并且剩余时间小于0 时才退出 
			 * 反之, 如果限定时间为0或更小, 或剩余时间大于0, 应该继续运行*/
			if(total > 0 && surplus <= 0)
				break;
			try {
				Thread.sleep(block * 1000);
			}catch(InterruptedException e) {
				LOGGER.warn(Other.stackTraceToString(e));
			}
			try {
				/* 写结果统计文件, 每次覆盖旧文件 */
				FileOperator.write(configuration.getOutput(), record());
				/* 检查 emptyInfo, 获得一个队列, 队列的意义是记录调用接口结果为空的任务，
				 * 这个列队的每个元素记录调用的接口和数据文件的绝对路径, 方便稳定性测试后手工排查
				 * 这里不用考虑线程安全性, Timer线程只有一个, poll()方法只会被这一个Timer线程调用 */
				handleMessages(statistic.getEmptyInfo(), configuration.getEmpty());
				/* 检查 exceptionInfo, 获得一个队列, 队列的意义是记录调用接口抛出异常的任务
				 * 这个队列的每个元素记录调用的接口和数据文件的绝对路径，方便终止进程后手工排查 */
				handleMessages(statistic.getExceptionInfo(), configuration.getException());
			} catch (IOException e) {
				LOGGER.error(Other.stackTraceToString(e));
			}
			if(surplus > Long.MIN_VALUE + block)
				surplus -= block;
			/* 错误数量超过限制, 退出 */
			if(tooManyErrors()){
				LOGGER.error("Too many errors, stoping test!");
				break;
			}
		}
		super.interrupt();
	}
	
	private static void handleMessages(ConcurrentLinkedQueue<String> queue, String writedFilePath) throws IOException {
		if(queue.size() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		for(int i=0, size=queue.size(); i<size; i++)
			sb.append(queue.poll()).append(System.lineSeparator());
		Map<String,String> options = new HashMap<String,String>();
		options.put("is.append.contents", "true");
		FileOperator.write(writedFilePath, sb.toString(), "UTF-8", options);
		LOGGER.debug(String.format("FileOperator.write(%s, sb.toString()[length=%d], UTF-8, options);", 
				writedFilePath, sb.length()));
	}
	
	private String record() {
		Map<String,Long> success = statistic.getSuccess();
		Map<String,Long> failure = statistic.getFailure();
		Map<String,Long> empty = statistic.getEmptyResult();
		StringBuilder stringBuilder = new StringBuilder();
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(start, now);
		
		stringBuilder.append("Start== ").append(start.format(FORMATTER)).append(System.lineSeparator())
					 .append("Now== ").append(now.format(FORMATTER)).append(System.lineSeparator())
					 .append("CostTime== ").append(duration.toMillis()/1000).append("seconds == ")
					 .append(duration.toHours()).append(" hours.").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("Success").append(System.lineSeparator());
		
		for(Entry<String, Long> e : success.entrySet())
			stringBuilder.append(e.getKey()).append("==").append(e.getValue()).append(System.lineSeparator());
		stringBuilder.append(System.lineSeparator()).append("Failure").append(System.lineSeparator());
		for(Entry<String, Long> e : failure.entrySet())
			stringBuilder.append(e.getKey()).append("==").append(e.getValue()).append(System.lineSeparator());
		stringBuilder.append(System.lineSeparator()).append("Empty").append(System.lineSeparator());
		for(Entry<String, Long> e : empty.entrySet())
			stringBuilder.append(e.getKey()).append("==").append(e.getValue()).append(System.lineSeparator());
		String result = stringBuilder.toString();
		LOGGER.debug("record(), result.length()=="+result.length());
		return result;
	}
	/**
	 * 判断错误是否过多
	 * @return
	 */
	private boolean tooManyErrors() {
		long totalFailure = calculateMapTotalNumber(statistic.getFailure());
		return totalFailure > stopErrorNum;
	}
	
	private static long calculateMapTotalNumber(Map<String, Long> map) {
		long total = 0;
		Object[] nums = map.values().toArray();
		for(int i=0; i<nums.length; i++)
			total += Long.parseLong(String.valueOf(nums[i]));
		return total;
	}
}
