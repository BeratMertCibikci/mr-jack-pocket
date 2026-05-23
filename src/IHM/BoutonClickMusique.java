package IHM;

import javax.sound.sampled.*;
import javax.swing.Timer;
import java.io.File;

public class BoutonClickMusique {

    private static long dernierClickAction = 0;
    private static Clip actionClipEnCours = null;

    public void jouerClick() {
        jouerClickAvecDuree(400);
    }

    public void jouerClickAction() {
        long maintenant = System.currentTimeMillis();

        // Çok hızlı üst üste tetiklenirse ikinci sesi engeller
        if (maintenant - dernierClickAction < 180) {
            return;
        }

        dernierClickAction = maintenant;

        try {
            if (actionClipEnCours != null) {
                actionClipEnCours.stop();
                actionClipEnCours.close();
                actionClipEnCours = null;
            }

            File fichierAudio = new File("assets/sounds/click.wav");

            if (!fichierAudio.exists()) {
                System.out.println("Son click non trouvé : " + fichierAudio.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

            actionClipEnCours = clip;

            // Action token için çok kısa tek tık
            Timer timer = new Timer(55, e -> {
                try {
                    if (clip.isRunning()) {
                        clip.stop();
                    }

                    clip.flush();
                    clip.close();
                    audioStream.close();

                    if (actionClipEnCours == clip) {
                        actionClipEnCours = null;
                    }

                } catch (Exception ex) {
                    System.out.println("Erreur fermeture son action : " + ex.getMessage());
                }
            });

            timer.setRepeats(false);
            timer.start();

        } catch (Exception e) {
            System.out.println("Erreur son bouton action : " + e.getMessage());
        }
    }

    private void jouerClickAvecDuree(int dureeMs) {
        try {
            File fichierAudio = new File("assets/sounds/click.wav");

            if (!fichierAudio.exists()) {
                System.out.println("Son click non trouvé : " + fichierAudio.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

            Timer timer = new Timer(dureeMs, e -> {
                try {
                    if (clip.isRunning()) {
                        clip.stop();
                    }

                    clip.flush();
                    clip.close();
                    audioStream.close();

                } catch (Exception ex) {
                    System.out.println("Erreur fermeture son : " + ex.getMessage());
                }
            });

            timer.setRepeats(false);
            timer.start();

        } catch (Exception e) {
            System.out.println("Erreur son bouton : " + e.getMessage());
        }
    }
}