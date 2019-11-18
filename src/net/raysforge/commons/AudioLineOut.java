package net.raysforge.commons;

import javax.sound.sampled.*;

public class AudioLineOut {

	SourceDataLine line;
	AudioFormat audioFormat;

	public AudioLineOut() {
		this(AudioUtils.CD_AUDIO_FORMAT, 16_000);
	}

	public AudioLineOut(AudioFormat audioFormat, int buffSize) {
		DataLine.Info info = null;
		if (buffSize == 0) {
			info = new DataLine.Info(SourceDataLine.class, audioFormat);
		} else {
			info = new DataLine.Info(SourceDataLine.class, audioFormat, buffSize);
		}
		this.audioFormat = audioFormat;

		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void drain() {
		line.drain();
	}

	public int write(byte b[], int len) {
		return line.write(b, 0, len);
	}

	public int write(byte b[], int offset, int len) {
		return line.write(b, offset, len);
	}

	public int write(final byte b[]) {
		return line.write(b, 0, b.length);
	}

	public int writeAsync(final byte b[]) {
		new Thread() {
			public void run() {
				line.write(b, 0, b.length);
			}
		}.start();
		return 0;
	}

	public AudioLineOut openstart() {
		try {
			line.open(audioFormat);
			line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return this;
	}

	public void stopclose() {
		line.stop();
		line.close();
	}

	public int getPos() {
		return line.getFramePosition();
	}

}
