package com.andieguo.taobao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.andieguo.taobao.bean.QueryBean;
import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.bean.ThreeTuple;
import com.andieguo.taobao.common.SetDifferentUtil;
import com.andieguo.taobao.dao.ProductDao;
import com.andieguo.taobao.dao.ProductDaoImpl;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

public class DownloadJob implements Job{
	private Logger logger = Logger.getLogger(DownloadJob.class);

	/**
	 * 1 定时器：每隔1个小时执行一次对比 -先将查询到的数据存放置数据库。 -1小时后，将查询到的数据存储到内存中.
	 * -比较差异，如果存在差异，删除数据库中的数据，更新内存的数据到数据库，并清空内存中的数据。
	 * 
	 * 问题1:如何判断一个线程执行完成
	 * 
	 * 2 对一个产品完成一次查询（1-5）页，并保存到cache或者是文件 3 界面以web的形式展现，服务以web后台的形式运行 4 发送邮件或短信
	 * @throws Exception 
	 */
	public void excuteJob() throws Exception {
		ProductDao productDao = new ProductDaoImpl();
		ExecutorService service = Executors.newSingleThreadExecutor(); // 创建线程池：区别newSingleThreadExecutor与newCachedThreadPool
		Exchanger<ThreeTuple<Integer, String, List<TaobaoProduct>>> exchanger = new Exchanger<ThreeTuple<Integer, String, List<TaobaoProduct>>>();// 子线程与主线程交换数据
		List<QueryBean> keys = new ArrayList<QueryBean>();
		keys.add(LoadQueryBean.load().get(0));
		int sum = keys.size();
		CountDownLatch downLatch = new CountDownLatch(sum);// 进度条计数
		for (QueryBean key : keys) {
			QueryRunnable queryRunnable = new QueryRunnable(key, 0, 5, exchanger, downLatch);
			service.execute(queryRunnable);// 为线程池添加任务
		}
		// 主线程交换数据
		Integer total = Integer.valueOf(0);
		for (int i = 0; i < sum; i++) {
			try {
				// 当主线程调用Exchange对象的exchange()方法后，他会陷入阻塞状态
				// 直到queryRunnable线程也调用了exchange()方法，然后以线程安全的方式交换数据，之后主线程继续运行
				ThreeTuple<Integer, String, List<TaobaoProduct>> threeTuple = exchanger.exchange(null);
				Integer progress = threeTuple.first;
				String key = threeTuple.second;
				List<TaobaoProduct> newResult = threeTuple.third;
				if (!productDao.existTable(key)) {// 不存在表
					productDao.creatTable(key);
				}
				List<TaobaoProduct> originResult = productDao.findAll(key);
				if (originResult.size() == 0) {// 没有原始数据
					productDao.saveProudctList(key, newResult);// 将新数据保存到数据库
				} else {// 有原始数据，执行对比
					Set<String> newKeys = convertList2MutiMap(newResult).keySet();//去重后的key
					Set<String> oldKeys = convertList2MutiMap(originResult).keySet();//去重后的key
					Set<String> resultKeys = SetDifferentUtil.getDifferent(newKeys, oldKeys);//差异的key
					if (resultKeys.size() > 0) {// 存在差异
						// 删除数据库中的数据，更新内存的数据到数据库
						productDao.deleteAll(key);
						productDao.saveProudctList(key, removeDuplicate(newResult));//保存去重后的记录
						// 发送邮件
						logger.info(key + "存在差异" + resultKeys.size());
					} else {
						logger.info(key + "不存在差异");
					}
				}
				if (progress != 0) {
					total = total + progress;
					// 更新进度条
					logger.info(String.format("Progress: %s/%s", total, sum));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		service.shutdown();// 关闭线程池

	}

	public static Multimap<String, TaobaoProduct> convertList2MutiMap(List<TaobaoProduct> products) {
		return Multimaps.index(products, new Function<TaobaoProduct, String>() {

			public String apply(TaobaoProduct input) {
				// TODO Auto-generated method stub
				return input.getNid();
			}

		});
	}

	public static Map<String, TaobaoProduct> convertList2Map(List<TaobaoProduct> products) {
		return Maps.uniqueIndex(products, new Function<TaobaoProduct, String>() {

			public String apply(TaobaoProduct input) {
				// TODO Auto-generated method stub
				return input.getNid() + input.getDetail_url();
			}

		});
	}

	public static void main(String[] args) {
		DownloadJob job = new DownloadJob();
		try {
			job.excuteJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static <T> List<T> removeDuplicate(final List<T> list) {
		 return Lists.newArrayList(Sets.newLinkedHashSet(list));
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		//定时上传到本地
		Thread thread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				logger.info("==========================开始执行zcloud-local定时任务==========================");
				try {
					excuteJob();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
