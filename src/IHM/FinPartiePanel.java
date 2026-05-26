package IHM;

import javax.swing.*;
import java.awt.*;

public class FinPartiePanel extends JPanel {
    public FinPartiePanel(FenetrePrincipale fenetre, String gagnant, String jackNom, int suspectsRestants, int hourglasses) {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 15));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel titre = new JLabel("FIN DE PARTIE");
        titre.setFont(new Font("Serif", Font.BOLD, 48));
        titre.setForeground(new Color(212, 175, 55));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gagnantLabel = new JLabel("Gagnant: " + gagnant);
        gagnantLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gagnantLabel.setForeground(Color.WHITE);
        gagnantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jackLabel = new JLabel("Mr Jack était: " + jackNom);
        jackLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        jackLabel.setForeground(Color.LIGHT_GRAY);
        jackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel suspectsLabel = new JLabel("Suspects restants : " + suspectsRestants);
        suspectsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        suspectsLabel.setForeground(Color.LIGHT_GRAY);
        suspectsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hourglassLabel = new JLabel("Hourglasses de Jack : " + hourglasses);
        hourglassLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        hourglassLabel.setForeground(Color.LIGHT_GRAY);
        hourglassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton menuButton = creerBouton("Retour au menu");
        menuButton.addActionListener(e -> {
            fenetre.setContentPane(new MenuPrincipalPanel(fenetre));
            fenetre.revalidate();
            fenetre.repaint();
        });

        center.add(Box.createVerticalGlue());
        center.add(titre);
        center.add(Box.createVerticalStrut(35));
        center.add(gagnantLabel);
        center.add(Box.createVerticalStrut(25));
        center.add(jackLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(suspectsLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(hourglassLabel);
        center.add(Box.createVerticalStrut(35));
        center.add(menuButton);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);
    }

    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 18));
        bouton.setForeground(Color.BLACK);
        bouton.setBackground(new Color(212, 175, 55));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setMaximumSize(new Dimension(260, 50));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return bouton;
    }
}
