package IHM;

import engine.GameEngine;
import java.awt.*;
import javax.swing.*;

public class JackInfoPanel extends JPanel {

    private GameEngine gameEngine;
    private GameMode mode;
    private String humanRole;
    private JLabel titleLabel;
    private JLabel jackIdentityLabel;
    private JLabel jackAlibisLabel;
    private JPanel hourglassPanel;
    
    public JackInfoPanel(GameEngine gameEngine) {
        this(gameEngine, GameMode.HUMAN_VS_HUMAN, "Investigator");
    }
    public JackInfoPanel(GameEngine gameEngine, GameMode mode) {
        this(gameEngine, mode, "Investigator");
    }

    public JackInfoPanel(GameEngine gameEngine, GameMode mode,String humanRole) {
        this.gameEngine = gameEngine;
        this.mode = mode;
        this.humanRole = humanRole;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(35, 30, 25));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        setPreferredSize(new Dimension(220, 120));
        setMaximumSize(new Dimension(220, 120));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel("Jack Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(245, 235, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jackIdentityLabel = creerPetitLabelJack();
        jackAlibisLabel = creerPetitLabelJack();
        hourglassPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        hourglassPanel.setOpaque(false);

        add(titleLabel);
        add(Box.createVerticalStrut(6));
        add(jackIdentityLabel);
        add(Box.createVerticalStrut(4));
        add(jackAlibisLabel);
        add(Box.createVerticalStrut(4));
        add(hourglassPanel);

        rafraichir();
    }

    private JLabel creerPetitLabelJack() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(245, 235, 210));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public void rafraichir() {
        if (gameEngine == null || gameEngine.getGameState() == null) {
            setVisible(false);
            return;
        }

        if (mode == GameMode.IA_VS_IA) {
            setVisible(true);
            remplirInfosJack();
            return;
        }

        boolean doitAfficher = shouldShowJackInfo();
        setVisible(doitAfficher);

        if (!doitAfficher) {
            revalidate();
            repaint();
            return;
        }

        remplirInfosJack();
    }

    private void remplirInfosJack() {
        try {
            String identiteJack = gameEngine.getGameState().getJackCharacter().getName();
            jackIdentityLabel.setText("Identity: " + identiteJack);
        } catch (Exception e) {
            jackIdentityLabel.setText("Identity: unknown");
        }

        jackAlibisLabel.setText("Alibis: " + getAlibisJackTexte());
        rafraichirHourglasses();
        revalidate();
        repaint();
    }

    private boolean shouldShowJackInfo() {
        if (mode == GameMode.IA_VS_IA) {
            return true;
        }

        if (mode == GameMode.HUMAN_VS_IA) {
            return "Jack".equals(humanRole);
        }

        try {
            boolean jackTurn = gameEngine.getGameState().getTurnManager().isJackTurn();
            String currentPlayer = String.valueOf(gameEngine.getCurrentPlayer());

            return jackTurn || currentPlayer.toLowerCase().contains("jack");

        } catch (Exception e) {
            return false;
        }
    }

    private String getAlibisJackTexte() {
        try {
            StringBuilder sb = new StringBuilder();

            for (model.AlibiCards card : gameEngine.getGameState().getAlibiDeckManager().getCards()) {
                if ("Jack".equals(card.getOwner())
                        && !card.getCharacter().isJack()) {

                    sb.append(card.getCharacter().getName())
                            .append(" (")
                            .append(card.getHourglassValue())
                            .append("), ");
                }
            }

            if (sb.length() == 0) {
                return "none";
            }

            sb.setLength(sb.length() - 2);
            return sb.toString();

        } catch (Exception e) {
            return "unavailable";
        }
    }
    private void rafraichirHourglasses() {
        if (hourglassPanel == null) {
            return;
        }

        hourglassPanel.removeAll();

        int count = getNombreSabliersJack();

        if (count < 0) {
            count = 0;
        }

        if (count > 6) {
            count = 6;
        }

        for (int i = 1; i <= 6; i++) {
            JLabel iconLabel = new JLabel();

            if (i <= count) {
                iconLabel.setIcon(chargerIconeHourglass(true));
            } else {
                iconLabel.setIcon(chargerIconeHourglass(false));
            }

            hourglassPanel.add(iconLabel);
        }

        hourglassPanel.revalidate();
        hourglassPanel.repaint();
    }

    private ImageIcon chargerIconeHourglass(boolean full) {
        String fileName = full ? "hourglass_full.png" : "hourglass_empty.png";

        String chemin = System.getProperty("user.dir")
                + "/assets/images/"
                + fileName;

        ImageIcon icon = new ImageIcon(chemin);

        Image image = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private int getNombreSabliersJack() {
        try {
            return gameEngine.getGameState()
                    .getWinConditionChecker()
                    .calculateJackHourglassTotal(
                            gameEngine.getGameState().getAlibiDeckManager(),
                            gameEngine.getGameState().getJackPlayerState()
                    );

        } catch (Exception e) {
            return 0;
        }
    }
}