package researchsim.entities;

import javafx.util.Pair;
import researchsim.logging.MoveEvent;
import researchsim.map.Coordinate;
import researchsim.map.Tile;
import researchsim.map.TileType;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.Collectable;
import researchsim.util.CoordinateOutOfBoundsException;
import researchsim.util.Movable;
import researchsim.util.NoSuchEntityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User is the player controlled character in the simulation.
 * A user can {@code collect} any class that implements the {@link researchsim.util.Collectable}
 * interface.
 * <p>
 * NOTE: Some methods in this class require interaction with the {@link ScenarioManager}. Only
 * interact with it when you need it.
 *
 * @ass2
 * @ass2_test
 */
public class User extends Entity implements Movable {
    /**
     * the name of this user
     */
    private String name;
    /**
     * The max move distance of user
     */
    private final int maxMoveDistance = 4;

    /**
     * Creates a user with a given coordinate and name.
     * A user a MEDIUM sized entity.
     * @param coordinate coordinate associated with the user
     * @param name the name of this user
     */
    public User(Coordinate coordinate, String name) {
        super(Size.MEDIUM, coordinate);
        this.name = name;
    }

    /**
     * Returns the name of this user.
     *
     * @return the name of this user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the machine-readable string representation of this user.
     *
     * The format of the string to return is
     * User-coordinate-name
     * Where:
     * coordinate is the encoding of the user's associated coordinate
     * name is the user's name
     * For example:
     * User-3,5-Bob
     *
     * @return encoded string representation of this user
     */
    @Override
    public String encode() {
        return String.format("%s-%s-%s",
                this.getClass().getSimpleName(),
                this.getCoordinate().encode(),
                this.getName());
    }

    /**
     * Returns the hash code of this user.
     * Two users that are equal according to the equals(Object) method should have the
     * same hash code.
     *
     * @return hash code of this user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    /**
     * Returns true if and only if this user is equal to the other given object.
     * For two users to be equal, they must have the same coordinate and name.
     *
     * @param other the reference object with which to compare
     * @return true if this user is the same as the other argument; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }

        User otherUser = (User) other;
        return super.equals(other) && this.getName().equals(otherUser.getName());
    }

    /**
     * Returns a List of all the possible coordinates that this user can move to.
     * The possible coordinates that this User can move to are defined as:
     * Any Coordinate in Movable.checkRange(int, Coordinate)
     * (checkRange(move distance, current coordinate)) that this user can move to.
     * Any CoordinateOutOfBoundsException's thrown by canMove(Coordinate) are squashed.
     *
     * The order of the returned coordinates does not matter.
     *
     * @return list of possible movements
     */
    public List<Coordinate> getPossibleMoves() {
        List<Coordinate> possibleMoves = new ArrayList<>();
        for (Coordinate coordinate : this.checkRange(getSize().moveDistance, getCoordinate())) {
            try {
                if (canMove(coordinate)) {
                    possibleMoves.add(coordinate);
                }
            } catch (CoordinateOutOfBoundsException e) {
                // Ignore it since we squash the exception thrown by canMove method
            }
        }
        return possibleMoves;
    }

    /**
     * Moves the user to the new coordinate.
     *
     * The Tile that the user moves to should now be occupied by this user.
     * The tile that the user moves from (the existing coordinate) should now have no occupant.
     * A MoveEvent should be created with the animal and new coordinate.
     * If the new coordinate has an entity that implements Collectable then this entity should be
     * collected with its implementation of collect(Coordinate).
     * The move event should be added to the log BEFORE calling collect(Coordinate).
     * Any exceptions that might be raised from collect(Coordinate) should be suppressed.
     *
     * @param coordinate The new coordinate to move to
     */
    public void move(Coordinate coordinate) {
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        scenario.getLog().add(new MoveEvent(this, coordinate));

        try {
            this.collect(coordinate);
        } catch (NoSuchEntityException | CoordinateOutOfBoundsException e) {
            // Ignore it since exceptions that raised from collect should be suppressed
        }
        scenario.getMapGrid()[this.getCoordinate().getIndex()].setContents(null);
        scenario.getMapGrid()[coordinate.getIndex()].setContents(this);
        this.setCoordinate(coordinate);
    }

