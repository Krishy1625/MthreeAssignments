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

    private UserIO io = new UserIOConsoleImpl();
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

        final String comma = ", ";
        ArrayList<Order> list_of_orders = (ArrayList<Order>) orderDao.getOrdersForDate();
        for (int i = 0; i < list_of_orders.size(); i++){

            io.print("Order #" + (i + 1) + ": ");
            Order order = list_of_orders.get(i);
            io.print(
                    order.getCustomerName() + comma
                    + order.getState()  + comma
                    + order.getTaxRate()  + comma
                    + order.getProductType()   + comma
                    + order.getArea()  + comma
                    + order.getCostPerSquareFoot()  + comma
                    + order.getLabourCostPerSquareFoot()  + comma
                    + order.getMaterialCost()    + comma
                    + order.getLabourCost()     + comma
                    + order.getTax()   + comma
                    + order.getTotal());
        }
    }










    public static void main(String[] args) {
        FlooringMasteryController fmc = new FlooringMasteryController();
        fmc.run();
    }
}
