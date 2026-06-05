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
        coinsPage.toggleVolumeOrderingDesc();
        coinsPage.toggleVolumeOrderingAsc();
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        Assert.assertTrue(coinsPage.checkVolumeCapColumnOrderAsc(100));
    }
    
    @Test
    public void verifyVolumeOrderingDes() {
        coinsPage.toggleVolumeOrderingDesc();
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        Assert.assertTrue(coinsPage.checkVolumeCapColumnOrderDes(100));
    }

    @Test
    public void verifyMarketCapAsc() {
        coinsPage.toggleMarketCapOrderingDesc();
        coinsPage.toggleMarketCapOrderingAsc();
        Assert.assertEquals(coinsPage.loadTheTable(), 101);
        Assert.assertTrue(coinsPage.checkMarketCapColumnOrderAsc(100));
    }

    @Test
    public void goToTheSecondPage() {
        coinsPage.goNextPage();
        Assert.assertEquals(driver.getCurrentUrl(), BASE_URL + getPath() + "/?page=2");
        coinsPage.loadTheTable();
        Assert.assertTrue(coinsPage.verifyRank(101, 200));
    }

    @Test
    public void changeRows() {
        int newRows = 200;

        coinsPage.changeNumberOfRows(newRows);
        //there is an extra 
        Assert.assertEquals(coinsPage.loadTheTable(), newRows + 1);
    }

    @Test
    public void filteringRangeMarketCap() {
        long min = 100000000;
        long max = 150000000;
        coinsPage.setMarketCapRange(min, max);
        coinsPage.loadTheTable();
        Assert.assertTrue(coinsPage.verifyMarketCapRange(min, max));
    }
}
