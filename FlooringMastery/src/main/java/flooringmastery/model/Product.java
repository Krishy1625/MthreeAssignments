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

    public void setProductType(String product_type) {this.product_type = product_type;}
    public void setCostPerSquareFoot(BigDecimal cost_per_square_foot) {
        this.cost_per_square_foot = cost_per_square_foot;
    }
    public void setLabourCostPerSquareFoot(BigDecimal labour_cost_per_square_foot) {
        this.labour_cost_per_square_foot = labour_cost_per_square_foot;
    }
}
