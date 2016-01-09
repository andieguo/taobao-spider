package com.andieguo.taobao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.andieguo.taobao.bean.TaobaoProduct;

public class ProductDaoImpl extends BaseDao implements ProductDao {

	public int executeNonQueryBatch(String sql,List<TaobaoProduct> products) { 
		int rows = -1;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);  
			pstmt = conn.prepareStatement(sql);
			for(TaobaoProduct product : products){
				Object[] params  = new Object[]{product.getNid(),product.getDetail_url(),product.getTitle(),product.getRaw_title(),product.getReserve_price(),product.getView_price(),product.getNick(),product.getView_sales(),product.getUser_id()};
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i+1, params[i]);
				}
				pstmt.addBatch();
			}
			int [] counts = pstmt.executeBatch(); 
			conn.commit(); 
			for(int count : counts){
				rows += count;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally{
			closeAll(conn, pstmt, null);
		}
		return rows;
	}
	
	public List<TaobaoProduct> findAll() {
		// TODO Auto-generated method stub
		return executeQuery(TaobaoProduct.class,"select * from tb_product order by id desc",new Object[]{});
	}

	public int saveProduct(TaobaoProduct product) {
		// TODO Auto-generated method stub
		int rows = this.executeNonQuery("insert into tb_product(nid,detail_url,title,raw_title,reserve_price,view_price,nick,view_sales,user_id) values(?,?,?,?,?,?,?,?,?)"
				,new Object[]{product.getNid(),product.getDetail_url(),product.getTitle(),product.getRaw_title(),product.getReserve_price(),product.getView_price(),product.getNick(),product.getView_sales(),product.getUser_id()});
		return rows;
	}

	public int saveProudctList(List<TaobaoProduct> products){
		int rows = executeNonQueryBatch("insert into tb_product(nid,detail_url,title,raw_title,reserve_price,view_price,nick,view_sales,user_id) values(?,?,?,?,?,?,?,?,?)",products);
		return rows;
	}
	
	public int deleteAll() {
		// TODO Auto-generated method stub
		int rows = this.executeNonQuery("delete from tb_product",new Object[]{});
		return rows;
	}
	
	public static void main(String[] args) {
		
	}

}
