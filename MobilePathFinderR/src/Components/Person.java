/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Algorithm.AStarPathFinder;
import Algorithm.DirectHeuristicCost;
import Algorithm.PathFinder;
import DataStructure.Commands;
import DataStructure.Coordinate;
import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.Path;
import Exceptions.WalkingDistanceError;
import test.utils.CoordinateServer;

/**
 *
 * @author rajeevan
 */
public class Person implements Runnable {

    //position and direction details of the person
    //*****************************************
    private int currentX;
    private int currentY;
    private Direction direction;
    private int terminalX;
    private int terminalY;
    //utilities
    //*****************************************
    //this is used to store the walking history of the person
    private Path way;
    //the map where the person use to navigate
    private Map map;
    //coordinate information received from Communicatable
    private Coordinate receivedCoordinate;
    //commands to the sound module
    Commands command;
    //person use it to find the path to the destination
    private PathFinder pf;
    //person use to find the direction where he have to move next
    private Navigator nav;
    //helps to get coordinate and help to call lift
    private Communicatable bt;
    //properties of person
    //**************************************************
    private double maximumStepDist = 2;
    private double stepDeviationFactor = 0.5;

    //constructor 
    //input : starting position and destination position
    public Person(int startX, int startY, Direction startingDir, Map m) {

        //basic initialization
        this.currentX = startX;
        this.currentY = startY;
        this.terminalX = m.getTerminalX();
        this.terminalY = m.getTerminalY();
        direction = startingDir;
        way = new Path();
        way.appendStep(startX, startY);
        bt = new BluetoothModule(BluetoothModule.HANDHELD);
        //bt = new CoordinateServer(0, 0, m);

        this.map = m;
        this.pf = new AStarPathFinder(map.getHeight() * map.getWidth(), new DirectHeuristicCost(), map.getWidth(), map.getHeight());
        this.nav = new Navigator();
        this.receivedCoordinate = ComponentsLib.coordinate;
        this.command = ComponentsLib.currentCommand;

        //communicator and sound module threads are initialized and started
        Thread communicator = new Thread(bt);
        Thread soundModule = new Thread(ComponentsLib.soundModule);
        communicator.start();
        soundModule.start();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMaximumStepDist(double maximumStepDist) {
        this.maximumStepDist = maximumStepDist;
    }

    public void setStepDeviationFactor(double stepDeviationFactor) {
        this.stepDeviationFactor = stepDeviationFactor;
    }

    public void updatePosition() throws WalkingDistanceError {
        int[] tempCoordinate = receivedCoordinate.getCoordinate();

        double distance = getDistance(tempCoordinate[0], tempCoordinate[1]);
        if ((distance <= (maximumStepDist * (1 + stepDeviationFactor))) && !map.isBlocked(tempCoordinate[0], tempCoordinate[1]).booleanValue()) {

            if (distance >= maximumStepDist) {
                maximumStepDist = distance;
            }

            direction = Direction.getDirection(currentX, currentY, tempCoordinate[0], tempCoordinate[1]);
            currentX = tempCoordinate[0];
            currentY = tempCoordinate[1];
            this.way.appendStep(currentX, currentY);

        } else {
            System.out.println("Person: Illegal coordinate arrived");
            throw new WalkingDistanceError();
        }
    }

    private double getDistance(int x, int y) {

        return Math.sqrt((x - currentX) * (x - currentX) + (y - currentY) * (y - currentY));

    }

    public String toString() {
        String s = "";
        for (int y = map.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getPath().contains(x, y)) {
                    s += "e";
                } else if (!way.contains(x, y)) {
                    s += map.getFloorPlan()[x][y].booleanValue() ? "_" : "o";
                } else {
                    s += "x";
                }
            }
            s += "\n";
        }
        return s;
    }

    public void run() {

        while (!(currentX == terminalX && currentY == terminalY)) {
            //System.out.println("per:("+currentX+","+currentY+")");
            try {
                updatePosition();
            } catch (WalkingDistanceError ex) {
                ComponentsLib.keyScanner.printAlert("Per: large Distance");
                continue;
            }
            pf.findPath(currentX, currentY, terminalX, terminalY, map);
            command=nav.navigateCommand(direction, map.pathStartingDirection());
            //ComponentsLib.currentCommand.setCommand(command);
            ComponentsLib.keyScanner.print(command.toString()+"(" + currentX + "," + currentY + ")");
           
        }
        ComponentsLib.keyScanner.printAlert("DEST ARRIVED");
        command.setCommand(Commands.ARRIVED);

        ComponentsLib.soundModule.changeMode(SoundModule.ORIENTATION);

        //start to aline the person in front of the elevator
        bt.changeSubMode(BluetoothModule.IR);
        bt.changeMode(BluetoothModule.ELEV);

        int pressedKey = ComponentsLib.pressedKey.getKey();
        bt.changeSubMode(BluetoothModule.UPDOWN);

        ComponentsLib.keyScanner.print("Select Up or Down");


        while (true) {
            pressedKey = ComponentsLib.pressedKey.getKey();
            if (pressedKey == EventCanvas.U) {
                
                ComponentsLib.keyScanner.print("Up selected");
                break;
            } else if (pressedKey == EventCanvas.D) {
                ComponentsLib.keyScanner.print("Down selected");
                break;
            }
        }


    }
}
