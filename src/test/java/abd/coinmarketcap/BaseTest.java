package abd.coinmarketcap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected static final String BASE_URL = "https://coinmarketcap.com";

    protected WebDriver driver;

    protected abstract String getPath();

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL + getPath());
    }

     @BeforeMethod
    public void startClean() {
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + getPath());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}