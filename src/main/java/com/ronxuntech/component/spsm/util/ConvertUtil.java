package com.ronxuntech.component.spsm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConvertUtil {

	private static ConvertUtil convertUtil = new ConvertUtil();

	/**
	 * 创建单例
	 * 
	 * @return
	 */
	public static ConvertUtil getInstance() {
		return convertUtil;
	}

	/**
	 * list去重
	 * 
	 * @param list
	 */
	public void removeDuplicateWithOrder(List<String> list) {
		Set<String> set = new HashSet();
		List<String> newList = new ArrayList();
		for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
			String element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		list.clear();
		list.addAll(newList);
	}

	/**
	 * list转换成set(用于去重)
	 * 
	 * @param list
	 * @return
	 */
	public Set<String> listToSet(List<String> list) {
		Set<String> set = new HashSet<>();
		set.addAll(list);
		return set;
	}

	/**
	 * set转换成list;
	 * 
	 * @param set
	 * @return
	 */
	public List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<>();
		list.addAll(set);
		return list;
	}

	/**
	 * stringbuffer to list by ',' 主要用于 得到网页中的附件地址
	 * 
	 * @param sb
	 * @return
	 */
	public List<String> stringToList(String sb) {
		String[] a = sb.split(",");
		List<String> annexUrlList = new ArrayList<>();
		for (int j = 0; j < a.length; j++) {
			annexUrlList.add(a[j]);
		}
		return annexUrlList;
	}
}
