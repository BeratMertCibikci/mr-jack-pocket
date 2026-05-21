package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.GameState;
import model.Token;

public class TimeTokensPanel extends JPanel {

    private GameState gameState;

    //private static final int TOKEN_SIZE = 58;
    private int tokenSize = 58;

    public TimeTokensPanel(GameState gameState) {
        this.gameState = gameState;

        //setPreferredSize(new Dimension(90, 0));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        afficherTimeTokens();

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
        removeAll();
        afficherTimeTokens();
        revalidate();
        repaint();
    }

    private void afficherTimeTokens() {
        int largeurDisponible = Math.max(70, getWidth() - 20);
        int hauteurDisponible = Math.max(40, getHeight() / 10);

        tokenSize = Math.min(largeurDisponible - 8, hauteurDisponible);
        tokenSize = Math.max(30, Math.min(tokenSize, 50));

        JLabel titre = new JLabel("Tours", SwingConstants.CENTER);
        titre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        int tailleTitre = Math.max(14, getHeight() / 28);
        titre.setFont(new Font("Arial", Font.BOLD, tailleTitre));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titre);
        add(Box.createVerticalStrut(15));

        for (int i = 1; i <= 8; i++) {
            JPanel tokenPanel = creerTimeTokenPanel(i);
            tokenPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(tokenPanel);
            int espace = Math.max(2, getHeight() / 120);
            add(Box.createVerticalStrut(espace));
        }
    }

    private JPanel creerTimeTokenPanel(int numeroTour) {
        JPanel panel = new JPanel(new BorderLayout());

        //int largeur = Math.max(85, getWidth() - 14);
        //int hauteur = Math.max(55, getHeight() / 10);

        Dimension taille = new Dimension(tokenSize + 2, tokenSize + 2);

        panel.setPreferredSize(taille);
        panel.setMinimumSize(taille);
        panel.setMaximumSize(taille);
        //panel.setBackground(new Color(45, 40, 35));
        //panel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        panel.setOpaque(false);
        panel.setBorder(null);

        JLabel label = new JLabel(String.valueOf(numeroTour), SwingConstants.CENTER);

        int tailleFont = Math.max(12, getHeight() / 35);
        label.setFont(new Font("Arial", Font.BOLD, tailleFont));
        label.setForeground(new Color(245, 235, 210));

        String proprietaire = getProprietaireToken(numeroTour);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon icon = chargerImageTimeToken(numeroTour, proprietaire);

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText(String.valueOf(numeroTour));
            imageLabel.setFont(new Font("Arial", Font.BOLD, 18));
            imageLabel.setForeground(new Color(245, 235, 210));
        }

        panel.add(imageLabel, BorderLayout.CENTER);

        if (proprietaire.equals("CURRENT")) {
            panel.setToolTipText("Tour actuel");
        } else if (proprietaire.equals("INVESTIGATOR")) {
            panel.setToolTipText("Pris par l'Investigateur");
        } else if (proprietaire.equals("JACK")) {
            panel.setToolTipText("Pris par Jack");
        } else {
            panel.setToolTipText("Tour " + numeroTour + " disponible");
        }

        return panel;
    }

    private ImageIcon chargerImageTimeToken(int numeroTour, String proprietaire) {
        String nomFichier = getNomFichierTimeToken(numeroTour, proprietaire);

        String chemin = System.getProperty("user.dir")
                + "/assets/images/time_tokens/"
                + nomFichier;

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image time token non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image time token illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImage(
                    imageOriginale,
                    tokenSize,
                    tokenSize
            );

            BufferedImage imageFinale = appliquerEtat(imageRedimensionnee, proprietaire);

            return new ImageIcon(imageFinale);

        } catch (Exception e) {
            System.out.println("Erreur chargement time token : " + e.getMessage());
            return null;
        }
    }

    private String getNomFichierTimeToken(int numeroTour, String proprietaire) {
        // Token alınmışsa kapalı/bitmiş görsel
        if (proprietaire.equals("INVESTIGATOR") || proprietaire.equals("JACK")) {
            return "time_token.png";
        }

        // Token henüz alınmadıysa veya current ise kendi tur görseli
        if (numeroTour % 2 == 1) {
            return "investigator_time_" + numeroTour + ".png";
        }

        return "jack_time_" + numeroTour + ".png";
    }

    private BufferedImage appliquerEtat(BufferedImage image, String proprietaire) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, null);

        if (proprietaire.equals("CURRENT")) {
            g.setColor(new Color(255, 215, 0));
            g.setStroke(new BasicStroke(4));
            g.drawOval(3, 3, image.getWidth() - 7, image.getHeight() - 7);

        } else if (proprietaire.equals("INVESTIGATOR")) {
            g.setColor(new Color(60, 130, 220));
            g.setStroke(new BasicStroke(4));
            g.drawOval(3, 3, image.getWidth() - 7, image.getHeight() - 7);

        } else if (proprietaire.equals("JACK")) {
            g.setColor(new Color(190, 45, 45));
            g.setStroke(new BasicStroke(4));
            g.drawOval(3, 3, image.getWidth() - 7, image.getHeight() - 7);
        }

        g.dispose();
        return result;
    }

    private BufferedImage redimensionnerImage(BufferedImage imageOriginale, int largeurMax, int hauteurMax) {
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

    private String getProprietaireToken(int numeroTour) {
        Token current = gameState.getCurrentTurnToken();

        if (current != null && valeurToken(current) == numeroTour) {
            return "CURRENT";
        }

        for (Token token : gameState.getDetectivePlayerState().getOwnedTurnTokens()) {
            if (valeurToken(token) == numeroTour) {
                return "INVESTIGATOR";
            }
        }

        for (Token token : gameState.getJackPlayerState().getOwnedTurnTokens()) {
            if (valeurToken(token) == numeroTour) {
                return "JACK";
            }
        }

        return "NONE";
    }

    protected int valeurToken(Token token) {
        String face = token.getFrontSide();

        switch (face) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            default:
                return 0;
        }
    }

    @Override
    public Dimension getPreferredSize() {

        int largeur = Math.max(78, getParent() != null
                ? getParent().getWidth() / 13
                : 90);

        largeur = Math.min(largeur, 105);

        return new Dimension(largeur, 0);
    }
}