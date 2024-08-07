package researchsim.scenario;

import org.junit.Before;
import org.junit.Test;
import researchsim.entities.Fauna;
import researchsim.entities.Size;
import researchsim.map.Coordinate;
import researchsim.map.TileType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AnimalControllerTest {

    private AnimalController controller;
    private Fauna a1;
    private Fauna a2;

    @Before
    public void setUp() throws Exception {
        controller = new AnimalController();
        a1 = new Fauna(Size.SMALL,new Coordinate(2,2), TileType.LAND);
        a2 = new Fauna(Size.GIANT,new Coordinate(2,2), TileType.LAND);
    }

    @Deprecated
    @Test
    public void testGetAnimalsModifiable() {
        controller.addAnimal(a1);
        List<Fauna> ret = controller.getAnimals();
        assertEquals(List.of(a1),ret);
        ret.add(a2);
        assertNotEquals(TestUtil.unmodifiableMsg,ret,controller.getAnimals());
    }
}