package researchsim.logging;

import org.junit.Before;
import org.junit.Test;
import researchsim.entities.Entity;
import researchsim.entities.Size;
import researchsim.entities.User;
import researchsim.map.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LoggerTest {

    private Event move1;
    private Event move2;
    private Event collect1;
    private Event collect2;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        Entity entity1 = new DummyEntity(Size.SMALL, new Coordinate(2, 3));
        Entity entity2 = new DummyEntity(Size.LARGE, new Coordinate());
        User user = new User(new Coordinate(), "BOB");

        move1 = new MoveEvent(entity1, new Coordinate());
        move2 = new MoveEvent(entity2, new Coordinate(2, 2));
        collect1 = new CollectEvent(user, entity1);
        collect2 = new CollectEvent(user, entity2);

        logger = new Logger();
    }

    @Test
    @Deprecated
    public void testGetEvents() {
        assertEquals(new ArrayList<>(), logger.getEvents());
        logger.add(move1);
        assertEquals(List.of(move1), logger.getEvents());
        logger.add(collect1);
        assertEquals(List.of(move1, collect1), logger.getEvents());
        logger.add(collect2);
        assertEquals(List.of(move1, collect1, collect2), logger.getEvents());
        logger.add(move2);
        assertEquals(List.of(move1, collect1, collect2, move2), logger.getEvents());
        logger.add(move2);
        assertEquals(List.of(move1, collect1, collect2, move2, move2), logger.getEvents());
    }

    private static class DummyEntity extends Entity {

        /**
         * DummyEntity doesn't require implementation of subclasses.
         */
        public DummyEntity(Size size, Coordinate coordinate) {
            super(size, coordinate);
        }

        @Override
        public String getName() {
            return "TEST";
        }
    }
}