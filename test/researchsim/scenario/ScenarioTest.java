package researchsim.scenario;

import org.junit.After;
import org.junit.Test;
import researchsim.entities.Entity;
import researchsim.entities.Size;
import researchsim.map.Coordinate;
import researchsim.util.BadSaveException;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;


public class ScenarioTest {

    @After
    public void tearDown() throws Exception {
        ScenarioManager.getInstance().reset();
    }

    /**
     * @ass2
     */
    public void testLoadBasicPass() {
        String encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        Scenario s = null;
        try {
            s = Scenario.load(new StringReader(encoding));
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException e) {
            fail("Scenario#Load should not throw an exception for a valid Reader.");
        }
        assertEquals("You must be able to correctly read a simple file before getting marks for "
                + "this section",
            TestUtil.createSafeTestScenario(
                "testLoadBasic", 5, 5), s);
        assertNotEquals("You must be able to correctly read a simple file before getting marks for "
            + "this section", TestUtil.createSafeTestScenario("testLoadBasic2", 6, 6), s);
    }

    @Test
    public void encodeTest() {
        Scenario scenario = TestUtil.createSafeTestScenario("encode");
        String encoding =
                "encode" + System.lineSeparator() +
                        "Width:5" + System.lineSeparator() +
                        "Height:5" + System.lineSeparator() +
                        "Seed:0" + System.lineSeparator() +
                        "=====" + System.lineSeparator() +
                        "LLLLL" + System.lineSeparator() +
                        "LLLLL" + System.lineSeparator() +
                        "LLLLL" + System.lineSeparator() +
                        "LLLLL" + System.lineSeparator() +
                        "LLLLL" + System.lineSeparator() +
                        "=====";
        assertEquals(encoding, scenario.encode());
    }
    /**
     * @ass2
     */
    @Test
    @Deprecated
    public void testLoadBasic() {
        String encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        Scenario s = null;
        try {
            s = Scenario.load(new StringReader(encoding));
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException e) {
            fail("Scenario#Load should not throw an exception for a valid Reader.");
        }
        assertEquals(TestUtil.createSafeTestScenario("testLoadBasic", 5, 5), s);
        assertNotEquals(TestUtil.createSafeTestScenario("testLoadBasic2", 6, 6), s);
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:-1" + System.lineSeparator() +
                "Height:-1" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        s = null;
        try {
            s = Scenario.load(new StringReader(encoding));
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException e) {
            fail("Scenario#Load should not throw an exception for a valid Reader.");
        }
        assertEquals(TestUtil.createSafeTestScenario("testLoadBasic", 5, 5), s);
        assertNotEquals(TestUtil.createSafeTestScenario("testLoadBasic3", 6, 6), s);
    }

    /**
     * @ass2 Key: Value pairs
     */
    @Deprecated
    @Test(timeout = 100000 + 2) // 2x weighting
    public void testLoadAdvancedSet2() {
        testLoadBasicPass();
        String encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5:" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should throw an exception for a reader with extra colons's.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width::5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with extra colons's");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "asd:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width : 5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:abc" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:-5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5:" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should throw an exception for a reader with extra colon's.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height::5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with extra colons.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "asd:5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height : 5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:abc" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:-5" + System.lineSeparator() +
                "Seed:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:0:" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should throw an exception for a reader with extra colon's.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed::0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with extra colons.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "asd:0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed : 0" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:asd" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
        encoding =
            "testLoadBasic" + System.lineSeparator() +
                "Width:5" + System.lineSeparator() +
                "Height:5" + System.lineSeparator() +
                "Seed:-999" + System.lineSeparator() +
                "=====" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "LLLLL" + System.lineSeparator() +
                "=====";
        try {
            Scenario.load(new StringReader(encoding));
            fail("Scenario#Load should not throw an exception for a reader with bad attributes.");
        } catch (IOException ignored) {
            // not possible
        } catch (BadSaveException expected) {
            // expected
        }
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