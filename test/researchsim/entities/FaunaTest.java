package researchsim.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import researchsim.map.Coordinate;
import researchsim.map.TileType;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.CoordinateOutOfBoundsException;

import java.util.List;

import static org.junit.Assert.*;

public class FaunaTest {

    private Fauna animal1;
    private Fauna animal3;


    @Before
    public void setUp() throws Exception {
        animal1 = new Fauna(Size.SMALL, new Coordinate(), TileType.LAND);
        animal3 = new Fauna(Size.GIANT, new Coordinate(1, 1), TileType.OCEAN);
    }

    @After
    public void tearDown() throws Exception {
        ScenarioManager.getInstance().reset();
    }

    /**
     * @ass2
     */
    @Test
    @Deprecated
    public void testGetPossibleMovesBasic() {
        Scenario s = TestUtil.createSafeTestScenario("testGetPossibleMovesBasic", new TileType[] {
            TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
            TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
            TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
            TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
        });
        s.getMapGrid()[12].setContents(animal1);
        animal1.setCoordinate(new Coordinate(2, 2));
        s.getMapGrid()[5].setContents(animal3);
        animal3.setCoordinate(new Coordinate(0, 1));
        List<Coordinate> expected = List.of(
            new Coordinate(2, 0),
            new Coordinate(2, 1),
            new Coordinate(0, 2), new Coordinate(1, 2),
            new Coordinate(3, 2), new Coordinate(4, 2),
            new Coordinate(2, 3),
            new Coordinate(2, 4)
        );
        List<Coordinate> actual = animal1.getPossibleMoves();
        String message = "Incorrect value returned:" + System.lineSeparator()
            + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
            + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
            + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
            expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                expected));
        expected = List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 1)
        );
        actual = animal3.getPossibleMoves();
        message = "Incorrect value returned:" + System.lineSeparator()
            + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
            + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
            + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
            expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                expected));

    }

    /**
     * @ass2
     */
    @Test(timeout = 100000 + 3) // 3x weighting
    @Deprecated
    public void testCanMove() {
        Scenario s = TestUtil.createSafeTestScenario("testCanMove", new TileType[] {
            TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.OCEAN,
            TileType.OCEAN, TileType.OCEAN, TileType.SAND, TileType.LAND, TileType.OCEAN,
            TileType.LAND, TileType.LAND, TileType.LAND, TileType.MOUNTAIN, TileType.LAND,
            TileType.LAND, TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN,
            TileType.OCEAN, TileType.OCEAN, TileType.OCEAN, TileType.OCEAN, TileType.OCEAN,
        });
        s.getMapGrid()[12].setContents(animal1);
        animal1.setCoordinate(new Coordinate(2, 2));
        try {
            assertFalse(animal1.canMove(new Coordinate(0, 0)));
            assertTrue(animal1.canMove(new Coordinate(1, 0)));
            assertTrue(animal1.canMove(new Coordinate(2, 0)));
            assertFalse(animal1.canMove(new Coordinate(3, 0)));
            assertFalse(animal1.canMove(new Coordinate(4, 0)));
            assertFalse(animal1.canMove(new Coordinate(0, 1)));
            assertFalse(animal1.canMove(new Coordinate(1, 1)));
            assertTrue(animal1.canMove(new Coordinate(2, 1)));
            assertTrue(animal1.canMove(new Coordinate(3, 1)));
            assertFalse(animal1.canMove(new Coordinate(4, 1)));
            assertTrue(animal1.canMove(new Coordinate(0, 2)));
            assertTrue(animal1.canMove(new Coordinate(1, 2)));
            assertFalse(animal1.canMove(new Coordinate(2, 2)));
            assertTrue(animal1.canMove(new Coordinate(3, 2)));
            assertTrue(animal1.canMove(new Coordinate(4, 2)));
            assertTrue(animal1.canMove(new Coordinate(0, 3)));
            assertFalse(animal1.canMove(new Coordinate(1, 3)));
            assertFalse(animal1.canMove(new Coordinate(2, 3)));
            assertTrue(animal1.canMove(new Coordinate(3, 3)));
            assertFalse(animal1.canMove(new Coordinate(4, 3)));
            assertFalse(animal1.canMove(new Coordinate(0, 4)));
            assertFalse(animal1.canMove(new Coordinate(1, 4)));
            assertFalse(animal1.canMove(new Coordinate(2, 4)));
            assertFalse(animal1.canMove(new Coordinate(3, 4)));
            assertFalse(animal1.canMove(new Coordinate(4, 4)));
        } catch (CoordinateOutOfBoundsException error) {
            fail("Should not throw an exception if given Coordinate is in bounds: " + error);
        }
    }
}