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
	
	By operator_onwards=By.xpath("//div[@id='filtersOnward']/div[2]/div[3]/div/span/span");
	By operators_List=(By.xpath("//*[@id=\"filtersOnward\"]/div[2]/div[3]/div[3]/form/ul/li"));
	By operators_return_List=(By.xpath("//*[@id=\"filtersReturn\"]/div[2]/div[3]/div[3]/form/ul/li"));
	
	By operators_returns=By.xpath("//div[@id='filtersReturn']/div[2]/div[3]/div/span/span");
	By selectSeat_link=By.xpath("//span[contains(text(),'Select Seat')]");
	By selectAPSRTC_service=By.xpath("//span[@id='ShowBtnHideAPSRTC1']");
	By selectAPSRTC_service_2=By.xpath("//span[@id='ShowBtnHideAPSRTC1'])[2]");
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
	
	public void click_operator_onwards()
	{
		driver.findElement(operator_onwards).click();
	}
	
	public void click_operator_return()
	{
		driver.findElement(operators_returns).click();
	}
	
	public void continuetopayment_click()
	{
		driver.findElement(continueToPayment_button).click();
	}
	
	public String getTotalfare()
	{
		return driver.findElement(total_fare).getText();
	}
	
	public void book_return_ticket(HashMap<String,String> ticketDetails)
	{
		try
		{
		click_operator_return();
		if(ticketDetails.containsKey("busOperator"))
		{
			select_operator(operators_return_List,ticketDetails.get("busOperator"));
			if(ticketDetails.get("busOperator").equals("APSRTC"))
			{
				System.out.println("Entered till hear");
				selectAPSRTC(selectAPSRTC_service);
			}
		}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		selectseatlink_click();
		if(ticketDetails.containsKey("dropingPoint"))
			select_drop();
		showLayout_click();
		selectseat();
		
	}
	
	
	
	
	/**
	 * Method Name:book_ticket
	 * Description : will book the ticket with the user specified details 
	 * user should specify bus operator and boarding point
	 * @param ticketDetails
	 */
	
	public void book_ticket(HashMap<String,String> ticketDetails)
	{
		click_operator_onwards();
		if(ticketDetails.containsKey("busOperator"))
		{
			select_operator(operators_List,ticketDetails.get("busOperator"));
			//If operator is APSRTC you need to click on this extra
			if(ticketDetails.get("busOperator").equals("APSRTC"))
			{
				selectAPSRTC(selectAPSRTC_service);
			}
		}
		
		selectseatlink_click();
		if(ticketDetails.containsKey("boardingPoint"))
			select_boarding();
		showLayout_click();
		selectseat();
		
	}
	
	
	
	
	
	
	
	/**
	 * Method Name:select_operator
	 * Description: select user specified operator
	 * 
	 * */
	public void select_operator(By element,String operatorName)
	{
		Boolean result=false;
		List<WebElement> operators=driver.findElements(element);
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
	
	public void selectAPSRTC(By Element)
	{
		if(driver.findElement(Element).isDisplayed())
		driver.findElement(Element).click();
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
