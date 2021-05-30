package com.trs.ckm.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Other {
	private Other(){}
	
	 /**
     * 获得当前正在执行的方法名称
     */
    public static String getMethodName(){
    	StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
	    StackTraceElement e = stacktrace[2];  
	    String methodName = e.getMethodName();  
	    return methodName;
    } 
    /**
     * 返回堆栈字符串
     * 
     * @param throwable
     * @return String 堆栈信息
     */
    public static String stackTraceToString(Throwable throwable){
    	StringWriter sw = new StringWriter();
 	    throwable.printStackTrace(new PrintWriter(sw, true));
 	    return sw.getBuffer().toString();
    } 
    /**
     * 获取当前正在运行的线程列表
     * 
     * @return Thread[]
     */
    public static Thread[] getListThreads(){
		Thread[] lstThreads;
		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		int noThreads = currentGroup.activeCount();
	    lstThreads = new Thread[noThreads];
	    currentGroup.enumerate(lstThreads);
	    return lstThreads;
    }
}
