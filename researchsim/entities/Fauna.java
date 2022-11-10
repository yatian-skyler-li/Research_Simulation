package researchsim.entities;

import javafx.util.Pair;
import researchsim.logging.CollectEvent;
import researchsim.logging.MoveEvent;
import researchsim.map.Coordinate;
import researchsim.map.Tile;
import researchsim.map.TileType;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.Collectable;
import researchsim.util.CoordinateOutOfBoundsException;
import researchsim.util.Movable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Fauna is all the animal life present in a particular region or time.
 * Fauna can move around the scenario and be collected by the {@link User}.
 * <p>
 * NOTE: Some methods in this class require interaction with the {@link ScenarioManager}. Only
 * interact with it when you need it.
 *
 * @ass1_partial
 * @ass1_test
 */
public class Fauna extends Entity implements Movable, Collectable {

    /**
     * The habitat associated with the animal.
     * That is, what tiles an animal can exist in.
     */
    private final TileType habitat;

    /**
     * Creates a fauna (Animal) with a given size, coordinate and habitat.
     *
     * @param size       size associated with the animal
     * @param coordinate coordinate associated with the animal
     * @param habitat    habitat tiles associated with the animal
     * @throws IllegalArgumentException if habitat is not {@link TileType#LAND} or
     *                                  {@link TileType#OCEAN}
     * @ass1
     */
    public Fauna(Size size, Coordinate coordinate, TileType habitat)
        throws IllegalArgumentException {
        super(size, coordinate);
        if (habitat != TileType.LAND && habitat != TileType.OCEAN) {
            throw new IllegalArgumentException("Animal was created with a bad habitat: " + habitat);
        }
        this.habitat = habitat;
    }

    /**
     * Returns the animal's habitat.
     *
     * @return animal's habitat
     * @ass1
     */
    public TileType getHabitat() {
        return habitat;
    }

    /**
     * Returns the human-readable name of this animal.
     * The name is determined by the following table.
     * <p>
     * <table border="1">
     *     <caption>Human-readable names</caption>
     *     <tr>
     *         <td rowspan="2" colspan="2" style="background-color:#808080">&nbsp;</td>
     *         <td colspan="3">Habitat</td>
     *     </tr>
     *     <tr>
     *         <td>LAND</td>
     *         <td>OCEAN</td>
     *     </tr>
     *     <tr>
     *         <td rowspan="4">Size</td>
     *         <td>SMALL</td>
     *         <td>Mouse</td>
     *         <td>Crab</td>
     *     </tr>
     *     <tr>
     *         <td>MEDIUM</td>
     *         <td>Dog</td>
     *         <td>Fish</td>
     *     </tr>
     *     <tr>
     *         <td>LARGE</td>
     *         <td>Horse</td>
     *         <td>Shark</td>
     *     </tr>
     *     <tr>
     *         <td>GIANT</td>
     *         <td>Elephant</td>
     *         <td>Whale</td>
     *     </tr>
     * </table>
     * <p>
     * e.g. if this animal is {@code MEDIUM} in size and has a habitat of {@code LAND} then its
     * name would be {@code "Dog"}
     *
     * @return human-readable name
     * @ass1
     */
    @Override
    public String getName() {
        String name;
        switch (getSize()) {
            case SMALL:
                name = habitat == TileType.LAND ? "Mouse" : "Crab";
                break;
            case MEDIUM:
                name = habitat == TileType.LAND ? "Dog" : "Fish";
                break;
            case LARGE:
                name = habitat == TileType.LAND ? "Horse" : "Shark";
                break;
            case GIANT:
            default:
                name = habitat == TileType.LAND ? "Elephant" : "Whale";
        }
        return name;
    }

    /**
     * Returns the machine-readable string representation of this animal.
     * <p>
     * The format of the string to return is:
     * <pre>Fauna-size-coordinate-habitat</pre>
     * Where:
     * <ul>
     *   <li>{@code size} is the animal's associated size</li>
     *   <li>{@code coordinate} is the encoding of animal's associated coordinate</li>
     *   <li>{@code habitat} is the animal's associated habitat</li>
     * </ul>
     * For example:
     *
     * <pre>Fauna-SMALL-4,6-LAND</pre>
     *
     * @return encoded string representation of this animal
     */
    @Override
    public String encode() {
        return super.encode() + "-" + this.getHabitat();
    }

