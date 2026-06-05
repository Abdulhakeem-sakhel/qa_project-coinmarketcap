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

    @Test
    public void verifyVolumeOrderingAsc() {
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        coinsPage.toggleVolumeOrderingDesc();
        coinsPage.toggleVolumeOrderingAsc();
        Assert.assertTrue(coinsPage.checkVolumeCapColumnOrderAsc(100));
    }
    
    @Test
    public void verifyVolumeOrderingDes() {
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        coinsPage.toggleVolumeOrderingDesc();
        Assert.assertTrue(coinsPage.checkVolumeCapColumnOrderDes(100));
    }
}
