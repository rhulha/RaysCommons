package net.raysforge.commons;

import java.util.ArrayList;
import java.util.HashMap;

public class Generics {

	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
}
