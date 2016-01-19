package com.andieguo.taobao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.andieguo.taobao.bean.PageBean;
import com.andieguo.taobao.bean.TaobaoProduct;
import com.andieguo.taobao.bean.TwoTuple;
import com.andieguo.taobao.bean.TwoTupleUtil;
import com.andieguo.taobao.common.Constants;
import com.andieguo.taobao.common.PropertiesUtil;
import com.andieguo.taobao.common.Rest;

public class DownloadRest {
	private static Logger logger = Logger.getLogger(DownloadRest.class);
	private static Properties properties = PropertiesUtil.loadFromFile(new File(Constants.SPIDERCOUNTFILE));
	private volatile static Integer count = Integer.valueOf(properties.getProperty("count"));
	private volatile static String cookie = CookiePool.getCookieQueue().poll();//只会加载一次

	public static void doRest(){
		String result = "";
		String url = "https://s.taobao.com/search?spm=a230r.1.0.0.WK6qFo&q=电脑&wq=%E7%94%B5%E8%84%91&source=suggest&suggest=magic_1_1&tag=%E7%AE%80%E7%BA%A6%E7%8E%B0%E4%BB%A3&ie=utf-8";
		try {
			File temp = new File(System.getProperty("user.home") + File.separator + "temp");
			if (!temp.exists()) temp.mkdir();
			//读取数据
			File taobaoHtml = new File(temp + File.separator + "taobao-1.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(taobaoHtml, false));//覆盖
			result = Rest.doRest("GET", url, null,cookie);
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
	public static TwoTuple<PageBean, List<TaobaoProduct>> downloadProduct(String key,double startprice,double endprice,int page){
		count ++;
		logger.info("count:"+count);
		try {
			String surl = "";
			String spm = "a21bo.7724922.8452-taobao-item.1";
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMDD");
			String initiative_id = "staobaoz_"+dateFormate.format(new Date());
			if(startprice == 0.0 && endprice == 0.0){
				surl = MessageFormat.format("https://s.taobao.com/search?spm={0}&q={1}&js=1&stats_click=search_radio_all%3A1&initiative_id={2}&ie=utf8&s={3}&cps=yes&cat=11", 
						spm,URLEncoder.encode(key, "UTF-8"),initiative_id,String.valueOf(page*44));
			}else{
				surl = MessageFormat.format("https://s.taobao.com/search?spm={0}&q={1}&js=1&stats_click=search_radio_all%3A1&initiative_id={2}&filter=reserve_price[{3},{4}]&ie=utf8&s={5}&cps=yes&cat=11",
						spm,URLEncoder.encode(key, "UTF-8"),initiative_id,String.valueOf(startprice),String.valueOf(endprice),String.valueOf(page*44));
			}
			logger.info("surl:"+surl);
			String result = Rest.doRest("GET", surl, null,cookie);
			if(result != null) return parseProduct(key,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("",e);
			if(e instanceof org.json.JSONException){
				logger.error("无法获取正确的JSON数据，需更换cookie或ip");
				cookie = CookiePool.getCookieQueue().poll();
				if(cookie != null){
					logger.error("当前的cookie为:"+cookie);
				}else{
					logger.error("cookie池已用完请更新cookie池");
				}
			}
		}
		return null;
	}

	private static TwoTuple<PageBean, List<TaobaoProduct>>  parseProduct(String key,String result){
		List<TaobaoProduct> products = new ArrayList<TaobaoProduct>();
		Document doc = Jsoup.parse(result,"http://www.dangdang.com");
//		logger.info(doc.toString());
		Element scripteElement = doc.select("script").get(4);
		String g_page_config = scripteElement.toString();
		g_page_config = g_page_config.substring(g_page_config.indexOf("=")+2, g_page_config.length());
		JSONObject jsonObject = new JSONObject(g_page_config);
		JSONObject modsObj = jsonObject.getJSONObject("mods");
		JSONObject pagerObj = modsObj.getJSONObject("pager");
		String pageStatus = pagerObj.getString("status");
		PageBean pageData = null;
		if(pageStatus.equals("show")){
			JSONObject pagerDataObj = pagerObj.getJSONObject("data");
			pageData = new PageBean(pagerDataObj.getInt("pageSize"), pagerDataObj.getInt("currentPage"), pagerDataObj.getInt("totalPage"));
		}else{
			JSONObject sortbarObj = modsObj.getJSONObject("sortbar");
			JSONObject sortbarDataObj = sortbarObj.getJSONObject("data");
			JSONObject sortbarPagerObj = sortbarDataObj.getJSONObject("pager");
			pageData = new PageBean(sortbarPagerObj.getInt("pageSize"), sortbarPagerObj.getInt("currentPage"), sortbarPagerObj.getInt("totalPage"));
		}
		logger.info(pageData);
		JSONObject itemlistObj = modsObj.getJSONObject("itemlist");
		String itemStatus = itemlistObj.getString("status");
		if(itemStatus.equals("show")){
			JSONObject dataObj = itemlistObj.getJSONObject("data");
			JSONArray auctionsArray = dataObj.getJSONArray("auctions");
			for(int i=0;i<auctionsArray.length();i++){
				try{
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
					logger.info(product);
					products.add(product);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("",e);
				}
			}
		}
//		File file = new File(Constants.PROJECTPATH +File.separator + key+"-"+pagerDataObj.getInt("currentPage"));
//		saveProductsToFile(products,file);
		return TwoTupleUtil.tuple(pageData, products);
	}

	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
		System.out.println(format.format(new Date()));
	}

	public static Integer getCount() {
		return count;
	}
	
}
