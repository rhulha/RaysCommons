package net.raysforge.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/*
 * Example:
 * 		StreamConverterThread sct = new StreamConverterThread(msg.getInputStream(), new Streamer(true, false, true));
		sct.start();
		GridFSInputFile file = gridFS.createFile(sct.pipedInputStream);
 */

public class StreamConverterThread extends Thread {

	public PipedInputStream pipedInputStream = new PipedInputStream();
	public PipedOutputStream pipedOutputStream;
	private InputStream inputStream;
	private Streamer streamer;

	public StreamConverterThread(InputStream inputStream, Streamer streamer) {
		this.inputStream = inputStream;
		this.streamer = streamer;
		try {
			pipedOutputStream = new PipedOutputStream(pipedInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		try {
			streamer.copy(inputStream, pipedOutputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
