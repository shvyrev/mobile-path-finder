/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rajeevan
 */
public class CartisianMap implements Map {

    //height & width are number of tiles in the floor on x axis and y axis
    private int width = 0;
    private int height = 0;
    //true ==> can travel false==> obstacle
    private Boolean[][] floor = new Boolean[width][height];
    private Path path;

    public CartisianMap(int width, int height, Boolean[][] floor) {
        this.width = width;
        this.height = height;
        this.floor = floor;
        this.path = new Path();
    }

    public CartisianMap(Boolean[][] floor) {
        this(floor.length, floor[0].length, floor);
    }

    public CartisianMap() {
        this(50, 50);
    }

    public CartisianMap(int width, int height) {
        Boolean[][] flr = new Boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                flr[x][y] = Boolean.TRUE;
            }
        }
        this.width = width;
        this.height = height;
        this.floor = flr;
        this.path = new Path();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Boolean isBlocked(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return floor[x][y].booleanValue() ? Boolean.FALSE : Boolean.TRUE;
        } else {
            return Boolean.TRUE;
        }
    }

    public void setMap(Boolean[][] floor) {
        this.floor = floor;
    }

    public String toString() {
        String s = "";
        if (path == null) {
            s += "No Path Available!";
        }
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (!path.contains(x, y)) {
                    s += floor[x][y].booleanValue() ? "+" : "x";
                } else {
                    s += "$";
                }
            }
            s += "\n";
        }

        return s;
    }

    public void setPath(Path p) {
        this.path = p;
    }

    public Path getPath() {
        return path;
    }

    public Boolean[][] getFloorPlan() {
        return floor;
    }

    public Boolean getValue(int x, int y) {
        return floor[x][y];
    }

    public void setValue(int x, int y, Boolean value) {
        this.floor[x][y] = value;
    }

    public Direction pathStartingDirection(int currentX,int currentY) {
        return Direction.getDirection(currentX,currentY,path.getLastX(),path.getLastY());
    }
}
