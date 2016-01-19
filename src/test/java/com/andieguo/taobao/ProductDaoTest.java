package com.andieguo.taobao;

import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.dao.ProductDao;
import com.andieguo.taobao.dao.ProductDaoImpl;
import com.andieguo.taobao.util.MD5Util;

import junit.framework.TestCase;

public class ProductDaoTest extends TestCase
{
	
	private ProductDao dao;
    
    @Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
    	dao = new ProductDaoImpl();
	}

	public void testFindAll(){
    	for(TaobaoProduct product:dao.findAll("E5-2648V3")){
			System.out.println(product);
		}
    }
    
    public void testSave(){
		int sum = dao.saveProduct("E5-2648V3",new TaobaoProduct("1","2","1","2",1.0,1.0,"1","1","1"));
		System.out.println(sum);
    }
    
    public void testSaveList(){
    	int sum  = dao.saveProudctList("电脑",dao.findAll("电脑"));
		System.out.println(sum);
    }
    
    public void testDeleteAll(){
		dao.deleteAll("E5-2648V3");
    }
    
    public void testCreateTable(){
    	dao.creatTable("E5-2648V3");
    }
    
    public void testExistTable(){
    	System.out.println("12723e967d890496c15c2affde86b05c".length());
    	System.out.println(MD5Util.MD5("E5-267V3"));
    	System.out.println(MD5Util.MD5("E5-2648V3"));
    	System.out.println(MD5Util.MD5("E5-2643"));
    	System.out.println(MD5Util.MD5("E5-2643V2"));
    	System.out.println(dao.existTable("E5-2648V3"));
    }
}
