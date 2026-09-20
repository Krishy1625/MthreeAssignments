package flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {
    void print(String message);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    LocalDate readDate(String s);
    String readCustomerName(String s);
    LocalDate readDateAfterToday(String s);
    String readUserState(String s);
    String readUserProductType(String s);
    BigDecimal readArea(String s);
    String readCustomerNameCanBeEmpty(String s, String customerName);
    String readUserStateCanBeEmpty(String s, String state);
    String readUserProductTypeCanBeEmpty(String s, String productType);
    BigDecimal readAreaCanBeEmpty(String s, BigDecimal area);
}