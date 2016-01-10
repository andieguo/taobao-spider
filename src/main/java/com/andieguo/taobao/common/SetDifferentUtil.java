package com.andieguo.taobao.common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class SetDifferentUtil {

	public static Set<String> genSet(int sum){
		Set<String> sets = Sets.newHashSet();
		for(int i=0;i<sum;i++){
			sets.add("test"+i);
		}
		return sets;
	}
	
	public static Set<String> genOtherSet(int sum){
		Set<String> sets = Sets.newHashSet();
		for(int i=0;i<sum;i++){
			sets.add("test"+i*2);
		}
		return sets;
	}
	
	/**
	 * set中有，而otherSet没有
	 * 时间：2700 时间复杂度：m*n
	 * @param set
	 * @param otherSet
	 * @return
	 */
	public static Set<String> getDifferent(Set<String> set ,Set<String> otherSet){
		long starttime = System.currentTimeMillis();
		Set<String> result = Sets.newHashSet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			String value = iterator.next();
			if(!otherSet.contains(value)){
				result.add(value);
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("时间："+(endtime-starttime));
		return result;
	}
	
	/**
	 * set有，otherset没有
	 * 时间：30 时间复杂度：m+n
	 * @param set
	 * @param otherSet
	 * @return
	 */
	public static Set<String> getDifferent2(Set<String> set ,Set<String> otherSet){
		long starttime = System.currentTimeMillis();
		Set<String> result = Sets.newHashSet();
		Map<String,Integer> map = new LinkedHashMap<String, Integer>();
		for(String key : set){
			map.put(key, 1);//v=1,set有，otherSet没有
		}
		for(String key : otherSet){
			Integer v = map.get(key);
			if(v != null){
				map.put(key, ++v);//v=2,set&otherSet均有
			}else{
				map.put(key,0);//v=0,set没有，otherset有
			}
		}
		for(String key : map.keySet()){
			if(map.get(key) == 1){
				result.add(key);
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("时间2："+(endtime-starttime));
		return result;
	}
	
	public static Set<String> getDifferent3(Set<String> set ,Set<String> otherSet){
		long starttime = System.currentTimeMillis();
		SetView<String> diffSetHandle=Sets.difference(set, otherSet);//是得到左边中不同或者特有的元素，若无，则返回长度为0的集合
		Set<String> result = diffSetHandle.immutableCopy();//返回一个不可变的左边Set中特有元素集合的Set拷贝
		long endtime = System.currentTimeMillis();
		System.out.println("时间3："+(endtime-starttime));
		return result;
	}
	
	public static void main(String[] args) {
		int sum = 10;
		Set<String> result = getDifferent(genOtherSet(sum),genSet(sum));
		Set<String> result2 = getDifferent2(genOtherSet(sum),genSet(sum));
		Set<String> result3 = getDifferent3(genOtherSet(sum),genSet(sum));
		System.out.println("result"+result.size());
		System.out.println("result2:"+result2.size());
		System.out.println("result3:"+result3.size());
		for(String key : result){
			System.out.println(key);
		}
		System.out.println("=================");
		for(String key : result2){
			System.out.println(key);
		}
		System.out.println("=================");
		for(String key : result3){
			System.out.println(key);
		}
	}
	
}
