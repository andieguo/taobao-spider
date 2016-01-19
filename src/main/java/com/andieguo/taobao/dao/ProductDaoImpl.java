package com.andieguo.taobao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.util.MD5Util;

public class ProductDaoImpl extends BaseDao implements ProductDao {
	private static Logger logger = Logger.getLogger(ProductDaoImpl.class);

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
			logger.error("", e);
		} finally{
			closeAll(conn, pstmt, null);
		}
		return rows;
	}
	
	public List<TaobaoProduct> findAll(String tableName) {
		// TODO Auto-generated method stub
		return executeQuery(TaobaoProduct.class,"select * from `"+MD5Util.MD5(tableName)+"`",new Object[]{});
	}

	public int saveProduct(String tableName,TaobaoProduct product) {
		// TODO Auto-generated method stub
		int rows = this.executeNonQuery("insert into `"+ MD5Util.MD5(tableName)
				+ "` (nid,detail_url,title,raw_title,reserve_price,view_price,nick,view_sales,user_id) values(?,?,?,?,?,?,?,?,?)"
				,new Object[]{product.getNid(),product.getDetail_url(),product.getTitle(),product.getRaw_title(),product.getReserve_price(),product.getView_price(),product.getNick(),product.getView_sales(),product.getUser_id()});
		return rows;
	}

	public int saveProudctList(String tableName,List<TaobaoProduct> products){
		int rows = executeNonQueryBatch("insert into `"+MD5Util.MD5(tableName)
				+ "` (nid,detail_url,title,raw_title,reserve_price,view_price,nick,view_sales,user_id) values(?,?,?,?,?,?,?,?,?)",products);
		return rows;
	}
	
	public int deleteAll(String tableName) {
		// TODO Auto-generated method stub
		int rows = this.executeNonQuery("delete from `"
				+ MD5Util.MD5(tableName)+"`",new Object[]{});
		return rows;
	}
	
	public int creatTable(String tableName){
		String sql = "CREATE TABLE `"+MD5Util.MD5(tableName)+"` ("+
					  "`nid` varchar(20) NOT NULL,"+
					  "`raw_title` varchar(100) NOT NULL,"+
					  "`detail_url` text,"+
					 " `title` text,"+
					  "`reserve_price` double DEFAULT NULL,"+
					 " `view_price` double DEFAULT NULL,"+
					 " `nick` varchar(20) DEFAULT NULL,"+
					  "`view_sales` varchar(20) DEFAULT NULL,"+
					  "`user_id` varchar(20) DEFAULT NULL,"+
					 " PRIMARY KEY (`nid`,`raw_title`)"+
					") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		int rows = this.executeNonQuery(sql, new Object[]{});
		return rows;
	}

	public boolean existTable(String tableName) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from `"+ MD5Util.MD5(tableName)+"` where 0=1");
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			return false;
		} finally{
			closeAll(conn, pstmt, null);
		}
		
	}

}
