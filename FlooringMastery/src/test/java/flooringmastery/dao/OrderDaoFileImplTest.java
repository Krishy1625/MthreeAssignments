package flooringmastery.dao;

import flooringmastery.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {

    @TempDir
    Path folder;

    private OrderDaoFileImpl dao;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        dao = new OrderDaoFileImpl(
                null,
                null,
                folder.toString() + "/",
                folder.resolve("Backup.txt").toString());
        date = LocalDate.of(2025, 3, 15);
    }

    private Order makeOrder() {
        Order order = new Order();
        order.setOrderNumber(1);
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setTaxRate("4.45");
        order.setProductType("Tile");
        order.setArea("200");
        order.setCostPerSquareFoot("3.50");
        order.setLabourCostPerSquareFoot("4.15");
        order.setMaterialCost("700.00");
        order.setLabourCost("830.00");
        order.setTax("68.15");
        order.setTotal("1598.15");
        return order;
    }

    @Test
    void saveThenReadItBack() throws Exception {
        dao.saveOrder(date, makeOrder());

        Order loaded = dao.getOrder(date, 1);

        assertNotNull(loaded);
        assertEquals("Ada Lovelace", loaded.getCustomerName());
        assertEquals("TX", loaded.getState());
    }

    @Test
    void deleteRemovesIt() throws Exception {
        dao.saveOrder(date, makeOrder());
        dao.deleteOrder(date, 1);

        assertNull(dao.getOrder(date, 1));
    }

    @Test
    void emptyDateGivesEmptyList() throws Exception {
        List<Order> orders = dao.getOrders(LocalDate.of(1999, 1, 1));

        assertTrue(orders.isEmpty());
    }

    @Test
    void savedOrderIsStillThereNextTimeWeOpenTheDao() throws Exception {
        dao.saveOrder(date, makeOrder());

        OrderDaoFileImpl reopened = new OrderDaoFileImpl(
                null,
                null,
                folder.toString() + "/",
                folder.resolve("Backup.txt").toString());

        assertNotNull(reopened.getOrder(date, 1),
                "order should have been saved to disk");
    }

    @Test
    void fileIsNamedAfterTheDate() throws Exception {
        dao.saveOrder(date, makeOrder());

        assertTrue(Files.exists(folder.resolve("Orders_03152025.txt")));
    }

    @Test
    void exportWritesTheOrderToTheBackupFile() throws Exception {
        dao.saveOrder(date, makeOrder());
        dao.exportDataWithDates();

        String backup = Files.readString(folder.resolve("Backup.txt"));

        assertTrue(backup.contains("Ada Lovelace"));
        assertTrue(backup.contains("03-15-2025"));
    }

    @Test
    void stringToDateHandlesBadInput() {
        assertNull(dao.stringToDate("nonsense"));
        assertNotNull(dao.stringToDate("03152025"));
    }
}