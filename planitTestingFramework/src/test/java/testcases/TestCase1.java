package testcases;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import pages.BusSearchPage;
import pages.HomePage;
import pages.PassengerInfoPage;
import utilities.*;

public class TestCase1 {
	
	/*******************Variable declaration****************************/ 
	
	public String source,destination,journeydate,returndate,expectedToolTipText,busOperator;
	public double onwardJourneyfare,returnJourneyfare,totalAmount;
	String path=System.getProperty("user.dir")+"//TestData//";
	ExcelUtil utilities;
	WebDriver driver=null;
	WebDriverWait myWait;
	HomePage homePage;
	BusSearchPage searchPage;
	PassengerInfoPage infoPage;
	ExtentHtmlReporter htmlreporter;
	ExtentReports report;
	ExtentTest test;
	public static final String project_path=System.getProperty("user.dir");
	
	/**************** script************************************************/
	
	@BeforeClass
	@Parameters("browserName")
	public void initialize(String browserName) throws IOException
	{
		htmlreporter =new ExtentHtmlReporter(project_path+"//Result/session.html");
		report=new ExtentReports();
		report.attachReporter(htmlreporter);
		test=report.createTest("Orange HRM","Orange HRM Login flow");
		launch(browserName); //Launch user specified browser
		//Object creation
		homePage=new HomePage(driver);
		searchPage=new BusSearchPage(driver);
		infoPage=new PassengerInfoPage(driver);
		test.log(Status.PASS, "browser launched sucessfully");
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		myWait=new WebDriverWait(driver, 30);
		
	}
	
	@BeforeTest
	
	public void getTestdata() throws IOException
	{
		utilities=new ExcelUtil(path, "TestData.xlsx", "Data");
		source=utilities.getCellData(1, 0);
		destination=utilities.getCellData(1, 1);
		journeydate=utilities.getCellData(1, 2);
		returndate=utilities.getCellData(1, 3);
		expectedToolTipText=utilities.getCellData(1, 4);
		busOperator=utilities.getCellData(1, 5);
	}
	
	
	// Search for buses with user specified criteria
	@Test (priority=1)
	
	public void bus_search()
	
	{
		try
		{
			driver.get("https://www.abhibus.com/");
			myWait.until(ExpectedConditions.titleContains("Online Bus Ticket Booking "));
			String tooltiptext=homePage.getCompanyLogo_TooltipText();
			if(compare(expectedToolTipText, tooltiptext)) //Compare tool tip text
				test.log(Status.PASS, "Tooltip text is displayed");
			else
				test.log(Status.FAIL,"Tooltip text is not displayed");
			HashMap<String ,String> searchCriteria=new HashMap<String, String>();
			searchCriteria.put("source", source);
			searchCriteria.put("destination", destination);
			searchCriteria.put("journeydate", journeydate);
			searchCriteria.put("returndate", returndate);
			homePage.busSearch(searchCriteria);// search for the buses with the specified criteria
			test.log(Status.INFO,"entered all details"+ test.addScreenCaptureFromPath(captureScreen()));
		}
		catch(Exception e)
		{
			test.log(Status.FAIL, ""+e.getMessage());
			teardown();
		}
	}
	
	@Test(priority=2,dependsOnMethods= {"bus_search"})
	
	public void book_onward_journey()
	{
		try
		{
		HashMap<String,String>journeyDetails=new HashMap<String, String>();
		journeyDetails.put("busOperator", busOperator);
		journeyDetails.put("boardingPoint", "2");
		searchPage.book_ticket(journeyDetails);
		onwardJourneyfare=Double.parseDouble(searchPage.getTotalfare());
		test.log(Status.INFO, "onward journey total fare"+onwardJourneyfare);
		System.out.println(onwardJourneyfare);
		}
		catch(Exception e)
		{
			test.log(Status.FAIL, "Error occured while booking onward journey");
			teardown();
		}
	}
	
	
	@Test(priority=3,dependsOnMethods= {"book_onward_journey"})
	
	public void book_return_journey() throws InterruptedException
	{
		try 
		{
		searchPage.bookreturn();
		HashMap<String,String>returnDetails=new HashMap<String, String>();
		returnDetails.put("busOperator", "APSRTC");
		returnDetails.put("dropingPoint", "2");
		searchPage.book_return_ticket(returnDetails);
		returnJourneyfare=Double.parseDouble(searchPage.getTotalfare_1());
		test.log(Status.INFO, "return journey total fare"+returnJourneyfare);
		System.out.println(returnJourneyfare);
		}
		catch (Exception e) {
			test.log(Status.FAIL, "Error occured while booking return ticket"+e.getMessage());
		}
	}
	
	@Test(priority=4,dependsOnMethods= {"book_return_journey"})
	
	public void verify_details_on_paymentpage() throws IOException
	{
		searchPage.continuetopayment_click();
		
		//verify total fare on passenger info page
		
		  totalAmount=infoPage.getTotalAmount();
		  
		  test.log(Status.INFO, " total fare"+totalAmount);
		  
		  System.out.println(totalAmount);
		  
		  if(totalAmount>(onwardJourneyfare+returnJourneyfare))
		  {
			  test.log(Status.FAIL, "Total fare is as expected"+test.addScreenCaptureFromPath(captureScreen()));
			  Assert.fail();
			  teardown();
		  }
		  
		  else 
		  {
			  test.log(Status.PASS, "Total fare is as expected"+test.addScreenCaptureFromPath(captureScreen()));
		  }
	}
	
	
	
	@AfterClass
	
	public void teardown()
	{
		driver.close();
		
		report.flush();
	}

	/***************************common method*******************************************/
	
		public String getcurrentdateandtime()
		{
			String str = null;
			try{
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
			Date date = new Date();
			str= dateFormat.format(date);
			str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");
			}
			catch(Exception e){

			}
			return str;
		}
		/**
		 * Method name:captureScreen
		 * Description:will take screenshot and saves in the image folder
		 * @return
		 * @throws IOException
		 */
		public String captureScreen() throws IOException
		{
			TakesScreenshot screen = (TakesScreenshot) driver;
			File src = screen.getScreenshotAs(OutputType.FILE);
			String dest =project_path+"//images//"+getcurrentdateandtime()+".png";
			File target = new File(dest);
			FileUtils.copyFile(src, target);
			return dest;
		}
		
		/**
		 * Method name:launch
		 * Description :launch user specified browser
		 * @param browserName
		 */
		public void launch(String browserName)
		{
			if(browserName.equalsIgnoreCase("firefox"));
			{
				System.setProperty("webdriver.gecko.driver", project_path+"\\Drivers\\geckodriver.exe");
				driver=new FirefoxDriver();
			}
			if(browserName.equalsIgnoreCase("chrome"))
			{	
				System.setProperty("webdriver.gecko.driver", project_path+"\\Drivers\\chromedriver.exe");
				driver=new ChromeDriver();
			}
			driver.manage().window().maximize();
		}
		/**
		 * MethodName: compare
		 * Description :compares two string if both match return true else false
		 * @param expected
		 * @param actual
		 * @return
		 */
		
		public Boolean compare(String expected,String actual)
		{
			if(actual.equalsIgnoreCase(expected))
				return true;
			else
				return false;
		}


}
