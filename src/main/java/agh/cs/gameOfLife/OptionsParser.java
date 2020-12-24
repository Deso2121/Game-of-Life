package agh.cs.gameOfLife;

import agh.cs.gameOfLife.constants.MoveDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsParser {

    public static MoveDirection[] parse(String[] args) {
        List<MoveDirection> moves = new ArrayList<>();
        Arrays.stream(args)
                .forEach(argument -> {
                        switch (argument) {
                            case "f":
                            case "forward": {
                                moves.add(MoveDirection.FORWARD);
                                break;
                            }
                            case "r":
                            case "right": {
                                moves.add(MoveDirection.RIGHT);
                                break;
                            }
                            case "b":
                            case "backward": {
                                moves.add(MoveDirection.BACKWARD);
                                break;
                            }
                            case "l":
                            case "left": {
                                moves.add(MoveDirection.LEFT);
                                break;
                            }
                            default:
                                throw new IllegalArgumentException(argument + " is not a legal move specification");
                        }
                });
        return moves.toArray(new MoveDirection[0]);
    }
}
