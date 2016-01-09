package com.andieguo.taobao.dao;

import java.util.List;

import com.andieguo.taobao.bean.TaobaoProduct;

public interface ProductDao {

	public List<TaobaoProduct> findAll();
	
	public int saveProduct(TaobaoProduct product);
	
	public int saveProudctList(List<TaobaoProduct> products);
	
	public int deleteAll();
}
