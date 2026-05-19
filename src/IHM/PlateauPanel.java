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
    private java.util.Map<String, BufferedImage> cacheImages = new java.util.HashMap<>();

    //private static final int DETECTIVE_SIZE = 70;
    //private static final int DETECTIVE_MARGIN = 25;

    public interface TileSelectionListener {
        void onTileSelected(Tile tile);
    }

    public void setTileSelectionListener(TileSelectionListener tileSelectionListener) {
        this.tileSelectionListener = tileSelectionListener;
    }

    public PlateauPanel(GameState gameState) {
        this.gameState = gameState;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(700, 700));
        setMinimumSize(new Dimension(500, 500));
        setBackground(couleurFondTourActuel());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        afficherPlateau();
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

        int taillePlateau = Math.min(getWidth() - 180, getHeight() - 120);
        if (taillePlateau <= 0){
            taillePlateau = 570;
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
        panel.setPreferredSize(new Dimension(64, 64));
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
}