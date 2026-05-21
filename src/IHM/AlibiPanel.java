package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AlibiPanel extends JPanel {
    private JLabel imageLabel;
    private BufferedImage imageOriginale;

    public AlibiPanel() {
        //setPreferredSize(new Dimension(250, 0));
        setBackground(new Color(35, 35, 35));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);

        imageOriginale = chargerImageOriginale("alibi_card.png");

        if (imageOriginale == null){
            imageLabel.setText("Image Alibi");
            imageLabel.setForeground(new Color(245, 235, 210));
        }else{
            rafraichir();
        }

        add(imageLabel, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    rafraichir();
                });
            }
        });
    }

    private BufferedImage chargerImageOriginale(String nomFichier) {
        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier;

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image alibi non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(fichierImage);

            if (image == null) {
                System.out.println("Image alibi illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            return image;

        } catch (Exception e) {
            System.out.println("Erreur chargement image alibi : " + e.getMessage());
            return null;
        }
    }

    private BufferedImage redimensionnerImage(BufferedImage imageOriginale, int largeurMax, int hauteurMax) {
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

    public void rafraichir(){
        if (imageOriginale == null){
            return;
        }

        int largeur = Math.max(120, (int)(getWidth() * 0.9));
        int hauteur = Math.max(120, (int)(getHeight() * 0.9));

        BufferedImage imageRedimensionnee = redimensionnerImage(imageOriginale, largeur, hauteur);

        imageLabel.setIcon(new ImageIcon(imageRedimensionnee));

        revalidate();
        repaint();
    }

    
}