package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DetectiveTokenPanel extends JPanel {

    private String nom;
    private BufferedImage image;

    public DetectiveTokenPanel(String nom) {
        this.nom = nom;
        this.image = chargerImageDetective(nom);

        //setPreferredSize(new Dimension(60, 60));
        //setMinimumSize(new Dimension(60, 60));
        //setMaximumSize(new Dimension(60, 60));

        setOpaque(false);
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = (int)(Math.min(getWidth(), getHeight()) * 0.85);
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

            int tailleFont = Math.max(12, size/3);
            g2.setFont(new Font("Arial", Font.BOLD, tailleFont));

            String lettre = getLettreDetective(nom);
            FontMetrics fm = g2.getFontMetrics();

            int textX = (getWidth() - fm.stringWidth(lettre)) / 2;
            int textY = (getHeight() + fm.getAscent()) / 2 - 3;

            g2.drawString(lettre, textX, textY);
        }

        g2.setColor(new Color(212, 175, 55));

        float epaisseur = Math.max(2f, size / 20f);
        g2.setStroke(new BasicStroke(epaisseur));
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

    @Override
    public Dimension getPreferredSize(){
        int size = Math.max(52, Math.min(getWidth(), getHeight()) - 8);

        return new Dimension(size, size);
    }
}