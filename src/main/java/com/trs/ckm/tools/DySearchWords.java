package com.trs.ckm.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import com.trs.ckm.api.master.TRSCkmRequest;
import com.trs.ckm.api.pojo.DySearchResult;

public class DySearchWords {
	
	private final static String DEFAULT_HOST = "http://127.0.0.1:8000";
	private final static String DEFAULT_START_WORD = "中国";
	private final static String DEFAULT_ENCODING = "UTF-8";
	private final static String OPTIONS = "0";
	private final static String SUCCESS_CODE = "1";
	/**
	 * 为 /rs/dy/search 接口准备检索词<br> 
	 * @param host 主机, 如 <code>http://192.168.105.216:8000</code>, 
	 * 空或空串采用默认值 <code>http://127.0.0.1:8000</code>
	 * @param startWord 提供一个有返回结果的短语,作为查找起点, 例如"中国"
	 * @param model 接口的模板名, 在此模板中寻找检索词
	 * @param limit 检索词最大个数, 若输入0或负数, 视为1
	 * @param output 输出文件路径
	 * @param encoding 文件编码
	 * @throws IOException
	 */
	public static void prepared(String host, 
			String startWord, String model, long limit, String output, String encoding) throws IOException {
		if(host == null || "".equals(host)) 
			host = DEFAULT_HOST;
		if(model == null) 
			model = "";
		if(limit <= 0) 
			limit = 1;
		if(startWord == null || "".equals(startWord)) 
			startWord = DEFAULT_START_WORD;
		if(encoding == null || "".equals(encoding))
			encoding = DEFAULT_ENCODING;
		innerPrepared(host, startWord, model, limit, output, encoding);
	}
	
	private static void innerPrepared(String host, 
			String startWord, String model, long limit, String output, String encoding) throws IOException {
		TRSCkmRequest request = new TRSCkmRequest(host, 60000, 60000);
		LinkedList<String> open = new LinkedList<String>();
		open.add(startWord);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), encoding));
		String word; DySearchResult dySearchResult; long count = 0;
		while(count <= limit && !open.isEmpty()) {
			word = open.poll();
			/* 这里不做排重, 排重见DuplicateCleaner类 */
			dySearchResult = request.dySearch(word, model, OPTIONS);
			if(!SUCCESS_CODE.equals(dySearchResult.getCode()))
				continue;
			writer.append(word).append(System.lineSeparator());
			writer.flush();
			count++;
			open.addAll(dySearchResult.getDyResult());
		}
		writer.close();
	}
}