    /**
     * Return true if user can move to the given coordinate in the given path
     * @param path the given path to the destination
     * @param coordinate the target coordinate to move to
     * @param scenario the current scenario
     * @return true if user can move to the given coordinate in the given path
     */
    private boolean validPath(List<Coordinate> path, Coordinate coordinate, Scenario scenario) {
        if (path.size() == 0) {
            return false;
        }
        for (Coordinate coordinateOnPath : path) {
            Tile tileOnPath  = scenario.getMapGrid()[coordinateOnPath.getIndex()];
            if (this.getCoordinate().equals(coordinate)
                    || !coordinateOnPath.isInBounds()
                    || tileOnPath.getType().equals(TileType.OCEAN)
                    || tileOnPath.getType().equals(TileType.MOUNTAIN)) {
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
     * Determines if the user can move to the specified coordinate.
     *
     * A User can move to the new coordinate if ALL of the following conditions are satisfied:
     * The new coordinate must be different from the current coordinate.
     * The coordinate given is on the scenario map.
     * The distance from the given coordinate to the current coordinate is not greater than four (4)
     * The tile at the coordinate is NOT OCEAN or MOUNTAIN
     * The entity has an unimpeded path for each tile it must traverse to reach the destination
     * A user can only turn once, i.e. can not move diagonally but rather n tiles in the
     * horizontal plane followed by m tiles in the vertical plane (or vice versa).
     *
     * For example:
     * If the user wants to move from (0,0) to (2,1) on the following encoded map.
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
     * @param coordinate coordinate to check
     * @return true if the instance can move to the specified coordinate else false
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
                && maxMoveDistance >= distance;
    }

    /**
     * Returns a List of all the possible coordinates that this user can collect from.
     *
     * A user can only collect from its immediate neighbouring tiles (only 1 tile away) that:
     * Are in the bounds of the current scenario
     * Have contents (Tile.hasContents())
     * If the contents of that tile implement the Collectable interface
     * The User can collect from ANY TileType.
     * The order of the returned coordinates does not matter.
     *
     * @return list of possible collections
     */
    public List<Coordinate> getPossibleCollection() {
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        List<Coordinate> possibleCollect = new ArrayList<>();
        // Check if user could collect the entity on the tile on the coordinate in range
        for (Coordinate coordinateInRange : this.checkRange(1, getCoordinate())) {
            try {
                if (coordinateInRange.isInBounds()) {
                    Tile tileCollected = scenario.getMapGrid()[coordinateInRange.getIndex()];
                    if (tileCollected.getContents() instanceof Flora
                            || tileCollected.getContents() instanceof Fauna) {
                        possibleCollect.add(coordinateInRange);
                    }
                }
            } catch (NoSuchEntityException e) {
                // Ignore it. We squash this exception
            }
        }
        return possibleCollect;

    }

    /**
     * Collects an entity from the specified coordinate.
     * If the entity at the given coordinate does not implement the Collectable interface then no
     * action is taken.
     * This method should collect the entity even if the Coordinate is more than 1 tile away.
     *
     * @param coordinate the coordinate we are collecting from
     * @throws NoSuchEntityException if the given coordinate is empty
     * @throws CoordinateOutOfBoundsException if the given coordinate is not in the map bounds.
     */
    public void collect(Coordinate coordinate)
            throws NoSuchEntityException, CoordinateOutOfBoundsException {
        Scenario scenario = ScenarioManager.getInstance().getScenario();
        Entity collectedItem = scenario.getMapGrid()[coordinate.getIndex()].getContents();
        if (collectedItem == null) {
            throw new NoSuchEntityException();
        }
        if (!coordinate.isInBounds()) {
            throw new CoordinateOutOfBoundsException();
        }
        if (collectedItem instanceof Fauna || collectedItem instanceof Flora) {
            ((Collectable) collectedItem).collect(this);
        }
    }
}
