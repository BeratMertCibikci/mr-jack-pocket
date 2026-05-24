package IHM;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    private FenetrePrincipale fenetre;
    private JPanel boutonsPanel;

    public MenuPrincipalPanel(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png";
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        boutonClickMusique = new BoutonClickMusique();

        setLayout(new BorderLayout());
        setOpaque(false);

        boutonsPanel = new JPanel();
        boutonsPanel.setOpaque(false);
        boutonsPanel.setLayout(new BoxLayout(boutonsPanel, BoxLayout.Y_AXIS));
        boutonsPanel.setBorder(BorderFactory.createEmptyBorder(385, 155, 40, 0));

        add(boutonsPanel, BorderLayout.CENTER);

        afficherMenuPrincipal();
    }

    private void afficherMenuPrincipal() {
        boutonsPanel.removeAll();

        JButton jouerButton = new JButton("Jouer");
        JButton reglageButton = new JButton("Réglage");
        JButton quitterButton = new JButton("Quitter");

        BoutonTexte(jouerButton);
        BoutonTexte(reglageButton);
        BoutonTexte(quitterButton);

        jouerButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherSousMenuJouer();
        });

        reglageButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherSousMenuReglage();
        });

        quitterButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            Timer timer = new Timer(200, event -> System.exit(0));
            timer.setRepeats(false);
            timer.start();
        });

        boutonsPanel.add(jouerButton);
        boutonsPanel.add(Box.createVerticalStrut(18));
        boutonsPanel.add(reglageButton);
        boutonsPanel.add(Box.createVerticalStrut(18));
        boutonsPanel.add(quitterButton);

        boutonsPanel.revalidate();
        boutonsPanel.repaint();
    }

    private void afficherSousMenuJouer() {
        boutonsPanel.removeAll();

        JLabel titreLabel = new JLabel("Jouer");
        titreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titreLabel.setForeground(new Color(212, 175, 55));
        titreLabel.setMaximumSize(new Dimension(300, 40));
        titreLabel.setPreferredSize(new Dimension(300, 40));
        titreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton nouvellePartieButton = new JButton("Nouvelle partie");
        JButton multijoueurButton = new JButton("Multijoueur");
        JButton loadButton = new JButton("Charger le jeu");
        JButton retourButton = new JButton("Retour");

        BoutonTexte(nouvellePartieButton);
        BoutonTexte(multijoueurButton);
        BoutonTexte(loadButton);
        BoutonTexte(retourButton);

        nouvellePartieButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            // Human vs Human / Human vs IA / IA vs IA ekranı
            fenetre.afficherChoixMode();
        });

        multijoueurButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            // Online / réseau multiplayer ekranı
            fenetre.afficherMultijoueur();
        });

        loadButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.chargerJeuDepuisMenu();
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherMenuPrincipal();
        });

        boutonsPanel.add(titreLabel);
        boutonsPanel.add(Box.createVerticalStrut(24));
        boutonsPanel.add(nouvellePartieButton);
        boutonsPanel.add(Box.createVerticalStrut(16));
        boutonsPanel.add(multijoueurButton);
        boutonsPanel.add(Box.createVerticalStrut(16));
        boutonsPanel.add(loadButton);
        boutonsPanel.add(Box.createVerticalStrut(16));
        boutonsPanel.add(retourButton);

        boutonsPanel.revalidate();
        boutonsPanel.repaint();
    }

    private void afficherSousMenuReglage() {
        boutonsPanel.removeAll();

        JButton reglageTitleButton = new JButton("Réglage");
        JButton reglesButton = new JButton("Règles");
        JButton muteButton = new JButton(fenetre.estMusiqueActivee() ? "Mute" : "Unmute");
        JButton retourButton = new JButton("Retour");

        BoutonTexte(reglageTitleButton);
        BoutonTexte(reglesButton);
        BoutonTexte(muteButton);
        BoutonTexte(retourButton);

        reglageTitleButton.setEnabled(false);
        reglageTitleButton.setForeground(new Color(212, 175, 55));

        reglesButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherRegles();
        });

        muteButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.toggleMusique();

            if (fenetre.estMusiqueActivee()) {
                muteButton.setText("Mute");
            } else {
                muteButton.setText("Unmute");
            }
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherMenuPrincipal();
        });

        boutonsPanel.add(reglageTitleButton);
        boutonsPanel.add(Box.createVerticalStrut(18));
        boutonsPanel.add(reglesButton);
        boutonsPanel.add(Box.createVerticalStrut(18));
        boutonsPanel.add(muteButton);
        boutonsPanel.add(Box.createVerticalStrut(18));
        boutonsPanel.add(retourButton);

        boutonsPanel.revalidate();
        boutonsPanel.repaint();
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

        bouton.setMaximumSize(new Dimension(300, 42));
        bouton.setPreferredSize(new Dimension(300, 42));

        bouton.setMargin(new Insets(0, 0, 0, 0));
        bouton.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}