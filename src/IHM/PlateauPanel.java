package IHM;

import javax.swing.*;
import java.awt.*;

import model.GameState;
import model.Tile;
import model.GameCharacter;

public class PlateauPanel extends JPanel{

    private GameState gameState;

    public PlateauPanel(GameState gameState){
        this.gameState = gameState;

        setPreferredSize(new Dimension(600,600));

        setLayout(new GridLayout(3,3,10,10));//3x3
        setBackground(new Color(40,40,40));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        afficherPlateau();
    }

    private void afficherPlateau() {
    Tile[][] board = gameState.getBoard().getBoardForUI();

    for (int li = 0; li < 3; li++) {
        for (int ci = 0; ci < 3; ci++) {
            Tile tile = board[li][ci];
            GameCharacter character = tile.getCharacter();
            String nom = character.getName();

            JPanel cartePanel = new JPanel(new BorderLayout());
            cartePanel.setBackground(new Color(245, 235, 210));
            cartePanel.setBorder(BorderFactory.createLineBorder(new Color(80, 55, 35), 3));
            cartePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel nomLabel = new JLabel(nom, SwingConstants.CENTER);
            nomLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nomLabel.setForeground(new Color(40, 30, 20));

            cartePanel.add(nomLabel, BorderLayout.CENTER);

            final int l = li;
            final int c = ci;

            cartePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    System.out.println("Carte cliquée : " + nom + " position (" + l + "," + c + ")");
                }
            });

            add(cartePanel);
        }
    }
}
}

