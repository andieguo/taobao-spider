package com.andieguo.taobao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.andieguo.taobao.bean.TaobaoProduct;

public class TaobaoProductRestUtil {
	
	public static void doRest(){
		String result = "";
		String url = "https://s.taobao.com/search?q=%E7%94%B5%E8%84%91&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20151230&ie=utf8";
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
	
	public static void readData(){
		try {
			String surl = "https://s.taobao.com/search?q=电脑&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20151231&ie=utf8";
			InputStream inputStream = Rest.doRestInpuStream("GET", surl, null);
			Document doc = Jsoup.parse(inputStream, "UTF-8", "http://www.dangdang.com");
			Element scripteElement = doc.select("script").get(4);
			String g_page_config = scripteElement.toString();
			g_page_config = g_page_config.substring(g_page_config.indexOf("=")+2, g_page_config.length());
			JSONObject jsonObject = new JSONObject(g_page_config);
			JSONObject modsObj = jsonObject.getJSONObject("mods");
			JSONObject pagerObj = modsObj.getJSONObject("pager");
			JSONObject pagerDataObj = pagerObj.getJSONObject("data");
			System.out.println(pagerDataObj.toString());
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
				System.out.println(product.toString());
			}
			inputStream.close();
			System.out.println(auctionsArray.length());
			//System.out.println(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		readData();
	}
}
