package net.raysforge.commons;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class RTFParser {
	public final Reader in;
	private final Writer out;
	private final String codePage;

	public RTFParser(Reader in, Writer out, String codePage) {
		this.in = in;
		this.out = out;
		this.codePage = codePage;
	}

	int read() throws IOException {
		return this.in.read();
	}

	void write(int c) throws IOException {
		if (c != 0)
			this.out.write(c);
		// out.write(Integer.toHexString(c));
	}

	void write(String s) throws IOException {
		this.out.write(s);
	}

	StringBuffer command = new StringBuffer();

	protected String parseCommand() throws IOException {
		this.command.setLength(0);
		int c;

		// first char
		if ((c = read()) != -1) {
			switch (c) {
			case '}':
				write('}');
				return "}";
			case '*':
				return "*";
			case '~':
				write(' ');
				break;
			case '{':
				write('{');
				return "{";
			case '\'':
				String h = parseHex();
				write(h);
				return h;
			case '\\':
				write('\\');
				return "\\";
			default:
				if (Character.isLetterOrDigit((char) c)) {
					this.command.append((char) c);
				} else {
					write(c);
				}
				break;
			}

		}

		while ((c = read()) != -1) {
			switch (c) {
			case ' ':
				//write(' ');
				return this.command.toString();
			case '\r':
				// swallow
				break;
			case '\n':
				return this.command.toString();
			case '}':
				return this.command.toString();
			case '{':
				//parseBrace();
				write(' ');
				return this.command.toString();
			case '\\':
				// swallow, its a new cmd.
				break;
			default:
				if (Character.isLetterOrDigit((char) c)) {
					this.command.append((char) c);
				} else {
					write(c);
				}
				break;
			}
		}
		return this.command.toString();
	}

	char hex[] = new char[2];
	byte decodeHex[] = new byte[1];

	private String parseHex() throws IOException {
		int count = this.in.read(this.hex);
		if (count == -1)
			return "";

		this.decodeHex[0] = (byte) Codecs.decodeHex(new String(this.hex));
		String string = new String(this.decodeHex, this.codePage);
		return string;
	}

	protected void parseBrace() throws IOException {
		int braceCount = 1;
		int c;
		while ((c = read()) != -1) {
			switch (c) {
			case '{':
				braceCount++;
				break;
			case '}':
				braceCount--;
				if (braceCount <= 0)
					return;
			}
		}
	}

	public void parseRTF() throws IOException {
		int c, last = 0;
		// read(); // throw away first '{'.
		try {
			while ((c = read()) != -1) {

				switch (c) {
				case '{':
					// MJB: leave this one out, it really mucks up things
					//parseBrace();
					break;
				case '}':
					if (last == ';')
						write("\r\n");
					break; // throw away last }
				case '\r':
					break;
				case '\n':
					break;
				case '\\':
					String cmd = parseCommand();
					if (cmd.startsWith("par"))
						write("\r\n");
					else if (cmd.startsWith("line"))
						write("\r\n");
					else if (cmd.startsWith("tab"))
						write(" ");
					else if (cmd.startsWith("plain"))
						write(" ");
					else if (cmd.startsWith("cgrid"))
						write(" ");
					else if (cmd.startsWith("pict"))
						parseBrace();
					break;
				default:
					write(c);
					break;
				}
				last = c;
			}
		} finally {
			close();
		}
	}

	public static void main(String[] args) throws IOException {
		//        RTFParser p = new RTFParser(new FileReader("C:\\test2.rtf"), new PrintWriter(System.out), "Windows-1252");
		//        // FileOutputStream fos = new FileOutputStream("C:\\test.txt");
		//        p.parseRTF();
		//        // fos.close();
		//        p.close();

		RTFParser p2 = new RTFParser(new FileReader("C:\\test.rtf"), new FileWriter("C:\\test.rtf.txt"), "Windows-1252");
		// FileOutputStream fos = new FileOutputStream("C:\\test.txt");
		p2.parseRTF();
		// fos.close();
		p2.close();
	}

	private void close() {
		try {
			if (this.in != null)
				this.in.close();
			if (this.out != null)
				this.out.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

}
