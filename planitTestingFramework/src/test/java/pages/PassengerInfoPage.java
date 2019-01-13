package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PassengerInfoPage 
{
	
	WebDriver driver;
	
	/**********objects in passenger info page****************/
	
	By netAmount=By.id("NetAmountmsg");
	
	
	
	/*******************************************/
	
	//Constructor
	
	public PassengerInfoPage(WebDriver driver)
	{
		this.driver=driver;
	}
	
	
	public double getTotalAmount()
	{
		
		return Double.parseDouble(driver.findElement(netAmount).getText());
		
	}
	
	
	

}
