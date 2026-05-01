package IHM;

import javax.swing.*;
import java.awt.*;

public class ChoixModePanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    public ChoixModePanel(FenetrePrincipale fenetre) {

        // 1. Bouton click sesi
        boutonClickMusique = new BoutonClickMusique();

        // 2. Arka plan fotoğrafını yükleme
        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.jpg";
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        // 3. Ana panel düzeni
        setLayout(new BorderLayout());
        setOpaque(false);

        // 4. Başlık
        JLabel titre = new JLabel("Choix du mode de jeu", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(new Color(245, 235, 210));
        titre.setOpaque(false);
        titre.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // 5. Butonları tutan panel
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Değerler: üst, sol, alt, sağ
        // Sol değeri artırırsan yazılar sağa gider, azaltırsan sola gider.
        panel.setBorder(BorderFactory.createEmptyBorder(250, 90, 40, 0));

        // 6. Butonlar
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

        // 7. Ekrana ekleme
        add(titre, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // 8. Buton aksiyonları
        humainVsHumainButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.HUMAN_VS_HUMAN);
        });

        humainVsIAButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.HUMAN_VS_IA);
        });

        iaVsIAButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.lancerJeu(GameMode.IA_VS_IA);
        });

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}