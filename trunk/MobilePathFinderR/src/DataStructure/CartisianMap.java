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
    private int destX;
    private int destY;
    private int terminalX;
    private int terminalY;
    //true ==> can travel false==> obstacle
    private Boolean[][] floor = new Boolean[width][height];
    private Path path;

    public CartisianMap(Boolean[][] floor, int destX, int destY, int terminalX, int terminalY) {
        this.width = floor.length;
        this.height = floor[0].length;
        this.floor = floor;
        this.path = new Path();
        this.destX = destX;
        this.destY = destY;
        this.terminalX = terminalX;
        this.terminalY = terminalY;
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

    public Direction pathStartingDirection() {
        return path.getStartingDirection();
    }

    public Direction destinationDirection(int currentX, int currentY) {
        return Direction.getDirection(currentX, currentY, destX, destY);
    }

    public int getTerminalX() {
        return this.terminalX;
    }

    public int getTerminalY() {
        return this.terminalY;
    }
}
