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
        watchlistPage.addCoinToWhishList("ETH");
    }

    @Test
    public void deleteCoin() {
        watchlistPage.deleteCoin("Bitcoin");
    }
}