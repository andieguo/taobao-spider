package com.andieguo.taobao.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListDifferentUtil {

	public static List<String> genList(){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<10000;i++){
			list.add("test"+i);
		}
		return list;
	}
	
	public static List<String> genOtherList(){
		List<String> otherList = new ArrayList<String>();
		for(int i=0;i<10000;i++){
			otherList.add("test"+i*2);
		}
		return otherList;
	}
	
	/**
	 * list中有，而otherList没有
	 * 时间：2700 时间复杂度：m*n
	 * @param list
	 * @param otherList
	 * @return
	 */
	public static List<String> getDifferent(List<String> list ,List<String> otherList){
		long starttime = System.currentTimeMillis();
		List<String> result = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			if(!otherList.contains(list.get(i))){
				result.add(list.get(i));
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("时间："+(endtime-starttime));
		return result;
	}
	/**
	 * list有，otherlist没有
	 * 时间：30 时间复杂度：m+n
	 * @param list
	 * @param otherList
	 * @return
	 */
	public static List<String> getDifferent3(List<String> list ,List<String> otherList){
		long starttime = System.currentTimeMillis();
		List<String> result = new ArrayList<String>();
		Map<String,Integer> map = new LinkedHashMap<String, Integer>();
		for(String key : list){
			map.put(key, 1);
		}
		for(String key : otherList){
			Integer v = map.get(key);
			if(v != null){
				map.put(key, ++v);//v=2,list&otherList均有
			}else{
				map.put(key,0);//v=0,list有，otherlist没有
			}//v=1,otherList有，list没有
		}
		for(String key : map.keySet()){
			if(map.get(key) == 0){
				result.add(key);
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("时间3："+(endtime-starttime));
		return result;
	}
	
	public static void main(String[] args) {
		List<String> result = getDifferent(genList(),genOtherList());
		List<String> result3 = getDifferent3(genList(),genOtherList());
		System.out.println("result"+result.size());
		System.out.println("result3:"+result3.size());
		for(String key : result){
			System.out.println(key);
		}
	}
	
}
