package IHM;

import java.awt.*;
import javax.swing.*;

public class ReglesPanel extends JPanel {
    private int pageActuelle;
    private int nbPages = 9;
    private JLabel imageLabel, pageLabel;
    private BoutonClickMusique boutonClickMusique;

    private JLabel titre;

    private JButton precedentButton;
    private JButton suivantButton;
    private JButton retourButton;

    private JPanel navigationPanel;

    public ReglesPanel(FenetrePrincipale fenetre) {
        pageActuelle = 1;
        setLayout(new BorderLayout()); // config layout principal
        setBackground(Color.BLACK);
        boutonClickMusique = new BoutonClickMusique();

        titre = new JLabel("RÈGLES DU JEU", SwingConstants.CENTER);
        //titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        imageLabel = new JLabel(); // création jlabel pour l'affichage des images
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(imageLabel); // scrollable view
        scrollPane.setBackground(Color.BLACK);
        // boutons de navigation
        precedentButton = new JButton("Précédent");
        suivantButton = new JButton("Suivant");
        retourButton = new JButton("Retour");

        pageLabel = new JLabel("", SwingConstants.CENTER);
        pageLabel.setForeground(Color.WHITE);
        //pageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // création panel boutons
        JPanel boutonsPanel = new JPanel(new BorderLayout());
        boutonsPanel.setBackground(Color.BLACK);
        // création panel navigation
        navigationPanel = new JPanel();
        navigationPanel.setBackground(Color.BLACK);
        navigationPanel.add(precedentButton);
        navigationPanel.add(pageLabel);
        navigationPanel.add(suivantButton);
        navigationPanel.add(retourButton);

        boutonsPanel.add(navigationPanel, BorderLayout.CENTER);

        add(titre, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(boutonsPanel, BorderLayout.SOUTH);

        precedentButton.addActionListener(e -> { // bouton "precedent"
            boutonClickMusique.jouerClick();
            if (pageActuelle > 1) {
                pageActuelle--;
                afficherPage();
            }
        });

        suivantButton.addActionListener(e -> { // bouton "suivant"
            boutonClickMusique.jouerClick();
            if (pageActuelle < nbPages) {
                pageActuelle++;
                afficherPage();
            }
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
        });

        afficherPage(); // afficher première page

        mettreAJourResponsiveLayout();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    mettreAJourResponsiveLayout();
                    afficherPage();
                });
            }
        });
    }

    private void afficherPage() {
        String chemin = System.getProperty("user.dir") + "/assets/images/regles/page" + pageActuelle + ".png";

        ImageIcon icon = new ImageIcon(chemin);
        // redimensionner
        Image image = icon.getImage();

        int largeur = Math.max(300, getWidth() - 120);
        int hauteur = Math.max(300, getHeight() - 220);

        double ratioLargeur = (double) largeur / image.getWidth(null);
        double ratioHauteur = (double) hauteur / image.getHeight(null);

        double ratio = Math.min(ratioLargeur, ratioHauteur);

        int nouvelleLargeur = (int)(image.getWidth(null) * ratio);
        int nouvelleHauteur = (int)(image.getHeight(null) * ratio);

        Image imageRedimensionnee = image.getScaledInstance(nouvelleLargeur, nouvelleHauteur, Image.SCALE_SMOOTH); // redimensionner l'image

        imageLabel.setIcon(new ImageIcon(imageRedimensionnee));
        pageLabel.setText("Page " + pageActuelle + " / " + nbPages);
    }

    private void mettreAJourResponsiveLayout(){
        int tailleTitre = Math.max(18, getHeight() / 22);
        int tailleTexte = Math.max(12, getHeight() / 45);

        titre.setFont(new Font("Arial", Font.BOLD, tailleTitre));

        Font boutonFont = new Font("Arial", Font.BOLD, tailleTexte);

        precedentButton.setFont(boutonFont);
        suivantButton.setFont(boutonFont);
        retourButton.setFont(boutonFont);

        pageLabel.setFont(new Font("Arial", Font.BOLD, tailleTexte));
    }
}
