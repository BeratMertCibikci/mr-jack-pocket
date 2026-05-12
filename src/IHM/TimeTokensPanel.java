package IHM;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import model.GameState;
import model.Token;

public class TimeTokensPanel extends JPanel {
    private GameState gameState;

    public TimeTokensPanel(GameState gameState) {
        this.gameState = gameState;

        setPreferredSize(new Dimension(90, 0));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        afficherTimeTokens();
    }

    public void rafraichir() {
        removeAll();
        afficherTimeTokens();
        revalidate();
        repaint();
    }

    private void afficherTimeTokens() {
        JLabel titre = new JLabel("Tours");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titre);
        add(Box.createVerticalStrut(15));

        for (int i = 1; i <= 8; i++) {
            JPanel tokenPanel = creerTimeTokenPanel(i);
            tokenPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(tokenPanel);
            add(Box.createVerticalStrut(8));
        }
    }

    private JPanel creerTimeTokenPanel(int numeroTour) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(55, 42));
        panel.setMaximumSize(new Dimension(55, 42));
        panel.setBackground(new Color(45, 40, 35));
        panel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));

        JLabel label = new JLabel(String.valueOf(numeroTour), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(245, 235, 210));

        String proprietaire = getProprietaireToken(numeroTour);
        
        if (proprietaire.equals("CURRENT")) {
            panel.setBackground(new Color(80, 65, 35));
            panel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
            label.setText(numeroTour + "*");
            panel.setToolTipText("Tour actuel");
        } else if (proprietaire.equals("INVESTIGATOR")) {
            panel.setBackground(new Color(35, 60, 90));
            label.setText(numeroTour + "I");
            panel.setToolTipText("Pris par l'Investigateur");
        } else if (proprietaire.equals("JACK")) {
            panel.setBackground(new Color(90, 35, 35));
            label.setText(numeroTour + "⌛");
            panel.setToolTipText("Pris par Jack");
        } else {
            panel.setToolTipText("Tour " + numeroTour + " disponible");
        }

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private String getProprietaireToken(int numeroTour) {
        Token current = gameState.getCurrentTurnToken();
        if (current != null && valeurToken(current) == numeroTour) return "CURRENT";

        for (Token token : gameState.getDetectivePlayerState().getOwnedTurnTokens()) {
            if (valeurToken(token) == numeroTour) return "INVESTIGATOR";
        }

        for (Token token : gameState.getJackPlayerState().getOwnedTurnTokens()) {
            if (valeurToken(token) == numeroTour) return "JACK";
        }

        return "NONE";
    }

    private int valeurToken(Token token) {
        String face = token.getFrontSide();

        switch (face) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            default:
                return 0;
        }
    }
}
