/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

/**
 *
 * @author rajeevan
 */
public class BluetoothReceiver implements Runnable {
    
    Person p;
    boolean coordinateAvailable;
    int currentX;
    int currentY;
    
    public BluetoothReceiver(Person p){
        this.p=p;
    }

    public void run() {
        while(true){
            if(coordinateAvailable){
            p.setCurrentX(currentX);
            p.setCurrentX(currentY);
            }
            updateCurrentPosition();
        }
    }
    
    
    //have to fill kaja
    public void updateCurrentPosition(){
        
    }
    
  
    
}
