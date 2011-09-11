/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Exceptions.WalkingDistanceError;

/**
 *
 * @author rajeevan
 */
public class GPSmodule implements Runnable {
    
    Person p;
    boolean coordinateAvailable;
    int currentX;
    int currentY;
    
    public GPSmodule(Person p){
        this.p=p;
    }

    public void run() {
        while(true){
            if(coordinateAvailable){
                try {
                    p.updatePosition(currentX, currentX);
                } catch (WalkingDistanceError ex) {
                    System.out.println("Invalied Coordinate input");
                }
            }
            updateCurrentPosition();
        }
    }
    
    
    //have to fill kaja
    public void updateCurrentPosition(){
        
    }
    
  
    
}
