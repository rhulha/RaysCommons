package snd;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.raysforge.commons.AudioUtils;

public class TestAudioUtils {
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		//Sounds s = new Sounds();
		
		ByteBuffer sandman = AudioUtils.readAudioFile(new File("C:\\Users\\Ray\\Downloads\\sandman", "sandman.wav"));
		
		AudioUtils.playAudioFile(sandman, 0);
		
	}

}
