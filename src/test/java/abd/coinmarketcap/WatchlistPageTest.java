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

    @BeforeMethod(onlyForGroups = "loggedIn")
    public void login() {
        doLogin();
    }

    private void doLogin() {
        loginComponent.loginFromUI("abodhakeemfabl@gmail.com", "Ab548220-*");
        Assert.assertTrue(loginComponent.isLogin());
    }

    
    @Test (priority = 1, groups = "loggedIn")
    public void addCoin() {
        String coinName = "Ethereum";
        watchlistPage.addCoinToWhishList(coinName);
        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
    }

    @Test (priority = 2, groups = "loggedIn")
    public void deleteCoin() {
        String coinName = "Ethereum";
        watchlistPage.deleteCoin(coinName);
        Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName));
    }

    @Test (priority = 3, groups = "loggedIn")
    public void theCoinWatchListIsPersists() {
        String coinName = "Ethereum";
        watchlistPage.addCoinToWhishList(coinName);
        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);

        this.tearDown();
        this.setUp();
        this.initPages();
        this.doLogin();

        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
        watchlistPage.deleteCoin(coinName);
        Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName));
    }

    @Test (priority = 4, groups = "loggedIn")
    public void addMultipleCoin() {
        String[] coinNames = {"Bitcoin", "Ethereum", "Solana"};
        for (String coinName: coinNames) {
            watchlistPage.addCoinToWhishList(coinName);
            Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
        }
    }

    @Test (priority = 5, groups = "loggedIn")
    public void deleteAllTest() {
        String[] coinNames = {"Bitcoin", "Ethereum", "Solana"};
        for (String coinName: coinNames) {
            watchlistPage.deleteCoin(coinName);
            Assert.assertTrue(watchlistPage.verifyDeleteMessage(coinName), "failed to delete :" + coinName);
        }
        Assert.assertTrue(watchlistPage.verifyEmptyWatchListMessage());
    }

    @Test (priority = 6) 
    public void addCoinLogout() {
        String coinName = "BNB";
        watchlistPage.addCoinToWhishList(coinName);
        Assert.assertTrue(watchlistPage.verifyLogoutPromptMessage(), "the prompt didn't appear");
        watchlistPage.closeLogoutPromptMessage();
        Assert.assertNotEquals(watchlistPage.getCoinRowIndex(coinName), -1);
    }
}
