package net.raysforge.commons.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Interpolator extends InputStream {
	byte mybuf[];
	AudioInputStream ais;
	float f, sum;

	public Interpolator(File file, float factor) {
		try {
			ais = AudioSystem.getAudioInputStream(file);
			f = factor;
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public int read(byte[] buf, int a, int len) throws IOException {
		int newlen = (int) (len * f);
		mybuf = new byte[newlen];
		int stat = ais.read(mybuf, a, newlen);
		if (stat > 0) {
			float x2, y1, y2, y3;
			int x1, x3;

			sum = 0;
			for (int t = 0; t < stat; t++) {
				sum += f;
				x1 = (int) Math.floor(sum);
				x3 = (int) Math.ceil(sum);
				y1 = mybuf[x1];
				y3 = mybuf[x3];
				x2 = sum;

				y2 = ((y3 - y1) / (x3 - x1)) * (x2 - x1) + y1;
				mybuf[t] = (byte) y2;
			}

		}
		return stat;
	}

	@Override
	public int read() throws IOException {
		throw new IOException("unimplemented");
	}
}
