package IHM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ParcheminDialog {

    private static final int DIALOG_W = 520;
    private static final int DIALOG_H = 560;

    public static void showMessage(Component parent, String title, String message) {
        JDialog dialog = creerDialog(parent, title);

        JPanel panel = new BackgroundParchmentPanel("rotate.png");
        panel.setLayout(null);
        panel.setOpaque(false);

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
        panel.setOpaque(false);

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
        panel.setOpaque(false);

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
        panel.setOpaque(false);

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
        panel.setOpaque(false);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static void showWitnessPhaseMessage(Component parent, boolean jackVisible) {
        JDialog dialog = creerDialog(parent, "Witness Phase");

        JPanel panel = new BackgroundParchmentPanel("witness_phase.png");
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel questionLabel = new JLabel("Jack est-il visible ?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Serif", Font.BOLD, 30));
        questionLabel.setForeground(new Color(45, 30, 18));
        questionLabel.setBounds(65, 220, 390, 45);

        JLabel answerLabel = new JLabel(jackVisible ? "OUI" : "NON", SwingConstants.CENTER);
        answerLabel.setFont(new Font("Serif", Font.BOLD, 40));
        answerLabel.setForeground(jackVisible
                ? new Color(75, 45, 20)
                : new Color(120, 35, 25));
        answerLabel.setBounds(160, 285, 200, 55);

        JLabel descriptionLabel = new JLabel(
                jackVisible
                        ? "<html><div style='text-align:center;'>Jack est visible par<br>au moins un détective.</div></html>"
                        : "<html><div style='text-align:center;'>Jack n'est visible par<br>aucun détective.</div></html>",
                SwingConstants.CENTER
        );

        descriptionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        descriptionLabel.setForeground(new Color(45, 30, 18));
        descriptionLabel.setBounds(70, 355, 380, 85);

        JButton okButton = creerBoutonTransparent("Continuer");
        okButton.setBounds(185, 495, 150, 38);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(questionLabel);
        panel.add(answerLabel);
        panel.add(descriptionLabel);
        panel.add(okButton);

        dialog.setContentPane(panel);
        afficherAvecFondAssombri(parent, dialog);
    }

    public static int demanderRotation(Component parent) {
        final int[] result = {0};

        JDialog dialog = creerDialog(parent, "Rotate");

        JPanel panel = new BackgroundParchmentPanel("rotate2.png");
        panel.setLayout(null);
        panel.setOpaque(false);

        JButton oneButton = creerBoutonTransparent("1");
        JButton twoButton = creerBoutonTransparent("2");
        JButton threeButton = creerBoutonTransparent("3");

        oneButton.setBounds(120, 450, 80, 45);
        twoButton.setBounds(220, 450, 80, 45);
        threeButton.setBounds(320, 450, 80, 45);

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
        afficherAvecFondAssombri(parent, dialog);

        return result[0];
    }

    private static JDialog creerDialog(Component parent, String title) {
        Window owner = SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(DIALOG_W, DIALOG_H);
        dialog.setResizable(false);
        dialog.setBackground(new Color(0, 0, 0, 0));

        return dialog;
    }

    public static void afficherAvecFondAssombri(Component parent, JDialog dialog) {
        Window window = SwingUtilities.getWindowAncestor(parent);

        if (!(window instanceof JFrame)) {
            dialog.setLocationRelativeTo(parent);
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
                g2.setColor(new Color(0, 0, 0, 170));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        darkOverlay.setOpaque(false);
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

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        retirerOverlay(layeredPane, darkOverlay);
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
                    g2.setColor(new Color(95, 65, 35, 210));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(150, 105, 55, 200));
                } else {
                    g2.setColor(new Color(120, 82, 42, 165));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor(new Color(55, 35, 20, 230));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        bouton.setFont(new Font("Serif", Font.BOLD, 22));
        bouton.setForeground(new Color(35, 22, 12));
        bouton.setFocusPainted(false);
        bouton.setOpaque(false);
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return bouton;
    }

    private static class BackgroundParchmentPanel extends JPanel {

        private BufferedImage background;

        public BackgroundParchmentPanel(String fileName) {
            setOpaque(false);

            String chemin = System.getProperty("user.dir")
                    + "/assets/images/action_message/"
                    + fileName;

            try {
                background = ImageIO.read(new File(chemin));
            } catch (Exception e) {
                System.out.println("Parchemin image non trouvée : " + chemin);
                background = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (background != null) {
                g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2.setColor(new Color(238, 218, 175));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2.setColor(new Color(120, 92, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 24, 24);
            }

            g2.dispose();
        }
    }

    public static void EchangeErreurMessage(Component parent) {
        JDialog dialog = creerDialog(parent, "Échange");

        JPanel panel = new BackgroundParchmentPanel("echange_erreur.png");
        panel.setLayout(null);
        panel.setOpaque(false);

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
}