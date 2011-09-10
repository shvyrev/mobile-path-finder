package Algorithm;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rajeevan
 */
public class DirectHeuristicCost implements HeuristicCost{

    public float getCost(int currentX, int currentY, int targetX, int targetY) {
        return (Math.abs(targetX-currentX)+Math.abs(targetY-currentY));
    }
    
}
