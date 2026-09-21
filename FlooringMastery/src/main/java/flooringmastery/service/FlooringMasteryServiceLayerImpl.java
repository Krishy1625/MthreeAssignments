package flooringmastery.service;

import flooringmastery.dao.OrderDao;
import flooringmastery.dao.ProductDao;
import flooringmastery.dao.TaxDao;
import flooringmastery.exception.FlooringMasteryDataValidationException;
import flooringmastery.exception.FlooringMasteryPersistenceException;
import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    private OrderDao orderDao;
    private ProductDao productDaoFile;
    private TaxDao taxDaoFile;

    public FlooringMasteryServiceLayerImpl(OrderDao orderDao, ProductDao productDaoFile, TaxDao taxDaoFile) {
        this.orderDao = orderDao;
        this.productDaoFile = productDaoFile;
        this.taxDaoFile = taxDaoFile;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException {
        return orderDao.getOrders(date);
    }

    @Override
    public Order getOrderForDate(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException{
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public int getNextOrderNumber(LocalDate date) throws FlooringMasteryPersistenceException{
        return orderDao.getOrders(date).stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Order calculateOrder(Order order) throws FlooringMasteryDataValidationException {

        Map<String, Tax> taxes_map = taxDaoFile.getAllTaxes();
        Map<String, Product> product_map = productDaoFile.getAllProducts();

        Tax tax = taxes_map.get(order.getState().toLowerCase());
        Product product = product_map.get(order.getProductType().toLowerCase());

        if (tax == null) {
            throw new FlooringMasteryDataValidationException("We cannot sell in state '" + order.getState() + "'.");
        }
        if (product == null) {
            throw new FlooringMasteryDataValidationException("'" + order.getProductType() + "' is not an available product.");
        }

        order.setTaxRate(tax.getTaxRate());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLabourCostPerSquareFoot(product.getLabourCostPerSquareFoot());

        BigDecimal material_cost = order.getArea().multiply(order.getCostPerSquareFoot());
        BigDecimal labour_cost = order.getArea().multiply(order.getLabourCostPerSquareFoot());
        BigDecimal calculated_tax = material_cost.add(labour_cost).multiply(order.getTaxRate().divide(new BigDecimal("100")));
        BigDecimal total = material_cost.add(labour_cost).add(calculated_tax);

        order.setMaterialCost(material_cost);
        order.setLabourCost(labour_cost);
        order.setTax(calculated_tax);
        order.setTotal(total);

        return order;
    }

    @Override
    public void saveOrder(LocalDate date, Order order)  throws FlooringMasteryPersistenceException{
        orderDao.saveOrder(date, order);
    }

    @Override
    public void deleteOrder(LocalDate date, int orderNumber)  throws FlooringMasteryPersistenceException{
        orderDao.deleteOrder(date, orderNumber);
    }

    @Override
    public void exportAllData()  throws FlooringMasteryPersistenceException{
        orderDao.exportAllData();
    }

    @Override
    public void exportDataWithDates() throws FlooringMasteryPersistenceException {
        orderDao.exportDataWithDates();
    }
}