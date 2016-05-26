package net.raysforge.commons;

public class StringUtils {
	
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
	
}