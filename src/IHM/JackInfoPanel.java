package IHM;

import engine.GameEngine;
import java.awt.*;
import javax.swing.*;

public class JackInfoPanel extends JPanel {

    private GameEngine gameEngine;
    private GameMode mode;

    private JLabel titleLabel;
    private JLabel jackIdentityLabel;
    private JLabel jackAlibisLabel;
    private JLabel jackHourglassLabel;

    public JackInfoPanel(GameEngine gameEngine) {
        this(gameEngine, GameMode.HUMAN_VS_HUMAN);
    }

    public JackInfoPanel(GameEngine gameEngine, GameMode mode) {
        this.gameEngine = gameEngine;
        this.mode = mode;

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
        jackHourglassLabel = creerPetitLabelJack();

        add(titleLabel);
        add(Box.createVerticalStrut(6));
        add(jackIdentityLabel);
        add(Box.createVerticalStrut(4));
        add(jackAlibisLabel);
        add(Box.createVerticalStrut(4));
        add(jackHourglassLabel);

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

        boolean doitAfficher = shouldShowJackInfo();
        setVisible(doitAfficher);

        if (!doitAfficher) {
            return;
        }

        try {
            String identiteJack = gameEngine.getGameState().getJackCharacter().getName();
            jackIdentityLabel.setText("Identity: " + identiteJack);
        } catch (Exception e) {
            jackIdentityLabel.setText("Identity: unknown");
        }

        jackAlibisLabel.setText("Alibis: " + getAlibisJackTexte());
        jackHourglassLabel.setText("Hourglass: " + getNombreSabliersJack());

        revalidate();
        repaint();
    }

    private boolean shouldShowJackInfo() {
        if (mode == GameMode.IA_VS_IA) {
            return true;
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