package flooringmastery.service;

import flooringmastery.dao.OrderDao;
import flooringmastery.dao.ProductDao;
import flooringmastery.dao.TaxDao;
import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    private OrderDao orderDao;
    private ProductDao productDaoFile;
    private TaxDao taxDaoFile;

    public FlooringMasteryServiceLayerImpl(OrderDao orderDao,
                                           ProductDao productDaoFile,
                                           TaxDao taxDaoFile) {
        this.orderDao = orderDao;
        this.productDaoFile = productDaoFile;
        this.taxDaoFile = taxDaoFile;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return orderDao.getOrders(date);
    }

    @Override
    public Order getOrderForDate(LocalDate date, int orderNumber) {
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public int getNextOrderNumber(LocalDate date) {
        return orderDao.getOrders(date).stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Order calculateOrder(Order order) {
        Map<String, Tax> taxes_map = taxDaoFile.getAllTaxes();
        Map<String, Product> product_map = productDaoFile.getAllProducts();

        Tax tax = taxes_map.get(order.getState().toLowerCase());
        Product product = product_map.get(order.getProductType().toLowerCase());

        order.setTaxRate(tax.getTaxRate());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLabourCostPerSquareFoot(product.getLabourCostPerSquareFoot());

        BigDecimal material_cost = order.getArea().multiply(order.getCostPerSquareFoot());
        BigDecimal labour_cost = order.getArea().multiply(order.getLabourCostPerSquareFoot());
        final String HUNDRED = "100";
        BigDecimal calculated_tax = material_cost.add(labour_cost).multiply(order.getTaxRate().divide(new BigDecimal(HUNDRED)));
        BigDecimal total = material_cost.add(labour_cost).add(calculated_tax);

        order.setMaterialCost(material_cost);
        order.setLabourCost(labour_cost);
        order.setTax(calculated_tax);
        order.setTotal(total);

        return order;
    }

    @Override
    public void saveOrder(LocalDate date, Order order) {
        orderDao.saveOrder(date, order);
    }

    @Override
    public void deleteOrder(LocalDate date, int orderNumber) {
        orderDao.deleteOrder(date, orderNumber);
    }

    @Override
    public void exportAllData() {
        orderDao.exportAllData();
    }
}