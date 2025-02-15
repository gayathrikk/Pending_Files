package TCases.Automation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions; // Import for Actions
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FilterAnnotation1 {
    private RemoteWebDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        URL url = new URL("http://172.20.23.92:4444/wd/hub");  // Update with your Grid URL
        driver = new RemoteWebDriver(url, dc);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(priority = 1)
    public void login() throws InterruptedException {
        driver.get("http://apollo2.humanbrain.in/");
        driver.manage().window().maximize();
        System.out.println("The server is opened successfully");

        WebDriverWait wait = new WebDriverWait(driver, 50);
        try {
            WebElement viewerSectionLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/viewer/assets/images/colorsvg/gallery.svg']")));
            viewerSectionLink.click();
            System.out.println("The Viewer Icon is clicked");

            // Handle login popup
            String parentWindow = driver.getWindowHandle();
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=' Log In ']")));
            loginButton.click();

            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!window.equals(parentWindow)) {
                    driver.switchTo().window(window);
                    break;
                }
            }

            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='email']")));
            emailInput.sendKeys("softwaretestingteam9@gmail.com");

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
            nextButton.click();

            WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
            passwordInput.sendKeys("Health#123");

            WebElement nextButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
            nextButton2.click();

            driver.switchTo().window(parentWindow);
            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("An error occurred during login: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void filterAnnotation() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        try {
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Search tags']")));
            searchBox.sendKeys("222\n");
            Thread.sleep(3000);
            System.out.println("-------------------------------------------------");
            System.out.println("The number is entered successfully");
        } catch (Exception e) {
            System.out.println("--------------------------*****************-----------------------");
            System.out.println("The number is not Entered successfully");
        }

        try {
            WebElement viewerIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nb-icon[@icon='viewer']")));
            viewerIcon.click();
            Thread.sleep(5000);
            System.out.println("The Viewer icon is clicked");
        } catch (Exception e) {
            System.out.println("--------------------------*****************-----------------------");
            System.out.println("The Viewer icon is not clicked");
            Thread.sleep(3000);
        }

        try {
            Actions actions = new Actions(driver);
            WebElement fi = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ngx-app/ngx-annotation/nb-layout/div/div/div/div/div/nb-layout-column/ngx-brain-browser/nb-layout/div/div/div/div/div/nb-layout-column/ngx-process-work-area/nb-card/nb-card-body/nb-layout/div/div/div/nb-sidebar/div/div/nb-menu/ul/li[10]")));
            fi.click();
         //   actions.moveToElement(fi).click().perform();

            System.out.println("--------------------------*****************-----------------------");
            System.out.println("The filter icon is clicked");
        } catch (Exception e) {
            System.out.println("--------------------------*****************-----------------------");
            System.out.println("The filter icon is not clicked");
        }
//        try {
//        	driver.switchTo().frame(0); // Use the correct index if multiple iframes exist
//        	WebElement fi = driver.findElement(By.xpath("//*[@id=\"back-menu-panel\"]/ul/li[10]"));
//        	fi.click();
//        	driver.switchTo().defaultContent(); // Switch back to the main page
//
//
////            WebElement fi = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nb-menu/ul/li[10]/a")));
////            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fi);
//            Thread.sleep(500);
//            
//            Actions actions = new Actions(driver);
//            actions.moveToElement(fi).click().perform();
//            
//            System.out.println("--------------------------*****************-----------------------");
//            System.out.println("The filter icon is clicked");
//        } catch (Exception e) {
//            System.out.println("--------------------------*****************-----------------------");
//            System.out.println("The filter icon is not clicked. Error: " + e.getMessage());
//        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully");
        }
    }
}
