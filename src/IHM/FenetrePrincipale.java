package IHM;

import ai.AIDifficulty;
import engine.GameEngine;
import java.awt.*;
import javax.swing.*;
import model.GameState;
import reseau.GameClient;
import reseau.GameServer;
import reseau.Message;

public class FenetrePrincipale extends JFrame {

    private MainMusique musique;
    private boolean musiqueActive = false;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JeuPanel activeJeuPanel;
    private GameClient networkClient;

    public FenetrePrincipale() {
        setTitle("Mr Jack Pocket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        Color popupBackground = new Color(45, 45, 45);
        Color popupText = new Color(245, 235, 210);
        Color popupButton = new Color(70, 70, 70);

        UIManager.put("OptionPane.background", popupBackground);
        UIManager.put("Panel.background", popupBackground);

        UIManager.put("OptionPane.messageForeground", popupText);
        UIManager.put("Label.foreground", popupText);

        UIManager.put("Button.background", popupButton);
        UIManager.put("Button.foreground", popupText);

        musique = new MainMusique();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new MenuPrincipalPanel(this), "menu principale");
        mainPanel.add(new ChoixModePanel(this), "choixMode");
        mainPanel.add(new MultijoueurPanel(this), "multijoueur");
        mainPanel.add(new ReglesPanel(this), "regles");

        add(mainPanel, BorderLayout.CENTER);
        afficherMenu();
    }

    public void afficherMenu() {
        cardLayout.show(mainPanel, "menu principale");
    }

    public void afficherChoixMode() {
        cardLayout.show(mainPanel, "choixMode");
    }

    public void afficherMultijoueur() {
        cardLayout.show(mainPanel, "multijoueur");
    }

    public void afficherRegles() {
        cardLayout.show(mainPanel, "regles");
    }

    public void lancerJeu(GameMode mode, String player1Role) {
        lancerJeu(mode, player1Role, AIDifficulty.HARD, AIDifficulty.HARD);
    }

    public void lancerJeu(GameMode mode, String player1Role, AIDifficulty difficulty) {
        lancerJeu(mode, player1Role, difficulty, difficulty);
    }

    public void lancerJeu(
            GameMode mode,
            String player1Role,
            AIDifficulty investigatorDifficulty,
            AIDifficulty jackDifficulty
    ) {
        System.out.println("Mode sélectionné: " + mode);
        System.out.println("Rôle Joueur 1 : " + player1Role);
        System.out.println("Difficulté IA Investigator : " + investigatorDifficulty);
        System.out.println("Difficulté IA Jack : " + jackDifficulty);

        JeuPanel jeuPanel = new JeuPanel(
                mode,
                player1Role,
                this,
                investigatorDifficulty,
                jackDifficulty
        );

        activeJeuPanel = jeuPanel;

        mainPanel.add(jeuPanel, "jeu");
        cardLayout.show(mainPanel, "jeu");

        revalidate();
        repaint();
    }

    public void toggleMusique() {
        if (musiqueActive) {
            musique.arreterMusique();
            musiqueActive = false;
        } else {
            musique = new MainMusique();
            musique.jouerMusique("assets/sounds/menu.wav");
            musiqueActive = true;
        }
    }

    public boolean estMusiqueActivee() {
        return musiqueActive;
    }

    public void changerVolumeMusique(int volume) {
        if (musique != null) {
            musique.changerVolume(volume);
        }
    }

    public void chargerJeuDepuisMenu() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Charger une partie");

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Fichiers sauvegarde (*.sav)",
                        "sav"
                )
        );

        int result = fileChooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File selectedFile = fileChooser.getSelectedFile();
        String path = selectedFile.getAbsolutePath();

        GameEngine loadedEngine = GameEngine.loadGame(path);

        if (loadedEngine == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Impossible de charger la partie.",
                    "Erreur chargement",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String player1Role = loadedEngine.getGameState().getPlayer1Role();

        JeuPanel jeuPanel = new JeuPanel(
                GameMode.HUMAN_VS_HUMAN,
                player1Role,
                this,
                loadedEngine
        );

        activeJeuPanel = jeuPanel;

        mainPanel.add(jeuPanel, "jeu");
        cardLayout.show(mainPanel, "jeu");

        revalidate();
        repaint();
    }

    public void demarrerReseauHost(String roleChoisi) {
        new Thread(() -> {
            GameServer gameServer = new GameServer(8080);
            gameServer.start();
        }).start();

        Timer timer = new Timer(800, e -> demarrerReseauClient("localhost", roleChoisi));
        timer.setRepeats(false);
        timer.start();
    }

    public void demarrerReseauClient(String ipAddress) {
        demarrerReseauClient(ipAddress, "Jack");
    }

    public void demarrerReseauClient(String ipAddress, String role) {
        new Thread(() -> {
            GameClient createdClient = new GameClient(ipAddress, 8080, role, msg -> {
                if (msg.getType() == Message.MessageType.UPDATE_STATE) {
                    GameState state = (GameState) msg.getGameStatePayload();
                    handleNetworkUpdate(state, role, msg);
                }
            });

            this.networkClient = createdClient;
        }).start();
    }

    private void handleNetworkUpdate(GameState state, String role, Message msg) {
        if (state == null) {
            return;
        }

        if (activeJeuPanel == null) {
            GameEngine proxyEngine = new GameEngine(state);
            proxyEngine.setGameState(state);

            activeJeuPanel = new JeuPanel(
                    GameMode.HUMAN_NETWORK,
                    role,
                    this,
                    proxyEngine
            );

            mainPanel.add(activeJeuPanel, "jeu");
            cardLayout.show(mainPanel, "jeu");

        } else {
            activeJeuPanel.appliquerEtatReseau(state);
        }

        if (activeJeuPanel != null && msg != null && msg.hasHighlight()) {
            activeJeuPanel.appliquerHighlightReseau(msg);
        }

        revalidate();
        repaint();

        fermerDialogsOuverts();
    }

    private void fermerDialogsOuverts() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog && window.isVisible()) {
                window.dispose();
            }
        }
    }

    public GameClient getNetworkClient() {
        return networkClient;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}