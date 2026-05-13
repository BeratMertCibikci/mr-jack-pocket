package IHM;

import ai.AIDifficulty;
import engine.GameEngine;
import java.awt.*;
import javax.swing.*;
import model.GameState;

public class JeuPanel extends JPanel {

    private GameMode mode;
    private GameEngine gameEngine;
    private GameState gameState;
    private FenetrePrincipale fenetre;

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JPanel gameScreen;
    private JPanel transitionScreen;
    private JLabel transitionMessageLabel;
    private JLabel transitionTitleLabel;
    private boolean premierAffichage = true;

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre) {
        this(mode, player1Role, fenetre, AIDifficulty.HARD);
    }

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre, AIDifficulty difficulty) {
        this.mode = mode;
        this.fenetre = fenetre;

        gameEngine = new GameEngine(player1Role);
        gameEngine.startGame();

        gameState = gameEngine.getGameState();

        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        cardLayout = new CardLayout();

        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(new Color(25, 25, 25));

        gameScreen = new JPanel(new BorderLayout());
        gameScreen.setBackground(new Color(25, 25, 25));

        JLabel titre = new JLabel("Mr Jack Pocket : " + mode, SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 26));
        titre.setForeground(new Color(245, 235, 210));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        PlateauPanel plateauPanel = new PlateauPanel(gameState);
        InfoJeuPanel infoJeuPanel = new InfoJeuPanel(gameEngine, mode, player1Role);
        TimeTokensPanel timeTokensPanel = new TimeTokensPanel(gameState);
        AlibiPanel alibiPanel = new AlibiPanel();

        ActionPanel actionPanel = new ActionPanel(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                this::afficherTransitionTour,
                mode,
                difficulty,
                player1Role
        );

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titre, BorderLayout.CENTER);

        JButton pauseButton = new JButton("⏸");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 26));
        pauseButton.setForeground(new Color(245, 235, 210));
        pauseButton.setBackground(new Color(55, 45, 35));
        pauseButton.setFocusPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        pauseButton.setPreferredSize(new Dimension(75, 45));

        pauseButton.addActionListener(e -> afficherPauseMenu());

        topPanel.add(pauseButton, BorderLayout.EAST);

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

        gameScreen.add(topPanel, BorderLayout.NORTH);
        gameScreen.add(plateauWrapper, BorderLayout.CENTER);

        transitionScreen = creerTransitionScreen();

        mainContainer.add(gameScreen, "GAME");
        mainContainer.add(transitionScreen, "TRANSITION");

        add(mainContainer, BorderLayout.CENTER);

        afficherTransitionTour();

        installerRaccourciPause();
    }

    private void installerRaccourciPause() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "pause");

        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                afficherPauseMenu();
            }
        });
    }

    private void afficherPauseMenu() {
        JDialog pauseDialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Pause",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        pauseDialog.setSize(320, 360);
        pauseDialog.setLocationRelativeTo(this);
        pauseDialog.setResizable(false);

        JPanel pausePanel = new JPanel();
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));
        pausePanel.setBackground(new Color(25, 25, 25));
        pausePanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titre = new JLabel("Pause");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton resumeButton = creerBoutonPause("Resume");
        JButton settingsButton = creerBoutonPause("Settings");
        JButton reglesButton = creerBoutonPause("Règles");
        JButton retourMenuButton = creerBoutonPause("Retour menu");

        resumeButton.addActionListener(e -> pauseDialog.dispose());

        settingsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    pauseDialog,
                    "Settings à ajouter plus tard.",
                    "Settings",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        reglesButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    pauseDialog,
                    "Règles du jeu à afficher ici.",
                    "Règles",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        retourMenuButton.addActionListener(e -> {
            pauseDialog.dispose();
            fenetre.afficherMenu();
        });

        pausePanel.add(titre);
        pausePanel.add(Box.createVerticalStrut(30));
        pausePanel.add(resumeButton);
        pausePanel.add(Box.createVerticalStrut(15));
        pausePanel.add(settingsButton);
        pausePanel.add(Box.createVerticalStrut(15));
        pausePanel.add(reglesButton);
        pausePanel.add(Box.createVerticalStrut(15));
        pausePanel.add(retourMenuButton);

        pauseDialog.setContentPane(pausePanel);
        pauseDialog.setVisible(true);
    }

    private JButton creerBoutonPause(String texte) {
        JButton bouton = new JButton(texte);

        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(220, 45));
        bouton.setPreferredSize(new Dimension(220, 45));

        bouton.setFont(new Font("Arial", Font.BOLD, 16));
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setBackground(new Color(55, 45, 35));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));

        return bouton;
    }

    private JPanel creerTransitionScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(120, 80, 120, 80));

        transitionTitleLabel = new JLabel("Changement de tour");
        transitionTitleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        transitionTitleLabel.setForeground(new Color(245, 235, 210));
        transitionTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        transitionMessageLabel = new JLabel("", SwingConstants.CENTER);
        transitionMessageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        transitionMessageLabel.setForeground(new Color(212, 175, 55));
        transitionMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton commencerButton = creerBoutonPause("Commencer le tour");

        commencerButton.addActionListener(e -> {
            cardLayout.show(mainContainer, "GAME");
        });

        panel.add(Box.createVerticalGlue());
        panel.add(transitionTitleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(transitionMessageLabel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(commencerButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void afficherTransitionTour() {
        String joueurActuel = gameEngine.getCurrentPlayer();

        if (premierAffichage) {
            transitionTitleLabel.setText("Début de partie");
            transitionMessageLabel.setText("Joueur actuel : " + joueurActuel);
            premierAffichage = false;
        } else {
            transitionTitleLabel.setText("Changement de tour");
            transitionMessageLabel.setText("Passez l'ordinateur à : " + joueurActuel);
        }

        cardLayout.show(mainContainer, "TRANSITION");
    }
}