package com.andieguo.taobao.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.andieguo.taobao.bean.QueryBean;
import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.common.Constants;
import com.andieguo.taobao.common.PropertiesUtil;


public class FinnishRunnable implements Runnable{
	private CountDownLatch downLatch;
	private Logger	logger = Logger.getLogger(FinnishRunnable.class);
	private Map<String,Set<TaobaoProduct>> resultMap;
	private List<QueryBean> failQueryBeanList;

	public FinnishRunnable(CountDownLatch downLatch,Map<String,Set<TaobaoProduct>> resultMap,List<QueryBean> failQueryBeanList){
		this.downLatch = downLatch;
		this.resultMap = resultMap;
		this.failQueryBeanList = failQueryBeanList;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.info("FinnishRunnable在等待所有的爬虫线程执行完毕！");
			this.downLatch.await();
			EmailUtil.sendNewProduct(resultMap,failQueryBeanList);
			saveCount(DownloadRest.getCount());
			logger.info("FinnishRunnable释放连接资源！");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("",e);
		}
	}
	
	public void saveCount(Integer count){
		Properties  properties = PropertiesUtil.loadFromFile(new File(Constants.SPIDERCOUNTFILE));
		properties.setProperty("count", String.valueOf(count));
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(Constants.SPIDERCOUNTFILE));
			properties.store(out, "update");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void main(String[] args) {
		
	}

}
