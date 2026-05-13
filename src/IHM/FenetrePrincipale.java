package IHM;

import ai.AIDifficulty;
import java.awt.*;
import javax.swing.*;
import engine.GameEngine;

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

        mainPanel.add(new MenuPrincipalPanel(this), "menu principale");
        mainPanel.add(new ChoixModePanel(this), "choixMode");
        mainPanel.add(new ReglesPanel(this), "regles");

        add(mainPanel, BorderLayout.CENTER);
        afficherMenu();
    }

    public void afficherMenu() {
        cardLayout.show(mainPanel, "menu principale");
    }

    public void afficherChoixMode() {
        cardLayout.show(mainPanel, "choixMode");
    }

    public void afficherRegles() {
        cardLayout.show(mainPanel, "regles");
    }

    public void lancerJeu(GameMode mode, String player1Role) {
        lancerJeu(mode, player1Role, AIDifficulty.HARD);
    }

    public void lancerJeu(GameMode mode, String player1Role, AIDifficulty difficulty) {
        System.out.println("Mode sélectionné: " + mode);
        System.out.println("Rôle Joueur 1 : " + player1Role);
        System.out.println("Difficulté IA : " + difficulty);

        JeuPanel jeuPanel = new JeuPanel(mode, player1Role, this, difficulty);

        mainPanel.add(jeuPanel, "jeu");
        cardLayout.show(mainPanel, "jeu");

        revalidate();
        repaint();
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
        if (musique != null) {
            musique.changerVolume(volume);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }

    public void chargerJeuDepuisMenu() {
        String filename = JOptionPane.showInputDialog(
                this,
                "Nom du fichier à charger :",
                "Charger une partie",
                JOptionPane.QUESTION_MESSAGE
        );

        if (filename == null) {
            return;
        }

        filename = filename.trim();

        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nom de fichier invalide.",
                    "Erreur chargement",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        GameEngine loadedEngine = GameEngine.loadGame(filename);

        if (loadedEngine == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Impossible de charger la partie.",
                    "Erreur chargement",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String player1Role = loadedEngine.getGameState().getPlayer1Role();

        JeuPanel jeuPanel = new JeuPanel(
                GameMode.HUMAN_VS_HUMAN,
                player1Role,
                this,
                loadedEngine
        );

        mainPanel.add(jeuPanel, "jeu");
        cardLayout.show(mainPanel, "jeu");
    }
} 

