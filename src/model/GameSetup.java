package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameSetup {

    private final Random random = new Random();

    public GameState setupGame() {
        Board board = createRandomBoard();

        randomizeTileOrientations(board);
        placeDetectives(board);

        GameCharacter mrJackIdentity = chooseMrJackIdentity();

        List<AlibiCard> alibiDeck = prepareAlibiDeck();
        List<TurnToken> turnTokens = prepareTurnTokens();

        Player firstPlayer = chooseFirstPlayer();

        return new GameState(
                board,
                mrJackIdentity,
                alibiDeck,
                turnTokens,
                firstPlayer
        );
    }

    private Board createRandomBoard() {
        List<Tile> tiles = createAllTiles();
        Collections.shuffle(tiles);

        return new Board(tiles);
    }

    private void randomizeTileOrientations(Board board) {
        for (Tile tile : board.getTiles()) {
            int orientation = random.nextInt(4);
            tile.setOrientation(orientation);
        }
    }

    private void placeDetectives(Board board) {
        Detective sherlock = new Detective("Sherlock");
        Detective watson = new Detective("Watson");
        Detective toby = new Detective("Toby");

        board.placeDetective(sherlock, 0);
        board.placeDetective(watson, 3);
        board.placeDetective(toby, 6);
    }

    private GameCharacter chooseMrJackIdentity() {
        List<GameCharacter> characters = createAllCharacters();
        Collections.shuffle(characters);

        return characters.get(0);
    }

    private List<AlibiCard> prepareAlibiDeck() {
        List<AlibiCard> deck = createAllAlibiCards();
        Collections.shuffle(deck);

        return deck;
    }

    private List<TurnToken> prepareTurnTokens() {
        List<TurnToken> tokens = createAllTurnTokens();
        Collections.shuffle(tokens);

        return tokens;
    }

    private Player chooseFirstPlayer() {
        if (random.nextBoolean()) {
            return Player.DETECTIVE;
        } else {
            return Player.MR_JACK;
        }
    }

    private List<Tile> createAllTiles() {
        List<Tile> tiles = new ArrayList<>();

        // Şimdilik örnek.
        // Gerçek tile constructor'ına göre değiştireceğiz.
        tiles.add(new Tile("Tile 1"));
        tiles.add(new Tile("Tile 2"));
        tiles.add(new Tile("Tile 3"));
        tiles.add(new Tile("Tile 4"));
        tiles.add(new Tile("Tile 5"));
        tiles.add(new Tile("Tile 6"));
        tiles.add(new Tile("Tile 7"));
        tiles.add(new Tile("Tile 8"));
        tiles.add(new Tile("Tile 9"));

        return tiles;
    }

    private List<GameCharacter> createAllCharacters() {
        List<GameCharacter> characters = new ArrayList<>();

        characters.add(new GameCharacter("Sherlock"));
        characters.add(new GameCharacter("Watson"));
        characters.add(new GameCharacter("Toby"));
        characters.add(new GameCharacter("Lestrade"));
        characters.add(new GameCharacter("Goodley"));
        characters.add(new GameCharacter("Bert"));
        characters.add(new GameCharacter("Smith"));
        characters.add(new GameCharacter("Stealthy"));

        return characters;
    }

    private List<AlibiCard> createAllAlibiCards() {
        List<AlibiCard> cards = new ArrayList<>();

        for (GameCharacter character : createAllCharacters()) {
            cards.add(new AlibiCard(character));
        }

        return cards;
    }

    private List<TurnToken> createAllTurnTokens() {
        List<TurnToken> tokens = new ArrayList<>();

        tokens.add(new TurnToken(1));
        tokens.add(new TurnToken(2));
        tokens.add(new TurnToken(3));
        tokens.add(new TurnToken(4));
        tokens.add(new TurnToken(5));
        tokens.add(new TurnToken(6));
        tokens.add(new TurnToken(7));
        tokens.add(new TurnToken(8));

        return tokens;
    }
}