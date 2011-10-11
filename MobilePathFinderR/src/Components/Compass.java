/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Commands;

/**
 *
 * @author rajeevan
 */
public class Compass implements Runnable{
    
    public Commands currentCommand;
    public BluetoothModule bt;
    
    public Compass(BluetoothModule bt){
        this.currentCommand=new Commands(0);
        ComponentsLib.soundModule.setCommand(currentCommand);
        this.bt=bt;
    }

    public void run() {
            
    }
    
    
    
    
}
