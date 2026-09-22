package flooringmastery.dao;

import flooringmastery.model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDao {

    private static final String PRODUCT_FILE= "src/main/resources/SampleFileData/Data/Products.txt";
    private static final String DELIMITER = ",";

    private final String productFile;
    Map<String, Product> productNameMapProductObject = new HashMap<>();


    public ProductDaoFileImpl() {
        this(PRODUCT_FILE);
    }

    // for testing
    public ProductDaoFileImpl(String productFile) {
        this.productFile = productFile;
    }


    @Override
    public void loadFile() {
        File product_file = new File(productFile);

        try(Scanner sc = new Scanner(product_file)){

            if(sc.hasNextLine()){
                sc.nextLine(); // get rid of the headers
            }

            while (sc.hasNextLine()) {

                String[] product_split =  sc.nextLine().trim().split(DELIMITER);
                Product product = new Product();

                product.setProductType(product_split[0].toLowerCase());

                BigDecimal cost_per_square_foot = new  BigDecimal(product_split[1]);
                product.setCostPerSquareFoot(cost_per_square_foot);

                BigDecimal labour_cost_per_square_foot = new BigDecimal(product_split[2]);
                product.setLabourCostPerSquareFoot(labour_cost_per_square_foot);

                productNameMapProductObject.put(product_split[0].toLowerCase(), product);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Product file not found: " + productFile);
        }
    }

    @Override
    public Map<String, Product> getAllProducts() {
        loadFile();
        return productNameMapProductObject;
    }
}