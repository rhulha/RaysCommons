package net.raysforge.commons;

import java.util.*;
import java.util.Map.Entry;

public class Generics {

	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> void printMap(Map<K, V> map) {
		Set<Entry<K, V>> entrySet = map.entrySet();
		for (Entry<K, V> entry : entrySet) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}

}
