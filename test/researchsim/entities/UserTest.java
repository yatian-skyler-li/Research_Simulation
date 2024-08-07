package researchsim.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import researchsim.logging.CollectEvent;
import researchsim.logging.Event;
import researchsim.logging.Logger;
import researchsim.logging.MoveEvent;
import researchsim.map.Coordinate;
import researchsim.map.Tile;
import researchsim.map.TileType;
import researchsim.scenario.AnimalController;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.BadSaveException;
import researchsim.util.CoordinateOutOfBoundsException;
import researchsim.util.NoSuchEntityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class UserTest {
    private User user1;
    private User user2;
    private User user2same;
    private User userNoName;


    @Before
    public void setUp() {
        user1 = new User(new Coordinate(), "Alan");
        userNoName = new User(new Coordinate(), "");
        user2 = new User(new Coordinate(1, 1),"Bob");
        user2same = new User(new Coordinate(1, 1),"Bob");
    }

    @Test
    public void constructorTest() {
        assertEquals(Size.MEDIUM, user1.getSize());
        assertEquals(Size.MEDIUM, user2.getSize());
        assertEquals(Size.MEDIUM, userNoName.getSize());
    }

    @Test
    public void getNameTest() {
        assertEquals("Incorrect name was returned.", "Alan", user1.getName());
        assertEquals("Incorrect name was returned.", "Bob", user2.getName());
        assertEquals("Incorrect name was returned.", "Bob", user2same.getName());
        assertEquals("", userNoName.getName());
    }

    @Test
    public void encodeTest() {
        assertEquals("Incorrect encode was returned.", "User-0,0-Alan", user1.encode());
        assertEquals("Incorrect encode was returned.", "User-1,1-Bob", user2.encode());
        assertEquals("Incorrect encode was returned.", "User-1,1-Bob", user2same.encode());
        assertEquals("Incorrect encode was returned.", "User-0,0-", userNoName.encode());
    }

    @Test
    public void hashCodeTest() {
        assertEquals("Incorrect hashCode was returned.", user2.hashCode(), user2same.hashCode());
        assertEquals(user1.hashCode(), new User(new Coordinate(), "Alan").hashCode());
    }

    @Test
    public void notEqualsHash() {
        assertNotEquals(user1, null);
        assertNotEquals(user1, user2);
        assertNotEquals(null, user2);
        assertNotEquals(user1, userNoName);
    }

    @Test
    public void equalsTest() {
        assertEquals("Incorrect equals check was returned.", user2, user2same);
        assertEquals(user1, new User(new Coordinate(), "Alan"));
    }

    @Test
    public void notEqualsTest() {
        assertNotEquals("Incorrect equals check was returned.", null, user2);
        assertNotEquals("Incorrect equals check was returned.", user1, null);
        assertNotEquals("Incorrect equals check was returned.", user2, user1);
        assertNotEquals(user1, userNoName);
    }

    @Test
    public void possibleMovesTest() {
        List<Coordinate> possibleMoves = new ArrayList<>();
        possibleMoves.add(new Coordinate(0,0));
        possibleMoves.add(new Coordinate(0,1));
        possibleMoves.add(new Coordinate(0,2));
        possibleMoves.add(new Coordinate(0,3));
        possibleMoves.add(new Coordinate(1,0));
        possibleMoves.add(new Coordinate(1,2));
        possibleMoves.add(new Coordinate(1,3));
        possibleMoves.add(new Coordinate(1,4));
        possibleMoves.add(new Coordinate(2,0));
        possibleMoves.add(new Coordinate(2,1));
        possibleMoves.add(new Coordinate(2,2));
        possibleMoves.add(new Coordinate(2,3));
        possibleMoves.add(new Coordinate(3,0));
        possibleMoves.add(new Coordinate(3,1));
        possibleMoves.add(new Coordinate(3,2));
        possibleMoves.add(new Coordinate(4,1));

        Scenario scenario = createSafeTestScenario("possible move");
        assertEquals("Incorrect possible moves was returned.", possibleMoves, user2.getPossibleMoves());
        assertTrue(possibleMoves.size() == user2.getPossibleMoves().size() && possibleMoves.containsAll(user2.getPossibleMoves()) && user2.getPossibleMoves().containsAll(
                possibleMoves));
        possibleMoves.add(new Coordinate(4, 3));
        assertNotEquals(possibleMoves, user2.getPossibleMoves());
    }

    @Test
    public void testGetPossibleMovesBasic() {
        User user1 = new User(new Coordinate(), "basic1");
        User user2 = new User(new Coordinate(1, 1), "basic2");
        Scenario s = createSafeTestScenario("testGetPossibleMovesBasic", new TileType[] {
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.LAND, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
        });
        s.getMapGrid()[12].setContents(user1);
        user1.setCoordinate(new Coordinate(2, 2));
        s.getMapGrid()[5].setContents(user2);
        user2.setCoordinate(new Coordinate(0, 1));
        List<Coordinate> expected = List.of(
                new Coordinate(0, 1),
                new Coordinate(2, 0),
                new Coordinate(2, 1),
                new Coordinate(0, 2), new Coordinate(1, 2),
                new Coordinate(3, 2), new Coordinate(4, 2),
                new Coordinate(2, 3),
                new Coordinate(2, 4)
        );
        List<Coordinate> actual = user1.getPossibleMoves();
        String message = "Incorrect value returned:" + System.lineSeparator()
                + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
                + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
                + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
                expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                        expected));
        expected = List.of(
                new Coordinate(0, 2),
                new Coordinate(1, 2),
                new Coordinate(2, 2)
        );
        actual = user2.getPossibleMoves();
        message = "Incorrect value returned:" + System.lineSeparator()
                + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
                + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
                + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
                expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                        expected));
    }

    @Test
    public void testGetPossibleMoves1() {
        User user1 = new User(new Coordinate(), "basic1");
        User user2 = new User(new Coordinate(1, 1), "basic2");
        Scenario s = createSafeTestScenario("testGetPossibleMovesBasic", new TileType[]{
                TileType.LAND, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
                TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN, TileType.OCEAN,
        });

        s.getMapGrid()[5].setContents(user2);
        user2.setCoordinate(new Coordinate(0, 0));
        List<Coordinate> expected = List.of();
        List<Coordinate> actual = user2.getPossibleMoves();
        String message = "Incorrect value returned:" + System.lineSeparator()
                + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
                + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
                + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
                expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                        expected));
    }

    @Test
    public void testGetPossibleMoves2() {
        User user2 = new User(new Coordinate(3, 3), "basic2");
        Scenario s = new Scenario("test", 7, 7, 0);
        TileType[] tiles = new TileType[]{
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.MOUNTAIN, TileType.LAND, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
        };
        Tile[] map = Arrays.stream(tiles).map(Tile::new).toArray(Tile[]::new);
        try {
            s.setMapGrid(map);
        } catch (CoordinateOutOfBoundsException e) {
            //
        }
        s.getMapGrid()[24].setContents(user2);
        ScenarioManager.getInstance().addScenario(s);
        List<Coordinate> expected = List.of(
                new Coordinate(2,1),
                 new Coordinate(1,2), new Coordinate(2,2),
                new Coordinate(0,3), new Coordinate(1,3), new Coordinate(2,3),
                 new Coordinate(1,4), new Coordinate(3,4), new Coordinate(4, 4),new Coordinate(5,4),
                 new Coordinate(2, 5), new Coordinate(3,5), new Coordinate(4, 5), new Coordinate(3, 6)
        );
        List<Coordinate> actual = user2.getPossibleMoves();
        String message = "Incorrect value returned:" + System.lineSeparator()
                + "Expected: " + System.lineSeparator() + expected + System.lineSeparator()
                + "But got: " + System.lineSeparator() + actual + System.lineSeparator()
                + "NOTE: The order the items appear does not matter, only that the items match";
        assertTrue(message,
                expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(
                        expected));
    }

    @Test
    public void moveEmptyTest() {
        Scenario scenario = createSafeTestScenario("move");
        scenario.getMapGrid()[0].setContents(user1);
        MoveEvent moveEvent = new MoveEvent(user1, new Coordinate(0,2));
        user1.move(new Coordinate(0,2));
        List<Event> events = scenario.getLog().getEvents();
        assertFalse(scenario.getMapGrid()[0].hasContents());
        try {
            assertEquals(null, scenario.getMapGrid()[0].getContents());
            assertEquals(user1, scenario.getMapGrid()[10].getContents());
        } catch (NoSuchEntityException e) {
            assertFalse(scenario.getMapGrid()[0].hasContents());
            assertTrue(scenario.getMapGrid()[10].hasContents());
        }
        assertEquals(new Coordinate(0,2), user1.getCoordinate());
            assertEquals(events.get(0).getInitialCoordinate(), moveEvent.getInitialCoordinate());
            assertEquals(events.get(0).getCoordinate(), moveEvent.getCoordinate());
            assertEquals(events.get(0).getEntity(), moveEvent.getEntity());
    }

    @Test
    public void moveFloraTest() {
        Scenario scenario = createSafeTestScenario("move");
        Flora flower = new Flora(Size.SMALL, new Coordinate(1,0));
        scenario.getMapGrid()[1].setContents(flower);
        scenario.getMapGrid()[0].setContents(user1);
        MoveEvent moveEvent = new MoveEvent(user1, new Coordinate(1,0));
        CollectEvent collectEvent = new CollectEvent(user1, flower);
        user1.move(new Coordinate(1,0));

        List<Event> events = scenario.getLog().getEvents();
        assertEquals(2, events.size());
        assertEquals(events.get(0).getInitialCoordinate(), moveEvent.getInitialCoordinate());
        assertEquals(events.get(0).getCoordinate(), moveEvent.getCoordinate());
        assertEquals(events.get(0).getEntity(), moveEvent.getEntity());
        assertEquals(events.get(1).getInitialCoordinate(), collectEvent.getInitialCoordinate());
        assertEquals(events.get(1).getCoordinate(), collectEvent.getCoordinate());
        assertEquals(events.get(1).getEntity(), collectEvent.getEntity());

        try {
            assertEquals(user1, scenario.getMapGrid()[1].getContents());
        } catch (NoSuchEntityException e) {
        }
        assertFalse(scenario.getMapGrid()[0].hasContents());
    }

    @Test
    public void moveFaunaTest() {
        Scenario scenario = createSafeTestScenario("move");
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(1, 0), TileType.LAND);
        Tile[] update = scenario.getMapGrid();
        update[1].setContents(animal);
        update[0].setContents(user1);
        AnimalController animalController = scenario.getController();
        animalController.addAnimal(animal);
        assertTrue(animalController.getAnimals().contains(animal));
        MoveEvent moveEvent = new MoveEvent(user1, new Coordinate(1,0));
        CollectEvent collectEvent = new CollectEvent(user1, animal);
        user1.move(new Coordinate(1,0));

        List<Event> events = scenario.getLog().getEvents();
        assertEquals(events.get(0).getInitialCoordinate(), moveEvent.getInitialCoordinate());
        assertEquals(events.get(0).getCoordinate(), moveEvent.getCoordinate());
        assertEquals(events.get(0).getEntity(), moveEvent.getEntity());
        assertEquals(events.get(1).getInitialCoordinate(), collectEvent.getInitialCoordinate());
        assertEquals(events.get(1).getCoordinate(), collectEvent.getCoordinate());
        assertEquals(events.get(1).getEntity(), collectEvent.getEntity());

        try {
            assertEquals(user1, scenario.getMapGrid()[1].getContents());
        } catch (NoSuchEntityException e) {
        }
        assertFalse(scenario.getMapGrid()[0].hasContents());
        assertFalse(animalController.getAnimals().contains(animal));
    }

    @Test
    public void canMoveTest() {
        Scenario scenario = createSafeTestScenario("canMove");
        try {
            assertTrue("Wrong can move status", user1.canMove(new Coordinate(0, 3)));
            assertFalse("Wrong can move status", user1.canMove(new Coordinate(-1, -1)));
            assertFalse("Wrong can move status", user1.canMove(new Coordinate(0, 8)));
            assertTrue("Wrong can move status", user1.canMove(new Coordinate(3, 1)));
            assertFalse(user1.canMove(new Coordinate(4,1)));
        } catch (CoordinateOutOfBoundsException e) {
        }
    }

    @Test
    public void testCanMoveGS() {
        Scenario s = createSafeTestScenario("testCanMove", new TileType[] {
                TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.OCEAN,
                TileType.OCEAN, TileType.OCEAN, TileType.SAND, TileType.LAND, TileType.OCEAN,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.MOUNTAIN, TileType.LAND,
                TileType.LAND, TileType.OCEAN, TileType.OCEAN, TileType.LAND, TileType.OCEAN,
                TileType.OCEAN, TileType.OCEAN, TileType.OCEAN, TileType.OCEAN, TileType.OCEAN,
        });
        s.getMapGrid()[12].setContents(user1);
        user1.setCoordinate(new Coordinate(2, 2));
        try {
            assertFalse(user1.canMove(new Coordinate(0, 0)));
            assertTrue(user1.canMove(new Coordinate(1, 0)));
            assertTrue(user1.canMove(new Coordinate(2, 0)));
            assertFalse(user1.canMove(new Coordinate(3, 0)));
            assertFalse(user1.canMove(new Coordinate(4, 0)));
            assertFalse(user1.canMove(new Coordinate(0, 1)));
            assertFalse(user1.canMove(new Coordinate(1, 1)));
            assertTrue(user1.canMove(new Coordinate(2, 1)));
            assertTrue(user1.canMove(new Coordinate(3, 1)));
            assertFalse(user1.canMove(new Coordinate(4, 1)));
            assertTrue(user1.canMove(new Coordinate(0, 2)));
            assertTrue(user1.canMove(new Coordinate(1, 2)));
            assertFalse(user1.canMove(new Coordinate(2, 2)));
            assertFalse(user1.canMove(new Coordinate(3, 2)));
            assertFalse(user1.canMove(new Coordinate(4, 2)));
            assertTrue(user1.canMove(new Coordinate(0, 3)));
            assertFalse(user1.canMove(new Coordinate(1, 3)));
            assertFalse(user1.canMove(new Coordinate(2, 3)));
            assertFalse(user1.canMove(new Coordinate(3, 3)));
            assertFalse(user1.canMove(new Coordinate(4, 3)));
            assertFalse(user1.canMove(new Coordinate(0, 4)));
            assertFalse(user1.canMove(new Coordinate(1, 4)));
            assertFalse(user1.canMove(new Coordinate(2, 4)));
            assertFalse(user1.canMove(new Coordinate(3, 4)));
            assertFalse(user1.canMove(new Coordinate(4, 4)));
        } catch (CoordinateOutOfBoundsException error) {
            fail("Should not throw an exception if given Coordinate is in bounds: " + error);
        }
    }

    @Test
    public void testCanMove1() {
        Scenario s = createSafeTestScenario("testCanMove", new TileType[] {
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.SAND, TileType.OCEAN, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
        }, 7, 7);
        s.getMapGrid()[28].setContents(user1);
        user1.setCoordinate(new Coordinate(3, 3));
        try {

            assertFalse(user1.canMove(new Coordinate(0, 0)));
            assertFalse(user1.canMove(new Coordinate(1, 0)));
            assertTrue(user1.canMove(new Coordinate(2, 0)));
            assertFalse(user1.canMove(new Coordinate(3, 0)));
            assertTrue(user1.canMove(new Coordinate(4, 0)));
            assertFalse(user1.canMove(new Coordinate(5, 0)));
            assertFalse(user1.canMove(new Coordinate(6, 0)));

            assertFalse(user1.canMove(new Coordinate(0, 1)));
            assertTrue(user1.canMove(new Coordinate(1, 1)));
            assertTrue(user1.canMove(new Coordinate(2, 1)));
            assertFalse(user1.canMove(new Coordinate(3, 1)));
            assertTrue(user1.canMove(new Coordinate(4, 1)));
            assertFalse(user1.canMove(new Coordinate(5, 1)));
            assertFalse(user1.canMove(new Coordinate(6, 1)));

            assertTrue(user1.canMove(new Coordinate(0, 2)));
            assertTrue(user1.canMove(new Coordinate(1, 2)));
            assertTrue(user1.canMove(new Coordinate(2, 2)));
            assertFalse(user1.canMove(new Coordinate(3, 2)));
            assertTrue(user1.canMove(new Coordinate(4, 2)));
            assertFalse(user1.canMove(new Coordinate(5, 2)));
            assertFalse(user1.canMove(new Coordinate(6, 2)));

            assertTrue(user1.canMove(new Coordinate(0, 3)));
            assertTrue(user1.canMove(new Coordinate(1, 3)));
            assertTrue(user1.canMove(new Coordinate(2, 3)));
            assertFalse(user1.canMove(new Coordinate(3, 3)));
            assertTrue(user1.canMove(new Coordinate(4, 3)));
            assertFalse(user1.canMove(new Coordinate(5, 3)));
            assertFalse(user1.canMove(new Coordinate(6, 3)));

            assertTrue(user1.canMove(new Coordinate(0, 4)));
            assertTrue(user1.canMove(new Coordinate(1, 4)));
            assertFalse(user1.canMove(new Coordinate(2, 4)));
            assertTrue(user1.canMove(new Coordinate(3, 4)));
            assertTrue(user1.canMove(new Coordinate(4, 4)));
            assertTrue(user1.canMove(new Coordinate(5, 4)));
            assertTrue(user1.canMove(new Coordinate(6, 4)));

            assertFalse(user1.canMove(new Coordinate(0, 5)));
            assertTrue(user1.canMove(new Coordinate(1, 5)));
            assertTrue(user1.canMove(new Coordinate(2, 5)));
            assertTrue(user1.canMove(new Coordinate(3, 5)));
            assertTrue(user1.canMove(new Coordinate(4, 5)));
            assertTrue(user1.canMove(new Coordinate(5, 5)));
            assertFalse(user1.canMove(new Coordinate(6, 5)));


            assertFalse(user1.canMove(new Coordinate(0, 6)));
            assertFalse(user1.canMove(new Coordinate(1, 6)));
            assertTrue(user1.canMove(new Coordinate(2, 6)));
            assertTrue(user1.canMove(new Coordinate(3, 6)));
            assertTrue(user1.canMove(new Coordinate(4, 6)));
            assertFalse(user1.canMove(new Coordinate(5, 6)));
            assertFalse(user1.canMove(new Coordinate(6, 6)));

        } catch (CoordinateOutOfBoundsException error) {
            fail("Should not throw an exception if given Coordinate is in bounds: " + error);
        }
    }

    @Test (expected = CoordinateOutOfBoundsException.class)
    public void testCanMove2() throws CoordinateOutOfBoundsException {
        Scenario s = createSafeTestScenario("testCanMove", new TileType[] {
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.SAND, TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.SAND, TileType.OCEAN, TileType.LAND,
                TileType.SAND, TileType.SAND, TileType.OCEAN, TileType.SAND, TileType.SAND, TileType.SAND, TileType.SAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
                TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND, TileType.LAND,
        }, 7, 7);
        s.getMapGrid()[28].setContents(user1);
        user1.setCoordinate(new Coordinate(3, 3));
            assertFalse(user1.canMove(new Coordinate(-1,-1)));
            assertFalse(user1.canMove(new Coordinate(-1,0)));
            assertFalse(user1.canMove(new Coordinate(-1,3)));
            assertFalse(user1.canMove(new Coordinate(0,-1)));
            assertFalse(user1.canMove(new Coordinate(6,7)));
            assertFalse(user1.canMove(new Coordinate(7,6)));
            assertFalse(user1.canMove(new Coordinate(7,7)));

            assertFalse(user1.canMove(new Coordinate(3, 7)));
            assertFalse(user1.canMove(new Coordinate(7, 3)));
    }

    @Test
    public void possibleCollectionTest() {
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(1,0), TileType.LAND);
        User uncollect = new User(new Coordinate(0, 1), "Dummy");
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[0].setContents(user1);
        update[1].setContents(animal);
        update[5].setContents(uncollect);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        List<Coordinate> collected = new ArrayList<>();
        collected.add(new Coordinate(1,0));
        assertEquals(collected, user1.getPossibleCollection());
    }

    @Test
    public void possibleCollectionRangeTest() {
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(1,0), TileType.LAND);
        User uncollect = new User(new Coordinate(0, 1), "Dummy");
        Fauna animal2 = new Fauna(Size.MEDIUM, new Coordinate(1,1), TileType.LAND);
        Scenario scenario = createSafeTestScenario("possibleCollect");
        scenario.getMapGrid()[0].setContents(user1);
        scenario.getMapGrid()[1].setContents(animal);
        scenario.getMapGrid()[5].setContents(uncollect);
        scenario.getMapGrid()[6].setContents(animal2);
        List<Coordinate> collected = new ArrayList<>();
        collected.add(new Coordinate(1,0));
        assertEquals(collected, user1.getPossibleCollection());
    }

    @Test
    public void possibleCollectionBoundTest() {
        user1.setCoordinate(new Coordinate(4, 3));
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(5,3), TileType.LAND);
        Fauna animal2 = new Fauna(Size.MEDIUM, new Coordinate(3,3), TileType.LAND);
        Scenario scenario = createSafeTestScenario("possibleCollect");
        scenario.getMapGrid()[19].setContents(user1);
        scenario.getMapGrid()[20].setContents(animal);
        scenario.getMapGrid()[18].setContents(animal2);
        List<Coordinate> collected = new ArrayList<>();
        collected.add(new Coordinate(3,3));
        assertEquals(collected, user1.getPossibleCollection());
    }

    @Test
    public void possibleCollectionEmptyFaunaTest() {
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(1,0), TileType.LAND);
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[6].setContents(user2);
        update[1].setContents(animal);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        List<Coordinate> collected = new ArrayList<>();
        collected.add(new Coordinate(1,0));
        assertEquals(collected, user2.getPossibleCollection());
    }

    @Test
    public void possibleCollectionFloraTest() {
        Flora flower = new Flora(Size.MEDIUM, new Coordinate(1,0));
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[6].setContents(user2);
        update[1].setContents(flower);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        List<Coordinate> collected = new ArrayList<>();
        collected.add(new Coordinate(1,0));
        assertEquals(collected, user2.getPossibleCollection());
    }

    @Test
    public void possibleCollectionEmptyUserTest() {
        User dummy = new User(new Coordinate(1, 0), "DummyYou");
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[1].setContents(dummy);
        update[6].setContents(user2);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        List<Coordinate> collected = new ArrayList<>();
        assertEquals(collected, user2.getPossibleCollection());
    }

    @Test
    public void possibleCollectionEmptyTest() {
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[6].setContents(user2);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        List<Coordinate> collected = new ArrayList<>();
        assertEquals(collected, user2.getPossibleCollection());
    }

    @Test
    public void collectFaunaTest() {
        Fauna animal = new Fauna(Size.MEDIUM, new Coordinate(1,0), TileType.LAND);
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[1].setContents(animal);
        update[0].setContents(user1);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        try {
            user1.collect(new Coordinate(1,0));
        } catch (NoSuchEntityException | CoordinateOutOfBoundsException e) {
        }
        try {
            assertEquals(null, scenario.getMapGrid()[5].getContents());
        } catch (NoSuchEntityException e) {
        }

        List<Event> events = scenario.getLog().getEvents();
        assertTrue(events.size() > 0);
        assertFalse(scenario.getController().getAnimals().contains(animal));
    }


    @Test
    public void collectFloraTest() {
        Flora flora = new Flora(Size.MEDIUM, new Coordinate(1,0));
        Scenario scenario = createSafeTestScenario("possibleCollect");
        Tile[] update = scenario.getMapGrid();
        update[1].setContents(flora);
        update[0].setContents(user1);
        try {
            scenario.setMapGrid(update);
        } catch (CoordinateOutOfBoundsException e) {
        }
        try {
            user1.collect(new Coordinate(1,0));
        } catch (NoSuchEntityException | CoordinateOutOfBoundsException e) {
        }
        try {
            assertEquals(null, scenario.getMapGrid()[5].getContents());
        } catch (NoSuchEntityException e) {
        }

        List<Event> events = scenario.getLog().getEvents();
        assertTrue(events.size() > 0);
    }

    @Test(expected = NoSuchEntityException.class)
    public void noEntityTest() throws CoordinateOutOfBoundsException, NoSuchEntityException {
        Scenario scenario = createSafeTestScenario("noEntity");
        scenario.getMapGrid()[0].setContents(user1);
        user1.collect(new Coordinate(2,2));
    }
    @Test(expected = CoordinateOutOfBoundsException.class)
    public void outBoundTest() throws CoordinateOutOfBoundsException, NoSuchEntityException {
        Flora flora = new Flora(Size.MEDIUM, new Coordinate(1,0));
        Scenario scenario = createSafeTestScenario("noEntity");
        Tile[] update = scenario.getMapGrid();
        update[15].setContents(flora);
        update[0].setContents(user1);
        scenario.setMapGrid(update);
        user1.collect(new Coordinate(5,2));
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
        Scenario s = new Scenario(name, width, height,0 );
        Tile[] map = Arrays.stream(tiles).map(Tile::new).toArray(Tile[]::new);
//        System.out.println(map.length);
//        System.out.println(s.getSize());
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