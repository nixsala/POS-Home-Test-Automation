import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;

public class POSTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.out.println("Setting up WebDriver and initializing the browser.");
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    }

    @Test(priority = 1)
    public void testBuyInsuranceAndVerifyNewTab() {
        System.out.println("Navigating to the POS Malaysia website.");
        driver.get("https://www.pos.com.my/");

        System.out.println("Waiting for the 'track-banner-container' section to be visible.");
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'track-banner-container')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);

        System.out.println("Clicking on the 'Buy Insurance' button.");
        WebElement buyInsuranceButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'quick-access-item-title') and contains(text(), 'Buy')]")));

        try {
            buyInsuranceButton.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("ElementClickInterceptedException occurred: " + e.getMessage());
            System.out.println("Handling the popup or overlay.");
            WebElement closePopup = driver.findElement(By.xpath("//img[@src='https://d38eiojdjahdit.cloudfront.net/Pop_Up_Banner_851e8179c1.jpg']"));
            closePopup.click(); // close the popup
            buyInsuranceButton.click(); // try clicking again
        }

        System.out.println("Switching to the new tab.");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); // Switch to the new tab

        String expectedUrl = "https://insurance.pos.com.my/";
        String actualUrl = driver.getCurrentUrl();
        System.out.println("Verifying the URL of the new tab.");
        Assert.assertEquals(actualUrl, expectedUrl);
    }

    @Test(priority = 2)
    public void testVehicleTypeSelectionAndForm() {
        System.out.println("Navigating to the specific insurance page.");
        driver.get("https://insurance.pos.com.my/");

        System.out.println("Waiting for the 'I drive a car' button to be clickable.");
        WebElement driveCarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(), 'I drive a car')]")));

        System.out.println("Clicking the 'I drive a car' button.");
        driveCarButton.click();

        System.out.println("Waiting for the 'Ok, let's get to know you' section to be visible.");
        WebElement getToKnowYouSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), \"Ok, let's get to know you\")]")));

        Assert.assertTrue(getToKnowYouSection.isDisplayed(), "The 'Ok, let's get to know you' section did not appear after clicking 'I drive a car'.");
        System.out.println("'Ok, let's get to know you' section is displayed.");

        System.out.println("Verifying the presence of the five different fields.");
        WebElement vehicleRegNumber = driver.findElement(By.name("regNo"));
        WebElement postcode = driver.findElement(By.name("postcode"));
        WebElement idTypeDropdown = driver.findElement(By.xpath("//select[@id='floatingInput']"));

        Select selectIdType = new Select(idTypeDropdown);
        WebElement idNumber = driver.findElement(By.name("newIc"));
        WebElement mobileNumber = driver.findElement(By.name("mobileNumber"));

        Assert.assertTrue(vehicleRegNumber.isDisplayed(), "Vehicle Registration Number field is not displayed.");
        Assert.assertTrue(postcode.isDisplayed(), "Postcode field is not displayed.");
        Assert.assertTrue(idTypeDropdown.isDisplayed(), "ID Type field is not displayed.");
        Assert.assertTrue(idNumber.isDisplayed(), "ID Number field is not displayed.");
        Assert.assertTrue(mobileNumber.isDisplayed(), "Mobile Number field is not displayed.");
        System.out.println("All required fields are displayed as expected.");
    }

    @Test(priority = 3)
    public void testCreateEConsignmentNoteLink() {
        System.out.println("Navigating to the POS Malaysia website.");
        driver.get("https://www.pos.com.my/");

        // Ensure the page is fully loaded
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
        );

//remove pop up
try {
            WebElement closePopup = driver.findElement(By.cssSelector("your-overlay-css"));
            if (closePopup.isDisplayed()) {
                closePopup.click();
            }
        } catch (Exception e) {
            System.out.println("No overlay found.");
        }

        // Attempt to find and click the 'Send' menu
        System.out.println("Attempting to find and click the 'Send' menu.");
        WebElement sendMenu = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Send')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sendMenu);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendMenu); // JavaScript click

        WebElement parcelOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Parcel')]")));
        parcelOption.click();

        System.out.println("Scrolling down to the 'Create shipment now' button under the Cash section.");
        WebElement createShipmentButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Create Shipment Now')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", createShipmentButton);

        System.out.println("Verifying that the 'Create shipment now' button is displayed.");
        Assert.assertTrue(createShipmentButton.isDisplayed(), "The 'Create shipment now' button is not visible.");
    }


    @Test(priority = 4)

    public void testEConsignmentNoteForm() {
        System.out.println("Navigating to the e-Consignment Note page.");
        driver.get("https://send.pos.com.my/home/e-connote?lg=en");

        // Verify the URL
        String expectedUrl = "https://send.pos.com.my/home/e-connote?lg=en";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "The URL does not match the expected e-Consignment Note URL.");

        // Verify "Sender Info" section
        WebElement senderInfoSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@formgroupname='senderAddress' and contains(@class, 'form-holder')]")));
        Assert.assertTrue(senderInfoSection.isDisplayed(), "Sender Info section is not displayed.");

        // Verify "Receiver Info" section
        WebElement receiverInfoSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@formgroupname='receiverAddress' and contains(@class, 'form-holder')]")));
        Assert.assertTrue(receiverInfoSection.isDisplayed(), "Receiver Info section is not displayed.");

        // Verify "Parcel Info" section
        WebElement parcelInfoSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@formgroupname='parcelDetails' and contains(@class, 'form-holder')]")));
        Assert.assertTrue(parcelInfoSection.isDisplayed(), "Parcel Info section is not displayed.");

        // Verify "Summary" section
        WebElement summarySection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tab-title' and text()='Summary']")));
        Assert.assertTrue(summarySection.isDisplayed(), "Summary section is not displayed.");

        System.out.println("All sections are correctly displayed on the e-Consignment Note page.");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Closing the browser.");
        if (driver != null) {
            driver.quit();
        }
    }
}
