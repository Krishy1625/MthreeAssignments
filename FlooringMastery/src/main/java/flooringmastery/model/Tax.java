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
}

