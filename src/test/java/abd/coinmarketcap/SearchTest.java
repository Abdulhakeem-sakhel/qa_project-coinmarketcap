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

import abd.coinmarketcap.util.RandomStringUtil;

public class SearchTest extends BaseTest {

    SearchComponent searchCom = null;
    CoinDetailPage coinPage = null;
    
    private String resolvePlaceholder(String raw) {
        if (raw == null) {
            return "";
        }
        switch (raw.trim()) {
            case "{WHITE_SPACE}":
                return " Bitcoin ";
            case "{LONG_STR}":
                return RandomStringUtil.randomString(256);
            default:
                return raw;
        }
    }

    @Override
    protected String getPath() {
        // the searching is a component that exist in most pages
        return "/";
    }

    @BeforeClass
    public void initPages() {
        searchCom = new SearchComponent(driver);
        coinPage = new CoinDetailPage(driver);
    }

    @DataProvider(name = "searchDataProvider")
    public Object[][] getSearchData() throws IOException {
        List<Object[]> data = new ArrayList<>();
        String csvFile = "src/test/resources/search_ddt_data.csv";
        String line;

        try (BufferedReader bufferReader = new BufferedReader(new FileReader(csvFile))) {
            bufferReader.readLine(); // skip header
            while ((line = bufferReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] row = line.split(",", -1);

                data.add(new Object[]{
                    row[0].trim(), // Test case ID
                    resolvePlaceholder(row[1].trim()), // Key word
                    row[2].trim(), // coin name
                    row[3].trim(), // CoinSymbol
                    row[4].trim(), // ExpectedBehavior
                    row[5].trim(), // Description
                });
            }
        }
        return data.toArray(new Object[0][]);
    }

    @Test (dataProvider = "searchDataProvider")
    public void executeConverterTest(
        String testCaseId,
        String keyword,
        String coinName,
        String coinSymbol,
        String expectedBehavior,
        String description
    ) {
        System.out.println("Running test " + testCaseId + " - " + description);
        
        searchCom.openSearch();
        searchCom.writeInSearchInput(keyword);

        switch (expectedBehavior) {
            case "SELECT_FIRST_VERIFY_COIN":      
                searchCom.selectFirstSearchResults();
                
                Assert.assertTrue(coinPage.verifyCoinUrl(coinName), "The url is for another coin which is " + driver.getCurrentUrl());
                Assert.assertTrue(coinPage.verifyCoinName(coinName), "The name of the coin is not there");
                Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
                Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
                break;
            case "VERIFY_RESULTS_CONTAIN":
                Assert.assertTrue(searchCom.verifySearchResults(keyword));
                break;
            
            case "NO_RESULTS":
                Assert.assertFalse(searchCom.verifyCypherAssetAppears());
                break;
            default:
                break;
        }

    }
}
