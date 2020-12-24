//import agh.cs.gameOfLife.*;
//import agh.cs.gameOfLife.constants.MapDirection;
//import agh.cs.gameOfLife.constants.MoveDirection;
//import agh.cs.gameOfLife.data.DataStorage;
//import agh.cs.gameOfLife.engine.SimulationEngine;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class RectangularMapTest {
//
//    @Test
//    public void run_ObjectAt(){
//        String[] args = new String[] {"f","b","r","l","f","f","r","r","f","f","f","f","f","f","f","f"};
//        MoveDirection[] directions = OptionsParser.parse(args);
//        AbstractWorldMap map = new RectangularMap(10, 5, new DataStorage());
//        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
//        SimulationEngine engine = new SimulationEngine(directions, map, positions);
//        engine.run();
//        Vector2d[] finalPositions = {new Vector2d(2, 4), new Vector2d(3, 2)};
//        MapDirection[] finalOrientations = {MapDirection.SOUTH, MapDirection.NORTH};
////        Checking animals' positions and orientations
//        int a = 0;
////        for (Animal animal : map.getAnimals().values()) {
////            assertEquals(animal.getPosition(), finalPositions[a]);
////            assertEquals(animal.getOrientation(), finalOrientations[a]);
////            a += 1;
////        }
//    }
//
//    @Test
//    public void place() {
//        String[] args = new String[] {"f","b","r","l","f","f","r","r","f","f","f","f","f","f","f","f"};
//        MoveDirection[] directions = OptionsParser.parse(args);
//        AbstractWorldMap map = new RectangularMap(10, 5, new DataStorage());
//        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
//        SimulationEngine engine = new SimulationEngine(directions, map, positions);
//        int a = 0;
////        for (Animal animal : map.getAnimals().values()) {
////            assertEquals(animal.getPosition(), positions[a]);
////            a += 1;
////        }
//    }
//
//    @Test
//    public void isOccupied() {
//        String[] args = new String[] {"f","b","r","l","f","f","r","r","f","f","f","f","f","f","f","f"};
//        MoveDirection[] directions = OptionsParser.parse(args);
//        AbstractWorldMap map = new RectangularMap(10, 5, new DataStorage());
//        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
//        SimulationEngine engine = new SimulationEngine(directions, map, positions);
//        assertTrue(map.isOccupied(new Vector2d(2, 2)));
//    }
//}
