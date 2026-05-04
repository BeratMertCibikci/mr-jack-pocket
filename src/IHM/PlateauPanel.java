package IHM;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import model.GameState;
import model.Tile;
import model.GameCharacter;
import model.Orientation;

public class PlateauPanel extends JPanel {

    private GameState gameState;
    private TileSelectionListener tileSelectionListener;

    public interface TileSelectionListener {
        void onTileSelected(Tile tile);
    }

    public void setTileSelectionListener(TileSelectionListener tileSelectionListener) {
        this.tileSelectionListener = tileSelectionListener;
}

    public PlateauPanel(GameState gameState) {
        this.gameState = gameState;

        setPreferredSize(new Dimension(700, 500));
        setLayout(new GridLayout(3, 3, 10, 10));
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        afficherPlateau();
    }

    public void rafraichir() {
        removeAll();
        afficherPlateau();
        revalidate();
        repaint();
    }

    private void afficherPlateau() {
        Tile[][] board = gameState.getBoard().getBoardForUI();

        for (int li = 0; li < 3; li++) {
            for (int ci = 0; ci < 3; ci++) {
                Tile tile = board[li][ci];
                GameCharacter character = tile.getCharacter();
                String nom = character != null ? character.getName() : "Carte vide";

                JPanel cartePanel = new JPanel(new BorderLayout());
                cartePanel.setBackground(new Color(245, 235, 210));
                cartePanel.setBorder(BorderFactory.createLineBorder(new Color(80, 55, 35), 3));
                cartePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

                JLabel nomLabel = new JLabel(nom, SwingConstants.CENTER);
                nomLabel.setFont(new Font("Arial", Font.BOLD, 14));
                nomLabel.setForeground(new Color(40, 30, 20));
                nomLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

                if (tile.isEliminated() || tile.isEmptySide()) {
                    ImageIcon dosIcon = chargerImageDos("derriere.png",tile.getOrientation());

                    if (dosIcon != null) {
                        imageLabel.setIcon(dosIcon);
                    } else {
                        //imageLabel.setText("Carte retournée");
                        imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        imageLabel.setForeground(new Color(245, 235, 210));
                    }

                    cartePanel.setBackground(new Color(45, 40, 35));
                    cartePanel.setBorder(BorderFactory.createLineBorder(new Color(120, 100, 70), 3));
                    //nomLabel.setText(tile.isEliminated() ? "Éliminé" : "Carte retournée");
                    nomLabel.setForeground(new Color(245, 235, 210));

                } else {
                    ImageIcon icon = chargerImagePersonnage(nom, tile.getOrientation());

                    if (icon != null) {
                        imageLabel.setIcon(icon);
                    } else {
                        imageLabel.setText("Image");
                        imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        imageLabel.setForeground(new Color(80, 55, 35));
                    }
                }

                cartePanel.add(imageLabel, BorderLayout.CENTER);
                cartePanel.add(nomLabel, BorderLayout.SOUTH);

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

                add(cartePanel);
            }
        }
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
            System.out.println("Image non trouvée : " + cheminBase + ".png/.jpg/.jpeg");
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichierImage);

            if (imageOriginale == null) {
                System.out.println("Image illisible : " + fichierImage.getAbsolutePath());
                return null;
            }

            BufferedImage imageRedimensionnee = redimensionnerImage(imageOriginale, 130, 100);
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

            BufferedImage imageRedimensionnee = redimensionnerImage(imageOriginale, 130, 100);
            BufferedImage imageTournee = tournerImage(imageRedimensionnee, orientation);

            return new ImageIcon(imageTournee);

        } catch (Exception e) {
            System.out.println("Erreur chargement image dos : " + e.getMessage());
            return null;
        }
    }

    private File trouverFichierImage(String cheminBase) {
        String[] extensions = {".png", ".jpg", ".jpeg"};

        for (String extension : extensions) {
            File fichier = new File(cheminBase + extension);

            if (fichier.exists()) {
                return fichier;
            }
        }

        return null;
    }

    private BufferedImage redimensionnerImage(BufferedImage imageOriginale, int largeur, int hauteur) {
        BufferedImage imageRedimensionnee = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imageRedimensionnee.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(imageOriginale, 0, 0, largeur, hauteur, null);
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
}