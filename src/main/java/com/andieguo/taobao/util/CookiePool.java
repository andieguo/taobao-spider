package com.andieguo.taobao.util;

import java.util.LinkedList;
import java.util.Queue;

public class CookiePool {

	private static Queue<String> cookieQueue = new LinkedList<String>();
	
	static{
		cookieQueue.offer("thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; _cc_=U%2BGCWk%2F7og%3D%3D; tg=0; uc3=nk2=&id2=&lg2=; tracknick=; v=0; cookie2=1c67d70671dee9a8eb8a836faadf8e15; t=31083139ae0a85d703a6735532152d6c; mt=ci%3D-1_0; l=AnNzLB7Jgmy2DzDl5aRpXluOg3mdhQdq; isg=FFA390ABA048B9681DD389F43192D727");
		cookieQueue.offer("thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; _cc_=U%2BGCWk%2F7og%3D%3D; tg=0; uc3=nk2=&id2=&lg2=; tracknick=; v=0; cookie2=1c67d70671dee9a8eb8a836faadf8e15; t=31083139ae0a85d703a6735532152d6c; JSESSIONID=3B01BF96C242293223CC087B8DA49576; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; mt=ci%3D-1_0; isg=DD6320610D1ABB4D14C784EC0C7D1C70; l=AkZGJT1ozyeLOOUekDu0xZeEFjLI/Yph");
		cookieQueue.offer("thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; _cc_=U%2BGCWk%2F7og%3D%3D; tg=0; uc3=nk2=&id2=&lg2=; tracknick=; v=0; cookie2=1c67d70671dee9a8eb8a836faadf8e15; t=31083139ae0a85d703a6735532152d6c; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; JSESSIONID=38FA3688DF47D9A3724CF0441D9477CC; mt=ci%3D-1_0; l=ApiYPeJw2Q0JtotUcp1CX9Xd6Mgq4/wL; isg=11C9B0EEFEBBD2D0F5F083D2F800FC85");
		cookieQueue.offer("thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; _cc_=U%2BGCWk%2F7og%3D%3D; tg=0; uc3=nk2=&id2=&lg2=; tracknick=; mt=ci%3D-1_0; v=0; cookie2=1c796f0010c841888c8009555279248a; t=31083139ae0a85d703a6735532152d6c; isg=40FD6093CA98968D272B91840B65480E; l=AqKiFb1uo4uPBMGK7E8oGRQvciYEg6YN");
		//cookieQueue.offer("thw=cn; cna=zox0Do/imyoCAXWXevis1SPZ; _cc_=VFC%2FuZ9ajQ%3D%3D; tg=0; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; swfstore=146589; uc3=nk2=&id2=&lg2=; tracknick=; mt=ci=0_0&cyk=0_0; v=0; cookie2=15278d01b1d73ceb2b29fa45ffe3ad3e; t=31083139ae0a85d703a6735532152d6c; _tb_token_=hWD8rBmx9SdpHM4; linezing_session=xBdrcKVoyQ1jJCOPuObdHLPF_1451570413090FtZq_1; JSESSIONID=5116FF04D43DD8EAF015EF50316870C6; isg=19FAEA3D98E5B43DB9D7A220C4A0FD2B; l=AhkZMf2ZCP5gKaq3W5rTdRFvqQvz0w1Y");
	}
	
	public static Queue<String> getCookieQueue(){
		return cookieQueue;
	}
	
	public static void main(String[] args) {
		String cookie = CookiePool.getCookieQueue().poll();
		System.out.println(cookie);
	}
}
