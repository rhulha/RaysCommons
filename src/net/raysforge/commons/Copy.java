package net.raysforge.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class Copy {

	public static void streamAndClose(InputStream is, OutputStream os) throws IOException {
		stream( is, os, null);
		is.close();
		os.close();
	}

	public static void streamAndClose(InputStream is, OutputStream os, AtomicInteger copyProgress) throws IOException {
		stream( is, os, copyProgress);
		is.close();
		os.close();
	}

	public static void stream(InputStream is, OutputStream os, AtomicInteger copyProgress) throws IOException {
		int count;
		byte[] buffer = new byte[8192];
		while ((count = is.read(buffer)) >= 0)
		{
			if( copyProgress != null)
				copyProgress.addAndGet(count);
			os.write(buffer, 0, count);
		}
	}

}
