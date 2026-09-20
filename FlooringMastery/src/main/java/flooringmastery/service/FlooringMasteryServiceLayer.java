package flooringmastery.service;

import java.time.LocalDate;

public interface FlooringMasteryServiceLayer {
    void addAndWriteOrder(LocalDate date);
    void displayOrdersForDate(LocalDate date);
    void editedOrderFinal(LocalDate date);
    void removeAnOrder(LocalDate date);
    void exportAllDataFinal();
}