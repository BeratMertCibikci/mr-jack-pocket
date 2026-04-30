package IHM;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {

    private Image backgroundImage;

    public MenuPrincipalPanel() {

        // 1. Arka plan fotoğrafını yükleme
        String cheminImage = System.getProperty("user.dir") + "/images/pic1519530.jpg"; // chemin absolu de l'image
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

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
            System.out.println("Bouton Jouer cliqué");
            String[] options = {"Humain vs Humain", "Humain vs IA", "IA vs IA"}; // options du jeu
            int choix = JOptionPane.showOptionDialog(
                MenuPrincipalPanel.this,
                "Sélectionnez un mode de jeu:",
                "Mode de Jeu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // par défaut
            );

            if (choix != JOptionPane.CLOSED_OPTION) {
                GameMode selectedMode = null;
            
                switch(choix) {
                    case 0: // Humain vs Humain
                        selectedMode = GameMode.HUMAN_VS_HUMAN;
                        break;
                    case 1: // Humain vs IA
                        selectedMode = GameMode.HUMAN_VS_IA;
                        break;
                    case 2: // IA vs IA
                        selectedMode = GameMode.IA_VS_IA;
                        break;
                    default:
                        return;
                }

                System.out.println("Mode séléctionné: " + selectedMode);
            }
        });

        reglesButton.addActionListener(e -> {
            System.out.println("Bouton Règles cliqué");
        });

        quitterButton.addActionListener(e -> {
            System.exit(0);
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