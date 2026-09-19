package flooringmastery.dao;

import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;
import flooringmastery.view.UserIOConsoleImpl;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    public final String DELIMITER = "::";
    public final String ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";
    private UserIOConsoleImpl io = new UserIOConsoleImpl();
    private ProductDaoFileImpl productDaoFile = new ProductDaoFileImpl();
    private TaxDaoFileImpl taxDaoFile = new TaxDaoFileImpl();
    public Map<Integer, Order> order_number_map_order = new HashMap<>();

    public String dateToFileName(LocalDate format_date) {
        final String FILE_TEMPLATE = "Orders_";
        final String FILE_FORMAT = ".txt";

        String formatted_date = format_date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        return FILE_TEMPLATE + formatted_date + FILE_FORMAT;
    }

    public boolean checkFileExists(String file_path) {
        return Files.exists(Paths.get(file_path));
    }

    public LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void writeToFIle(LocalDate date) {

        final String HEARDER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(ORDER_FOLDER + dateToFileName(date)));
            fw.write(HEARDER);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }














    public void writeEverythingToFIle(LocalDate date) {

        final String HEARDER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(ORDER_FOLDER + dateToFileName(date)));
            fw.write(HEARDER);
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


    public void addAnOrder(LocalDate date) {

        if (!order_number_map_order.isEmpty()) {
            order_number_map_order.clear();
        }

        loadOrdersForDate(date);
        Order order_to_add = addOrder(date);
        order_number_map_order.put(order_to_add.getOrderNumber(), order_to_add);
        writeEverythingToFIle(date);

    }




    public Order addOrder(LocalDate users_date) {

        io.print("*** Add an order ***");
        //LocalDate users_date = io.readDateAfterToday("Enter order date (must be in the future in the dd-mm-yyyy format): ");

        int order_number = 1;

        if (checkFileExists(ORDER_FOLDER + dateToFileName(users_date))) {
            System.out.println("Order File Exists  krish uniq");

            loadOrdersForDate(users_date);

            order_number = order_number_map_order.size() + 1;
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

        prettyPrint(order);
        return order;
    }

    public void loadOrdersForDate(LocalDate date) {

        String format_date = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        final String file_template = "Orders_";
        final String file_format = ".txt";

        String file_name = file_template + format_date + file_format;

        File file = new File(ORDER_FOLDER + file_name);

        try (Scanner sc = new Scanner(file)) {

            if (sc.hasNextLine()) {
                sc.nextLine(); // get rid of the headers
            }

            while (sc.hasNextLine()) {

                String[] order_split = sc.nextLine().trim().split(DELIMITER);
                Order order = createOrder(order_split);

                order_number_map_order.put(Integer.valueOf(order_split[0]), order);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Product file not found: " + file);
        }
    }


    // helper method to create deez orders
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

    public List<Order> getOrdersForDate() {

        io.print("Display Orders Selected");
        LocalDate date_of_order =  io.readDate("What data would you like to display orders for? (dd-mm-yyyy)");
        String format_date = date_of_order.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        final String file_template = "Orders_";
        final String file_format = ".txt";

        String file_name = file_template + format_date + file_format;

        File file = new File(ORDER_FOLDER + file_name);

        try(Scanner sc = new Scanner(file)){

            if(sc.hasNextLine()){
                sc.nextLine(); // get rid of the headers
            }

            while (sc.hasNextLine()) {

                String[] order_split =  sc.nextLine().trim().split(DELIMITER);
                Order order = createOrder(order_split);

                order_number_map_order.put(Integer.valueOf(order_split[0]),order);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Product file not found: " + file);
        }

        return new ArrayList<>(order_number_map_order.values());
        //remember array lists start from 0 but orders are from 1 onward
    }

    public static void main(String[] args) {
        OrderDaoFileImpl orderDaoFileImpl = new OrderDaoFileImpl();
        //orderDaoFileImpl.writeToFIle(LocalDate.now());
        //.addOrder();
    }

    public void prettyPrint(Order order) {
        io.print(
                order.getOrderNumber() + DELIMITER +
                order.getCustomerName() + DELIMITER +
                order.getState() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLabourCostPerSquareFoot() + DELIMITER +
                order.getMaterialCost() + DELIMITER +
                order.getLabourCost() + DELIMITER +
                order.getTax() + DELIMITER +
                order.getTotal()
                );
    }


}
