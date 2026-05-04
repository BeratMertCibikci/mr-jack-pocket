package IHM;

import javax.swing.*;
import java.awt.*;

import engine.GameEngine;

public class InfoJeuPanel extends JPanel {

    private GameEngine gameEngine;
    private GameMode mode;
    private String joueur1Role;

    private JLabel modeLabel;
    private JLabel joueur1RoleLabel;
    private JLabel joueur2RoleLabel;
    private JLabel joueurActuelLabel;
    private JLabel roundLabel;
    private JLabel actionLabel;
    private JLabel selectedActionLabel;
    private JLabel roundOverLabel;
    private JLabel holmesLabel;
    private JLabel watsonLabel;
    private JLabel tobyLabel;

    public InfoJeuPanel(GameEngine gameEngine, GameMode mode, String joueur1Role) {
        this.gameEngine = gameEngine;
        this.mode = mode;
        this.joueur1Role = joueur1Role;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, 0));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel titre = new JLabel("Info jeu");
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);

        modeLabel = creerLabel();
        joueur1RoleLabel = creerLabel();
        joueur2RoleLabel = creerLabel();
        joueurActuelLabel = creerLabel();
        roundLabel = creerLabel();
        actionLabel = creerLabel();
        selectedActionLabel = creerLabel();
        roundOverLabel = creerLabel();
        holmesLabel = creerLabel();
        watsonLabel = creerLabel();
        tobyLabel = creerLabel();

        add(titre);
        add(Box.createVerticalStrut(25));
        add(joueur1RoleLabel);
        add(Box.createVerticalStrut(15));
        add(joueur2RoleLabel);
        add(Box.createVerticalStrut(15));
        add(modeLabel);
        add(Box.createVerticalStrut(15));
        add(joueurActuelLabel);
        add(Box.createVerticalStrut(15));
        add(roundLabel);
        add(Box.createVerticalStrut(15));
        add(actionLabel);
        add(Box.createVerticalStrut(15));
        add(selectedActionLabel);
        add(Box.createVerticalStrut(15));
        add(roundOverLabel);
        add(Box.createVerticalStrut(25));
        add(holmesLabel);
        add(Box.createVerticalStrut(15));
        add(watsonLabel);
        add(Box.createVerticalStrut(15));
        add(tobyLabel);

        rafraichir();
    }

    private JLabel creerLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(new Color(245, 235, 210));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public void rafraichir() {
        modeLabel.setText("Mode : " + mode);

        joueur1RoleLabel.setText("Joueur 1 : " + joueur1Role);

        String joueur2Role;
        if (joueur1Role.equals("Investigator")) {
            joueur2Role = "Jack";
        } else {
            joueur2Role = "Investigator";
        }

        joueur2RoleLabel.setText("Joueur 2 : " + joueur2Role);

        joueurActuelLabel.setText("Joueur actuel : " + gameEngine.getCurrentPlayer());
        roundLabel.setText("Round : " + gameEngine.getRoundNumber());
        actionLabel.setText("Action : " + gameEngine.getCurrentActionIndex());

        try {
            selectedActionLabel.setText("Action choisie : " + gameEngine.getSelectedActionType());
        } catch (IllegalStateException e) {
            selectedActionLabel.setText("Action choisie : aucune");
        }

        roundOverLabel.setText("Round terminé : " + gameEngine.isRoundOver());
        holmesLabel.setText("Holmes position : " + gameEngine.getGameState().getDetectiveTokens().getHolmes().getPosition());
        watsonLabel.setText("Watson position : " + gameEngine.getGameState().getDetectiveTokens().getWatson().getPosition());
        tobyLabel.setText("Toby position : " + gameEngine.getGameState().getDetectiveTokens().getToby().getPosition());
    }
}