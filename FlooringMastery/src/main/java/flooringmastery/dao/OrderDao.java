package flooringmastery.dao;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    void writeToFile();
    void loadFromFile();
    int getNextOrderNumber();
    Order addOrder(Order order);
    Order editOrder(LocalDate date , int order_id);
    Order getOrder(LocalDate date, int order_id);
    List<Order> getOrdersForDate(LocalDate date);
    Map<LocalDate, Map<Integer, Order>> getAllOrders();
    Order removeOrder(LocalDate date, int number);
}

