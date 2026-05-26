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

    private String playerRole;

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JPanel gameScreen;
    private JPanel transitionScreen;

    private JLabel transitionMessageLabel;
    private JLabel transitionTitleLabel;

    private JackInfoPanel jackInfoPanel;
    private JPanel rightGamePanel;

    private boolean premierAffichage = true;

    private PlateauPanel plateauPanel;
    private InfoJeuPanel infoJeuPanel;
    private TimeTokensPanel timeTokensPanel;
    private AlibiPanel alibiPanel;
    private ActionPanel actionPanel;

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre) {
        this(mode, player1Role, fenetre, AIDifficulty.HARD, AIDifficulty.HARD, null);
    }

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre, AIDifficulty difficulty) {
        this(mode, player1Role, fenetre, difficulty, difficulty, null);
    }

    public JeuPanel(
            GameMode mode,
            String player1Role,
            FenetrePrincipale fenetre,
            AIDifficulty investigatorDifficulty,
            AIDifficulty jackDifficulty
    ) {
        this(mode, player1Role, fenetre, investigatorDifficulty, jackDifficulty, null);
    }

    public JeuPanel(GameMode mode, String player1Role, FenetrePrincipale fenetre, GameEngine loadedEngine) {
        this(mode, player1Role, fenetre, AIDifficulty.HARD, AIDifficulty.HARD, loadedEngine);
    }

    private JeuPanel(
            GameMode mode,
            String player1Role,
            FenetrePrincipale fenetre,
            AIDifficulty investigatorDifficulty,
            AIDifficulty jackDifficulty,
            GameEngine loadedEngine
    ) {
        this.mode = mode;
        this.fenetre = fenetre;
        this.playerRole = player1Role;

        if (loadedEngine != null) {
            gameEngine = loadedEngine;
        } else {
            gameEngine = new GameEngine(player1Role);
            gameEngine.startGame();
        }

        gameState = gameEngine.getGameState();

        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(new Color(25, 25, 25));

        gameScreen = new JPanel(new BorderLayout());
        gameScreen.setBackground(new Color(25, 25, 25));

        String titreText = "Mr Jack Pocket : " + mode;
        if (mode == GameMode.HUMAN_NETWORK) {
            titreText = "Mr Jack Pocket - Réseau [" + player1Role + "]";
        }

        JLabel titre = new JLabel(titreText, SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        titre.setForeground(new Color(245, 235, 210));
        titre.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        rightGamePanel = new JPanel();
        rightGamePanel.setOpaque(false);
        rightGamePanel.setPreferredSize(new Dimension(240, 0));
        rightGamePanel.setLayout(new BoxLayout(rightGamePanel, BoxLayout.Y_AXIS));

        plateauPanel = new PlateauPanel(gameState);
        infoJeuPanel = new InfoJeuPanel(gameEngine, mode, player1Role);
        timeTokensPanel = new TimeTokensPanel(gameState);
        alibiPanel = new AlibiPanel();

        jackInfoPanel = new JackInfoPanel(gameEngine, mode, player1Role);

        actionPanel = new ActionPanel(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                jackInfoPanel,
                this::afficherTransitionTour,
                mode,
                investigatorDifficulty,
                jackDifficulty,
                player1Role
        );

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        topPanel.add(titre, BorderLayout.CENTER);

        JButton pauseButton = new JButton("⏸");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.setForeground(new Color(245, 235, 210));
        pauseButton.setBackground(new Color(55, 45, 35));
        pauseButton.setFocusPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        pauseButton.setPreferredSize(new Dimension(50, 24));
        pauseButton.addActionListener(e -> afficherPauseMenu());
        topPanel.add(pauseButton, BorderLayout.EAST);

        rightGamePanel = new JPanel();
        rightGamePanel.setOpaque(false);
        rightGamePanel.setPreferredSize(new Dimension(240, 0));
        rightGamePanel.setLayout(new BoxLayout(rightGamePanel, BoxLayout.Y_AXIS));
        rightGamePanel.setMinimumSize(new Dimension(240, 0));

        alibiPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jackInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        alibiPanel.setPreferredSize(new Dimension(220, 210));
        //alibiPanel.setMaximumSize(new Dimension(220, 250));

        jackInfoPanel.setPreferredSize(new Dimension(220, 150));
        //jackInfoPanel.setMaximumSize(new Dimension(220, 180));

        //int actionPanelHeight = Math.max(300, getHeight() - 420);

        actionPanel.setPreferredSize(new Dimension(220, 320));
        actionPanel.setMaximumSize(new Dimension(220, 320));
        actionPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        rightGamePanel.add(Box.createVerticalStrut(15));
        rightGamePanel.add(alibiPanel);
        rightGamePanel.add(Box.createVerticalStrut(15));

        if (mode == GameMode.HUMAN_NETWORK) {
            if ("Jack".equalsIgnoreCase(player1Role)) {
                rightGamePanel.add(jackInfoPanel);
                rightGamePanel.add(Box.createVerticalStrut(15));
            }
        } else {
            rightGamePanel.add(jackInfoPanel);
            rightGamePanel.add(Box.createVerticalStrut(15));
        }

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

        if (mode == GameMode.IA_VS_IA) {
            premierAffichage = false;
            cardLayout.show(mainContainer, "GAME");
        } else if (mode == GameMode.HUMAN_NETWORK) {
            premierAffichage = false;
            cardLayout.show(mainContainer, "GAME");
        } else {
            afficherTransitionTour("Début de partie");
        }

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

        pauseDialog.setSize(420, 560);
        pauseDialog.setLocationRelativeTo(this);
        pauseDialog.setResizable(false);
        pauseDialog.setUndecorated(true);

        JPanel pausePanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("assets/images/action_message/pause.png").getImage();

            {
                setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                g2.dispose();
            }
        };

        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));

        // Görselde üstte "Pause" yazısı zaten var.
        // Bu değerler butonları ortadaki boş alana yerleştiriyor.
        pausePanel.setBorder(BorderFactory.createEmptyBorder(220, 75, 60, 75));

        JButton resumeButton = creerBoutonPauseParchemin("Resume");
        JButton saveButton = creerBoutonPauseParchemin("Save");
        JButton settingsButton = creerBoutonPauseParchemin("Settings");
        JButton reglesButton = creerBoutonPauseParchemin("Game Rules");
        JButton saveQuitButton = creerBoutonPauseParchemin("Save and Quit");
        JButton retourMenuButton = creerBoutonPauseParchemin("Return to Menu");

        resumeButton.addActionListener(e -> pauseDialog.dispose());

        saveButton.addActionListener(e -> sauvegarderAvecDialogue(pauseDialog));

        saveQuitButton.addActionListener(e -> {
            boolean saved = sauvegarderAvecDialogue(pauseDialog);
            if (saved) {
                pauseDialog.dispose();
                fenetre.afficherMenu();
            }
        });

        settingsButton.addActionListener(e -> afficherSettingsDialog(pauseDialog));

        reglesButton.addActionListener(e -> {
            JDialog rulesDialog = new JDialog(
                    pauseDialog,
                    "Règles du jeu",
                    Dialog.ModalityType.APPLICATION_MODAL
            );

            rulesDialog.setSize(700, 700);
            rulesDialog.setLocationRelativeTo(pauseDialog);
            rulesDialog.setResizable(false);

            ReglesPanel reglesPanel = new ReglesPanel(() -> rulesDialog.dispose());

            rulesDialog.setContentPane(reglesPanel);
            rulesDialog.setVisible(true);
        });

        retourMenuButton.addActionListener(e -> {
            pauseDialog.dispose();
            fenetre.afficherMenu();
        });

        pausePanel.add(resumeButton);
        pausePanel.add(Box.createVerticalStrut(8));
        pausePanel.add(saveButton);
        pausePanel.add(Box.createVerticalStrut(8));
        pausePanel.add(settingsButton);
        pausePanel.add(Box.createVerticalStrut(8));
        pausePanel.add(reglesButton);
        pausePanel.add(Box.createVerticalStrut(8));
        pausePanel.add(saveQuitButton);
        pausePanel.add(Box.createVerticalStrut(8));
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
    private JButton creerBoutonPauseParchemin(String texte) {
        JButton bouton = new JButton(texte);

        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(230, 34));
        bouton.setPreferredSize(new Dimension(230, 34));

        bouton.setFont(new Font("Serif", Font.BOLD, 16));
        bouton.setForeground(new Color(35, 20, 8));

        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);

        bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 38, 12), 2),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));

        return bouton;
    }

    private void afficherSettingsDialog(JDialog parentDialog) {
        JDialog settingsDialog = new JDialog(
                parentDialog,
                "Settings",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        settingsDialog.setSize(360, 260);
        settingsDialog.setLocationRelativeTo(parentDialog);
        settingsDialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel titre = new JLabel("Settings");
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox musiqueCheckBox = new JCheckBox("Musique activée");
        musiqueCheckBox.setSelected(fenetre.estMusiqueActivee());
        musiqueCheckBox.setFont(new Font("Arial", Font.BOLD, 15));
        musiqueCheckBox.setForeground(new Color(245, 235, 210));
        musiqueCheckBox.setBackground(new Color(25, 25, 25));
        musiqueCheckBox.setFocusPainted(false);
        musiqueCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        volumeLabel.setForeground(new Color(245, 235, 210));
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, 40);
        volumeSlider.setMaximumSize(new Dimension(230, 45));
        volumeSlider.setOpaque(false);
        volumeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton fermerButton = creerBoutonPause("Fermer");

        musiqueCheckBox.addActionListener(e -> {
            fenetre.toggleMusique();
            musiqueCheckBox.setSelected(fenetre.estMusiqueActivee());
        });

        volumeSlider.addChangeListener(e -> {
            fenetre.changerVolumeMusique(volumeSlider.getValue());
        });

        fermerButton.addActionListener(e -> settingsDialog.dispose());

        panel.add(titre);
        panel.add(Box.createVerticalStrut(20));
        panel.add(musiqueCheckBox);
        panel.add(Box.createVerticalStrut(15));
        panel.add(volumeLabel);
        panel.add(volumeSlider);
        panel.add(Box.createVerticalStrut(20));
        panel.add(fermerButton);

        settingsDialog.setContentPane(panel);
        settingsDialog.setVisible(true);
    }

    private boolean sauvegarderAvecDialogue(Component parent) {
        String filename = JOptionPane.showInputDialog(
                parent, "Nom du fichier de sauvegarde :", "Sauvegarder", JOptionPane.QUESTION_MESSAGE);

        if (filename == null) return false;
        filename = filename.trim();

        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nom de fichier invalide.", "Erreur sauvegarde", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        gameEngine.saveGame(filename);
        JOptionPane.showMessageDialog(parent, "Partie sauvegardée : " + filename, "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
        return true;
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
            rafraichirPanneauDroite();
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

    private void rafraichirPanneauDroite() {
        if (jackInfoPanel != null) jackInfoPanel.rafraichir();
        if (rightGamePanel != null) {
            rightGamePanel.revalidate();
            rightGamePanel.repaint();
        }
    }

    private void afficherTransitionTour(String titreTransition) {
        if (mode == GameMode.HUMAN_NETWORK) {
            rafraichirPanneauDroite();
            cardLayout.show(mainContainer, "GAME");
            return;
        }

        if (mode == GameMode.IA_VS_IA) {
            rafraichirPanneauDroite();
            cardLayout.show(mainContainer, "GAME");
            return;
        }

        String joueurActuel = gameEngine.getCurrentPlayer();

        if (premierAffichage) {
            transitionTitleLabel.setText("Début de partie");
            transitionMessageLabel.setText("Joueur actuel : " + joueurActuel);
            premierAffichage = false;
        } else {
            transitionTitleLabel.setText(titreTransition);
            transitionMessageLabel.setText("Passez l'ordinateur à : " + joueurActuel);
        }

        cardLayout.show(mainContainer, "TRANSITION");
    }

    public GameEngine getGameEngine() {
        return this.gameEngine;
    }

    public void rafraichirToutesLesVues() {
        if (plateauPanel != null) plateauPanel.setGameState(gameEngine.getGameState());
        if (timeTokensPanel != null) timeTokensPanel.setGameState(gameEngine.getGameState());

        if (plateauPanel != null) plateauPanel.rafraichir();
        if (infoJeuPanel != null) infoJeuPanel.rafraichir();
        if (timeTokensPanel != null) timeTokensPanel.rafraichir();
        if (jackInfoPanel != null) jackInfoPanel.rafraichir();
        if (actionPanel != null) actionPanel.rafraichir();

        this.revalidate();
        this.repaint();
    }
}