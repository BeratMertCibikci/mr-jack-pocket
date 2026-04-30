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

        Character mrJackIdentity = chooseMrJackIdentity();

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

    private Character chooseMrJackIdentity() {
        List<Character> characters = createAllCharacters();
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

    private List<Character> createAllCharacters() {
        List<Character> characters = new ArrayList<>();

        characters.add(new Character("Sherlock"));
        characters.add(new Character("Watson"));
        characters.add(new Character("Toby"));
        characters.add(new Character("Lestrade"));
        characters.add(new Character("Goodley"));
        characters.add(new Character("Bert"));
        characters.add(new Character("Smith"));
        characters.add(new Character("Stealthy"));

        return characters;
    }

    private List<AlibiCard> createAllAlibiCards() {
        List<AlibiCard> cards = new ArrayList<>();

        for (Character character : createAllCharacters()) {
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