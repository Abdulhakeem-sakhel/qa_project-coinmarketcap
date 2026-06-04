package abd.coinmarketcap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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

    @BeforeMethod
    public void startClean() {
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + getPath());
    }
    
    @Test
    public void convertCryptoToFiat() {
        String fromCurrency = "Bitcoin";
        String toCurrency = "USD";
        float amount = 5;

        converterPage.setFromCurrency(fromCurrency);
        converterPage.setToCurrency(toCurrency);
        converterPage.setAmount(amount);

        Assert.assertTrue(converterPage.verifyCalculationNumberIsDisplayed());
        Assert.assertTrue(converterPage.verifyCurrency(fromCurrency, toCurrency));
    }

    @Test
    public void swappingFromTo() {
        String fromCurrency = "Bitcoin";
        String toCurrency = "USD";
        float amount = 5;

        converterPage.setFromCurrency(fromCurrency);
        converterPage.setToCurrency(toCurrency);
        converterPage.setAmount(amount);
        converterPage.swapCurrencies();
        Assert.assertTrue(converterPage.verifyCalculationNumberIsDisplayed());
        Assert.assertTrue(converterPage.verifyCurrency(toCurrency, fromCurrency));
    }
}
