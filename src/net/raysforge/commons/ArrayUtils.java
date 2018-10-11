package net.raysforge.commons;

public class ArrayUtils {

	public static <T> boolean contains(final T[] array, final T v) {
		if (v == null) {
			for (final T e : array)
				if (e == null)
					return true;
		} else {
			for (final T e : array)
				if (e == v || v.equals(e))
					return true;
		}

		return false;
	}
}
