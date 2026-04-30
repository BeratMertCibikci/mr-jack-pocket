package IHM;

import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame {

    private SoundManager musique;

    public FenetrePrincipale() {
        setTitle("Mr Jack Pocket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        musique = new SoundManager();
        musique.jouerMusique("sounds/menu.wav");

        MenuPrincipalPanel menuPrincipalPanel = new MenuPrincipalPanel();

        add(menuPrincipalPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}