# Mr. Jack Pocket - Model Files Summary

This document summarizes the functions of the classes in the `model` package and the important methods they contain.

---

## 1. Tile.java
Manages an individual tile on the game board and its properties.
- **`getId()`, `getCharacter()`, `setCharacter()`**: Reads or assigns the tile's ID and the character on it.
- **`hasCharacter()`, `isSuspectSide()`, `isEmptySide()`**: Checks if there is a character on the tile and which side of the tile is face up.
- **`flipToEmptySide()`, `flipToSuspectSide()`**: Flips the tile to the empty side or the suspect side.
- **`isEliminated()`, `eliminate()`, `clearSuspect()`**: Checks if the suspect on it has been eliminated and applies elimination processes.
- **`isSuspectVisible()`, `canBeSeen()`**: Queries whether the suspect is currently visible.
- **`getOrientation()`, `setOrientation()`, `rotateClockwise()`, `rotate()`**: Sets the orientation of the tile and rotates it clockwise.
- **`hasRoad()`, `hasWall()`, `getCurrentRoads()`, `getSuspectSideRoads()`, `getEmptySideRoads()`**: Checks for roads or walls in specified directions and returns a list of open roads.
- **`getPosition()`, `getRow()`, `getCol()`, `setPosition()`, `setRow()`, `setCol()`**: Reads and modifies the row/column coordinates of the tile on the board.
- **`hasBarricade()`, `setBarricade()`, `blocksLineOfSight()`**: Checks if there is a barricade on the tile and if it blocks the line of sight.

## 2. Direction.java
An enum class specifying directions for tile roads.
- **`rotateClockwise()`**: Rotates the direction one turn clockwise (e.g., North becomes East).

## 3. Board.java
Controls the grid structure of the game board and tile placement.
- **`setupRandomBoard()`**: Randomly places tiles on the board by shuffling them.
- **`randomizeOrientation()`**: Randomly sets the orientation of a specified tile.
- **`getTile()`, `setTile()`**: Retrieves the tile at a specific row and column or assigns a new tile to that position.
- **`swapTiles()`**: Swaps the positions of two tiles on the board.
- **`getAllTiles()`, `getBoardForUI()`**: Returns a flat list of tiles or a matrix copy for the user interface.
- **`resetBoard()`**: Clears the board by resetting it.
- **`getSize()`**: Returns the size of the board (3x3).

## 4. AreaSet.java
Holds the initial configurations for the 9 different special area tiles.
- **`createDefaultAreas()`, `createAreaSameRoads()`, `createAreaDifferentRoads()`**: Generates the 9 basic tiles based on characters and road directions.
- **`roads()`**: Converts direction parameters into a Set.
- **`findCharacterByName()`**: Finds the relevant character object from a list by its name.
- **`getAllAreas()`, `getAreaByIndex()`, `getAreaById()`**: Returns the generated tiles by index, ID, or as a list.
- **`size()`**: Gives the total number of areas.
- **`remainingSuspects()`**: Calculates the number of characters still in suspect status (visible) on the board.

## 5. Token.java
Holds the general structure of double-sided tokens in the game.
- **`detectType()`**: Determines the type (Time or Action) by looking at the back side of the token.
- **`getId()`, `getName()`, `getType()`**: Retrieves basic identity and type information of the token.
- **`isDetectiveToken()`, `isActionToken()`, `isTimeToken()`**: Boolean checks to verify the type of the token.
- **`getPosition()`, `setPosition()`**: Reads and sets the token's position around the board.
- **`getFrontSide()`, `getBackSide()`, `getCurrentSide()`, `head()`, `isFrontSideUp()`**: Checks which side of the token is face up and returns the text on that side.
- **`move()`**: Moves the token around the board (positions 0-11) by a specified number of steps.
- **`turn()`**: Flips the token.

## 6. TimeTokens.java
Manages the time (hourglass) tokens used to track turn progress.
- **`value()`**: Returns the numerical turn value (1-8) corresponding to the drawn time token.
- **`extraite()`**: Draws the first available time token from the array and removes it.
- **`getTimeTokens()`**: Retrieves the array of all time tokens.

