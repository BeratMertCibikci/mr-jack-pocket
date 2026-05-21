package IHM;

import ai.AIDifficulty;
import engine.GameEngine;
import java.awt.*;
import javax.swing.*;
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
        //setSize(900, 600);

        musique = new MainMusique();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new MenuPrincipalPanel(this), "menu principale");
        mainPanel.add(new ChoixModePanel(this), "choixMode");
        mainPanel.add(new MultijoueurPanel(this), "multijoueur");
        mainPanel.add(new ReglesPanel(this), "regles");

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(900, 700));
        setLocationRelativeTo(null);

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
        String filename = JOptionPane.showInputDialog(
                this,
                "Nom du fichier à charger :",
                "Charger une partie",
                JOptionPane.QUESTION_MESSAGE
        );

        if (filename == null) {
            return;
        }

        filename = filename.trim();

        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nom de fichier invalide.",
                    "Erreur chargement",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        GameEngine loadedEngine = GameEngine.loadGame(filename);

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

        mainPanel.add(jeuPanel, "jeu");
        cardLayout.show(mainPanel, "jeu");

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
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
            demarrerReseauClient(ipAddress, "Client");
        }

    public void demarrerReseauClient(String ipAddress, String role) {
        new Thread(() -> {
            this.networkClient = new GameClient(ipAddress, 8080, role, msg -> {
                if (msg.getType() == Message.MessageType.UPDATE_STATE) {
                    model.GameState state = (model.GameState) msg.getGameStatePayload();
                    handleNetworkUpdate(state, role);
                }
            });
        }).start();
    }

    private void handleNetworkUpdate(model.GameState state, String role) {
        SwingUtilities.invokeLater(() -> {
            if (activeJeuPanel == null) {
                engine.GameEngine proxyEngine = new engine.GameEngine(state.getPlayer1Role());
                
                activeJeuPanel = new JeuPanel(GameMode.HUMAN_VS_HUMAN, role, this, proxyEngine);
                mainPanel.add(activeJeuPanel, "jeu");
                cardLayout.show(mainPanel, "jeu");
            } else {
                
                activeJeuPanel.getGameEngine().setGameState(state);
                activeJeuPanel.rafraichirToutesLesVues(); 
            }
            revalidate();
            repaint();

            
            
            for (Window window : Window.getWindows()) {
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
        });
        
    }
}
