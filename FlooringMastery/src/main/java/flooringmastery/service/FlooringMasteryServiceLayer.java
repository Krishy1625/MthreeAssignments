package flooringmastery.service;

import flooringmastery.exception.FlooringMasteryDataValidationException;
import flooringmastery.exception.FlooringMasteryPersistenceException;
import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryServiceLayer {
    List<Order> getOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException;
    Order getOrderForDate(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    int getNextOrderNumber(LocalDate date) throws FlooringMasteryPersistenceException;
    Order calculateOrder(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException;
    void saveOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException;
    void deleteOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    void exportAllData() throws FlooringMasteryPersistenceException;
    void exportDataWithDates() throws FlooringMasteryPersistenceException;
}