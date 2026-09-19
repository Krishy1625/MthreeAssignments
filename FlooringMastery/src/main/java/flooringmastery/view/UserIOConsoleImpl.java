package flooringmastery.view;

import flooringmastery.dao.ProductDaoFileImpl;
import flooringmastery.dao.TaxDao;
import flooringmastery.dao.TaxDaoFileImpl;
import flooringmastery.model.Product;
import flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

    TaxDaoFileImpl taxDao = new TaxDaoFileImpl();
    ProductDaoFileImpl productDaoFile = new ProductDaoFileImpl();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        print(prompt); // use print method I made above
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        print(prompt);

        while(!sc.hasNextInt()) {
            sc.nextLine();
            print(prompt);
        }

        int user_result = sc.nextInt();
        sc.nextLine();
        return user_result;
    }

    @Override
    public int readInt(String prompt, int min, int max) {

        if (max < min) {
            System.out.println("Maximum value is lower than minimum value");
            System.exit(-1);
        }

        print(prompt);

        while(true) {

            if(sc.hasNextInt()) {
                int user_input = sc.nextInt();

                if (user_input >= min && user_input <= max) {
                    sc.nextLine();
                    return user_input;
                }
                else {
                    System.out.println("Error: Integer is out of range.");
                    sc.nextLine();
                    print(prompt);
                }
            }
            else {
                sc.nextLine();
                print(prompt);
            }
        }
    }

    public LocalDate readDate(String prompt) {
        print(prompt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while(true) {
            String user_input_date = sc.nextLine();

            try {
                return LocalDate.parse(user_input_date, formatter);
            }
            catch (DateTimeParseException e) {
                print("Error: Invalid date format.");
                System.out.println();
                print(prompt);
            }
        }
    }


    public LocalDate readDateAfterToday(String prompt) {
        print(prompt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while(true) {
            String user_input_date = sc.nextLine();

            try {
                LocalDate users_date = LocalDate.parse(user_input_date, formatter);

                if (users_date.isAfter(LocalDate.now())) {
                    return users_date;
                }
                else  {
                    print("Error: Date must be after today's date, try again");
                }
            }
            catch (DateTimeParseException e) {
                print("Error: Invalid date format.");
                System.out.println();
                print(prompt);
            }
        }
    }


    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot);
    }

    public BigDecimal calculateLabourCost(BigDecimal area, BigDecimal LabourCostPerSquareFoot) {
        return area.multiply(LabourCostPerSquareFoot);
    }


    public String readCustomerNameCanBeEmpty(String prompt, String defaultCustomerName) {
        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip();

            String regex = "[a-zA-Z0-9 .,]+"; // + means 1 or more

            if (user_input.isEmpty()) {
                return defaultCustomerName;
            }
            if (!user_input.matches(regex)) {
                print("Error: Customer name can only consist of");
                print("alphanumeric characters, periods and commas.");
            }
            else{
                return user_input;
            }
        }
    }

    public String readCustomerName(String prompt) {
        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip();

            String regex = "[a-zA-Z0-9 .,]+"; // + means 1 or more

            if (user_input.isEmpty()) {
                print("Error: Customer name cannot be empty.");
            }
            if (!user_input.matches(regex)) {
                print("Error: Customer name can only consist of");
                print("alphanumeric characters, periods and commas.");
            }
            else{
                return user_input;
            }
        }
    }

    public BigDecimal readArea(String prompt) {
        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip();

            final String hundred = "100";

            final BigDecimal hundred_big_decimal = new BigDecimal(hundred);

            try{
                BigDecimal area = new BigDecimal(user_input);

                if (area.compareTo(hundred_big_decimal) >= 0) {
                    return area;
                }
                else {
                    print("Error: Minimum order size is " + hundred_big_decimal + "sq ft");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Error: Area is below zero or Number cannot be converted to BigDecimal");
            }

        }
    }


    public BigDecimal readAreaCanBeEmpty(String prompt, BigDecimal defaultArea) {
        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip();

            if (user_input.isEmpty()) {
                return defaultArea;
            }

            final String hundred = "100";

            final BigDecimal hundred_big_decimal = new BigDecimal(hundred);

            try{
                BigDecimal area = new BigDecimal(user_input);

                if (area.compareTo(hundred_big_decimal) >= 0) {
                    return area;
                }
                else {
                    print("Error: Minimum order size is " + hundred_big_decimal + "sq ft");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Error: Area is below zero or Number cannot be converted to BigDecimal");
            }

        }
    }


    public String readUserProductType(String prompt) {

        Map<String, Product> product = productDaoFile.getAllProducts();

        print(prompt);
        listAllProductsAndPricingInformation();

        while(true) {
            String user_input = sc.nextLine().toLowerCase().strip();

            if (product.containsKey(user_input)){
                return user_input;
            }
            else {
                print("Error: Product type not found.");
                listAllProductsAndPricingInformation();
            }
        }
    }

    public String readUserProductTypeCanBeEmpty(String prompt, String defaultProductType) {

        Map<String, Product> product = productDaoFile.getAllProducts();

        print(prompt);
        listAllProductsAndPricingInformation();

        while(true) {
            String user_input = sc.nextLine().toLowerCase().strip();

            if(user_input.isEmpty()) {
                return defaultProductType;
            }

            if (product.containsKey(user_input)){
                return user_input;
            }
            else {
                print("Error: Product type not found.");
                listAllProductsAndPricingInformation();
            }
        }
    }

    public void listAllProductsAndPricingInformation() {
        Map<String, Product> product = productDaoFile.getAllProducts();

        final String header = "ProductType, CostPerSquareFoot, LaborCostPerSquareFoot";
        System.out.println("*** Listing product details ***");
        System.out.println(header);

        for (Map.Entry<String, Product> entry : product.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue().getCostPerSquareFoot() + ", " + entry.getValue().getLabourCostPerSquareFoot());
        }

        System.out.println("*** Finished listing product details ***");
    }

    public String readUserState(String prompt) {
        Map<String, Tax> tax_map = taxDao.getAllTaxes();

        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip().toLowerCase();

            if (tax_map.containsKey(user_input)) {
                return user_input;
            }
            else{
                print("We cannot find any tax for this state or we cannot sell there.");
                print("The only valid states are: ");
                for(Map.Entry<String, Tax> entry : tax_map.entrySet()) {
                    System.out.println(entry.getKey());
                }
            }
        }
    }

    public String readUserStateCanBeEmpty(String prompt, String default_state) {
        Map<String, Tax> tax_map = taxDao.getAllTaxes();

        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip().toLowerCase();

            if (tax_map.containsKey(user_input)) {
                return default_state;
            }
            else{
                print("We cannot find any tax for this state or we cannot sell there.");
                print("The only valid states are: ");
                for(Map.Entry<String, Tax> entry : tax_map.entrySet()) {
                    System.out.println(entry.getKey());
                }
            }
        }
    }



    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal labourCost, BigDecimal taxRate) {

        /**
         *  Tax rates are stored as whole numbers
         */

        BigDecimal material_plus_labour_cost = materialCost.add(labourCost);
        BigDecimal tax_rate_divided_100 = taxRate.divide(new BigDecimal(100));

        return material_plus_labour_cost.multiply(tax_rate_divided_100);
    }

    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal labourCost, BigDecimal tax) {
        return materialCost.add(labourCost).add(tax);
    }


    public static void main(String[] args) {
        UserIOConsoleImpl userIOConsoleImpl = new UserIOConsoleImpl();
        //userIOConsoleImpl.readString("Hi");
        //userIOConsoleImpl.readInt("How are you?", 10, 3);
        //userIOConsoleImpl.readDate("Enter a date in the (dd-mm-yyyy) format: ");
        //userIOConsoleImpl.readUserState("Enter a state: ");
        //userIOConsoleImpl.readArea("Enter a area: ");
        //userIOConsoleImpl.readUserProductType("Enter a product: ");
        //userIOConsoleImpl.readCustomerName("Enter Customer Name");
         BigDecimal ans = userIOConsoleImpl.calculateTax(new BigDecimal(2134.5435), new BigDecimal(1234.213), new BigDecimal(1235423.5432));

        System.out.println(ans);
    }


}

