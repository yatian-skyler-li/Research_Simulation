package researchsim.scenario;

import researchsim.entities.*;
import researchsim.logging.Logger;
import researchsim.map.Coordinate;
import researchsim.map.Tile;
import researchsim.map.TileType;
import researchsim.util.BadSaveException;
import researchsim.util.CoordinateOutOfBoundsException;
import researchsim.util.Encodable;
import researchsim.util.NoSuchEntityException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


/**
 * The scenario is the overriding class of the simulation.
 * It is similar to a level in a video game.
 * <p>
 * NOTE: Some methods in this class require interaction with the {@link ScenarioManager}. Only
 * interact with it when you need it.
 *
 * @ass1_partial
 */
public class Scenario implements Encodable {

    /**
     * The minimum dimensions of the map grid.
     * The value of this constant is {@value}
     *
     * @ass1
     */
    public static final int MIN_SIZE = 5;
    /**
     * The maximum dimensions of the map grid.
     * The value of this constant is {@value}
     *
     * @ass1
     */
    public static final int MAX_SIZE = 15;
    /**
     * Maximum number of tiles that the grid contains.
     * The value of this constant is {@value}
     *
     * @ass1
     */
    public static final int MAX_TILES = MAX_SIZE * MAX_SIZE;
    /**
     * The name of this scenario.
     */
    private final String name;
    /**
     * The width of the map in the scenario.
     */
    private final int width;
    /**
     * The height of the map in the scenario.
     */
    private final int height;
    /**
     * The tile grid for this scenario.
     */
    private
    Tile[] mapGrid;
    /**
     * the random seed for this scenario
     */
    private int seed;
    /**
     * The default value of width, height and seed
     */
    private static int defaultValue = 5;
    /**
     * The logger of this scenario
     */
    private Logger logger;
    /**
     * The random generated in this scenario
     */
    private Random random;
    /**
     * The animal controller in this scenario
     */
    private AnimalController animalController;

