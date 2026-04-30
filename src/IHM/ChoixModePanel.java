package IHM;

import javax.swing.*;
import java.awt.*;

public class ChoixModePanel extends JPanel {

    private BoutonClickMusique boutonClickMusique;
    private Image backgroundImage;

    public ChoixModePanel(FenetrePrincipale fenetre) {

        boutonClickMusique = new BoutonClickMusique();

        String cheminImage = System.getProperty("user.dir") + "/images/deneme2.jpg";
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titre = new JLabel("Choix du mode de jeu", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setOpaque(false);
        titre.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JButton b1 = new JButton("Humain vs Humain");
        JButton b2 = new JButton("Humain vs IA");
        JButton b3 = new JButton("IA vs IA");
        JButton retour = new JButton("Retour");

        b1.setFont(new Font("Arial", Font.BOLD, 20));
        b2.setFont(new Font("Arial", Font.BOLD, 20));
        b3.setFont(new Font("Arial", Font.BOLD, 20));
        retour.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(retour);

        add(titre, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        b1.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.HUMAN_VS_HUMAN);
        });

        b2.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.HUMAN_VS_IA);
        });

        b3.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.IA_VS_IA);
        });

        retour.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}