package com.andieguo.taobao.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.andieguo.taobao.quartz.QuartzManager;

public class InitServlet implements ServletContextListener {

	private Logger logger = Logger.getLogger(InitServlet.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("系统关闭");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("系统启动");
		QuartzManager.addJob("DownloadRestJob", DownloadJob.class.getName(), "0 */2 * * * ?");
	}

}
