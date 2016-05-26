package net.raysforge.commons;

import java.lang.reflect.Array;
import java.text.Collator;

public class Service {

	public static Object[] arrayCopy(Object[] src) {
		Object dest = Array.newInstance(src.getClass().getComponentType(), src.length);
		System.arraycopy(src, 0, dest, 0, src.length);
		return (Object[]) dest;
	}

	public static void arrayCopy(Object[] src, Object[] dest) {
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	public static boolean hasLength(Object text) {
		return (text != null && text.toString().length() > 0);
	}

	public static Object or(Object a, Object b) {
		return (a == null ? b : a);
	}

	public static boolean equals(String s, String t) {
		if ((s == null) || t == null) {
			if ((s == null) && t == null)
				return true;

			return false;
		}

		return Collator.getInstance().equals(s, t);
	}
}
