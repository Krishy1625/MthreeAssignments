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

    public final String DELIMITER = "::";
    public static final String ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";
    public final String BACKUP_FILE = "src/main/resources/SampleFileData/Backup/DataExport.txt";

    private ProductDao productDaoFile;
    private TaxDao taxDaoFile;

    private Map<Integer, Order> order_number_map_order = new HashMap<>();

    public OrderDaoFileImpl(ProductDao productDaoFile, TaxDao taxDaoFile) {
        this.productDaoFile = productDaoFile;
        this.taxDaoFile = taxDaoFile;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        return new ArrayList<>(order_number_map_order.values());
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException{
        loadOrdersForDate(date);
        return order_number_map_order.get(orderNumber);
    }

    @Override
    public void saveOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException {
        loadOrdersForDate(date);
        order_number_map_order.put(order.getOrderNumber(), order);
        writeEverythingToFIle(date);
    }

    @Override
    public void deleteOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException{
        loadOrdersForDate(date);
        order_number_map_order.remove(orderNumber);
        writeEverythingToFIle(date);
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

        // making sure the backup folder exists
        File backupFile = new File(BACKUP_FILE);
        File backupDir = backupFile.getParentFile();
        if (backupDir != null && !backupDir.exists()) {
            backupDir.mkdirs(); // mkdir
        }

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(BACKUP_FILE))) {

            fw.write(HEADER);
            fw.newLine();

            for (String orderDateString : orderDates) {

                LocalDate date = stringToDate(orderDateString);
                if (date == null) continue;

                File orderFile = new File(ORDER_FOLDER + dateToFileName(date));
                if (!orderFile.exists()) continue;

                loadOrdersForDate(date);

                for (Order order : order_number_map_order.values()) {
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
                    "Could not write export file: " + BACKUP_FILE, e);
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

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(BACKUP_FILE))) {

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
                File orderFile = new File(ORDER_FOLDER + fileName);

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
            throw new FlooringMasteryPersistenceException("Could not write export file: " + BACKUP_FILE, e);
        }
    }

    // io helpers

    public static ArrayList<String> getOrderFileDates() {
        File folder = new File(ORDER_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) return new ArrayList<>();

        return (ArrayList<String>) Arrays.stream(files)
                .map(File::getName)
                .map((name) -> name.substring(
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
        order_number_map_order.clear();
        File file = new File(ORDER_FOLDER + dateToFileName(date));

        try {
            Files.lines(file.toPath())
                    .skip(1)
                    .map(String::trim)
                    .forEach((line) -> {
                        try {
                            String[] split = line.split(DELIMITER);
                            Order order = createOrder(split);
                            order_number_map_order.put(order.getOrderNumber(), order);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Skipping malformed order line: " + line);
                        }
                    });
        } catch (IOException e) {
            // file doesn't exist — map stays empty, not an error
        }
    }

    public void writeEverythingToFIle(LocalDate date) throws FlooringMasteryPersistenceException {
        final String HEADER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(ORDER_FOLDER + dateToFileName(date)));
            fw.write(HEADER);
            fw.newLine();

            order_number_map_order.forEach((key, value) -> {
                try {
                    fw.write(String.valueOf(value.getOrderNumber()) + DELIMITER
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not write order file for: " + date, e);
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