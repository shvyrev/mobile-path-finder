/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Algorithm.AStarPathFinder;
import Algorithm.DirectHeuristicCost;
import Algorithm.PathFinder;
import DataStructure.Coordinate;
import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.Path;
import Exceptions.WalkingDistanceError;

/**
 *
 * @author rajeevan
 */
public class Person implements Runnable {

    //coordinate properties
    private int currentX;
    private int currentY;
    private Direction direction;
    private int destX;
    private int destY;
    private Path way = new Path();  //this is used to store the walking history of the person
    private Map map;
    private PathFinder pf;
    private Navigator nav;
    private Coordinate receivedCoordinate;
    //properties of person
    private double maximumStepDist = 2;
    private double stepDeviationFactor = 0.5;

    //constructor 
    //input : starting position and destination position
    public Person(int startX, int startY, int destX, int destY, Direction startingDir, Coordinate c) {
        this.currentX = startX;
        this.currentY = startY;
        this.destX = destX;
        this.destY = destY;
        direction = startingDir;
        way.appendStep(startX, startY);

        this.map = ComponentsLib.map;
        this.pf = new AStarPathFinder(map.getHeight() * map.getWidth(), new DirectHeuristicCost(),map.getWidth(),map.getHeight());
        this.nav = new Navigator();
        this.receivedCoordinate=c;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMaximumStepDist(double maximumStepDist) {
        this.maximumStepDist = maximumStepDist;
    }

    public void setStepDeviationFactor(double stepDeviationFactor) {
        this.stepDeviationFactor = stepDeviationFactor;
    }

    public void updatePosition() throws WalkingDistanceError{
        int[] tempCoordinate = receivedCoordinate.getCoordinate();

        double distance = getDistance(tempCoordinate[0], tempCoordinate[1]);
        if ((distance <= (maximumStepDist * (1 + stepDeviationFactor))) && !map.isBlocked(tempCoordinate[0], tempCoordinate[1]).booleanValue()) {
            maximumStepDist = distance;
            direction = Direction.getDirection(currentX, currentY, tempCoordinate[0], tempCoordinate[1]);
            currentX = tempCoordinate[0];
            currentY = tempCoordinate[1];
            this.way.appendStep(currentX, currentY);

        } else {
            System.out.println("Person: Illegal coordinate arrived");
            throw new WalkingDistanceError();
        }
    }

    private double getDistance(int x, int y) {

        return Math.sqrt((x - currentX) * (x - currentX) + (y - currentY) * (y - currentY));

    }

    public String toString() {
        String s = "";
        for (int y = map.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getPath().contains(x, y)) {
                    s += "e";
                } else if (!way.contains(x, y)) {
                    s += map.getFloorPlan()[x][y].booleanValue() ? "_" : "o";
                } else {
                    s += "x";
                }
            }
            s += "\n";
        }
        return s;
    }

    public void run() {
        while (true) {
            try {
                updatePosition();
            } catch (WalkingDistanceError ex) {
                ComponentsLib.f.append(ex.toString());
                continue;
            }
            pf.findPath(currentX,currentY, destX, destY,map);
            System.out.println(nav.updateCommandDirection(direction, map.pathStartingDirection()));
            System.out.println(this);
        }
    }
}
