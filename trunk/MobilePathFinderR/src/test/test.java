/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import Algorithm.AStarPathFinder;
import DataStructure.Path;
import DataStructure.Map;
import javax.microedition.lcdui.Form;
/**
 *
 * @author rajeevan
 */
public class test {
    
    public static void run(Form frm){
        frm.append("starting..");

        frm.append("\nThe Floor:\n");
        Map m=RandomMapGen.randomMap(10, 10);

        frm.append(m.toString());
        Path p=new AStarPathFinder(m).findPath(0, 0, 9, 9);

        frm.append("\nThe navigation Path:\n");

        frm.append(m.toString());
    }

    
}
