/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.util.Utils;

/**
 * 过滤相同的点向量，将所有距离相加(大雾)
 */
public class LocalDensityReducer extends Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {
	private DoubleWritable sumAll = new DoubleWritable();

	@Override
	public void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context)
			throws IOException, InterruptedException {
		double sum = 0;
		for (DoubleWritable v : values) {
			sum += v.get();
		}
		sumAll.set(sum);//
		context.write(key, sumAll);
		Utils.simpleLog("vectorI:" + key.get() + ",density:" + sumAll);
	}
}
