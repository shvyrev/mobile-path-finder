package Algorithm;

import Components.MapLib;
import DataStructure.Collections;
import DataStructure.Path;
import DataStructure.Map;
import DataStructure.VectorSub;
import DataStructure.MyComparable;

/**
 *
 * @author rajeevan
 */
public class AStarPathFinder implements PathFinder {

    //variables
    ///##########################################################################
    private VectorSub closed = new VectorSub();
    private SortedList open = new SortedList();
    private int maxSearchDistance;
    Map map;
    private Node[][] nodes;
    private boolean allowDiagMovement = true;
    private HeuristicCost heuristic;
    ///##########################################################################

    //inner classes 
    ///##########################################################################
    private class Node implements MyComparable {

        /** The x coordinate of the node */
        private int x;
        /** The y coordinate of the node */
        private int y;
        /** The path cost for this node */
        private float pathCost;
        /** The heuristic cost of this node */
        private float heuristicCost;
        /** The parent of this node, how we reached it in the search */
        private Node parent;
        /** The search depth of this node */
        private int depth;

        /**
         * Create a new node
         */
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.depth = parent.depth + 1;
        }

        /**
         * Set the parent of this node
         */
        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;
            return depth;
        }

        public int CompareTo(Object other) {
            Node o = (Node) other;
            float f = heuristicCost + pathCost;
            float of = o.heuristicCost + o.pathCost;
            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                //System.out.println("this is not cool");
                return 0;

            }
        }

        public boolean equals(Object o) {
            Node n;
            if (o instanceof Node && o != null) {
                n = (Node) o;
                if (n.x == this.x && n.y == this.y) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int hash = 3;
            hash = 73 * hash + this.x;
            hash = 73 * hash + this.y;
            hash = 73 * hash + Float.floatToIntBits(this.pathCost);
            hash = 73 * hash + Float.floatToIntBits(this.heuristicCost);
            hash = 73 * hash + (this.parent != null ? this.parent.hashCode() : 0);
            hash = 73 * hash + this.depth;
            return hash;
        }

        public String toString() {
            return "(" + this.x + "," + this.y + ")" + "totalCost=" + (this.pathCost + this.heuristicCost) + "\n";
        }
    }

    private class SortedList {

        /** The list of elements */
        private VectorSub list = new VectorSub();

        /**
         * Retrieve the first element from the list
         */
        public Object first() {
            return list.elementAt(0);
        }

        /**
         * Empty the list
         */
        public void clear() {
            list.removeAllElements();
        }

        /**
         * Add an element to the list - causes sorting
         */
        public void add(Object t) {
            list.addElement(t);
            Collections.sort(list);
        }

        /**
         * Remove an element from the list
         */
        public void remove(Object t) {
            list.removeElement(t);
        }

        /**
         * Get the number of elements in the list
         */
        public int size() {
            return list.size();
        }

        /**
         * Check if an element is in the list
         */
        public boolean contains(Object t) {
            return list.contains(t);
        }
    }
    ///##########################################################################

    //constructors
    ///##########################################################################
    public AStarPathFinder(int maxSearchDistance, HeuristicCost heuristic) {
        this.map = MapLib.map;
        this.maxSearchDistance = maxSearchDistance;
        this.heuristic = heuristic;
        
        nodes = new Node[map.getWidth()][map.getHeight()];
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }
    public AStarPathFinder(){
        this(MapLib.map.getHeight()*MapLib.map.getWidth(),new DirectHeuristicCost());
    }

    ///##########################################################################

    /**
     * @return The first element in the open list
     */
    private Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
     * Add a node to the open list
     * It will cause sorting the open list
     */
    private void addToOpen(Node node) {
        open.add(node);
    }

    /**
     * Check if a node is in the open list
     */
    private boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     */
    private void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     */
    private void addToClosed(Node node) {
        closed.addElement(node);
    }

    /**
     * Check if the node supplied is in the closed list
     */
    private boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     */
    private void removeFromClosed(Node node) {
        closed.removeElement(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     */
    private boolean isValidLocation(int sx, int sy, int x, int y,Map map) {

        boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidth()) || (y >= map.getHeight());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.isBlocked(x, y).booleanValue();
        }

        return !invalid;
    }

    /**
     * Get the cost to move through a given location
     * here i choose 1 for each direction 
     * actually 4 main directions(up,down,right,left)have lower cost than 
     * corner movements.
     */
    public float getMovementCost(int sx, int sy, int tx, int ty) {

        if (tx == sx || ty == sy) {
            return 1;
        } else {
            return (float) 1.5;
        }
    }

    /**
     * Get the heuristic cost for the given location. This determines in which
     * order the locations are processed.
     */
    public float getHeuristicCost(int x, int y, int tx, int ty) {
        return heuristic.getCost(x, y, tx, ty);
    }

    //this is the actual implementation of A* path finding algorithm
    
    public Path findPath(int sx, int sy, int tx, int ty) {
        // first check, if the destination is blocked, we can't get there
        if (map.isBlocked(tx, ty).booleanValue()) {
            return null;
        }


        // initial state for A*. The closed group is empty. Only the starting
        //tile is in the open list and it'e're already there
        nodes[sx][sy].pathCost = 0;
        nodes[sx][sy].depth = 0;
        closed.removeAllElements();
        open.clear();
        open.add(nodes[sx][sy]);
        nodes[tx][ty].parent = null;


        // while we haven'n't exceeded our max search depth
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            //System.out.println("here..");
            // pull out the first node in our open list, this is determined to
            // be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating
            // them as next steps

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {

                    // not a neighbour, its the current tile
                    if ((x == 0) && (y == 0)) {
                        continue;
                    }

                    // if we're not allowing diaganol movement then only
                    // one of x or y can be set

                    if (!allowDiagMovement) {
                        if ((x != 0) && (y != 0)) {
                            continue;
                        }
                    }

                    // determine the location of the neighbour and evaluate it

                    int xp = x + current.x;
                    int yp = y + current.y;

                    if (isValidLocation(sx, sy, xp, yp,map)) {

                        // the cost to get to this node is cost the current plus the movement
                        // cost to reach this node. 
                        //whis is going to be used in the path cost
                        //Note that the heursitic value is only used in the sorted open list

                        float nextStepCost = current.pathCost + getMovementCost(current.x, current.y, xp, yp);
                        Node neighbour = nodes[xp][yp];

                        // if the new cost we've determined for this node is lower than
                        // it has been previously makes sure the node hasn'e've
                        // determined that there might have been a better path to get to
                        // this node so it needs to be re-evaluated

                        if (nextStepCost < neighbour.pathCost) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);

                            }
                        }

                        // if the node hasn't already been processed and discarded then

                        // reset it's cost to our current cost and add it as a next possible

                        // step (i.e. to the open list)

                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.pathCost = nextStepCost;
                            neighbour.heuristicCost = getHeuristicCost(xp, yp, tx, ty);
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }

        // since we'e've run out of search
        // there was no path. Just return null

        if (nodes[tx][ty].parent == null) {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent

        // references of the nodes to find out way from the target location back

        // to the start recording the nodes on the way.

        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);
        map.setPath(path);
        //System.out.println("path is "+path);
        // thats it, we have our path

        return path;
    }

}
