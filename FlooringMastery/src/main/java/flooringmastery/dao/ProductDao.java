package flooringmastery.dao;

import flooringmastery.model.Product;

import java.util.Map;

public interface ProductDao {
    void loadFile();
    Map<String, Product> getAllProducts();
}