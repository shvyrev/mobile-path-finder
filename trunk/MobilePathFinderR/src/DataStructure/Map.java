package DataStructure;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rajeevan
 */
public interface Map {

    public int getWidth();

    public int getHeight();

    public Boolean isBlocked(int x, int y);

    public void setMap(Boolean[][] floor);
    
    public void setPath(Path p);
    
    public Path getPath();
    
    public Boolean[][] getFloorPlan();
    
    public Boolean getValue(int x,int y);
    
    public void setValue(int x,int y,Boolean value);
    
    public Direction pathStartingDirection();
}
