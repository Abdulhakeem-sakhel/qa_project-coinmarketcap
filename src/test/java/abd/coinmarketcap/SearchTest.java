package abd.coinmarketcap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

    SearchComponent searchCom = null;
    CoinDetailPage coinPage = null;
    
    @Override
    protected String getPath() {
        // the searching is a component that exist in most pages
        return "/";
    }

    @BeforeClass
    public void initPages() {
        searchCom = new SearchComponent(driver);
        coinPage = new CoinDetailPage(driver);
    }

    @Test
    public void searchForCoinByName() {
        String coinName = "Bitcoin";
        String coinSymbol = "BTC";
        searchCom.openSearch();
        searchCom.writeInSearchInput(coinName);
        searchCom.selectFirstSearchResults();
        
        Assert.assertTrue(coinPage.verifyCoinUrl(coinName), "The url is for another coin which is " + driver.getCurrentUrl());
        Assert.assertTrue(coinPage.verifyCoinName(coinName), "The name of the coin is not there");
        Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
        Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
    }

    @Test
    public void searchForCoinSymbol() {
        String coinName = "Ethereum";
        String coinSymbol = "ETH";
        searchCom.openSearch();
        searchCom.writeInSearchInput(coinSymbol);
        searchCom.selectFirstSearchResults();
        
        Assert.assertTrue(coinPage.verifyCoinUrl(coinName), "The url is for another coin which is " + driver.getCurrentUrl());
        Assert.assertTrue(coinPage.verifyCoinName(coinName), "The name of the coin is not there");
        Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
        Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
    }
    
    @Test 
    public void checkSearchResult() {
        String keyword = "doge";
        
        searchCom.openSearch();
        searchCom.writeInSearchInput(keyword);

        Assert.assertTrue(searchCom.verifySearchResults(keyword));
    }

    @Test
    public void searchCaseInsensitive() {
        String coinName = "bitcoin";
        String coinSymbol = "BTC";
        searchCom.openSearch();
        searchCom.writeInSearchInput(coinName);
        searchCom.selectFirstSearchResults();
        
        Assert.assertTrue(coinPage.verifyCoinUrl(coinName), "The url is for another coin which is " + driver.getCurrentUrl());
        Assert.assertTrue(coinPage.verifyCoinName(coinName), "The name of the coin is not there");
        Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
        Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
    }

    @Test
    public void searchWithWhiteSpace() {
        String coinName = " Bitcoin ";
        String coinSymbol = "BTC";
        searchCom.openSearch();
        searchCom.writeInSearchInput(coinName);
        searchCom.selectFirstSearchResults();
        
        Assert.assertTrue(coinPage.verifyCoinUrl(coinName.trim()), "The url is for another coin which is " + driver.getCurrentUrl());
        Assert.assertTrue(coinPage.verifyCoinName(coinName.trim()), "The name of the coin is not there");
        Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
        Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
    } 

    @Test
    public void searchWithGibberishLetters() {
        String coinName = " Bitcoin ";
        String coinSymbol = "BTC";
        searchCom.openSearch();
        searchCom.writeInSearchInput(coinName);
        searchCom.selectFirstSearchResults();
        
        Assert.assertTrue(coinPage.verifyCoinUrl(coinName.trim()), "The url is for another coin which is " + driver.getCurrentUrl());
        Assert.assertTrue(coinPage.verifyCoinName(coinName.trim()), "The name of the coin is not there");
        Assert.assertTrue(coinPage.verifyCoinSymbol(coinSymbol));
        Assert.assertTrue(coinPage.verifyLivePriceIsDisplayed(), "The price is not displayed");
    }
}
