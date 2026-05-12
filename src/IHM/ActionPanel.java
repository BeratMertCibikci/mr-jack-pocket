package IHM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import engine.GameEngine;
import engine.ActionType;
import model.Token;
import model.GameCharacter;
import model.Tile;

public class ActionPanel extends JPanel {// action token panel  

    private GameEngine gameEngine;
    private InfoJeuPanel infoJeuPanel;
    private PlateauPanel plateauPanel;
    private TimeTokensPanel timeTokensPanel;
    private BoutonClickMusique boutonClickMusique;

    public ActionPanel(GameEngine gameEngine, InfoJeuPanel infoJeuPanel, PlateauPanel plateauPanel, TimeTokensPanel timeTokensPanel) {
        //engine, info panel ve plateau alıyor 
        this.gameEngine = gameEngine;
        this.infoJeuPanel = infoJeuPanel;
        this.plateauPanel = plateauPanel;
        this.timeTokensPanel = timeTokensPanel;
        this.boutonClickMusique = new BoutonClickMusique();

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));// ortalı şekilde boşluklu ayarlıyor
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        afficherActions();
    }

    private void afficherActions() {
        Token[] actionTokens = gameEngine.getGameState()
                .getActionTokens()
                .getActionTokens();// on prend les actions tokens du modele 

        for (int i = 0; i < actionTokens.length; i++) {// pour chaque token, on créé un JPanel et on l ajoute dans le panel
            Token token = actionTokens[i];
            JPanel tokenPanel = creerActionTokenPanel(token, i);
            add(tokenPanel);
        }

        JButton tourSuivantButton = new JButton("Tour suivant");
        tourSuivantButton.setFont(new Font("Arial", Font.BOLD, 14));
        tourSuivantButton.setBackground(new Color(80, 65, 35));
        tourSuivantButton.setForeground(new Color(245, 235, 210));
        tourSuivantButton.setFocusPainted(false);
        tourSuivantButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tourSuivantButton.addActionListener(e -> {
            try {
                boutonClickMusique.jouerClick();
                gameEngine.endRound();

                plateauPanel.rafraichir();
                infoJeuPanel.rafraichir();
                if (timeTokensPanel != null) timeTokensPanel.rafraichir();
                removeAll();
                afficherActions();
                revalidate();
                repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur tour suivant",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        add(tourSuivantButton);
    }

    private JPanel creerActionTokenPanel(Token token, int index) {// on crée le visualisation du token
        JPanel tokenPanel = new JPanel(new BorderLayout());
        tokenPanel.setPreferredSize(new Dimension(160, 70)); //pour cahque token on a un dim de 160x70

        tokenPanel.setBackground(new Color(35, 30, 25));
        tokenPanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        tokenPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel tokenLabel = new JLabel(token.getCurrentSide(), SwingConstants.CENTER);// on ecrit la face courrent du token 
        tokenLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tokenLabel.setForeground(new Color(245, 235, 210));

        tokenPanel.add(tokenLabel, BorderLayout.CENTER); // on l ajoute dans le panel 

        tokenPanel.addMouseListener(new MouseAdapter() {// fonction qui nous permet de cliqué le token 
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    boutonClickMusique.jouerClick();

                    Token selectedToken = gameEngine.selectActionToken(index);

                    // lorsqu on clique le token on envoie dans l engine et on controle si ce dernier peut etre choisit
                    System.out.println("Action token choisi : " + selectedToken.getCurrentSide());
                    System.out.println("Type action : " + gameEngine.getSelectedActionType());

                    tokenPanel.setBackground(new Color(70, 60, 50));
                    tokenPanel.setBorder(BorderFactory.createLineBorder(new Color(120, 100, 70), 2));
                    tokenPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    //si choisit on change le couleur 
                    infoJeuPanel.rafraichir();
                    traiterActionSelectionnee();

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

    private void traiterActionSelectionnee() {
        try {
            //cette partie decide quelle mode il faut appeler (main)
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
                    preparerRotate();
                    break;

                case EXCHANGE:
                    preparerExchange();
                    break;

                case JOKER:
                    traiterJoker();
                    break;
            }

            plateauPanel.rafraichir();
            infoJeuPanel.rafraichir();
            if (timeTokensPanel != null) timeTokensPanel.rafraichir();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur action",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void traiterAlibi() {
        if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {// on demande ici si le joueur courrent est un invetigateur ou Jack
            GameCharacter eliminated = gameEngine.investigatorDrawsAlibi();

            if (eliminated != null) {//si Investigateur
                JOptionPane.showMessageDialog(
                        this,
                        "Alibi pioché : " + eliminated.getName(),
                        "Alibi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {//sinon
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

        String reponse = (String) JOptionPane.showInputDialog(
            this,
            "Choisissez un détective pour l'action Joker:",
            "Joker",
            JOptionPane.QUESTION_MESSAGE,
            null,
            choix,
            choix[0]
        );
        if (reponse == null) throw new IllegalStateException("Action Joker annulée.");

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


    private void preparerRotate() {
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
                        JOptionPane.QUESTION_MESSAGE,null,choix,choix[0]);

                if (reponse == null) {
                    plateauPanel.setTileSelectionListener(null);
                    throw new IllegalStateException("Action annulée.");
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
                plateauPanel.rafraichir();
                infoJeuPanel.rafraichir();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur Rotate",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void preparerExchange() {
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
                                    + " (" + tile.getRow() + "," + tile.getCol() + ")",
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
                    plateauPanel.rafraichir();
                    infoJeuPanel.rafraichir();
                }

            } catch (Exception ex) {
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

        String reponse = (String) JOptionPane.showInputDialog(
                this,
                detectiveName + " doit avancer de combien de pas ?",
                "Déplacement",
                JOptionPane.QUESTION_MESSAGE,
                null,
                choix,
                choix[0]
        );

        if (reponse == null) {
            throw new IllegalStateException("Action annulée.");
        }

        return Integer.parseInt(reponse);
    }
} 