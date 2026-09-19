package flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

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

    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot);
    }

    public BigDecimal LabourCost(BigDecimal area, BigDecimal LabourCostPerSquareFoot) {
        return area.multiply(LabourCostPerSquareFoot);
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
    }
}

