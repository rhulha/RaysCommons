package net.raysforge.commons;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.Map.Entry;

// Warming: This class is not heavily tested. I just like to write parsers in my spare time... 
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
		return readUntil(r, ch, false);
	}

	private String readUntil(Reader r, char ch, boolean checkForQuote) throws IOException {
		StringBuffer sb = new StringBuffer();
		int i;
		boolean lastCharWasQuote = false;
		while ((i = r.read()) >= 0) {
			char c = (char) i;
			if (checkForQuote) {
				if (!lastCharWasQuote && c == ch)
					break;

			} else {
				if (c == ch)
					break;
			}
			if (checkForQuote) {
				if (lastCharWasQuote) {
					switch (c) {
						case '"':
							sb.append('"');
							break;
						case 'r':
							sb.append('\r');
							break;
						case 'n':
							sb.append('\n');
							break;
						case 't':
							sb.append('\t');
							break;
						case 'b':
							break;
						case 'f':
							break;
					}
				} else if (c == '\\') {
					// do nothing
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
			lastCharWasQuote = c == '\\';
		}
		return sb.toString();
	}

	private boolean readExact(Reader r, String str) throws IOException {
		for (int i = 0; i < str.length(); i++) {
			if ((char) r.read() != str.charAt(i))
				return false;
		}
		return true;
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
				return readUntil(r, '"', true);
			else if (c == 't') {
				if (readExact(r, "rue")) // true
					return true;
				else
					throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
			} else if (c == 'n') {
				if (readExact(r, "ull")) // null
					return true;
				else
					throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
			} else if (c == 'f') {
				if (readExact(r, "alse")) // false
					return false;
				else
					throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
			} else if (Character.isDigit(c) || (c == '-'))
				return parseDouble(c, r);
			else if (Character.isWhitespace(c))
				continue;
			else
				throw new RuntimeException("unsupported char at: " + c + StreamUtils.readCompleteReader(r));
		}
		return null;
	}

	public static String toJsonString(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		boolean first = true;

		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("\"" + entry.getKey() + "\":");
			appendValue(sb, entry.getValue());
		}

		sb.append("}");
		return sb.toString();
	}

	public static String toJsonString(List<Object> array) {
		StringBuffer sb = new StringBuffer();
		appendValue(sb, array);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static void appendValue(StringBuffer sb, Object value) {
		if (value instanceof String) {
			sb.append("\"" + value + "\""); // TODO QUOTE
		} else if (value instanceof Map<?, ?>) {
			sb.append(toJsonString((Map<String, Object>) value));
		} else if (value instanceof List<?>) {
			List<?> l = (List<?>) value;
			sb.append("[");
			String delimiter = "";
			for (Object o : l) {
				sb.append(delimiter);
				appendValue(sb, o);
				delimiter = ",";
			}
			sb.append("]");
		} else if (value instanceof Double || value instanceof Long || value instanceof Boolean || value instanceof Integer) {
			sb.append(value);
		} else
			throw new RuntimeException("unsupported type: " + value.getClass());
	}
	
	// example path: "$.store.book[0].author"
	@SuppressWarnings("unchecked")
	public static Object getJsonPath(Object jsonObject, String path) {

		if (!path.startsWith("$."))
			throw new RuntimeException("Error: JsonPath path must start with '$.'");
		String parts[] = path.substring(2).split("\\.");
		for (String part : parts) {
			if (part.endsWith("]")) {
				String nr = part.substring(part.indexOf('[') + 1, part.indexOf(']'));
				part = part.substring(0, part.indexOf('['));
				List<Object> list = (List<Object>) ((Map<String, Object>) jsonObject).get(part);
				jsonObject = list.get(Integer.parseInt(nr));
			} else {
				jsonObject = ((Map<String, Object>) jsonObject).get(part);
			}
		}
		return jsonObject;
	}

	public static void main(String[] args) throws IOException {
		String json = "{ \"store\": {\r\n" + "    \"book\": [ \r\n" + "      { \"category\": \"reference\",\r\n" + "        \"author\": \"Nigel Rees\",\r\n" + "        \"title\": \"Sayings of the Century\",\r\n" + "        \"price\": 8.95\r\n" + "      },\r\n"
				+ "      { \"category\": \"fiction\",\r\n" + "        \"author\": \"Evelyn Waugh\",\r\n" + "        \"title\": \"Sword of Honour\",\r\n" + "        \"price\": 12.99\r\n" + "      },\r\n" + "      { \"category\": \"fiction\",\r\n"
				+ "        \"author\": \"Herman Melville\",\r\n" + "        \"title\": \"Moby Dick\",\r\n" + "        \"isbn\": \"0-553-21311-3\",\r\n" + "        \"price\": 8.99\r\n" + "      },\r\n" + "      { \"category\": \"fiction\",\r\n"
				+ "        \"author\": \"J. R. R. Tolkien\",\r\n" + "        \"title\": \"The Lord of the Rings\",\r\n" + "        \"isbn\": \"0-395-19395-8\",\r\n" + "        \"price\": 22.99\r\n" + "      }\r\n" + "    ],\r\n" + "    \"bicycle\": {\r\n"
				+ "      \"color\": \"red\",\r\n" + "      \"price\": 19.95\r\n" + "    }\r\n" + "  }\r\n" + "}";

		Object parsed = new Json().parse(new StringReader(json));
		Object result = getJsonPath(parsed, "$.store.book[1].author");
		System.out.println("result: " + result);

		String s = "Hello \\\"World\\\" shoudBeIncluded\"shouldBeIgnored";
		StringReader r = new StringReader(s);
		System.out.println(new Json().readUntil(r, '"', false));
		r = new StringReader(s);
		System.out.println(new Json().readUntil(r, '"', true));
	}
}
