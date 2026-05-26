package IHM;

import javax.sound.sampled.*;
import javax.swing.Timer;
import java.io.File;

public class BoutonClickMusique {

    public void jouerClick() {
        new Thread(() -> {
            try {
                File fichierAudio = new File("assets/sounds/click.wav");

                if (!fichierAudio.exists()) {
                    System.out.println("Son click non trouvé : " + fichierAudio.getAbsolutePath());
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
                Clip clip = AudioSystem.getClip();

                clip.open(audioStream);
                clip.start();

                Timer timer = new Timer(400, e -> {
                    try {
                        if (clip.isRunning()) {
                            clip.stop();
                        }

                        clip.close();
                        audioStream.close();

                    } catch (Exception ex) {
                        System.out.println("Erreur fermeture son bouton : " + ex.getMessage());
                    }
                });

                timer.setRepeats(false);
                timer.start();

            } catch (Exception e) {
                System.out.println("Erreur son bouton : " + e.getMessage());
            }
        }).start();
    }

    public void jouerClickAction() {
        jouerClick();
    }
}