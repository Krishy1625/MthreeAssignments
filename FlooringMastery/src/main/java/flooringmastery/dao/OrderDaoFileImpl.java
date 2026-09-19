package flooringmastery.dao;

import flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderDaoFileImpl implements OrderDao {

    public final String DELIMITER = "::";
    public final String ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";

    public Map<LocalDate, Map<Integer, Order>> orders_map_collection;

    @Override
    public void writeToFile() {

    }

    @Override
    public void loadFromFile() {

    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public Order addOrder(Order order) {
        return null;
    }

    @Override
    public Order editOrder(LocalDate date, int order_id) {
        return null;
    }

    @Override
    public Order getOrder(LocalDate date, int order_id) {
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        return Map.of();
    }

    @Override
    public Order removeOrder(LocalDate date, int number) {
        return null;
    }
}
