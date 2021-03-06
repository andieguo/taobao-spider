package com.andieguo.taobao.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.andieguo.taobao.common.CollectionUtil;
import com.andieguo.taobao.common.SetDifferentUtil;
import com.andieguo.taobao.dao.ProductDao;
import com.andieguo.taobao.dao.ProductDaoImpl;

public class DownloadJob implements Job{
	private Logger logger = Logger.getLogger(DownloadJob.class);
	private volatile Map<String,Set<TaobaoProduct>> resultMap = new LinkedHashMap<String,Set<TaobaoProduct>>();
	private volatile List<QueryBean> failQueryBeanList = new ArrayList<QueryBean>();
	/**
	 * 1 定时器：每隔1个小时执行一次对比 -先将查询到的数据存放置数据库。 -1小时后，将查询到的数据存储到内存中.
	 * -比较差异，如果存在差异，删除数据库中的数据，更新内存的数据到数据库，并清空内存中的数据。
	 * 
	 * 问题1:如何判断一个线程执行完成
	 * 
	 * 2 对一个产品完成一次查询（1-5）页，并保存到cache或者是文件 3 界面以web的形式展现，服务以web后台的形式运行 4 发送邮件或短信
	 * @throws Exception 
	 */
	public void excuteJob(List<QueryBean> keys) throws Exception {
		ProductDao productDao = new ProductDaoImpl();
		ExecutorService service = Executors.newSingleThreadExecutor(); // 创建线程池：区别newSingleThreadExecutor与newCachedThreadPool
		Exchanger<ThreeTuple<Integer, QueryBean, List<TaobaoProduct>>> exchanger = new Exchanger<ThreeTuple<Integer, QueryBean, List<TaobaoProduct>>>();// 子线程与主线程交换数据
		if(keys == null){
			logger.info("加载查询对象失败");
			return;
		}
		int sum = keys.size();
		CountDownLatch downLatch = new CountDownLatch(sum);// 进度条计数
		for (int i=0;i<sum;i++) {
			QueryRunnable queryRunnable = new QueryRunnable(keys.get(i), 0, 5, exchanger, downLatch);
			service.execute(queryRunnable);// 为线程池添加任务
		}
		FinnishRunnable finnishRunable = new FinnishRunnable(downLatch,resultMap,failQueryBeanList);
		service.execute(finnishRunable);// 为线程池添加任务
		// 主线程交换数据
		Integer total = Integer.valueOf(0);
		for (int i = 0; i < sum; i++) {
			try {
				// 当主线程调用Exchange对象的exchange()方法后，他会陷入阻塞状态
				// 直到queryRunnable线程也调用了exchange()方法，然后以线程安全的方式交换数据，之后主线程继续运行
				ThreeTuple<Integer, QueryBean, List<TaobaoProduct>> threeTuple = exchanger.exchange(null);
				Integer progress = threeTuple.first;
				QueryBean queryBean = threeTuple.second;
				String key = queryBean.getKey();
				List<TaobaoProduct> newResult = threeTuple.third;
				if (!productDao.existTable(key)) {// 不存在表
					productDao.creatTable(key);
				}
				List<TaobaoProduct> originResult = productDao.findAll(key);
				if(newResult != null && newResult.size() > 0){//有新的数据过来
					if (originResult.size() == 0) {// 没有原始数据
						productDao.saveProudctList(key, newResult);// 将新数据保存到数据库
					} else {// 有原始数据，执行对比
						
							Set<TaobaoProduct> newKeys = CollectionUtil.removeDuplicate4Set(newResult);//去重后的集合
							Set<TaobaoProduct> oldKeys = CollectionUtil.removeDuplicate4Set(originResult);//去重后的集合
							Set<TaobaoProduct> resultKeys = SetDifferentUtil.getDifferent4(newKeys, oldKeys);//差异的key
							if (resultKeys.size() > 0) {// 存在差异
								// 删除数据库中的数据，更新内存的数据到数据库
								productDao.deleteAll(key);
								productDao.saveProudctList(key,CollectionUtil.removeDuplicate4List(newResult));//保存去重后的记录
								// 发送邮件
								logger.info(key + "存在差异" + resultKeys.size());
								resultMap.put(key, resultKeys);
							} else {
								logger.info(key + "不存在差异");
							}
					}
				}else{
					logger.error(key+"下载数据失败");
					failQueryBeanList.add(queryBean);
				}
				if (progress != 0) {
					total = total + progress;
					// 更新进度条
					logger.info(String.format("Progress: %s/%s", total, sum));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("",e);
			}

		}
		service.shutdown();// 关闭线程池
	}
	
	public static void main(String[] args) {
		DownloadJob job = new DownloadJob();
		try {
			List<QueryBean> keys = job.genQueryBeans();//LoadQueryBean.loadProperties();
			job.excuteJob(keys);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<QueryBean> genQueryBeans(){
		List<QueryBean> keys = new ArrayList<QueryBean>();
		keys.add(new QueryBean("E5-2603",850.0,850.0));
		return keys;
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		//定时上传到本地
		Thread thread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				logger.info("==========================开始执行zcloud-local定时任务==========================");
				try {
					List<QueryBean> keys = LoadQueryBean.loadProperties();
					excuteJob(keys);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
