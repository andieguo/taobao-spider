package com.andieguo.taobao.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
	
	public static String doRest(String type, String surl, String data) throws Exception {
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
		connection.setRequestProperty("cookie", "thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; _cc_=VFC%2FuZ9ajQ%3D%3D; tg=0; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; swfstore=146589; uc3=nk2=&id2=&lg2=; tracknick=; mt=ci=0_0&cyk=0_0; v=0; cookie2=15278d01b1d73ceb2b29fa45ffe3ad3e; t=31083139ae0a85d703a6735532152d6c; _tb_token_=hWD8rBmx9SdpHM4; linezing_session=xBdrcKVoyQ1jJCOPuObdHLPF_1451570413090FtZq_1; JSESSIONID=5116FF04D43DD8EAF015EF50316870C6; isg=19FAEA3D98E5B43DB9D7A220C4A0FD2B; l=AhkZMf2ZCP5gKaq3W5rTdRFvqQvz0w1Y");
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
	}
	
}
