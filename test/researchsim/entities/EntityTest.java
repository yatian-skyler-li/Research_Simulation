package researchsim.entities;

import org.junit.Before;
import org.junit.Test;
import researchsim.map.Coordinate;

import static org.junit.Assert.*;

public class EntityTest {

    private DummyEntity entity1;
    private DummyEntity entity1Dup;

    @Before
    public void setUp() throws Exception {
        entity1 = new DummyEntity(Size.SMALL, new Coordinate());
        entity1Dup = new DummyEntity(Size.SMALL, new Coordinate());
    }

    /**
     * @ass2
     */
    @Test
    @Deprecated
    public void testHashCode() {
        TestUtil.assertHashCodeOverridden(Entity.class);
        assertHashCodeNotHardcoded();
        assertEquals(entity1.hashCode(), entity1Dup.hashCode());
    }

    @Test
    public void testEncode() {
        Flora flower = new Flora(Size.MEDIUM, new Coordinate());
        assertEquals("Flora-MEDIUM-0,0", flower.encode());
    }

    /**
     * @ass2
     */
    @Deprecated
    @Test
    public void testEqualsTrap() {
        TestUtil.assertEqualsOverridden(Entity.class);
        try {
            assertNotEquals(TestUtil.equalsTrapMsg, entity1, "Not an Entity instance");
        } catch (ClassCastException error) {
            fail(TestUtil.equalsTrapMsg + "\n " + error.getMessage());
        }
    }

    private void assertHashCodeNotHardcoded() {
        int[] hashCodes = new int[100];
        for (int i = 0; i < 100; ++i) {
            Coordinate coordinate = new Coordinate(i, i % 10);
            switch (i % 4) {
                case 0:
                    hashCodes[i] = new DummyEntity(Size.SMALL, coordinate).hashCode();
                    break;
                case 1:
                    hashCodes[i] = new DummyEntity(Size.MEDIUM, coordinate).hashCode();
                    break;
                case 2:
                    hashCodes[i] = new DummyEntity(Size.LARGE, coordinate).hashCode();
                    break;
                case 3:
                default:
                    hashCodes[i] = new DummyEntity(Size.GIANT, coordinate).hashCode();
                    break;
            }
        }
        TestUtil.assertHashCodesNotAllSame(hashCodes);
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