import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class playGameOverSound 
{
	public static void playGameOverSound() {
		try {
	        String soundName = "over.wav";
	        AudioInputStream audioInputStream = null;
	        audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (UnsupportedAudioFileException ex) {
	        Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (LineUnavailableException ex) {
	        Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
}
