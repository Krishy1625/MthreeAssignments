package flooringmastery.service;

import flooringmastery.dao.OrderDao;

import java.time.LocalDate;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    private OrderDao orderDao;

    public FlooringMasteryServiceLayerImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void addAndWriteOrder(LocalDate date) {
        orderDao.addAndWriteOrder(date);
    }

    @Override
    public void displayOrdersForDate(LocalDate date) {
        orderDao.displayOrdersForDate(date);
    }

    @Override
    public void editedOrderFinal(LocalDate date) {
        orderDao.editedOrderFinal(date);
    }

    @Override
    public void removeAnOrder(LocalDate date) {
        orderDao.removeAnOrder(date);
    }

    @Override
    public void exportAllDataFinal() {
        orderDao.exportAllDataFinal();
    }
}