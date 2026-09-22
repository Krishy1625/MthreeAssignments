package flooringmastery.service;

import flooringmastery.dao.OrderDao;
import flooringmastery.dao.ProductDao;
import flooringmastery.dao.TaxDao;
import flooringmastery.exception.FlooringMasteryDataValidationException;
import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceLayerImplTest {

    private FlooringMasteryServiceLayerImpl service;
    private InMemoryOrderDao orderDao;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        // one state and one product so calculateOrder has something to find
        Map<String, Tax> taxes = new HashMap<>();
        Tax tx = new Tax();
        tx.setStateAbbreviation("TX");
        tx.setStateName("Texas");
        tx.setTaxRate(new BigDecimal("4.45"));
        taxes.put("texas", tx);

        Map<String, Product> products = new HashMap<>();
        Product tile = new Product();
        tile.setProductType("Tile");
        tile.setCostPerSquareFoot(new BigDecimal("3.50"));
        tile.setLabourCostPerSquareFoot(new BigDecimal("4.15"));
        products.put("tile", tile);

        orderDao = new InMemoryOrderDao();
        service = new FlooringMasteryServiceLayerImpl(orderDao, new StubProductDao(products), new StubTaxDao(taxes));

        date = LocalDate.of(2025, 3, 15);
    }

    private Order makeOrder() {
        Order order = new Order();
        order.setOrderNumber(1);
        order.setCustomerName("Ada Lovelace");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("200"));
        return order;
    }

    @Test
    void getOrdersForDateReturnsWhatWeSaved() throws Exception {
        orderDao.saveOrder(date, makeOrder());

        List<Order> orders = service.getOrdersForDate(date);

        assertEquals(1, orders.size());
        assertEquals("Ada Lovelace", orders.get(0).getCustomerName());
    }

    @Test
    void nextOrderNumberIsOneWhenNothingExists() throws Exception {
        assertEquals(1, service.getNextOrderNumber(date));
    }

    @Test
    void nextOrderNumberIsOneMoreThanTheHighest() throws Exception {
        orderDao.saveOrder(date, makeOrder());     // order 1

        Order second = makeOrder();
        second.setOrderNumber(5);
        orderDao.saveOrder(date, second);

        assertEquals(6, service.getNextOrderNumber(date));
    }

    @Test
    void calculateOrderFillsInTheNumbers() throws Exception {
        Order order = makeOrder();     // TX, Tile, 200 sq ft

        service.calculateOrder(order);

        // material = 200 * 3.50 = 700
        assertEquals(0, order.getMaterialCost().compareTo(new BigDecimal("700")));
        // labour = 200 * 4.15 = 830
        assertEquals(0, order.getLabourCost().compareTo(new BigDecimal("830")));
        // tax = (700 + 830) * 4.45% = 68.085
        assertEquals(0, order.getTax().compareTo(new BigDecimal("68.085")));
        // total = 700 + 830 + 68.085
        assertEquals(0, order.getTotal().compareTo(new BigDecimal("1598.085")));
    }

    @Test
    void unknownStateBlowsUp() {
        Order order = makeOrder();
        order.setState("ZZ");

        assertThrows(FlooringMasteryDataValidationException.class, () -> service.calculateOrder(order));
    }

    @Test
    void unknownProductBlowsUp() {
        Order order = makeOrder();
        order.setProductType("Gold");

        assertThrows(FlooringMasteryDataValidationException.class,
                () -> service.calculateOrder(order));
    }

    // in-memory for the DAOs

    static class InMemoryOrderDao implements OrderDao {
        private final Map<LocalDate, Map<Integer, Order>> saved = new HashMap<>();

        @Override
        public List<Order> getOrders(LocalDate date) {
            return new ArrayList<>(saved.getOrDefault(date, Map.of()).values());
        }

        @Override
        public Order getOrder(LocalDate date, int orderNumber) {
            return saved.getOrDefault(date, Map.of()).get(orderNumber);
        }

        @Override
        public void saveOrder(LocalDate date, Order order) {
            saved.computeIfAbsent(date, d -> new HashMap<>()).put(order.getOrderNumber(), order);
        }

        @Override
        public void deleteOrder(LocalDate date, int orderNumber) {
            saved.getOrDefault(date, Map.of()).remove(orderNumber);
        }

        @Override public void exportAllData() { }
        @Override public void exportDataWithDates() { }
    }

    static class StubTaxDao implements TaxDao {
        private final Map<String, Tax> map;
        StubTaxDao(Map<String, Tax> map) { this.map = map; }
        @Override public Map<String, Tax> getAllTaxes() { return map; }
        @Override public void loadFile() { }
    }

    static class StubProductDao implements ProductDao {
        private final Map<String, Product> map;
        StubProductDao(Map<String, Product> map) { this.map = map; }
        @Override public Map<String, Product> getAllProducts() { return map; }
        @Override public void loadFile() { }
    }
}