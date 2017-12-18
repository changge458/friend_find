/**
 * 
 */
package com.fz.thread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.fastcluster.ClusterDataJob;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 */
public class RunCluster2 implements Runnable {

	private String input;
	private String output;
	private String delta;
	private String k;
	
	private Logger log = LoggerFactory.getLogger(RunCluster2.class);
	@Override
	public void run() {
		input=input==null?HUtils.FILTER_PREPAREVECTORS:input;
		
		// 删除iter_i(i>0)的所有文件
		try {
			HUtils.clearCenter((output==null?HUtils.CENTERPATH:output));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		output=output==null?HUtils.CENTERPATHPREFIX:output+"/iter_";
		
		// 加一个操作，把/user/root/preparevectors里面的数据复制到/user/root/_center/iter_0/unclustered里面
		HUtils.copy(input,output+"0/unclustered");
		try {
			Thread.sleep(200);// 暂停200ms 
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		// 求解dc的阈值，这里的dc不用传入进来即可，即delta的值
		// 阈值问题可以在讨论，这里暂时使用传进来的阈值即可
//		double dc =dcs[0];
		// 读取聚类中心文件
		Map<Object,Object> vectorsMap= HUtils.readSeq(output+"0/clustered/part-m-00000", Integer.parseInt(k));
		//用户1的声望值	赞数	踩数	浏览数
		//用户2的声望值	赞数	踩数	浏览数
		double[][] vectors = HUtils.getCenterVector(vectorsMap);
		//网红用户两两之间的距离，(n*(n-1)/2)个值,将此距离从小到大排序
		double[] distances= Utils.getDistances(vectors);
		// 这里不使用传入进来的阈值
		
		int iter_i=0;
		int ret=0;
		double tmpDelta=0;
		int kInt = Integer.parseInt(k);
		try {
			do{
				//distances.length == n*(n-1)/2
				if(iter_i>=distances.length){
//					delta= String.valueOf(distances[distances.length-1]/2);
					// 这里使用什么方式还没有想好。。。
					
					
					//delta是阈值
					tmpDelta=Double.parseDouble(delta);
					//k是迭代次数，阈值为
					while(kInt-- > 0){// 超过k次后就不再增大
						tmpDelta*=2;// 每次翻倍
					}
					delta=String.valueOf(tmpDelta);
				}else{
					//默认为最小的距离/2
					delta=String.valueOf(distances[iter_i]/2);
				}
				log.info("this is the {} iteration,with dc:{}",new Object[]{iter_i,delta});
				String[] ar={
						HUtils.getHDFSPath(output)+iter_i+"/unclustered",
						HUtils.getHDFSPath(output)+(iter_i+1),//output
						//HUtils.getHDFSPath(HUtils.CENTERPATHPREFIX)+iter_i+"/clustered/part-m-00000",//center file
						k,
						delta,
						String.valueOf((iter_i+1))
				};
				try{
					ret = ToolRunner.run(HUtils.getConf(), new ClusterDataJob(), ar);
					if(ret!=0){
						log.info("ClusterDataJob failed, with iteration {}",new Object[]{iter_i});
						break;
					}	
				}catch(Exception e){
					e.printStackTrace();
				}
				iter_i++;
				HUtils.JOBNUM++;// 每次循环后加1

			}while(shouldRunNextIter());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		if(ret==0){
			log.info("All cluster Job finished with iteration {}",new Object[]{iter_i});
		}
		
	}
	
	/**
	 * 是否应该继续下次循环
	 * 直接使用分类记录数和未分类记录数来判断
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	private boolean shouldRunNextIter()  {
		
		if(HUtils.UNCLUSTERED==0||HUtils.CLUSTERED==0){
			HUtils.JOBNUM-=2;// 不用监控 则减去2;
			return false;
		}
		return true;
		
	}
	
	public RunCluster2(){}
	
	public RunCluster2(String input,String output,String delta,String k){
		this.delta=delta;
		this.input=input;
		this.output=output;
		this.k=k;
	}
}
