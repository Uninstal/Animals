package org.uninstal.ark.animals.data.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DoubleKeysHashMap<T, K> {

	private Map<T, K> map1 = new HashMap<>();
	private Map<T, K> map2 = new HashMap<>();
	
	public void put(T key1, T key2, K value) {
		map1.put(key1, value);
		map2.put(key2, value);
	}
	
	public boolean containsKey(T key) {
		return map1.containsKey(key)
				|| map2.containsKey(key);
	}
	
	public K getValueFromKey1(T key1) {
		return map1.get(key1);
	}
	
	public K getValueFromKey2(T key2) {
		return map2.get(key2);
	}
	
	public void remove(T key1, T key2) {
		map1.remove(key1);
		map2.remove(key2);
	}
	
	public Collection<K> values(){
		return map1.values();
	}
}
