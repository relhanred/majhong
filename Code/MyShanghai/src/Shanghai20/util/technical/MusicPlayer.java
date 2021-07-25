package Shanghai20.util.technical;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Shanghai20.UserSave;
import Shanghai20.util.Contract;

public class MusicPlayer {

	// ATTRIBUT
	
	private Clip clip;
	
	// CONSTRUCTEUR
	
	public MusicPlayer(URL path) {
		Contract.checkCondition(path != null);
		
		clip = null;
		try {
			AudioInputStream audioIn = null;
			audioIn = AudioSystem.getAudioInputStream(path);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		setVolume(50);
	}
	
	// REQUETE
	
	public int getVolume() {
		int value = 0;
		FloatControl volume = 
				(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    if (volume != null) {
	    	value = (int) (Math.pow(10f, volume.getValue() / 20f) * 100.0f);
	    }
	    return value;
	}
	
	// COMMANDE
	
	public void setVolume(int level) {
		Contract.checkCondition(level >= 0 && level <= 100);
		
	    FloatControl volume = 
	    		(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    if (volume != null) {
	        volume.setValue(20f * (float) Math.log10(level / 100.0f));     
	    }
	}
	
	public MusicPlayer play() {
		if (!UserSave.isMuted) {
			clip.setFramePosition(0);
			clip.start();
		}
		return this;
	}
	
	public MusicPlayer loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		return this;
	}
	
	public MusicPlayer pause() {
		clip.stop();
		return this;
	}
}
