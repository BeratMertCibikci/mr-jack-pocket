package IHM;

import ai.AIDifficulty;
import java.awt.*;
import javax.swing.*;

public class ChoixModePanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    public ChoixModePanel(FenetrePrincipale fenetre) {
        boutonClickMusique = new BoutonClickMusique();

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png";
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titre = new JLabel("Choix Mode Jeu", SwingConstants.LEFT);
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        titre.setHorizontalAlignment(SwingConstants.LEFT);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(new Color(212, 175, 55));
        titre.setOpaque(false);
        titre.setMaximumSize(new Dimension(360, 42));
        titre.setPreferredSize(new Dimension(360, 42));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton humainVsHumainButton = new JButton("Humain vs Humain");
        JButton humainVsIAButton = new JButton("Humain vs IA");
        JButton iaVsIAButton = new JButton("IA vs IA");
        JButton retourButton = new JButton("Retour");

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

        JPanel contenuPanel = new JPanel();
        contenuPanel.setOpaque(false);
        contenuPanel.setLayout(new BoxLayout(contenuPanel, BoxLayout.Y_AXIS));
        contenuPanel.setBorder(BorderFactory.createEmptyBorder(365, 155, 40, 0));

        titre.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenuPanel.add(titre);
        contenuPanel.add(Box.createVerticalStrut(24));
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
            afficherChoixDifficulteInvestigatorIA(fenetre);
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
        });
    }

    private void BoutonTexte(JButton bouton) {
        bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bouton.setFont(new Font("Arial", Font.BOLD, 22));
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setHorizontalAlignment(SwingConstants.LEFT);
        bouton.setHorizontalTextPosition(SwingConstants.LEFT);
        bouton.setMaximumSize(new Dimension(360, 42));
        bouton.setPreferredSize(new Dimension(360, 42));
        bouton.setMargin(new Insets(0, 0, 0, 0));
        bouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void afficherChoixRoleIcones(FenetrePrincipale fenetre, GameMode mode) {
        JDialog dialog = new JDialog(fenetre, "Choix du rôle", true);

        dialog.setSize(720, 520);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
private Image parcheminImage = new ImageIcon(
        System.getProperty("user.dir") + "/assets/images/action_message/choix_du_role.png"
).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.drawImage(parcheminImage, 0, 0, getWidth(), getHeight(), this);

                g2.dispose();
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false);

        JPanel panelRoles = new JPanel(new FlowLayout(FlowLayout.CENTER, 90, 0));
        panelRoles.setOpaque(false);
        panelRoles.setBorder(BorderFactory.createEmptyBorder(125, 0, 0, 0));

JLabel investigator = creerRoleIconeParchemin(
        "Investigator",
        System.getProperty("user.dir") + "/assets/images/characters/investigator_new.png"
);

JLabel jack = creerRoleIconeParchemin(
        "Jack",
        System.getProperty("user.dir") + "/assets/images/characters/jack_new.png"
);

        investigator.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();

                if (mode == GameMode.HUMAN_VS_IA) {
                    afficherChoixDifficulteUniqueIA(fenetre, mode, "Investigator");
                } else {
                    fenetre.lancerJeu(mode, "Investigator");
                }
            }
        });

        jack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();

                if (mode == GameMode.HUMAN_VS_IA) {
                    afficherChoixDifficulteUniqueIA(fenetre, mode, "Jack");
                } else {
                    fenetre.lancerJeu(mode, "Jack");
                }
            }
        });

        panelRoles.add(investigator);
        panelRoles.add(jack);

        backgroundPanel.add(panelRoles, BorderLayout.CENTER);

        dialog.setContentPane(backgroundPanel);
        dialog.setVisible(true);
    }

    private void afficherChoixDifficulteUniqueIA(
            FenetrePrincipale fenetre,
            GameMode mode,
            String player1Role
    ) {
        JDialog dialog = creerDialogDifficulte(fenetre, "Choix de difficulté IA");

        JLabel titre = new JLabel("Choisissez la difficulté de l'IA", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panel = creerPanelDifficulte();

        ajouterBoutonDifficulte(panel, "Easy", AIDifficulty.EASY, () -> {
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.EASY);
        });

        ajouterBoutonDifficulte(panel, "Medium", AIDifficulty.MEDIUM, () -> {
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.MEDIUM);
        });

        ajouterBoutonDifficulte(panel, "Hard", AIDifficulty.HARD, () -> {
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.HARD);
        });

        ajouterBoutonDifficulte(panel, "Expert", AIDifficulty.EXPERT, () -> {
            dialog.dispose();
            fenetre.lancerJeu(mode, player1Role, AIDifficulty.EXPERT);
        });

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void afficherChoixDifficulteInvestigatorIA(FenetrePrincipale fenetre) {
        JDialog dialog = creerDialogDifficulte(fenetre, "Difficulté Investigator IA");

        JLabel titre = new JLabel("Choisissez la difficulté de l'Investigator IA", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panel = creerPanelDifficulte();

        ajouterBoutonDifficulte(panel, "Easy", AIDifficulty.EASY, () -> {
            dialog.dispose();
            afficherChoixDifficulteJackIA(fenetre, AIDifficulty.EASY);
        });

        ajouterBoutonDifficulte(panel, "Medium", AIDifficulty.MEDIUM, () -> {
            dialog.dispose();
            afficherChoixDifficulteJackIA(fenetre, AIDifficulty.MEDIUM);
        });

        ajouterBoutonDifficulte(panel, "Hard", AIDifficulty.HARD, () -> {
            dialog.dispose();
            afficherChoixDifficulteJackIA(fenetre, AIDifficulty.HARD);
        });

        ajouterBoutonDifficulte(panel, "Expert", AIDifficulty.EXPERT, () -> {
            dialog.dispose();
            afficherChoixDifficulteJackIA(fenetre, AIDifficulty.EXPERT);
        });

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void afficherChoixDifficulteJackIA(
            FenetrePrincipale fenetre,
            AIDifficulty investigatorDifficulty
    ) {
        JDialog dialog = creerDialogDifficulte(fenetre, "Difficulté Jack IA");

        JLabel titre = new JLabel("Choisissez la difficulté de Jack IA", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panel = creerPanelDifficulte();

        ajouterBoutonDifficulte(panel, "Easy", AIDifficulty.EASY, () -> {
            dialog.dispose();
            fenetre.lancerJeu(
                    GameMode.IA_VS_IA,
                    "Investigator",
                    investigatorDifficulty,
                    AIDifficulty.EASY
            );
        });

        ajouterBoutonDifficulte(panel, "Medium", AIDifficulty.MEDIUM, () -> {
            dialog.dispose();
            fenetre.lancerJeu(
                    GameMode.IA_VS_IA,
                    "Investigator",
                    investigatorDifficulty,
                    AIDifficulty.MEDIUM
            );
        });

        ajouterBoutonDifficulte(panel, "Hard", AIDifficulty.HARD, () -> {
            dialog.dispose();
            fenetre.lancerJeu(
                    GameMode.IA_VS_IA,
                    "Investigator",
                    investigatorDifficulty,
                    AIDifficulty.HARD
            );
        });

        ajouterBoutonDifficulte(panel, "Expert", AIDifficulty.EXPERT, () -> {
            dialog.dispose();
            fenetre.lancerJeu(
                    GameMode.IA_VS_IA,
                    "Investigator",
                    investigatorDifficulty,
                    AIDifficulty.EXPERT
            );
        });

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JDialog creerDialogDifficulte(FenetrePrincipale fenetre, String titre) {
        JDialog dialog = new JDialog(fenetre, titre, true);
        dialog.setSize(420, 340);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }

    private JPanel creerPanelDifficulte() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 90, 25, 90));
        return panel;
    }

    private void ajouterBoutonDifficulte(
            JPanel panel,
            String texte,
            AIDifficulty difficulty,
            Runnable action
    ) {
        JButton button = new JButton(texte);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(212, 175, 55));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 60, 20), 2));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40));

        button.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            action.run();
        });

        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
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

    private JLabel creerRoleIconeParchemin(String texte, String cheminImage) {
        ImageIcon icon = new ImageIcon(cheminImage);
        Image image = icon.getImage().getScaledInstance(145, 145, Image.SCALE_SMOOTH);

        JLabel label = new JLabel(texte, new ImageIcon(image), SwingConstants.CENTER);

        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);

        label.setFont(new Font("Serif", Font.BOLD, 28));
        label.setForeground(new Color(35, 20, 8));

        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setOpaque(false);

        label.setPreferredSize(new Dimension(190, 210));
        label.setMaximumSize(new Dimension(190, 210));

        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}