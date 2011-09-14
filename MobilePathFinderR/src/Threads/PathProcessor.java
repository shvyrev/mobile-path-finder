/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Components.Person;
import Algorithm.PathFinder;
import Components.MapLib;
import DataStructure.Map;

/**
 *
 * @author rajeevan
 */
public class PathProcessor implements Runnable {

    private Person p;
    private int DestX;
    private int DestY;
    private Map m;
    private PathFinder pathFinder;
    
    public PathProcessor(Person p, PathFinder pf){
        this.p=p;
        this.DestX=p.getDestX();
        this.DestY=p.getDestY();
        this.m=MapLib.map;
        this.pathFinder=pf;
                
    }
    
    public void run() {
        while(true){
            if(p.isUpdated()){
                this.updatePath();
                p.setUsed();
            }
        }
    }
    
    
    private  void updatePath(){
        synchronized(p){
        pathFinder.findPath(p.getCurrentX(),p.getCurrentY(), DestX,DestY);
        //System.out.println(this.m);
        }
    }
}
