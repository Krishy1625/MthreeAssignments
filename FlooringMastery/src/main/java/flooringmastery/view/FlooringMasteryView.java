package flooringmastery.view;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    //menu stuff



    public void displayMessage(String message) {
        System.out.println(message);
    }

    public int displayMainMenuAndGetSelection() {
        io.print("");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program Main Menu>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Export Data to Backup");
        io.print("* 7. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("");
        return io.readInt("Please select from the above choices.", 1, 7);
    }

    public void exitMessage() { io.print("Cya later, BYE!"); }
    public void unknownCommand() { io.print("Unknown Command!!!"); }

    //user prompts and inputs

    public LocalDate getOrderDate() {
        return io.readDate("Enter order date (dd-MM-yyyy): ");
    }

    public LocalDate getFutureOrderDate() {
        return io.readDateAfterToday("Enter order date (must be in the future in the dd-mm-yyyy format): ");
    }

    public int getOrderNumber() {
        return io.readInt("Enter order number: ");
    }

    public Order getNewOrderInfo() {
        Order order = new Order();
        order.setCustomerName(io.readCustomerName("Enter customer name: "));
        order.setState(io.readUserState("Enter state name (e.g. Texas): ").toLowerCase());
        order.setProductType(io.readUserProductType("Enter product type: "));
        order.setArea(io.readArea("Enter area for this order: "));
        return order;
    }

    public Order getEditedOrderInfo(Order existing) {
        Order order = new Order();
        order.setOrderNumber(existing.getOrderNumber());

        order.setCustomerName(io.readCustomerNameCanBeEmpty("Edit customer name (" + existing.getCustomerName() + "): ", existing.getCustomerName()));
        order.setState(io.readUserStateCanBeEmpty("Edit state name (" + existing.getState() + "): ", existing.getState()).toLowerCase());
        order.setProductType(io.readUserProductTypeCanBeEmpty("Edit product type (" + existing.getProductType() + "): ", existing.getProductType()));
        order.setArea(io.readAreaCanBeEmpty("Edit area for this order (" + existing.getArea() + "): ", existing.getArea()));

        return order;
    }

    public boolean confirm(String message) {
        return io.readString(message).strip().equalsIgnoreCase("yes");
    }

    //dispplay stuff

    public void displayOrders(List<Order> orders) {
        io.print("");
        io.print("*** DISPLAYING ORDERS ***");
        io.print("");
        orders.forEach((order) -> {
            io.print("");
            detailedPrint(order);
            io.print("");
        });
        io.print("");
        io.print("*** FINISH DISPLAYING ORDERS ***");
    }

    public void detailedPrint(Order order) {
        io.print("Order Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Tax Rate: " + order.getTaxRate());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Cost PerSquare Foot: " + order.getCostPerSquareFoot());
        io.print("Labour Cost PerSquare: " + order.getLabourCostPerSquareFoot());
        io.print("Material Cost: " + order.getMaterialCost());
        io.print("Labour Cost: " + order.getLabourCost());
        io.print("Tax: " + order.getTax());
        io.print("Total: " + order.getTotal());
    }

    public void displayOrderSummary(Order order) {
        io.print("");
        io.print("*** SUMMARY OF ORDER ***");
        io.print("");
        detailedPrint(order);
        io.print("");
        io.print("*** END OF SUMMARY OF ORDER ***");
        io.print("");
    }

    public void displayNoOrdersForDate() {
        io.print("No orders exist for this date.");
    }

    public void displayError(String message) {
        io.print("*** ERROR: " + message + " ***");
    }

    public void displaySuccess(String message) {
        io.print(">>> " + message);
    }

    public void displayExportSuccess() {
        io.print("*** DATA EXPORTED TO 'DataExport.txt' UNDER THE 'Backup' FOLDER ***");
    }

    public void displayExportWithDatesSuccess() {
        io.print("*** DATA EXPORTED TO 'DataExport.txt' UNDER THE 'Backup' FOLDER (WITH DATES) ***");
    }
}