/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.unit;

import DataStructure.Direction;

/**
 *
 * @author rajeevan
 */
public class DirectionTest {
    public static void run(){
        Direction start=Direction.d_270;
        Direction end=Direction.d_0;
        
        System.out.println(start);
        System.out.println(end);
        
        Direction difference=Direction.getDirection(start,end);
        System.out.println(difference);
        
    }
}
