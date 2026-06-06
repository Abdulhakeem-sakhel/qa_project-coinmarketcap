package abd.coinmarketcap;

import java.rmi.server.ExportException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WatchlistPage {
    private By addCoinButtonBy = new By.ByXPath("//span[contains(text(), 'New Asset')]/ancestor::button");
    private By searchCoinInputBy = new By.ByCssSelector("div.sc-c98001fb-0 input.cmc-input.input-el");
    private By searchResultsBy = new By.ByCssSelector("div.coin-item-list-wrapper");
    private By saveButtonBy = new By.ByXPath("//div[@data-role='btn-content-item' and contains(text(), 'Save')]");
    private By deleteRowButtonBy = new By.ByCssSelector("span.icon-Star-Filled");
    private By coinNameCellsBy = new By.ByCssSelector("p.coin-item-name");
    private By emptyWatchListMessageBy = new By.ByXPath("//h3[contains(text(), 'Add Coins to Your Watchlist')]");
    private By logoutPromptExitButtonBy = new By.ByXPath("//div[@data-scope='modal-container']//button[@aria-label='Close']");
    private By logoutPromptMessageBy = new By.ByXPath("//div[@data-scope='modal-container']//span[contains(text(), 'Save your watchlist permanently')]");
    private By coinSymbolCellsBy = new By.ByCssSelector("coin-item-symbol");

    private By getDeleteMessageBy(String coinName) {
        return new By.ByXPath(String.format("//span[@data-role='mi-content-item' and contains(text(), '%s has been removed')]", coinName));
    }

    private WebDriver driver;
    private WebDriverWait wait;

    public WatchlistPage(WebDriver driver ) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void addCoinToWhishList(String coinName) {
        wait.until(ExpectedConditions.elementToBeClickable(addCoinButtonBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchCoinInputBy)).sendKeys(coinName);

        List<WebElement> resultsElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsBy));
        for (WebElement results: resultsElement) {
            if (results.getText().contains(coinName)) {
                results.findElement(new By.ByCssSelector("input")).click();
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonBy)).click();

        wait.until(driver -> driver.findElements(coinNameCellsBy).stream()
                .anyMatch(cell -> cell.getText().contains(coinName)));
    }

    public int getCoinRowIndex(String coinName) {
        try {
            List<WebElement> namesCell = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(coinNameCellsBy));
            for (int rowIndex = 0; rowIndex < namesCell.size(); rowIndex++) {
                WebElement name = namesCell.get(rowIndex);
                if (name.getText().contains(coinName)) {
                    return rowIndex;
                }
            }
        } catch(Exception e) {
            return -1;
        }
        return -1;
    }

    public void deleteCoin(String coinName) {
        int rowIndex = getCoinRowIndex(coinName);
        if (rowIndex == -1) {
            //throw new Exception("didn't find the coin name");
        }
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(deleteRowButtonBy)).get(rowIndex).click();;

    }
    public boolean verifyDeleteMessage(String coinName) {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(getDeleteMessageBy(coinName)));
            wait.until(ExpectedConditions.stalenessOf(message));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyEmptyWatchListMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emptyWatchListMessageBy));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean verifyLogoutPromptMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutPromptMessageBy));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void closeLogoutPromptMessage() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutPromptExitButtonBy)).click();
    }
}
