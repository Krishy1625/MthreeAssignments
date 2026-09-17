package flooringmastery.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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
            return -1;
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




    public static void main(String[] args) {
        UserIOConsoleImpl userIOConsoleImpl = new UserIOConsoleImpl();
        //userIOConsoleImpl.readString("Hi");
        //userIOConsoleImpl.readInt("How are you?", 10, 30);
        userIOConsoleImpl.readDate("Enter a date in the (dd-mm-yyyy) format: ");
    }
}

