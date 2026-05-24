package IHM;

import javax.swing.*;
import java.awt.*;

public class ReglesPanel extends JPanel {

    private int pageActuelle;
    private int nbPages = 9;

    private JLabel imageLabel;
    private JLabel pageLabel;

    private BoutonClickMusique boutonClickMusique;
    private Runnable onRetour;

    public ReglesPanel(FenetrePrincipale fenetre) {
        this(() -> fenetre.afficherMenu());
    }

    public ReglesPanel(Runnable onRetour) {
        this.onRetour = onRetour;

        pageActuelle = 1;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        boutonClickMusique = new BoutonClickMusique();

        JLabel titre = new JLabel("RÈGLES DU JEU", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(null);

        JButton precedentButton = creerBouton("Précédent");
        JButton suivantButton = creerBouton("Suivant");
        JButton retourButton = creerBouton("Retour");

        pageLabel = new JLabel("", SwingConstants.CENTER);
        pageLabel.setForeground(Color.WHITE);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel boutonsPanel = new JPanel(new BorderLayout());
        boutonsPanel.setBackground(Color.BLACK);

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navigationPanel.setBackground(Color.BLACK);

        navigationPanel.add(precedentButton);
        navigationPanel.add(pageLabel);
        navigationPanel.add(suivantButton);
        navigationPanel.add(retourButton);

        boutonsPanel.add(navigationPanel, BorderLayout.CENTER);

        add(titre, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(boutonsPanel, BorderLayout.SOUTH);

        precedentButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            if (pageActuelle > 1) {
                pageActuelle--;
                afficherPage();
            }
        });

        suivantButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            if (pageActuelle < nbPages) {
                pageActuelle++;
                afficherPage();
            }
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            if (onRetour != null) {
                onRetour.run();
            }
        });

        afficherPage();
    }

    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);

        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setBackground(new Color(55, 45, 35));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));

        return bouton;
    }

    private void afficherPage() {
        String chemin = System.getProperty("user.dir")
                + "/assets/images/regles/page"
                + pageActuelle
                + ".png";

        ImageIcon icon = new ImageIcon(chemin);

        if (icon.getIconWidth() <= 0) {
            imageLabel.setIcon(null);
            imageLabel.setText("Image non trouvée : " + chemin);
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            pageLabel.setText("Page " + pageActuelle + " / " + nbPages);
            return;
        }

        Image image = icon.getImage();

        Image imageRedimensionnee = image.getScaledInstance(
                500,
                500,
                Image.SCALE_SMOOTH
        );

        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(imageRedimensionnee));

        pageLabel.setText("Page " + pageActuelle + " / " + nbPages);
    }
}