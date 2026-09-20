package flooringmastery.service;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryServiceLayer {
    List<Order> getOrdersForDate(LocalDate date);
    Order getOrderForDate(LocalDate date, int orderNumber);
    int getNextOrderNumber(LocalDate date);
    Order calculateOrder(Order order);
    void saveOrder(LocalDate date, Order order);
    void deleteOrder(LocalDate date, int orderNumber);
    void exportAllData();
}