package net.raysforge.commons.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StreamMixer {
	Vector<InputStream> streams;
	Vector<String> names;
	byte stream[];
	int framesize;
	AudioFormat format;
	byte buf[];

	public StreamMixer(int fsize) {
		streams = new Vector<InputStream>();
		names = new Vector<String>();
		framesize = fsize;
//      AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) 
		format = new AudioFormat(44100.0F, 16, 2, true, false);
		stream = new byte[framesize];
		buf = new byte[framesize];
	}

	public void removeAll() {
		// streams.removeAll();
		streams = new Vector<InputStream>();
	}

	public AudioFormat getFormat() {
		return format;
	}

	public int addStream(AudioInputStream ais) {
		streams.add(ais);
		return 0;
	}

	public int addSoundFile(File f, String name) // no chance of seeking to start so always open file again
	{
		try {
			streams.add(AudioSystem.getAudioInputStream(f));
			names.add(name);
			ep("adding " + name + ": streams.size: " + streams.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return streams.size() - 1;

	}

	public int addInterpolatedSoundFile(File f, String name, float p) // no chance of seeking to start so always open file again
	{
		try {
			streams.add(new Interpolator(f, p));
			names.add(name);
			ep("adding " + name + ": streams.size: " + streams.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return streams.size() - 1;
	}

	public int addAndConvertSoundFile(File f) throws IOException, UnsupportedAudioFileException {
		AudioFormat targetFormat = new AudioFormat(44100.0F, 16, 2, true, false);

		streams.add(AudioSystem.getAudioInputStream(targetFormat, AudioSystem.getAudioInputStream(f)));
		return 0;
	}

	public byte[] getNextFrame() throws IOException {
		AudioInputStream ais;
		int res;
		int i = 0;

		if (streams.size() > i) {
			ais = (AudioInputStream) streams.get(i);
			ep("reading: " + names.get(i));
			res = ais.read(buf, 0, framesize);
			if (res == -1) {
				ep("removing: " + names.get(i));
				streams.remove(i);
				names.remove(i);
			} else {
				i = 1;
			}

			int b = 0;
			for (; b < res; b++) {
				if (b % 2 == 1) {
					int s = (buf[b] << 8) + (buf[b - 1] & 0xff);
					s /= 4;
					stream[b] = (byte) (((s >> 8) & 0xff));
					stream[b - 1] = (byte) ((s & 0xff));
					if (false && (stream[b] != buf[b]) && (stream[b - 1] != buf[b - 1])) {
						ep(Integer.toBinaryString(buf[b] & 0xff) + " " + Integer.toBinaryString(buf[b - 1] & 0xff));
						ep(Integer.toBinaryString(s & 0xffff) + "");
						ep(Integer.toBinaryString(stream[b] & 0xff) + " " + Integer.toBinaryString(stream[b - 1] & 0xff));
						System.exit(0);
					}
				}
			}

			for (; b < framesize; b++) {
				stream[b] = 0; // kill rest ( signed ! )
			}
		} else {
			return new byte[framesize];
		}

		while (i < streams.size()) {
			ais = (AudioInputStream) streams.get(i);
			ep("reading: " + names.get(i));
			res = ais.read(buf, 0, framesize);
			if (res == -1) {
				streams.remove(i);
				ep("removing: " + names.get(i));
				continue;
			} else {
				i++;
			}

			for (int b = 0; b < res; b++) {
				if (b % 2 == 1) {
					int s = (buf[b] << 8) + (buf[b - 1] & 0xff);
					s /= 4;
					s += (stream[b] << 8) + (stream[b - 1] & 0xff);
					stream[b] = (byte) (((s >> 8) & 0xff));
					stream[b - 1] = (byte) ((s & 0xff));
				}
			}
		}
		return stream;
	}

	public void ep(String s) {
		System.err.println(s);
	}

	public void p(String s) {
		System.out.println(s);
	}

}
