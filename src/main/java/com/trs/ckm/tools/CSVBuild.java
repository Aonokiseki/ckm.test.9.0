package com.trs.ckm.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.trs.ckm.util.FileOperator;

public class CSVBuild {
	
	private final static String EMPTY_STRING = "";
	private final static Map<String,String> EMPTY_MAPPLE = new HashMap<String,String>();
	private final static String DEFAULT_ENCODING = "UTF-8";
	private final static String DEFAULT_COLUMN_NAME = "text";
	
	/**
	 * 将目录中的(一)多个txt文件整合成一个csv文件, csv文件只有一列
	 * @param directory 源文本所在目录
	 * @param readEncoding 读文件时的编码,默认UTF-8
	 * @param output 输出文件名, 必须是单个文件
	 * @param outputEncoding 输出编码
	 * @param title 列名,默认值text
	 * @param symbolMapple Map&ltkey, value&gt 符号映射规则, 有时你需要对文本中的部分字符做替换等处理,
	 * 如英文半角引号替换为空串。将文本中的${key}替换为${value}
	 * @throws IOException
	 */
	public static void execute(String directory, String readEncoding, String output, 
			String outputEncoding, String title, Map<String,String> symbolMapple) throws IOException {
		if(symbolMapple == null) 
			symbolMapple = EMPTY_MAPPLE;
		if(readEncoding == null || EMPTY_STRING.equals(readEncoding)) 
			readEncoding = DEFAULT_ENCODING;
		if(outputEncoding == null || EMPTY_STRING.equals(outputEncoding)) 
			outputEncoding = DEFAULT_ENCODING;
		if(title == null || "".equals(title))
			title = DEFAULT_COLUMN_NAME;
		innerProcess(directory, readEncoding, output, outputEncoding, title, symbolMapple);
	}
	private static void innerProcess(String directory, String readEncoding, String output,
			String outputEncoding, String title, Map<String,String> symbolMapple) throws IOException {
		List<File> files = FileOperator.traversal(directory, ".txt", false);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), outputEncoding));
		String text;
		writer.append(title).append(System.lineSeparator());
		Set<Entry<String,String>> symbolMappleEntrySet = symbolMapple.entrySet();
		for(int i=0, size=files.size(); i<size; i++) {
			text = FileOperator.read(files.get(i).getAbsolutePath(), readEncoding, null);
			/* 换行符一律替换为空串 */
			text = text.replaceAll(System.lineSeparator(), EMPTY_STRING);
			/* 按照映射规则, 替换符号 */
			for(Entry<String,String> entry : symbolMappleEntrySet)
				text = text.replaceAll(entry.getKey(), entry.getValue());
			/* 擦除空格后, 检查是否为空 */
			if(text.trim().isEmpty())
				continue;
			/* 写文件 */
			writer.append(text).append(System.lineSeparator());
			writer.flush();
		}
		writer.close();
	}
}
