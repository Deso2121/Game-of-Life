//import agh.cs.gameOfLife.*;
//import agh.cs.gameOfLife.animal.Animal;
//import agh.cs.gameOfLife.constants.MapDirection;
//import agh.cs.gameOfLife.data.DataStorage;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class AnimalTest {
//    @Test
//    public void startingPosition() {
//        Animal animal = new Animal(new GrassField(5, 5, 5, new DataStorage()));
//        assertEquals(animal.getPosition(), new Vector2d(2, 2));
//        assertEquals(animal.getOrientation(), MapDirection.NORTH);
//    }
//
//    @Test
//    public void movement() {
//        Animal animal = new Animal(new GrassField(5, 5, 5, new DataStorage()));
//        String[] commands = {"r", "f", "forward", "f"};
//        animal.move(OptionsParser.parse(commands));
//        assertEquals(animal.getPosition(), new Vector2d(0, 2));
//        assertEquals(animal.getOrientation(), MapDirection.EAST);
//    }
//}
