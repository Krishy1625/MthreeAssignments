package flooringmastery.controller;

import flooringmastery.view.FlooringMasteryView;
import flooringmastery.view.UserIO;
import flooringmastery.view.UserIOConsoleImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                    displayOrders();
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
        io.print("Display Orders Selected");
        LocalDate date_of_order =  io.readDate("What data would you like to display orders for? (dd-mm-yyyy)");
        String format_date = date_of_order.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        final String file_template = "Orders_";
        final String file_format = ".txt";

        System.out.println(file_template + format_date + file_format);




    }

    public static void main(String[] args) {
        FlooringMasteryController fmc = new FlooringMasteryController();
        fmc.displayOrders();
    }
}
