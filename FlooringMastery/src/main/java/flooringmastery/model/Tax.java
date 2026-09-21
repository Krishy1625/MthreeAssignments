package flooringmastery.model;

import java.math.BigDecimal;

public class Tax {
    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    //getters
    public String getStateAbbreviation() {return stateAbbreviation;}
    public String getStateName() {return stateName;}
    public BigDecimal getTaxRate() {return taxRate;}

    public void setStateAbbreviation(String stateAbbreviation){
        this.stateAbbreviation=stateAbbreviation;
    }
    public void setStateName(String stateName){
        this.stateName=stateName;
    }
    public void setTaxRate(BigDecimal taxRate){
        this.taxRate=taxRate;
    }

}

