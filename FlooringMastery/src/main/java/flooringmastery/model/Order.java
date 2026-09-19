package flooringmastery.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private int orderNumber;
    private String customerName;
    private String state;
    private Date orderDate;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal labourCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal area;
    private BigDecimal labourCost;
    private BigDecimal tax;
    private BigDecimal total;


    //Getters
    public int getOrderNumber() {return orderNumber;}
    public String getCustomerName() {return customerName;}
    public String getState() {return state;}
    public Date getOrderDate() {return orderDate;}
    public BigDecimal getTaxRate() {return taxRate;}
    public String getProductType() {return productType;}
    public BigDecimal getCostPerSquareFoot() {return costPerSquareFoot;}
    public BigDecimal getLabourCostPerSquareFoot() { return labourCostPerSquareFoot;}
    public BigDecimal getMaterialCost() {return materialCost;}
    public BigDecimal getArea() {return area;}
    public BigDecimal getLabourCost() {return labourCost;}
    public BigDecimal getTax() {return tax;}
    public BigDecimal getTotal() {return total;}

    //Setters
    public void setOrderNumber(int orderNumber) {this.orderNumber = orderNumber;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public void setState(String state) {this.state = state;}
    public void setOrderDate(Date orderDate) {this.orderDate = orderDate;}
    public void setTaxRate(BigDecimal taxRate) {this.taxRate = taxRate;}
    public void setProductType(String productType) {this.productType = productType;}
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot){this.costPerSquareFoot = costPerSquareFoot;}
    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot){this.labourCostPerSquareFoot = labourCostPerSquareFoot;}
    public void setMaterialCost(BigDecimal materialCost) {this.materialCost = materialCost;}
    public void setArea(BigDecimal area) {this.area = area;}
    public void setLabourCost(BigDecimal labourCost) {this.labourCost = labourCost;}
    public void setTax(BigDecimal tax) {this.tax = tax;}
    public void setTotal(BigDecimal total) {this.total = total;}

    // got a bit annoyed so made some overloaded setters with strings to big decimals
    public void setTaxRate(String taxRate) {this.taxRate = new BigDecimal(taxRate);}
    public void setTotal(String total) {this.total = new BigDecimal(total);}
    public void setCostPerSquareFoot(String costPerSquareFoot){this.costPerSquareFoot = new BigDecimal(costPerSquareFoot);}
    public void setLabourCostPerSquareFoot(String labourCostPerSquareFoot){this.labourCostPerSquareFoot = new BigDecimal(labourCostPerSquareFoot);}
    public void setMaterialCost(String materialCost){this.materialCost = new BigDecimal(materialCost);}
    public void setArea(String area){this.area = new BigDecimal(area);}
    public void setLabourCost(String labourCost){this.labourCost = new BigDecimal(labourCost);}
    public void setTax(String tax){this.tax = new BigDecimal(tax);}
    public void setOrderNumber(String orderNumber){this.orderNumber = Integer.parseInt(orderNumber);}


}

