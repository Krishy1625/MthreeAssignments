package flooringmastery.dao;

import flooringmastery.model.Product;
import flooringmastery.view.UserIOConsoleImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDao {

    private static final String PRODUCT_FILE= "src/main/resources/SampleFileData/Data/Products.txt";
    private static final String DELIMITER = ",";

    Map<String, Product> product_name_map_product_object = new HashMap<>();

    @Override
    public void loadFile() {
        File product_file = new File(PRODUCT_FILE);

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

                product_name_map_product_object.put(product_split[0].toLowerCase(), product);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Product file not found: " + PRODUCT_FILE);
        }
    }

    public void listAllProductsAndPricingInformation() {
        loadFile();

        final String header = "ProductType, CostPerSquareFoot, LaborCostPerSquareFoot";
        System.out.println("*** Listing product details ***");
        System.out.println(header);

        for (Map.Entry<String, Product> entry : product_name_map_product_object.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue().getCostPerSquareFoot() + ", " + entry.getValue().getLabourCostPerSquareFoot());
        }

        System.out.println("*** Finished listing product details ***");
    }


    public Map<String, Product> getAllProducts() {
        loadFile();
        return product_name_map_product_object;
    }

    public static void main(String[] args) {
        ProductDaoFileImpl productDaoFileImpl = new ProductDaoFileImpl();
        productDaoFileImpl.listAllProductsAndPricingInformation();
    }
}
