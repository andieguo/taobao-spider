package com.andieguo.taobao.util;

import java.util.LinkedList;
import java.util.Queue;

public class QueueUtil {

	public static void main(String[] args) {
		Queue<String> queue = new LinkedList<String>();
		//使用offer()来加入元素
		queue.offer("a");
		queue.offer("b");
		String str = null;
		//使用peek()来获取头元素
		while((str = queue.poll())!=null){//使用poll()来获取并移出元素
			System.out.println(str);
		}
		System.out.println(queue.size());
	}
}
