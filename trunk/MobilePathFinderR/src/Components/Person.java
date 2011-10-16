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
import Exceptions.IllegalCoordinate;
import Exceptions.SameCoordinate;
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
    //display
    EventCanvas disp;
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
        disp = ComponentsLib.keyScanner;


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

    public void updatePosition() throws WalkingDistanceError, IllegalCoordinate, SameCoordinate {
        //blocked
        int[] tempCoordinate = receivedCoordinate.getCoordinate();
        if ((tempCoordinate[0] == currentX && tempCoordinate[1] == currentY)) {
            throw new SameCoordinate();
        } else if (map.isBlocked(tempCoordinate[0], tempCoordinate[1]).booleanValue()) {
            throw new IllegalCoordinate();
        } else {
            double distance = getDistance(tempCoordinate[0], tempCoordinate[1]);
            if ((distance <= (maximumStepDist * (1 + stepDeviationFactor)))) {

                if (distance >= maximumStepDist) {
                    maximumStepDist = distance;
                }
                direction = Direction.getDirection(currentX, currentY, tempCoordinate[0], tempCoordinate[1]);
                currentX = tempCoordinate[0];
                currentY = tempCoordinate[1];
                this.way.appendStep(currentX, currentY);
            } else {
                throw new WalkingDistanceError();
            }
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


        //navigation part:
        while (true) {
            try {
                updatePosition();
                if ((currentX == terminalX && currentY == terminalY)) {
                    disp.printCoordinate("DEST ARRIVED");
                    command.setCommand(Commands.ARRIVED);
                    break;
                }
                pf.findPath(currentX, currentY, terminalX, terminalY, map);
                ComponentsLib.currentCommand.setCommand(nav.navigateCommand(direction, map.pathStartingDirection()));
                disp.printCoordinate(command.toString() + "(" + currentX + "," + currentY + ")");
            } catch (WalkingDistanceError ex) {
                disp.printCoordinate("unbelievable step length");
            } catch (IllegalCoordinate ex) {
                disp.printCoordinate("illegal coordinate");
            } catch (SameCoordinate ex) {
                ComponentsLib.currentCommand.setCommand(nav.navigateCommand(direction, map.pathStartingDirection()));
                disp.printCoordinate(command.toString() + "(" + currentX + "," + currentY + ")");
                disp.printCoordinate("same coordinate arrived");
            }

        }
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//           
//        }

        

        //start to aline the person in front of the elevator
        bt.changeSubMode(BluetoothModule.IR);
        bt.changeMode(BluetoothModule.ELEV);


       


        //here the flow of the programme should be reviewed.
         ComponentsLib.pressedKey.getKey();
         bt.changeSubMode(BluetoothModule.UPDOWN);
       
//        bt.changeSubMode(BluetoothModule.UPDOWN);
//
//
//        
//        
//        
//        
//        while (true) {
//            disp.printCoordinate("Select Up or Down");
//            ComponentsLib.currentCommand.setCommand(Commands.SELECTUPORDOWN);
//            pressedKey = ComponentsLib.pressedKey.getKey();
//            if (pressedKey == EventCanvas.U) {
//                ComponentsLib.currentCommand.setCommand(Commands.UP);
//                ComponentsLib.elevator.setData(Elevator.UP);
//                disp.printCoordinate("Up selected");
//                break;
//            } else if (pressedKey == EventCanvas.D) {
//                ComponentsLib.currentCommand.setCommand(Commands.DOWN);
//                ComponentsLib.elevator.setData(Elevator.DOWN);
//                disp.printCoordinate("Down selected");
//                break;
//            }
//        }
        
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
        
        //bt.turnOff();
    }

    public void exitSystem() {
        bt.turnOff();
    }
}