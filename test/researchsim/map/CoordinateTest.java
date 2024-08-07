package researchsim.map;

import javafx.scene.layout.CornerRadii;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.BadSaveException;
import researchsim.util.CoordinateOutOfBoundsException;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Q1: test null.getAbsX()? distance(null)
 */

public class CoordinateTest {

    private Coordinate coordinate11;
    private Coordinate coordinate11Dup;
    private Coordinate coordinate21;
    private Coordinate coordinateneg21;
    private Coordinate origin;

    @Before
    public void setUp() throws Exception {
        origin = new Coordinate();
        coordinate11 = new Coordinate(1, 1);
        coordinate11Dup = new Coordinate(1, 1);
        coordinate21 = new Coordinate(2, 1);
        coordinateneg21 = new Coordinate(-2, -1);
    }

    @After
    public void tearDown() throws Exception {
        ScenarioManager.getInstance().reset();
    }

    @Test
    public void testDefaultConstructor() {
        Coordinate origin = new Coordinate();
        assertEquals("Incorrect value was returned.", 0, origin.getX());
        assertEquals("Incorrect value was returned.", 0, origin.getY());
    }

    @Test
    public void testIndexConstructor() {
        createSafeTestScenario("testIndexConstructor", 5, 5);
        Coordinate origin = new Coordinate(12);
        assertEquals("Incorrect value was returned.", 2, origin.getX());
        assertEquals("Incorrect value was returned.", 2, origin.getY());
    }

    @Test
    public void testGetX() {
        assertEquals("Incorrect value was returned.", 1, coordinate11.getX());
        assertEquals("Incorrect value was returned.", -2, coordinateneg21.getX());
    }

    @Test
    public void testGetAbsX() {
        Coordinate origin = new Coordinate();
        assertEquals("Incorrect value was returned.", 1, coordinate11.getAbsX());
        assertEquals("Incorrect value was returned.", 2, coordinateneg21.getAbsX());
        assertEquals("Incorrect value was returned.", 0, origin.getAbsX());
    }

    @Test
    public void testGetY() {
        assertEquals("Incorrect value was returned.", 1, coordinate11.getY());
        assertEquals("Incorrect value was returned.", -1, coordinateneg21.getY());
    }

    @Test
    public void testGetAbsY() {
        assertEquals("Incorrect value was returned.", 1, coordinate11.getAbsY());
        assertEquals("Incorrect value was returned.", 1, coordinateneg21.getAbsY());
        assertEquals("Incorrect value was returned.", 0, origin.getAbsX());
    }

    @Test
    public void testGetIndex() {
        createSafeTestScenario("testGetIndex", 5, 5);
        assertEquals("Incorrect value was returned.",
            6, coordinate11.getIndex());
        assertEquals("Incorrect value was returned.",
            7, coordinate21.getIndex());
        assertEquals("Incorrect value was returned.",
            -7, coordinateneg21.getIndex());
    }

    @Test
    public void testIsInBounds() {
        createSafeTestScenario("testIsInBounds", 10, 10);
        assertTrue("Incorrect value was returned.", coordinate11.isInBounds());
        assertFalse("Incorrect value was returned.", coordinateneg21.isInBounds());
    }

    @Test
    public void testConvert() {
        createSafeTestScenario("testConvert", 10, 10);
        assertEquals("Incorrect value was returned.",
            55, Coordinate.convert(5, 5));
        createSafeTestScenario("testConvert2", 5, 7);
        assertEquals("Incorrect value was returned.",
            30, Coordinate.convert(5, 5));
    }

