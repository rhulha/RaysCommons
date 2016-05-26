/*
 * Created on 02.05.2006
 */
package net.raysforge.commons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class Codecs
{
	public static String toBase64(byte[] val) {
		return DatatypeConverter.printBase64Binary(val);
	}
	
	public static byte[] fromBase64(String s) {
		return DatatypeConverter.parseBase64Binary(s);
	}
	
    private static final char[] UCDIGITS =
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static final char[] LCDIGITS =
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String encodeHex(String data, String charsetName) throws UnsupportedEncodingException
    {
        return encodeHex(data.getBytes(charsetName), UCDIGITS);
    }

    public static String encodeHex(byte[] data)
    {
        return encodeHex(data, UCDIGITS);
    }

    public static String encodeHexLowerCase(byte[] data)
    {
        return encodeHex(data, LCDIGITS);
    }

    public static String encodeHex(byte[] data, char[] DIGITS)
    {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++)
        {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return new String(out);
    }

    public static byte[] decodeHexToByteArray(String hexStr) throws IOException
    {

        char[] charArray = hexStr.toLowerCase().toCharArray();
        byte[] bytes = new byte[charArray.length/2];

        int l = hexStr.length();
        if (l % 2 != 0)
            throw new IOException("hexStr.length() % 2 != 0");
        int x=0;
        for (int i = 0; i < charArray.length; i++)
        {
            char c = charArray[i++];
            char c2 = charArray[i];
            int h1 = Arrays.binarySearch(LCDIGITS, c);
            int h2 = Arrays.binarySearch(LCDIGITS, c2);
            bytes[x++] = (byte)((h1<<4)+h2);

        }
        return bytes;

    }

    // see maybe also: http://david.tribble.com/src/java/tribble/util/Hex.java
    public static int decodeHex(String hexStr)
    {
        // hexStr = hexStr.toLowerCase();

        // int l = hexStr.length();
        // if( l % 2 != 0)
        // throw new IOException("hexStr.length() % 2 != 0");

        return Integer.parseInt(hexStr, 16);
    }

    public static char[] encodeHexLowerCaseToChars(byte[] data)
    {
        return encodeHexToChars(data, LCDIGITS);
    }

    public static char[] encodeHexToChars(byte[] data, char[] DIGITS)
    {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++)
        {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

    public static String encodeHexTest(String data)
    {
        StringBuffer s =new StringBuffer();
        for (int i = 0; i < data.length(); i++)
        {
            //logger.debug("" + (0 + data.charAt(i)));
            // s += Integer.toHexString( data.charAt(i) ).substring(0,2);
            s.append(Integer.toHexString(0xff & data.charAt(i)));
        }
        return s.toString();
    }

  
}