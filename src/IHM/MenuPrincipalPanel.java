package IHM;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    public MenuPrincipalPanel(FenetrePrincipale fenetre) {

        // 1. Arka plan fotoğrafını yükleme
        String cheminImage = System.getProperty("user.dir") + "/images/deneme2.jpg"; // chemin absolu de l'image
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        //buton click
        boutonClickMusique = new BoutonClickMusique();

        // 2. Ana panel düzeni
        setLayout(new BorderLayout());
        setOpaque(false);

        // 3. Başlık
        /*JLabel titreLabel = new JLabel("MR. JACK POCKET", SwingConstants.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setOpaque(false); // pour qu'on ne bloque pas l'arriere-plan
        titreLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0)); */

        // 4. Butonları tutan panel
        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setOpaque(false); // pour qu'on ne bloque pas l'arriere-plan
        boutonsPanel.setLayout(new GridLayout(3, 1, 15, 15)); // 3 lignes, 1 colonne, 15 pixels entre les boutons
        boutonsPanel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300)); // le comptour des boutons sont vides

        // 5. Butonlar
        JButton jouerButton = new JButton("Jouer");
        JButton reglesButton = new JButton("Règles");
        JButton quitterButton = new JButton("Quitter");

        jouerButton.setFont(new Font("Arial", Font.BOLD, 20));
        reglesButton.setFont(new Font("Arial", Font.BOLD, 20));
        quitterButton.setFont(new Font("Arial", Font.BOLD, 20));

        boutonsPanel.add(jouerButton);
        boutonsPanel.add(reglesButton);
        boutonsPanel.add(quitterButton);

        // 6. Ekrana ekleme
        //add(titreLabel, BorderLayout.NORTH);
        add(boutonsPanel, BorderLayout.CENTER);

        // 7. Buton aksiyonları
        jouerButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Jouer cliqué");
            fenetre.afficherChoixMode();
        });

        reglesButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Règles cliqué");
        });

        quitterButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.exit(0);

            Timer timer = new Timer(200, event -> System.exit(0));
            timer.setRepeats(false);
            timer.start();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // étale la photo sur le panneau
        }
    }
}