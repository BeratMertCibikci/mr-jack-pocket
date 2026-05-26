package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.GameCharacter;
import model.GameState;
import model.Orientation;
import model.Tile;

public class PlateauPanel extends JPanel {

    private GameState gameState;
    private TileSelectionListener tileSelectionListener;

    private java.util.List<Integer> highlightedDetectivePositions = new java.util.ArrayList<>();
    private java.util.function.Consumer<Integer> detectivePositionSelectionListener;
    private String selectionMessage;

    private java.util.List<String> highlightedTiles = new java.util.ArrayList<>();
    private Timer highlightTimer;
    private String ghostDetectiveName;
    private int ghostDetectivePosition = -1;

    /*
    private static final int PANEL_W = 920;
    private static final int PANEL_H = 760;

    private static final int BOARD_X = 300;
    private static final int BOARD_Y = 135;
    private static final int BOARD_W = 574;
    private static final int BOARD_H = 574;
    private static final int CARD_SIZE = 190;
    */

    private int boardX;
    private int boardY;
    private int boardSize;

    private int cardSize;

    private static final int DETECTIVE_SIZE = 70;
    private static final int DETECTIVE_MARGIN = 25;

    public interface TileSelectionListener {
        void onTileSelected(Tile tile);
    }

    public void setTileSelectionListener(TileSelectionListener tileSelectionListener) {
        this.tileSelectionListener = tileSelectionListener;
    }

    public void setDetectiveMoveSelection(
            java.util.List<Integer> positions,
            String message,
            java.util.function.Consumer<Integer> listener
    ) {
        highlightedDetectivePositions.clear();

        if (positions != null) {
            highlightedDetectivePositions.addAll(positions);
        }

        selectionMessage = message;
        detectivePositionSelectionListener = listener;

        removeAll();
        afficherPlateau();
        revalidate();
        repaint();
    }

    public void clearDetectiveMoveSelection() {
        highlightedDetectivePositions.clear();
        selectionMessage = null;
        detectivePositionSelectionListener = null;
        rafraichir();
    }