    /**
     * Returns the hash code of this animal.
     *
     * Two animals that are equal according to the equals(Object) method should have
     * the same hash code.
     *
     * @return hash code of this animal.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getHabitat());
    }

    /**
     * Returns true if and only if this animal is equal to the other given object.
     *
     * For two animals to be equal, they must have the same size, coordinate and habitat.
     *
     * @param other the reference object with which to compare
     * @return true if this animal is the same as the other argument; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Fauna)) {
            return false;
        }
        Fauna otherFauna = (Fauna) other;
        return super.equals(other) && this.getHabitat().equals(otherFauna.getHabitat());
    }

    /**
     * Returns the human-readable string representation of this animal.
     * <p>
     * The format of the string to return is:
     * <pre>name [Fauna] at coordinate [habitat]</pre>
     * Where:
     * <ul>
     *   <li>{@code name} is the animal's human-readable name according to {@link #getName()}</li>
     *   <li>{@code coordinate} is the animal's associated coordinate in human-readable form</li>
     *   <li>{@code habitat} is the animal's associated habitat</li>
     *
     * </ul>
     * For example:
     *
     * <pre>Dog [Fauna] at (2,5) [LAND]</pre>
     *
     * @return human-readable string representation of this animal
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s [%s]",
            super.toString(),
            this.habitat);
    }

    /**
     * Returns a List of all the possible coordinates that this animal can move to.
     *
     * The possible coordinates that this animal can move to are defined as:
     * Any Coordinate in Movable.checkRange(int, Coordinate)
     * (checkRange(move distance, current coordinate)) that this animal can move to.
     * Any CoordinateOutOfBoundsException's thrown by canMove(Coordinate) are squashed.
     *
     * The order of the returned coordinates does not matter.
     *
     * @return list of possible movements.
     */
    public List<Coordinate> getPossibleMoves() {
        List<Coordinate> possibleMoves = new ArrayList<>();
        List<Coordinate> inRangeMoves = this.checkRange(getSize().moveDistance, getCoordinate());
        for (Coordinate coordinate : inRangeMoves) {
            try {
                if (this.canMove(coordinate)) {
                    possibleMoves.add(coordinate);
                }
            } catch (CoordinateOutOfBoundsException e) {
                // Ignore it because it won't be thrown
            }
        }
        return possibleMoves;
    }

    /**
     * Moves the animal to the new coordinate.
     *
     * The Tile that the animal moves to should now be occupied by this animal.
     * The tile that the animal moves from (the existing coordinate) should now have no occupant.
     * A MoveEvent should be created and added to the current scenario logger.
     *
     * @param coordinate The new coordinate to move to.
     */
    public void move(Coordinate coordinate) {
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        scenario.getMapGrid()[getCoordinate().getIndex()].setContents(null);
        scenario.getMapGrid()[coordinate.getIndex()].setContents(this);

        scenario.getLog().add(new MoveEvent(this, coordinate));
        this.setCoordinate(coordinate);
    }

