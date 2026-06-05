package abd.coinmarketcap;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginComponent {
    private By openLoginButtonBy = new By.ByCssSelector("button[data-btnname='Log In']");
    private By emailInputBy = new By.ByCssSelector("div[style='display: block;'] input[type='email']");
    private By passwordInputBy = new By.ByCssSelector("div[style='display: block;'] input[type='password']");
    private By loginButtonBy = new By.ByCssSelector("button[data-test='login-btn']");
    private By loginSuccessMsgBy = new By.ByCssSelector("[data-test='login-success-toast']");


    private WebDriver driver;
    private WebDriverWait wait;

    public LoginComponent(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
    }
    public void loginFromUI(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(openLoginButtonBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputBy)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInputBy)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButtonBy)).click();
    }

    public boolean isLogin() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginSuccessMsgBy));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