    public PlateauPanel(GameState gameState) {
        this.gameState = gameState;

        setLayout(null);
        //setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        //setMaximumSize(new Dimension(PANEL_W, PANEL_H));
        setBackground(couleurFondTourActuel());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        afficherPlateau();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                rafraichir();
            }
        });
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void rafraichir() {
        setBackground(couleurFondTourActuel());
        removeAll();
        afficherPlateau();
        revalidate();
        repaint();
    }

    private Color couleurFondTourActuel() {
        String joueur = gameState.getTurnManager().getCurrentPlayer();

        if (joueur.equals("Investigator")) {
            return new Color(18, 28, 48);
        }

        if (joueur.equals("Jack")) {
            return new Color(48, 18, 18);
        }

        return new Color(35, 35, 35);
    }

    private void afficherPlateau() {
        int leftSpace = 120;
        int rightSpace = 260;

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int availableBoardWidth = availableWidth - leftSpace - rightSpace;

        boardSize = Math.min(availableBoardWidth, availableHeight) - 180;

        boardSize = Math.max(boardSize, 420);

        boardX = leftSpace + (availableBoardWidth - boardSize) / 2;
        boardY = (availableHeight - boardSize) / 2;

        cardSize = boardSize / 3;

        Tile[][] board = gameState.getBoard().getBoardForUI();

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 2, 2));
        boardPanel.setOpaque(false);
        boardPanel.setBounds(boardX, boardY, boardSize, boardSize);

        for (int li = 0; li < 3; li++) {
            for (int ci = 0; ci < 3; ci++) {
                Tile tile = board[li][ci];
                GameCharacter character = tile.getCharacter();
                String nom = character != null ? character.getName() : "Carte vide";

                final String tileKey = getTileKey(li, ci);

                JPanel cartePanel = new JPanel(new BorderLayout()) {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);

                        if (highlightedTiles.contains(tileKey)) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            g2.setColor(new Color(255, 215, 0));
                            g2.setStroke(new BasicStroke(7));

                            int margin = 5;

                            g2.drawRoundRect(
                                    margin,
                                    margin,
                                    getWidth() - margin * 2 - 1,
                                    getHeight() - margin * 2 - 1,
                                    18,
                                    18
                            );

                            g2.dispose();
                        }
                    }
                };

                cartePanel.setOpaque(false);
                cartePanel.setBorder(null);
                cartePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);

                if (tile.isEliminated() || tile.isEmptySide()) {
                    ImageIcon dosIcon;

                    if (tile.getCharacter() != null
                            && "Joseph Lane".equals(tile.getCharacter().getName())) {
                        dosIcon = chargerImageDos("derriere_joseph.png", tile.getOrientation());
                    } else {
                        dosIcon = chargerImageDos("derriere.png", tile.getOrientation());
                    }
                    if (dosIcon != null) {
                        imageLabel.setIcon(dosIcon);
                    } else {
                        imageLabel.setText("Carte retournée");
                        imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        imageLabel.setForeground(new Color(245, 235, 210));
                    }
                } else {
                    ImageIcon icon = chargerImagePersonnage(nom, tile.getOrientation());

                    if (icon != null) {
                        imageLabel.setIcon(icon);
                    } else {
                        imageLabel.setText("Image");
                        imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        imageLabel.setForeground(new Color(245, 235, 210));
                    }
                }

                cartePanel.add(imageLabel, BorderLayout.CENTER);

                final int l = li;
                final int c = ci;
                final Orientation orientation = tile.getOrientation();
                final String nomFinal = nom;

                cartePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        System.out.println(
                                "Carte cliquée : " + nomFinal
                                        + " position (" + l + "," + c + ")"
                                        + " orientation : " + orientation
                        );

                        if (tileSelectionListener != null) {
                            tileSelectionListener.onTileSelected(tile);
                        }
                    }
                });

                boardPanel.add(cartePanel);
            }
        }

        add(boardPanel);

        if (selectionMessage != null) {
            ajouterMessageSelection();
        }

        ajouterDetectiveTokensAutourPlateau();
        ajouterJackHourglassesVertical();
        ajouterFlecheDirection();
    }

    private String getTileKey(int row, int col) {
        return row + "," + col;
    }

    public void highlightTileTemporarily(int row, int col) {
        highlightedTiles.clear();
        highlightedTiles.add(getTileKey(row, col));

        startHighlightTimer();
    }

    public void highlightTilesTemporarily(int row1, int col1, int row2, int col2) {
        highlightedTiles.clear();
        highlightedTiles.add(getTileKey(row1, col1));
        highlightedTiles.add(getTileKey(row2, col2));

        startHighlightTimer();
    }

    private void startHighlightTimer() {
        if (highlightTimer != null && highlightTimer.isRunning()) {
            highlightTimer.stop();
        }

        rafraichir();
    }

    public void clearTileHighlight() {
        highlightedTiles.clear();

        if (highlightTimer != null && highlightTimer.isRunning()) {
            highlightTimer.stop();
        }

        rafraichir();
    }

    private void ajouterMessageSelection() {
        JLabel messageLabel = new JLabel(selectionMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(255, 215, 0));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(25, 25, 25, 210));
        messageLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));

        int largeur = 300;
        int hauteur = 42;
        int x = 15;
        int y = 20;

        messageLabel.setBounds(x, y, largeur, hauteur);
        add(messageLabel, 0);
    }
    public void setGhostDetectivePosition(String detectiveName, int position) {
        this.ghostDetectiveName = detectiveName;
        this.ghostDetectivePosition = position;
        rafraichir();
    }

    public void clearGhostDetectivePosition() {
        this.ghostDetectiveName = null;
        this.ghostDetectivePosition = -1;
        rafraichir();
    }

    private ImageIcon chargerImagePersonnage(String nom, Orientation orientation) {
        String nomFichier = nom.toLowerCase()
                .replace(".", "")
                .replace(" ", "_");

        String cheminBase = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier;

        File fichierImage = trouverFichierImage(cheminBase);

        if (fichierImage == null) {
            System.out.println("Image non trouvée : " + cheminBase + ".png");
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageCover(imageOriginale, cardSize, cardSize);
            BufferedImage imageTournee = tournerImage(imageRedimensionnee, orientation);

            return new ImageIcon(imageTournee);

        } catch (Exception e) {
            System.out.println("Erreur chargement image : " + e.getMessage());
            return null;
        }
    }

    private ImageIcon chargerImageDos(String nomFichier, Orientation orientation) {
        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier;

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image dos non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image dos illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageCover(imageOriginale, cardSize, cardSize);
            BufferedImage imageTournee = tournerImage(imageRedimensionnee, orientation);

            return new ImageIcon(imageTournee);

        } catch (Exception e) {
            System.out.println("Erreur chargement image dos : " + e.getMessage());
            return null;
        }
    }

    private File trouverFichierImage(String cheminBase) {
        File fichier = new File(cheminBase + ".png");

        if (fichier.exists()) {
            return fichier;
        }

        return null;
    }

    private BufferedImage redimensionnerImageCover(BufferedImage imageOriginale, int largeurCible, int hauteurCible) {
        int largeurOriginale = imageOriginale.getWidth();
        int hauteurOriginale = imageOriginale.getHeight();

        double ratioLargeur = (double) largeurCible / largeurOriginale;
        double ratioHauteur = (double) hauteurCible / hauteurOriginale;
        double ratio = Math.max(ratioLargeur, ratioHauteur);

        int nouvelleLargeur = (int) Math.round(largeurOriginale * ratio);
        int nouvelleHauteur = (int) Math.round(hauteurOriginale * ratio);

        BufferedImage imageRedimensionnee = new BufferedImage(
                largeurCible,
                hauteurCible,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imageRedimensionnee.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = (largeurCible - nouvelleLargeur) / 2;
        int y = (hauteurCible - nouvelleHauteur) / 2;

        g.drawImage(imageOriginale, x, y, nouvelleLargeur, nouvelleHauteur, null);
        g.dispose();

        return imageRedimensionnee;
    }

    private BufferedImage tournerImage(BufferedImage image, Orientation orientation) {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();

        BufferedImage imageTournee = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imageTournee.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int angle = getAngleOrientation(orientation);

        g.rotate(Math.toRadians(angle), largeur / 2.0, hauteur / 2.0);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return imageTournee;
    }

    private int getAngleOrientation(Orientation orientation) {
        if (orientation == Orientation.NORTH) {
            return 0;
        }

        if (orientation == Orientation.EAST) {
            return 90;
        }

        if (orientation == Orientation.SOUTH) {
            return 180;
        }

        if (orientation == Orientation.WEST) {
            return 270;
        }

        return 0;
    }

    private void ajouterDetectiveTokensAutourPlateau() {
        int holmesPos = gameState.getDetectiveTokens().getHolmes().getPosition();
        int watsonPos = gameState.getDetectiveTokens().getWatson().getPosition();
        int tobyPos = gameState.getDetectiveTokens().getToby().getPosition();

        for (int position = 0; position < 12; position++) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.setOpaque(false);

            panel.setBounds(
                    getXDetective(position),
                    getYDetective(position),
                    DETECTIVE_SIZE,
                    DETECTIVE_SIZE * 2
            );

            boolean highlighted = highlightedDetectivePositions.contains(position);
            boolean contientDetective = false;
            boolean contientGhost = ghostDetectiveName != null
                && ghostDetectivePosition == position
                && !isDetectiveCurrentlyAtPosition(ghostDetectiveName, position);   
            

            if (highlighted) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                panel.setOpaque(false);
                panel.add(creerBoutonPositionDetective(position));
                contientDetective = true;
            } else {
                if (holmesPos == position) {
                    panel.add(creerBadgeDetective("H"));
                    contientDetective = true;
                }

                if (watsonPos == position) {
                    panel.add(creerBadgeDetective("W"));
                    contientDetective = true;
                }

                if (tobyPos == position) {
                    panel.add(creerBadgeDetective("T"));
                    contientDetective = true;
                }
                if (contientGhost) {
                    panel.add(creerGhostBadgeDetective(ghostDetectiveName));
                    contientDetective = true;
                }
            }

            if (!contientDetective) {
                panel.add(creerPointVide());
            }

            add(panel);
        }
    }
