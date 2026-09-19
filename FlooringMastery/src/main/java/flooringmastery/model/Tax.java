package flooringmastery.model;

import java.math.BigDecimal;

public class Tax {
    private String state_abbreviation;
    private String state_name;
    private BigDecimal tax_rate;

    //getters
    public String getStateAbbreviation() {return state_abbreviation;}
    public String getStateName() {return state_name;}
    public BigDecimal getTaxRate() {return tax_rate;}

    public void setStateAbbreviation(String state_abbreviation){
        this.state_abbreviation=state_abbreviation;
    }
    public void setStateName(String state_name){
        this.state_name=state_name;
    }
    public void setTaxRate(BigDecimal tax_rate){
        this.tax_rate=tax_rate;
    }
}

