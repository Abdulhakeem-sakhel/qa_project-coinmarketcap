package abd.coinmarketcap;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllCoinPage {
    // any column cell have a unique class even in a different layout
    private By marketCapCells = new By.ByCssSelector("span.sc-11478e5d-1");
    private By priceCells = new By.ByCssSelector("div.sc-631098c-0.ilZTOW");

    private By marketCapSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Market Cap')]");
    private By priceSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Price')]");


    private WebDriver driver;
    private WebDriverWait wait;

    private long parseNumber(String text) {
        String cleaned = text.replaceAll("[^0-9.]", "");  // keep digits and "."
        return Long.parseLong(cleaned);
    }

    public AllCoinPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int loadTheTable() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (true) {
            final int before = driver.findElements(marketCapCells).size();
            List<WebElement> rows = driver.findElements(marketCapCells);
            js.executeScript("arguments[0].scrollIntoView(true);", rows.get(rows.size() - 1));
            try {
                wait.until(driver -> driver.findElements(marketCapCells).size() > before);
            } catch (Exception e) {
                break;
            }
        }
        js.executeScript("window.scrollTo(0, 0);");
        return driver.findElements(marketCapCells).size();
    }

    public boolean checkMarketCapColumnOrderDes(int numberOfRows) {
        List<WebElement> marketCapCells = driver.findElements(marketCapSortingToggleBy);
        int leftIndex = -1;
        int rightIndex = -1;

        if (marketCapCells.size() == numberOfRows + 1) {
            leftIndex = 1; // the first index should be an advertisement skip it
            rightIndex = 2;
        } else {
            leftIndex = 0;
            rightIndex = 1;
        }

        while (leftIndex < marketCapCells.size() - 1) {
            long leftValue = parseNumber(marketCapCells.get(leftIndex).getText());
            long rightValue = parseNumber(marketCapCells.get(rightIndex).getText());
            if (rightValue > leftValue) {
                return false;
            }
        }
        return true;
    }
}
