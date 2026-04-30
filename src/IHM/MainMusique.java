package IHM;

import javax.sound.sampled.*;
import java.io.File;

public class MainMusique {

    private Clip clip;

    public void jouerMusique(String cheminFichier) {
        try {
            File fichierAudio = new File(cheminFichier);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Volume de la musique de fond
            // 0.0f = volume normal
            // -10.0f = plus bas
            // -15.0f = assez discret
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-40.0f);

            // La musique tourne en boucle
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture de la musique : " + e.getMessage());
        }
    }

    public void arreterMusique() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}