package abd.coinmarketcap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WatchlistPageTest extends BaseTest {

    LoginComponent loginComponent;
    WatchlistPage watchlistPage;
    @Override
    protected String getPath() {
        return "/?type=coins&tableRankBy=watchlist";
    }

    @BeforeClass
    public void initPages() {
        loginComponent = new LoginComponent(driver);
        watchlistPage = new WatchlistPage(driver);
    }

    @BeforeMethod
    public void login() {
        loginComponent.loginFromUI("abodhakeemfabl@gmail.com", "Ab548220-*");
        Assert.assertTrue(loginComponent.isLogin());
    }

    
    @Test
    public void addCoin() {
        String coinName = "Ethereum";
        watchlistPage.addCoinToWhishList(coinName);
        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
    }

    @Test
    public void deleteCoin() {
        String coinName = "Ethereum";
        watchlistPage.deleteCoin(coinName);
        Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName));
    }

    @Test 
    public void theCoinWatchListIsPersists() {
        String coinName = "Ethereum";
        watchlistPage.addCoinToWhishList(coinName);
        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);

        this.tearDown();
        this.setUp();
        this.initPages();
        this.login();

        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
        watchlistPage.deleteCoin(coinName);
        Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName));
    }

    @Test
    public void addMultipleCoin() {
        String[] coinNames = {"Bitcoin", "Ethereum", "Solana"};
        for (String coinName: coinNames) {
            watchlistPage.addCoinToWhishList(coinName);
            Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
        }
        
        for (String coinName: coinNames) {
            watchlistPage.deleteCoin(coinName);
            Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName), "failed to delete :" + coinName);
        }
    }
}