    /**
     * Creates a new Scenario with a given name, width, height and random seed. <br>
     * A one dimensional (1D) array of tiles is created as the board with the given width and
     * height. <br>
     * An empty Animal Controller and logger is also initialised. <br>
     * An instance of the {@link Random} class in initialised with the given seed.
     *
     * @param name   scenario name
     * @param width  width of the board
     * @param height height of the board
     * @param seed   the random seed for this scenario
     * @throws IllegalArgumentException if width &lt; {@value Scenario#MIN_SIZE} or width &gt;
     *                                  {@value Scenario#MAX_SIZE} or height
     *                                  &lt; {@value Scenario#MIN_SIZE} or height &gt;
     *                                  {@value Scenario#MAX_SIZE} or seed &lt; 0 or name is {@code
     *                                  null}
     * @ass1_partial
     * @see Random (<a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html">Link</a>)
     */
    public Scenario(String name, int width, int height, int seed)
        throws IllegalArgumentException {
        if (width > MAX_SIZE || width < MIN_SIZE) {
            throw new IllegalArgumentException("The given width does not conform to the "
                + "requirement: " + MIN_SIZE + " <= width <= " + MAX_SIZE + ".");
        }
        if (height > MAX_SIZE || height < MIN_SIZE) {
            throw new IllegalArgumentException("The given height does not conform to the "
                + "requirement: " + MIN_SIZE + " <= height <= " + MAX_SIZE + ".");
        }
        if (name == null) {
            throw new IllegalArgumentException("The given name does not conform to the "
                + "requirement: name != null.");
        }
        if (seed < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.logger = new Logger();
        this.animalController = new AnimalController();
        this.mapGrid = new Tile[width * height];
        this.random = new Random(seed);
    }

    /**
     * Returns the name of the scenario.
     *
     * @return scenario name
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the map grid for this scenario.
     * <p>
     * Adding or removing elements from the returned array should not affect the original array.
     *
     * @return map grid
     * @ass1
     */
    public Tile[] getMapGrid() {
        return Arrays.copyOf(mapGrid, getSize());
    }

    /**
     * Updates the map grid for this scenario.
     * <p>
     * Adding or removing elements from the array that was passed should not affect the class
     * instance array.
     *
     * @param map the new map
     * @throws CoordinateOutOfBoundsException (param) map length != size of current scenario map
     * @ass1_partial
     */
    public void setMapGrid(Tile[] map) throws CoordinateOutOfBoundsException {
        if (map.length != this.getSize()) {
            throw new CoordinateOutOfBoundsException();
        }
        mapGrid = Arrays.copyOf(map, getSize());
    }


    /**
     * Returns the width of the map for this scenario.
     *
     * @return map width
     * @ass1
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the map for this scenario.
     *
     * @return map height
     * @ass1
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the size of the map in the scenario.<br>
     * The size of a map is the total number of tiles in the Tile array.
     *
     * @return map size
     * @ass1
     */
    public int getSize() {
        return width * height;
    }

    /**
     * Returns the scenarios random instance.
     *
     * @return random
     */
    public Random getRandom() {
        return this.random;
    }

    /**
     * Returns the scenario's activity log.
     *
     * @return game log
     */
    public Logger getLog() {
        return this.logger;
    }

    /**
     * Returns the scenarios enemy manager.
     *
     * @return enemy manager
     */
    public AnimalController getController() {
        return this.animalController;
    }

    /**
     * Read the line of width, height, or seed. Return the value of it.
     * @param line The line in the file to be read
     * @param expectedName The expected name of value
     * @return the value of width, height, or seed
     * @throws BadSaveException the Width, Height or Seed lines do not contain exactly one colon.
     * or any of the Width, Height and Seed keys (before colon) are not "Width", "Height" and
     * "Seed" respectively. Or any of the Width, Height and Seed values (after colon)
     * are not a valid integer (i.e. cannot be parsed by Integer.parseInt(String)).
     * Or Width, Height and Seed values cause an IllegalArgumentException when used to create
     * a Scenario with the constructor Scenario(String, int, int, int).
     * Scenario(ScenarioName, Width, Height, Seed)
     */
    private static int widthHeightSeedReader(String line, String expectedName)
            throws BadSaveException {
        int stat;
        if (line.split(":", -1).length != 2
                || !line.split(":", -1)[0].equals(expectedName)) {
            throw new BadSaveException();
        }
        try {
            stat = Integer.parseInt(line.split(":", -1)[1]);
        } catch (NumberFormatException e) {
            throw new BadSaveException();
        }
        if (stat == -1) {
            stat = defaultValue;
        }
        return stat;
    }

    /**
     * Read the map of scenario from the file
     *
     * @param bufferedReader the buffered reader
     * @param width the width of the scenario
     * @param height the height of the scenario
     * @return the list of tiles in the given scenario
     * @throws BadSaveException if The number of characters on that line is not exactly the
     * Width value (or its default, see below).
     * NOTE: This includes trailing whitespace such as tabs and spaces ( "\t" and " ").
     * If the number of characters provided is not equal to the size of the scenario created
     * with the Width and Height values
     * If ANY character provided can not be parsed by TileType.decode(String)
     * @throws IOException if the file is empty or have trouble reading the file
     */
    private static Tile[] mapReader(BufferedReader bufferedReader, int width, int height)
            throws BadSaveException, IOException {
        Tile[] map = new Tile[width * height];
        StringBuilder mapElements = new StringBuilder();
        for (int row = 0; row < height; row++) {
            String line = bufferedReader.readLine();
            if (line.length() != width) {
                throw new BadSaveException();
            }
            mapElements.append(line);
        }

        char[] tiles = mapElements.toString().toCharArray();
        int count = 0;
        for (char tile : tiles) {
            if (tile != 'L' && tile != 'O' && tile != 'S' && tile != 'X') {
                throw new BadSaveException();
            }
            map[count] = new Tile(TileType.decode(String.valueOf(tile)));
            count++;
        }
        return map;
    }

    /**
     * Read the entity in the file
     * @param line the given line containing entity information
     * @param map the map of the scenario
     * @param width the width of the scenario
     * @return The entity in the file
     * @throws BadSaveException if The line does not contain the correct number of hyphen ("-")
     * characters for its respective encoding (Fauna.encode(), Entity.encode(), User.encode())
     * The line does not start with "Fauna" or "Flora" or "User"
     * If the Coordinate component can not be decoded by Coordinate.decode(String)
     * If the Coordinate specified already has an Entity assigned.
     * i.e. You can not have multiple entities request the same Coordiante in the reader
     * If line starts with "Fauna" or "Flora" AND the Size component can not be decoded by
     * Size.valueOf(String)
     * If line starts with "Fauna" AND the Habitat component can not be decoded by
     * TileType.valueOf(String)
     * If line starts with "Fauna" AND the Habitat value causes an IllegalArgumentException
     * to be thrown
     * If line starts with "Fauna" AND the Habitat at the Tile specified by the Coordinate
     * is not suitable
     * That is, if Habitat is TileType.OCEAN the Tile's type must be TileType.OCEAN.
     * If the Habitat is TileType.LAND the Tile's type must NOT be TileType.OCEAN.
     * If line starts with "Flora" AND the Tile specified by the Coordinate is not suitable
     * That is, if Tile's type is TileType.OCEAN then it is INVALID.
     * If line starts with "User" AND the Tile specified by the Coordinate is not suitable
     * That is, if Tile's type is TileType.OCEAN or TileType.MOUNTAIN then it is INVALID.
     */
    private static Entity entityReader(String line, Tile[] map, int width) throws BadSaveException {
        String[] elements = line.split("-", -1);
        Coordinate coordinate = null;
        Size size;
        TileType habitat;
        String name;
        Entity entity = null;

        // The first element is type of Entity
        if (elements[0].equals("Fauna")) {
            // the expected length is 4
            if (elements.length != 4) {
                throw new BadSaveException();
            }
            // the third element is coordinate
            coordinate = Coordinate.decode(elements[2]);
            try {
                // the second element is size
                size = Size.valueOf(elements[1]);
                // the fourth element is habitat
                habitat = TileType.valueOf(elements[3]);
            } catch (Exception e) {
                throw new BadSaveException();
            }
            int index = coordinate.getX() + coordinate.getY() * width;
            if (habitat.equals(TileType.OCEAN)) {
                if (!map[index].getType().equals(TileType.OCEAN)) {
                    throw new BadSaveException();
                }
            } else if (habitat.equals(TileType.LAND)) {
                if (map[index].getType().equals(TileType.OCEAN)) {
                    throw new BadSaveException();
                }
            } else {
                throw new BadSaveException();
            }

            entity = new Fauna(size, coordinate, habitat);
        } else if (elements[0].equals("Flora")) {
            if (elements.length != 3) {
                throw new BadSaveException();
            }
            coordinate = Coordinate.decode(elements[2]);
            try {
                size = Size.valueOf(elements[1]);
            } catch (Exception e) {
                throw new BadSaveException();
            }

            int index = coordinate.getX() + coordinate.getY() * width;
            entity = new Flora(size, coordinate);
            if (map[index].getType().equals(TileType.OCEAN)) {
                throw new BadSaveException();
            }
        } else if (elements[0].equals("User")) {
            if (elements.length != 3) {
                throw new BadSaveException();
            }
            coordinate = Coordinate.decode(elements[1]);
            name = String.valueOf(elements[2]);
            int index = coordinate.getX() + coordinate.getY() * width;
            if (map[index].getType().equals(TileType.OCEAN)
                    || map[index].getType().equals(TileType.MOUNTAIN)) {
                throw new BadSaveException();
            }
            entity = new User(coordinate, name);
        } else {
            throw new BadSaveException();
        }

        if (map[coordinate.getX() + coordinate.getY() * width].hasContents()) {
            throw new BadSaveException();
        }
        return entity;
    }

    /**
     * Creates a Scenario instance by reading information from the given reader.
     * The provided reader should contain data in the format:
     *
     *  {ScenarioName}
     *  Width:{Width}
     *  Height:{Height}
     *  Seed:{Seed}
     *  {Separator}
     *  {map}
     *  {Separator}
     *  {entity}
     *  {entity...}
     *
     * (As specified by encode())
     * The reader is invalid if any of the following conditions are true:
     *
     * The given reader is empty
     * The reader hits EOF (end of file) before all of the required information is present.
     * The required information is:
     * ScenarioName
     * Width
     * Height
     * Seed
     * map
     * It is not required to have an entity. But a Separator must exist after the map.
     * The required information does NOT appear in the order specified above.
     * If the Width, Height or Seed lines do not contain exactly one (1) colon (":")
     * If any of the Width, Height and Seed keys (before colon) are not "Width", "Height"
     * and "Seed" respectively.
     * If any of the Width, Height and Seed values (after colon) are not a valid integer
     * (i.e. cannot be parsed by Integer.parseInt(String))
     * If any of the ScenarioName, Width, Height and Seed values cause an IllegalArgumentException
     * when used to create a Scenario with the constructor Scenario(String, int, int, int).
     * Scenario(ScenarioName, Width, Height, Seed)
     * A separator does not have exactly the Width value (or its default, see below) number of
     * equals characters ("=").
     * i.e. Width == 5 -> separator == "=====".
     * A separator must appear on the line immediately after the Seed and the last line of the map.
     * If any of the following hold true for a map line:
     * The number of characters on that line is not exactly the Width value (or its default,
     * see below).
     * NOTE: This includes trailing whitespace such as tabs and spaces ( "\t" and " ").
     * If the number of characters provided is not equal to the size of the scenario created with
     * the Width and Height values
     * If ANY character provided can not be parsed by TileType.decode(String)
     * If any of the following hold true for an entity line:
     * The line does not contain the correct number of hyphen ("-") characters for its respective
     * encoding (Fauna.encode(), Entity.encode(), User.encode())
     * The line does not start with "Fauna" or "Flora" or "User"
     * If the Coordinate component can not be decoded by Coordinate.decode(String)
     * If the Coordinate specified already has an Entity assigned.
     * i.e. You can not have multiple entities request the same Coordiante in the reader
     * If line starts with "Fauna" or "Flora" AND the Size component can not be decoded by
     * Size.valueOf(String)
     * If line starts with "Fauna" AND the Habitat component can not be decoded by
     * TileType.valueOf(String)
     * If line starts with "Fauna" AND the Habitat value causes an IllegalArgumentException
     * to be thrown
     * If line starts with "Fauna" AND the Habitat at the Tile specified by the Coordinate is not
     * suitable
     * That is, if Habitat is TileType.OCEAN the Tile's type must be TileType.OCEAN. If the Habitat
     * is TileType.LAND the Tile's type must NOT be TileType.OCEAN.
     * If line starts with "Flora" AND the Tile specified by the Coordinate is not suitable
     * That is, if Tile's type is TileType.OCEAN then it is INVALID.
     * If line starts with "User" AND the Tile specified by the Coordinate is not suitable
     * That is, if Tile's type is TileType.OCEAN or TileType.MOUNTAIN then it is INVALID.
     * If the Width, Height and Seed values are -1 they should be assigned a default value of
     * MIN_SIZE (5).
     *
     * The created Scenario should be added to the ScenarioManager class by calling
     * ScenarioManager.addScenario(Scenario).
     *
     * The created Scenario map should be set to the map as descriped in the Reader.
     *
     * The created entities should be inhabiting the Tiles at the Coordinate specified.
     * (HINT: Make sure that you add the Scenario before this step so that you can utilise the
     * index of the Coordinate by Coordinate.getIndex() or Coordinate.convert(int, int).)
     *
     * For example, the reader could contain:
     *
     *  Example File
     *  Width:-1
     *  Height:6
     *  Seed:20
     *  =====
     *  LLLLS
     *  LLSSO
     *  LLSOO
     *  LLSSS
     *  LLLLL
     *  LLLLL
     *  =====
     *  Fauna-SMALL-1,1-LAND
     *  Flora-LARGE-2,5-LAND
     *
     * Noting that the Width is set to a default value of MIN_SIZE (5) and as such the Map is valid
     * The simplest file would be:
     *
     *  Example File
     *  Width:5
     *  Height:5
     *  Seed:5
     *  =====
     *  LLLLL
     *  LLLLL
     *  LLLLL
     *  LLLLL
     *  LLLLL
     *  =====
     *
     * @param reader reader from which to load all info (will not be null)
     * @return scenario created by reading from the given reader
     * @throws IOException if an IOException is encountered when reading from the reader
     * @throws BadSaveException if the reader contains a line that does not adhere to the
     * rules above (thus indicating that the contents of the reader are invalid)
     */
    public static Scenario load(Reader reader) throws IOException, BadSaveException {
        if (reader == null) {
            throw new BadSaveException();
        }
        List<Entity> entities = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(reader);
        Object checkEmpty = null;

        checkEmpty = bufferedReader.readLine();
        if (checkEmpty == null) {
            throw new BadSaveException();
        }
        int width = 0;
        width = widthHeightSeedReader(bufferedReader.readLine(), "Width");
        int height = 0;
        height = widthHeightSeedReader(bufferedReader.readLine(), "Height");
        int seed = -2;
        seed = widthHeightSeedReader(bufferedReader.readLine(), "Seed");

        StringBuilder separator = new StringBuilder();
        separator.append("=".repeat(Math.max(0, width)));
        if (!bufferedReader.readLine().equals(separator.toString())) {
            throw new BadSaveException();
        }
        String scenarioName = String.valueOf(checkEmpty);
        if (width > MAX_SIZE || width < MIN_SIZE
                || height > MAX_SIZE || height < MIN_SIZE
                || seed < 0 || scenarioName == null) {
            throw new BadSaveException();
        }
        Scenario scenario = new Scenario(scenarioName, width, height, seed);

        Tile[] grid = null;
        grid = mapReader(bufferedReader, width, height);

        if (!separator.toString().equals(bufferedReader.readLine())) {
            throw new BadSaveException();
        }

        List<Coordinate> occupied = new ArrayList<>();
        String restLine = bufferedReader.readLine();
        while (restLine != null) {
            Entity addedEntity = entityReader(restLine, grid, width);
            if (occupied.contains(addedEntity.getCoordinate())) {
                throw new BadSaveException();
            }
            entities.add(addedEntity);
            occupied.add(addedEntity.getCoordinate());
            restLine = bufferedReader.readLine();
        }
        bufferedReader.close();

        for (Entity entity : entities) {
            int indEntity = entity.getCoordinate().getX() + entity.getCoordinate().getY() * width;
            Tile update = grid[indEntity];
            update.setContents(entity);
            grid[indEntity] = update;
            if (entity instanceof Fauna) {
                scenario.animalController.addAnimal((Fauna) entity);
            }
        }

        try {
            scenario.setMapGrid(grid);
        } catch (CoordinateOutOfBoundsException e) {
            // Ignore it since we squash this exception
        }
        ScenarioManager manager = ScenarioManager.getInstance();
        manager.addScenario(scenario);
        return scenario;
    }

    /**
     * Returns the hash code of this scenario.
     * Two scenarios that are equal according to the equals(Object) method should have the
     * same hash code.
     *
     * @return hash code of this scenario.
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.getMapGrid()),
                this.getWidth(), this.getHeight(), this.getName());
    }

    /**
     * Returns true if and only if this scenario is equal to the other given object.
     * For two scenarios to be equal, they must have the same:
     *
     * name
     * width
     * height
     * map contents (The tile array)
     *
     * @param other the reference object with which to compare
     * @return true if this scenario is the same as the other argument; false otherwise
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Scenario)) {
            return false;
        }

        Scenario scenario = (Scenario) other;
        return this.getName().equals(scenario.getName())
                && this.getWidth() == scenario.getWidth()
                && this.getHeight() == scenario.getHeight()
                && Arrays.equals(this.getMapGrid(), scenario.getMapGrid());
    }


    /**
     * Returns the human-readable string representation of this scenario.
     * <p>
     * The format of the string to return is:
     * <pre>
     *     (name)
     *     Width: (width), Height: (height)
     *     Entities: (entities)
     * </pre>
     * Where:
     * <ul>
     *   <li>{@code (name)} is the scenario's name</li>
     *   <li>{@code (width)} is the scenario's width</li>
     *   <li>{@code (height)} is the scenario's height</li>
     *   <li>{@code (entities)} is the number of entities currently on the map in the scenario</li>
     * </ul>
     * For example:
     *
     * <pre>
     *     Beach retreat
     *     Width: 6, Height: 5
     *     Entities: 4
     * </pre>
     * <p>
     * Each line should be separated by a system-dependent line separator.
     *
     * @return human-readable string representation of this scenario
     * @ass1
     */
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        result.add(name);
        result.add(String.format("Width: %d, Height: %d", width, height));
        result.add(String.format("Entities: %d",
            Arrays.stream(mapGrid).filter(Objects::nonNull).filter(Tile::hasContents).count()));
        return result.toString();
    }