    @Test(expected = BadSaveException.class)
    public void testDecode0() throws BadSaveException {
        Coordinate.decode("1");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFail2() throws BadSaveException {
        Coordinate.decode("1,1,1");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailStr() throws BadSaveException {
        Coordinate.decode("1,d");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailStr1() throws BadSaveException {
        Coordinate.decode("f,1");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailStr2() throws BadSaveException {
        Coordinate.decode("f,a");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos() throws BadSaveException {
        Coordinate.decode(",1");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos1() throws BadSaveException {
        Coordinate.decode("1,");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos2() throws BadSaveException {
        Coordinate.decode(",");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos3() throws BadSaveException {
        Coordinate.decode("");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos4() throws BadSaveException {
        Coordinate.decode(" ");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos5() throws BadSaveException {
        Coordinate.decode("1,1,");
    }
    @Test(expected = BadSaveException.class)
    public void testDecodeFailPos6() throws BadSaveException {
        Coordinate.decode(",1,1");
    }

    @Test
    public void decodeSuccessTest() {
        try {
            assertEquals(coordinate11, Coordinate.decode("1,1"));
        } catch (BadSaveException e) {
        }
    }

    @Test
    public void testDecodeBroad() {
        String message = "'decode()' should be able to correctly parse some values.";
        try {
            assertEquals(message, new Coordinate(), Coordinate.decode("0,0"));
            assertEquals(message, coordinate11, Coordinate.decode("1,1"));
            assertEquals(message, coordinateneg21, Coordinate.decode("-2,-1"));
        } catch (BadSaveException e) {
            fail(message + "\n " + e.getMessage());
        }
    }

    @Test
    public void testDistance() {
        assertEquals(new Coordinate(1, 0),coordinate11.distance(coordinate21));
        assertEquals(new Coordinate(-1,-1), coordinate11.distance(origin));
        assertEquals(new Coordinate(1, 1), origin.distance(coordinate11));
        assertEquals(new Coordinate(0, 0), coordinate11.distance(coordinate11Dup
        ));
    }


    @Test
    public void testTranslate() {
        assertEquals(new Coordinate(2, 2),coordinate11.translate(1,1));
        assertEquals(new Coordinate(1, 1),coordinate11.translate(0,0));
        assertEquals(new Coordinate(0, 2),coordinate11.translate(-1,1));
        assertEquals(new Coordinate(1, 2),origin.translate(1,2));
    }

    @Test
    public void testTranslateBroad() {
        assertEquals(new Coordinate(1, 1), new Coordinate().translate(1, 1));
        assertEquals(new Coordinate(1, 0), coordinate11.translate(0, -1));
        assertEquals(new Coordinate(6, 4), coordinate11.translate(5, 3));
    }

    @Test
    public void testEncode() {
        assertEquals("1,1",coordinate11.encode());
        assertEquals("0,0",origin.encode());
        assertEquals("-2,-1", coordinateneg21.encode());
    }

    @Test
    public void testHash() {
        assertEquals(coordinate11Dup.hashCode(),coordinate11.hashCode());
        assertEquals(origin.hashCode(),new Coordinate().hashCode());
    }

    @Test
    public void testNotEqualHash() {
        assertNotEquals(coordinate11.hashCode(),coordinate21.hashCode());
        assertNotEquals(coordinateneg21.hashCode(),coordinate21.hashCode());
        assertNotEquals(coordinateneg21.hashCode(),origin.hashCode());
    }

    @Test
    public void testEquals() {
        Scenario scenario = createSafeTestScenario("equals");
        assertEquals(coordinate11Dup, coordinate11);
        assertEquals(coordinate11, new Coordinate(1, 1));
        assertEquals(origin, new Coordinate());
        assertEquals(origin, origin);
        assertEquals(coordinate11, new Coordinate(6));
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(coordinate11, coordinate21);
        assertNotEquals(coordinate11, origin);
        assertNotEquals(origin, coordinate11);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(coordinate11, null);
        //assertNotEquals(coordinate11, null);
    }

    @Test
    public void testToString() {
        assertEquals("Incorrect string was returned.",
            "(1,1)", coordinate11.toString());
        assertEquals("Incorrect string was returned.",
            "(0,0)", origin.toString());
        assertEquals("Incorrect string was returned.",
            "(-2,-1)", coordinateneg21.toString());
    }

    /**
     * Creates a new scenario and adds it to the scenario manager.
     * The scenario created has a 5x5 map of LAND. A Seed of 0.
     *
     * @param name of the scenario
     * @return generated scenario
     * @see #createSafeTestScenario(String, TileType[])
     */
    public static Scenario createSafeTestScenario(String name) {
        return createSafeTestScenario(name, new TileType[] {
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND
        });
    }

    /**
     * Creates a new scenario and adds it to the scenario manager.
     * The scenario created has a 5x5 map with the array of tiles based on the array provided. A
     * Seed of 0.
     *
     * @param name  of the scenario
     * @param tiles the map of the scenario
     * @return generated scenario
     * @see #createSafeTestScenario(String, TileType[], int, int)
     */
    public static Scenario createSafeTestScenario(String name, TileType[] tiles) {
        return createSafeTestScenario(name, tiles, 5, 5);
    }

    /**
     * Creates a new scenario and adds it to the scenario manager.
     * The scenario created has an n x m map with the array of LAND tiles. A
     * Seed of 0.
     *
     * @param name  of the scenario
     * @param width  the width of the scenario
     * @param height the height of the scenario
     * @return generated scenario
     * @see #createSafeTestScenario(String, TileType[], int, int)
     */
    public static Scenario createSafeTestScenario(String name, int width, int height) {
        int size = width * height;
        TileType[] tiles = new TileType[size];
        Arrays.fill(tiles,0,size,TileType.LAND);
        return createSafeTestScenario(name, tiles, width, height);
    }

    /**
     * Creates a new scenario and adds it to the scenario manager.
     * The scenario created has a n x m map with the array of tiles based on the array provided. A
     * Seed of 0.
     *
     * @param name   of the scenario
     * @param tiles  the map of the scenario
     * @param width  the width of the scenario
     * @param height the height of the scenario
     * @return generated scenario
     */
    public static Scenario createSafeTestScenario(String name, TileType[] tiles,
                                                  int width, int height) {
        Scenario s = new Scenario(name, width, height, 0);
        Tile[] map = Arrays.stream(tiles).map(Tile::new).toArray(Tile[]::new);
        try {
            s.setMapGrid(map);
        } catch (CoordinateOutOfBoundsException error) {
            fail("Failed to update a scenario map for test: " + name + "\n "
                + error.getMessage());
        }
        ScenarioManager.getInstance().addScenario(s);
        try {
            ScenarioManager.getInstance().setScenario(name);
        } catch (BadSaveException error) {
            fail("Failed to update a scenario map for test: " + name + "\n "
                + error.getMessage());
        }
        return s;
    }
}