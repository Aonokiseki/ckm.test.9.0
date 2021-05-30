package com.trs.ckm.test.stability;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Configuration {
	private List<Method> interfaces;
	private String input;
	private String encode;
	private String threadNumber;
	/* 
	 * 遍历后的文件列表, 遍历的起点是 [input] , 
	 * 这个成员变量在json配置文件不存在同名配置项,
	 * 因此无法被json解析并填充, 值一定是null
	 * 但没有关系, 我们手工填充
	 * 之所以一定要在这里给出这个成员变量, 
	 * 就是希望在Spring返回Configuration这个bean时顺便检查异常的抛出
	 * 好处是不用等到任务提交(即主方法)的时候再去处理这个异常
	 */
	private List<File> files;
	/* 任务持续总时间, 单位秒 */
	private String totalTime;
	/* 相隔多长时间打印一次统计结果, 单位秒 */
	private String block;
	/* 统计文件位置 */
	private String output;
	/* 调用接口结果为空时, 将接口和文件绝对路径记录到一个文件中,
	 * empty 就是这个文件的路径*/
	private String empty;
	/* 调用接口出现异常时, 将接口和文件绝对路径记录到一个文件中,
	 * exception 就是这个文件的路径 */
	private String exception;
	/* 结果为Failure的数量超过此值时, 程序终止  */
	private String stopErrorNum;
	/* 短语列表 */
	private List<String> dys;
	/* 存放短语的文件路径 */
	private String dysPath;
	@Override
	public String toString() {
		return "Configuration [interfaces=" + interfaces + ", input=" + input + ", encode=" + encode + ", threadNumber="
				+ threadNumber + ", files=" + files + ", totalTime=" + totalTime + ", block=" + block + ", output="
				+ output + ", empty=" + empty + ", exception=" + exception + ", stopErrorNum="+ stopErrorNum + 
				", dysPath" + dysPath + ", dys.size=" + dys.size() + "]";
	}
	public String getDysPath() {
		return dysPath;
	}
	public void setDysPath(String dysPath) {
		this.dysPath = dysPath;
	}
	public List<String> getDys(){
		return dys;
	}
	public void setDys(List<String> dys) {
		this.dys = dys;
	}
	public String getException() {
		return this.exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getEmpty() {
		return empty;
	}
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public List<Method> getInterfaces() {
		return interfaces;
	}
	public String getInput() {
		return input;
	}
	public String getThreadNumber() {
		return threadNumber;
	}
	public void setInterfaces(List<Method> interfaces) {
		this.interfaces = interfaces;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public void setThreadNumber(String threadNumber) {
		this.threadNumber = threadNumber;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getStopErrorNum() {
		return stopErrorNum;
	}
	public void setStopErrorNum(String stopErrorNum) {
		this.stopErrorNum = stopErrorNum;
	}

	public static class Method{
		private String name;
		private Map<String,String> params;
		@Override
		public String toString() {
			return "[name=" + name + ", params=" + params + "]";
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
	}
}
