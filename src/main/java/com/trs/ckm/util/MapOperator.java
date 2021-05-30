package com.trs.ckm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class MapOperator {
	
	private MapOperator(){}
	/**
	 * 安全获取map中的value
	 * @param map
	 * @param key
	 * @param resultWhenICannotGetValue 当获取指定value失败时的返回值, 此值由使用者决定
	 * @return T
	 */
	public static <T> T safetyGet(Map<String, T> map, String key, T resultWhenICannotGetValue){
		if(map == null || map.isEmpty() || map.size() == 0 || key == null || "".equals(key.trim()))
			return resultWhenICannotGetValue;
		if(!map.containsKey(key))
			return resultWhenICannotGetValue;
		return map.get(key);
	}
	
	/**
	 * 自定义排序
	 * @param map 待排序的map
	 * @param comparator 比较器
	 * @return LinkedHashMap&ltString, Double&gt
	 */
	public static <T> LinkedHashMap<String, Object> customSort(Map<String, T> map, Comparator<Entry<String, T>> comparator){
		ArrayList<Map.Entry<String, T>> list = new ArrayList<Map.Entry<String, T>>(map.entrySet());
		Collections.sort(list, comparator);
		LinkedHashMap<String, Object> newMap = new LinkedHashMap<String, Object>();
		for(int i=list.size()-1; i>=0; i--)
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
        return newMap;  
	}
}
