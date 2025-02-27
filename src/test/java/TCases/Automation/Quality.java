package TCases.Automation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Quality {
	private RemoteWebDriver driver;

	@BeforeTest
	public void setup() throws MalformedURLException {
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		URL url = new URL("http://172.20.23.92:4444/wd/hub");
		driver = new RemoteWebDriver(url, dc);
	}
	@Test(priority = 1)
	public void Login() throws InterruptedException {
		driver.get("http://apollo2.humanbrain.in/");
		driver.manage().window().maximize();
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("The server is Opened sucessfully");
		WebDriverWait wait = new WebDriverWait(driver, 50);
		// WebElement viewerSectionLink = wait
		// 		.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/viewer/assets/images/colorsvg/gallery.svg']")));
		// viewerSectionLink.click();
		// System.out.println("--------------------------*****************-----------------------");
		// System.out.println("The Viewer Icon is clicked");
		String parentWindow = driver.getWindowHandle();
		WebDriverWait wait1 = new WebDriverWait(driver, 20);
		WebElement login = wait1
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=' Log In ']")));
		login.click();
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("The login Button is clicked");
		Thread.sleep(4000);
		Set<String> allWindows = driver.getWindowHandles();
		for (String window : allWindows) {
			if (!window.equals(parentWindow)) {
				driver.switchTo().window(window);
				break;
			}
		}
		Thread.sleep(4000);
		WebDriverWait wait2 = new WebDriverWait(driver, 20);
		WebElement emailInput = wait2
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='email']")));
		emailInput.sendKeys("softwaretestingteam9@gmail.com");
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("Mail I'd is entered");
		WebDriverWait wait3 = new WebDriverWait(driver, 20);
		WebElement Next = wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
		Next.click();
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("The Next Button is clicked");
		WebDriverWait wait4 = new WebDriverWait(driver, 20);
		System.out.println("--------------------------*****************-----------------------");
		WebElement PasswordInput = wait4
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
		PasswordInput.sendKeys("Health#123");
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("Password is entered");
		WebDriverWait wait5 = new WebDriverWait(driver, 20);
		WebElement Next2 = wait5.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
		Next2.click();
		System.out.println("--------------------------*****************-----------------------");
		System.out.println("The Next Button is clicked");
		Thread.sleep(5000);
		driver.switchTo().window(parentWindow);
		Thread.sleep(5000);
	}
	  @Test(priority = 2)
	    public void table() throws InterruptedException {
	        String parentWindow = driver.getWindowHandle();
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement dashb = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/viewer/assets/images/colorsvg/cellreports.svg']")));
	            dashb.click();
	            Thread.sleep(3000);
	            System.out.println("The Dashboard click Successfully");
	        } catch (Exception e) {
	            System.err.println("The Dashboard not Entered clciked: " + e.getMessage());
	        }

	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement quality = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Quality']")));
	            quality.click();
	            Thread.sleep(3000);
	            System.out.println("The quality is clicked");
	        } catch (Exception e) {
	            System.err.println("The quality is not clicked: " + e.getMessage());
	        }
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement list = wait.until(ExpectedConditions.elementToBeClickable(
	            	    By.xpath("(//select[@class='form-control form-control-sm ng-untouched ng-pristine ng-valid'])[1]")
	            	));
	            list.click();
	            Thread.sleep(3000);
	            System.out.println("The Options click Successfully");
	        } catch (Exception e) {
	            System.err.println("The Options not Entered clciked: " + e.getMessage());
	        }
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement Mouse = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()=' Mouse ']")));
	            Mouse.click();
	            Thread.sleep(3000);
	            System.out.println("The Mouse click Successfully");
	        } catch (Exception e) {
	            System.err.println("The Mouse not  clciked: " + e.getMessage());
	        }
	        
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement orgtype = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='form-control form-control-sm ng-valid ng-touched ng-dirty']")));
	            orgtype.click();
	            Thread.sleep(3000);
	            System.out.println("The Organ Choosen Successfully");
	        } catch (Exception e) {
	            System.err.println("The Organ is  not  Choosen: " + e.getMessage());
	        }	            Thread.sleep(3000);

	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 30);
	            WebElement typ = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='Brain']")));
	            typ.click();
	            Thread.sleep(3000);
	            System.out.println("The Brain Choosen Successfully");
	        } catch (Exception e) {
	            System.err.println("The Brain is  not  Choosen: " + e.getMessage());
	        }
//	        try {
//	            WebDriverWait wait = new WebDriverWait(driver, 30);
//	            WebElement orgName = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//select[@class='form-control form-control-sm ng-valid ng-touched ng-dirty'])[3]")));
//	            orgName.click();
//	            Thread.sleep(3000);
//	            System.out.println("The Organ Name Successfully");
//	        } catch (Exception e) {
//	            System.err.println("The Organ name is  not  Choosen: " + e.getMessage());
//	        }
//	        try {
//	            WebDriverWait wait = new WebDriverWait(driver, 30);
//	            WebElement name1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='MTB 10']")));
//	            name1.click();
//	            Thread.sleep(3000);
//	            System.out.println("The MTB10 Choosen Successfully");
//	        } catch (Exception e) {
//	            System.err.println("The MTB10 is  not  Choosen: " + e.getMessage());
//	        }
//	        try {
//	            WebDriverWait wait = new WebDriverWait(driver, 30);
//	            WebElement orgName = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//select[@class='form-control form-control-sm ng-untouched ng-pristine ng-valid'])[2]")));
//	            orgName.click();
//	            Thread.sleep(3000);
//	            System.out.println("The Organ Name Successfully");
//	        } catch (Exception e) {
//	            System.err.println("The Organ name is  not  Choosen: " + e.getMessage());
//	        } try {
//	            WebDriverWait wait = new WebDriverWait(driver, 30);
//	            WebElement sereistype = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='form-control form-control-sm ng-pristine ng-valid ng-touched']")));
//	            sereistype.click();
//	            Thread.sleep(3000);
//	            System.out.println("The sereistype  Successfully");
//	        } catch (Exception e) {
//	            System.err.println("The sereistype is  not  Choosen: " + e.getMessage());
//	        }
	        } 
	  
@AfterTest
public void tearDown() {
    if (driver != null) {
        driver.quit();
        System.out.println("Browser closed successfully");
    }}}
