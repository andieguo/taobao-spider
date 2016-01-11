package com.andieguo.taobao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.andieguo.taobao.bean.TaobaoProduct;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import junit.framework.TestCase;

public class CollectionTest extends TestCase{

	public void testListToSet(){
		List<String> one = new ArrayList<String>();
		one.add("aello");
		one.add("bello");
		one.add("hello");
		one.add("hello1");
		one.add("hello");
		one.add("hello1");
		one.add("aello");
		one.add("bello");
		Set<String> two = Sets.newLinkedHashSet(one);//将list集合转换为set集合
		Set<String> three = Sets.newHashSet(one);//将list集合转换为set集合
		for(String k:two){
			System.out.println(k);
		}
		for(String k:three){
			System.out.println(k);
		}
	}
	
	public void testListObjectToSet(){
		List<TaobaoProduct> products = new ArrayList<TaobaoProduct>();
		products.add(new TaobaoProduct("1", "1", "1", "1", 1.0, 1.0, "1", "1", "1"));
		products.add(new TaobaoProduct("2", "2", "2", "2", 1.0, 1.0, "2", "2", "2"));
		products.add(new TaobaoProduct("1", "1", "1", "1", 1.0, 1.0, "1", "1", "1"));
		products.add(new TaobaoProduct("2", "2", "2", "2", 1.0, 1.0, "2", "2", "2"));
		Set<TaobaoProduct> productSet = Sets.newLinkedHashSet(products);
		for(TaobaoProduct p:productSet){
			System.out.println(p);
		}
	}
	
	public void testMutiMapToSet(){
		Multimap<String, String> myMultimap = ArrayListMultimap.create();
		 // Adding some key/value
	    myMultimap.put("Fruits", "Bannana");
	    myMultimap.put("Fruits", "Apple");
	    myMultimap.put("Fruits", "Pear");
	    myMultimap.put("Fruits", "Pear");
	    myMultimap.put("Vegetables", "Carrot");
	    Set<String> four = myMultimap.keySet();
	    for(String key:four){
	    	System.out.println(key);
	    }
	}
	
	
}
