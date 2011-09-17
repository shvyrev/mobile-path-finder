/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Exceptions.WalkingDistanceError;
import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.Path;

/**
 *
 * @author rajeevan
 */
public class Person {

    //coordinate properties
    private int currentX;
    private int currentY;
    private Direction direction;
    private int destX;
    private int destY;
    private Path way = new Path();  //this is used to store the walking history of the person
    private boolean updated;
    private Map map;
    //properties of person
    private double maximumStepDist = 1.5;
    private double stepDeviationFactor = 0.5;

    //constructor 
    //input : starting position and destination position
    public Person(int startX, int startY, int destX, int destY, Direction startingDir) {
        this.currentX = startX;
        this.currentY = startY;
        this.destX = destX;
        this.destY = destY;
        way.appendStep(startX, startY);
        this.updated = true;
        direction = startingDir;
        this.map = MapLib.map;
    }

    //Getterz and Setters
    public boolean isUpdated() {
        return updated;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getMaximumStepDist() {
        return maximumStepDist;
    }

    public void setMaximumStepDist(double maximumStepDist) {
        this.maximumStepDist = maximumStepDist;
    }

    public void setStepDeviationFactor(double stepDeviationFactor) {
        this.stepDeviationFactor = stepDeviationFactor;
    }

    public boolean updatePosition(int x, int y) throws WalkingDistanceError {
        synchronized (this) {
            double distance = getDistance(x, y);
            if ((distance <= (maximumStepDist * (1 + stepDeviationFactor))) && !map.isBlocked(x, y).booleanValue()) {
                maximumStepDist = distance;
                direction = Direction.getDirection(currentX, currentY, x, y);
                currentX = x;
                currentY = y;
                this.way.appendStep(x, y);
                updated = true;
                System.out.println("Person: direction " + direction + " " + x + " " + y);
                //System.out.println(this);
                System.out.println("Person: updated");
                return true;
            } else {
                throw new WalkingDistanceError();
            }
        }
    }

    private double getDistance(int x, int y) {
        synchronized (this) {
            return Math.sqrt((x - currentX) * (x - currentX) + (y - currentY) * (y - currentY));
        }
    }

    public void setUsed() {
        this.updated = false;
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
}
