package flooringmastery.dao;

import flooringmastery.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {

    @Test
    void loadsProductsAndSkipsHeader(@TempDir Path tmp) throws IOException {
        Path file = tmp.resolve("Products.txt");
        Files.writeString(file,
                "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
                        "Carpet,2.25,2.10\n" +
                        "Tile,3.50,4.15\n");

        ProductDaoFileImpl dao = new ProductDaoFileImpl(file.toString());
        Map<String, Product> products = dao.getAllProducts();

        assertEquals(2, products.size());

        Product carpet = products.get("carpet");
        assertEquals("carpet", carpet.getProductType());
        assertEquals(new BigDecimal("2.25"), carpet.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), carpet.getLabourCostPerSquareFoot());
    }

    @Test
    void missingFileLeavesMapEmpty() {
        ProductDaoFileImpl dao = new ProductDaoFileImpl("this/file/does/not/exist.txt");
        dao.loadFile();                     // should not cause issuws
        assertTrue(dao.getAllProducts().isEmpty());
    }

    @Test
    void emptyFileLeavesMapEmpty(@TempDir Path tmp) throws IOException {
        Path file = tmp.resolve("empty.txt");
        Files.createFile(file);

        ProductDaoFileImpl dao = new ProductDaoFileImpl(file.toString());
        dao.loadFile();

        assertTrue(dao.getAllProducts().isEmpty());
    }
}