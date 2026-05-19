package IHM;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;

    private JPanel boutonsPanel;
    
    private JButton jouerButton;
    private JButton loadButton;
    private JButton reglesButton;
    private JButton quitterButton;
    private JButton muteButton;
    private JSlider volumeSlider;

    public MenuPrincipalPanel(FenetrePrincipale fenetre) {

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png"; // chemin absolu de l'image
        ImageIcon icon = new ImageIcon(cheminImage);
        backgroundImage = icon.getImage();

        boutonClickMusique = new BoutonClickMusique();

        setLayout(new BorderLayout());
        setOpaque(false);

        boutonsPanel = new JPanel();
        boutonsPanel.setOpaque(false); // pour qu'on ne bloque pas l'arriere-plan
        //boutonsPanel.setLayout(new GridLayout(3, 1, 15, 15)); // 3 lignes, 1 colonne, 15 pixels entre les boutons
        boutonsPanel.setLayout(new BoxLayout(boutonsPanel, BoxLayout.Y_AXIS));

        int top = Math.max(280, getHeight()/2);
        int left = Math.max(110, getWidth() / 10);
        boutonsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, 40, 0)); // le comptour des boutons sont vides

        jouerButton = new JButton("Jouer");
        loadButton = new JButton("Load Game");
        reglesButton = new JButton("Règles");
        quitterButton = new JButton("Quitter");
        muteButton = new JButton("Unmute");

        volumeSlider = new JSlider(0, 100, 40);
        volumeSlider.setOpaque(false);

        int largeurSlider = Math.max(140, getWidth() / 5);
        volumeSlider.setMaximumSize(new Dimension(largeurSlider, 40));
        volumeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));

        BoutonTexte(jouerButton);
        BoutonTexte(loadButton);
        BoutonTexte(reglesButton);
        BoutonTexte(quitterButton);
        BoutonTexte(muteButton); 

        jouerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        reglesButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        quitterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        muteButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        int espace = Math.max(8, getHeight() / 70);

        boutonsPanel.add(jouerButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));
        boutonsPanel.add(loadButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));
        boutonsPanel.add(reglesButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));
        boutonsPanel.add(muteButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));
        boutonsPanel.add(volumeSlider);
        boutonsPanel.add(Box.createVerticalStrut(espace));
        boutonsPanel.add(quitterButton);

        //add(titreLabel, BorderLayout.NORTH);
        add(boutonsPanel, BorderLayout.CENTER);

        jouerButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            System.out.println("Bouton Jouer cliqué");
            fenetre.afficherChoixMode();
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

        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            fenetre.changerVolumeMusique(volume);
        });

        mettreAJourResponsiveLayout();

        addComponentListener(new java.awt.event.ComponentAdapter(){
            @Override
            public void componentResized(java.awt.event.ComponentEvent e){
                SwingUtilities.invokeLater(() -> {
                    mettreAJourResponsiveLayout();
                });
            }
        });
    }

    private void BoutonTexte(JButton bouton) {
        //int tailleFont = Math.max(16, getHeight()/35);
        //bouton.setFont(new Font("Arial", Font.BOLD, tailleFont));
        bouton.setForeground(new Color(245, 235, 210));
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setMargin(new Insets(4, 8, 4, 8));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void mettreAJourResponsiveLayout(){
        int top = Math.min(350, Math.max(120, getHeight() / 3));
        int left = Math.min(180, Math.max(30, getWidth() / 10));

        boutonsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, 40, 0));
        int espace = Math.max(8, getHeight() / 70);

        boutonsPanel.removeAll();
        
        boutonsPanel.add(jouerButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));

        boutonsPanel.add(loadButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));

        boutonsPanel.add(reglesButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));

        boutonsPanel.add(muteButton);
        boutonsPanel.add(Box.createVerticalStrut(espace));

        boutonsPanel.add(volumeSlider);
        boutonsPanel.add(Box.createVerticalStrut(espace));

        boutonsPanel.add(quitterButton);

        int largeurSlider = Math.max(140, getWidth() / 5);
        int hauteurSlider = Math.max(30, getHeight()/20);

        volumeSlider.setMaximumSize(new Dimension(largeurSlider, hauteurSlider));
        volumeSlider.setPreferredSize(new Dimension(largeurSlider, hauteurSlider));

        int tailleFont = Math.max(16, getHeight() / 35);

        Font font = new Font("Arial", Font.BOLD, tailleFont);

        jouerButton.setFont(font);
        loadButton.setFont(font);
        reglesButton.setFont(font);
        quitterButton.setFont(font);
        muteButton.setFont(font);

        revalidate();
        repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // étale la photo sur le panneau
        }
    }
}