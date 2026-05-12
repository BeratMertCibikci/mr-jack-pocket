package IHM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import engine.GameEngine;
import engine.ActionType;
import model.Token;
import model.GameCharacter;
import model.Tile;

public class ActionPanel extends JPanel {

    private GameEngine gameEngine;
    private InfoJeuPanel infoJeuPanel;
    private PlateauPanel plateauPanel;
    private TimeTokensPanel timeTokensPanel;
    private BoutonClickMusique boutonClickMusique;
    private boolean actionEnCours = false;
    private AlibiPanel alibiPanel;

    public ActionPanel(GameEngine gameEngine, InfoJeuPanel infoJeuPanel, PlateauPanel plateauPanel, TimeTokensPanel timeTokensPanel) {
        this.gameEngine = gameEngine;
        this.infoJeuPanel = infoJeuPanel;
        this.plateauPanel = plateauPanel;
        this.timeTokensPanel = timeTokensPanel;
        this.boutonClickMusique = new BoutonClickMusique();

        setLayout(new GridLayout(0, 2, 10, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(220, 360));
        setMaximumSize(new Dimension(220, 360));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        afficherActions();
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

        JButton tourSuivantButton = new JButton("Tour suivant");
        tourSuivantButton.setFont(new Font("Arial", Font.BOLD, 14));
        tourSuivantButton.setBackground(new Color(80, 65, 35));
        tourSuivantButton.setForeground(new Color(245, 235, 210));
        tourSuivantButton.setFocusPainted(false);
        tourSuivantButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tourSuivantButton.setPreferredSize(new Dimension(90, 35));

        tourSuivantButton.addActionListener(e -> {
            try {
                if (actionEnCours) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Une action est en cours. Terminez-la avant de passer au tour suivant.",
                            "Action en cours",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                boutonClickMusique.jouerClick();
                gameEngine.endRound();

                plateauPanel.setTileSelectionListener(null);
                actionEnCours = false;

                plateauPanel.rafraichir();
                infoJeuPanel.rafraichir();

                if (timeTokensPanel != null) {
                    timeTokensPanel.rafraichir();
                }

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

    private JPanel creerActionTokenPanel(Token token, int index) {
        JPanel tokenPanel = new JPanel(new BorderLayout());
        tokenPanel.setPreferredSize(new Dimension(90, 90));

        tokenPanel.setOpaque(false);
        tokenPanel.setBorder(null);
        tokenPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String actionName = token.getCurrentSide();

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon icon = chargerImageAction(actionName);

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText(actionName);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 14));
            imageLabel.setForeground(new Color(245, 235, 210));
        }

        tokenPanel.add(imageLabel, BorderLayout.CENTER);

        tokenPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
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

                    Token selectedToken = gameEngine.selectActionToken(index);
                    actionEnCours = true;

                    System.out.println("Action token choisi : " + selectedToken.getCurrentSide());
                    System.out.println("Type action : " + gameEngine.getSelectedActionType());

                    tokenPanel.setOpaque(false);
                    tokenPanel.setBorder(null);
                    tokenPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

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

            if (timeTokensPanel != null) {
                timeTokensPanel.rafraichir();
            }

            if (actionType != ActionType.ROTATE && actionType != ActionType.EXCHANGE) {
                actionEnCours = false;
            }

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

                plateauPanel.rafraichir();
                infoJeuPanel.rafraichir();

                if (timeTokensPanel != null) {
                    timeTokensPanel.rafraichir();
                }

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

                    plateauPanel.rafraichir();
                    infoJeuPanel.rafraichir();

                    if (timeTokensPanel != null) {
                        timeTokensPanel.rafraichir();
                    }
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

    public ActionPanel(
        GameEngine gameEngine,
        InfoJeuPanel infoJeuPanel,
        PlateauPanel plateauPanel,
        TimeTokensPanel timeTokensPanel,
        AlibiPanel alibiPanel) {

        this.gameEngine = gameEngine;
        this.infoJeuPanel = infoJeuPanel;
        this.plateauPanel = plateauPanel;
        this.timeTokensPanel = timeTokensPanel;
        this.alibiPanel = alibiPanel;
        this.boutonClickMusique = new BoutonClickMusique();

        setLayout(new GridLayout(0,2,8,8));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        afficherActions();
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
}