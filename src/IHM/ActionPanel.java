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

    private java.util.Map<String, BufferedImage> cacheImages = new java.util.HashMap<>();

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

        setLayout(new GridLayout(0, 2, 2, 2));
        setOpaque(false);
        //setPreferredSize(new Dimension(220, 360));
        //setMaximumSize(new Dimension(220, 360));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new Dimension(120, 180));

        dernierJoueurAffiche = String.valueOf(gameEngine.getCurrentPlayer());

        mettreAJourResponsiveLayout();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    mettreAJourResponsiveLayout();
                    revalidate();
                    repaint();
                });
            }
        });

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

        if (alibiPanel != null){
            alibiPanel.rafraichir();
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
                boutonClickMusique.jouerClick();

                actionEnCours = false;
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
                boutonClickMusique.jouerClick();

                actionEnCours = false;
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
                    boutonClickMusique.jouerClick();
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
                    boutonClickMusique.jouerClick();

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
                boutonClickMusique.jouerClick();

                autoAIEnabled = !autoAIEnabled;
                updateAutoAIButtonText();

                if (autoAIEnabled) {
                    jouerSiTourIA();
                }
            });

            add(autoAIButton);
            JButton speedButton = creerBoutonControle(getAutoSpeedText(), new Color(55, 70, 90));
            speedButton.addActionListener(e -> {
                boutonClickMusique.jouerClick();

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
    private String getNomTuile(int row, int col){
        try {
            
            Tile tile = gameEngine.getGameState().getBoard().getTile(row, col);
            if (tile == null){
                return "vide";
            }
            if (tile.getCharacter() == null){
                return "vide";
            }
            return tile.getCharacter().getName();
        } catch (Exception e){
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
        JButton bouton = new JButton(texte);

        int tailleFont = Math.max(10, getHeight() / 36);

        bouton.setFont(new Font("Arial", Font.BOLD, tailleFont));
        bouton.setBackground(couleur);
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bouton.setMargin(new Insets(0,0,0,0));

        return bouton;
    }

    private JPanel creerActionTokenPanel(Token token, int index) {
        JPanel tokenPanel = new JPanel(new BorderLayout());

        int taillePanel = Math.max(80, getWidth() / 2 - 8);
        taillePanel = Math.min(taillePanel, 125);

        tokenPanel.setPreferredSize(new Dimension(taillePanel, taillePanel));

        tokenPanel.setMaximumSize(new Dimension(taillePanel, taillePanel));
        
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

        int taille = Math.min(getWidth(), getHeight()) / 5;

        taille = Math.max(65, taille);
        taille = Math.min(110, taille);

        if (taille <= 0){
            taille = 100;
        }

        ImageIcon icon = chargerImageAction(actionName, taille);

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
            public void mouseClicked(MouseEvent e) {
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

                    boutonClickMusique.jouerClick();

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
                    gameEngine.moveHolmes(demanderNombrePas("Holmes"));
                    break;

                case WATSON:
                    gameEngine.moveWatson(demanderNombrePas("Watson"));
                    break;

                case TOBY:
                    gameEngine.moveToby(demanderNombrePas("Toby"));
                    break;

                case ALIBI:
                    traiterAlibi();
                    break;

                case ROTATE:
                    preparerRotate(joueurAvantAction);
                    break;

                case EXCHANGE:
                    preparerExchange(joueurAvantAction);
                    break;

                case JOKER:
                    traiterJoker();
                    break;
            }

            if (actionType != ActionType.ROTATE && actionType != ActionType.EXCHANGE) {
                actionEnCours = false;
                rafraichirToutesLesVues();

                if (gameEngine.isRoundOver()) {
                    verifierFinDeRoundEtTransition();
                } else {
                    verifierChangementDeJoueurEtTransition(joueurAvantAction);
                }
            } else {
                plateauPanel.rafraichir();
                infoJeuPanel.rafraichir();

                if (timeTokensPanel != null) {
                    timeTokensPanel.rafraichir();
                }

                if (jackInfoPanel != null) {
                    jackInfoPanel.rafraichir();
                }
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

    private void verifierFinDeRoundEtTransition() {
        if (!gameEngine.isRoundOver()) {
            return;
        }

        try {
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
                JOptionPane.showMessageDialog(
                        this,
                        "Alibi pioché : " + eliminated.getName(),
                        "Alibi",
                        JOptionPane.INFORMATION_MESSAGE
                );
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

            JOptionPane.showMessageDialog(
                    this,
                    "Jack a pioché une carte Alibi.",
                    "Alibi",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void traiterJoker() {
        boolean jackTurn = gameEngine.getGameState().getTurnManager().isJackTurn();
        String[] choix;

        if (jackTurn) {
            choix = new String[] {"Holmes", "Watson", "Toby", "Ne pas bouger"};
        } else {
            choix = new String[] {"Holmes", "Watson", "Toby"};
        }

        String reponse = null;

        while (reponse == null) {
            reponse = (String) JOptionPane.showInputDialog(
                    this,
                    "Choisissez un détective pour l'action Joker:",
                    "Joker",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choix,
                    choix[0]
            );

            if (reponse == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "L'action Joker est déjà choisie. Vous devez la résoudre.",
                        "Action obligatoire",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }

        if (reponse.equals("Ne pas bouger")) {
            gameEngine.skipJokerMove();

            JOptionPane.showMessageDialog(
                    this,
                    "Jack ne déplace aucun détective",
                    "Joker",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            gameEngine.moveDetectiveWithJoker(reponse);

            JOptionPane.showMessageDialog(
                    this,
                    reponse + " avance d'une case",
                    "Joker",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void preparerRotate(String joueurAvantAction) {
        JOptionPane.showMessageDialog(
                this,
                "Sélectionnez une carte à tourner sur le plateau.",
                "Rotate",
                JOptionPane.INFORMATION_MESSAGE
        );

        plateauPanel.setTileSelectionListener(tile -> {
            try {
                String[] choix = {"1", "2", "3"};

                String reponse = (String) JOptionPane.showInputDialog(
                        this,
                        "Combien de fois voulez-vous tourner cette carte ?",
                        "Rotate",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choix,
                        choix[0]
                );

                if (reponse == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "L'action Rotate est déjà choisie. Sélectionnez une rotation pour terminer l'action.",
                            "Action obligatoire",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int rotations = Integer.parseInt(reponse);

                gameEngine.rotateTile(tile, rotations);

                JOptionPane.showMessageDialog(
                        this,
                        "Carte tournée : "
                                + tile.getCharacter().getName()
                                + " (" + tile.getRow() + "," + tile.getCol() + ") "
                                + rotations + " fois.",
                        "Rotate",
                        JOptionPane.INFORMATION_MESSAGE
                );

                plateauPanel.setTileSelectionListener(null);
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
                        "Erreur Rotate",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void preparerExchange(String joueurAvantAction) {
        JOptionPane.showMessageDialog(
                this,
                "Sélectionnez deux cartes à échanger sur le plateau.",
                "Exchange",
                JOptionPane.INFORMATION_MESSAGE
        );

        final Tile[] premiereCarte = new Tile[1];

        plateauPanel.setTileSelectionListener(tile -> {
            try {
                if (premiereCarte[0] == null) {
                    premiereCarte[0] = tile;

                    JOptionPane.showMessageDialog(
                            this,
                            "Première carte sélectionnée : "
                                    + tile.getCharacter().getName()
                                    + "\nSélectionnez maintenant la deuxième carte.",
                            "Exchange",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    Tile deuxiemeCarte = tile;

                    if (premiereCarte[0] == deuxiemeCarte) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Vous devez sélectionner deux cartes différentes.",
                                "Exchange",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    gameEngine.exchangeTiles(premiereCarte[0], deuxiemeCarte);

                    JOptionPane.showMessageDialog(
                            this,
                            "Cartes échangées : ("
                                    + premiereCarte[0].getRow() + "," + premiereCarte[0].getCol()
                                    + ") et ("
                                    + deuxiemeCarte.getRow() + "," + deuxiemeCarte.getCol()
                                    + ")",
                            "Exchange",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    plateauPanel.setTileSelectionListener(null);
                    actionEnCours = false;

                    rafraichirToutesLesVues();

                    if (gameEngine.isRoundOver()) {
                        verifierFinDeRoundEtTransition();
                    } else {
                        verifierChangementDeJoueurEtTransition(joueurAvantAction);
                    }
                }

            } catch (Exception ex) {
                actionEnCours = false;

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur Exchange",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private int demanderNombrePas(String detectiveName) {
        String[] choix = {"1", "2"};

        while (true) {
            String reponse = (String) JOptionPane.showInputDialog(
                    this,
                    detectiveName + " doit avancer de combien de pas ?",
                    "Déplacement",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choix,
                    choix[0]
            );

            if (reponse != null) {
                return Integer.parseInt(reponse);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "L'action est déjà choisie. Vous devez sélectionner 1 ou 2.",
                    "Action obligatoire",
                    JOptionPane.WARNING_MESSAGE
            );
        }
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
                    gameEngine.endRound();
                    rafraichirToutesLesVues();

                    if (onTransitionTour != null && mode != GameMode.IA_VS_IA) {
                        dernierJoueurAffiche = String.valueOf(gameEngine.getCurrentPlayer());
                        Timer transitionTimer = new Timer(1000, evt -> onTransitionTour.accept("Changement de tour"));
                        transitionTimer.setRepeats(false);
                        transitionTimer.start();
                    }
                } else {
                    String jouerApresAction = String.valueOf(gameEngine.getCurrentPlayer());
                    
                    if (!joueurAvantAction.equals(jouerApresAction)
                        && onTransitionTour != null
                        && mode == GameMode.HUMAN_VS_IA
                        && !gameEngine.getGameState().isGameOver()) {
                            Timer transitionTimer = new Timer(1000, evt -> onTransitionTour.accept("Joueur"));
                            transitionTimer.setRepeats(false);
                            transitionTimer.start();
                        } else {
                            verifierChangementDeJoueurEtTransition(joueurAvantAction);
                        }
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
    private ImageIcon chargerImageAction(String actionName, int taille) {
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
            BufferedImage imageOriginale = cacheImages.get(nomFichier);

            if (imageOriginale == null){
                imageOriginale = ImageIO.read(fichierImage);
                cacheImages.put(nomFichier, imageOriginale);
            }

            if (imageOriginale == null) {
                System.out.println("Image action illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageAction(imageOriginale, taille, taille);
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
                nouvelleLargeur,
                nouvelleHauteur,
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

        String message;

        if ("Investigator".equals(winner)) {
            message = "L'Investigator gagne la partie.";
        } else if ("Jack".equals(winner)) {
            message = "Jack gagne la partie.";
        } else {
            message = "La partie est terminée.";
        }

        JOptionPane.showMessageDialog(
                this,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mettreAJourResponsiveLayout(){
        int gap = 2;

        GridLayout layout = (GridLayout) getLayout();

        layout.setHgap(gap);
        layout.setVgap(gap);

        int largeurPanel = Math.max(140, getWidth());
        int hauteurPanel = Math.max(220, getHeight());

        int largeurBouton = Math.max(60, largeurPanel / 2 - 12);
        int hauteurBouton = Math.max(32, hauteurPanel / 9);

        largeurBouton = Math.min(largeurBouton, 140);
        hauteurBouton = Math.min(hauteurBouton, 60);

        for (Component c : getComponents()) {

            c.setPreferredSize(new Dimension(
                    largeurBouton,
                    hauteurBouton
            ));

            c.setMaximumSize(new Dimension(
                    largeurBouton,
                    hauteurBouton
            ));

            if (c instanceof JButton) {

                JButton b = (JButton)c;

                int tailleFont = Math.max(10, hauteurBouton / 4);
                tailleFont = Math.min(tailleFont, 15);

                b.setFont(new Font(
                        "Arial",
                        Font.BOLD,
                        tailleFont
                ));
            }
        }

        revalidate();
        repaint();
    }
}