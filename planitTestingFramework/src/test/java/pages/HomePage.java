/**
 * 
 */
package pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author sudheer
 * @ Description  This page contains home page objects and the actions can be performed on the objects
 */
public class HomePage {
	
	WebDriver driver;
	
	/*********** objects in home page***************/
	 By source_text=By.id("source");
	 By source_list=By.xpath("//ul[@id='ui-id-1']/li");
	 By destination_text=By.id("destination");
	 By destination_list=By.xpath("//ul[@id='ui-id-2']/li");
	 By company_logo=By.xpath("//a[contains(@href,'https://www.abhibus.com/')]/img");
	 String journeydate="datepicker1";
	 String returndate="datepicker2";
	 By searchButton=By.xpath("//*[text()='Search']");
	 
	 /********************************************/
	 //Constructor
	 
	 public HomePage(WebDriver driver)
	 {
		this.driver=driver;
	 }
	
	/**
	 * Method name:autoCompleteText
	 * Description:enter text in auto complete text box
	 * @param locator
	 * @param itemlist
	 * @param value
	 */
	
	public void autoCompleteText(By locator,By itemlist,String value)
	{
		 //waitForElementToAppear(locator);
		 WebElement autoOptions2= driver.findElement(locator);
		 autoOptions2.sendKeys(value);
		 List<WebElement> optionsToSelect2 = driver.findElements(itemlist);
		 for(WebElement option : optionsToSelect2)
		   {
		        if(option.getText().equals(value)) 
		        {
		            
		            option.click();
		            break;
		        }
		    }
	 }
	/**
	 * Method name: datepicker_setDate
	 * Description : select the user specified date
	 * @param id
	 * @param date
	 */
	 
	 public void datepicker_setDate(String id,String date)
	 {
		 JavascriptExecutor js=((JavascriptExecutor)driver);
		 js.executeScript("document.getElementById('"+id+"').value='"+date+"'");
		 
	 }
	 
	 /**
	  * Method Name:busSearch
	  * Description:search for a bus which matched for user criteria
	  * @param searchCriteria
	  */
	 
	 public void busSearch(HashMap<String, String> searchCriteria)
	 {
		 
		 if(searchCriteria.containsKey("source"))
			 autoCompleteText(source_text, source_list, searchCriteria.get("source"));
		 if(searchCriteria.containsKey("destination"))
			 autoCompleteText(destination_text, destination_list, searchCriteria.get("destination"));
		 if(searchCriteria.containsKey("journeydate"))
			 datepicker_setDate(journeydate,searchCriteria.get("journeydate"));
		if(searchCriteria.containsKey("returndate"))
			datepicker_setDate(returndate,searchCriteria.get("returndate"));
		clickSearch();
			
	 }
	 
	 
	 /**
	  * Mehod Name: clickSearch
	  * Description :clicks on search button
	  */
	 
	 public void clickSearch()
	 {
		 driver.findElement(searchButton).click();
	 }
	 
	 public String getCompanyLogo_TooltipText() throws InterruptedException
	 {
		 Actions act=new Actions(driver);
		 WebElement companyLogo=driver.findElement(company_logo);
		 act.moveToElement(companyLogo).build().perform();
		 String tooltipText=companyLogo.getAttribute("title");
		 Thread.sleep(2000);
		 return tooltipText;
	 }

}
