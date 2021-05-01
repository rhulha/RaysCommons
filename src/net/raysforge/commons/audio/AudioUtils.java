package net.raysforge.commons.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import net.raysforge.commons.StreamUtils;

//AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) 
//AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);

public class AudioUtils {

	public static final AudioFormat CD_AUDIO_FORMAT = new AudioFormat(44100.0F, 16, 2, true, false);

	public static ByteBuffer readAudioFile(File file) throws UnsupportedAudioFileException, IOException {
		return readAudioFile(AudioSystem.getAudioInputStream(file));
	}

	public static ByteBuffer readAudioFile(AudioInputStream ais) throws UnsupportedAudioFileException, IOException {
		if (!ais.getFormat().matches(CD_AUDIO_FORMAT)) {
			ais = AudioSystem.getAudioInputStream(CD_AUDIO_FORMAT, ais);
		}
		
		ByteArrayOutputStream baos = StreamUtils.readCompleteInputStream(ais);
		
		return ByteBuffer.wrap(baos.toByteArray()).order(ByteOrder.LITTLE_ENDIAN);
	}

	public static void mix(ByteBuffer song, ByteBuffer sound, int offset, float volume) {
		mix(song.asShortBuffer(), sound.asShortBuffer(), offset, volume);
	}

	public static void mix(ShortBuffer song, ShortBuffer sound, int offset, float volume) {
		for (int i = 0; i < sound.remaining(); i++) {
			short v = (short) (song.get(offset + i) * volume + sound.get(i) * volume);
			song.put(offset + i, v);
		}
	}

	public static void playAudioFile(ByteBuffer audio, int offset) throws IOException {
		AudioLineOut lineOut = new AudioLineOut().openstart();
		lineOut.write(audio.array(), offset, audio.capacity() - offset);
		lineOut.drain();
		lineOut.stopclose();
	}

	public static void playAudioFile(byte[] audio, int offset) throws IOException {
		AudioLineOut lineOut = new AudioLineOut().openstart();
		lineOut.write(audio, offset, audio.length - offset);
		lineOut.drain();
		lineOut.stopclose();
	}

	void checkFormat(AudioInputStream ais) {
		if (!ais.getFormat().getEncoding().equals(Encoding.PCM_SIGNED)) {
			System.out.println("Encoding not equals(Encoding.PCM_SIGNED");
			System.exit(1);
		}

		if (ais.getFormat().getChannels() != 2) {
			System.out.println("Channels() != 2");
			System.exit(1);
		}
		if (ais.getFormat().isBigEndian()) {
			System.out.println("Format isBigEndian");
			System.exit(1);
		}
		if (ais.getFormat().getSampleSizeInBits() != 16) {
			System.out.println("SampleSizeInBits != 16");
			System.exit(1);
		}
	}

	public static AudioLineOut getAudioLineOut() {
		return new AudioLineOut();
	}

	public static void saveAsWav(ByteBuffer audio, String fileName) throws IOException {
		saveAsWav(audio.array(), fileName);
	}

	public static int saveAsWav(byte[] audio, String fileName) throws IOException {
		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(audio), CD_AUDIO_FORMAT, audio.length);
		return AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(fileName));
	}

}
