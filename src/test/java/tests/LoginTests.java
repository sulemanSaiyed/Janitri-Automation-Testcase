package tests;

import base.BaseTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTests extends BaseTest {

    @Test
    public void testLoginPageElementsPresent() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isPageLoaded(), "All login page elements should be visible.");
    }

    @Test
    public void testLoginButtonDisabledWhenFieldsAreEmpty() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Login button should be disabled when fields are empty.");
    }

@Test
public void testPasswordMaskedButton() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.enterPassword("Test123");
    Assert.assertTrue(loginPage.isPasswordMasked(), "Password should be masked initially.");
    loginPage.togglePasswordVisibility();
    Assert.assertFalse(loginPage.isPasswordMasked(), "Password should be unmasked after toggle.");
}

@Test
public void testInvalidLoginShowErrorMsg() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.enterUserId("invalid");
    loginPage.enterPassword("invalid");
    loginPage.clickLogin();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Wait until the error div contains the expected text
    WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.className("invalid-credential-div")
    ));

    // Wait until the text is exactly what we want
    boolean messageAppeared = wait.until(ExpectedConditions.textToBePresentInElement(
        errorDiv, "Invalid Credentials"
    ));

    String errorText = errorDiv.getText().trim();
    System.out.println("Error Message Shown: " + errorText);

    Assert.assertTrue(messageAppeared, "Expected 'Invalid Credentials' error to appear.");
}

}
