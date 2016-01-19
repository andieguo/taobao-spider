package com.andieguo.taobao.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.andieguo.taobao.common.PropertiesUtil;

/**
 * 处理JDBC基本的操作
 * @author administrator
 *
 */
public class BaseDao {
	
	//private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//private static final String URL = "jdbc:sqlserver://localhost:1433;DataBaseName=bbs";
	private static String DRIVER = null;
	private static String URL = null;
	private static String USER = null;
	private static String PASSWORD = null;
	private static Logger logger = Logger.getLogger(BaseDao.class);
	
	static{
		/**Properties properties = PropertiesUtil.loadFromInputStream(BaseDao.class.getClassLoader().getResourceAsStream("/config.properties"));
		DRIVER =  properties.getProperty("jdbc.driver");
		URL = properties.getProperty("jdbc.url");
		USER = properties.getProperty("jdbc.user");
		PASSWORD = properties.getProperty("jdbc.password");
		**/
		DRIVER =  "com.mysql.jdbc.Driver";
		URL = "jdbc:mysql://localhost:3306/db_taospider?useUnicode=true&amp;characterEncoding=utf-8";
		USER = "root";
		PASSWORD = "root";
	}
	
	/**
	 * 只适应用于没有引用关系的类
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> executeQuery(Class<T> clazz,String sql,Object... params){
		List<T> objects = new ArrayList<T>();
		T obj = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++){
				pstmt.setObject(i+1, params[i]);
			}
			// 执行查询
			rs = pstmt.executeQuery();
			// 获取结果
			while (rs.next()) {
				rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				String[] colName = new String[count];
				for (int i = 0; i < count; i++)
					colName[i] = rsmd.getColumnLabel(i + 1);
				obj = clazz.newInstance();//实例化clazz类
				Method[] ms = clazz.getMethods();//获取到clazz类的所有方法
				for (Method m : ms)
					for (int i = 0; i < colName.length; i++)
						if (m.getName().equalsIgnoreCase("set" + colName[i])) 
							m.invoke(obj, rs.getObject(colName[i]));
				objects.add(obj);
			}
			return objects;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return objects;
	}


	/**
	 * 获取连接对象
	 * @return
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			//加载驱动
			Class.forName(DRIVER);
			//获取连接对象
			conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			logger.error("", e);
		} 
		return conn;
	}
	
	
	/**
	 * 通用的执行增，删，改的方法
	 * 
	 * @param sql 预编译的语句
	 * @param params 参数
	 * @return 受影响的行数
	 */
	public int executeNonQuery(String sql,Object...params) { 
		int rows = -1;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i+1, params[i]);
			}
			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("", e);
		} finally{
			closeAll(conn, pstmt, null);
		}
		return rows;
	}
	
	/**
	 * 关闭释放资源
	 * @param conn 连接对象
	 * @param pstmt 预处理语句
	 * @param res 结果集
	 */
	public void closeAll(Connection conn,PreparedStatement pstmt,ResultSet res) {
		try {
			if(res!=null) {
				res.close();
				res = null;
			}
			if(pstmt!=null) {
				pstmt.close();
				pstmt = null;
			}
			if(conn!=null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			logger.error("", e);
		}

	}
	
	public static void main(String[] args) {
		
		BaseDao baseDao = new BaseDao();
		System.out.println("conn:"+baseDao.getConnection());
	}
	
}
