package com.aqacourses.waits;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class OpenSiteWithWaits {

    // Instances of WebDriver and WenDriverWait
    private WebDriver driver;
    private WebDriverWait wait;

    // Some constants
    private final String URL = "http://www.seleniumframework.com/";
    private final String PYTHON_LINK_XPATH = "//ul[@id='main-nav']//span[.='PYTHON']/..";
    private final String TITLE = "Selenium Framework | Python Course";

    /**
     * Set up method
     */
    @Before
    public void setUp() {
        // If you want to disable infobars please use this code
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption(
                "excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Initialize path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // Initialize instance of ChromeDriver and add options
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        // Implicitly waits
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  // Waits for elements
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);  // Waits for full page load. This string will cause fail of test
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS); // Waits for script load

        // Explicitly wait
        wait = new WebDriverWait(driver, 10);
        wait.ignoring(TimeoutException.class)
                .withMessage("OMG Where the element?1")   // Print this message
                .withTimeout(Duration.ofSeconds(10))      // timeout
                .pollingEvery(Duration.ofSeconds(2));     // Tries each second
    }

    /**
     * Open site and wait with different conditions
     */
    @Test
    public void testOpenSiteWithWaits() {
        driver.get(URL);

        // Wait for element to be clickable and for text in the element
        wait.until(ExpectedConditions.and(
                ExpectedConditions.elementToBeClickable(By.xpath(PYTHON_LINK_XPATH)),
                ExpectedConditions.textToBe(By.xpath(PYTHON_LINK_XPATH), "PYTHON")));

        // Wait that no alert
        wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));

        // Wait for visibility of element and click on element
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(PYTHON_LINK_XPATH)))).click();

        // Custom wait using lambda
        Predicate<WebDriver> isTextInTheElement
                = webDriver -> webDriver.findElement(By.xpath(PYTHON_LINK_XPATH)).getText().equals("PYTHON");

        // Using custom wait
        Assert.assertTrue(isTextInTheElement.test(driver));

        // Compare titles
        Assert.assertEquals("Title is not as expected", TITLE, driver.getTitle());

    }

    /**
     * Makes driver quit
     */
    @After
    public void tearDown() {
        driver.quit();
    }

}
