package IHM;

import java.awt.*;
import javax.swing.*;

public class FenetrePrincipale extends JFrame {

    private MainMusique musique;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public FenetrePrincipale() {
        setTitle("Mr Jack Pocket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        musique = new MainMusique();
        musique.jouerMusique("sounds/menu.wav");

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

    public void lancerJeu(GameMode mode) {
        System.out.println("Mode séléctionné: " + mode);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}