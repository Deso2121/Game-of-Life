package agh.cs.gameOfLife.data;
import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONParser {

    public static Integer[] readConfig(String confPath) throws IOException {
        String content = Files.readString(Paths.get(confPath));
        JSONObject obj = new JSONObject(content);
        return new Integer[]{
                obj.getInt("width"),
                obj.getInt("height"),
                obj.getInt("numberOfPlants"),
                obj.getInt("plantsEnergy"),
                obj.getInt("startEnergy"),
                obj.getInt("jungleRatio"),
                obj.getInt("numberOfAnimals"),
                obj.getInt("moveEnergy"),
                obj.getInt("speed"),
                obj.getInt("numberOfSimulations"),
                obj.getInt("dayWhenDataSaved")
        };
    }
}
