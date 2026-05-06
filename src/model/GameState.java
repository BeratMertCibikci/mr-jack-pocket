package model;

import java.util.ArrayList;
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

    private String player1Role;
    private String player2Role;

    private PlayerState detectivePlayerState;
    private PlayerState jackPlayerState;
    private Token currentTurnToken;

    public GameState(String player1Role) {
        this.gameStarted = false;
        this.gameOver = false;
        this.winner = null;

        this.player1Role = player1Role;
        this.player2Role = player1Role.equals("Investigator") ? "Jack" : "Investigator";
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

        this.detectivePlayerState = new PlayerState(Player.DETECTIVE);
        this.jackPlayerState = new PlayerState(Player.MR_JACK);
        this.currentTurnToken = timeTokens.extraite();

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

        return witnessManager.resolveAppealForWitnesses(
                characters,
                jackCharacter,
                currentTurnToken,
                detectivePlayerState,
                jackPlayerState
        );
    }

    public void drawNextTurnToken() {
        this.currentTurnToken = timeTokens.extraite();
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

    public String getPlayer1Role() {
        return player1Role;
    }

    public String getPlayer2Role() {
        return player2Role;
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

    public PlayerState getDetectivePlayerState() {
        return detectivePlayerState;
    }

    public PlayerState getJackPlayerState() {
        return jackPlayerState;
    }

    public Token getCurrentTurnToken() {
        return currentTurnToken;
    }

    public GameState deepCopy(){
        GameState copy = new GameState(this.player1Role);

        copy.player2Role = this.player2Role;

        copy.gameStarted = this.gameStarted;
        copy.gameOver = this.gameOver;
        copy.winner = this.winner;

        // Character
        copy.characters = new ArrayList<>();
        for (GameCharacter c : this.characters){
            copy.characters.add(c.deepCopy());
        }

        // Board
        copy.board = this.board.deepCopy(copy.characters);

        for (Tile tile : copy.board.getAllTiles()){
            if (tile.getCharacter() != null){
                tile.getCharacter().setTile(tile);
            }
        }

        // AreaSet
        copy.areaSet = new AreaSet(copy.characters);

        // Tokens
        copy.actionTokens = this.actionTokens.deepCopy();
        copy.detectiveTokens = this.detectiveTokens.deepCopy();
        copy.timeTokens = this.timeTokens.deepCopy();

        // Turn
        copy.turnManager = this.turnManager.deepCopy();

        // Alibi
        copy.alibiDeckManager = this.alibiDeckManager.deepCopy(copy.characters);

        // Players
        copy.detectivePlayerState = this.detectivePlayerState.deepCopy();
        copy.jackPlayerState = this.jackPlayerState.deepCopy();

        // Jack
        copy.jackCharacter = findCharacterCopy(copy.characters, this.jackCharacter.getId());

        // Current Turn Token
        if (this.currentTurnToken != null){
            copy.currentTurnToken = this.currentTurnToken.deepCopy();
        }
        
        copy.lineOfSightService = new LineOfSightService();
        copy.witnessManager = new WitnessManager();
        copy.winConditionChecker = new WinConditionChecker();

        copy.relinkCharactersToBoardTiles();
        return copy;
    }
    private GameCharacter findCharacterCopy(List<GameCharacter> copies, int id){
        for (GameCharacter c : copies){
            if (c.getId() == id){
                return c;
            }
        }
        return null;
    }

    private void relinkCharactersToBoardTiles() {
        for (GameCharacter character : characters) {
            for (Tile tile : board.getAllTiles()) {
                if (tile.getCharacter() != null
                        && tile.getCharacter().getId() == character.getId()) {

                    tile.setCharacter(character);
                    character.setTile(tile);
                    break;
                }
            }
        }
    }
}