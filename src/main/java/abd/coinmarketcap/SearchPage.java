package abd.coinmarketcap;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
	private By searchBoxBy = new By.ByCssSelector("div.search-input-static");
	private By searchInputBy = new By.ByCssSelector("input.search-input");
	private By searchResultsCoinNamesBy = new By.ByCssSelector("div.SearchCryptoRow_item-name__OwkC9 > span:first-child");


	private WebDriver driver;
    private WebDriverWait wait;

	public SearchPage(WebDriver driver) {
		this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	public void openSearch() {
		wait.until(ExpectedConditions.elementToBeClickable(searchBoxBy)).click();
	}

	public void writeInSearchInput(String keyword) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputBy)).sendKeys(keyword);
	}

	public boolean verifySearchResults(String keyword) {
		List<WebElement> coinNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsCoinNamesBy));
		for (WebElement coinName : coinNames ) {
			if (!coinName.getText().contains(keyword)) {
				return false;
			}
		}
		return true;
	}
}
