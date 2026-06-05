package abd.coinmarketcap;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllCoinPage {
    // any column cell have a unique class even in a different layout
    private By marketCapCellsBy = new By.ByCssSelector("span.sc-11478e5d-1");
    private By volumeCellsBy = new By.ByCssSelector("div.sc-4c05d6ef-0.sc-8dd8fbb5-0 p.fOLOxZ");

    private By marketCapSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Market Cap')]");
    private By volumeSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Volume')]");
    private By activeSortingBy = new By.ByCssSelector("span[data-active='true']");

    private WebDriver driver;
    private WebDriverWait wait;

    private Long parseNumber(WebElement cell) {

        String cleaned = cell.getText().replaceAll("[^0-9.]", ""); // keep digits and "."
        return Long.parseLong(cleaned);
    }

    public AllCoinPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int loadTheTable() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (true) {
            final int before = driver.findElements(marketCapCellsBy).size();
            List<WebElement> rows = driver.findElements(marketCapCellsBy);
            js.executeScript("arguments[0].scrollIntoView(true);", rows.get(rows.size() - 1));
            try {
                wait.until(driver -> driver.findElements(marketCapCellsBy).size() > before);
            } catch (Exception e) {
                break;
            }
        }
        js.executeScript("window.scrollTo(0, 0);");
        return driver.findElements(marketCapCellsBy).size();
    }

    public boolean checkMarketCapColumnOrderDes(int numberOfRows) {
        List<WebElement> marketCapCells = driver.findElements(marketCapCellsBy);
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
            long leftValue = parseNumber(marketCapCells.get(leftIndex));
            long rightValue = parseNumber(marketCapCells.get(rightIndex));
            if (rightValue > leftValue) {
                return false;
            }
            leftIndex++;
            rightIndex++;
        }
        return true;
    }

    // ordering = desc or asc
    public void toggleVolumeOrderingAsc() {
        WebElement arrowIndentor = driver.findElement(new By.ByCssSelector("span[data-active=\"true\"]"));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "desc"));
        driver.findElement(volumeSortingToggleBy).click();
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "asc"));
    }

    public void toggleVolumeOrderingDesc() {
        driver.findElement(volumeSortingToggleBy).click();
        WebElement arrowIndentor = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSortingBy));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "desc"));
    }

    public boolean checkVolumeCapColumnOrderDes(int numberOfRows) {
        List<WebElement> priceCells = driver.findElements(volumeCellsBy);
        int leftIndex = -1;
        int rightIndex = -1;

        if (priceCells.size() == numberOfRows + 1) {
            leftIndex = 1; // the first index should be an advertisement skip it
            rightIndex = 2;
        } else {
            leftIndex = 0;
            rightIndex = 1;
        }

        while (leftIndex < priceCells.size() - 1) {
            long leftValue = parseNumber(priceCells.get(leftIndex));
            long rightValue = parseNumber(priceCells.get(rightIndex));
            if (rightValue > leftValue ) {
                return false;
            }
            leftIndex++;
            rightIndex++;
        }
        return true;    
    }

    public boolean checkVolumeCapColumnOrderAsc(int numberOfRows) {
        List<WebElement> priceCells = driver.findElements(volumeCellsBy);
        int leftIndex = -1;
        int rightIndex = -1;

        if (priceCells.size() == numberOfRows + 1) {
            leftIndex = 1; // the first index should be an advertisement skip it
            rightIndex = 2;
        } else {
            leftIndex = 0;
            rightIndex = 1;
        }

        while (leftIndex < priceCells.size() - 1) {
            long leftValue = parseNumber(priceCells.get(leftIndex));
            long rightValue = parseNumber(priceCells.get(rightIndex));
            if (rightValue < leftValue) {
                return false;
            }
            leftIndex++;
            rightIndex++;
        }
        return true;    
    }
}
