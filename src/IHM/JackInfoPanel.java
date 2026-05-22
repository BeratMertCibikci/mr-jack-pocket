package IHM;

import engine.GameEngine;
import java.awt.*;
import javax.swing.*;

public class JackInfoPanel extends JPanel {

    private GameEngine gameEngine;
    private GameMode mode;
    private String humanRole;
    private JLabel titleLabel;
    private JLabel jackIdentityTextLabel;
    private JLabel jackIdentityImageLabel;
    private JLabel jackAlibisLabel;
    
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

        setPreferredSize(new Dimension(220, 180));
        setMaximumSize(new Dimension(220, 180));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel("Jack Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(245, 235, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jackIdentityTextLabel = creerPetitLabelJack();
        jackIdentityTextLabel.setText("Identity:");

        jackIdentityImageLabel = new JLabel();
        jackIdentityImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jackIdentityImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        jackAlibisLabel = creerPetitLabelJack();

        add(titleLabel);
        add(Box.createVerticalStrut(6));
        add(jackIdentityTextLabel);
        add(Box.createVerticalStrut(4));
        add(jackIdentityImageLabel);
        add(Box.createVerticalStrut(4));
        add(jackAlibisLabel);

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

            String nomFichier = identiteJack.toLowerCase()
                    .replace(".", "")
                    .replace(" ", "_");

            String chemin = System.getProperty("user.dir")
                    + "/assets/images/characters/identity_"
                    + nomFichier
                    + ".png";

            ImageIcon icon = new ImageIcon(chemin);

            Image image = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            jackIdentityImageLabel.setIcon(new ImageIcon(image));
            jackIdentityImageLabel.setText("");

        } catch (Exception e) {
            jackIdentityImageLabel.setIcon(null);
            jackIdentityImageLabel.setText("?");
        }

        jackAlibisLabel.setText("Alibis: " + getAlibisJackTexte());
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

    public int getNombreSabliersJack() {
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