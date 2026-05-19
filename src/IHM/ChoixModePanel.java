package IHM;

import ai.AIDifficulty;
import java.awt.*;
import javax.swing.*;

public class ChoixModePanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    private JPanel contenuPanel;
    private JPanel panel;

    private JLabel titre;

    private JButton humainVsHumainButton;
    private JButton humainVsIAButton;
    private JButton iaVsIAButton;
    private JButton retourButton;

    public ChoixModePanel(FenetrePrincipale fenetre) {
        boutonClickMusique = new BoutonClickMusique();

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png";
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        setLayout(new BorderLayout());
        setOpaque(false);

        titre = new JLabel("Choix du mode de jeu", SwingConstants.LEFT);
        //titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(new Color(245, 235, 210));
        titre.setOpaque(false);
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        humainVsHumainButton = new JButton("Humain vs Humain");
        humainVsIAButton = new JButton("Humain vs IA");
        iaVsIAButton = new JButton("IA vs IA");
        retourButton = new JButton("Retour");

        BoutonTexte(humainVsHumainButton);
        BoutonTexte(humainVsIAButton);
        BoutonTexte(iaVsIAButton);
        BoutonTexte(retourButton);

        humainVsHumainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        humainVsIAButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        iaVsIAButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        retourButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(humainVsHumainButton);
        panel.add(Box.createVerticalStrut(18));
        panel.add(humainVsIAButton);
        panel.add(Box.createVerticalStrut(18));
        panel.add(iaVsIAButton);
        panel.add(Box.createVerticalStrut(18));
        panel.add(retourButton);

        contenuPanel = new JPanel();
        contenuPanel.setOpaque(false);
        contenuPanel.setLayout(new BoxLayout(contenuPanel, BoxLayout.Y_AXIS));
        contenuPanel.setBorder(BorderFactory.createEmptyBorder(280, 90, 40, 0));

        titre.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenuPanel.add(titre);
        contenuPanel.add(Box.createVerticalStrut(55));
        contenuPanel.add(panel);

        add(contenuPanel, BorderLayout.CENTER);

        humainVsHumainButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherChoixRoleIcones(fenetre, GameMode.HUMAN_VS_HUMAN);
        });

        humainVsIAButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherChoixRoleIcones(fenetre, GameMode.HUMAN_VS_IA);
        });

        iaVsIAButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherChoixDifficulte(fenetre, GameMode.IA_VS_IA, "Investigator");
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
        });

        mettreAJourResponsiveLayout();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    mettreAJourResponsiveLayout();
                });
            }
        });
    }

    private void BoutonTexte(JButton bouton) {
        //bouton.setFont(new Font("Arial", Font.BOLD, 20));
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setMargin(new Insets(4, 8, 4, 8));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void afficherChoixRoleIcones(FenetrePrincipale fenetre, GameMode mode) {
        JDialog dialog = new JDialog(fenetre, "Choix du rôle", true);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Choisissez le rôle du Joueur 1", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panelRoles = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 20));

        JLabel investigator = creerRoleIcone("Investigator", "assets/images/characters/investigator.png");
        JLabel jack = creerRoleIcone("Jack", "assets/images/characters/jack.png");

        investigator.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();

                if (mode == GameMode.HUMAN_VS_IA) {
                    afficherChoixDifficulte(fenetre, mode, "Investigator");
                } else {
                    fenetre.lancerJeu(mode, "Investigator");
                }
            }
        });

        jack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();

                if (mode == GameMode.HUMAN_VS_IA) {
                    afficherChoixDifficulte(fenetre, mode, "Jack");
                } else {
                    fenetre.lancerJeu(mode, "Jack");
                }
            }
        });

        panelRoles.add(investigator);
        panelRoles.add(jack);

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panelRoles, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void afficherChoixDifficulte(FenetrePrincipale fenetre, GameMode mode, String player1Role) {
        JDialog dialog = new JDialog(fenetre, "Choix de difficulté IA", true);
        dialog.setSize(420, 340);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Choisissez la difficulté de l'IA", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 90, 25, 90));

        JButton randomButton = new JButton("Random");
        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");
        JButton expertButton = new JButton("Expert");

        JButton[] buttons = {
                randomButton,
                easyButton,
                mediumButton,
                hardButton,
                expertButton
        };

        for (JButton button : buttons) {
            BoutonTexte(button);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(220, 40));
        }

        randomButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.RANDOM);
        });

        easyButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.EASY);
        });

        mediumButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.MEDIUM);
        });

        hardButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.HARD);
        });

        expertButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.EXPERT);
        });

        panel.add(randomButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(easyButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(mediumButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(hardButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(expertButton);

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JLabel creerRoleIcone(String texte, String cheminImage) {
        ImageIcon icon = new ImageIcon(cheminImage);
        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel label = new JLabel(texte, new ImageIcon(image), SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return label;
    }

    private void mettreAJourResponsiveLayout(){
        int top = Math.min(350, Math.max(120, getHeight() / 3));
        int left = Math.min(180, Math.max(30, getWidth() / 10));

        contenuPanel.setBorder(BorderFactory.createEmptyBorder(top, left, 40, 0));

        int tailleTitre = Math.max(20, getHeight() / 25);
        int tailleBouton = Math.max(16, getHeight() / 35);

        titre.setFont(new Font("Arial", Font.BOLD, tailleTitre));

        Font boutonFont = new Font("Arial", Font.BOLD, tailleBouton);

        humainVsHumainButton.setFont(boutonFont);
        humainVsIAButton.setFont(boutonFont);
        iaVsIAButton.setFont(boutonFont);
        retourButton.setFont(boutonFont);

        int espace = Math.max(8, getHeight() / 70);

        panel.removeAll();
        
        panel.add(humainVsHumainButton);
        panel.add(Box.createVerticalStrut(espace));

        panel.add(humainVsIAButton);
        panel.add(Box.createVerticalStrut(espace));

        panel.add(iaVsIAButton);
        panel.add(Box.createVerticalStrut(espace));

        panel.add(retourButton);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}