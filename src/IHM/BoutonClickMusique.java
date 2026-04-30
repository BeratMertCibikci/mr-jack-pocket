package IHM;

import javax.sound.sampled.*;
import java.io.File;

public class BoutonClickMusique {

    public void jouerEffet(String cheminFichier) {
        try {
            File fichierAudio = new File(cheminFichier);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);

            Clip effet = AudioSystem.getClip();
            effet.open(audioStream);
            effet.start();

        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture de l'effet sonore : " + e.getMessage());
        }
    }
}