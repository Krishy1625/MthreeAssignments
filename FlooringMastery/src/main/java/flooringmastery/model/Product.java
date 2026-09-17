package flooringmastery.model;

import java.math.BigDecimal;

public class Product {
    private String product_type;
    private BigDecimal cost_per_square_foot;
    private BigDecimal labour_cost_per_square_foot;

    //getters
    public String getProductType() {return product_type;}
    public BigDecimal getCostPerSquareFoot() {return cost_per_square_foot;}
    public BigDecimal getLabourCostPerSquareFoot() {return labour_cost_per_square_foot;}
}
