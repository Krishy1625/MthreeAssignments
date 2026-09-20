package flooringmastery.dao;

import java.time.LocalDate;

public interface OrderDao {
    void addAndWriteOrder(LocalDate date);
    void displayOrdersForDate(LocalDate date);
    void editedOrderFinal(LocalDate date);
    void removeAnOrder(LocalDate date);
    void exportAllDataFinal();
}