    /**
     * Returns the machine-readable string representation of this Scenario.
     * The format of the string to return is
     *
     *  {ScenarioName}
     *  Width:{Width}
     *  Height:{Height}
     *  Seed:{Seed}
     *  {Separator}
     *  {map}
     *  {Separator}
     *  {entity}
     *  {entity...}
     *
     * Where:
     * {ScenarioName} is the name of the scenario
     * {Width} is the width of the scenario
     * {Height} is the Height of the scenario
     * {Seed} is the seed of the scenario
     * NOTE: There is no whitespace between the ':' and value for the above conditions
     * {Separator} is a string of repeated equals "=" characters where the number of characters
     * is equal to the width of the scenario
     * i.e. width == 5 -> separator == "====="
     * {map} is the tile map grid where:
     * Each tile is represented by its TileType encoding (TileType.encode())
     * A system-dependent line separator is added after Width characters are written
     * (See example below)
     * {entity} is the Entity.encode() of each entity found in the map where:
     * Each entity is added in the order it appears in the array by index (i.e. an entity
     * inhabiting a tile with index 1 appears before an entity inhabiting a tile with index 4
     * A system-dependent line separator is added after entity EXCEPT the last entity
     * For example, a simple scenario with the following attributes:
     * Name - Scenario X
     * Width - 5
     * Height - 5
     * Seed - 0
     * A Mouse located at Coordinate (1,1)
     * See Fauna.getName()
     * The map is as shown in the save
     * Each Tile is represented by its TileType.encode() value of its type
     * would be return the following string
     *  Scenario X
     *  Width:5
     *  Height:5
     *  Seed:0
     *  =====
     *  LLLLS
     *  LLSSO
     *  LLSOO
     *  LLSSS
     *  LLLLL
     *  =====
     *  Fauna-SMALL-1,1-LAND
     *
     * @return encoded string representation of this Scenario
     */
    public String encode() {
        StringBuilder separator = new StringBuilder();
        separator.append("=".repeat(Math.max(0, this.getWidth())));

        StringBuilder map = new StringBuilder();
        StringJoiner repEntity = new StringJoiner(System.lineSeparator());
        for (Tile tile : this.getMapGrid()) {
            map.append(tile.getType().encode());
            try {
                repEntity.add(tile.getContents().encode());
            } catch (NoSuchEntityException e) {
                // Ignore it since we squash it
            }
        }

        StringJoiner repMap = new StringJoiner(System.lineSeparator());
        for (int cut = 0; cut < this.getSize(); cut += this.getWidth()) {
            repMap.add(map.substring(cut, cut + this.getWidth()));
        }

        StringJoiner scenarioRep = new StringJoiner(System.lineSeparator());
        scenarioRep.add(this.getName());
        scenarioRep.add("Width:" + this.getWidth());
        scenarioRep.add("Height:" + this.getHeight());
        scenarioRep.add("Seed:" + this.seed);
        scenarioRep.add(separator.toString());
        scenarioRep.add(repMap.toString());
        scenarioRep.add(separator.toString());
        if (repEntity.toString().length() != 0) {
            scenarioRep.add(repEntity.toString());
        }
        return scenarioRep.toString();
    }
}
