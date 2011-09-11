/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Exceptions.WalkingDistanceError;
import DataStructure.Direction;

/**
 *
 * @author rajeevan
 */
public class Person {

    private int currentX;
    private int currentY;
    private int destX;
    private int destY;
    
    private double maximumStepDist = 1;
    private double stepDeviationFactor = 0.5;
    private Direction direction;
    private boolean updated;

    public boolean isUpdated() {
        return updated;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    //lot of confusion.
    //need forward error correction
    //using maximumStepDist have to update the current Position.
    //update maximumStepDist dynamically.
    public  boolean updatePosition(int x, int y) throws WalkingDistanceError {
        if (getDistance(x, y) <= (maximumStepDist * (1 + stepDeviationFactor))) {
            maximumStepDist = getDistance(x, y);
            currentX = x;
            currentY = y;
            updated=true;
            return true;
        } else {
            throw new WalkingDistanceError();
        }
    }

    private double getDistance(int x, int y) {
        return Math.sqrt((x - currentX) * (x - currentX) + (y - currentY) * (y - currentY));
    }

    public int getCurrentX() {
        return currentX;
    }

    private void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    private void setCurrentY(int currentY) {
        this.currentY = currentY;
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

    public double getStepDeviationFactor() {
        return stepDeviationFactor;
    }

    public void setStepDeviationFactor(double stepDeviationFactor) {
        this.stepDeviationFactor = stepDeviationFactor;
    }
}
