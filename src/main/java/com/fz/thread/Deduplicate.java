/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

import com.fz.filter.DeduplicateJob;
import com.fz.util.HUtils;

/**
 * 作为分线程，调用DeduplicateJob
 * 作用:将users.xml 去除重复记录
 */
public class Deduplicate implements Runnable {

	private String input;
	private String output;
	
	public Deduplicate(String input,String output){
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
			ToolRunner.run(HUtils.getConf(), new DeduplicateJob(),args );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
