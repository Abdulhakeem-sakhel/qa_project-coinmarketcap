package abd.coinmarketcap;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchComponent {
	private By searchBoxBy = new By.ByCssSelector("div.search-input-static");
	private By searchInputBy = new By.ByCssSelector("input.search-input");
	private By searchResultsCoinNamesBy = new By.ByCssSelector("div.SearchCryptoRow_item-name__OwkC9 > span:first-child");
	private By searchResultsCoinSymbolBy = new By.ByCssSelector("div.SearchCryptoRow_item-symbol__gYcb1");
	private By searchCategoryCypherAssetsBy = new By.ByXPath("//div[@data-role='chip-content-item' and contains(text(), 'Cryptoassets')]");

	@SuppressWarnings("unused")
	private WebDriver driver;
    private WebDriverWait wait;

	public SearchComponent(WebDriver driver) {
		this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	public void openSearch() {
		wait.until(ExpectedConditions.elementToBeClickable(searchBoxBy)).click();
	}

	public void writeInSearchInput(String keyword) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputBy)).sendKeys(keyword);
	}

	public void selectFirstSearchResults() {
		List <WebElement> names =wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsCoinNamesBy));
		names.get(0).click();
	}

	public boolean verifySearchResults(String keyword) {
		List<WebElement> coinNamesElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsCoinNamesBy));
		List<WebElement> coinSymbolElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsCoinSymbolBy));
		
		for (int rowIndex = 0; rowIndex < coinNamesElements.size(); rowIndex++) {
			String coinName = coinNamesElements.get(rowIndex).getText();
			String coinSymbol = coinSymbolElements.get(rowIndex).getText();

			if(	!coinName.toLowerCase().contains(keyword) && 
				!coinSymbol.toLowerCase().contains(keyword)	) 
				{
					return false;
				}
		}
		
		return true;
	}

	public boolean verifyCypherAssetAppears() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(searchCategoryCypherAssetsBy));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
