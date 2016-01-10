package com.andieguo.taobao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

import org.apache.log4j.Logger;

import com.andieguo.taobao.bean.PageBean;
import com.andieguo.taobao.bean.QueryBean;
import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.bean.ThreeTuple;
import com.andieguo.taobao.bean.ThreeTupleUtil;
import com.andieguo.taobao.bean.TwoTuple;

public  class QueryRunnable implements Runnable{

	private QueryBean queryBean;
	private int startpage;
	private int endpage;
	private Exchanger<ThreeTuple<Integer,String,List<TaobaoProduct>>> exchanger;
	private CountDownLatch downLatch;
	private List<TaobaoProduct> productList = new ArrayList<TaobaoProduct>();
	private Logger logger = Logger.getLogger(QueryRunnable.class);
	
	public QueryRunnable(QueryBean queryBean, int startpage, int endpage,Exchanger<ThreeTuple<Integer,String,List<TaobaoProduct>>> exchanger,CountDownLatch downLatch) {
		super();
		this.queryBean = queryBean;
		this.startpage = startpage;
		this.endpage = endpage;
		this.downLatch = downLatch;
		this.exchanger = exchanger;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			for(int i=startpage;i<endpage;i++){
				logger.info(queryBean.getKey()+"==>开始下载第"+startpage+ "页");
				TwoTuple<PageBean,List<TaobaoProduct>> result = DownloadRest.downloadProduct(queryBean.getKey(),queryBean.getStartprice(),queryBean.getEndprice(),startpage);
				PageBean pageBean = result.first;
				if(pageBean.getTotalPage() < endpage) endpage = pageBean.getTotalPage();
				List<TaobaoProduct> products = result.second;
				if(products != null){
					productList.addAll(products);
				}
				logger.info(queryBean.getKey()+"==>成功下载第"+startpage+ "页");
				startpage = startpage + 1;
				Random random = new Random();
				int t = 1000*random.nextInt(10);
				logger.info(queryBean.getKey()+"==>中场休息"+t/1000+"秒");
				Thread.sleep(t);//休眠
			}
			exchanger.exchange(ThreeTupleUtil.tuple(1,queryBean.getKey(), productList));// 与主线程交换信息
			downLatch.countDown();// 计数减少1
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

