package IHM;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    private Clip clip;

    public void jouerMusique(String cheminFichier) {
        try {
            File fichierAudio = new File(cheminFichier);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Müziği sürekli döndürür
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du son : " + e.getMessage());
        }
    }

    public void arreterMusique() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}