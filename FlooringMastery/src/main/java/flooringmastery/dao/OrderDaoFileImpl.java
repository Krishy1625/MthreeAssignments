package flooringmastery.dao;

import flooringmastery.model.Order;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;
import flooringmastery.view.UserIOConsoleImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    public final String DELIMITER = "::";
    public final String ORDER_FOLDER = "src/main/resources/SampleFileData/Orders/";
    private UserIOConsoleImpl io = new UserIOConsoleImpl();
    private ProductDaoFileImpl productDaoFile = new ProductDaoFileImpl();

    public Map<LocalDate, Map<Integer, Order>> orders_map_collection;
    public Map<Integer, Order> order_number_map_order = new HashMap<>();


    /**
     * Add an Order
     * To add an order will query the user for each piece of order data necessary:
     *
     * Order Date – Must be in the future
     * Customer Name – May not be blank and is limited to characters [a-z][0-9] as well as periods and comma characters. "Acme, Inc." is a valid name.
     * State – Entered states must be checked against the tax file. If the state does not exist in the tax file, we cannot sell there. If the tax file is modified to include the state, it should be allowed without changing the application code.
     * Product Type – Show a list of available products and pricing information to choose from. Again, if a product is added to the file it should show up in the application without a code change.
     * Area – The area must be a positive decimal. Minimum order size is 100 sq ft.
     * The remaining fields are calculated from the user entry and the tax/product information in the files. Show a summary of the order once the calculations are completed and prompt the user as to whether they want to place the order (Y/N). If yes, the data will be added to in-memory storage. If no, simply return to the main menu.
     *
     * The system should generate an order number for the user based on the next available order # (so if there are two orders and the max order number is 4, the next order number should be 5).
     *
     *
     *
     *   get date from user until date is in the future
     *   get customer name from user until it meets the regex
     *   get state from user until the state is the state from the state file
     *   show a list of available products and their pricing information
     *   get area from the user as a big decimal where the area >= 100
     *
     *   calculate the remaining fields from the daos
     *   show a summary of the order
     *   prompt the user if they want to place the order (default yes)
     *
     *   if yes:
     *      save
     *   if no:
     *      back to home
     *      break;
     *
     *
     *
     * @param
     * @return
     */

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

                List<Order> orders_for_this_date = getOrdersForDate(users_date);

                io.print(String.valueOf(orders_for_this_date));

                String customer_name = io.readCustomerName("Enter customer name");

                String state = io.readUserState("Enter state name (e.g. Texas): ").toLowerCase();

                productDaoFile.listAllProductsAndPricingInformation();


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }




    }

    public static void main(String[] args) {
        OrderDaoFileImpl orderDaoFileImpl = new OrderDaoFileImpl();
        orderDaoFileImpl.addOrder();
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
