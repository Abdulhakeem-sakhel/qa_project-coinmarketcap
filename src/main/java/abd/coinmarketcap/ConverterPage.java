package abd.coinmarketcap;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConverterPage {
    private By converterFromInputBy = new By.ByCssSelector("div.cmc_converter__controls input[data-sensors-click=\"true\"]");

    private By fromCoinInputBy = new By.ByCssSelector("div.cmc-select__from input");
    private By fromCoinDropDownToggleBy = new By.ByCssSelector("div.cmc-select__from div.cmc-select__dropdown-indicator");
    private By fromCoinSelectIndicatorBy = new By.ByCssSelector("div.cmc-select__from div.cmc-select__control");

    private By toCoinInputBy = new By.ByCssSelector("div.cmc-select__to input");
    private By toCoinDropDownToggleBy = new By.ByCssSelector("div.cmc-select__to div.cmc-select__dropdown-indicator");
    private By toCoinSelectIndicatorBy = new By.ByCssSelector("div.cmc-select__to div.cmc-select__control");

    private By loadingIconBy = new By.ByCssSelector("div.cmc-converter__text svg");
    private By resultsNumberBy = new By.ByCssSelector("em.cmc-converter__conversion-result");

    private By swapButtonBy = new By.ByCssSelector("button[data-qa-id='swap-currencies']");
    private By fromConvertTextBy = new By.ByCssSelector("div.converter__text-row > div:nth-child(1)");
    private By toConvertTextBy = new By.ByCssSelector("div.converter__text-row > div:nth-child(3)");

    private WebDriver driver;
    private WebDriverWait wait;

    public ConverterPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void setFromCurrency(String currencyName) {
        wait.until(ExpectedConditions.elementToBeClickable(fromCoinDropDownToggleBy)).click();
        wait.until(ExpectedConditions.attributeContains(fromCoinSelectIndicatorBy, "class","cmc-select__control--menu-is-open"));
        wait.until(ExpectedConditions.elementToBeClickable(fromCoinInputBy)).sendKeys(currencyName + Keys.ENTER);
    }
    
    public void setToCurrency(String currencyName) {
        wait.until(ExpectedConditions.elementToBeClickable(toCoinDropDownToggleBy)).click();
        wait.until(ExpectedConditions.attributeContains(toCoinSelectIndicatorBy, "class","cmc-select__control--menu-is-open"));
        wait.until(ExpectedConditions.elementToBeClickable(toCoinInputBy)).sendKeys(currencyName + Keys.ENTER);
    }

    public void setAmount(float amount) {
        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(converterFromInputBy));
        amountInput.sendKeys(Keys.DELETE);
        amountInput.sendKeys(String.valueOf(amount));
    }

    public boolean verifyCalculationNumberIsDisplayed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementWithText(loadingIconBy, "The loading icon is still on"));
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(resultsNumberBy));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void swapCurrencies() {
        wait.until(ExpectedConditions.elementToBeClickable(swapButtonBy)).click();
    }

    public boolean verifyCurrency(String fromCurrency, String toCurrency) {
        String fromText = wait.until(ExpectedConditions.visibilityOfElementLocated(fromConvertTextBy)).getText();
        String toText = wait.until(ExpectedConditions.visibilityOfElementLocated(toConvertTextBy)).getText();
        System.out.println(fromText);
        System.out.println(toText);
        return fromText.toLowerCase().contains(fromCurrency.toLowerCase()) &&
                toText.toLowerCase().contains(toCurrency.toLowerCase());
    }
}
