package net.raysforge.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FullText {

	public static boolean compare(byte[] buf, byte[] phrase, int pos) {
		for (int i = 0; i < phrase.length; i++) {
			if (phrase[i] != buf[(pos++) % buf.length])
				return false;
		}
		return true;
	}

	public static boolean search(File f, byte[] phrase) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return search(fis, phrase);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			StreamUtils.close(fis);
		}
	}

	public static boolean search(InputStream is, byte[] phrase) throws IOException {
		if (!(is instanceof BufferedInputStream)) {
			is = new BufferedInputStream(is);
		}

		byte[] ringbuf = new byte[phrase.length];
		byte[] onebyte = new byte[1];
		int pos = 0;

		while (is.read(onebyte) != -1) {
			ringbuf[pos % ringbuf.length] = onebyte[0];
			pos++;
			if (compare(ringbuf, phrase, pos % ringbuf.length))
				return true;
		}
		return false;
	}

}
