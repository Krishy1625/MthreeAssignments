package flooringmastery.dao;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.Map;

public interface ExportDao {
    void writeToFile();
    void exportData (Map<LocalDate, Map<Integer, Order>> map_localdate_integer);
}