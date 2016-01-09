package com.andieguo.taobao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.andieguo.taobao.bean.PageData;
import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.common.Rest;

public class DownloadRest {
	
	/**
	 * 1 定时器：每隔1个小时执行一次对比
	 * -先将查询到的数据存放置数据库。
	 * -1小时后，将查询到的数据存储到内存中.
	 * -比较差异，如果存在差异，删除数据库中的数据，更新内存的数据到数据库，并清空内存中的数据。
	 * 
	 * 问题1:如何判断一个线程执行完成
	 * 
	 * 2 对一个产品完成一次查询（1-5）页，并保存到cache或者是文件
	 * 3 界面以web的形式展现，服务以web后台的形式运行
	 * 4 发送邮件或短信
	 */
	public static void doRest(){
		String result = "";
		String url = "https://s.taobao.com/search?spm=a230r.1.0.0.WK6qFo&q=电脑&wq=%E7%94%B5%E8%84%91&source=suggest&suggest=magic_1_1&tag=%E7%AE%80%E7%BA%A6%E7%8E%B0%E4%BB%A3&ie=utf-8";
		try {
			File temp = new File(System.getProperty("user.home") + File.separator + "temp");
			if (!temp.exists()) temp.mkdir();
			//读取数据
			File taobaoHtml = new File(temp + File.separator + "taobao-1.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(taobaoHtml, false));//覆盖
			result = Rest.doRest("GET", url, null);
			writer.write(result);
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param startpage 0-99（从0开始）
	 */
	public static List<TaobaoProduct> downloadProduct(String key,int page){
		List<TaobaoProduct> products = null;
		try {
			String surl = MessageFormat.format("https://s.taobao.com/search?spm=a230r.1.0.0.WK6qFo&q={0}&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20151231&ie=utf8&s={1}", key,String.valueOf(page*44));
			System.out.println("surl:"+surl);
			String result = Rest.doRest("GET", surl, null);
			products = parseProduct(key,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}

	private static List<TaobaoProduct>  parseProduct(String key,String result) throws IOException {
		List<TaobaoProduct> products = new ArrayList<TaobaoProduct>();
		Document doc = Jsoup.parse(result,"http://www.dangdang.com");
//		System.out.println(doc.toString());
		Element scripteElement = doc.select("script").get(4);
		String g_page_config = scripteElement.toString();
		g_page_config = g_page_config.substring(g_page_config.indexOf("=")+2, g_page_config.length());
		JSONObject jsonObject = new JSONObject(g_page_config);
		JSONObject modsObj = jsonObject.getJSONObject("mods");
		JSONObject pagerObj = modsObj.getJSONObject("pager");
		JSONObject pagerDataObj = pagerObj.getJSONObject("data");
		@SuppressWarnings("unused")
		PageData pageData = new PageData(pagerDataObj.getInt("pageSize"), pagerDataObj.getInt("currentPage"), pagerDataObj.getInt("totalPage"));
		JSONObject itemlistObj = modsObj.getJSONObject("itemlist");
		JSONObject dataObj = itemlistObj.getJSONObject("data");
		JSONArray auctionsArray = dataObj.getJSONArray("auctions");
		for(int i=0;i<auctionsArray.length();i++){
			JSONObject auctionObj = auctionsArray.getJSONObject(i);
			String detail_url = auctionObj.getString("detail_url");
			String nid = auctionObj.getString("nid");
			double reserve_price = auctionObj.getDouble("reserve_price");
			String title = auctionObj.getString("title");
			double view_price = auctionObj.getDouble("view_price");
			String nick = auctionObj.getString("nick");
			String view_sales = auctionObj.getString("view_sales");
			String user_id = auctionObj.getString("user_id");
			String raw_title = auctionObj.getString("raw_title");
			TaobaoProduct product = new TaobaoProduct(nid, detail_url, title, raw_title, reserve_price, view_price, nick, view_sales, user_id);
			System.out.println(product);
			products.add(product);
		}
//		File file = new File(Constants.PROJECTPATH +File.separator + key+"-"+pagerDataObj.getInt("currentPage"));
//		saveProductsToFile(products,file);
		return products;
	}
	
}
