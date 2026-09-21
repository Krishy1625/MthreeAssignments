package flooringmastery.model;

import java.math.BigDecimal;

public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal labourCostPerSquareFoot;

    //getters
    public String getProductType() {return productType;}
    public BigDecimal getCostPerSquareFoot() {return costPerSquareFoot;}
    public BigDecimal getLabourCostPerSquareFoot() {return labourCostPerSquareFoot;}

    public void setProductType(String productType) {this.productType = productType;}
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }
    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }
}
