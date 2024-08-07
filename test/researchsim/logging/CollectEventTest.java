package researchsim.logging;

import org.junit.Test;
import researchsim.entities.Entity;
import researchsim.entities.Size;
import researchsim.entities.User;
import researchsim.map.Coordinate;

import static org.junit.Assert.assertEquals;

public class CollectEventTest {

    @Deprecated
    @Test
    public void testToString() {
        Entity target = new DummyEntity(Size.SMALL, new Coordinate());
        User user = new User(new Coordinate(1, 1), "BOB");
        assertEquals(
            "BOB [User] at (1,1)" + System.lineSeparator() + "COLLECTED" + System.lineSeparator()
                + "TEST [DummyEntity] at (0,0)" + System.lineSeparator() + "-".repeat(5),
            new CollectEvent(user, target).toString());
        assertEquals(
            "BOB [User] at (1,1)" + System.lineSeparator() + "COLLECTED" + System.lineSeparator()
                + "BOB [User] at (1,1)" + System.lineSeparator() + "-".repeat(5),
            new CollectEvent(user, user).toString());
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