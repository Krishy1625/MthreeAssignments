package flooringmastery.view;

import flooringmastery.dao.ProductDaoFileImpl;
import flooringmastery.dao.TaxDao;
import flooringmastery.dao.TaxDaoFileImpl;
import flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

    TaxDaoFileImpl taxDao = new TaxDaoFileImpl();
    private final Scanner sc = new Scanner(System.in);
    ProductDaoFileImpl productDao = new ProductDaoFileImpl();

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

    public BigDecimal LabourCost(BigDecimal area, BigDecimal LabourCostPerSquareFoot) {
        return area.multiply(LabourCostPerSquareFoot);
    }


    public String readCustomerName(String prompt) {
        print(prompt);

        while(true) {
            String user_input = sc.nextLine().strip();

            String regex = "[a-zA-Z0-9 .,]+"; // + means 1 or more

            if (user_input.equals("")) {
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



//    public BigDecimal Tax(BigDecimal materialCost, BigDecimal labourCost, BigDecimal taxRate) {
//
//
//        /**
//         *  DO: Tax rates are stored as whole numbers
//         */
//        BigDecimal material_plus_labour_cost = materialCost.add(labourCost);
//        BigDecimal tax_rate_divided_100 = taxRate.divide(new BigDecimal(100));
//
//        return material_plus_labour_cost.multiply(tax_rate_divided_100);
//    }
//
//    public BigDecimal Total(BigDecimal materialCost, BigDecimal labourCost) {
//
//    }


    public static void main(String[] args) {
        UserIOConsoleImpl userIOConsoleImpl = new UserIOConsoleImpl();
        //userIOConsoleImpl.readString("Hi");
        //userIOConsoleImpl.readInt("How are you?", 10, 3);
        //userIOConsoleImpl.readDate("Enter a date in the (dd-mm-yyyy) format: ");
        userIOConsoleImpl.readUserState("Enter a state: ");
    }


}

