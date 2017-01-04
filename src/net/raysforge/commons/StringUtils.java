package net.raysforge.commons;

import java.util.List;

public class StringUtils {
	
	public static boolean hasLength(String text) {
		return (text != null && text.length() > 0);
	}

	public static String join(String con, String args[]) {
		if (args.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(args[0]);
		for (int n = 1; n < args.length; n++) {
			sb.append(con);
			sb.append(args[n]);
		}
		return sb.toString();
	}
	
	public static String join(String con, List<String> args) {
		if (args.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(args.get(0));
		for (int n = 1; n < args.size(); n++) {
			sb.append(con);
			sb.append(args.get(n));
		}
		return sb.toString();
	}

	public static String substring(String str, char a, char b)
    {
        return str.substring(str.indexOf(a) + 1, str.indexOf(b));
    }

}