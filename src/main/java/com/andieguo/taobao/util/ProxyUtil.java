package com.andieguo.taobao.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class ProxyUtil {

	@SuppressWarnings("serial")
	public static HashMap<String, String> proxyMap = new HashMap<String, String>() {
		{
//			put("http", "107.150.96.188:8080");
//			put("https", "162.208.49.45:3127");
//			put("https", "120.195.194.186:80");
//			put("https", "120.195.104.157:80");
//			put("socks4", "211.236.185.151:1080");
//			put("socks5", "218.21.230.156:443");
			put("http","218.92.227.165:20912");
			put("socks4","174.141.143.207:10200");
			put("socks4","217.197.251.102:1080");
//			put("http","120.195.194.169:80");
		}
	};
	
	
	public static void main(String args[]){
		int dataLen = 0;

		/** proxy protocol
		System.out.println("+++++++++ proxy protocol +++++++++");
		Iterator<String> it = ProxyUtil.proxyMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = ProxyUtil.proxyMap.get(key);
			dataLen = proxy_proto(value, key);
			System.out.println(key + " : " + value + " --> " + dataLen);
		}**/

		// proxy property
		System.out.println("\n+++++++++ proxy property +++++++++");
		Iterator<String> it2 = ProxyUtil.proxyMap.keySet().iterator();
		while(it2.hasNext()){
			String key = it2.next();
			String value = ProxyUtil.proxyMap.get(key);
			dataLen = proxy_prop(value, key);
			System.out.println(key + " : " + value + " --> " + dataLen);
		}

		/** proxy socks
		System.out.println("\n++++++++ proxy socks +++++++++++");
		Iterator<String> it3 = ProxyUtil.proxyMap.keySet().iterator();
		while(it3.hasNext()){
			String key = it3.next();
			String value = ProxyUtil.proxyMap.get(key);
			dataLen = proxy_socks(value, key);
			System.out.println(key + " : " + value + " --> " + dataLen);
		}**/
	}
	
	
	// 设置系统代理，支持全部协议 http，https，socks4，socks5
	private static int proxy_prop(String proxyStr, String proto_type) {
		int dataLen = 0;

		String proxy_ip = proxyStr.split(":")[0];
		String proxy_port = proxyStr.split(":")[1];
		
		Properties prop = System.getProperties();
		
		// http
		if(proto_type.equals("http")){
			prop.setProperty("http.proxyHost", proxy_ip);
			prop.setProperty("http.proxyPort", proxy_port);
			prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");
		}
		
		// https
		if(proto_type.equals("https")){
			prop.setProperty("https.proxyHost", proxy_ip);
	        prop.setProperty("https.proxyPort", proxy_port);
		}
        
        // socks
		if(proto_type.equals("socks4") || proto_type.equals("socks5")){
	        prop.setProperty("socksProxyHost", proxy_ip);
	        prop.setProperty("socksProxyPort", proxy_port);
		}
        
        // ftp
		if(proto_type.equals("ftp")){
	        prop.setProperty("ftp.proxyHost", proxy_ip);
	        prop.setProperty("ftp.proxyPort", proxy_port);
	        prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		}
        
//        // auth 设置登陆代理服务器的用户名和密码
//        Authenticator.setDefault(new MyAuthenticator("user", "pwd"));
        
		try{
			URL url = new URL("http://www.baidu.com");		// http://proxy.mimvp.com
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(30 * 1000);
			
			InputStream in = conn.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			char[] ch = new char[1024];
			int len = 0;
			String data = "";
			while((len = reader.read(ch)) > 0) {
				String newData = new String(ch, 0, len);
				data += newData;
			}
			dataLen = data.length();
			
		} catch(Exception e) {
//			e.printStackTrace();
		}
        return dataLen;
	}
	
	static class MyAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";
        public MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }
	
	
	// 使用函数协议，仅支持 HTTP 和 SOCKS5
	@SuppressWarnings("unused")
	private static int proxy_proto(String proxyStr, String proto_type){
		int dataLen = 0;

		String proxy_ip = proxyStr.split(":")[0];
		int proxy_port = Integer.parseInt(proxyStr.split(":")[1]);
		
		try{
			URL url = new URL("http://www.baidu.com");		// http://proxy.mimvp.com
			
			InetSocketAddress addr = new InetSocketAddress(proxy_ip, proxy_port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			if(proto_type.equals("socks4") || proto_type.equals("socks5")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
			}
			
			URLConnection conn = url.openConnection(proxy);
			conn.setConnectTimeout(30 * 1000);
			
			InputStream in = conn.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			char[] ch = new char[1024];
			int len = 0;
			String data = "";
			while((len = reader.read(ch)) > 0) {
				String newData = new String(ch, 0, len);
				data += newData;
			}
			dataLen = data.length();
			
		} catch(Exception e) {
//			e.printStackTrace();
		}
		return dataLen;
	}
	
	
	// proxy socket，测试用
	@SuppressWarnings("unused")
	private static int proxy_socks(String proxyStr, String proto_type){
		int dataLen = 0;
		Socket socket = null;
		
		String proxy_ip = proxyStr.split(":")[0];
		int proxy_port = Integer.parseInt(proxyStr.split(":")[1]);
		
		try {
			socket = new Socket(proxy_ip, proxy_port);
			
			byte[] ch = new String("GET http://www.baidu.com/ HTTP/1.1\r\n\r\n").getBytes();
			socket.getOutputStream().write(ch);
			socket.setSoTimeout(30 * 1000);
			
			byte[] bt = new byte[1024];
			InputStream in = socket.getInputStream();
			int len = 0;
			String data = "";
			while((len = in.read(bt)) > 0) {
				String newData = new String(bt, 0, len);
				data += newData;
			}
			dataLen = data.length();
		}catch(Exception e) {
//			e.printStackTrace();
		} finally{
			try {
				if(socket != null){
					socket.close();
				}
			} catch (IOException e) {
//				e.printStackTrace();
			}
			socket = null;
		}
		return dataLen;
	}
}


/**
	Run Result : 
	
		+++++++++ proxy protocol +++++++++
		https : 120.195.104.157:80 --> 0
		http : 107.150.96.188:8080 --> 97345
		socks5 : 218.21.230.156:443 --> 97034
		socks4 : 211.236.185.151:1080 --> 0
		
		+++++++++ proxy property +++++++++
		https : 120.195.104.157:80 --> 97173
		http : 107.150.96.188:8080 --> 97087
		socks5 : 218.21.230.156:443 --> 97063
		socks4 : 211.236.185.151:1080 --> 97356
		
		++++++++ proxy socks +++++++++++
		https : 120.195.104.157:80 --> 0
		http : 107.150.96.188:8080 --> 0
		socks5 : 218.21.230.156:443 --> 0
		socks4 : 211.236.185.151:1080 --> 0
*/