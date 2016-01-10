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
import com.andieguo.taobao.common.Rest;

public class DownloadRest {
	private static Logger logger = Logger.getLogger(DownloadRest.class);

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
	public static TwoTuple<PageBean, List<TaobaoProduct>> downloadProduct(String key,double startprice,double endprice,int page){
		try {
			String surl = "";
			String spm = "a21bo.7724922.8452-taobao-item.1";
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMDD");
			String initiative_id = "staobaoz_"+dateFormate.format(new Date());
			if(startprice == 0.0 && endprice == 0.0){
				surl = MessageFormat.format("https://s.taobao.com/search?spm={0}&q={1}&js=1&stats_click=search_radio_all%3A1&initiative_id={2}&ie=utf8&s={3}", 
						spm,URLEncoder.encode(key, "UTF-8"),initiative_id,String.valueOf(page*44));
			}else{
				surl = MessageFormat.format("https://s.taobao.com/search?spm={0}&q={1}&js=1&stats_click=search_radio_all%3A1&initiative_id={2}&filter=reserve_price[{3},{4}]&ie=utf8&s={5}",
						spm,URLEncoder.encode(key, "UTF-8"),initiative_id,String.valueOf(startprice),String.valueOf(endprice),String.valueOf(page*44));
			}
			logger.info("surl:"+surl);
			String result = Rest.doRest("GET", surl, null);
			return parseProduct(key,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		JSONObject pagerDataObj = pagerObj.getJSONObject("data");
		PageBean pageData = new PageBean(pagerDataObj.getInt("pageSize"), pagerDataObj.getInt("currentPage"), pagerDataObj.getInt("totalPage"));
		logger.info(pageData);
		JSONObject itemlistObj = modsObj.getJSONObject("itemlist");
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
}
