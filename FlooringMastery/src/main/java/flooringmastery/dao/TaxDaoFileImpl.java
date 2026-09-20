package flooringmastery.dao;

import flooringmastery.model.Tax;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao {

    private static final String TAX_FILE= "src/main/resources/SampleFileData/Data/Taxes.txt";
    private static final String DELIMITER = ",";

    Map<String, Tax> state_map_taxes = new HashMap<>();

    @Override
    public void loadFile() {

        File tax_file = new File(TAX_FILE);

        try(Scanner sc = new Scanner(tax_file)){

            if(sc.hasNextLine()){
                sc.nextLine(); // get rid of the headers
            }

            while (sc.hasNextLine()) {

                String[] tax =  sc.nextLine().trim().split(DELIMITER);
                Tax tax_obj = new Tax();

                tax_obj.setStateAbbreviation(tax[0]);
                tax_obj.setStateName(tax[1]);
                BigDecimal tax_rate = new BigDecimal(tax[2]); // tax[2] is string for exact calculations with BigD
                tax_obj.setTaxRate(tax_rate);

                state_map_taxes.put(tax[1].toLowerCase(),tax_obj);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Tax file not found: " + TAX_FILE);
        }
    }

    @Override
    public Map<String, Tax> getAllTaxes() {
        loadFile();
        return state_map_taxes;
    }
}
