package flooringmastery.dao;

import flooringmastery.model.Tax;

import java.util.List;

public interface TaxDao {
    void loadFile();
    List<Tax> getAllTaxes();
}
