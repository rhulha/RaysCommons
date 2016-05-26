package net.raysforge.commons;

import java.util.ArrayList;

// ignore case für alle funcs ! append

/*

  public class RayString[]
  { //  `pop', `push', `shift', `splice', `unshift', reverse, sort
    public RayString[]
    {
      for(int i=0; i < this.length; i++)
        this[i] = new RayString();
    }

    public RayString join( String con) // darf ich jetzt auf private felder zugreifen ?
    {
      for(int i=0; i < this.length; i++)
        s.o.
    }

    public RayString sort()
    {
      for(int i=0; i < this.length; i++)
    }
  }
  arraycopy(Object source, int sourcePosition, Object destination, int destinationPosition, int numberOfElements)
*/

public class RayString implements java.io.Serializable, Comparable<String>
{
	private static final long serialVersionUID = -6005198056071320035L;
    private char string[];

    public RayString()
    {
        string = new char[0];
    }

    public RayString(char c)
    {
        string = new char[] { c };
    }

    public static char[] arraycopy(char from[])
    {
        char nstr[] = new char[from.length];
        System.arraycopy(from, 0, nstr, 0, from.length);
        return nstr;
    }

    public static void arraycopy(char from[], char to[])
    {
        System.arraycopy(from, 0, to, 0, from.length);
    }

    public static void arraycopy(Object from[], Object to[])
    {
        System.arraycopy(from, 0, to, 0, from.length);
    }

    public RayString(char string[])
    {
        this.string = arraycopy(string);
    }

    // wichtig um funktionen dieser klasse die möglichkeit zu geben, instanzen dieser klasse zurück geben zu können
    // aus schon in der funktion gemachten arrays. ( zB replace );
    // DIESE FUNK KOPIERT NÄMLICH NICHT, junk wegen eindeutigkeit der funk - kann leer sein
    private RayString(char string[], int junk)
    {
        this.string = string;
    }

    public RayString(char str[], int from, int to)
    {
        if (to < from)
            throw new IllegalArgumentException();
        string = new char[to - from];
        System.arraycopy(str, from, string, 0, to - from);
    }

    public RayString(RayString rstring)
    {
        string = new char[rstring.string.length];
        if (rstring.string.length != 0)
            arraycopy(rstring.string, this.string);
    }
    
    public RayString(String string)
    {
        this.string = string.toCharArray();
        //      this.string = new char[length];      string.getChars(0, length, this.string, 0);
    }

    public static ArrayList<RayString> grep(String what, RayString[] rs)
    {
    	ArrayList<RayString> result = Generics.newArrayList();
    	for (RayString rayString : rs) {
			if( rayString.index(what)>=0)
				result.add(rayString);
		}
        return result;
    }

    static int hash(Object x)
    {
        int h = x.hashCode();

        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        h ^= (h >>> 10);
        return h;
    }

    public static RayString join(String Con, RayString args[])
    {
        char con[] = Con.toCharArray();

        int sum = 0;
        int cl = con.length;
        for (int z = 0; z < args.length; z++)
            sum += args[z].string.length + cl;
        sum -= cl;

        char array[] = new char[sum];

        sum = 0;
        System.arraycopy(args[0].string, 0, array, sum, args[0].string.length);
        sum += args[0].string.length;
        for (int z = 1; z < args.length; z++)
        {
            //        System.err.println( new String(args[z].string));
            //        System.err.println( new String(array) );
            System.arraycopy(con, 0, array, sum, cl);
            sum += cl;
            System.arraycopy(args[z].string, 0, array, sum, args[z].string.length);
            sum += args[z].string.length;
        }
        return new RayString(array, 0);
    }

    public void appendInPlace(char str)
    {
        string = join("", new RayString[] { this, new RayString(str)}).string; // .toString().toCharArray();
    }
    public void appendInPlace(char str[])
    {
        string = join("", new RayString[] { this, new RayString(str)}).string;
    }
    public void appendInPlace(RayString str)
    {
        string = join("", new RayString[] { this, str }).string;
    }
    public void appendInPlace(String str)
    {
        string = join("", new RayString[] { this, new RayString(str)}).string;
    }

    // minus gibt von hinten aus
    public char charAt(int index)
    {
        int length = string.length;
        if (index >= string.length)
        {
            throw new IndexOutOfBoundsException();
            //            return 0;
        }
        if (index < 0)
        {
            if (length - index < 0)
                return 0;
            //          throw new StringIndexOutOfBoundsException(index);
            else
                return string[length - index];
        }
        else
        {
            return string[index];
        }
    }

    public int counti(String str)
    {
        int p = 0, c = 0;
        while ((p = indexi(str, p)) != -1)
        {
            p++;
            c++;
        }
        return c;
    }

    public boolean equals(Object o) // WICHTIG !!! die andere equals kann man in die tonne treten :-)
    {
        if (o instanceof RayString)
            return equals((RayString) o);
        else if (o instanceof String)
            return equals((String) o);
        else
            return super.equals(o);
    }

