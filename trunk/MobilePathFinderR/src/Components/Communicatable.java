/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

/**
 *
 * @author rajeevan
 */
public interface Communicatable extends Runnable {

   public void changeMode();

   public void changeSubMode();

   public void turnOff();
}
