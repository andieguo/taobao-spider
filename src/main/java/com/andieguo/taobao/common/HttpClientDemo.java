package com.andieguo.taobao.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpClientDemo {
	private final String USER_AGENT = "Mozilla/5.0";

	public void doGet() throws ClientProtocolException, IOException{
		String url = "https://s.taobao.com/search?q=%E7%94%B5%E8%84%91&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20151230&ie=utf8";
//		String url = "http://www.google.com/search?q=httpClient";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		Document doc = Jsoup.parse(result.toString());
		System.out.println(doc.toString());
	}
	
	public static void main(String[] args) {
		HttpClientDemo demo = new HttpClientDemo();
		try {
			demo.doGet();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
