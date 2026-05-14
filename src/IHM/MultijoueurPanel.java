package IHM;

import java.awt.*;
import java.util.prefs.Preferences;
import javax.swing.*;

public class MultijoueurPanel extends JPanel {

    private Image backgroundImage;
    private BoutonClickMusique boutonClickMusique;
    private FenetrePrincipale fenetre;
    private Preferences prefs;

    public MultijoueurPanel(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;
        this.boutonClickMusique = new BoutonClickMusique();
        // Java'nın hafıza (çerez) yöneticisi
        this.prefs = Preferences.userNodeForPackage(MultijoueurPanel.class);

        String cheminImage = System.getProperty("user.dir") + "/assets/images/deneme2.png";
        backgroundImage = new ImageIcon(cheminImage).getImage();

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titre = new JLabel("Mode Multijoueur", SwingConstants.LEFT);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(new Color(245, 235, 210));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel panelBoutons = new JPanel();
        panelBoutons.setOpaque(false);
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));

        JButton creerButton = new JButton("Créer une partie (Host)");
        JButton rejoindreButton = new JButton("Rejoindre une partie (Client)");
        JButton retourButton = new JButton("Retour");

        BoutonTexte(creerButton);
        BoutonTexte(rejoindreButton);
        BoutonTexte(retourButton);

        creerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        rejoindreButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        retourButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBoutons.add(creerButton);
        panelBoutons.add(Box.createVerticalStrut(18));
        panelBoutons.add(rejoindreButton);
        panelBoutons.add(Box.createVerticalStrut(18));
        panelBoutons.add(retourButton);

        JPanel contenuPanel = new JPanel();
        contenuPanel.setOpaque(false);
        contenuPanel.setLayout(new BoxLayout(contenuPanel, BoxLayout.Y_AXIS));
        contenuPanel.setBorder(BorderFactory.createEmptyBorder(280, 90, 40, 0));

        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenuPanel.add(titre);
        contenuPanel.add(Box.createVerticalStrut(55));
        contenuPanel.add(panelBoutons);

        add(contenuPanel, BorderLayout.CENTER);

        retourButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            fenetre.afficherMenu();
        });

        creerButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherChoixRoleHost();
        });

        rejoindreButton.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            afficherDialogConnexion();
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

    private void afficherChoixRoleHost() {
        JDialog dialog = new JDialog(fenetre, "Créer une partie", true);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Choisissez votre rôle (Host)", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panelRoles = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 20));

        JLabel investigator = creerRoleIcone("Investigator", "assets/images/characters/investigator.png");
        JLabel jack = creerRoleIcone("Jack", "assets/images/characters/jack.png");

        investigator.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();
                fenetre.demarrerReseauHost("Investigator");
            }
        });

        jack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();
                fenetre.demarrerReseauHost("Jack");
            }
        });

        panelRoles.add(investigator);
        panelRoles.add(jack);

        dialog.add(titre, BorderLayout.NORTH);
        dialog.add(panelRoles, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void afficherDialogConnexion() {
        JDialog dialog = new JDialog(fenetre, "Rejoindre une partie", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(35, 35, 35));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel ipLabel = new JLabel("Adresse IP du Host :");
        ipLabel.setForeground(new Color(245, 235, 210));
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hafızadan son girilen IP'yi çekiyoruz (Yoksa localhost yazar)
        String lastIp = prefs.get("last_ip", "localhost");
        JTextField ipField = new JTextField(lastIp);
        ipField.setMaximumSize(new Dimension(200, 30));
        ipField.setHorizontalAlignment(JTextField.CENTER);

        JLabel portLabel = new JLabel("Port (Fixe) :");
        portLabel.setForeground(new Color(245, 235, 210));
        portLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField portField = new JTextField("8080");
        portField.setMaximumSize(new Dimension(100, 30));
        portField.setHorizontalAlignment(JTextField.CENTER);
        portField.setEditable(false); // Port değiştirilemez!
        portField.setBackground(new Color(100, 100, 100));

        formPanel.add(ipLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(ipField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(portLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(portField);

        JButton connectBtn = new JButton("Connexion");
        connectBtn.setFocusPainted(false);
        connectBtn.setBackground(new Color(55, 45, 35));
        connectBtn.setForeground(new Color(245, 235, 210));

        connectBtn.addActionListener(e -> {
            boutonClickMusique.jouerClick();
            String ip = ipField.getText().trim();
            if (!ip.isEmpty()) {
                // Başarılı girişte IP'yi hafızaya kaydet
                prefs.put("last_ip", ip);
                dialog.dispose();
                fenetre.demarrerReseauClient(ip);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(connectBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel creerRoleIcone(String texte, String cheminImage) {
        ImageIcon icon = new ImageIcon(cheminImage);
        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel label = new JLabel(texte, new ImageIcon(image), SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}