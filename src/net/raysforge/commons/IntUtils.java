package net.raysforge.commons;

public class IntUtils {

	public final static int const16K = 16 * 1024;
    public final static int const1MB = 1024 * 1024;
    
    public static int threeBytesToInt(byte bytes[])
    {
        return ((0xFF & bytes[0]) << 16) + ((0xFF & bytes[1]) << 8) + (0xFF & bytes[2]);
    }
    
    // earlier bytes are more significant
    public static int bytesToInt(byte bytes[])
    {
        int res = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            res += ((0xFF & b) << (8*(bytes.length-i-1)));
        }
        return res;
    }
    
	/*
	Einschränkungen:
	sei prim_m*prim_n = prim_produkt

	so sind:
	m und n ungerade,
	m < wurzel(produkt) < n

	*/
	public static int[] factor(int product) {
		int root = (int) Math.sqrt(product) + 1;

		for (int m = root; m > 1; m -= 2) {
			for (int n = root; n < (product / 2); n += 2) {
				if ((m * n) == product) {
					return new int[] { m, n };
				}
			}
		}
		return new int[0];
	}

	public static int parseInt(String s) throws NumberFormatException {
		return parseInt(s, 10);
	}

	// parse as much as we can. "123hello" will return 123

	public static int parseInt(String s, int radix) throws NumberFormatException {
		if (s == null || s.length() == 0) {
			return 0;
		}

		if (radix < Character.MIN_RADIX) {
			throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
		}

		if (radix > Character.MAX_RADIX) {
			throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
		}

		int result = 0;
		boolean negative = false;
		int i = 0, max = s.length();
		int limit;
		int multmin;
		int digit;

		if (s.charAt(0) == '-') {
			negative = true;
			limit = Integer.MIN_VALUE;
			i++;
		} else {
			limit = -Integer.MAX_VALUE;
		}
		multmin = limit / radix;
		if (i < max) {
			digit = Character.digit(s.charAt(i++), radix);
			if (digit < 0) {
				return 0;
			} else {
				result = -digit;
			}
		}
		while (i < max) {
			// Accumulating negatively avoids surprises near MAX_VALUE
			digit = Character.digit(s.charAt(i++), radix);
			if (digit < 0) {
				break;
			}
			if (result < multmin) {
				break;
			}
			result *= radix;
			if (result < limit + digit) {
				break;
			}
			result -= digit;
		}
		if (negative) {
			if (i > 1) {
				return result;
			} else { /* Only got "-" */
				return 0;
			}
		} else {
			return -result;
		}
	}
	
    public static String formatNumber(long number) {
        String formatted;
        if (number > 1000000000) {
            formatted = Long.toString(number / 1000000000) + " GB";
        } else if (number > 1000000) {
            formatted = Long.toString(number / 1000000) + " MB";
        } else if (number > 1000) {
            formatted = Long.toString(number / 1000) + " KB";
        } else {
            formatted = Long.toString(number) + " bytes";
        }
        return (formatted);

    }


	public static void main(String[] args) {
		System.out.println(parseInt("123hello"));
	}
}
