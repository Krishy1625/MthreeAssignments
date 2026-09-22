package flooringmastery.dao;

import flooringmastery.exception.FlooringMasteryPersistenceException;
import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDaoFileImpl implements OrderDao {

    public static final String DEFAULT_ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";
    public static final String DEFAULT_BACKUP_FILE = "src/main/resources/SampleFileData/Backup/DataExport.txt";

    public final String DELIMITER = "::";

    private final String orderFolder;
    private final String backupFile;

    private ProductDao productDaoFile;
    private TaxDao taxDaoFile;

    private Map<Integer, Order> orderNumberMapOrder = new HashMap<>();

    public OrderDaoFileImpl(ProductDao productDaoFile, TaxDao taxDaoFile) {
        this(productDaoFile, taxDaoFile, DEFAULT_ORDER_FOLDER, DEFAULT_BACKUP_FILE);
    }

    // for testing
    public OrderDaoFileImpl(ProductDao productDaoFile, TaxDao taxDaoFile, String orderFolder, String backupFile) {
        this.productDaoFile = productDaoFile;
        this.taxDaoFile = taxDaoFile;
        this.orderFolder = orderFolder;
        this.backupFile = backupFile;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        return new ArrayList<>(orderNumberMapOrder.values());
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        return orderNumberMapOrder.get(orderNumber);
    }

    @Override
    public void saveOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        orderNumberMapOrder.put(order.getOrderNumber(), order);
        writeEverythingToFile(date);
    }

    @Override
    public void deleteOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        orderNumberMapOrder.remove(orderNumber);
        writeEverythingToFile(date);
    }

    @Override
    public void exportDataWithDates() throws FlooringMasteryPersistenceException {

        ArrayList<String> orderDates = getOrderFileDates();

        final String HEADER = "OrderNumber" + DELIMITER
                + "CustomerName" + DELIMITER
                + "State" + DELIMITER
                + "TaxRate" + DELIMITER
                + "ProductType" + DELIMITER
                + "Area" + DELIMITER
                + "CostPerSquareFoot" + DELIMITER
                + "LaborCostPerSquareFoot" + DELIMITER
                + "MaterialCost" + DELIMITER
                + "LaborCost" + DELIMITER
                + "Tax" + DELIMITER
                + "Total" + DELIMITER
                + "OrderDate";

        final DateTimeFormatter EXPORT_DATE = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        File backup = new File(backupFile);
        File backupDir = backup.getParentFile();
        if (backupDir != null && !backupDir.exists()) {
            backupDir.mkdirs();
        }

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(backupFile))) {

            fw.write(HEADER);
            fw.newLine();

            for (String orderDateString : orderDates) {

                LocalDate date = stringToDate(orderDateString);
                if (date == null) continue;

                File orderFile = new File(orderFolder + dateToFileName(date));
                if (!orderFile.exists()) continue;

                loadOrdersForDate(date);

                for (Order order : orderNumberMapOrder.values()) {
                    fw.write(order.getOrderNumber() + DELIMITER
                            + order.getCustomerName() + DELIMITER
                            + order.getState() + DELIMITER
                            + order.getTaxRate() + DELIMITER
                            + order.getProductType() + DELIMITER
                            + order.getArea() + DELIMITER
                            + order.getCostPerSquareFoot() + DELIMITER
                            + order.getLabourCostPerSquareFoot() + DELIMITER
                            + order.getMaterialCost() + DELIMITER
                            + order.getLabourCost() + DELIMITER
                            + order.getTax() + DELIMITER
                            + order.getTotal() + DELIMITER
                            + date.format(EXPORT_DATE));
                    fw.newLine();
                }
            }

        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not write export file: " + backupFile, e);
        }
    }

    @Override
    public void exportAllData() throws FlooringMasteryPersistenceException {
        Map<String, Tax> taxes = taxDaoFile.getAllTaxes();
        Map<String, Product> products = productDaoFile.getAllProducts();
        ArrayList<String> orderDates = getOrderFileDates();

        final String TAX_HEADER = "State,StateName,TaxRate";
        final String PRODUCTS_HEADER = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot";
        final String COMMA = ",";

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(backupFile))) {

            fw.write("*** DISPLAYING TAX INFORMATION ***");
            fw.newLine(); fw.newLine();
            fw.write(TAX_HEADER); fw.newLine();

            for (Tax tax : taxes.values()) {
                fw.write(tax.getStateAbbreviation() + COMMA
                        + tax.getStateName() + COMMA
                        + tax.getTaxRate());
                fw.newLine();
            }

            fw.newLine();
            fw.write("*** END OF DISPLAYING TAX INFORMATION ***");
            fw.newLine(); fw.newLine();

            fw.write("*** DISPLAYING PRODUCT INFORMATION ***");
            fw.newLine(); fw.newLine();
            fw.write(PRODUCTS_HEADER); fw.newLine();

            for (Product product : products.values()) {
                fw.write(product.getProductType() + COMMA
                        + product.getCostPerSquareFoot() + COMMA
                        + product.getLabourCostPerSquareFoot());
                fw.newLine();
            }

            fw.newLine();
            fw.write("*** END OF DISPLAYING PRODUCT INFORMATION ***");
            fw.newLine(); fw.newLine();

            for (String orderDate : orderDates) {
                LocalDate date = stringToDate(orderDate);
                fw.write("*** DISPLAYING ORDERS FOR " + date + " ***");
                fw.newLine(); fw.newLine();

                String fileName = dateToFileName(date);
                File orderFile = new File(orderFolder + fileName);

                if (!orderFile.exists()) {
                    fw.write("No order file found for " + date);
                    fw.newLine(); fw.newLine();
                    continue;
                }

                try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        fw.write(line);
                        fw.newLine();
                    }
                }

                fw.newLine();
                fw.write("*** END OF ORDERS FOR " + date + " ***");
                fw.newLine(); fw.newLine();
            }

        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not write export file: " + backupFile, e);
        }
    }

    // io helpers

    public ArrayList<String> getOrderFileDates() {
        File folder = new File(orderFolder);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) return new ArrayList<>();

        return (ArrayList<String>) Arrays.stream(files)
                .map(File::getName)
                .map(name -> name.substring(
                        "Orders_".length(),
                        name.length() - ".txt".length()))
                .collect(Collectors.toList());
    }

    public LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println();
        }

        return null;
    }

    public String dateToFileName(LocalDate format_date) {
        String formatted_date = format_date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        return "Orders_" + formatted_date + ".txt";
    }

    public void loadOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException {
        orderNumberMapOrder.clear();
        File file = new File(orderFolder + dateToFileName(date));

        try {
            Files.lines(file.toPath())
                    .skip(1)
                    .map(String::trim)
                    .forEach(line -> {
                        try {
                            String[] split = line.split(DELIMITER);
                            Order order = createOrder(split);
                            orderNumberMapOrder.put(order.getOrderNumber(), order);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Skipping malformed order line: " + line);
                        }
                    });

        } catch (IOException e) {
            // file doesn't exist — map stays empty, not an error
        }
    }

    public void writeEverythingToFile(LocalDate date) throws FlooringMasteryPersistenceException {
        final String HEADER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";

        File orderFile = new File(orderFolder + dateToFileName(date));
        File parent = orderFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(orderFile))) {
            fw.write(HEADER);
            fw.newLine();

            for (Order value : orderNumberMapOrder.values()) {
                fw.write(value.getOrderNumber() + DELIMITER
                        + value.getCustomerName() + DELIMITER
                        + value.getState() + DELIMITER
                        + value.getTaxRate() + DELIMITER
                        + value.getProductType() + DELIMITER
                        + value.getArea() + DELIMITER
                        + value.getCostPerSquareFoot() + DELIMITER
                        + value.getLabourCostPerSquareFoot() + DELIMITER
                        + value.getMaterialCost() + DELIMITER
                        + value.getLabourCost() + DELIMITER
                        + value.getTax() + DELIMITER
                        + value.getTotal());
                fw.newLine();
            }
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not write order file for: " + date, e);
        }
    }

    private static Order createOrder(String[] order_split) {
        Order order = new Order();
        order.setOrderNumber(order_split[0]);
        order.setCustomerName(order_split[1]);
        order.setState(order_split[2]);
        order.setTaxRate(order_split[3]);
        order.setProductType(order_split[4]);
        order.setArea(order_split[5]);
        order.setCostPerSquareFoot(order_split[6]);
        order.setLabourCostPerSquareFoot(order_split[7]);
        order.setMaterialCost(order_split[8]);
        order.setLabourCost(order_split[9]);
        order.setTax(order_split[10]);
        order.setTotal(order_split[11]);
        return order;
    }
}