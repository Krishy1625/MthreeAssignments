package flooringmastery.controller;

import flooringmastery.view.FlooringMasteryView;
import flooringmastery.view.UserIO;
import flooringmastery.view.UserIOConsoleImpl;

public class FlooringMasteryController {

    private UserIO io = new UserIOConsoleImpl();
    private FlooringMasteryView view = new FlooringMasteryView();

    public void run(){

        boolean keepRunning = true;
        int selection = 0;

        while(keepRunning){

            selection = view.displayMainMenuAndGetSelection();

            switch (selection){
                case 1:
                    io.print("Display Orders");
                    break;
                case 2:
                    io.print("Add an Order");
                    break;
                case 3:
                    io.print("Edit an Order");
                    break;
                case 4:
                    io.print("Remove an Order");
                    break;
                case 5:
                    io.print("Export All Data");
                    break;
                case 6:
                    keepRunning = false;
                    break;
                default:
                    view.unknownCommand();
            }
        }
        view.exitMessage();
    }

    public void displayOrders(){

    }
}
