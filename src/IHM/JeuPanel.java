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

        JButton retourButton = new JButton("Retour menu");
        retourButton.addActionListener(e -> fenetre.afficherMenu());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(titre, BorderLayout.CENTER);
        topPanel.add(retourButton, BorderLayout.EAST);

        JPanel plateauWrapper = new JPanel(new BorderLayout());
        plateauWrapper.setBackground(new Color(35, 35, 35));
        plateauWrapper.add(timeTokensPanel, BorderLayout.WEST);
        plateauWrapper.add(plateauPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(plateauWrapper, BorderLayout.CENTER);
        add(infoJeuPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }
}