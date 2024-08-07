package researchsim.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SizeTest {

    @Test
    @Deprecated
    public void testMoveDistance() {
        assertEquals(TestUtil.incorrectValueMsg, 4, Size.SMALL.moveDistance);
        assertEquals(TestUtil.incorrectValueMsg, 3, Size.MEDIUM.moveDistance);
        assertEquals(TestUtil.incorrectValueMsg, 2, Size.LARGE.moveDistance);
        assertEquals(TestUtil.incorrectValueMsg, 1, Size.GIANT.moveDistance);
    }
}