package com.trs.ckm.tools;

import com.trs.ckm.util.FileOperator;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * 数据文件的文本排重<br>
 * <br>
 * 假设文件words.txt有众多行,如下:<br>
 * <br>
 * <table>
 *     <th>检索词</th>
 *     <tr><td>1.文化</td></tr>
 *     <tr><td>2.军事</td></tr>
 *     <tr><td>3.经济</td></tr>
 *     <tr><td>4.政治</td></tr>
 *     <tr><td>5.军事</td></tr>
 *     <tr><td>...</td></tr>
 * </table>
 * 
 * 直接做内排重可能会触发内存溢出;即使不触发内存溢出,这种全局排重也会拖累性能。
 * 这里的做法是取每一行的字符串的hashCode, 对相同质数取模, 模不同的字符串散落到不同文件,
 * hashCode相同(文本不一定一样)的字符串放到相同文件中，每个文件局部内排重,然后全局归并。<br>
 * <br>
 * 因此,需要的参数有输入文件路径input, 输出路径output, 文件编码encoding(默认UTF-8)<br>
 * 除此之外还有一个参数divideNumber,这个参数决定了要散落到多少份(临时)文件中, 默认值为7<br>
 * <br>
 * 示例:<br>
 * <code>String input = "D:/words.txt";</code><br/>
 * <code>String output = "D:/output.txt";</code><br/>
 * <code>String encoding = "UTF-8";</code><br/>
 * <code>int divideNumber = 7;</code><br/>
 * <code>DuplicateCleaner cleaner = new DuplicateCleaner(intput, output, encoding, divideNumber);</code><br/>
 * <code>cleanner.execute();</code><br/>
 *
 */
public class DuplicateCleaner {
    private final static int DEFAULT_DIVIDE = 7;
    private final static String DEFAULT_ENCODING = "UTF-8";
    private final static String DEFAULT_TEMP_FILE_BEFORE_CLEAN = "./testdata/temp/duplicateClean/before";
    private final static String DEFAULT_TEMP_FILE_AFTER_CLEAN = "./testdata/temp/duplicateClean/after";
    private List<BufferedWriter> writers;
    private String input;
    private String output;
    private String encoding;
    private int divideNumber;
    
    public DuplicateCleaner(String input, String output) {
    	 this.input = input;
         this.output = output;
         this.encoding = DEFAULT_ENCODING;
         this.divideNumber = DEFAULT_DIVIDE;
    }
    public DuplicateCleaner(String input, String output, String encoding) {
    	 this(input, output);
         this.encoding = encoding;
    }
    public DuplicateCleaner(String input, String output, String encoding, int divideNumber){
    	this(input, output, encoding);
        this.divideNumber = divideNumber;
    }
    
    public void execute() throws IOException {
        initializeWriters();
        divideToDifferentFiles();
        redirectWriters();
        checkAllFilesDuplication();
        buildFinalFile();
        clearTempFiles();
    }

    private void initializeWriters() throws IOException{
        this.writers = new ArrayList<BufferedWriter>(this.divideNumber);
        for(int i=0; i<divideNumber; i++)
            writers.add(i, new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(DEFAULT_TEMP_FILE_BEFORE_CLEAN + "/" + i + ".txt"), encoding)));
    }

    private void divideToDifferentFiles() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), encoding));
        String current = null;
        int hashCode = -1; int mod = 0;BufferedWriter writer = null;
        while((current = reader.readLine()) != null){
            if(current.isEmpty())
                continue;
            hashCode = current.hashCode();
            /* 注意, floorMod 才是取模, 百分号是取余 */
            mod = Math.floorMod(hashCode, divideNumber);
            writer = writers.get(mod);
            writer.append(current).append(System.lineSeparator());
            writer.flush();
        }
        reader.close();
    }

    private void redirectWriters() throws IOException {
        for(int i=0; i<writers.size(); i++)
        	writers.get(i).close();
        this.writers.clear();
        for(int i=0; i<divideNumber; i++) 
        	 writers.add(new BufferedWriter(
                     new OutputStreamWriter(
                             new FileOutputStream(DEFAULT_TEMP_FILE_AFTER_CLEAN + "/" + i + ".txt"), encoding)));      
    }

    private void checkAllFilesDuplication() throws IOException {
       List<File> files = FileOperator.traversal(DEFAULT_TEMP_FILE_BEFORE_CLEAN, ".txt", false);
       for(int i=0, size=Math.min(divideNumber, files.size()); i<size; i++)
           removeDuplication(i, files.get(i));
    }

    private void removeDuplication(int fileId, File file) throws IOException {
    	/* Q:这个地方为什么不用hashSet?
    	 * A:hashSet会根据hashCode做初步排重, 当文本过长时, hashCode有可能溢出导致判断不准确而过滤了不应该过滤的文本 */
        List<String> contents = new LinkedList<String>();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        String line; BufferedWriter writer = null;
        while((line = reader.readLine())!=null){
            if(contents.contains(line))
            	continue;
            contents.add(line);
            writer = writers.get(fileId);
            writer.append(line).append(System.lineSeparator());
            writer.flush();
        }
        contents.clear();
        reader.close();
    }

    private void buildFinalFile() throws IOException {
        List<File> files = FileOperator.traversal(DEFAULT_TEMP_FILE_AFTER_CLEAN, ".txt", false);
        BufferedWriter finalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), encoding));
        BufferedReader finalReader = null;
        String line;
        for(int i=0, size=Math.min(files.size(), divideNumber); i<size; i++){
            finalReader = new BufferedReader(
            		new InputStreamReader(new FileInputStream(files.get(i).getAbsolutePath()), encoding));
            while((line = finalReader.readLine())!=null)
                finalWriter.append(line).append(System.lineSeparator());
            finalWriter.flush();
            /* 这个地方一定要关掉文件流, 不然无法删除临时文件*/
            finalReader.close();
        }
        finalWriter.close();
    }

    private void clearTempFiles() throws IOException {
        for(int i=0; i<writers.size(); i++)
        	writers.get(i).close();
        this.writers.clear();
        List<File> files = FileOperator.traversal(DEFAULT_TEMP_FILE_BEFORE_CLEAN, null, false);
        files.addAll(FileOperator.traversal(DEFAULT_TEMP_FILE_AFTER_CLEAN, null, false));
        for(int i=0,size=files.size(); i<size; i++)
            files.get(i).delete();
    }
}