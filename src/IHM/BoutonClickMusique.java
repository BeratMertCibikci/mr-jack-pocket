package IHM;

import javax.sound.sampled.*;
import java.io.File;
import javax.swing.Timer;

public class BoutonClickMusique {

    public void jouerClick() {
        try {
            File fichierAudio = new File("assets/sounds/click.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            // 2 saniye sonra click sesini durdurur
            Timer timer = new Timer(400, e -> {
                clip.stop();
                clip.close();
            });
            timer.setRepeats(false);
            timer.start();

        } catch (Exception e) {
            System.out.println("Erreur son bouton : " + e.getMessage());
        }
    }
}