    public boolean equals(RayString rs)
    {
        if (string.length != rs.length())
            return false;
        for (int i = 0; i < string.length; i++)
            if (string[i] != rs.charAt(i))
                return false;
        return true;
    }

    public boolean equals(String s)
    {
        if (string.length != s.length())
            return false;
        for (int i = 0; i < string.length; i++)
            if (string[i] != s.charAt(i))
                return false;
        return true;
    }

    public double getdouble()
    {
        return Double.parseDouble(new String(string));
    }

    public float getfloat()
    {
        return Float.parseFloat(new String(string));
    }

    public int getint()
    {
        return Integer.parseInt(new String(string));
    }

    public long getlong()
    {
        return Long.parseLong(new String(string));
    }
    public int hashCode()
    {
        return new String(string).hashCode();
        /*        int h = hash;
                if (h == 0)
                {
                    for (int i = 0; i < string.length; i++)
                    {
                        h = 31 * h + string[i];
                    }
                    hash = h;
                }
                return h;
                */
    }

    public int index(String str)
    {
        return index(str, 0);
    }

    public int index(String str, int pos)
    {
        char[] chr = str.toCharArray();
        int m = 0;
        int max = string.length;
        for (int p = pos; p < max; p++)
        {
            m = 0;
            while ((p + m < max) && (string[p + m] == chr[m]))
            {
                m++;
                if (m == chr.length)
                    return p;
            }
        }
        return -1;
    }
    public int indexi(String str)
    {
        return indexi(str, 0);
    }

    public int indexi(String str, int pos)
    {
        char[] chr = str.toLowerCase().toCharArray();
        int m = 0;
        int max = string.length;
        for (int p = pos; p < max; p++)
        {
            m = 0;
            while ((p + m < max) && (Character.toLowerCase(string[p + m]) == chr[m]))
            {
                m++;
                if (m == chr.length)
                    return p;
            }
        }
        return -1;
    }

    public boolean isTrue()
    {
        int length = string.length;
        if (length == 0)
            return false;
        if (length == 1)
            if (string[0] == '0')
                return false;
        return true;
    }

    /*********************************/

    public int l()
    {
        return string.length;
    }

    public int length()
    {
        return string.length;
    }

    public RayString replace(String str, String with)
    {
        return join(with, split(str));
    }

    public int size()
    {
        return string.length;
    }

    public RayString[] split(String delim)
    {
        int alt = 0, c = 1, neu = 0; // count
        char chars[];

        while ((alt = index(delim, alt)) != -1)
        {
            alt++;
            c++;
        }
        RayString rs[] = new RayString[c];

        alt = 0;
        neu = 0;
        c = 0;
        int delimlength = delim.length();
        while ((neu = index(delim, alt)) != -1)
        {
            chars = new char[neu - alt];
            System.arraycopy(string, alt, chars, 0, neu - alt);
            rs[c] = new RayString(chars, 0);
            alt = neu + (delimlength);
            c++;
        }
        neu = string.length;
        chars = new char[neu - alt];
        System.arraycopy(string, alt, chars, 0, neu - alt);
        rs[c] = new RayString(chars);

        return rs;
    }

    public RayString[] split(String delim, int count)
    {
        int alt = 0, c = 1, neu = 0; // count
        char chars[];

        int up = 0;

        while ((++up < count) && ((alt = index(delim, alt)) != -1))
        {
            alt++;
            c++;
        }
        RayString rs[] = new RayString[c];

        alt = 0;
        neu = 0;
        c = 0;
        int delimlength = delim.length();
        up = 0;
        while ((++up < count) && ((neu = index(delim, alt)) != -1))
        {
            chars = new char[neu - alt];
            System.arraycopy(string, alt, chars, 0, neu - alt);
            rs[c] = new RayString(chars, 0);
            alt = neu + (delimlength);
            c++;
        }
        neu = string.length;
        chars = new char[neu - alt];
        System.arraycopy(string, alt, chars, 0, neu - alt);
        rs[c] = new RayString(chars);

        return rs;
    }

    public boolean startsWithLetter()
    {
        if (string.length > 0)
            if (Character.isLetter(string[0]))
                return true;
        return false;
    }

    public RayString substringlen(int from, int len)
    {
        char chars[] = new char[len];
        System.arraycopy(string, from, chars, 0, len);
        return new RayString(chars, 0);
    }

    public RayString substringto(int from, int to)
    {
        int len;
        if (to < 0)
            len = string.length + to + 1 - from;
        else if (to < from)
            return new RayString("");
        else
            len = to - from;
        return substringlen(from, len);
    }

    public RayString tagAsString(String tag, int pos)
    {
        int a, b = 0;
        a = index("<" + tag + ">", pos);
        if (a < 0)
            return null;
        a += ("<" + tag + ">").length();
        pos = b = index("</" + tag + ">", a);
        if (b < a)
            return null;
        return substringto(a, b);
    }

    public char[] getCharArray() // better copy ?
    {
        return this.string;
    }

    public String toString()
    {
        if (string.length == 0)
            return "";
        return new String(string);
    }

    public int compareTo(String o)
    {
        return this.toString().compareTo(o.toString());
    }
    
    // ##################
}
