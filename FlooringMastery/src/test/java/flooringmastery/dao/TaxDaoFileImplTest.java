package flooringmastery.dao;

import flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    private TaxDaoFileImpl dao;

    @BeforeEach
    void setUp() {
        dao = new TaxDaoFileImpl();
    }

    @Test
    void testGetAllTaxesReturnsNonNullMap() {
        Map<String, Tax> taxes = dao.getAllTaxes();
        assertNotNull(taxes);
        assertFalse(taxes.isEmpty(), "Tax map should not be empty");
    }

    @Test
    void testHeaderLineIsSkipped() {
        // If the header row ("State,StateName,TaxRate") weren't skipped,
        // a malformed entry with key "statename" would appear.
        Map<String, Tax> taxes = dao.getAllTaxes();
        assertFalse(taxes.containsKey("statename"), "Header row should not become a map entry");
        assertFalse(taxes.containsKey("state"));
    }

    @Test
    void testKeysAreLowercaseStateNames() {
        Map<String, Tax> taxes = dao.getAllTaxes();
        for (String key : taxes.keySet()) {
            assertEquals(key.toLowerCase(), key, "Map keys should be lowercase state names");
        }
    }

    @Test
    void testKnownStateIsParsedCorrectly() {
        Map<String, Tax> taxMap = dao.getAllTaxes();

        Tax tx = taxMap.get("texas");
        assertNotNull(tx, "Texas should be present in the tax map");
        assertEquals("TX", tx.getStateAbbreviation());
        assertEquals("Texas", tx.getStateName());
        assertEquals(0, tx.getTaxRate().compareTo(new BigDecimal("4.45")), "Texas tax rate should be 4.45");
    }

    @Test
    void testAllTaxRatesAreNonNullAndNonNegative() {
        Map<String, Tax> taxes = dao.getAllTaxes();
        for (Tax t : taxes.values()) {
            assertNotNull(t.getTaxRate(), "Tax rate for " + t.getStateName() + " was null");
            assertTrue(t.getTaxRate().compareTo(BigDecimal.ZERO) >= 0, "Tax rate for " + t.getStateName() + " was negative");
        }
    }

    @Test
    void testReloadDoesntDuplicateEntries() {
        Map<String, Tax> first  = dao.getAllTaxes();
        int firstSize = first.size();
        Map<String, Tax> second = dao.getAllTaxes();
        assertEquals(firstSize, second.size(), "Calling getAllTaxes() twice should not duplicate entries");
    }

    @Test
    void testInvalidTaxFile() {
        TaxDaoFileImpl dao = new TaxDaoFileImpl("tax/file/invalid.txt");
        assertDoesNotThrow(dao::loadFile);
        assertTrue(dao.getAllTaxes().isEmpty());
    }

    @Test
    void testEmptyFileLeavesMapEmpty(@TempDir Path tmp) throws IOException {
        Path empty = tmp.resolve("Taxes.txt");
        Files.createFile(empty);   // exists, but zero lines

        TaxDaoFileImpl dao = new TaxDaoFileImpl(empty.toString());
        assertDoesNotThrow(dao::loadFile);
        assertTrue(dao.getAllTaxes().isEmpty());
    }
}