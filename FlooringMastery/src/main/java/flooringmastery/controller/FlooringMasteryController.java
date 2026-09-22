package flooringmastery.controller;

import flooringmastery.exception.FlooringMasteryDataValidationException;
import flooringmastery.exception.FlooringMasteryPersistenceException;
import flooringmastery.model.Order;
import flooringmastery.service.FlooringMasteryServiceLayer;
import flooringmastery.view.FlooringMasteryView;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            int selection = view.displayMainMenuAndGetSelection();
            try {
                switch (selection) {
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
                        exportAllData();
                        break;
                    case 6:
                        exportDataWithDates();
                        break;
                    case 7:
                        keepRunning = false;
                        break;
                    default:
                        view.unknownCommand();
                }
            } catch (FlooringMasteryPersistenceException e) {
                view.displayError("There was a problem reading or writing data: " + e.getMessage());
            } catch (FlooringMasteryDataValidationException e) {
                view.displayError(e.getMessage());
            }
        }
        view.exitMessage();
    }

    public void displayOrders() throws FlooringMasteryPersistenceException{
        view.displayMessage("Displaying orders...");
        view.displayMessage("");

        LocalDate date = view.getOrderDate();
        List<Order> orders = service.getOrdersForDate(date);
        if (orders.isEmpty()) {
            view.displayNoOrdersForDate();
        } else {
            view.displayOrders(orders);
        }
    }

    public void addOrder() throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException{
        view.displayMessage("Adding a new order...");
        view.displayMessage("");

        LocalDate date = view.getFutureOrderDate();

        Order order = view.getNewOrderInfo();

        order.setOrderNumber(service.getNextOrderNumber(date));
        order = service.calculateOrder(order);

        view.displayOrderSummary(order);

        if (view.confirm("Enter Yes or No to confirm ADDING this order, (Default is NO): ")) {
            service.saveOrder(date, order);
            view.displaySuccess("ORDER SUCCESSFULLY ADDED");
        } else {
            view.displaySuccess("Order not added.");
        }
    }

    public void editOrder() throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException{

        view.displayMessage("Edit Order Selected ...");
        view.displayMessage("");

        LocalDate date = view.getOrderDate();

        List<Order> orders = service.getOrdersForDate(date);
        if (orders.isEmpty()) {
            view.displayNoOrdersForDate();
            return;
        }

        view.displayOrders(orders);
        int orderNumber = view.getOrderNumber();

        Order existing = service.getOrderForDate(date, orderNumber);
        if (existing == null) {
            view.displayError("No order #" + orderNumber + " exists for that date.");
            return;
        }

        Order updated = view.getEditedOrderInfo(existing);
        updated = service.calculateOrder(updated);

        view.displayOrderSummary(updated);

        if (view.confirm("Enter Yes or No to confirm EDITING this order, (Default is NO): ")) {
            service.saveOrder(date, updated);
            view.displaySuccess("ORDER SUCCESSFULLY EDITED");
        } else {
            view.displaySuccess("Order not edited.");
        }
    }

    public void removeOrder() throws FlooringMasteryPersistenceException{

        view.displayMessage("Removing Order Selected ...");
        view.displayMessage("");

        LocalDate date = view.getOrderDate();

        List<Order> orders = service.getOrdersForDate(date);
        if (orders.isEmpty()) {
            view.displayNoOrdersForDate();
            return;
        }

        view.displayOrders(orders);
        int orderNumber = view.getOrderNumber();

        Order existing = service.getOrderForDate(date, orderNumber);
        if (existing == null) {

            view.displayError("No order #" + orderNumber + " exists for that date.");
            return;
        }

        view.displayOrderSummary(existing);

        if (view.confirm("Enter Yes or No to confirm DELETING this order, (Default is NO): ")) {
            service.deleteOrder(date, orderNumber);
            view.displaySuccess("ORDER SUCCESSFULLY REMOVED");
        } else {
            view.displaySuccess("Order not removed.");
        }
    }

    public void exportAllData() throws FlooringMasteryPersistenceException {
        service.exportAllData();
        view.displayExportSuccess();
    }

    public void exportDataWithDates() throws FlooringMasteryPersistenceException {
        service.exportDataWithDates();
        view.displayExportWithDatesSuccess();
    }
}