package net.raysforge.commons;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

public class StreamUtils {

	public static void close(InputStream is) {
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
			}
	}

	public static void close(OutputStream is) {
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
			}
	}

	public static void close(Writer w) {
		if (w != null)
			try {
				w.close();
			} catch (IOException e) {
			}
	}

	public static void close(Channel c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(Reader r) {
		if (r != null)
			try {
				r.close();
			} catch (IOException e) {
			}
	}

	// this method is dangerous, as it may buffer more bytes than neccessary and
	// thus swallow bytes.
	public static String readOneBufferedLineOfInputStream(InputStream is, String charSet) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(is, charSet));
		/*
		 * StringBuffer sb = new StringBuffer(); char buffer[] = new
		 * char[0xffff]; int nchars;
		 */
		String line = r.readLine();

		/*
		 * while ( (nchars = r.read(buffer)) != -1 ) sb.append( buffer, 0,
		 * nchars);
		 */
		is.close();
		// return sb.toString();
		return line;
	}

	public static String readOneLineISO88591(InputStream is) throws IOException {
		return readOneLineOfInputStream(is, Charset.forName("ISO-8859-1"));
	}

	public static String readOneLineOfInputStream(InputStream is, Charset cs) throws IOException {
		// local variable is threadsafe (2048 is not too big for local field)
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int ch = 0;
		int count = 0;
		while ((ch = is.read()) != -1) {
			count++;
			if (ch == '\r')
				continue;
			if (ch == '\n')
				break;
			baos.write(ch);
		}
		// return null, in case we got called at the end of the stream already.
		if (count == 0)
			return null;
		return baos.toString(cs.name());
	}

	public static byte[] readOneLineOfInputStream(InputStream is) throws IOException {
		// local variable is threadsafe (2048 is not too big for local field)
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int ch = 0;
		int count = 0;
		while ((ch = is.read()) != -1) {
			count++;
			if (ch == '\r')
				continue;
			if (ch == '\n')
				break;
			baos.write(ch);
		}
		// return null, in case we got called at the end of the stream already.
		if (count == 0)
			return null;
		return baos.toByteArray();
	}

	public static String readCompleteInputStream(InputStream is, String charset) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(is, charset));
		StringBuffer sb = new StringBuffer();
		char buffer[] = new char[0xffff];
		try {
			for (int nchars; (nchars = r.read(buffer)) != -1;)
				sb.append(buffer, 0, nchars);
		} finally {
			r.close();
		}
		return sb.toString();
	}

	public static ByteArrayOutputStream readCompleteInputStream(InputStream is) throws IOException {
		return readCompleteInputStream(is, 1024);
	}

	public static ByteArrayOutputStream readCompleteInputStream(InputStream is, int bufferSize) throws IOException {
		return readCompleteInputStream(is, bufferSize, false);
	}

	public static ByteArrayOutputStream readCompleteInputStream(InputStream is, int bufferSize, boolean gzip) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize * 16);
		OutputStream os = baos;
		try {
			if (gzip)
				os = new GZIPOutputStream(baos);
			byte[] buf = new byte[bufferSize];
			int i = 0;
			while ((i = is.read(buf)) != -1) {
				os.write(buf, 0, i);
			}

		} finally {
			close(is);
			close(os);
			// closing a ByteArrayOutputStream is a no-op.
		}

		return baos;
	}

	public static void NIOStreamCopy(String srcFile, String destFile) throws FileNotFoundException, IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			inputChannel = fis.getChannel();
			outputChannel = fos.getChannel();
			inputChannel.transferTo(0, inputChannel.size(), outputChannel);
		} finally {
			close(fis);
			close(fos);
			close(inputChannel);
			close(outputChannel);
		}
	}

	public static String readCompleteReader(Reader r) throws IOException {
		int BUF_CAP = 65536;
		char[] cbuf = new char[BUF_CAP];
		StringBuffer stringbuf = new StringBuffer(BUF_CAP);
		try {
			if (r != null) {
				int read_this_time = 0;
				while (read_this_time != -1) {
					read_this_time = r.read(cbuf, 0, BUF_CAP);
					if (read_this_time > 0)
						stringbuf.append(cbuf, 0, read_this_time);
				} //end while
			}
		} finally {
			close(r);
		}
		return stringbuf.toString();
	}

	public InputStream dumpInputStream(InputStream is, String tempFilePrefix) throws IOException {
		File createTempFile = File.createTempFile(tempFilePrefix, null);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(createTempFile);
			new Streamer().copy(is, fos);
		} finally {
			StreamUtils.close(fos);
		}
		return new FileInputStream(createTempFile);
	}

}
