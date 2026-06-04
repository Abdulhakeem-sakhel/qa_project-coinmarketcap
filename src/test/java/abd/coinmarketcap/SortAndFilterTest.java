package abd.coinmarketcap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SortAndFilterTest extends BaseTest {

    AllCoinPage coinsPage = null;

    @Override
    protected String getPath() {
        return "/coins";
    }

    @BeforeClass
    public void initPage() {
        coinsPage = new AllCoinPage(driver);
    }

    @Test
    public void verifyTheDefaultTableOrdering() {
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        Assert.assertTrue(coinsPage.checkMarketCapColumnOrderDes(100));
    }
    
}
