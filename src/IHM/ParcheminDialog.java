package IHM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ParcheminDialog {

    private static final int DIALOG_W = 520;
    private static final int DIALOG_H = 560;

    private static final int ROTATION_DIALOG_W = 420;
    private static final int ROTATION_DIALOG_H = 300;

    private static final String ROTATION_CHOICE_TITLE = "RotationChoice";

    private static final Color PARCHMENT_BG = new Color(238, 218, 175);
    private static final Color PARCHMENT_BORDER = new Color(120, 92, 50);

    public static void showMessage(Component parent, String title, String message) {
        JDialog dialog = creerDialog(parent, title);

        JPanel panel = new BackgroundParchmentPanel("rotate.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void EchangeMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Échange");

        JPanel panel = new BackgroundParchmentPanel("echange.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void Echange2Message(Component parent) {
        JDialog dialog = creerDialog(parent, "Échange");

        JPanel panel = new BackgroundParchmentPanel("echange2.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void JackAlibiMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Alibi");

        JPanel panel = new BackgroundParchmentPanel("alibi_jack.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void InspecteurAlibiMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Alibi");

        JPanel panel = new BackgroundParchmentPanel("alibi_insp.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void showWitnessPhaseMessage(Component parent, boolean jackVisible) {
        JDialog dialog = creerDialogWitness(parent);

        JPanel panel = new BackgroundParchmentPanel("witness_phase_new.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JLabel questionLabel = new JLabel("Jack est-il visible ?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Serif", Font.BOLD, 34));
        questionLabel.setForeground(new Color(45, 30, 18));
        questionLabel.setBounds(120, 125, 380, 50);

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setBounds(295, 195, 240, 325);

        String imageName = jackVisible
                ? "witness_jack_visible.png"
                : "witness_jack_hidden.png";

        ImageIcon witnessIcon = chargerImageWitness(imageName, 220, 300);

        if (witnessIcon != null) {
            imageLabel.setIcon(witnessIcon);
        } else {
            imageLabel.setText(jackVisible ? "Jack visible" : "Jack caché");
            imageLabel.setFont(new Font("Serif", Font.BOLD, 24));
            imageLabel.setForeground(new Color(45, 30, 18));
        }

        JLabel answerLabel = new JLabel(jackVisible ? "OUI" : "NON", SwingConstants.CENTER);
        answerLabel.setFont(new Font("Serif", Font.BOLD, 44));
        answerLabel.setForeground(jackVisible
                ? new Color(75, 45, 20)
                : new Color(120, 35, 25));
        answerLabel.setBounds(95, 215, 180, 60);

        JLabel descriptionLabel = new JLabel(
                jackVisible
                        ? "<html><div style='text-align:center;'>Jack est visible par<br>au moins un détective.</div></html>"
                        : "<html><div style='text-align:center;'>Jack n'est visible par<br>aucun détective.</div></html>",
                SwingConstants.CENTER
        );

        descriptionLabel.setFont(new Font("Serif", Font.BOLD, 21));
        descriptionLabel.setForeground(new Color(45, 30, 18));
        descriptionLabel.setBounds(55, 270, 260, 90);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(235, 625, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(questionLabel);
        panel.add(imageLabel);
        panel.add(answerLabel);
        panel.add(descriptionLabel);
        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static int demanderRotation(Component parent) {
        final int[] result = {0};

        Window owner = SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(owner, ROTATION_CHOICE_TITLE, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(ROTATION_DIALOG_W, ROTATION_DIALOG_H);
        dialog.setResizable(false);
        dialog.setBackground(PARCHMENT_BG);

        JPanel panel = new BackgroundParchmentPanel("rotate2.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JButton oneButton = creerPetitBoutonTransparent("1");
        JButton twoButton = creerPetitBoutonTransparent("2");
        JButton threeButton = creerPetitBoutonTransparent("3");

        oneButton.setBounds(82, 185, 72, 42);
        twoButton.setBounds(174, 185, 72, 42);
        threeButton.setBounds(266, 185, 72, 42);

        oneButton.addActionListener(e -> {
            result[0] = 1;
            dialog.dispose();
        });

        twoButton.addActionListener(e -> {
            result[0] = 2;
            dialog.dispose();
        });

        threeButton.addActionListener(e -> {
            result[0] = 3;
            dialog.dispose();
        });

        panel.add(oneButton);
        panel.add(twoButton);
        panel.add(threeButton);

        dialog.setContentPane(panel);
        positionnerDialogEnHautDroite(parent, dialog);
        afficherAvecFondAssombri(parent, dialog);

        return result[0];
    }

    private static JDialog creerDialog(Component parent, String title) {
        Window owner = SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(DIALOG_W, DIALOG_H);
        dialog.setResizable(false);
        dialog.setBackground(PARCHMENT_BG);

        return dialog;
    }

    private static JDialog creerDialogWitness(Component parent) {
        Window owner = SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(owner, "Witness Phase", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(620, 720);
        dialog.setResizable(false);
        dialog.setBackground(new Color(0, 0, 0, 0));

        return dialog;
    }

    private static void positionnerDialogEnHautDroite(Component parent, JDialog dialog) {
        Window window = SwingUtilities.getWindowAncestor(parent);

        if (window == null) {
            dialog.setLocationRelativeTo(parent);
            return;
        }

        try {
            Point windowLocation = window.getLocationOnScreen();

            int marginRight = 35;
            int marginTop = 95;

            int x = windowLocation.x + window.getWidth() - dialog.getWidth() - marginRight;
            int y = windowLocation.y + marginTop;

            dialog.setLocation(x, y);

        } catch (IllegalComponentStateException e) {
            dialog.setLocationRelativeTo(parent);
        }
    }

    public static void afficherAvecFondAssombri(Component parent, JDialog dialog) {
        Window window = SwingUtilities.getWindowAncestor(parent);

        if (!(window instanceof JFrame)) {
            positionnerDialogSelonType(parent, dialog);
            dialog.setVisible(true);
            return;
        }

        JFrame frame = (JFrame) window;
        JLayeredPane layeredPane = frame.getLayeredPane();

        JPanel darkOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        darkOverlay.setOpaque(true);
        darkOverlay.setBackground(new Color(0, 0, 0));
        darkOverlay.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        layeredPane.add(darkOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                retirerOverlay(layeredPane, darkOverlay);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                retirerOverlay(layeredPane, darkOverlay);
            }
        });

        positionnerDialogSelonType(parent, dialog);
        dialog.setVisible(true);

        retirerOverlay(layeredPane, darkOverlay);
    }

    private static void positionnerDialogSelonType(Component parent, JDialog dialog) {
        if (ROTATION_CHOICE_TITLE.equals(dialog.getTitle())) {
            if (dialog.getLocation().x == 0 && dialog.getLocation().y == 0) {
                positionnerDialogEnHautDroite(parent, dialog);
            }
        } else {
            dialog.setLocationRelativeTo(parent);
        }
    }

    private static void retirerOverlay(JLayeredPane layeredPane, JPanel darkOverlay) {
        if (darkOverlay.getParent() != null) {
            layeredPane.remove(darkOverlay);
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    private static JButton creerBoutonTransparent(String texte) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(95, 65, 35));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(150, 105, 55));
                } else {
                    g2.setColor(new Color(120, 82, 42));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor(new Color(55, 35, 20));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        bouton.setFont(new Font("Serif", Font.BOLD, 22));
        bouton.setForeground(new Color(35, 22, 12));
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return bouton;
    }

    private static JButton creerPetitBoutonTransparent(String texte) {
        JButton bouton = creerBoutonTransparent(texte);
        bouton.setFont(new Font("Serif", Font.BOLD, 18));
        return bouton;
    }

    private static class BackgroundParchmentPanel extends JPanel {

        private BufferedImage background;

        public BackgroundParchmentPanel(String fileName) {
            setOpaque(true);
            setBackground(PARCHMENT_BG);
            background = chargerImageParchemin(fileName);
        }

        private BufferedImage chargerImageParchemin(String fileName) {
            String resourcePath = "/assets/images/action_message/" + fileName;
            String filePath = System.getProperty("user.dir")
                    + "/assets/images/action_message/"
                    + fileName;

            try {
                File file = new File(filePath);

                if (file.exists()) {
                    return ImageIO.read(file);
                }

                try (InputStream inputStream = ParcheminDialog.class.getResourceAsStream(resourcePath)) {
                    if (inputStream != null) {
                        return ImageIO.read(inputStream);
                    }
                }

                System.out.println("Parchemin image non trouvée : " + filePath);
                System.out.println("Resource aussi introuvable : " + resourcePath);
                return null;

            } catch (Exception e) {
                System.out.println("Erreur chargement parchemin : " + fileName + " -> " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(PARCHMENT_BG);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(PARCHMENT_BORDER);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(8, 8, getWidth() - 16, getHeight() - 16);

            if (background != null) {
                g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }

            g2.dispose();
        }
    }

    public static void EchangeErreurMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Échange");

        JPanel panel = new BackgroundParchmentPanel("echange_erreur.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align:center;'>Vous devez sélectionner<br>deux cartes différentes.</div></html>",
                SwingConstants.CENTER
        );

        messageLabel.setFont(new Font("Serif", Font.BOLD, 28));
        messageLabel.setForeground(new Color(45, 30, 18));
        messageLabel.setBounds(65, 245, 390, 110);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(messageLabel);
        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void ActionEnCoursMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Action en cours");

        JPanel panel = new BackgroundParchmentPanel("action_erreur.png");
        panel.setLayout(null);
        panel.setOpaque(true);

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align:center;'>Une action est déjà en cours.<br>Terminez-la avant de choisir<br>un autre token.</div></html>",
                SwingConstants.CENTER
        );

        messageLabel.setFont(new Font("Serif", Font.BOLD, 24));
        messageLabel.setForeground(new Color(45, 30, 18));
        messageLabel.setBounds(60, 245, 400, 125);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(messageLabel);
        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    private static ImageIcon chargerImageWitness(String nomFichier, int largeur, int hauteur) {
        String chemin = System.getProperty("user.dir")
                + "/assets/images/action_message/"
                + nomFichier;

        File fichier = new File(chemin);

        if (!fichier.exists()) {
            System.out.println("Image witness non trouvée : " + chemin);
            return null;
        }

        try {
            BufferedImage imageOriginale = ImageIO.read(fichier);

            if (imageOriginale == null) {
                return null;
            }

            Image imageRedimensionnee = imageOriginale.getScaledInstance(
                    largeur,
                    hauteur,
                    Image.SCALE_SMOOTH
            );

            return new ImageIcon(imageRedimensionnee);

        } catch (Exception e) {
            System.out.println("Erreur image witness : " + e.getMessage());
            return null;
        }
    }
}