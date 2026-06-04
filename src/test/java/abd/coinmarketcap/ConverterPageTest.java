package abd.coinmarketcap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConverterPageTest extends BaseTest {

    ConverterPage converterPage = null;

    @Override
    protected String getPath() {
        return "/converter";
        
    } 

    @BeforeClass
    public void initPage() {
        converterPage = new ConverterPage(driver);
    }
    
    @DataProvider(name = "converterDataProvider")
    public Object[][] getConverterData() throws IOException {
        List<Object[]> data = new ArrayList<>();
        String csvFile = "src/test/resources/converter_ddt_data.csv";
        String line;

        try (BufferedReader bufferReader = new BufferedReader(new FileReader(csvFile))) {
            bufferReader.readLine(); // skip header
            while ((line = bufferReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] row = line.split(",", -1);

                data.add(new Object[]{
                    row[0].trim(), //Test case ID
                    row[1].trim(), //From Currency
                    row[2].trim(), //to Currency
                    row[3].trim(), //amount
                    row[4].trim(), // ExpectedBehavior
                    row[5].trim(), // Description
                });
            }
        }
        return data.toArray(new Object[0][]);
    }

    @Test (dataProvider = "converterDataProvider")
    public void executeConverterTest(
        String testCaseId,
        String fromCurrency,
        String toCurrency,
        String amount,
        String expectedBehavior,
        String description
    ) {
        System.out.println("Running test " + testCaseId + " - " + description);
        converterPage.setFromCurrency(fromCurrency);
        converterPage.setToCurrency(toCurrency);
        converterPage.setAmount(amount);

        switch (expectedBehavior) {
            case "NUMERIC_RESULT":
                Assert.assertTrue(converterPage.verifyCalculationNumberIsDisplayed());
                Assert.assertTrue(converterPage.verifyCurrency(fromCurrency, toCurrency));
                Assert.assertTrue(converterPage.verifyAmount(amount));
                break;
            case "SWAP_UPDATES":
                converterPage.swapCurrencies();
                Assert.assertTrue(converterPage.verifyCalculationNumberIsDisplayed());
                Assert.assertTrue(converterPage.verifyCurrency(toCurrency, fromCurrency));
                Assert.assertTrue(converterPage.verifyAmount(amount));
                break;
            case "REJECT_INPUT":
                Assert.assertTrue(converterPage.verifyCalculationNumberIsDisplayed());
                Assert.assertTrue(converterPage.verifyCurrency(fromCurrency, toCurrency));
                Assert.assertTrue(converterPage.verifyAmount("1")); // that the default value left
                break;
            default:
                break;
        }
    }


}
