package IHM;

import javax.swing.*;
import java.awt.*;

import engine.GameEngine;
import model.GameState;

public class JeuPanel extends JPanel {

    private GameMode mode;
    private GameEngine gameEngine;
    private GameState gameState;

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre) {
        this.mode = mode;

        // Oyun motorunu başlatıyoruz
        gameEngine = new GameEngine(player1Role); 
        gameEngine.startGame();

        // Engine içindeki mevcut GameState'i alıyoruz
        gameState = gameEngine.getGameState();

        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        JLabel titre = new JLabel("Mr Jack Pocket : " + mode, SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 26));
        titre.setForeground(new Color(245, 235, 210));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        PlateauPanel plateauPanel = new PlateauPanel(gameState);
        InfoJeuPanel infoJeuPanel = new InfoJeuPanel(gameEngine, mode, player1Role);
        TimeTokensPanel timeTokensPanel = new TimeTokensPanel(gameState);
        ActionPanel actionPanel = new ActionPanel(gameEngine, infoJeuPanel, plateauPanel, timeTokensPanel);
        AlibiPanel alibiPanel = new AlibiPanel();

        JButton retourButton = new JButton("Retour menu");
        retourButton.addActionListener(e -> fenetre.afficherMenu());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(titre, BorderLayout.CENTER);
        topPanel.add(retourButton, BorderLayout.EAST);

        JPanel plateauEtActionsPanel = new JPanel(new BorderLayout());
        plateauEtActionsPanel.setOpaque(false);
        plateauEtActionsPanel.add(plateauPanel, BorderLayout.CENTER);

        JPanel rightGamePanel = new JPanel();
        rightGamePanel.setOpaque(false);
        rightGamePanel.setPreferredSize(new Dimension(240, 0));
        rightGamePanel.setLayout(new BoxLayout(rightGamePanel, BoxLayout.Y_AXIS));

        alibiPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        alibiPanel.setPreferredSize(new Dimension(220, 250));
        alibiPanel.setMaximumSize(new Dimension(220, 250));

        actionPanel.setPreferredSize(new Dimension(220, 360));
        actionPanel.setMaximumSize(new Dimension(220, 360));

        rightGamePanel.add(Box.createVerticalStrut(30));
        rightGamePanel.add(alibiPanel);
        rightGamePanel.add(Box.createVerticalStrut(20));
        rightGamePanel.add(actionPanel);
        rightGamePanel.add(Box.createVerticalGlue());

        JPanel plateauWrapper = new JPanel(new BorderLayout());
        plateauWrapper.setBackground(new Color(35, 35, 35));
        plateauWrapper.add(timeTokensPanel, BorderLayout.WEST);
        plateauWrapper.add(plateauPanel, BorderLayout.CENTER);
        plateauWrapper.add(rightGamePanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(plateauWrapper, BorderLayout.CENTER);
        //info panel
        //add(infoJeuPanel, BorderLayout.EAST);
    }
}   