package model;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;
public class AreaSet implements Serializable {
    private Tile[] areas;

    public AreaSet(List<GameCharacter> characters) {
        this.areas = createDefaultAreas(characters);
    }

    private Tile[] createDefaultAreas(List<GameCharacter> characters) {
        Tile area1 = createAreaSameRoads(
                1,
                findCharacterByName(characters, "Madame"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area2 = createAreaSameRoads(
                2,
                findCharacterByName(characters, "Sgt Goodley"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area3 = createAreaSameRoads(
                3,
                findCharacterByName(characters, "Jeremy Bert"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area4 = createAreaSameRoads(
                4,
                findCharacterByName(characters, "William Gull"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area5 = createAreaSameRoads(
                5,
                findCharacterByName(characters, "Miss Stealthy"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area6 = createAreaSameRoads(
                6,
                findCharacterByName(characters, "John Smith"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area7 = createAreaSameRoads(
                7,
                findCharacterByName(characters, "Insp. Lestrade"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );

        Tile area8 = createAreaSameRoads(
                8,
                findCharacterByName(characters, "John Pizer"),
                Direction.WEST,
                Direction.EAST,
                Direction.NORTH
        );
        // only area where suspect side and empty side have different roads.
        Tile area9 = createAreaDifferentRoads(
                9,
                findCharacterByName(characters, "Joseph Lane"),

                roads(Direction.WEST, Direction.EAST, Direction.NORTH),

                roads(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH)
        );

        return new Tile[]{
                area1,
                area2,
                area3,
                area4,
                area5,
                area6,
                area7,
                area8,
                area9
        };
    }

    private Tile createAreaSameRoads(int id, GameCharacter character, Direction... roads) {
        Set<Direction> roadSet = roads(roads);

        return new Tile(
                id,
                character,
                new Position(-1, -1),
                roadSet,
                roadSet
        );
    }

    private Tile createAreaDifferentRoads(
            int id,
            GameCharacter character,
            Set<Direction> suspectSideRoads,
            Set<Direction> emptySideRoads
    ) {
        return new Tile(
                id,
                character,
                new Position(-1, -1),
                suspectSideRoads,
                emptySideRoads
        );
    }

    private Set<Direction> roads(Direction... directions) {
        if (directions.length == 0) {
            return EnumSet.noneOf(Direction.class);
        }

        Set<Direction> roads = EnumSet.noneOf(Direction.class);

        for (Direction direction : directions) {
            roads.add(direction);
        }

        return roads;
    }

    private GameCharacter findCharacterByName(List<GameCharacter> characters, String name) {
        for (GameCharacter character : characters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }

        throw new IllegalArgumentException("Character not found: " + name);
    }

    public Tile[] getAllAreas() {
        return areas;
    }

    public Tile getAreaByIndex(int index) {
        return areas[index];
    }

    public Tile getAreaById(int id) {
        for (Tile area : areas) {
            if (area.getId() == id) {
                return area;
            }
        }

        return null;
    }

    public int size() {
        return areas.length;
    }

    public int remainingSuspects() {
        int count = 0;

        for (Tile area : areas) {
            if (area.isSuspectVisible()) {
                count++;
            }
        }

        return count;
    }
}