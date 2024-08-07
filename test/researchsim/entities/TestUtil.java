package researchsim.entities;

import org.junit.Test;
import researchsim.map.Tile;
import researchsim.map.TileType;
import researchsim.scenario.Scenario;
import researchsim.scenario.ScenarioManager;
import researchsim.util.BadSaveException;
import researchsim.util.CoordinateOutOfBoundsException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.fail;

/**
 * Utility methods for assignment unit testing.
 */
public class TestUtil {

    public static final String incorrectValueMsg = "Incorrect value was returned.";
    public static final String hashcodeEqualMsg = "'hashcode()' should return true for "
        + "two instances "
        + "that are equal.";
    public static final String equalsTrapMsg = "'equals()' should " +
        "not always return true.";
    public static final String equalsNullMsg = "'equals()' should " +
        "not fail for a parameter that is null.";
    public static final String unmodifiableMsg = "Changes to the value returned should not affect"
        + " the original.";


    /**
     * Fails the current test if the given class has not overridden the Object#equals(Object)
     * method.
     *
     * @param clazz class to assert has overridden equals()
     */
    public static void assertEqualsOverridden(Class<?> clazz) {
        // equals() takes a single Object parameter
        Class<?>[] arguments = new Class[1];
        arguments[0] = Object.class;

        try {
            assertMethodOverridden(clazz, clazz.getMethod("equals", arguments));
        } catch (NoSuchMethodException ignored) {
            // equals will always exist either in Object or overridden in clazz
        }
    }

    /**
     * Fails the current test if the given class has not overridden the Object#hashCode() method.
     *
     * @param clazz class to assert has overridden hashCode()
     */
    public static void assertHashCodeOverridden(Class<?> clazz) {
        try {
            assertMethodOverridden(clazz, clazz.getMethod("hashCode"));
        } catch (NoSuchMethodException ignored) {
            // hashCode will always exist either in Object or overridden in clazz
        }
    }

    /**
     * Checks the given array of hash codes and fails the current test if they are all the same.
     * This indicates that the hashCode method simply returns a constant.
     *
     * @param hashCodes array of generated hash codes to check
     */
    public static void assertHashCodesNotAllSame(int[] hashCodes) {
        int current = hashCodes[0];
        for (int i = 1; i < hashCodes.length; ++i) {
            if (hashCodes[i] != current) {
                return;
            }
        }
        // All hash codes have been the same, hashCode is probably hardcoded to return a constant
        fail("hashCode() should not just return a constant value");
    }

    /**
     * Fails the current test if the given class has not overridden the given method.
     *
     * @param clazz  class to check for overriding method
     * @param method method that must be overridden in clazz
     */
    private static void assertMethodOverridden(Class<?> clazz, Method method) {
        if (method.getDeclaringClass() != clazz) {
            fail(method.getName() + " has not been overridden in " + clazz.getSimpleName()
                + ", so cannot be tested.");
        }
    }

    /**
     * Resets the value of a static field in a class to its default value
     * <p>
     * The field will be in the same accessibility after the method as before.
     *
     * @param clazz     class with the field to reset
     * @param fieldName name of the field that needs to be reset
     * @require The field given must be static in nature
     */
    public static void resetStaticField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            // check if the field is accessible
            boolean access = field.canAccess(null);
            if (!access) {
                field.setAccessible(true);
            }
            Class<?> fieldClass = field.get(null).getClass();
            field.set(null, fieldClass.getDeclaredConstructor().newInstance());
            // set to inaccessible if required
            if (!access) {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException ignored) {
            // No such field so no need to reset
        } catch (IllegalAccessException e) { // For some reason we can't access
            System.out.println("An unexpected error occurred when trying to " +
                "access the field: " + e);
            System.exit(1);
        } catch (NoSuchMethodException e) { // No constructor ??
            System.out.println("An unexpected error occurred when trying to " +
                "get the constructor for the field: " + e);
            System.exit(1);
        } catch (InstantiationException e) { // something happened with new
            System.out.println("An unexpected error occurred when trying to " +
                "instantiate a new object for the field: " + e);
            System.exit(1);
        } catch (InvocationTargetException e) { // ???
            System.out.println("An unexpected error occurred when trying to " +
                "change the field: " + e);
            System.exit(1);
        }
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

    // Dummy test that always passes to avoid "No runnable methods" error for this class
    @Test
    public void dummyTest() {
    }

}
