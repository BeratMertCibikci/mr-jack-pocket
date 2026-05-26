package IHM;

import ai.AIDifficulty;
import ai.AIFactory;
import ai.AIMove;
import ai.AIPlayer;
import ai.EvaluationPerspective;
import ai.MinimaxAI;
import engine.ActionType;
import engine.GameEngine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.GameCharacter;
import model.Tile;
import model.Token;
import model.HourglassCalculator;

public class ActionPanel extends JPanel {

    private GameEngine gameEngine;
    private InfoJeuPanel infoJeuPanel;
    private PlateauPanel plateauPanel;
    private TimeTokensPanel timeTokensPanel;
    private AlibiPanel alibiPanel;
    private JackInfoPanel jackInfoPanel;
    private BoutonClickMusique boutonClickMusique;

    private boolean actionEnCours = false;
    private Consumer<String> onTransitionTour;
    private String dernierJoueurAffiche;

    private GameMode mode;
    private AIDifficulty investigatorDifficulty;
    private AIDifficulty jackDifficulty;
    private String humanRole;

    private AIPlayer investigatorAI;
    private AIPlayer jackAI;
    private boolean aiEnCours = false;
    private boolean autoAIEnabled = false;
    private JButton autoAIButton;
    private boolean gameOverDialogShown = false;
    private int autoAIDelayMs = 700;

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel
    ) {
        this(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                null,
                null,
                null,
                GameMode.HUMAN_VS_HUMAN,
                AIDifficulty.HARD,
                AIDifficulty.HARD,
                "Investigator"
        );
    }

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel,
            AlibiPanel alibiPanel
    ) {
        this(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                null,
                null,
                GameMode.HUMAN_VS_HUMAN,
                AIDifficulty.HARD,
                AIDifficulty.HARD,
                "Investigator"
        );
    }

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel,
            AlibiPanel alibiPanel,
            Consumer<String> onTransitionTour
    ) {
        this(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                null,
                onTransitionTour,
                GameMode.HUMAN_VS_HUMAN,
                AIDifficulty.HARD,
                AIDifficulty.HARD,
                "Investigator"
        );
    }

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel,
            AlibiPanel alibiPanel,
            Consumer<String> onTransitionTour,
            GameMode mode,
            AIDifficulty difficulty,
            String humanRole
    ) {
        this(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                null,
                onTransitionTour,
                mode,
                difficulty,
                difficulty,
                humanRole
        );
    }

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel,
            AlibiPanel alibiPanel,
            GameMode mode,
            AIDifficulty difficulty,
            String humanRole
    ) {
        this(
                gameEngine,
                infoJeuPanel,
                plateauPanel,
                timeTokensPanel,
                alibiPanel,
                null,
                null,
                mode,
                difficulty,
                difficulty,
                humanRole
        );
    }

    public ActionPanel(
            GameEngine gameEngine,
            InfoJeuPanel infoJeuPanel,
            PlateauPanel plateauPanel,
            TimeTokensPanel timeTokensPanel,
            AlibiPanel alibiPanel,
            JackInfoPanel jackInfoPanel,
            Consumer<String> onTransitionTour,
            GameMode mode,
            AIDifficulty investigatorDifficulty,
            AIDifficulty jackDifficulty,
            String humanRole
    ) {
        this.gameEngine = gameEngine;
        this.infoJeuPanel = infoJeuPanel;
        this.plateauPanel = plateauPanel;
        this.timeTokensPanel = timeTokensPanel;
        this.alibiPanel = alibiPanel;
        this.jackInfoPanel = jackInfoPanel;
        this.onTransitionTour = onTransitionTour;
        this.mode = mode;
        this.investigatorDifficulty = investigatorDifficulty;
        this.jackDifficulty = jackDifficulty;
        this.humanRole = humanRole;
        this.boutonClickMusique = new BoutonClickMusique();

        setupAIPlayers();

        setLayout(new GridLayout(0, 2, 8, 8));
        setOpaque(false);
        setPreferredSize(new Dimension(220, 360));
        setMaximumSize(new Dimension(220, 360));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dernierJoueurAffiche = String.valueOf(gameEngine.getCurrentPlayer());

        afficherActions();
    }

    private void synchroniserGameStateDansVues() {
        plateauPanel.setGameState(gameEngine.getGameState());

        if (timeTokensPanel != null) {
            timeTokensPanel.setGameState(gameEngine.getGameState());
        }
    }

    public void rafraichir() {
        removeAll();
        afficherActions();
        revalidate();
        repaint();
    }

    private void rafraichirToutesLesVues() {
        synchroniserGameStateDansVues();

        plateauPanel.rafraichir();
        infoJeuPanel.rafraichir();

        if (timeTokensPanel != null) {
            timeTokensPanel.rafraichir();
        }

        if (jackInfoPanel != null) {
            jackInfoPanel.rafraichir();
        }

        rafraichir();
        verifierGameOverEtAfficher();
    }

    private void afficherActions() {
        Token[] actionTokens = gameEngine.getGameState()
                .getActionTokens()
                .getActionTokens();

        for (int i = 0; i < actionTokens.length; i++) {
            Token token = actionTokens[i];
            JPanel tokenPanel = creerActionTokenPanel(token, i);
            add(tokenPanel);
        }

        JButton undoButton = creerBoutonControle("Undo", new Color(55, 55, 70));
        undoButton.addActionListener(e -> {
            try {
                boutonClickMusique.jouerClickAction();

                actionEnCours = false;
                plateauPanel.clearDetectiveMoveSelection();
                plateauPanel.setTileSelectionListener(null);

                gameEngine.undo();
                rafraichirToutesLesVues();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur undo",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        add(undoButton);

        JButton redoButton = creerBoutonControle("Redo", new Color(55, 55, 70));
        redoButton.addActionListener(e -> {
            try {
                boutonClickMusique.jouerClickAction();

                actionEnCours = false;
                plateauPanel.clearDetectiveMoveSelection();
                plateauPanel.setTileSelectionListener(null);

                gameEngine.redo();
                rafraichirToutesLesVues();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur redo",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        add(redoButton);

        if (mode == GameMode.HUMAN_VS_HUMAN || mode == GameMode.HUMAN_VS_IA) {
            JButton hintButton = creerBoutonControle("Conseil IA", new Color(70, 55, 90));

            hintButton.addActionListener(e -> {
                try {
                    boutonClickMusique.jouerClickAction();
                    afficherConseilIA();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Erreur conseil IA",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            add(hintButton);
        }

        if (mode == GameMode.IA_VS_IA || mode == GameMode.HUMAN_VS_IA) {
            JButton nextAIButton = creerBoutonControle("Next IA", new Color(80, 65, 35));

            nextAIButton.addActionListener(e -> {
                try {
                    boutonClickMusique.jouerClickAction();

                    if (!isAITurn()) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Ce n'est pas le tour de l'IA.",
                                "Tour humain",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    autoAIEnabled = false;
                    updateAutoAIButtonText();
                    jouerSiTourIA();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Erreur IA",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            add(nextAIButton);
        }

        if (mode == GameMode.IA_VS_IA) {
            autoAIButton = creerBoutonControle("Auto IA", new Color(45, 85, 45));

            autoAIButton.addActionListener(e -> {
                boutonClickMusique.jouerClickAction();

                autoAIEnabled = !autoAIEnabled;
                updateAutoAIButtonText();

                if (autoAIEnabled) {
                    jouerSiTourIA();
                }
            });

            add(autoAIButton);

            JButton speedButton = creerBoutonControle(getAutoSpeedText(), new Color(55, 70, 90));
            speedButton.addActionListener(e -> {
                boutonClickMusique.jouerClickAction();

                if (autoAIDelayMs == 1000) {
                    autoAIDelayMs = 700;
                    speedButton.setText("Speed: Normal");
                } else if (autoAIDelayMs == 700) {
                    autoAIDelayMs = 300;
                    speedButton.setText("Speed: Fast");
                } else {
                    autoAIDelayMs = 1000;
                    speedButton.setText("Speed: Slow");
                }
            });

            add(speedButton);
        }
    }

    private void afficherConseilIA() {
        if (gameEngine.getGameState().isGameOver()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La partie est terminée.",
                    "Conseil IA",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (gameEngine.isRoundOver()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La manche est terminée. Passez à la manche suivante avant de demander un conseil.",
                    "Conseil IA",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (mode == GameMode.HUMAN_VS_IA && isAITurn()) {
            JOptionPane.showMessageDialog(
                    this,
                    "C'est le tour de l'IA. Utilisez Next IA pour jouer son coup.",
                    "Conseil IA",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        EvaluationPerspective perspective;

        if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {
            perspective = EvaluationPerspective.INVESTIGATOR;
        } else {
            perspective = EvaluationPerspective.JACK;
        }

        MinimaxAI expertAI = new MinimaxAI(4, perspective);
        AIMove suggestedMove = expertAI.chooseBestMove(gameEngine);

        String message = formatConseilIA(suggestedMove);

        JOptionPane.showMessageDialog(
                this,
                "Suggestion IA Expert pour " + gameEngine.getCurrentPlayer() + " :\n\n" + message,
                "Conseil IA",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String formatConseilIA(AIMove move) {
        if (move == null) {
            return "Aucun coup disponible.";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Choisir le token numéro ")
                .append(move.getActionTokenIndex())
                .append("\n\n");

        switch (move.getActionType()) {
            case HOLMES:
                sb.append("Déplacer Holmes de ")
                        .append(move.getSteps())
                        .append(" case(s).");
                break;

            case WATSON:
                sb.append("Déplacer Watson de ")
                        .append(move.getSteps())
                        .append(" case(s).");
                break;

            case TOBY:
                sb.append("Déplacer Toby de ")
                        .append(move.getSteps())
                        .append(" case(s).");
                break;

            case JOKER:
                if (move.isJokerSkip()) {
                    sb.append("Ne pas déplacer de détective avec le Joker.");
                } else {
                    sb.append("Utiliser le Joker pour déplacer ")
                            .append(move.getDetectiveName())
                            .append(".");
                }
                break;

            case ALIBI:
                sb.append("Piocher une carte Alibi.");
                break;

            case ROTATE:
                sb.append("Tourner la tuile ")
                        .append(getNomTuile(move.getRow(), move.getCol()))
                        .append(" en position (")
                        .append(move.getRow())
                        .append(",")
                        .append(move.getCol())
                        .append(") de ")
                        .append(move.getRotations())
                        .append(" rotation(s).");
                break;

            case EXCHANGE:
                sb.append("Échanger la tuile ")
                        .append(getNomTuile(move.getRowA(), move.getColA()))
                        .append(" en position (")
                        .append(move.getRowA())
                        .append(",")
                        .append(move.getColA())
                        .append(") avec la tuile ")
                        .append(getNomTuile(move.getRowB(), move.getColB()))
                        .append(" en position (")
                        .append(move.getRowB())
                        .append(",")
                        .append(move.getColB())
                        .append(").");
                break;

            default:
                sb.append(move.toString());
                break;
        }

        return sb.toString();
    }

    private String getNomTuile(int row, int col) {
        try {
            Tile tile = gameEngine.getGameState().getBoard().getTile(row, col);

            if (tile == null || tile.getCharacter() == null) {
                return "vide";
            }

            return tile.getCharacter().getName();

        } catch (Exception e) {
            return "inconnue";
        }
    }

    private void updateAutoAIButtonText() {
        if (autoAIButton == null) {
            return;
        }

        if (autoAIEnabled) {
            autoAIButton.setText("Pause IA");
            autoAIButton.setBackground(new Color(95, 55, 45));
        } else {
            autoAIButton.setText("Auto IA");
            autoAIButton.setBackground(new Color(45, 85, 45));
        }
    }

    private JButton creerBoutonControle(String texte, Color couleur) {
        JButton bouton = new JButton(texte){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(45, 45, 45));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(85, 85, 85));
                } else {
                    g2.setColor(new Color(65, 65, 65));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        bouton.setFont(new Font("Arial", Font.BOLD, 13));
        bouton.setForeground(new Color(245, 235, 210));

        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);

        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return bouton;
    }

    private JPanel creerActionTokenPanel(Token token, int index) {
        JPanel tokenPanel = new JPanel(new BorderLayout());
        tokenPanel.setPreferredSize(new Dimension(90, 90));
        tokenPanel.setOpaque(false);
        tokenPanel.setBorder(null);

        if (token.isUsed()) {
            tokenPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            tokenPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        String actionName = token.getCurrentSide();

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon icon = chargerImageAction(actionName);

        if (icon != null) {
            if (token.isUsed()) {
                imageLabel.setIcon(griserIcone(icon));
            } else {
                imageLabel.setIcon(icon);
            }
        } else {
            imageLabel.setText(actionName);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 13));
            imageLabel.setForeground(token.isUsed()
                    ? new Color(130, 130, 130)
                    : new Color(245, 235, 210));
        }

        if (token.isUsed()) {
            imageLabel.setEnabled(false);
        }

        tokenPanel.add(imageLabel, BorderLayout.CENTER);

        tokenPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (token.isUsed()) {
                        return;
                    }

                    if (isAITurn()) {
                        JOptionPane.showMessageDialog(
                                tokenPanel,
                                "C'est le tour de l'IA.",
                                "Tour IA",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    if (actionEnCours) {
                        JOptionPane.showMessageDialog(
                                tokenPanel,
                                "Une action est déjà en cours. Terminez-la avant de choisir un autre token.",
                                "Action en cours",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    boutonClickMusique.jouerClickAction();

                    plateauPanel.clearTileHighlight();

                    String joueurAvantAction = String.valueOf(gameEngine.getCurrentPlayer());

                    Token selectedToken = gameEngine.selectActionToken(index);
                    actionEnCours = true;

                    System.out.println("Action token choisi : " + selectedToken.getCurrentSide());
                    System.out.println("Type action : " + gameEngine.getSelectedActionType());

                    tokenPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    infoJeuPanel.rafraichir();
                    traiterActionSelectionnee(joueurAvantAction);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            tokenPanel,
                            ex.getMessage(),
                            "Erreur action",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        return tokenPanel;
    }

    private void traiterActionSelectionnee(String joueurAvantAction) {
        try {
            ActionType actionType = gameEngine.getSelectedActionType();

            switch (actionType) {
                case HOLMES:
                    preparerDeplacementDetective("Holmes", joueurAvantAction);
                    return;

                case WATSON:
                    preparerDeplacementDetective("Watson", joueurAvantAction);
                    return;

                case TOBY:
                    preparerDeplacementDetective("Toby", joueurAvantAction);
                    return;

                case ALIBI:
                    traiterAlibi();
                    break;

                case ROTATE:
                    preparerRotate(joueurAvantAction);
                    return;

                case EXCHANGE:
                    preparerEchange(joueurAvantAction);
                    return;

                case JOKER:
                    traiterJoker();
                    return;
            }

            actionEnCours = false;
            rafraichirToutesLesVues();

            if (gameEngine.isRoundOver()) {
                verifierFinDeRoundEtTransition();
            } else {
                verifierChangementDeJoueurEtTransition(joueurAvantAction);
            }

        } catch (Exception ex) {
            actionEnCours = false;

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur action",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void continuerApresHighlight(String joueurAvantAction) {
        Timer timer = new Timer(1300, e -> {
            if (gameEngine.isRoundOver()) {
                verifierFinDeRoundEtTransition();
            } else {
                verifierChangementDeJoueurEtTransition(joueurAvantAction);
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void verifierFinDeRoundEtTransition() {
        if (!gameEngine.isRoundOver()) {
            return;
        }

        try {
            if (mode != GameMode.IA_VS_IA || !autoAIEnabled) {
                afficherWitnessPhaseDialog();
            }

            gameEngine.endRound();

            plateauPanel.setTileSelectionListener(null);
            actionEnCours = false;

            rafraichirToutesLesVues();

            if (onTransitionTour != null
                    && mode == GameMode.HUMAN_VS_HUMAN
                    && !gameEngine.getGameState().isGameOver()) {
                dernierJoueurAffiche = String.valueOf(gameEngine.getCurrentPlayer());
                onTransitionTour.accept("Changement de tour");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur fin de round",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void verifierChangementDeJoueurEtTransition(String joueurAvantAction) {
        if (mode != GameMode.HUMAN_VS_HUMAN) {
            return;
        }

        String joueurApresAction = String.valueOf(gameEngine.getCurrentPlayer());

        if (joueurAvantAction == null) {
            joueurAvantAction = "";
        }

        if (!joueurAvantAction.equals(joueurApresAction)) {
            dernierJoueurAffiche = joueurApresAction;

            if (onTransitionTour != null && !gameEngine.getGameState().isGameOver()) {
                onTransitionTour.accept("Joueur");
            }
        }
    }

    private void traiterAlibi() {
        if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {
            GameCharacter eliminated = gameEngine.investigatorDrawsAlibi();

            if (eliminated != null) {
                ParcheminDialog.InspecteurAlibiMessage(plateauPanel);

                afficherAlibiDetective(eliminated);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Le paquet Alibi est vide.",
                        "Alibi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } else {
            gameEngine.jackDrawsAlibi();

            ParcheminDialog.JackAlibiMessage(plateauPanel);
        }
    }

    private void traiterJoker() {
        boolean jackTurn = gameEngine.getGameState().getTurnManager().isJackTurn();

        JDialog jokerDialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Joker",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        jokerDialog.setSize(360, jackTurn ? 260 : 210);
        jokerDialog.setLocationRelativeTo(this);
        jokerDialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel label = new JLabel("Choisissez un détective");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(245, 235, 210));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel boutonsDetectives = new JPanel(new GridLayout(1, 3, 10, 0));
        boutonsDetectives.setOpaque(false);
        boutonsDetectives.setMaximumSize(new Dimension(280, 55));

        JButton holmesButton = creerBoutonControle("Holmes", new Color(55, 45, 35));
        JButton watsonButton = creerBoutonControle("Watson", new Color(55, 45, 35));
        JButton tobyButton = creerBoutonControle("Toby", new Color(55, 45, 35));

        boutonsDetectives.add(holmesButton);
        boutonsDetectives.add(watsonButton);
        boutonsDetectives.add(tobyButton);

        holmesButton.addActionListener(e -> {
            jokerDialog.dispose();
            resoudreJokerAvecDetective("Holmes");
        });

        watsonButton.addActionListener(e -> {
            jokerDialog.dispose();
            resoudreJokerAvecDetective("Watson");
        });

        tobyButton.addActionListener(e -> {
            jokerDialog.dispose();
            resoudreJokerAvecDetective("Toby");
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(boutonsDetectives);

        if (jackTurn) {
            JButton skipButton = creerBoutonControle("Ne pas bouger", new Color(80, 45, 45));
            skipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            skipButton.setMaximumSize(new Dimension(220, 45));

            skipButton.addActionListener(e -> {
                try {
                    jokerDialog.dispose();
                    gameEngine.skipJokerMove();

                    actionEnCours = false;
                    plateauPanel.clearDetectiveMoveSelection();
                    rafraichirToutesLesVues();

                    if (gameEngine.isRoundOver()) {
                        verifierFinDeRoundEtTransition();
                    } else {
                        verifierChangementDeJoueurEtTransition(dernierJoueurAffiche);
                    }

                } catch (Exception ex) {
                    actionEnCours = false;

                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Erreur Joker",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            panel.add(Box.createVerticalStrut(18));
            panel.add(skipButton);
        }

        jokerDialog.setContentPane(panel);
        jokerDialog.setVisible(true);
    }

    private void resoudreJokerAvecDetective(String detectiveName) {
        try {
            gameEngine.moveDetectiveWithJoker(detectiveName);

            actionEnCours = false;
            plateauPanel.clearDetectiveMoveSelection();
            rafraichirToutesLesVues();

            if (gameEngine.isRoundOver()) {
                verifierFinDeRoundEtTransition();
            } else {
                verifierChangementDeJoueurEtTransition(dernierJoueurAffiche);
            }

        } catch (Exception ex) {
            actionEnCours = false;

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur Joker",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void preparerRotate(String joueurAvantAction) {
        plateauPanel.clearDetectiveMoveSelection();

        ParcheminDialog.showMessage(
            plateauPanel,
            "Rotate",
            "Sélectionnez une carte à tourner sur le plateau."
        );

        plateauPanel.setTileSelectionListener(tile -> {
            try {
                int rotations = ParcheminDialog.demanderRotation(plateauPanel);

                if (rotations == 0) {
                    ParcheminDialog.showMessage(
                            plateauPanel,
                            "Action obligatoire",
                            "L'action Rotate est déjà choisie. Sélectionnez une rotation pour terminer l'action."
                    );
                    return;
                }

                int row = tile.getRow();
                int col = tile.getCol();

                gameEngine.rotateTile(tile, rotations);

                /*JOptionPane.showMessageDialog(
                        this,
                        "Carte tournée : "
                                + tile.getCharacter().getName()
                                + " (" + row + "," + col + ") "
                                + rotations + " fois.",
                        "Rotate",
                        JOptionPane.INFORMATION_MESSAGE
                );*/

                plateauPanel.setTileSelectionListener(null);
                actionEnCours = false;

                rafraichirToutesLesVues();
                plateauPanel.highlightTileTemporarily(row, col);

                continuerApresHighlight(joueurAvantAction);

            } catch (Exception ex) {
                actionEnCours = false;

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur Rotate",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void preparerEchange(String joueurAvantAction) {
        plateauPanel.clearDetectiveMoveSelection();

        ParcheminDialog.EchangeMessage(plateauPanel);

        final Tile[] premiereCarte = new Tile[1];

        plateauPanel.setTileSelectionListener(tile -> {
            try {
                if (premiereCarte[0] == null) {
                    premiereCarte[0] = tile;

                    ParcheminDialog.Echange2Message(plateauPanel);

                    return;
                }

                Tile deuxiemeCarte = tile;

                if (premiereCarte[0] == deuxiemeCarte) {
                    ParcheminDialog.EchangeErreurMessage(plateauPanel);
                    return;
                }

                int row1 = premiereCarte[0].getRow();
                int col1 = premiereCarte[0].getCol();
                int row2 = deuxiemeCarte.getRow();
                int col2 = deuxiemeCarte.getCol();

                gameEngine.exchangeTiles(premiereCarte[0], deuxiemeCarte);

                plateauPanel.setTileSelectionListener(null);
                actionEnCours = false;

                rafraichirToutesLesVues();
                plateauPanel.highlightTilesTemporarily(row1, col1, row2, col2);

                continuerApresHighlight(joueurAvantAction);

            } catch (Exception ex) {
                actionEnCours = false;

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur Echange",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void preparerDeplacementDetective(String detectiveName, String joueurAvantAction) {
        int currentPosition;

        if ("Holmes".equals(detectiveName)) {
            currentPosition = gameEngine.getGameState().getDetectiveTokens().getHolmes().getPosition();
        } else if ("Watson".equals(detectiveName)) {
            currentPosition = gameEngine.getGameState().getDetectiveTokens().getWatson().getPosition();
        } else {
            currentPosition = gameEngine.getGameState().getDetectiveTokens().getToby().getPosition();
        }

        java.util.List<Integer> positions = new java.util.ArrayList<>();
        positions.add((currentPosition + 1) % 12);
        positions.add((currentPosition + 2) % 12);

        plateauPanel.setDetectiveMoveSelection(
                positions,
                "Choisissez votre déplacement",
                selectedPosition -> {
                    try {
                        int steps = (selectedPosition - currentPosition + 12) % 12;

                        if (steps == 0) {
                            steps = 12;
                        }

                        if ("Holmes".equals(detectiveName)) {
                            gameEngine.moveHolmes(steps);
                        } else if ("Watson".equals(detectiveName)) {
                            gameEngine.moveWatson(steps);
                        } else {
                            gameEngine.moveToby(steps);
                        }

                        plateauPanel.clearDetectiveMoveSelection();
                        actionEnCours = false;
                        rafraichirToutesLesVues();

                        if (gameEngine.isRoundOver()) {
                            verifierFinDeRoundEtTransition();
                        } else {
                            verifierChangementDeJoueurEtTransition(joueurAvantAction);
                        }

                    } catch (Exception ex) {
                        plateauPanel.clearDetectiveMoveSelection();
                        actionEnCours = false;

                        JOptionPane.showMessageDialog(
                                this,
                                ex.getMessage(),
                                "Erreur déplacement",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }

    private void setupAIPlayers() {
        AIDifficulty selectedInvestigatorDifficulty = investigatorDifficulty;
        AIDifficulty selectedJackDifficulty = jackDifficulty;

        if (selectedInvestigatorDifficulty == null) {
            selectedInvestigatorDifficulty = AIDifficulty.HARD;
        }

        if (selectedJackDifficulty == null) {
            selectedJackDifficulty = AIDifficulty.HARD;
        }

        if (mode == GameMode.HUMAN_VS_IA) {
            if ("Investigator".equals(humanRole)) {
                jackAI = AIFactory.create(selectedJackDifficulty, EvaluationPerspective.JACK);
            } else {
                investigatorAI = AIFactory.create(
                        selectedInvestigatorDifficulty,
                        EvaluationPerspective.INVESTIGATOR
                );
            }

        } else if (mode == GameMode.IA_VS_IA) {
            investigatorAI = AIFactory.create(
                    selectedInvestigatorDifficulty,
                    EvaluationPerspective.INVESTIGATOR
            );

            jackAI = AIFactory.create(
                    selectedJackDifficulty,
                    EvaluationPerspective.JACK
            );
        }
    }

    private boolean isAITurn() {
        if (mode == null) {
            return false;
        }

        if (gameEngine.getGameState().isGameOver()) {
            return false;
        }

        if (gameEngine.isRoundOver()) {
            return false;
        }

        boolean investigatorTurn = gameEngine.getGameState().getTurnManager().isInvestigatorTurn();
        boolean jackTurn = gameEngine.getGameState().getTurnManager().isJackTurn();

        if (mode == GameMode.IA_VS_IA) {
            return investigatorTurn || jackTurn;
        }

        if (mode == GameMode.HUMAN_VS_IA) {
            if ("Investigator".equals(humanRole)) {
                return jackTurn;
            }

            return investigatorTurn;
        }

        return false;
    }

    private AIPlayer getCurrentAI() {
        if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {
            return investigatorAI;
        }

        if (gameEngine.getGameState().getTurnManager().isJackTurn()) {
            return jackAI;
        }

        return null;
    }

    private void jouerSiTourIA() {
        if (aiEnCours || actionEnCours) {
            return;
        }

        if (!isAITurn()) {
            autoAIEnabled = false;
            updateAutoAIButtonText();
            return;
        }

        AIPlayer currentAI = getCurrentAI();

        if (currentAI == null) {
            autoAIEnabled = false;
            updateAutoAIButtonText();
            return;
        }

        aiEnCours = true;

        Timer timer = new Timer(500, e -> {
            try {
                String joueurAvantAction = String.valueOf(gameEngine.getCurrentPlayer());

                currentAI.play(gameEngine);

                plateauPanel.setTileSelectionListener(null);
                actionEnCours = false;

                rafraichirToutesLesVues();

                if (gameEngine.isRoundOver() && !gameEngine.getGameState().isGameOver()) {
                    if (mode != GameMode.IA_VS_IA || !autoAIEnabled) {
                        afficherWitnessPhaseDialog();
                    }

                    gameEngine.endRound();
                    rafraichirToutesLesVues();

                    if (onTransitionTour != null
                            && mode == GameMode.HUMAN_VS_HUMAN
                            && !gameEngine.getGameState().isGameOver()) {
                        dernierJoueurAffiche = String.valueOf(gameEngine.getCurrentPlayer());

                        Timer transitionTimer = new Timer(
                                1000,
                                evt -> onTransitionTour.accept("Changement de tour")
                        );

                        transitionTimer.setRepeats(false);
                        transitionTimer.start();
                    }

                } else {
                    verifierChangementDeJoueurEtTransition(joueurAvantAction);
                }

                if (gameEngine.getGameState().isGameOver()) {
                    autoAIEnabled = false;
                    updateAutoAIButtonText();
                }

                aiEnCours = false;

                if (mode == GameMode.IA_VS_IA
                        && autoAIEnabled
                        && !gameEngine.getGameState().isGameOver()) {
                    Timer autoTimer = new Timer(autoAIDelayMs, next -> jouerSiTourIA());
                    autoTimer.setRepeats(false);
                    autoTimer.start();
                }

            } catch (Exception ex) {
                aiEnCours = false;
                autoAIEnabled = false;
                updateAutoAIButtonText();

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur IA",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private String getAutoSpeedText() {
        if (autoAIDelayMs == 1000) {
            return "Speed: Slow";
        }

        if (autoAIDelayMs == 300) {
            return "Speed: Fast";
        }

        return "Speed: Normal";
    }

    private ImageIcon chargerImageAction(String actionName) {
        String nomFichier = getNomFichierAction(actionName);

        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier
                + ".png";

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image action non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image action illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageAction(imageOriginale, 85, 85);
            return new ImageIcon(imageRedimensionnee);

        } catch (Exception e) {
            System.out.println("Erreur chargement image action : " + e.getMessage());
            return null;
        }
    }

    private String getNomFichierAction(String actionName) {
        String action = actionName.toLowerCase();

        if (action.contains("holmes")) {
            return "sherlock_action";
        }

        if (action.contains("watson")) {
            return "watson_action";
        }

        if (action.contains("toby")) {
            return "toby_action";
        }

        if (action.contains("rotate")) {
            return "rotate_action";
        }

        if (action.contains("exchange")) {
            return "exchange";
        }

        if (action.contains("alibi")) {
            return "alibi_action";
        }

        if (action.contains("joker")) {
            return "joker_action";
        }

        return action.replace(" ", "_");
    }

    private BufferedImage redimensionnerImageAction(BufferedImage imageOriginale, int largeurMax, int hauteurMax) {
        int largeurOriginale = imageOriginale.getWidth();
        int hauteurOriginale = imageOriginale.getHeight();

        double ratioLargeur = (double) largeurMax / largeurOriginale;
        double ratioHauteur = (double) hauteurMax / hauteurOriginale;
        double ratio = Math.min(ratioLargeur, ratioHauteur);

        int nouvelleLargeur = (int) (largeurOriginale * ratio);
        int nouvelleHauteur = (int) (hauteurOriginale * ratio);

        BufferedImage imageRedimensionnee = new BufferedImage(
                largeurMax,
                hauteurMax,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imageRedimensionnee.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = (largeurMax - nouvelleLargeur) / 2;
        int y = (hauteurMax - nouvelleHauteur) / 2;

        g.drawImage(imageOriginale, x, y, nouvelleLargeur, nouvelleHauteur, null);
        g.dispose();

        return imageRedimensionnee;
    }

    private ImageIcon griserIcone(ImageIcon icon) {
        Image image = icon.getImage();

        BufferedImage original = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = original.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        BufferedImage grisee = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int rgba = original.getRGB(x, y);
                Color color = new Color(rgba, true);

                int alpha = color.getAlpha();
                int gris = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                Color nouvelleCouleur = new Color(
                        gris,
                        gris,
                        gris,
                        alpha / 2
                );

                grisee.setRGB(x, y, nouvelleCouleur.getRGB());
            }
        }

        return new ImageIcon(grisee);
    }

    private void verifierGameOverEtAfficher() {
        if (gameOverDialogShown) {
            return;
        }

        if (!gameEngine.getGameState().isGameOver()) {
            return;
        }

        gameOverDialogShown = true;
        autoAIEnabled = false;
        updateAutoAIButtonText();

        String winner = gameEngine.getGameState().getWinner();
        Window window = SwingUtilities.getWindowAncestor(this);

        if (window instanceof FenetrePrincipale) {
            FenetrePrincipale fenetre = (FenetrePrincipale) window;
            String jackNom = "Inconnu";

            if (gameEngine.getGameState().getJackCharacter() != null) {
                jackNom = gameEngine.getGameState().getJackCharacter().getName();
            }

            int suspectsRestants = 0;
            for (GameCharacter character : gameEngine.getGameState().getCharacters()) {
                if (!character.isEliminated()) suspectsRestants++;
            }

            int hourglasses = HourglassCalculator.calculateActualJackHourglasses(
                gameEngine.getGameState().getAlibiDeckManager(),
                gameEngine.getGameState().getJackPlayerState()
            );
            
            FinPartiePanel finPanel = new FinPartiePanel(fenetre, winner, jackNom, suspectsRestants, hourglasses);

            fenetre.setContentPane(finPanel);
            fenetre.revalidate();
            fenetre.repaint();
        } else {
            String message;
            
            if ("Investigator".equals(winner)) {
                message = "L'investigateur gagne la partie.";
            } else if ("Jack".equals(winner)) {
                message = "Jack gagne la partie.";
            } else {
                message = "La partie est terminée.";
            }

            JOptionPane.showMessageDialog(
                this,
                message,
                "Jeu terminé",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private boolean isJackVisibleForWitnessPhase() {
        try {
            GameCharacter jack = gameEngine.getGameState().getJackCharacter();

            if (jack == null) {
                return false;
            }

            java.util.List<GameCharacter> visibleCharacters =
                    gameEngine.getGameState()
                            .getLineOfSightService()
                            .getVisibleCharacters(
                                    gameEngine.getGameState().getBoard(),
                                    gameEngine.getGameState().getDetectiveTokens(),
                                    gameEngine.getGameState().getCharacters()
                            );

            for (GameCharacter character : visibleCharacters) {
                if (character.getId() == jack.getId()) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private void afficherWitnessPhaseDialog() {
    boolean jackVisible = isJackVisibleForWitnessPhase();

    ParcheminDialog.showWitnessPhaseMessage(
            plateauPanel,
            jackVisible
    );
}
    private ImageIcon chargerImageAlibiCharacter(GameCharacter character) {
        String nomFichier = character.getName().toLowerCase()
                .replace(".", "")
                .replace("'", "")
                .replace("-", "_")
                .replace(" ", "_");

        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/identity_"
                + nomFichier
                + ".png";

        File fichier = new File(chemin);

        if (!fichier.exists()) {
            System.out.println("Image alibi identity non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichier);

            if (imageOriginale == null) {
                return null;
            }

            BufferedImage imageFinale = redimensionnerImageAlibi(imageOriginale, 155, 155);
            return new ImageIcon(imageFinale);

        } catch (Exception e) {
            System.out.println("Erreur image alibi identity : " + e.getMessage());
            return null;
        }
    }

    private BufferedImage redimensionnerImageAlibi(BufferedImage imageOriginale, int largeurMax, int hauteurMax) {
        int largeurOriginale = imageOriginale.getWidth();
        int hauteurOriginale = imageOriginale.getHeight();

        double ratioLargeur = (double) largeurMax / largeurOriginale;
        double ratioHauteur = (double) hauteurMax / hauteurOriginale;
        double ratio = Math.min(ratioLargeur, ratioHauteur);

        int nouvelleLargeur = (int) Math.round(largeurOriginale * ratio);
        int nouvelleHauteur = (int) Math.round(hauteurOriginale * ratio);

        BufferedImage imageRedimensionnee = new BufferedImage(
                largeurMax,
                hauteurMax,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imageRedimensionnee.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = (largeurMax - nouvelleLargeur) / 2;
        int y = (hauteurMax - nouvelleHauteur) / 2;

        g.drawImage(imageOriginale, x, y, nouvelleLargeur, nouvelleHauteur, null);
        g.dispose();

        return imageRedimensionnee;
    }

    private JButton creerBoutonAlibi(String texte) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(95, 65, 35, 210));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(150, 105, 55, 200));
                } else {
                    g2.setColor(new Color(120, 82, 42, 165));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor(new Color(55, 35, 20, 230));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        bouton.setFont(new Font("Serif", Font.BOLD, 22));
        bouton.setForeground(new Color(35, 22, 12));
        bouton.setFocusPainted(false);
        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return bouton;
    }

    private void afficherAlibiDetective(GameCharacter character) {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(plateauPanel),
                "Alibi",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setUndecorated(true);
        dialog.setSize(520, 560);
        dialog.setResizable(false);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(null) {
            private BufferedImage background;

            {
                String chemin = System.getProperty("user.dir")
                        + "/assets/images/action_message/alibi_piochée.png";

                try {
                    background = ImageIO.read(new File(chemin));
                } catch (Exception e) {
                    System.out.println("Image alibi_piochee non trouvée : " + chemin);
                    background = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (background != null) {
                    g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setColor(new Color(238, 218, 175));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                }

                g2.dispose();
            }
        };

        panel.setOpaque(false);

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setBounds(180, 218, 160, 160);

        ImageIcon characterIcon = chargerImageAlibiCharacter(character);

        if (characterIcon != null) {
            imageLabel.setIcon(characterIcon);
        } else {
            imageLabel.setText(character.getName());
            imageLabel.setFont(new Font("Serif", Font.BOLD, 18));
            imageLabel.setForeground(new Color(45, 32, 20));
        }

        JButton okButton = creerBoutonAlibi("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(imageLabel);
        panel.add(okButton);

        dialog.setContentPane(panel);
        ParcheminDialog.afficherAvecFondAssombri(plateauPanel, dialog);
    }
}