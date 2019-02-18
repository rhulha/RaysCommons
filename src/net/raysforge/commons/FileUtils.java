package net.raysforge.commons;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;

public class FileUtils {
	public static void copyFile(String is, String os) throws IOException {

		FileInputStream iss = null;
		FileOutputStream oss = null;
		try {
			iss = new FileInputStream(is);
			oss = new FileOutputStream(os);
			copyFile(iss, oss);
		} finally {
			StreamUtils.close(iss);
			StreamUtils.close(oss);
		}

	}

	public static void copyFile(File is, File os) throws IOException {
		FileInputStream iss = null;
		FileOutputStream oss = null;
		try {
			iss = new FileInputStream(is);
			oss = new FileOutputStream(os);
			copyFile(iss, oss);
		} finally {
			StreamUtils.close(iss);
			StreamUtils.close(oss);
		}
	}

	public static void copyFile(FileInputStream is, FileOutputStream os) throws IOException {
		FileChannel sourceChannel = is.getChannel();
		FileChannel destinationChannel = os.getChannel();
		try {
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null)
				sourceChannel.close();
			if (destinationChannel != null)
				destinationChannel.close();
			StreamUtils.close(is);
			StreamUtils.close(os);
		}
		// or
		// destinationChannel.transferFrom(sourceChannel, 0,
		// sourceChannel.size());
		//        sourceChannel.close();
		//      destinationChannel.close();
	}
	
	public static char[] readCompleteFileUntested(File f) throws IOException {
		FileReader reader = new FileReader(f);
		int len = (int) f.length();
		char buf[] = new char[len];
		int count = 0;
		while(count<len) {
			count += reader.read(buf, count, len-count);
		}
		reader.close();
		return buf;
	}

	public static byte[] readCompleteFile(File f) throws IOException {
		FileInputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(f);
			baos = new ByteArrayOutputStream((int) f.length());
			new Streamer().copy(is, baos);
		} finally {
			StreamUtils.close(is);
		}
		//StreamUtils.streamCopy(is, baos);

		return baos.toByteArray();
	}

	public static StringBuffer readCompleteFile(File f, String codePage) throws IOException {
		InputStream input = new FileInputStream(f);
		Reader r = new InputStreamReader(input, codePage);
		StringWriter sw = new StringWriter();
		Streamer s = new Streamer();
		s.copy(r, sw);
		return sw.getBuffer();
	}

	public static void writeAll(Writer writer, Object s[]) throws IOException {
		for (int i = 0; i < s.length; i++) {
			writer.write("" + s[i]);
		}
	}

	public static void writeFile(File f, StringBuffer buf, String codePage) throws IOException {
		writeFile(f, new ByteArrayInputStream(buf.toString().getBytes(codePage)), true, 16384);
	}

	public static void writeFile(File f, String string, String codePage) throws IOException {
		writeFile(f, new ByteArrayInputStream(string.getBytes(codePage)), true, 16384);
	}

	public static void writeFile(String filename, String text, String codePage) throws IOException {
		writeFile(filename, new ByteArrayInputStream(text.getBytes(codePage)), true, 16384);
	}

	public static void writeFile(String filename, byte buffer[]) throws IOException {
		writeFile(filename, new ByteArrayInputStream(buffer), true, 16384);
	}

	public static void writeFile(String filename, InputStream is) throws IOException {
		writeFile(filename, is, true, 16384);
	}
	public static void writeFile(File file, InputStream is, Streamer streamer) throws IOException {
		writeFile(file, is, true, streamer);
	}

	public static void writeFile(String filename, InputStream is, boolean writeBuffered, int bufferSize) throws IOException {
		Streamer s = new Streamer();
		s.bufferSize=bufferSize;
		writeFile(new File(filename), is, writeBuffered, s);
	}
	public static void writeFile(File file, InputStream is, boolean writeBuffered, int bufferSize) throws IOException {
		Streamer s = new Streamer();
		s.bufferSize=bufferSize;
		writeFile(file, is, writeBuffered, s);
	}
	public static void writeFile(File file, InputStream is, boolean writeBuffered, Streamer streamer) throws IOException {
		OutputStream os = new FileOutputStream(file);
		if (writeBuffered)
			os = new BufferedOutputStream(os);
		try {
			streamer.copy(is, os);
			//StreamUtils.streamCopy(is, os, bufferSize);
		} finally {
			os.close();
		}
	}
	
	public String getFileExtension(File file) {
		String fileName = file.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p &&  i < fileName.length()-1 ) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}

}