    /**
     * Return true if animal could move to the given coordinate in the path provided.
     *
     * @param coordinate The new coordinate to move to.
     * @param path The list of coordinates on the path.
     * @param scenario The current scenario
     *
     * @return true if animal could move to the given coordinate in the path provided.
     */
    private boolean validPath(List<Coordinate> path, Coordinate coordinate, Scenario scenario) {
        if (path.size() == 0) {
            return false;
        }
        for (Coordinate coordinateOnPath : path) {
            TileType typeOnPath = scenario.getMapGrid()[coordinateOnPath.getIndex()].getType();
            // Check if the habitat type is valid
            boolean habitatTile;
            if (getHabitat() == TileType.OCEAN && typeOnPath == TileType.OCEAN) {
                habitatTile = true;
            } else if (getHabitat() == TileType.LAND && typeOnPath != TileType.OCEAN) {
                habitatTile = true;
            } else {
                habitatTile = false;
            }
            if (getCoordinate().equals(coordinateOnPath)
                    || !coordinateOnPath.isInBounds()
                    || !habitatTile
                    || scenario.getMapGrid()[coordinateOnPath.getIndex()].hasContents()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the pair of the paths to the target destination.
     * Each path is a list of coordinates on the path.
     *
     * @param target the target coordinate to move to
     * @return the pair of paths to the target
     */
    private Pair<List<Coordinate>, List<Coordinate>> getPath(Coordinate target) {
        int currentX = getCoordinate().getX();
        int currentY = getCoordinate().getY();
        int targetX = target.getX();
        int targetY = target.getY();
        int distanceX = targetX - currentX;
        int distanceY = targetY - currentY;

        List<Coordinate> pathHorizontal = new ArrayList<>();
        List<Coordinate> pathVertical = new ArrayList<>();

        // Check the target on the horizontal level, left or right
        if (distanceX > 0) {
            for (int x = distanceX; x > 0; x--) {
                pathHorizontal.add(new Coordinate(currentX + x, currentY));
                pathVertical.add(new Coordinate(currentX + x, targetY));
            }
        } else if (distanceX < 0) {
            for (int x = distanceX; x < 0; x++) {
                pathHorizontal.add(new Coordinate(currentX + x, currentY));
                pathVertical.add(new Coordinate(currentX + x, targetY));
            }
        }
        // Check the target on the vertical level, up or down
        if (distanceY > 0) {
            for (int y = distanceY; y > 0; y--) {
                pathHorizontal.add(new Coordinate(targetX, currentY + y));
                pathVertical.add(new Coordinate(currentX, currentY + y));
            }
        } else if (distanceY < 0) {
            for (int y = distanceY; y < 0; y++) {
                pathHorizontal.add(new Coordinate(targetX, currentY + y));
                pathVertical.add(new Coordinate(currentX, currentY + y));
            }
        }
        return new Pair<List<Coordinate>, List<Coordinate>>(pathHorizontal, pathVertical);
    }

    /**
     * Determines if the animal can move to the new coordinate.
     * An animal can move to the new coordinate if ALL of the following conditions are satisfied:
     *
     * The new coordinate must be different from the current coordinate.
     * The coordinate given is on the current scenario map (See ScenarioManager).
     * The distance from the given coordinate to the current coordinate is not greater than the
     * distance this animal can move (Size.moveDistance)
     * If the animal's habitat is OCEAN then the tile at the coordinate must be OCEAN
     * If the animal's habitat is LAND then the tile at the coordinate must NOT be OCEAN
     * The tile at the coordinate is not already occupied
     * The animal has an unimpeded path (meaning all the above conditions are true) for each tile
     * it must traverse to reach the destination coordinate
     * The animal can only turn once.
     *
     * For example:
     * If the animal wants to move from (0,0) to (2,1) on the following encoded map.
     *
     *  LLL
     *  LSL
     *  LLL
     *
     * (The above map is not possible to be created normally as the minimum dimensions are 5 x 5)
     * It would have to be able to move to all the following coordinates:
     * [(1,0),(2,0),(2,1)]
     * OR
     * [(0,1),(1,1),(2,1)]
     *
     * @param coordinate The new coordinate
     * @return true if the above conditions are satisfied else false
     * @throws CoordinateOutOfBoundsException if the coordinate given is out of bounds
     */
    public boolean canMove(Coordinate coordinate) throws CoordinateOutOfBoundsException {
        if (!coordinate.isInBounds()) {
            throw new CoordinateOutOfBoundsException();
        }
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        int distance = Math.abs(coordinate.getX() - this.getCoordinate().getX())
                + Math.abs(coordinate.getY() - this.getCoordinate().getY());

        return (validPath(getPath(coordinate).getKey(), coordinate, scenario)
                    || validPath(getPath(coordinate).getValue(), coordinate, scenario))
                && this.getSize().moveDistance >= distance;
    }

    /**
     * A User interacts and collects this animal.
     *
     * Upon collection the following should occur:
     * A CollectEvent should be created with the animal and coordinate.
     * The Tile that the animal was occupying should now be unoccupied (empty).
     * The animal should be removed from the current scenario's animal controller.
     * The given user will gain a number of points for collecting this animal.
     * This value is determined by the animals size.
     *
     * @param user the user that collects the entity.
     * @return points earned
     */
    public int collect(User user) {
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        scenario.getLog().add(new CollectEvent(user, this));
        scenario.getMapGrid()[getCoordinate().getIndex()].setContents(null);
        scenario.getController().removeAnimal(this);
        return this.getSize().points;
    }
}
