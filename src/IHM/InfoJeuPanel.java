package IHM;

import engine.GameEngine;
import java.awt.*;
import javax.swing.*;

public class InfoJeuPanel extends JPanel {

    private GameEngine gameEngine;
    private GameMode mode;
    private String joueur1Role;

    private JLabel modeLabel;
    private JLabel joueur1RoleLabel;
    private JLabel joueur2RoleLabel;
    private JLabel joueurActuelLabel;
    private JLabel roundLabel;
    private JLabel actionLabel;
    private JLabel selectedActionLabel;
    private JLabel roundOverLabel;
    private JLabel holmesLabel;
    private JLabel watsonLabel;
    private JLabel tobyLabel;
    private JPanel personnagesPanel;

    //bu constructor 3 tane bilgi kaynağı alıyor biz de bunları yazıyoruz
    public InfoJeuPanel(GameEngine gameEngine, GameMode mode, String joueur1Role) {
        this.gameEngine = gameEngine;
        this.mode = mode;
        this.joueur1Role = joueur1Role;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));// alt alta yazma 
        //setPreferredSize(new Dimension(260, 0)); //boyut
        setBackground(new Color(25, 25, 25));//arka plan koyu
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));//iç boşluk veriyor

        JLabel titre = new JLabel("Info jeu");
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setForeground(new Color(245, 235, 210));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);//sola hizalı

        modeLabel = creerLabel();
        joueur1RoleLabel = creerLabel();
        joueur2RoleLabel = creerLabel();
        joueurActuelLabel = creerLabel();
        roundLabel = creerLabel();
        actionLabel = creerLabel();
        selectedActionLabel = creerLabel();
        roundOverLabel = creerLabel();
        holmesLabel = creerLabel();
        watsonLabel = creerLabel();
        tobyLabel = creerLabel();
        
        personnagesPanel = new JPanel();
        personnagesPanel.setOpaque(false);
        personnagesPanel.setLayout(new BoxLayout(personnagesPanel, BoxLayout.Y_AXIS));
        personnagesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //panele ekleme 
        add(titre);
        add(Box.createVerticalStrut(15));
        add(joueur1RoleLabel);
        add(Box.createVerticalStrut(5));
        add(joueur2RoleLabel);
        add(Box.createVerticalStrut(5));
        add(modeLabel);
        add(Box.createVerticalStrut(5));
        add(joueurActuelLabel);
        add(Box.createVerticalStrut(5));
        add(roundLabel);
        add(Box.createVerticalStrut(5));
        add(actionLabel);
        add(Box.createVerticalStrut(5));
        add(selectedActionLabel);
        add(Box.createVerticalStrut(5));
        add(roundOverLabel);
        add(Box.createVerticalStrut(5));
        add(holmesLabel);
        add(Box.createVerticalStrut(5)); 
        add(watsonLabel);
        add(Box.createVerticalStrut(5));
        add(tobyLabel);
        add(Box.createVerticalStrut(15));
        
        JLabel personnagesTitre = new JLabel("Personnages");
        personnagesTitre.setFont(new Font("Arial", Font.BOLD, 15));
        personnagesTitre.setForeground(new Color(245, 235, 210));
        personnagesTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(personnagesTitre);
        add(Box.createVerticalStrut(12));
        add(personnagesPanel);

        rafraichir();
    }

    private JLabel creerLabel() {// her bilgi için ayarlamalar 
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(new Color(245, 235, 210));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public void rafraichir() {// yazıları güncelleme (coeur)
        modeLabel.setText("Mode : " + mode);//ecriture du mode 
        joueur1RoleLabel.setText("Joueur 1 : " + joueur1Role);

        joueur2RoleLabel.setText("Joueur 2 : " + gameEngine.getGameState().getPlayer2Role());

        joueurActuelLabel.setText("Joueur actuel : " + gameEngine.getCurrentPlayer());//info qu on prend d'engine pour ecrire
        roundLabel.setText("Round : " + gameEngine.getRoundNumber());
        actionLabel.setText("Action : " + gameEngine.getCurrentActionIndex());

        try {
            selectedActionLabel.setText("Action choisie : " + gameEngine.getSelectedActionType());
        } catch (IllegalStateException e) {
            selectedActionLabel.setText("Action choisie : aucune");
        }

        roundOverLabel.setText("Round terminé : " + gameEngine.isRoundOver());//de meme
        holmesLabel.setText("Holmes position : " + gameEngine.getGameState().getDetectiveTokens().getHolmes().getPosition());
        watsonLabel.setText("Watson position : " + gameEngine.getGameState().getDetectiveTokens().getWatson().getPosition());
        tobyLabel.setText("Toby position : " + gameEngine.getGameState().getDetectiveTokens().getToby().getPosition());
        rafraichirPersonnages();
    }
    private void rafraichirPersonnages() {
        personnagesPanel.removeAll();

        for (model.GameCharacter character : gameEngine.getGameState().getCharacters()) {
            JPanel ligne = creerLignePersonnage(character);
            personnagesPanel.add(ligne);
            personnagesPanel.add(Box.createVerticalStrut(6));
        }

        personnagesPanel.revalidate();
        personnagesPanel.repaint();
    }

    private JPanel creerLignePersonnage(model.GameCharacter character) {
        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setOpaque(false);
        //ligne.setMaximumSize(new Dimension(220, 24));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomLabel = new JLabel(character.getName());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 13));

        if (character.isEliminated()) {
            nomLabel.setForeground(new Color(120, 120, 120));
            nomLabel.setText(character.getName() + " éliminé");
        } else {
            nomLabel.setForeground(couleurDepuisModel(character.getColor()));
        }

        ligne.add(nomLabel, BorderLayout.CENTER);

        return ligne;
    }

    private Color couleurDepuisModel(Object couleur) {
        if (couleur == null) {
            return new Color(180, 180, 180);
        }

        String c = couleur.toString().toLowerCase();

        if (c.contains("red") || c.contains("rouge")) {
            return new Color(180, 45, 45);
        }

        if (c.contains("blue")) {
            return new Color(40, 110, 220);
        }

        if (c.contains("green")) {
            return new Color(90, 100, 65);
        }

        if (c.contains("yellow")) {
            return new Color(220, 185, 45);
        }

        if (c.contains("purple")) {
            return new Color(140, 80, 180);
        }

        if (c.contains("grey")) {
            return new Color(170, 170, 170);
        }

        if (c.contains("black")) {
            return new Color(210, 210, 210);
        }

        if (c.contains("orange")) {
            return new Color(220, 120, 35);
        }

        if (c.contains("brown") || c.contains("marron")) {
            return new Color(120, 75, 40);
        }

        return new Color(160, 160, 160);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(260, 600);
    }
}