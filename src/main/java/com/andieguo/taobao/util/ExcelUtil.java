package com.andieguo.taobao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.andieguo.taobao.bean.QueryBean;
import com.andieguo.taobao.common.PropertiesUtil;

public class ExcelUtil {
	
	public static List<QueryBean> readProperties(InputStream input){
		List<QueryBean> result = new ArrayList<QueryBean>();
		Properties properties = PropertiesUtil.loadFromInputStream(input);
		Set<Entry<Object, Object>> sets = properties.entrySet();
		for(Entry<Object, Object> key : sets){
			String typekey = (String) key.getKey();
			String typeprice = (String) key.getValue();//[0-0]
			String price[] = typeprice.substring(1, typeprice.length()-1).split("-");
			QueryBean queryBean = new QueryBean(typekey,Double.valueOf(price[0]),Double.valueOf(price[1]));
			result.add(queryBean);
		}
		return result;
	}

	//读取xls文件
	public static List<QueryBean> readXls(InputStream input) throws Exception{
		List<QueryBean> result = new ArrayList<QueryBean>();
		//HSSFWorkbook表示整个excel
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(input);
		//for(int numSheet=0;numSheet<hssfWorkbook.getNumberOfSheets();numSheet++);
		//获取第一个sheet
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		if(hssfSheet == null) return null;
		for(int i=0;i< hssfSheet.getLastRowNum();i=i+2){
			HSSFRow firstRow = hssfSheet.getRow(i);//HSSFRow表示行
			HSSFRow twoRow = hssfSheet.getRow(i+1);//HSSFRow表示行
			for(int j=1;j<4;j++){
				HSSFCell firstCell = firstRow.getCell(j);
				HSSFCell twoCell = twoRow.getCell(j);
				if(firstCell!=null && !firstCell.toString().equals("")){ 
					QueryBean queryBean = new QueryBean();
					queryBean.setKey(firstCell.toString());
					if(twoCell!=null){
						queryBean.setEndprice(twoCell.getNumericCellValue());
						queryBean.setStartprice(twoCell.getNumericCellValue());
					}
					result.add(queryBean);
				}
			}
		}
		return result;
	}
	
	public static void exportFile(List<QueryBean> result,OutputStream out){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(out));
			 
			for (int i = 0; i < result.size(); i++) {
				QueryBean queryBean = result.get(i);
				bw.write(queryBean.getKey()+"=["+queryBean.getStartprice()+"-"+queryBean.getEndprice()+"]");
				bw.newLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void exportFile(List<QueryBean> result,File fout){
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(fout);
			bw = new BufferedWriter(new OutputStreamWriter(fos));//是否支持覆盖
			 
			for (int i = 0; i < result.size(); i++) {
				QueryBean queryBean = result.get(i);
				bw.write(queryBean.getKey()+"=["+queryBean.getStartprice()+"-"+queryBean.getEndprice()+"]");
				bw.newLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static List<QueryBean> readXls(String path) throws Exception{
		InputStream input = new FileInputStream(path);
		return readXls(input);
	}

	//读取xlsx文件
	public static List<String> readXlsx(InputStream input) throws Exception{
		List<String> result = new ArrayList<String>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(input);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if(xssfSheet == null) return null;
		for(int i=0;i<xssfSheet.getLastRowNum();i++){
			XSSFRow xssfRow = xssfSheet.getRow(i);
			XSSFCell cell = xssfRow.getCell(0);
			if(cell!=null) {
				result.add(cell.toString());
			}
		}
		return result;
	}
	
	public static List<String> readXlsx(String path) throws Exception{
		InputStream input = new FileInputStream(path);
		return readXlsx(input);
	}
	
	public static void main(String[] args) {
		System.out.println(ExcelUtil.class.getClassLoader().getResource("data.properties").toString());
		try {
			List<QueryBean> queryBeans = ExcelUtil.readProperties(ExcelUtil.class.getClassLoader().getResourceAsStream("data.properties"));
			System.out.println(queryBeans.size());
			for(QueryBean bean : queryBeans){
				System.out.println(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testExportFile(){
		System.out.println(ExcelUtil.class.getClassLoader().getResource("data.xls").toString());
		try {
			List<QueryBean> queryBeans = ExcelUtil.readXls(ExcelUtil.class.getClassLoader().getResourceAsStream("data.xls"));
			ExcelUtil.exportFile(queryBeans, new File("C:\\Users\\andieguo\\taobao-spider\\data.properties"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testReadExcel(){
		System.out.println(ExcelUtil.class.getClassLoader().getResource("data.xls").toString());
		try {
			List<QueryBean> queryBeans = ExcelUtil.readXls(ExcelUtil.class.getClassLoader().getResourceAsStream("data.xls"));
			System.out.println(queryBeans.size());
			for(QueryBean bean : queryBeans){
				System.out.println(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
