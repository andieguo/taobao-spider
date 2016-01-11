package com.andieguo.taobao.util;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;


public class FinnishRunnable implements Runnable{
	private CountDownLatch downLatch;
	private Logger	logger = Logger.getLogger(FinnishRunnable.class);

	public FinnishRunnable(CountDownLatch downLatch){
		this.downLatch = downLatch;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.info("FinnishRunnable在等待所有的爬虫线程执行完毕！");
			this.downLatch.await();
			logger.info("FinnishRunnable释放连接资源！");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
