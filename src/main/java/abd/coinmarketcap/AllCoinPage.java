package abd.coinmarketcap;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllCoinPage {
    // any column cell have a unique class even in a different layout
    private static final String MARKET_CAP_CELLS_CSS = "span.sc-11478e5d-1";
    private static final String VOLUME_CELLS_CSS = "div.sc-4c05d6ef-0.sc-8dd8fbb5-0 p.fOLOxZ";
    private static final String RANK_CELLS_CSS = "p.sc-71024e3e-0.biekbf";

    private By marketCapCellsBy = By.cssSelector(MARKET_CAP_CELLS_CSS);

    private By volumeSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Volume')]");
    private By marketSortingToggleBy = new By.ByXPath("//p[contains(@class, 'llNEXf') and contains(text(), 'Market Cap')]");
    private By activeSortingBy = new By.ByCssSelector("span[data-active='true']");

    private By paginationListBy = new By.ByCssSelector("div.sc-4c05d6ef-0 ul.pagination");
    private By paginationNextButtonBy = new By.ByCssSelector("div.sc-4c05d6ef-0 ul.pagination li.next");

    private By numberOfRowsButtonPopup = new By.ByCssSelector("div[data-role='select-trigger']");
    private By getRowSelect(int row) {
        return new By.ByXPath(String.format("//div[@data-role='pp-item']//div[contains(text(), '%s')]", row));
    }
    private By openFilterButtonBy = new By.ByXPath("//span[contains(text(), 'Filters')]");
    private By maxRageInputMarketCapBy = new By.ByXPath("//div[contains(@class, 'Form_label__8XTvj') and contains(text(), 'Market Cap')]/..//input[contains(@placeholder, 'Max')]");
    private By minRageInputMarketCapBy = new By.ByXPath("//div[contains(@class, 'Form_label__8XTvj') and contains(text(), 'Market Cap')]/..//input[contains(@placeholder, 'Min')]");
    private By applyFilterButtonBy = new By.ByXPath("//div[contains(text(), 'Apply')]");

    private static final int STABLE_ROUNDS = 6;

    private static final double ORDER_TOLERANCE = 0.10;

    private WebDriver driver;
    private WebDriverWait wait;

    public AllCoinPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int loadTheTable() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait growthWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        wait.until(ExpectedConditions.presenceOfElementLocated(marketCapCellsBy));

        int stableRounds = 0;
        while (stableRounds < STABLE_ROUNDS) {
            final int before = driver.findElements(marketCapCellsBy).size();
            // scroll gradually so each lazy-load batch is triggered as it enters the viewport
            js.executeScript("window.scrollBy(0, document.documentElement.clientHeight * 0.7);");
            try {
                growthWait.until(driver -> driver.findElements(marketCapCellsBy).size() > before);
                stableRounds = 0; // new rows arrived, keep going
            } catch (Exception e) {
                stableRounds++;
            }
        }
        js.executeScript("window.scrollTo(0, 0);");
        return driver.findElements(marketCapCellsBy).size();
    }

    public void toggleVolumeOrderingAsc() {
        driver.findElement(volumeSortingToggleBy).click();
        WebElement arrowIndentor = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSortingBy));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "asc"));
    }

    public void toggleVolumeOrderingDesc() {
        driver.findElement(volumeSortingToggleBy).click();
        WebElement arrowIndentor = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSortingBy));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "desc"));
    }

    public void toggleMarketCapOrderingAsc() {
        driver.findElement(marketSortingToggleBy).click();
        WebElement arrowIndentor = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSortingBy));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "asc"));
    }

    public void toggleMarketCapOrderingDesc() {
        driver.findElement(marketSortingToggleBy).click();
        WebElement arrowIndentor = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSortingBy));
        wait.until(ExpectedConditions.attributeContains(arrowIndentor, "data-direction", "desc"));
    }

    private boolean isColumnOrdered(String css, int numberOfRows, boolean descending) {
        List<Long> values = snapshotColumnValues(css);

        // the first row can be an advertisement; skip it when present
        int start = (values.size() == numberOfRows + 1) ? 1 : 0;

        for (int cellIndex = start; cellIndex < values.size() - 1; cellIndex++) {
            long current = values.get(cellIndex);
            long next = values.get(cellIndex + 1);
            if (descending && next > current + (long) (current * ORDER_TOLERANCE)) {
                return false;
            }
            if (!descending && next < current - (long) (current * ORDER_TOLERANCE)) {
                return false;
            }
        }
        return true;
    }

    private List<Long> snapshotColumnValues(String css) {
        // this will take the hole column number in one go
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<String> texts = (List<String>) js.executeScript(
                "return Array.from(document.querySelectorAll(arguments[0]), e => e.textContent);", css);

        List<Long> values = new ArrayList<>(texts.size());
        for (String text : texts) {
            String cleaned = text.replaceAll("[^0-9]", ""); // keep digits only (values are integer dollar amounts)
            if (!cleaned.isEmpty()) {
                values.add(Long.parseLong(cleaned));
            }
        }
        return values;
    }

    public boolean checkMarketCapColumnOrderDes(int numberOfRows) {
        return isColumnOrdered(MARKET_CAP_CELLS_CSS, numberOfRows, true);
    }

    public boolean checkMarketCapColumnOrderAsc(int numberOfRows) {
        return isColumnOrdered(MARKET_CAP_CELLS_CSS, numberOfRows, false);
    }
    public boolean checkVolumeCapColumnOrderDes(int numberOfRows) {
        return isColumnOrdered(VOLUME_CELLS_CSS, numberOfRows, true);
    }

    public boolean checkVolumeCapColumnOrderAsc(int numberOfRows) {
        return isColumnOrdered(VOLUME_CELLS_CSS, numberOfRows, false);
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public boolean verifyRank(int start, int end) {
        List<Long> rankList = snapshotColumnValues(RANK_CELLS_CSS);
        for (long rank : rankList) {
            if (rank < start || rank > end) {
                return false;
            }
        }
        return true;
    }
    public void goNextPage() {
        scrollIntoView(driver.findElement(paginationListBy));
        wait.until(ExpectedConditions.visibilityOfElementLocated(paginationNextButtonBy)).click();
    }

    public void setMarketCapRange(long min, long max) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(openFilterButtonBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(minRageInputMarketCapBy)).sendKeys(String.valueOf(min));
        wait.until(ExpectedConditions.visibilityOfElementLocated(maxRageInputMarketCapBy)).sendKeys(String.valueOf(max));
        wait.until(ExpectedConditions.visibilityOfElementLocated(applyFilterButtonBy)).click();
    }

    public boolean verifyMarketCapRange(long min, long max) {
        List<Long> marketCapValues = snapshotColumnValues(MARKET_CAP_CELLS_CSS);
        for (long marketCapValue : marketCapValues) {
            if (marketCapValue > max || marketCapValue < min) {
                return false;
            }
        }
        return true;
    }

    public void changeNumberOfRows(int rows) {
        // keep a handle on a current row so we can detect when the table reloads
        WebElement existingRow = driver.findElement(marketCapCellsBy);

        WebElement rowsButton = wait.until(ExpectedConditions.presenceOfElementLocated(numberOfRowsButtonPopup));
        scrollIntoView(rowsButton);
        wait.until(ExpectedConditions.elementToBeClickable(rowsButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(getRowSelect(rows))).click();

        // changing the page size reloads the table; wait until the old rows are gone
        wait.until(ExpectedConditions.stalenessOf(existingRow));
    }
}
