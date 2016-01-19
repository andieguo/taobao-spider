package com.andieguo.taobao.util;

import java.util.List;

import com.andieguo.taobao.bean.QueryBean;

public class LoadQueryBean {

	public static List<QueryBean> loadExcel(){
		try {
			List<QueryBean> queryBeans = ExcelUtil.readXls(ExcelUtil.class.getClassLoader().getResourceAsStream("data.xls"));
			return queryBeans;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<QueryBean> loadProperties(){
		try {
			List<QueryBean> queryBeans = ExcelUtil.readProperties(ExcelUtil.class.getClassLoader().getResourceAsStream("data.properties"));
			return queryBeans;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
