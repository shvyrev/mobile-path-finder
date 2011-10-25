/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import Components.ComponentsLib;

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
    
    
    //////////////////////////////////////
    private Coordinate[]tempCoord;
//////////////////////////////////////////

    public CartisianMap(Boolean[][] floor, int destX, int destY, int terminalX, int terminalY) {
        this.width = floor.length;
        this.height = floor[0].length;
        this.floor = floor;
        this.path = new Path();
        this.destX = destX;
        this.destY = destY;
        this.terminalX = terminalX;
        this.terminalY = terminalY;
        this.tempCoord=new Coordinate[]{new Coordinate(terminalX,terminalY)};
    }
    
    public CartisianMap(Boolean[][] floor, int destX, int destY, int terminalX, int terminalY,Coordinate[]tempPath){
        this(floor,destX,destY,terminalX,terminalY);
        tempCoord=tempPath;
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

    public Coordinate getTerminal(int currentX,int currentY) {
        try{
        for(int i=0;i<tempCoord.length;i++){
            if(!(currentX==tempCoord[i].getX()&&currentY==tempCoord[i].getY())){
            if(currentX>=tempCoord[i].getX()&&currentY>=tempCoord[i].getY()){
                return tempCoord[i];
            }
            }
        }
        }catch(NullPointerException e){
            ComponentsLib.keyScanner.printException("MAP:NUll pointer");
        }
            
        return new Coordinate(terminalX,terminalY);
    }

    public Coordinate getDest() {
        return new Coordinate(destX,destY);
    }

    public boolean hasObstacleInRange(Coordinate currentPosition,Direction currentDirection) {
        Direction temp;
       for(int x=-1;x<=1;x++){
           for(int y=-1;y<=1;y++){
               temp=Direction.getDirection(currentPosition.getX(),currentPosition.getY(), (currentPosition.getX()+x),(currentPosition.getY()+y));
               temp=Direction.getDirection(currentDirection, temp);
              
               
           }
          
       }
       return false;
    }

    public Direction getObstacleDirection(Coordinate currentPosition,Direction currentDirection) {
      Coordinate[]forward= Direction.getForwardArea(currentPosition, currentDirection);
      for(int i=0;i<5;i++){
          if(isBlocked(forward[i].getX(), forward[i].getY()).booleanValue()){
              return Direction.getDirection(currentPosition, currentPosition);
          }
      }
      return null;
    }

    
}
