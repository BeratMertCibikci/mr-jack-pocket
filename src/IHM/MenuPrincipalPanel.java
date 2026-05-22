package IHM;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    public MenuPrincipalPanel(FenetrePrincipale fenetre) {

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png"; // chemin absolu de l'image
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        boutonClickMusique = new BoutonClickMusique();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setOpaque(false); // pour qu'on ne bloque pas l'arriere-plan
        //boutonsPanel.setLayout(new GridLayout(3, 1, 15, 15)); // 3 lignes, 1 colonne, 15 pixels entre les boutons
        boutonsPanel.setLayout(new BoxLayout(boutonsPanel, BoxLayout.Y_AXIS));
        boutonsPanel.setBorder(BorderFactory.createEmptyBorder(330, 140, 40, 0)); // le comptour des boutons sont vides

        JButton jouerButton = new JButton("Jouer");
        JButton multijoueurButton = new JButton("Multijoueur");
        JButton loadButton = new JButton("Load Game");
        JButton reglesButton = new JButton("Règles");
        JButton quitterButton = new JButton("Quitter");
        JButton muteButton = new JButton("Unmute");

        BoutonTexte(jouerButton);
        BoutonTexte(multijoueurButton); 
        BoutonTexte(loadButton);
        BoutonTexte(reglesButton);
        BoutonTexte(quitterButton);
        BoutonTexte(muteButton); 

        jouerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        multijoueurButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        reglesButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        quitterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        muteButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        boutonsPanel.add(jouerButton);
        boutonsPanel.add(Box.createVerticalStrut(10));
        boutonsPanel.add(multijoueurButton); 
        boutonsPanel.add(Box.createVerticalStrut(10));
        boutonsPanel.add(loadButton);
        boutonsPanel.add(Box.createVerticalStrut(10));
        boutonsPanel.add(reglesButton);
        boutonsPanel.add(Box.createVerticalStrut(10));
        boutonsPanel.add(muteButton);
        boutonsPanel.add(Box.createVerticalStrut(10));
        boutonsPanel.add(quitterButton);

        //add(titreLabel, BorderLayout.NORTH);
        add(boutonsPanel, BorderLayout.CENTER);

        jouerButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Jouer cliqué");
            fenetre.afficherChoixMode();
        });

        multijoueurButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMultijoueur(); // Birazdan FenetrePrincipale'e bu metodu ekleyeceğiz
        });

        loadButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Load Game cliqué");
            fenetre.chargerJeuDepuisMenu();
        });

        reglesButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Règles cliqué");
            fenetre.afficherRegles();
        });

        muteButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Mute cliqué");
            fenetre.toggleMusique();

            if (fenetre.estMusiqueActivee()) {
                muteButton.setText("Mute");
            } else {
                muteButton.setText("Unmute");
            }
        });

        quitterButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            //System.exit(0);

            Timer timer = new Timer(200, event -> System.exit(0));
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void BoutonTexte(JButton bouton) {

            bouton.setFont(new Font("Arial", Font.BOLD, 20));
            bouton.setForeground(new Color(245, 235, 210));
            bouton.setContentAreaFilled(false);
            bouton.setOpaque(false);
            bouton.setBorderPainted(false);
            bouton.setFocusPainted(false);
            bouton.setMargin(new Insets(4, 8, 4, 8));
            bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // étale la photo sur le panneau
        }
    }
}