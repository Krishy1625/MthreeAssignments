package flooringmastery.controller;

import flooringmastery.dao.OrderDaoFileImpl;
import flooringmastery.model.Order;
import flooringmastery.view.FlooringMasteryView;
import flooringmastery.view.UserIO;
import flooringmastery.view.UserIOConsoleImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FlooringMasteryController {

    private UserIOConsoleImpl io = new UserIOConsoleImpl();
    private FlooringMasteryView view = new FlooringMasteryView();
    private OrderDaoFileImpl orderDao = new OrderDaoFileImpl();

    public void run(){

        boolean keepRunning = true;
        int selection = 0;

        while(keepRunning){

            selection = view.displayMainMenuAndGetSelection();

            switch (selection){
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
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
        io.print("*** DISPLAYING ORDERS ***");
        LocalDate user_date = io.readDate("What date do you want to display orders for?");
        orderDao.displayOrdersForDate(user_date);
    }

    public void addOrder(){
        io.print("*** Add Order ***");
        LocalDate users_date = io.readDateAfterToday("Enter order date (must be in the future in the dd-mm-yyyy format): ");
        orderDao.addAndWriteOrder(users_date);
    }

    public void editOrder(){
        io.print("*** Edit Order ***");
        LocalDate userdate = io.readDate("Enter order date to be edited: ");
        orderDao.editedOrderFinal(userdate);
    }

    public void removeOrder(){
        io.print("*** Remove Order ***");
        LocalDate user_date = io.readDate("Enter order date to be removed: ");
        orderDao.removeAnOrder(user_date);
    }

    public static void main(String[] args) {
        FlooringMasteryController fmc = new FlooringMasteryController();
        fmc.run();
    }
}
