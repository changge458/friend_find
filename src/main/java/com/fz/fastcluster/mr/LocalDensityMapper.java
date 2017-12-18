/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.filter.keytype.IntPairWritable;


/**
 * 输入为<距离d_ij,<向量i编号，向量j编号>>
 * 根据距离dc阈值判断距离d_ij是否小于dc，符合要求则
 * 输出
 * 向量i编号，1
 * 向量j编号，1
 */
public class LocalDensityMapper extends Mapper<DoubleWritable, IntPairWritable, IntWritable, DoubleWritable> {

	private double dc;
	private String method =null;
	
	private IntWritable vectorId= new IntWritable();
	private DoubleWritable one= new DoubleWritable(1);
	
	@Override 
	public void setup(Context context){
		dc=context.getConfiguration().getDouble("DC", 0);
		method = context.getConfiguration().get("METHOD", "gaussian");
	}
	
	@Override
	public void map(DoubleWritable key,IntPairWritable value,Context context)throws InterruptedException,IOException{
		double distance= key.get();
		
		if(method.equals("gaussian")){
			//简单一维高斯函数：简单说明，人的社交圈大致呈正态分布。
			//距离的计算是本MR的重点
            one.set(Math.pow(Math.E, -(distance/dc)*(distance/dc)));
        }
		
		//如果距离小于方差(即朋友间距离处于正态分布范围之内，则说明此人可能被此人所感兴趣)
		if(distance<dc){
			vectorId.set(value.getFirst());
			context.write(vectorId, one);
			vectorId.set(value.getSecond());
			context.write(vectorId, one);
		}
	}

	
	
}
