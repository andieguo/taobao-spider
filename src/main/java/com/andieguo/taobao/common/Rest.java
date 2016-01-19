package com.andieguo.taobao.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class Rest {
	private static Logger logger = Logger.getLogger(Rest.class);

	public static String doRest(String type, String surl, String data,String cookie) throws Exception {
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		URL url = new URL(surl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(5000);
		connection.setRequestMethod(type);
		connection.setRequestProperty("ContentType", "text;charset=utf-8");
		connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2");
		connection.setRequestProperty("referer", surl);
		connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
		connection.setRequestProperty("Host", "s.taobao.com");
		if(cookie != null){
			connection.setRequestProperty("cookie", cookie);
			if (data != null) {
				connection.setDoOutput(true);
				OutputStream os = connection.getOutputStream();
				os.write(data.getBytes("utf-8"));// 写入data信息
			}
			connection.setDoInput(true);
			//服务器端返回来的是UTF-8编码之后的流，读取的时候需要使用UTF-8格式进行读取
			in = new InputStreamReader(connection.getInputStream(),"UTF-8");
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			connection.disconnect();
			return strBuffer.toString();
		}else{
			logger.error("cookie池中的cookie已用完，请更新cookie池");
			return null;
		}
	}
}