private boolean isDetectiveCurrentlyAtPosition(String detectiveName, int position) {
    if ("Holmes".equals(detectiveName)) {
        return gameState.getDetectiveTokens().getHolmes().getPosition() == position;
    }

    if ("Watson".equals(detectiveName)) {
        return gameState.getDetectiveTokens().getWatson().getPosition() == position;
    }

    if ("Toby".equals(detectiveName)) {
        return gameState.getDetectiveTokens().getToby().getPosition() == position;
    }

    return false;
}

private JComponent creerGhostBadgeDetective(String detectiveName) {
    return new DetectiveTokenPanel(detectiveName, true);
}
    private JButton creerBoutonPositionDetective(int position) {
        JButton bouton = new JButton("●") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 215, 0, 150));
                g2.fillOval(4, 4, getWidth() - 8, getHeight() - 8);

                g2.setColor(new Color(255, 245, 160, 210));
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(4, 4, getWidth() - 8, getHeight() - 8);

                g2.setColor(new Color(25, 25, 25, 220));
                g2.fillOval(getWidth() / 2 - 4, getHeight() / 2 - 4, 8, 8);

                g2.dispose();
            }
        };

        bouton.setPreferredSize(new Dimension(46, 46));
        bouton.setMaximumSize(new Dimension(46, 46));
        bouton.setMinimumSize(new Dimension(46, 46));
        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bouton.addActionListener(e -> {
            if (detectivePositionSelectionListener != null) {
                detectivePositionSelectionListener.accept(position);
            }
        });

        return bouton;
    }

    private int getXDetective(int position) {
        int left = boardX - DETECTIVE_SIZE - DETECTIVE_MARGIN;
        int right = boardX + boardSize + DETECTIVE_MARGIN;

        int x0 = boardX + boardSize / 6 - DETECTIVE_SIZE / 2;
        int x1 = boardX + boardSize / 2 - DETECTIVE_SIZE / 2;
        int x2 = boardX + (5 * boardSize) / 6 - DETECTIVE_SIZE / 2;

        switch (position) {
            case 0:
                return x0;
            case 1:
                return x1;
            case 2:
                return x2;
            case 3:
            case 4:
            case 5:
                return right;
            case 6:
                return x2;
            case 7:
                return x1;
            case 8:
                return x0;
            case 9:
            case 10:
            case 11:
                return left;
            default:
                return 0;
        }
    }

    private int getYDetective(int position) {
        int top = boardY - DETECTIVE_SIZE - DETECTIVE_MARGIN + 2;
        int bottom = boardY + boardSize + DETECTIVE_MARGIN;

        int y0 = boardY + boardSize / 6 - DETECTIVE_SIZE / 2;
        int y1 = boardY + boardSize / 2 - DETECTIVE_SIZE / 2;
        int y2 = boardY + (5 * boardSize) / 6 - DETECTIVE_SIZE / 2;

        switch (position) {
            case 0:
            case 1:
            case 2:
                //return top - 25;
                return top;
            case 3:
                return y0;
            case 4:
                return y1;
            case 5:
                return y2;
            case 6:
            case 7:
            case 8:
                return bottom;
            case 9:
                return y2;
            case 10:
                return y1;
            case 11:
                return y0;
            default:
                return 0;
        }
    }

    private JComponent creerBadgeDetective(String texte) {
        if (texte.equals("H")) {
            return new DetectiveTokenPanel("Holmes");
        }

        if (texte.equals("W")) {
            return new DetectiveTokenPanel("Watson");
        }

        if (texte.equals("T")) {
            return new DetectiveTokenPanel("Toby");
        }

        JLabel badge = new JLabel(texte, SwingConstants.CENTER);
        badge.setPreferredSize(new Dimension(40, 40));
        badge.setOpaque(true);
        badge.setBackground(Color.WHITE);
        badge.setForeground(Color.BLACK);
        badge.setFont(new Font("Arial", Font.BOLD, 14));
        badge.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return badge;
    }

    private JLabel creerPointVide() {
        JLabel point = new JLabel("•", SwingConstants.CENTER);
        point.setPreferredSize(new Dimension(16, 16));
        point.setForeground(new Color(180, 180, 180));
        point.setFont(new Font("Arial", Font.BOLD, 16));

        return point;
    }

    private BufferedImage sharpenImage(BufferedImage image) {
        float[] sharpenKernel = {
                0f, -0.4f, 0f,
                -0.4f, 2.6f, -0.4f,
                0f, -0.4f, 0f
        };

        java.awt.image.Kernel kernel = new java.awt.image.Kernel(3, 3, sharpenKernel);
        java.awt.image.ConvolveOp op = new java.awt.image.ConvolveOp(
                kernel,
                java.awt.image.ConvolveOp.EDGE_NO_OP,
                null
        );

        return op.filter(image, null);
    }

    private void ajouterJackHourglassesVertical() {
        int hourglassSize = Math.max(28, boardSize / 10);
        String joueur = gameState.getTurnManager().getCurrentPlayer();

        if (!joueur.equals("Jack")) {
            return;
        }

        int count;

        try {
            count = gameState.getWinConditionChecker().calculateJackHourglassTotal(
                    gameState.getAlibiDeckManager(),
                    gameState.getJackPlayerState()
            );
        } catch (Exception e) {
            return;
        }

        JPanel hourglassContainer = new JPanel();
        hourglassContainer.setOpaque(false);
        hourglassContainer.setLayout(new BoxLayout(hourglassContainer, BoxLayout.Y_AXIS));

        int containerW = hourglassSize;
        int targetX = getWidth() - 95;
        hourglassContainer.setBounds(targetX, boardY, containerW, boardSize);

        for (int i = 1; i <= 6; i++) {
            JLabel iconLabel = new JLabel();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            String fileName = i <= count ? "hourglass_full.png" : "hourglass_empty.png";
            String chemin = System.getProperty("user.dir") + "/assets/images/" + fileName;

            ImageIcon icon = new ImageIcon(chemin);
            Image img = icon.getImage().getScaledInstance(hourglassSize, hourglassSize, Image.SCALE_SMOOTH);

            iconLabel.setIcon(new ImageIcon(img));

            hourglassContainer.add(iconLabel);
            hourglassContainer.add(Box.createVerticalStrut(hourglassSize / 2));
        }

        add(hourglassContainer, 0);
    }

    private void ajouterFlecheDirection() {
        int flecheW = 120;
        int flecheH = 80;

        int targetX = boardX + boardSize + 20; 
        int targetY = boardY - 75; 

        JPanel flechePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String chemin = System.getProperty("user.dir") + "/assets/images/fleche.png";
                File fichierImage = new File(chemin);
                
                if (fichierImage.exists()) {
                    try {
                        BufferedImage imgOriginale = ImageIO.read(fichierImage);
                        if (imgOriginale != null) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            
                            g2.drawImage(imgOriginale, 0, 0, flecheW, flecheH, null);
                            g2.dispose();
                        }
                    } catch (Exception e) {
                        
                    }
                }
            }
        };

        flechePanel.setOpaque(false);
        flechePanel.setBounds(targetX, targetY, flecheW, flecheH);
        add(flechePanel, 0);
    }
}