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
        contenuPanel.setBorder(BorderFactory.createEmptyBorder(280, 140, 40, 0));

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

JLabel investigator = creerRoleIcone(
        "Investigator",
        assetPath("assets/images/characters/investigator_new.png")
);

JLabel jack = creerRoleIcone(
        "Jack",
        assetPath("assets/images/characters/jack_new.png")
);

        investigator.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();
                fenetre.demarrerReseauHost("Investigator");
                afficherAttenteJoueur(); 
            }
        });

        jack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boutonClickMusique.jouerClick();
                dialog.dispose();
                fenetre.demarrerReseauHost("Jack");
                afficherAttenteJoueur(); 
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
        dialog.setSize(380, 360);
        dialog.setLocationRelativeTo(fenetre);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(35, 35, 35));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JLabel ipLabel = new JLabel("Adresse IP du Host :");
        ipLabel.setForeground(new Color(245, 235, 210));
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        portField.setEditable(false);
        portField.setBackground(new Color(100, 100, 100));

        JLabel roleLabel = new JLabel("Votre rôle :");
        roleLabel.setForeground(new Color(245, 235, 210));
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(280, 45));

        JRadioButton rbInvestigator = new JRadioButton("Investigator");
        JRadioButton rbJack = new JRadioButton("Jack");

        rbInvestigator.setForeground(new Color(245, 235, 210));
        rbInvestigator.setBackground(new Color(35, 35, 35));
        rbInvestigator.setFocusPainted(false);

        rbJack.setForeground(new Color(245, 235, 210));
        rbJack.setBackground(new Color(35, 35, 35));
        rbJack.setFocusPainted(false);

        String lastRole = prefs.get("last_client_role", "Investigator");

        if ("Jack".equals(lastRole)) {
            rbJack.setSelected(true);
        } else {
            rbInvestigator.setSelected(true);
        }

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(rbInvestigator);
        roleGroup.add(rbJack);

        rolePanel.add(rbInvestigator);
        rolePanel.add(rbJack);

        JLabel warnLabel = new JLabel("⚠ Votre rôle doit être différent de celui du Host");
        warnLabel.setForeground(new Color(212, 175, 55));
        warnLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        warnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(ipLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(ipField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(portLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(portField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(rolePanel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(warnLabel);

        JButton connectBtn = new JButton("Connexion");
        connectBtn.setFocusPainted(false);
        connectBtn.setBackground(new Color(55, 45, 35));
        connectBtn.setForeground(new Color(245, 235, 210));
        connectBtn.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 1));

        connectBtn.addActionListener(e -> {
            boutonClickMusique.jouerClick();

            String ip = ipField.getText().trim();
            String role = rbJack.isSelected() ? "Jack" : "Investigator";

            if (!ip.isEmpty()) {
                prefs.put("last_ip", ip);
                prefs.put("last_client_role", role);

                dialog.dispose();
                fenetre.demarrerReseauClient(ip, role);
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

    private String getLocalIP() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface iface = interfaces.nextElement();
                
                
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                java.util.Enumeration<java.net.InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress addr = addresses.nextElement();
                    
                    
                    if (addr instanceof java.net.Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur IP: " + e.getMessage());
        }
        return "127.0.0.1";
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
private void afficherAttenteJoueur() {
    JDialog waitDialog = new JDialog(fenetre, "Attente", false);
    waitDialog.setSize(400, 200);
    waitDialog.setLocationRelativeTo(fenetre);
    
    JPanel p = new JPanel(new GridLayout(3, 1));
    p.setBackground(new Color(25, 25, 25));
    
    JLabel l1 = new JLabel("En attente d'un adversaire...", SwingConstants.CENTER);
    l1.setForeground(Color.WHITE);
    
    JLabel l2 = new JLabel("Votre adresse IP : " + getLocalIP(), SwingConstants.CENTER);
    l2.setForeground(new Color(212, 175, 55));
    l2.setFont(new Font("Arial", Font.BOLD, 18));
    
    JLabel l3 = new JLabel("Port : 8080", SwingConstants.CENTER);
    l3.setForeground(Color.GRAY);
    
    p.add(l1);
    p.add(l2);
    p.add(l3);
    
    waitDialog.add(p);
    waitDialog.setVisible(true); 
}
private String assetPath(String path) {
    if (path.startsWith("/")) {
        path = path.substring(1);
    }

    return System.getProperty("user.dir") + "/" + path;
}
}