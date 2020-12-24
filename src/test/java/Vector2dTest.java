//
//
//import agh.cs.gameOfLife.Vector2d;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class Vector2dTest {
//
//    Vector2d vector1;
//    Vector2d vector2;
//
//    @Before
//    public void setUp() throws Exception {
//        vector1 = new Vector2d(1,2);
//        vector2 = new Vector2d(2,3);
//    }
//
//    @Test
//    public void precedes() {
//        assertTrue(vector1.precedes(vector2));
//        assertFalse(vector2.precedes(vector1));
//    }
//
//    @Test
//    public void follows() {
//        assertTrue(vector2.follows(vector1));
//        assertFalse(vector1.follows(vector2));
//    }
//
//    @Test
//    public void upperRight() {
//        assertEquals(vector1.upperRight(vector2), new Vector2d(2,3));
//    }
//
//    @Test
//    public void lowerLeft() {
//        assertEquals(vector1.lowerLeft(vector2), new Vector2d(1,2));
//    }
//
//    @Test
//    public void add() {
//        assertEquals(vector1.add(vector2), new Vector2d(3, 5));
//    }
//
//    @Test
//    public void subtract() {
//        assertEquals(vector1.subtract(vector2), new Vector2d(-1, -1));
//    }
//
//    @Test
//    public void opposite() {
//        assertEquals(vector1.opposite(), new Vector2d(-1, -2));
//        assertEquals(vector2.opposite(), new Vector2d(-2, -3));
//    }
//
//    @Test
//    public void testEquals() {
//        assertTrue(vector1.equals(vector1));
//        assertFalse(vector1.equals(vector2));
//    }
//
//    @Test
//    public void testToString() {
//        assertEquals(vector1.toString(), "(1,2)");
//        assertEquals(vector2.toString(), "(2,3)");
//    }
//}
