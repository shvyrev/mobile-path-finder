/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Components.Person;
import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.NavDirCommand;


/**
 *
 * @author rajeevan
 */
public class Navigator implements Runnable{
    
    NavDirCommand navDir=new NavDirCommand(0);
    Person p;
    Map m;
    
    public void run() {
        
    }
    
    private void updateCommandDirection(){
        Direction currentDirection=p.getDirection();
        Direction pathDirection=m.pathStartingDirection(p.getCurrentX(), p.getCurrentY());
        
        
    }
}
