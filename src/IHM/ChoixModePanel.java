package IHM;

import javax.swing.*;
import java.awt.*;

public class ChoixModePanel extends JPanel {
    public ChoixModePanel(FenetrePrincipale fenetre) {
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Choix du mode de jeu", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JButton b1 = new JButton("Humain vs Humain");
        JButton b2 = new JButton("Humain vs IA");
        JButton b3 = new JButton("IA vs IA");
        JButton retour = new JButton("Retour");
        panel.add(b1); panel.add(b2); panel.add(b3); panel.add(retour);

        add(titre, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        b1.addActionListener(e -> fenetre.lancerJeu(GameMode.HUMAN_VS_HUMAN));
        b2.addActionListener(e -> fenetre.lancerJeu(GameMode.HUMAN_VS_IA));
        b3.addActionListener(e -> fenetre.lancerJeu(GameMode.IA_VS_IA));
        retour.addActionListener(e -> fenetre.afficherMenu());
    }
}
