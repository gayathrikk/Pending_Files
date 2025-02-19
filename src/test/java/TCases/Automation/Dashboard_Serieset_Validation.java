package TCases.Automation;
import java.net.MalformedURLException;
import java.net.URL;
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
public class Dashboard_Serieset_Validation {
	private RemoteWebDriver driver;

	@BeforeTest
	public void setup() throws MalformedURLException {
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		URL url = new URL("http://172.20.23.92:4443/wd/hub");
		driver = new RemoteWebDriver(url, dc);
	}

	@Test(priority = 1)
	public void Login() throws InterruptedException {
		driver.get("http://apollo2.humanbrain.in");
		driver.manage().window().maximize();
		System.out.println("The server is Opened sucessfully");
		WebDriverWait wait = new WebDriverWait(driver, 50);
//		WebElement viewerSectionLink = wait
//				.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/viewer/assets/images/colorsvg/gallery.svg']")));
//		viewerSectionLink.click();
//		System.out.println("The Viewer Icon is clicked");
		String parentWindow = driver.getWindowHandle();
		WebDriverWait wait1 = new WebDriverWait(driver, 30);
		WebElement login = wait1
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=' Log In ']")));
		login.click();
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
		System.out.println("Mail I'd is entered");
		WebDriverWait wait3 = new WebDriverWait(driver, 20);
		WebElement Next = wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
		Next.click();
		System.out.println("The Next Button is clicked");
		WebDriverWait wait4 = new WebDriverWait(driver, 20);
		WebElement PasswordInput = wait4
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
		PasswordInput.sendKeys("Health#123");
		System.out.println("Password is entered");
		WebDriverWait wait5 = new WebDriverWait(driver, 20);
		WebElement Next2 = wait5.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
		Next2.click();
		System.out.println("The Next Button is clicked");
		Thread.sleep(5000);
		driver.switchTo().window(parentWindow);
		Thread.sleep(5000);
	}
	
	@Test(priority = 2)
	public void table() throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		try {
			WebDriverWait wait6 = new WebDriverWait(driver, 30);
			WebElement Atlas = wait6.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/viewer/assets/images/colorsvg/cellreports.svg']")));
			Atlas.click();
			Thread.sleep(3000);
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Dashboard Click Successfully");
		} catch (Exception e) {
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Dashboard is not Click");
		}
		try {
			WebDriverWait wait7 = new WebDriverWait(driver, 30);
			WebElement AtlasDashboard = wait7.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Atlas Dashboard']")));
			AtlasDashboard.click();			
			Thread.sleep(3000);
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Atlas Dashboard Click Successfully");
		} catch (Exception e) {
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Atlas Dashboard is not Click");
		}
		try {
			WebDriverWait wait7 = new WebDriverWait(driver, 30);
			WebElement Brainame = wait7.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[text()='Fetal brain 15']")));
			Brainame.click();			
			Thread.sleep(3000);
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Brain Name Fetal Brain 15 Click Successfully");
		} catch (Exception e) {
			System.out.println("--------------------------*****************-----------------------");
			System.out.println("The Brain Name Fetal Brain 15 is not Click");
		}
		Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }}
        try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			WebElement Brainame = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='1672'])[1]")));
			Brainame.click();	
			System.out.println("The Number is Click Successfully");
	            WebElement popupTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='B_201_FB15-SL_558-ST_NISL-SE_1672_thumbnail.jpg']")));
	            System.out.println("--------------------------*****************-----------------------");
			 String actualText = popupTextElement.getText().trim();
			 String expectedText = "B_201_FB15-SL_558-ST_NISL-SE_1672_thumbnail.jpg";
			 System.out.println("Actual Text: [" + actualText + "]");
			Thread.sleep(3000);
			
			
			System.out.println("--------------------------*****************-----------------------");
			 
	
        if (actualText.equals(expectedText)) {
            System.out.println("Validation Passed: The expected text matches the actual text.");
        } else {
            System.err.println("Validation Failed: Expected [" + expectedText + "] but found [" + actualText + "]");
        }
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
        
	}
					@AfterTest
					public void tearDown() {
						if (driver != null) {
							driver.quit();
						}
					}}