/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author rajeevan
 */
public class SameCoordinate extends Exception {
    public String toString(){
        return "current coordinate and previous coordinate are same";
    }
}
