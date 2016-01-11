package com.andieguo.taobao.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.andieguo.taobao.bean.TaobaoProduct;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class CollectionUtil {

	public static Set<TaobaoProduct> getDifferent(Set<TaobaoProduct> set,Set<TaobaoProduct> otherSet){
		long starttime = System.currentTimeMillis();
		SetView<TaobaoProduct> diffSetHandle = Sets.difference(set, otherSet);//是得到左边中不同或者特有的元素，若无，则返回长度为0的集合
		Set<TaobaoProduct> result = diffSetHandle.immutableCopy();//返回一个不可变的左边Set中特有元素集合的Set拷贝
		long endtime = System.currentTimeMillis();
		System.out.println("时间3："+(endtime-starttime));
		return result;
	}
	
	public static Multimap<String, TaobaoProduct> convertList2MutiMap(List<TaobaoProduct> products) {
		return Multimaps.index(products, new Function<TaobaoProduct, String>() {

			public String apply(TaobaoProduct input) {
				// TODO Auto-generated method stub
				return input.getNid();
			}

		});
	}

	public static Map<String, TaobaoProduct> convertList2Map(List<TaobaoProduct> products) {
		return Maps.uniqueIndex(products, new Function<TaobaoProduct, String>() {

			public String apply(TaobaoProduct input) {
				// TODO Auto-generated method stub
				return input.getNid() + input.getDetail_url();
			}

		});
	}

	/**
	 * 对象集合去重
	 * @param list
	 * @return
	 */
	public static <T> List<T> removeDuplicate4List(final List<T> list) {
		 return Lists.newArrayList(Sets.newLinkedHashSet(list));
	}
	
	/**
	 * list转set集合
	 * @param list
	 * @return
	 */
	public static <T> Set<T> removeDuplicate4Set(List<T> list){
		return Sets.newLinkedHashSet(list);
	}

}
