package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DetectiveTokenPanel extends JPanel {

    private String nom;
    private BufferedImage image;
    private boolean ghost;

    public DetectiveTokenPanel(String nom) {
        this(nom, false);
    }

    public DetectiveTokenPanel(String nom, boolean ghost) {
        this.nom = nom;
        this.ghost = ghost;
        this.image = chargerImageDetective(nom);

        setPreferredSize(new Dimension(60, 60));
        setMinimumSize(new Dimension(60, 60));
        setMaximumSize(new Dimension(60, 60));

        setOpaque(false);
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        if (ghost) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
    }

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 6;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        if (image != null) {
            Shape oldClip = g2.getClip();

            g2.setClip(new java.awt.geom.Ellipse2D.Double(x, y, size, size));
            g2.drawImage(image, x, y, size, size, null);
            g2.setClip(oldClip);
        } else {
            g2.setColor(new Color(35, 30, 25));
            g2.fillOval(x, y, size, size);

            g2.setColor(new Color(245, 235, 210));
            g2.setFont(new Font("Arial", Font.BOLD, 16));

            String lettre = getLettreDetective(nom);
            FontMetrics fm = g2.getFontMetrics();

            int textX = (getWidth() - fm.stringWidth(lettre)) / 2;
            int textY = (getHeight() + fm.getAscent()) / 2 - 3;

            g2.drawString(lettre, textX, textY);
        }

        g2.setColor(new Color(212, 175, 55));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x, y, size, size);

        g2.dispose();
    }

    private BufferedImage chargerImageDetective(String nom) {
        String nomFichier = getNomFichierDetective(nom);

        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier
                + ".png";

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image detective non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image detective illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            return imageOriginale;

        } catch (Exception e) {
            System.out.println("Erreur chargement image detective : " + e.getMessage());
            return null;
        }
    }

    private String getNomFichierDetective(String nom) {
        String n = nom.toLowerCase();

        if (n.contains("holmes") || n.contains("sherlock")) {
            return "sherlock_action";
        }

        if (n.contains("watson")) {
            return "watson_action";
        }

        if (n.contains("toby")) {
            return "toby_action";
        }

        return n.replace(" ", "_");
    }

    private String getLettreDetective(String nom) {
        String n = nom.toLowerCase();

        if (n.contains("holmes") || n.contains("sherlock")) {
            return "H";
        }

        if (n.contains("watson")) {
            return "W";
        }

        if (n.contains("toby")) {
            return "T";
        }

        return "?";
    }
}