package IHM;

import java.awt.*;
import javax.swing.*;

public class FenetrePrincipale extends JFrame {

    private MainMusique musique;
    private boolean musiqueActive = false;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public FenetrePrincipale() {
        setTitle("Mr Jack Pocket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        musique = new MainMusique();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new MenuPrincipalPanel(this), "menu");
        mainPanel.add(new ChoixModePanel(this), "choixMode");
        mainPanel.add(new ReglesPanel(this), "regles");

        add(mainPanel, BorderLayout.CENTER);
        afficherMenu();
    }

    public void afficherMenu() {
        cardLayout.show(mainPanel, "menu");
    }

    public void afficherChoixMode() {
        cardLayout.show(mainPanel, "choixMode");
    }

    public void afficherRegles() {
        cardLayout.show(mainPanel, "regles");
    }

    public void lancerJeu(GameMode mode, String player1Role) {
        System.out.println("Mode sélectionné: " + mode);
        System.out.println("Rôle Joueur 1 : " + player1Role);

        JeuPanel jeuPanel = new JeuPanel(mode, player1Role, this);
        mainPanel.add(jeuPanel, "jeu");// yeni oyun ekranı oluşturma
        cardLayout.show(mainPanel, "jeu"); // CardLayout içine ekleme yani ekranda menü değil oyun ekranı döndürüyoz
    }

    public void toggleMusique() {
        if (musiqueActive) {
            musique.arreterMusique();
            musiqueActive = false;
        } else {
            musique = new MainMusique();
            musique.jouerMusique("assets/sounds/menu.wav");
            musiqueActive = true;
        }
    }

    public boolean estMusiqueActivee() {
        return musiqueActive;
    }

    public void changerVolumeMusique(int volume) {
        if (musique != null) musique.changerVolume(volume);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}