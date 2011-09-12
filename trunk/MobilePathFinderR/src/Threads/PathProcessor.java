/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Algorithm.PathFinder;
import DataStructure.Map;

/**
 *
 * @author rajeevan
 */
public class PathProcessor implements Runnable {

    private Person p;
    private Map m;
    private PathFinder pathFinder;
    
    public void run() {
        while(true){
            if(p.isUpdated()){
                this.updatePath();
            }
        }
    }
    
    
    private  void updatePath(){
        synchronized(p){
        pathFinder.findPath(p.getCurrentX(), p.getCurrentY(), p.getDestX(), p.getDestY(),m);
        }
    }
    
}
