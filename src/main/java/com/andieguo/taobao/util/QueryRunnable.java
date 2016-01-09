package com.andieguo.taobao.util;

import java.util.List;
import java.util.Random;

import com.andieguo.taobao.bean.TaobaoProduct;

public  class QueryRunnable implements Runnable{

	private String key;
	private int startpage;
	private int endpage;
	private List<TaobaoProduct> productList;
	
	public QueryRunnable(String key, int startpage, int endpage) {
		super();
		this.key = key;
		this.startpage = startpage;
		this.endpage = endpage;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			Random random = new Random();
			int t = 1000*random.nextInt(10);
			System.out.println("中场休息"+t/1000+"秒");
			Thread.sleep(t);
			for(int i=startpage;i<endpage;i++){
				startpage = startpage + 1;
				System.out.println("开始下载第"+startpage+ "页");
				List<TaobaoProduct> products = DownloadRest.downloadProduct(key,startpage);
				if(products != null){
					productList.addAll(products);
				}
				System.out.println("成功下载第"+startpage+ "页");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//休眠5s
	}
	
}

