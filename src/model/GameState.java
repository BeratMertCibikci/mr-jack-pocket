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

    //private String currentPlayer;
    //private int currentTurn;
    private boolean gameStarted;
    private TurnManager turnManager;
    private WinConditionChecker winConditionChecker;
    private boolean gameOver;
    private String winner;
    private static final int MAX_ROUNDS = 8;

    public GameState() {
        this.gameStarted = false;
        //this.currentTurn = 1;
        //this.currentPlayer = "Investigator";
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

        this.actionTokens.lancer();
        this.lineOfSightService.updateVisibility(board, detectiveTokens, characters);
        this.winConditionChecker = new WinConditionChecker();
        this.gameStarted = true;
    }

    public Token playActionToken(int tokenIndex){
        if (!gameStarted){
            throw new IllegalStateException("Game has not started.");
        }
        if (gameOver){
            throw new IllegalStateException("Game is already over");
        }
        if (turnManager.isRoundOver()){
            throw new IllegalStateException("Round is already over");
        }                                                               // verifier que lq partie est valide
        Token selectedToken = actionTokens.selectToken(tokenIndex);     // selectionne une jeton d'action et marquer jeton comme utilise
        turnManager.nextActionTurn();                                   // passer au prochain tour

        return selectedToken;                                           // retoruner jeton selectionne
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

    /*
    public void nextTurn() {
        currentTurn++;

        if (currentPlayer.equals("Investigator")) {
            currentPlayer = "Jack";
        } else {
            currentPlayer = "Investigator";
        }
    }
    */

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
        return turnManager.getCurrentPlayer();
    }

    public int getCurrentTurn() {
        return turnManager.getRoundNumber();
    }
    public TurnManager getTurnManager() {
        return turnManager;
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
    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public WinConditionChecker getWinConditionChecker() {
        return winConditionChecker;
    }
    public void endRound() {
        if (!gameStarted) {
            throw new IllegalStateException("Game has not started.");
        }

        if (gameOver) {
            throw new IllegalStateException("Game is already over.");
        }

        if (!actionTokens.allTokensUsed()) {
            throw new IllegalStateException("Cannot end round before all action tokens are used.");
        }

        applyWitnessPhase();

        turnManager.startNextRound();

        if (winConditionChecker.hasJackWonByTime(turnManager.getRoundNumber(), MAX_ROUNDS)) {
            gameOver = true;
            winner = "Jack";
            return;
        }

        actionTokens.lancer();
        updateVisibility();
    }
    public void accuse(GameCharacter accusedCharacter) {
        if (!gameStarted) {
            throw new IllegalStateException("Game has not started.");
        }

        if (gameOver) {
            throw new IllegalStateException("Game is already over.");
        }

        if (winConditionChecker.hasInvestigatorWonByAccusation(accusedCharacter)) {
            gameOver = true;
            winner = "Investigator";
        } else if (winConditionChecker.hasJackWonByWrongAccusation(accusedCharacter)) {
            gameOver = true;
            winner = "Jack";
        }
    }

    public void moveDetective(String detectiveName, int steps){
        if (!gameStarted){
            throw new IllegalStateException("Game has not started.");
        }
        if (gameOver){
            throw new IllegalStateException("Game is already over.");
        }
        detectiveTokens.moveDetective(detectiveName, steps);
        updateVisibility();
    }

    public Token playDetectiveActionToken(int tokenIndex, int steps){
        Token token = playActionToken(tokenIndex);
        String action = token.getCurrentSide();

        if (!action.equals("Holmes") && !action.equals("Watson") && !action.equals("Toby")){
            throw new IllegalArgumentException("This token is not a detective movement token.");
        }
        moveDetective(action, steps);
        return token;
    }

    public AlibiCards playAlibiAction(String player){
        if (player.equals("Investigator")){
            GameCharacter eliminated = alibiDeckManager.investigatorDraws();

            if (eliminated != null){
                Tile tile = eliminated.getTile();

                if (tile != null){
                    tile.flipToEmptySide();
                }
            }
            return null;
        }else{
            return alibiDeckManager.mrJackDraws(false);
        }
    }

    public Token playAlibiToken(int tokenIndex){
        String playerBeforeAction = turnManager.getCurrentPlayer();

        Token token = playActionToken(tokenIndex);

        if (!token.getCurrentSide().equals("Alibi")){
            throw new IllegalArgumentException("Token is not an Alibi token.");
        }

        playAlibiAction(playerBeforeAction);
        return token;
    }
}