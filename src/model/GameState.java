package model;

import java.util.List;

public class GameState {
    private List<GameCharacter> characters;

    private AreaSet areaSet;
    private Board board;

    private AlibiDeckManager alibiDeckManager;
    private LineOfSightService lineOfSightService;
    private WitnessManager witnessManager;

    private DetectiveTokens detectiveTokens;
    private ActionTokens actionTokens;
    private TimeTokens timeTokens;

    private GameCharacter jackCharacter;

    private String currentPlayer;
    private int currentTurn;
    private boolean gameStarted;

    public GameState() {
        this.gameStarted = false;
        this.currentTurn = 1;
        this.currentPlayer = "Investigator";
    }

    public void setupGame() {
        this.characters = CharacterFactory.createCharacters();

        this.alibiDeckManager = new AlibiDeckManager(characters);
        this.jackCharacter = chooseJackIdentityFromAlibiDeck();

        this.areaSet = new AreaSet(characters);

        this.board = new Board();
        this.board.setupRandomBoard(areaSet.getAllAreas());

        this.detectiveTokens = new DetectiveTokens();
        this.actionTokens = new ActionTokens();
        this.timeTokens = new TimeTokens();

        this.lineOfSightService = new LineOfSightService();
        this.witnessManager = new WitnessManager();

        this.actionTokens.lancer();
        this.lineOfSightService.updateVisibility(board, detectiveTokens, characters);

        this.gameStarted = true;
    }

    private GameCharacter chooseJackIdentityFromAlibiDeck() {
        AlibiCards jackIdentityCard = alibiDeckManager.mrJackDraws(true);

        if (jackIdentityCard == null) {
            throw new IllegalStateException("Cannot choose Mr. Jack identity. Alibi deck is empty.");
        }

        GameCharacter jack = jackIdentityCard.getCharacter();
        jack.setJack(true);

        return jack;
    }

    public void nextTurn() {
        currentTurn++;

        if (currentPlayer.equals("Investigator")) {
            currentPlayer = "Jack";
        } else {
            currentPlayer = "Investigator";
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public AreaSet getAreaSet() {
        return areaSet;
    }

    public Board getBoard() {
        return board;
    }

    public AlibiDeckManager getAlibiDeckManager() {
        return alibiDeckManager;
    }

    public DetectiveTokens getDetectiveTokens() {
        return detectiveTokens;
    }

    public ActionTokens getActionTokens() {
        return actionTokens;
    }

    public TimeTokens getTimeTokens() {
        return timeTokens;
    }

    public GameCharacter getJackCharacter() {
        return jackCharacter;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
    public void updateVisibility() {
        lineOfSightService.updateVisibility(board, detectiveTokens, characters);
    }

    public LineOfSightService getLineOfSightService() {
        return lineOfSightService;
    }

    public WitnessManager getWitnessManager() {
        return witnessManager;
    }
    public List<GameCharacter> applyWitnessPhase() {
        updateVisibility();

        return witnessManager.eliminateCharactersByWitness(
                characters,
                jackCharacter
        );
    }
    
}