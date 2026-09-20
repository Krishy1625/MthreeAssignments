package flooringmastery.dao;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getOrders(LocalDate date);
    Order getOrder(LocalDate date, int orderNumber);
    void saveOrder(LocalDate date, Order order);
    void deleteOrder(LocalDate date, int orderNumber);
    void exportAllData();
}