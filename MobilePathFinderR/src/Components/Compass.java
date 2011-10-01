/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Command;

/**
 *
 * @author rajeevan
 */
public class Compass implements Runnable{
    
    public Command currentCommand;
    
    public Compass(){
        this.currentCommand=ComponentsLib.command;
    }

    public void run() {
        
    }
    
    
}
