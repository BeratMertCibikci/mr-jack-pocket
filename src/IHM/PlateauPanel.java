package IHM;

import javax.swing.*;
import javax.swing.border.Border;

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
    private java.util.Map<String, BufferedImage> cacheImages = new java.util.HashMap<>();

    //private static final int DETECTIVE_SIZE = 70;
    //private static final int DETECTIVE_MARGIN = 25;
    private java.util.List<Integer> highlightedDetectivePositions = new java.util.ArrayList<>();
    private java.util.function.Consumer<Integer> detectivePositionSelectionListener;
    private String selectionMessage;

    private static final int PANEL_W = 920;
    private static final int PANEL_H = 760;

    private static final int BOARD_X = 300;
    private static final int BOARD_Y = 135;
    private static final int BOARD_W = 574;
    private static final int BOARD_H = 574;
    private static final int CARD_SIZE = 190;

    private static final int DETECTIVE_SIZE = 70;
    private static final int DETECTIVE_MARGIN = 25;

    public interface TileSelectionListener {
        void onTileSelected(Tile tile);
    }

    public void setTileSelectionListener(TileSelectionListener tileSelectionListener) {
        this.tileSelectionListener = tileSelectionListener;
    }

    public void setDetectiveMoveSelection(java.util.List<Integer> positions, String message, java.util.function.Consumer<Integer> listener) {
        highlightedDetectivePositions.clear();
        if (positions != null) highlightedDetectivePositions.addAll(positions);

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

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(620, 620));
        setMinimumSize(new Dimension(500, 500));
        setBackground(couleurFondTourActuel());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        afficherPlateau();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    rafraichir();
                });
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

        removeAll();

        int margeX = getWidth() / 10;
        int margeY = getHeight() / 12;

        int taillePlateau = Math.min(getWidth() - margeX, getHeight() - margeY);
        if (taillePlateau <= 0){
            taillePlateau = 300;
        }

        int tailleCarte = taillePlateau / 3;

        Tile[][] board = gameState.getBoard().getBoardForUI();

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 4, 4));
        boardPanel.setOpaque(false);
        boardPanel.setPreferredSize(new Dimension(taillePlateau, taillePlateau));
        //boardPanel.setBounds(BOARD_X, BOARD_Y, BOARD_W, BOARD_H);

        for (int li = 0; li < 3; li++) {
            for (int ci = 0; ci < 3; ci++) {
                Tile tile = board[li][ci];
                GameCharacter character = tile.getCharacter();
                String nom = character != null ? character.getName() : "Carte vide";

                JPanel cartePanel = new JPanel(new BorderLayout());
                cartePanel.setOpaque(false);
                cartePanel.setBorder(null);
                cartePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);

                if (tile.isEliminated() || tile.isEmptySide()) {
                    ImageIcon dosIcon = chargerImageDos("derriere.png", tile.getOrientation(), tailleCarte);

                    if (dosIcon != null) {
                        imageLabel.setIcon(dosIcon);
                    } else {
                        imageLabel.setText("Carte retournée");
                        imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        imageLabel.setForeground(new Color(245, 235, 210));
                    }

                } else {
                    ImageIcon icon = chargerImagePersonnage(nom, tile.getOrientation(), tailleCarte);

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
                    public void mouseClicked(java.awt.event.MouseEvent e) {
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

        //add(boardPanel);
        JPanel plateauContainer = new JPanel(new BorderLayout());
        plateauContainer.setOpaque(false);
        plateauContainer.add(boardPanel, BorderLayout.CENTER);

        plateauContainer.add(creerDetectivesTop(), BorderLayout.NORTH);
        plateauContainer.add(creerDetectivesBottom(), BorderLayout.SOUTH);
        plateauContainer.add(creerDetectivesLeft(), BorderLayout.WEST);
        plateauContainer.add(creerDetectivesRight(), BorderLayout.EAST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(plateauContainer, gbc);
        
        //ajouterDetectiveTokensAutourPlateau();
    }

    private JPanel creerDetectivesTop(){
        int gap = Math.max(8, getWidth() / 30);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, gap, 0));
        panel.setOpaque(false);

        panel.add(creerCaseDetective(0));
        panel.add(creerCaseDetective(1));
        panel.add(creerCaseDetective(2));
        return panel;
    }

    private JPanel creerDetectivesBottom(){
        int gap = Math.max(8, getWidth() / 30);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, gap, 0));
        panel.setOpaque(false);

        panel.add(creerCaseDetective(8));
        panel.add(creerCaseDetective(7));
        panel.add(creerCaseDetective(6));
        return panel;
    }

    private JPanel creerDetectivesLeft(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        int espace = Math.max(10, getHeight() / 18);

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(11));

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(10));

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(9));
        return panel;
    }

    private JPanel creerDetectivesRight(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        int espace = Math.max(10, getHeight() / 18);

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(3));

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(4));

        panel.add(Box.createVerticalStrut(espace));
        panel.add(creerCaseDetective(5));
        return panel;
    }

    private JPanel creerCaseDetective(int position){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        int tailleDetective = Math.max(58, getWidth() / 14);
        tailleDetective = Math.min(tailleDetective, 92);
        panel.setPreferredSize(new Dimension(tailleDetective, tailleDetective));
        panel.setOpaque(false);

        int holmesPos = gameState.getDetectiveTokens().getHolmes().getPosition();
        int watsonPos = gameState.getDetectiveTokens().getWatson().getPosition();
        int tobyPos = gameState.getDetectiveTokens().getToby().getPosition();

        boolean contientDetective = false;

        if (holmesPos == position){
            panel.add(creerBadgeDetective("H"));
            contientDetective = true;
        }

        if (watsonPos == position){
            panel.add(creerBadgeDetective("W"));
            contientDetective = true;
        }

        if (tobyPos == position){
            panel.add(creerBadgeDetective("T"));
            contientDetective = true;
        }

        if (!contientDetective){
            panel.add(creerPointVide());
        }

        return panel;
    }

    private ImageIcon chargerImagePersonnage(String nom, Orientation orientation, int tailleCarte) {
        add(boardPanel);
        if (selectionMessage != null) ajouterMessageSelection();
        ajouterDetectiveTokensAutourPlateau();
    }

    private void ajouterMessageSelection() {
        JLabel messageLabel = new JLabel(selectionMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(255, 215, 0));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(25, 25, 25, 210));
        messageLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));

        int largeur = 300, hauteur = 42;
        int x = 15, y = 20;

        messageLabel.setBounds(x, y, largeur, hauteur);
        add(messageLabel, 0);
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
            BufferedImage imageOriginale = cacheImages.get(fichierImage.getAbsolutePath());

            if (imageOriginale == null) {
                imageOriginale = ImageIO.read(fichierImage);
                cacheImages.put(fichierImage.getAbsolutePath(), imageOriginale);
            }

            if (imageOriginale == null) {
                System.out.println("Image illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageCover(imageOriginale, tailleCarte, tailleCarte);
            BufferedImage imageTournee = tournerImage(imageRedimensionnee, orientation);

            return new ImageIcon(imageTournee);

        } catch (Exception e) {
            System.out.println("Erreur chargement image : " + e.getMessage());
            return null;
        }
    }

    private ImageIcon chargerImageDos(String nomFichier, Orientation orientation, int tailleCarte) {
        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier;

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image dos non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = cacheImages.get(fichierImage.getAbsolutePath());

            if (imageOriginale == null) {
                imageOriginale = ImageIO.read(fichierImage);
                cacheImages.put(fichierImage.getAbsolutePath(), imageOriginale);
            }

            if (imageOriginale == null) {
                System.out.println("Image dos illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImageCover(imageOriginale, tailleCarte, tailleCarte);
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
        }

        if (!contientDetective) {
            panel.add(creerPointVide());
        }
        
        add(panel); 
    }
}

    private JButton creerBoutonPositionDetective(int position) {
        JButton bouton = new JButton("●");
        bouton.setPreferredSize(new Dimension(46, 46));
        bouton.setMaximumSize(new Dimension(46, 46));
        bouton.setMinimumSize(new Dimension(46, 46));
        bouton.setFont(new Font("Arial", Font.BOLD, 26));
        bouton.setForeground(new Color(40, 40, 40));
        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bouton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        bouton.addActionListener(e -> {
            if (detectivePositionSelectionListener != null) {
                detectivePositionSelectionListener.accept(position);
            }
        });

        bouton = new JButton("●") {
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
        int left = BOARD_X - DETECTIVE_SIZE - DETECTIVE_MARGIN;
        int right = BOARD_X + BOARD_W + DETECTIVE_MARGIN;

        int x0 = BOARD_X + BOARD_W / 6 - DETECTIVE_SIZE / 2;
        int x1 = BOARD_X + BOARD_W / 2 - DETECTIVE_SIZE / 2;
        int x2 = BOARD_X + (5 * BOARD_W) / 6 - DETECTIVE_SIZE / 2;

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
        int top = BOARD_Y - DETECTIVE_SIZE - DETECTIVE_MARGIN + 2;
        int bottom = BOARD_Y + BOARD_H + DETECTIVE_MARGIN;

        int y0 = BOARD_Y + BOARD_H / 6 - DETECTIVE_SIZE / 2;
        int y1 = BOARD_Y + BOARD_H / 2 - DETECTIVE_SIZE / 2;
        int y2 = BOARD_Y + (5 * BOARD_H) / 6 - DETECTIVE_SIZE / 2;

        switch (position) {
            case 0:
            case 1:
            case 2:
                return top-25;

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
        
        int taille = Math.max(18, getWidth() / 35);
        point.setPreferredSize(new Dimension(taille, taille));
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
}