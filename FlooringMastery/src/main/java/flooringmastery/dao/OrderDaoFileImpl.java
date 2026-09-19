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

    public Map<LocalDate, Map<Integer, Order>> orders_map_collection;
    public Map<Integer, Order> order_number_map_order = new HashMap<>();
    Map<String, Product>  product_name_map_product_object = new HashMap<>();



    public String dateToFileName(LocalDate format_date) {
        final String FILE_TEMPLATE = "Orders_";
        final String FILE_FORMAT = ".txt";

        String formatted_date = format_date.format(DateTimeFormatter.ofPattern("MMddyyyy"));;

        return FILE_TEMPLATE + formatted_date + FILE_FORMAT;
    }

    public boolean checkFileExists(String file_path) {
        return Files.exists(Paths.get(file_path));
    }

    public LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            return LocalDate.parse(date, formatter);
        }
        catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void writeToFIle(LocalDate date) {

        final String HEARDER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";


        if (!order_number_map_order.isEmpty()){
            order_number_map_order.clear();
        };

        loadOrdersForDate(stringToDate("01-06-2013"));

        if (checkFileExists(ORDER_FOLDER + dateToFileName(date))){
            System.out.println("Order File Exists");
        }
        else{
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter(ORDER_FOLDER + dateToFileName(date)));
                fw.write(HEARDER);
                fw.newLine();

                order_number_map_order.forEach((key, value) -> {
                    try {
                        fw.write(String.valueOf(value.getOrderNumber()) + DELIMITER +
                                     String.valueOf(value.getCustomerName())  + DELIMITER +
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                fw.flush();
                fw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




    public void addOrder(){

        io.print("Add an order");

        LocalDate users_date =  io.readDateAfterToday("Enter order date (must be in the future in the dd-mm-yyyy format) : ");

        String format_date = users_date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        final String file_template = "Orders_";
        final String file_format = ".txt";

        String file_name = file_template + format_date + file_format;
        File file = new File(ORDER_FOLDER + file_name);


        try {
            if(file.createNewFile()){
                System.out.println("File created" + file.getName());
            }
            else {
                System.out.println("File already exists " + file.getName());

                String customer_name = io.readCustomerName("Enter customer name: ");

                String state = io.readUserState("Enter state name (e.g. Texas): ").toLowerCase();

                String product_type = io.readUserProductType("Enter product type: ");

                BigDecimal area = io.readArea("Enter area for this order: ");

                productDaoFile.listAllProductsAndPricingInformation();

                product_name_map_product_object = productDaoFile.getAllProducts();

                Order order = new Order();


                // ORDER NUMBER CANNOT BE FIGURED OUT RIGHT NOW


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OrderDaoFileImpl orderDaoFileImpl = new OrderDaoFileImpl();
        orderDaoFileImpl.writeToFIle(LocalDate.now());
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

    public List<Order> getOrdersForDate(LocalDate date) {

        String format_date = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

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


    public void loadOrdersForDate(LocalDate date) {

        String format_date = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));

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

}
