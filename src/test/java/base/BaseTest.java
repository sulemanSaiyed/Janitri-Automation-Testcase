package base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;  

public class BaseTest {
    protected WebDriver driver;

@BeforeMethod
public void setup() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("profile.default_content_setting_values.notifications", 2); // block notifications
    options.setExperimentalOption("prefs", prefs);

    driver = new ChromeDriver(options);
    driver.manage().window().maximize();
    driver.get("https://dev-dash.janitri.in/");

    // Handle custom notification overlay *before* interacting with page
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    try {
        WebElement overlayText = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[contains(text(), 'please allow') and contains(text(), 'Reload')]")
        ));

        System.out.println("Overlay detected.");

        try {
            WebElement reloadBtn = driver.findElement(By.xpath("//button[contains(text(), 'Reload')]"));
            if (reloadBtn.isDisplayed() && reloadBtn.isEnabled()) {
                System.out.println("Clicking Reload...");
                reloadBtn.click();
                wait.until(ExpectedConditions.invisibilityOf(overlayText));
                System.out.println("Overlay cleared.");
            }
        } catch (Exception e) {
            System.out.println("Reload button not found. Removing overlay...");
        }

        ((JavascriptExecutor) driver).executeScript(
            "document.querySelector('.dialog-login')?.remove();"
        );
        System.out.println("Overlay forcibly removed.");

    } catch (Exception e) {
        System.out.println("No overlay detected.");
    }
}

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
