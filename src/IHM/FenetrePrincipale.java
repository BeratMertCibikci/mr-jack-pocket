package IHM;

import java.awt.*;
import javax.swing.*;

public class FenetrePrincipale extends JFrame {

    public FenetrePrincipale() {
        setTitle("Mr Jack Pocket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        MenuPrincipalPanel menuPrincipalPanel = new MenuPrincipalPanel();

        add(menuPrincipalPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}