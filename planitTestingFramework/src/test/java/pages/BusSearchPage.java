package pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class BusSearchPage {
	WebDriver driver;
	
	/*****************objects in bus search page*****************/
	By operator=By.xpath("//span[contains(text(),'Operator')]/child::span");
	By operators_List=(By.xpath("//*[@id=\"filtersOnward\"]/div[2]/div[3]/div[3]/form/ul/li"));
	By selectSeat_link=By.xpath("//span[contains(text(),'Select Seat')]");
	By selectAPSRTC_service=By.xpath("//*[@id=\"ShowBtnHideAPSRTC1\"]");
	By boardingpoint_dropdown=By.id("pickup_id1");
	By dropingpoint_dropdown=By.id("drop_id2");
	By showlayout_button=By.id("btnEnable11");
	By bookReturn_button=By.id("btnEnable1");
	By empty_seat=By.xpath("//div[contains(@class,'seats')]/ul/li[contains(@class,'seat available')]/a");
	By total_fare=By.id("totalfare");
	By continueToPayment_button=By.xpath("//input[@id='btnEnable1'][@value='Continue to Payment ']");
	
	/************************************************/
	//Constructor
	public BusSearchPage(WebDriver driver)
	{
		this.driver=driver;
	}
	
	public void click_operator()
	{
		driver.findElement(operator).click();
	}
	
	public void continuetopayment_click()
	{
		driver.findElement(continueToPayment_button).click();
	}
	
	public String getTotalfare()
	{
		return driver.findElement(total_fare).getText();
	}
	/**
	 * Method Name:book_ticket
	 * Description : will book the ticket with the user specified details 
	 * user should specify bus operator and boarding point
	 * @param ticketDetails
	 */
	
	public void book_ticket(HashMap<String,String> ticketDetails)
	{
		click_operator();
		if(ticketDetails.containsKey("busOperator"))
		{
			select_operator(ticketDetails.get("busOperator"));
			if(ticketDetails.get("busOperator").equals("APSRTC"))
			{
				selectAPSRTC();
			}
		}
		
		selectseatlink_click();
		if(ticketDetails.containsKey("boardingPoint"))
			select_boarding();
		if(ticketDetails.containsKey("dropingPoint"))
			select_drop();
		showLayout_click();
		selectseat();
		
	}
	
	
	
	
	
	
	
	/**
	 * Method Name:select_operator
	 * Description: select user specified operator
	 * 
	 * */
	public void select_operator(String operatorName)
	{
		Boolean result=false;
		List<WebElement> operators=driver.findElements(operators_List);
		for(WebElement ope:operators)
		{
			
			if(ope.getText().equals(operatorName))
			{
				ope.click();
				result=true;
				break;
			}
		}
		//if operator not found select the first operator
		if(!result)
		{
			driver.findElement(By.xpath("//*[@id=\"filtersOnward\"]/div[2]/div[3]/div[3]/form/ul/li[1]/label"));
		}
	}
	
	public void selectAPSRTC()
	{
		if(driver.findElement(selectAPSRTC_service).isDisplayed())
		driver.findElement(selectAPSRTC_service).click();
	}
	
	public void selectseatlink_click()
	{
		driver.findElement(selectSeat_link).click();
	}
	
	public void select_boarding()
	{
		Select boarding=new Select(driver.findElement(boardingpoint_dropdown));
		boarding.selectByIndex(2);
	}
	
	public void showLayout_click()
	{
		driver.findElement(showlayout_button).click();
	}
	
	/**
	 * Name: selectseat
	 * Description : select first empty seat 
	 */
	public void selectseat()
	{
		driver.findElement(empty_seat).click();
	}
	
	public void bookreturn()
	{
		driver.findElement(bookReturn_button).click();
	}
	
	
	public void select_drop()
	{
		Select drop=new Select(driver.findElement(dropingpoint_dropdown));
		drop.selectByIndex(2);
	}

}
