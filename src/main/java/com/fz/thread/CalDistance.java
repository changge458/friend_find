/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

import com.fz.filter.CalDistanceJob;
import com.fz.util.HUtils;

/**
 * 作为线程调用CalDistanceJob方法
 * 作用：计算记录两两之间的距离 map输出 reduce 输出 距离，<样本id，样本id>
 */
public class CalDistance implements Runnable {

	private String input;
	private String output;
	
	public CalDistance(String input,String output){
		this.input=input;
		this.output=output;
	}
	
	@Override
	public void run() {
		String [] args ={
				HUtils.getHDFSPath(input),
				HUtils.getHDFSPath(output)
		};
		try {
			ToolRunner.run(HUtils.getConf(), new CalDistanceJob(),args );
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	

}
