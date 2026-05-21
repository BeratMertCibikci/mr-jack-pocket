package IHM;

import engine.GameEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class JackAlibiCardsPanel extends JPanel {

    private GameEngine gameEngine;
    private JPanel cardsContainer;

    public JackAlibiCardsPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(170, 230));
        setMaximumSize(new Dimension(170, 230));

        JLabel title = new JLabel("Jack Alibis", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(245, 235, 210));
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        cardsContainer = new JPanel();
        cardsContainer.setOpaque(false);
        cardsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        rafraichir();
    }

    public void rafraichir() {
        cardsContainer.removeAll();

        if (gameEngine == null || gameEngine.getGameState() == null) {
            revalidate();
            repaint();
            return;
        }

        boolean jackTurn = gameEngine.getGameState().getTurnManager().isJackTurn();
        setVisible(jackTurn);

        if (!jackTurn) {
            revalidate();
            repaint();
            return;
        }

        try {
            boolean hasCard = false;

            for (model.AlibiCards card : gameEngine.getGameState().getAlibiDeckManager().getCards()) {
                if ("Jack".equals(card.getOwner()) && !card.getCharacter().isJack()) {
                    JPanel cardPanel = creerCarteAlibi(
                            card.getCharacter().getName(),
                            card.getHourglassValue()
                    );

                    cardsContainer.add(cardPanel);
                    hasCard = true;
                }
            }

            if (!hasCard) {
                JLabel emptyLabel = new JLabel("Aucune carte");
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                emptyLabel.setForeground(new Color(180, 180, 180));
                cardsContainer.add(emptyLabel);
            }

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Indisponible");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            errorLabel.setForeground(new Color(180, 180, 180));
            cardsContainer.add(errorLabel);
        }

        revalidate();
        repaint();
    }

    private JPanel creerCarteAlibi(String characterName, int hourglassValue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(72, 92));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(68, 68));

        ImageIcon icon = chargerImagePersonnage(characterName, 64, 64);

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText(characterName);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 9));
            imageLabel.setForeground(new Color(245, 235, 210));
        }

        JLabel infoLabel = new JLabel("⌛ " + hourglassValue, SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 11));
        infoLabel.setForeground(new Color(212, 175, 55));

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private ImageIcon chargerImagePersonnage(String characterName, int largeurMax, int hauteurMax) {
        String nomFichier = characterName.toLowerCase()
                .replace(".", "")
                .replace(" ", "_");

        String chemin = System.getProperty("user.dir")
                + "/assets/images/characters/"
                + nomFichier
                + ".png";

        File fichierImage = new File(chemin);

        if (!fichierImage.exists()) {
            System.out.println("Image alibi Jack non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImage(imageOriginale, largeurMax, hauteurMax);
            return new ImageIcon(imageRedimensionnee);

        } catch (Exception e) {
            System.out.println("Erreur image alibi Jack : " + e.getMessage());
            return null;
        }
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
}