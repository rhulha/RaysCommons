package net.raysforge.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Streamer {
	public final static int DEFAULT_BUFFER_SIZE = 16384;
	public int bufferSize = DEFAULT_BUFFER_SIZE;
	public String hashMethod = null;
	public boolean compress = false;
	public boolean decompress = false;
	public boolean closeInputStream = true;
	public boolean closeOutputStream = true;
	public long maxBytesToRead = -1;

	public String calculatedHash = null;

	public long count = 0;

	public Streamer() {
	}

	public Streamer(String hashMethod) {
		this.hashMethod = hashMethod; // cannot be used for reader/writers
	}
	
	public Streamer(boolean compress, boolean decompress, boolean closeStreams) {
		this.compress = compress;
		this.decompress = decompress;
		this.closeInputStream = closeStreams;
		this.closeOutputStream = closeStreams;
	}

	public Streamer(int bufferSize, String hashMethod, boolean compress, boolean decompress, boolean closeStreams) {
		this.bufferSize = (bufferSize > 0) ? bufferSize : DEFAULT_BUFFER_SIZE;
		this.hashMethod = hashMethod;
		this.compress = compress;
		this.decompress = decompress;
		this.closeInputStream = closeStreams;
		this.closeOutputStream = closeStreams;
	}

	public void copy(Reader r, Writer w) throws IOException {
		if (this.decompress || this.compress)
			throw new UnsupportedOperationException("compression not supported by Readers/Writers.");

		if (this.hashMethod != null)
			throw new UnsupportedOperationException("hashing not supported by Readers/Writers.");

		// Buffer set up
		char[] buf = new char[this.bufferSize];
		int i = 0;

		try {
			if (this.maxBytesToRead > 0) {
				while (this.count < this.maxBytesToRead) {
					// Read into buffer
					// Math.min will return at max the value buf.length
					// that is guaranteed to be int, so we can cast safely back.
					i = (int) Math.min(buf.length, this.maxBytesToRead - this.count);
					i = r.read(buf, 0, i);
					if (i == -1)
						break;
					w.write(buf, 0, i);
					this.count += i;
				}

			} else // we have exceed the max
			{
				while ((i = r.read(buf)) != -1) {
					w.write(buf, 0, i);
					this.count += i;
				}
			}
		} finally {
			if (this.closeInputStream)
				StreamUtils.close(r);
			if (this.closeOutputStream)
				StreamUtils.close(w);
		}

	}

	public void copy(InputStream pis, OutputStream pos) throws IOException {
		this.count = 0;

		byte[] buf = new byte[this.bufferSize];
		MessageDigest md = null;
		int i = 0;
		InputStream is = pis;
		OutputStream os = pos;
		try {
			if (this.decompress)
				is = new GZIPInputStream(is);
			if (this.compress)
				os = new GZIPOutputStream(os);

			if (this.hashMethod != null) {
				try {
					md = MessageDigest.getInstance(this.hashMethod);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
				is = new DigestInputStream(is, md);
			}

			if (this.maxBytesToRead > 0) {
				while (this.count < this.maxBytesToRead) {
					// Math.min will return at max the value buf.length
					// that is guaranteed to be int, so we can cast safely back.
					i = (int) Math.min(buf.length, this.maxBytesToRead - this.count);
					i = is.read(buf, 0, i);
					if (i == -1)
						break;
					os.write(buf, 0, i);
					this.count += i;
				}

			} else {
				while ((i = is.read(buf)) != -1) {
					os.write(buf, 0, i);
					this.count += i;
				}
			}
		} finally {
			if (this.closeOutputStream)
				StreamUtils.close(os);
			if (this.closeInputStream)
				StreamUtils.close(is);
		}

		if ((this.hashMethod != null) && (md != null))
			this.calculatedHash = Codecs.encodeHex(md.digest());
		else
			this.calculatedHash = "";

	}
}
