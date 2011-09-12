/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Exceptions.WalkingDistanceError;
import DataStructure.Direction;
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
    //properties of person
    private double maximumStepDist = 1.5;
    private double stepDeviationFactor = 0.5;

    //constructor 
    //input : starting position and destination position
    public Person(int startX, int startY, int destX, int destY, Direction startingDir) {
        this.currentX = startX;
        this.currentY = startY;
        way.appendStep(startX, startY);
        this.updated = true;
        direction = startingDir;
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

    ///#################################################################################
    public boolean updatePosition(int x, int y) throws WalkingDistanceError {
        synchronized (this) {
            double distance = getDistance(x, y);
            System.out.println("x=" + x + " y=" + y + " distance=" + distance);


            if (distance <= (maximumStepDist * (1 + stepDeviationFactor))) {
                maximumStepDist = distance;

                direction = Direction.getDirection(currentX, currentY, x, y);

                //only update currentX, currentY after update the direction
                currentX = x;
                currentY = y;
                this.way.appendStep(x, y);
                updated = true;
                System.out.println(this);
                return true;
            } else {
                throw new WalkingDistanceError();
            }
        }
    }

    private double getDistance(int x, int y) {
        synchronized(this){
        return Math.sqrt((x - currentX) * (x - currentX) + (y - currentY) * (y - currentY));
        }
    }

    public String toString() {
        return "X=" + currentX + " Y=" + currentY + " Dir=" + direction;
    }
}
