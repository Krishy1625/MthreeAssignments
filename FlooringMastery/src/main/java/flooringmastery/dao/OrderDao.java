package flooringmastery.dao;

import flooringmastery.exception.FlooringMasteryPersistenceException;
import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getOrders(LocalDate date) throws FlooringMasteryPersistenceException;
    Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    void saveOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException;
    void deleteOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    void exportAllData() throws FlooringMasteryPersistenceException;
}