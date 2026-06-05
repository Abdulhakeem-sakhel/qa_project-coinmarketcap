package abd.coinmarketcap;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WatchlistPage {
    private By addCoinButtonBy = new By.ByXPath("//span[contains(text(), 'New Asset')]");
    private By searchCoinInputBy = new By.ByCssSelector("div.sc-c98001fb-0 input.cmc-input.input-el");
    private By searchResultsBy = new By.ByCssSelector("div.coin-item-list-wrapper");
    private By saveButtonBy = new By.ByXPath("//div[@data-role='btn-content-item' and contains(text(), 'Save')]");
    private By coinNameCellsBy = new By.ByCssSelector("p.coin-item-name");
    private By coinSymbolCellsBy = new By.ByCssSelector("coin-item-symbol");

    private WebDriver driver;
    private WebDriverWait wait;

    public WatchlistPage(WebDriver driver ) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void addCoinToWhishList(String coinName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addCoinButtonBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchCoinInputBy)).sendKeys(coinName);

        List<WebElement> resultsElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsBy));
        for (WebElement results: resultsElement) {
            if (results.getText().contains(coinName)) {
                results.findElement(new By.ByCssSelector("input")).click();
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonBy)).click();
    }
}
