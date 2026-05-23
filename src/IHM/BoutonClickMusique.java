package IHM;

import javax.sound.sampled.*;
import java.io.File;

public class BoutonClickMusique {

    public void jouerClick() {
        // Sesi ayrı bir kanalda açıyoruz ki oyun donmasın ve üst üste binmesin
        new Thread(() -> {
            try {
                File fichierAudio = new File("assets/sounds/click.wav");
                if (!fichierAudio.exists()) return;

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                // 400 milisaniye bekle ve sesi temizle (Rastgele ses patlamalarını önler)
                Thread.sleep(400); 
                clip.stop();
                clip.close();
            } catch (Exception e) {
                // Hata olursa sessizce geç
            }
        }).start();
    }
}