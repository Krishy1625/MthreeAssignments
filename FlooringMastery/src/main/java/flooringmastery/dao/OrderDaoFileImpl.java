package flooringmastery.dao;

import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;
import flooringmastery.view.UserIO;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDaoFileImpl implements OrderDao {

    public final String DELIMITER = "::";
    public static final String ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";
    public final String BACKUP_FILE = "src/main/resources/SampleFileData/Backup/DataExport.txt";

    private UserIO io;
    private ProductDao productDaoFile;
    private TaxDao taxDaoFile;

    public Map<Integer, Order> order_number_map_order = new HashMap<>();

    public OrderDaoFileImpl(UserIO io, ProductDao productDaoFile, TaxDao taxDaoFile) {
        this.io = io;
        this.productDaoFile = productDaoFile;
        this.taxDaoFile = taxDaoFile;
    }


    // Main Methods

    public void addAndWriteOrder(LocalDate date) {

        cleanMap();
        loadOrdersForDate(date);
        Order order_to_add = returnAddedOrder(date);

        System.out.println("*** SUMMARY OF ORDER ***");
        detailedPrint(order_to_add);
        System.out.println("*** END OF SUMMARY OF ORDER ***");

        if (userConfirmation("Enter Yes or No to confirm ADDING this order, (Default is NO): ")){
            order_number_map_order.put(order_to_add.getOrderNumber(), order_to_add);
            writeEverythingToFIle(date);
        }
    }

    public void displayOrdersForDate(LocalDate date){

        System.out.println();
        io.print("*** DISPLAYING ORDERS ***");
        System.out.println();

        final String comma = ", ";

        if (!order_number_map_order.isEmpty()) {
            order_number_map_order.clear();
        }

        loadOrdersForDate(date);

        order_number_map_order.forEach((order_number, order) -> {
            io.print("Order #" + order.getOrderNumber());
            io.print(
                    order.getCustomerName() + comma
                            + order.getState()  + comma
                            + order.getTaxRate()  + comma
                            + order.getProductType()   + comma
                            + order.getArea()  + comma
                            + order.getCostPerSquareFoot()  + comma
                            + order.getLabourCostPerSquareFoot()  + comma
                            + order.getMaterialCost()    + comma
                            + order.getLabourCost()     + comma
                            + order.getTax()   + comma
                            + order.getTotal());
        });
        System.out.println();
        io.print("*** FINISH DISPLAYING ORDERS ***");
        System.out.println();
    }

    public void editedOrderFinal(LocalDate user_date){

        if (!order_number_map_order.isEmpty()) {
            order_number_map_order.clear();
        }

        if(editAnOrder(user_date)) {
            writeEverythingToFIle(user_date);
        }
        else{
            System.out.println("NO orders were edited for this date.");
        }
    }

    public void removeAnOrder(LocalDate date) {

        io.print("*** REMOVING ORDER ***");

        boolean exists = checkFileExists(ORDER_FOLDER + dateToFileName(date));

        if (!exists) {
            System.out.println("No such file exists currently for this date.");
        }
        else {

            loadOrdersForDate(date);

            if(!order_number_map_order.isEmpty()) {
                String prompt = "There are " + order_number_map_order.size() + " orders for this date. Which order do you want to delete? ";

                displayOrdersForDate(date);

                int user_order_number = io.readInt(prompt, 1, order_number_map_order.size());

                io.print("*** SUMMARY OF ORDER TO REMOVE ***");
                detailedPrint(order_number_map_order.get(user_order_number));
                io.print("*** END OF ORDER SUMMARY TO REMOVE ***");

                if(userConfirmation("Enter Yes or No to confirm DELETING this order, (Default is NO): ")){
                    order_number_map_order.remove(user_order_number);
                    writeEverythingToFIle(date);
                    io.print("*** ORDER SUCCESSFULLY REMOVED ***");
                }
            }
        }
    }

    public static ArrayList<String> getOrderFileDates() {
        File folder = new File(ORDER_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            return new ArrayList<>();
        }

        return (ArrayList<String>) Arrays.stream(files)
                .map(File::getName)   // .map((f) -> f.getName()) ide suggestion
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
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void exportAllDataFinal() {

        Map<String, Tax> taxes = taxDaoFile.getAllTaxes();
        Map<String, Product> products = productDaoFile.getAllProducts();
        ArrayList<String> orderDates = getOrderFileDates();

        final String TAX_HEADER = "State,StateName,TaxRate";

        final String PRODUCTS_HEADER = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot";

        final String COMMA = ",";

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(BACKUP_FILE))) {

            fw.write("*** DISPLAYING TAX INFORMATION ***");
            fw.newLine();
            fw.newLine();

            fw.write(TAX_HEADER);
            fw.newLine();

            for (Tax tax : taxes.values()) {

                fw.write(tax.getStateAbbreviation() + COMMA +
                        tax.getStateName() + COMMA +
                        tax.getTaxRate()
                );

                fw.newLine();
            }

            fw.newLine();
            fw.write("*** END OF DISPLAYING TAX INFORMATION ***");
            fw.newLine();
            fw.newLine();


            fw.write("*** DISPLAYING PRODUCT INFORMATION ***");
            fw.newLine();
            fw.newLine();

            fw.write(PRODUCTS_HEADER);
            fw.newLine();

            for (Product product : products.values()) {

                fw.write(product.getProductType() + COMMA +
                        product.getCostPerSquareFoot() + COMMA +
                        product.getLabourCostPerSquareFoot()
                );

                fw.newLine();
            }

            fw.newLine();
            fw.write("*** END OF DISPLAYING PRODUCT INFORMATION ***");
            fw.newLine();
            fw.newLine();


            for (String orderDate : orderDates) {

                LocalDate date = stringToDate(orderDate);

                fw.write("*** DISPLAYING ORDERS FOR " + date + " ***");

                fw.newLine();
                fw.newLine();

                String fileName = dateToFileName(date);

                File orderFile = new File(ORDER_FOLDER + fileName);

                if (!orderFile.exists()) {

                    fw.write("No order file found for " + date);

                    fw.newLine();
                    fw.newLine();

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

                fw.newLine();
                fw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }










    // helper methods

    public String dateToFileName(LocalDate format_date) {
        final String FILE_TEMPLATE = "Orders_";
        final String FILE_FORMAT = ".txt";

        String formatted_date = format_date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        return FILE_TEMPLATE + formatted_date + FILE_FORMAT;
    }

    public boolean checkFileExists(String file_path) {
        return Files.exists(Paths.get(file_path));
    }

    public boolean userConfirmation(String message) {
        String confimation = io.readString(message).strip().toLowerCase();
        return confimation.equals("yes");
    }

    public void cleanMap(){
        if (!order_number_map_order.isEmpty()) {
            order_number_map_order.clear();
        }
    }

    public void loadOrdersForDate(LocalDate date) {
        File file = new File(ORDER_FOLDER + dateToFileName(date));

        try {
            Files.lines(file.toPath())
                    .skip(1)                                    // skip the header line
                    .map(String::trim)
                    .map((line) -> line.split(DELIMITER))
                    .map(OrderDaoFileImpl::createOrder)
                    .forEach((order) -> order_number_map_order.put(order.getOrderNumber(), order));

        } catch (IOException e) {
            System.out.println("No such file exists currently for this date.");
        }
    }

    public void writeEverythingToFIle(LocalDate date) {

        final String HEADER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(ORDER_FOLDER + dateToFileName(date)));
            fw.write(HEADER);
            fw.newLine();

            order_number_map_order.forEach((key, value) -> {
                try {
                    fw.write(String.valueOf(value.getOrderNumber()) + DELIMITER +
                            String.valueOf(value.getCustomerName()) + DELIMITER +
                            String.valueOf(value.getState()) + DELIMITER +
                            String.valueOf(value.getTaxRate()) + DELIMITER +
                            String.valueOf(value.getProductType()) + DELIMITER +
                            String.valueOf(value.getArea()) + DELIMITER +
                            String.valueOf(value.getCostPerSquareFoot()) + DELIMITER +
                            String.valueOf(value.getLabourCostPerSquareFoot()) + DELIMITER +
                            String.valueOf(value.getMaterialCost()) + DELIMITER +
                            String.valueOf(value.getLabourCost()) + DELIMITER +
                            String.valueOf(value.getTax()) + DELIMITER +
                            String.valueOf(value.getTotal())
                    );
                    fw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            fw.flush();
            fw.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Order returnAddedOrder(LocalDate users_date) {

        io.print("*** Add an order ***");
        int order_number = 1;

        if (checkFileExists(ORDER_FOLDER + dateToFileName(users_date))) {
            loadOrdersForDate(users_date);

            order_number = order_number_map_order.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 1;
        }

        String customer_name = io.readCustomerName("Enter customer name: ");
        String state = io.readUserState("Enter state name (e.g. Texas): ").toLowerCase();
        String product_type = io.readUserProductType("Enter product type: ");
        BigDecimal area = io.readArea("Enter area for this order: ");

        Map<String, Tax> taxes_map = taxDaoFile.getAllTaxes();
        Map<String, Product> product_map = productDaoFile.getAllProducts();

        BigDecimal material_cost = io.calculateMaterialCost(area, product_map.get(product_type).getCostPerSquareFoot());
        BigDecimal labour_cost = io.calculateLabourCost(area, product_map.get(product_type).getLabourCostPerSquareFoot());
        BigDecimal calculated_tax = io.calculateTax(material_cost, labour_cost, taxes_map.get(state).getTaxRate());
        BigDecimal total = io.calculateTotal(material_cost, labour_cost, calculated_tax);

        Order order = new Order();

        order.setOrderNumber(order_number);
        order.setCustomerName(customer_name);
        order.setState(state);
        order.setTaxRate(taxes_map.get(state).getTaxRate());
        order.setProductType(product_type);
        order.setArea(area);
        order.setCostPerSquareFoot(product_map.get(product_type).getCostPerSquareFoot());
        order.setLabourCostPerSquareFoot(product_map.get(product_type).getLabourCostPerSquareFoot());
        order.setMaterialCost(material_cost);
        order.setLabourCost(labour_cost);
        order.setTax(calculated_tax);
        order.setTotal(total);

        return order;
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

    public void detailedPrint(Order order) {
        io.print("Order Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Tax Rate: " + order.getTaxRate());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Cost PerSquare Foot: " + order.getCostPerSquareFoot());
        io.print("Labour Cost PerSquare: " + order.getLabourCostPerSquareFoot());
        io.print("Material Cost: " + order.getMaterialCost());
        io.print("Labour Cost: " + order.getLabourCost());
        io.print("Tax: " + order.getTax());
        io.print("Total: " + order.getTotal());
    }

    public boolean editAnOrder(LocalDate date) {

        io.print("*** EDITING ORDER ***");

        boolean result = false;

        boolean exists = checkFileExists(ORDER_FOLDER + dateToFileName(date));

        if (!exists) {
            System.out.println("No such file exists currently for this date.");
        }
        else{
            loadOrdersForDate(date);

            if (!order_number_map_order.isEmpty()) {
                String prompt = "There are " + order_number_map_order.size() + " orders for this date. Which order do you want to edit? ";
                displayOrdersForDate(date);
                System.out.println();
                int user_order_number = io.readInt(prompt, 1, order_number_map_order.size());

                Order order_to_edit = order_number_map_order.get(user_order_number);

                String customer_name = io.readCustomerNameCanBeEmpty("Edit customer name (" + order_to_edit.getCustomerName() + "): ", order_to_edit.getCustomerName());
                String state = io.readUserStateCanBeEmpty("Edit state name, (" + order_to_edit.getState() + "): ", order_to_edit.getState());
                String product_type = io.readUserProductTypeCanBeEmpty("Edit product type ("  + order_to_edit.getProductType() + "): ", order_to_edit.getProductType());
                BigDecimal area = io.readAreaCanBeEmpty("Edit area for this order (" + order_to_edit.getArea() + "): ", order_to_edit.getArea());

                Map<String, Tax> taxes_map = taxDaoFile.getAllTaxes();
                Map<String, Product> product_map = productDaoFile.getAllProducts();

                BigDecimal material_cost = io.calculateMaterialCost(area, product_map.get(product_type).getCostPerSquareFoot());
                BigDecimal labour_cost = io.calculateLabourCost(area, product_map.get(product_type).getLabourCostPerSquareFoot());
                BigDecimal calculated_tax = io.calculateTax(material_cost, labour_cost, taxes_map.get(state).getTaxRate());
                BigDecimal total = io.calculateTotal(material_cost, labour_cost, calculated_tax);

                Order modified_order = new Order();

                modified_order.setOrderNumber(order_to_edit.getOrderNumber());
                modified_order.setCustomerName(customer_name);
                modified_order.setState(state);
                modified_order.setTaxRate(taxes_map.get(state).getTaxRate());
                modified_order.setProductType(product_type);
                modified_order.setArea(area);
                modified_order.setCostPerSquareFoot(product_map.get(product_type).getCostPerSquareFoot());
                modified_order.setLabourCostPerSquareFoot(product_map.get(product_type).getLabourCostPerSquareFoot());
                modified_order.setMaterialCost(material_cost);
                modified_order.setLabourCost(labour_cost);
                modified_order.setTax(calculated_tax);
                modified_order.setTotal(total);

                System.out.println("*** SUMMARY OF EDITED ORDER ***");
                detailedPrint(modified_order);
                System.out.println("*** END OF SUMMARY OF EDITED ORDER ***");

                if (userConfirmation("Enter Yes or No to confirm EDITING this order, (Default is NO): ")) {
                    order_number_map_order.put(modified_order.getOrderNumber(), modified_order);
                    result = true;
                }
            }
            else{
                System.out.println("No orders exists for this date.");
            }
        }
        io.print("*** FINISH EDITING ORDER ***");
        return result;
    }
}
