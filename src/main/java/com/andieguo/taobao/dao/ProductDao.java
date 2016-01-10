package com.andieguo.taobao.dao;

import java.util.List;

import com.andieguo.taobao.bean.TaobaoProduct;

public interface ProductDao {

	public List<TaobaoProduct> findAll(String tableName);
	
	public int saveProduct(String tableName,TaobaoProduct product);
	
	public int saveProudctList(String tableName,List<TaobaoProduct> products);
	
	public int deleteAll(String tableName);
	
	public int creatTable(String tableName);
	
	public boolean existTable(String tableName);
}
