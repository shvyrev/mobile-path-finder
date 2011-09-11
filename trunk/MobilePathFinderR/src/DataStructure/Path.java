package DataStructure;

import java.util.Vector;

/**
 *
 * @author rajeevan
 */
public class Path {

    private Vector steps = new Vector();

    public class Step {

        /** The x coordinate at the given step */
        private int x;
        /** The y coordinate at the given step */
        private int y;

        /**
         * Create a new step
         */
        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * @return The x coordinate of the new step
         */
        public int getX() {
            return x;
        }

        /**
         * @return The y coordinate of the new step
         */
        public int getY() {
            return y;
        }

        /**
         * @see Object#hashCode()
         */
        public int hashCode() {
            return x * y;
        }

        /**
         * @see Object#equals(Object)
         */
        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o = (Step) other;
                return (o.x == x) && (o.y == y);
            }

            return false;
        }

        public String toString() {
            String s = "";
            s = "(" + String.valueOf(this.x) + "," + String.valueOf(this.y) + ") \n";
            return s;
        }
    }

    //return The number of steps in this path
    public int getLength() {
        return steps.size();
    }

    //Get the step at a given index in the path
    private Step getStep(int index) {
        return (Step) steps.elementAt(index);
    }

    //Get the x coordinate for the step at the given index
    public int getX(int index) {
        return getStep(index).x;
    }

    //Get the y coordinate for the step at the given index
    public int getY(int index) {
        return getStep(index).y;
    }

    //Append a step to the path.
    public void appendStep(int x, int y) {
        steps.addElement(new Step(x, y));
    }

    //Prepend a step to the path.
    public void prependStep(int x, int y) {
        steps.insertElementAt(new Step(x, y), 0);
    }

    //Check if this path contains the given step
    public boolean contains(int x, int y) {
        return steps.contains(new Step(x, y));
    }

    
    public String toString() {
        String s = "";
        for (int i = 0; i < steps.size(); i++) {
            s += steps.elementAt(i).toString();
        }
        return s;
    }
    
    
    public int getLastX(){
        Step lastElement=(Step)steps.lastElement();
        return getX(lastElement.x);
    }
    
    public int getLastY(){
        Step lastElement=(Step)steps.lastElement();
        return getX(lastElement.y);
    }
}
