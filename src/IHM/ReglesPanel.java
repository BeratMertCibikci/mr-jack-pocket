package IHM;

import javax.swing.*;
import java.awt.*;

public class ReglesPanel extends JPanel {
    private int pageActuelle;
    private int nbPages = 9;
    private JLabel imageLabel, pageLabel;
    private BoutonClickMusique boutonClickMusique;

    public ReglesPanel(FenetrePrincipale fenetre) {
        pageActuelle = 1;
        setLayout(new BorderLayout()); // config layout principal
        setBackground(Color.BLACK);
        boutonClickMusique = new BoutonClickMusique();

        JLabel titre = new JLabel("RÈGLES DU JEU", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        imageLabel = new JLabel(); // création jlabel pour l'affichage des images
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(imageLabel); // scrollable view
        scrollPane.setBackground(Color.BLACK);
        // boutons de navigation
        JButton precedentButton = new JButton("Précédent");
        JButton suivantButton = new JButton("Suivant");
        JButton retourButton = new JButton("Retour");

        pageLabel = new JLabel("", SwingConstants.CENTER);
        pageLabel.setForeground(Color.WHITE);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // création panel boutons
        JPanel boutonsPanel = new JPanel(new BorderLayout());
        boutonsPanel.setBackground(Color.BLACK);
        // création panel navigation
        JPanel navigationPanel = new JPanel();
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
    }

    private void afficherPage() {
        String chemin = System.getProperty("user.dir") + "/assets/images/regles/page" + pageActuelle + ".png";

        ImageIcon icon = new ImageIcon(chemin);
        // redimensionner
        Image image = icon.getImage();
        Image imageRedimensionnee = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH); // redimensionner l'image

        imageLabel.setIcon(new ImageIcon(imageRedimensionnee));
        pageLabel.setText("Page " + pageActuelle + " / " + nbPages);
    }
}
