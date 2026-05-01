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

    private TurnManager turnManager;
    private WinConditionChecker winConditionChecker;

    private GameCharacter jackCharacter;

    private boolean gameStarted;
    private boolean gameOver;
    private String winner;

    public GameState() {
        this.gameStarted = false;
        this.gameOver = false;
        this.winner = null;
    }

    public void setupGame() {
        this.characters = CharacterFactory.createCharacters();

        this.alibiDeckManager = new AlibiDeckManager(characters);
        this.alibiDeckManager.shuffleCards();
        this.jackCharacter = chooseJackIdentityFromAlibiDeck();

        this.areaSet = new AreaSet(characters);

        this.board = new Board();
        this.board.setupRandomBoard(areaSet.getAllAreas());

        this.detectiveTokens = new DetectiveTokens();
        this.actionTokens = new ActionTokens();
        this.timeTokens = new TimeTokens();

        this.lineOfSightService = new LineOfSightService();
        this.witnessManager = new WitnessManager();

        this.turnManager = new TurnManager();
        this.winConditionChecker = new WinConditionChecker();

        this.actionTokens.lancer();
        this.updateVisibility();

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

    public void updateVisibility() {
        lineOfSightService.updateVisibility(board, detectiveTokens, characters);
    }

    public List<GameCharacter> applyWitnessPhase() {
        updateVisibility();

        return witnessManager.eliminateCharactersByWitness(
                characters,
                jackCharacter
        );
    }

    public void finishGame(String winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
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

    public LineOfSightService getLineOfSightService() {
        return lineOfSightService;
    }

    public WitnessManager getWitnessManager() {
        return witnessManager;
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

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public WinConditionChecker getWinConditionChecker() {
        return winConditionChecker;
    }

    public GameCharacter getJackCharacter() {
        return jackCharacter;
    }
}