package com.andieguo.taobao;

import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.dao.ProductDao;
import com.andieguo.taobao.dao.ProductDaoImpl;

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
    	for(TaobaoProduct product:dao.findAll("电脑2")){
			System.out.println(product);
		}
    }
    
    public void testSave(){
		int sum = dao.saveProduct("电脑",new TaobaoProduct("1","2","1","2",1.0,1.0,"1","1","1"));
		System.out.println(sum);
    }
    
    public void testSaveList(){
    	int sum  = dao.saveProudctList("电脑",dao.findAll("电脑"));
		System.out.println(sum);
    }
    
    public void testDeleteAll(){
		dao.deleteAll("电脑");
		dao.deleteAll("西服");
    }
    
    public void testCreateTable(){
    	dao.creatTable("电脑");
    }
    
    public void testExistTable(){
    	System.out.println(dao.existTable("电脑"));
    }
}
