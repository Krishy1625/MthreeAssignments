package flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {
    void print(String message);
    String readString(String prompt);
    int readInt(String prompt, int min, int max);
    LocalDate readDate(String s);
    String readCustomerName(String s);
    LocalDate readDateAfterToday(String s);
    String readUserState(String s);
    String readUserProductType(String s);
    BigDecimal readArea(String s);
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calculateLabourCost(BigDecimal area, BigDecimal labourCostPerSquareFoot);
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal labourCost, BigDecimal taxRate);
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal labourCost, BigDecimal calculatedTax);
    String readCustomerNameCanBeEmpty(String s, String customerName);
    String readUserStateCanBeEmpty(String s, String state);
    String readUserProductTypeCanBeEmpty(String s, String productType);
    BigDecimal readAreaCanBeEmpty(String s, BigDecimal area);
}
