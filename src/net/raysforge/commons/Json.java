package net.raysforge.commons;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Json {

	private Map<String, Object> parseObject(Reader r) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        int i;
        while ((i = r.read()) >= 0) {
            char c = (char) i;
            if (Character.isWhitespace(c))
                continue;
            if (c == '}')
                break;
            if (c != '"')
                readUntil(r, '"');
            String name = readUntil(r, '"');
            readUntil(r, ':');
            if (name != null && name.length() > 0)
                map.put(name, parse(r));
            else
                Thread.dumpStack();
        }
        return map;
    }

    private List<Object> parseArray(Reader r) throws IOException {
        List<Object> list = new ArrayList<Object>();
        while (true) {
            Object v = parse(r);
            if (v == arrayEndIndicator)
                break;
            if (v == commaIndicator)
                continue;
            list.add(v);
        }
        return list;
    }
    
    private Double parseDouble(char c, Reader r) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(c);
        int i;
        while ((i = r.read()) >= 0) {
            c = (char) i;
            if (Character.isDigit(c) || (c == '-') || (c == '.'))
                sb.append(c);
            else
                break;
        }
    	return Double.parseDouble(sb.toString());
    }

    private String readUntil(Reader r, char ch) throws IOException {
        StringBuffer sb = new StringBuffer();
        int i;
        while ((i = r.read()) >= 0) {
            char c = (char) i;
            if (c == ch)
                break;
            sb.append(c);
        }
        return sb.toString();
    }

    Object arrayEndIndicator = new Object();
    Object commaIndicator = new Object();

    public Object parse(Reader r) throws IOException {
        int i;
        while ((i = r.read()) >= 0) {
            char c = (char) i;
            if (c == '{')
                return parseObject(r);
            else if (c == '[')
                return parseArray(r);
            else if (c == ']')
                return arrayEndIndicator;
            else if (c == ',')
                return commaIndicator;
            else if (c == '"')
                return readUntil(r, '"');
            else if (c == 't') {
            	String s = readUntil(r, 'e'); // true
            	if("ru".equals(s))
            		return true;
            	else throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
            }
            else if (c == 'n') {
            	String s = readUntil(r, 'l'); // null
            	if("ul".equals(s))
            		return null;
            	else throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
            }
            else if (c == 'f') {
            	String s = readUntil(r, 'e'); // false
            	if("als".equals(s))
            		return false;
            	else throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
            }
            else if (Character.isDigit(c) || (c == '-'))
                return parseDouble(c, r);
            else if (Character.isWhitespace(c))
                continue;
            else throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public static String toJsonString(Map<String, Object> map) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{");
    	
    	boolean first = true;
    	
    	Set<Entry<String, Object>> entrySet = map.entrySet();
    	for (Entry<String, Object> entry : entrySet) {
    		if(first) {
    			first = false;
    		} else {
    			sb.append(",");
    		}
			sb.append("\""+entry.getKey()+"\":");
			if( entry.getValue() instanceof String) {
				sb.append("\""+entry.getValue()+"\"");
			}
			else if( entry.getValue() instanceof Map<?,?>) {
				sb.append(toJsonString((Map<String, Object>) entry.getValue()));
			}
			else if( entry.getValue() instanceof Double) {
				sb.append(entry.getValue());
			}
			else throw new RuntimeException("unsupported type: " + entry.getValue().getClass());
		}
    	
    	sb.append("}");
    	return sb.toString();
    }
}
