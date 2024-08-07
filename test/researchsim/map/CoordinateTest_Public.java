package researchsim.map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import researchsim.entities.TestUtil;
import researchsim.scenario.ScenarioManager;
import researchsim.util.BadSaveException;

import static org.junit.Assert.*;


public class CoordinateTest {

    private Coordinate coordinate11;
    private Coordinate coordinateneg21;

    @Before
    public void setUp() throws Exception {
        coordinate11 = new Coordinate(1, 1);
        coordinateneg21 = new Coordinate(-2, -1);
    }

    @After
    public void tearDown() throws Exception {
        ScenarioManager.getInstance().reset();
    }

    /**
     * @ass2
     */
    @Deprecated
    @Test
    public void testDecode() {
        String message = "'decode()' should be able to correctly parse some values.";
        try {
            assertEquals(message, new Coordinate(), Coordinate.decode("0,0"));
            assertEquals(message, coordinate11, Coordinate.decode("1,1"));
            assertEquals(message, coordinateneg21, Coordinate.decode("-2,-1"));
        } catch (BadSaveException e) {
            fail(message + "\n " + e.getMessage());
        }
    }

    /**
     * @ass2
     */
    @Test
    @Deprecated
    public void testTranslate() {
        assertEquals(TestUtil.incorrectValueMsg,
            new Coordinate(1, 1), new Coordinate().translate(1, 1));
        assertEquals(TestUtil.incorrectValueMsg,
            new Coordinate(1, 0), coordinate11.translate(0, -1));
        assertEquals(TestUtil.incorrectValueMsg,
            new Coordinate(6, 4), coordinate11.translate(5, 3));
    }

    /**
     * @ass2
     */
    @Test
    @Deprecated
    public void testEqualsNull() {
        TestUtil.assertEqualsOverridden(Coordinate.class);
        try {
            assertNotEquals(TestUtil.equalsNullMsg, coordinate11, null);
        } catch (NullPointerException error) {
            fail(TestUtil.equalsNullMsg + "\n " + error.getMessage());
        }

    }
}