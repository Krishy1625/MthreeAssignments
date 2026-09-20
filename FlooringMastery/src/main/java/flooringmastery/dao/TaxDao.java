package flooringmastery.dao;

import flooringmastery.model.Tax;

import java.util.Map;

public interface TaxDao {
    void loadFile();
    Map<String, Tax> getAllTaxes();
}