## 7. DetectiveTokens.java
Manages detective tokens and their positions.
- **`head()`**: Returns the name and current position of the detective token as a string.
- **`getHolmes()`, `getWatson()`, `getToby()`, `getAllDetectives()`**: Returns the respective detective objects individually or as a complete list.

## 8. ActionTokens.java
Controls the action tokens thrown each turn.
- **`lancer()`**: Throws the action tokens (randomly flipping them) to determine their faces for that turn.
- **`getActionTokens()`**: Returns the array of 4 action tokens.

## 9. GameSetup.java
Handles the initial preparations of the game.
- **`setupGame()`**: Creates the initial game state (GameState) by setting up the board, detectives, decks, and identity selections.
- **`createRandomBoard()`, `randomizeTileOrientations()`, `placeDetectives()`**: Sets up the board layout, rotates tiles, and places detectives at their starting points.
- **`chooseMrJackIdentity()`, `prepareAlibiDeck()`, `prepareTurnTokens()`, `chooseFirstPlayer()`**: Selects Mr. Jack's secret identity, shuffles decks, and determines the first player (Detective or Jack) by lot.
- **`createAllTiles()`, `createAllCharacters()`, `createAllAlibiCards()`, `createAllTurnTokens()`**: Creates draft lists of game objects.

## 10. GameCharacter.java
Tracks the status of characters (suspect, Mr. Jack, etc.).
- **`getId()`, `getName()`, `getColor()`**: Retrieves the character's ID, name, and color information.
- **`isSuspect()`, `markAsSuspect()`, `eliminate()`, `isEliminated()`**: Queries if the character is a suspect and updates the elimination status.
- **`isJack()`, `setJack()`**: Sets and reads whether the character is in the Mr. Jack role.
- **`isVisible()`, `setVisible()`**: Determines if the character is in the detectives' line of sight.
- **`getTile()`, `setTile()`**: Assigns or returns the tile the character is standing on.

## 11. CharacterFactory.java
Defines the 9 characters in the game.
- **`createCharacters()`**: Creates all 9 characters with their name, ID, and color properties and returns them as a list.

## 12. AlibiDeckManager.java
Manages the deck of Alibi cards.
- **`initializeDeck()`**: Fills the deck with initial alibi cards belonging to the characters.
- **`findCharacterByName()`**: Finds the relevant character object in the deck by name.
- **`shuffleCards()`**: Shuffles the deck.
- **`drawCard()`**: Draws the first card that has not yet been drawn from the deck.
- **`investigatorDraws()`**: When the investigator draws a card, it eliminates that character and updates the card owner.
- **`mrJackDraws()`**: When Mr. Jack draws a card, it adds the hourglass value on the card to his own count.
- **`getJackHourglassTotal()`**: Returns the total amount of hourglasses Mr. Jack has earned from cards in his hand.
- **`getEliminatedCharacters()`**: Provides a list of eliminated suspects.
- **`getCards()`**: Returns the current list of all cards.

## 13. AlibiCards.java
Represents a single alibi card in the deck.
- **`getId()`, `getCharacter()`, `getHourglassValue()`**: Retrieves the card's ID, the name of the character it belongs to, and the hourglass value written on it.
- **`isDrawn()`, `setDrawn()`**: Queries and modifies whether the card has been drawn from the deck.
- **`getOwner()`, `setOwner()`**: Reads and sets who currently holds the card (Deck, Investigator, Jack).

## 14. Position.java
Holds the row/column coordinates of objects on the game board.
- **`getRow()`, `getCol()`, `setRow()`, `setCol()`**: Retrieves or changes the row and column positions.

## 15. Orientation.java
An enum class containing directions and rotation operations.
- **`rotateClockwise()`**: Rotates the current direction to the next direction clockwise.

## 16. Player.java
A simple enum class defining the two roles that determine the order of play (DETECTIVE, MR_JACK).
