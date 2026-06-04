package abd.coinmarketcap;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CoinDetailPage {
    private By coinNameBy = By.cssSelector("span[data-role=\"coin-name\"]");
    private By coinSymbolBy = By.cssSelector("span[data-role=\"coin-symbol\"]");
    // for this we need to be careful since it keep displaying new price
    private By coinLivePriceDisplay = By.cssSelector("span[data-test=\"text-cdp-price-display\"]");


    @SuppressWarnings("unused")
    private WebDriver driver;
    private WebDriverWait wait;

	public CoinDetailPage(WebDriver driver) {
		this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

    public boolean verifyCoinName(String coinName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(coinNameBy)).getText().equals(coinName);
    }

    public boolean verifyCoinSymbol(String CoinSymbol) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(coinSymbolBy)).getText().equals(CoinSymbol);
    }

    public boolean verifyCoinUrl(String coinName) {
        return wait.until(ExpectedConditions.urlContains("/currencies/" + coinName.toLowerCase()));
    }

    public boolean verifyLivePriceIsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(coinLivePriceDisplay)).isDisplayed();
    }
}
